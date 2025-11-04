---
layout: default.md
title: "Developer Guide"
pageNav: 3
---

# SoCTAssist Developer Guide

## **Acknowledgements**

This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org).

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2526S1-CS2103T-W11-1/tp/blob/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2526S1-CS2103T-W11-1/tp/blob/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2526S1-CS2103T-W11-1/tp/blob/master/src/main/java/seedu/address/MainApp.java) is specified in [`MainWindow.fxml`](https://github.com/AY2526S1-CS2103T-W11-1/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2526S1-CS2103T-W11-1/tp/blob/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user
<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

---
### Add Homework Feature

The add homework feature allows users to assign a homework task to either a specific student or all students. Each homework is identified by an assignment ID.

The sequence diagram below illustrates the interactions within the `Logic` component for adding homework:

<puml src="diagrams/AddHomeworkSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `addhomework` Command" />

<box type="info" seamless>

**Note:** The lifeline for `AddHomeworkCommandParser` should end at the destroy marker (X), but due to a limitation of PlantUML, the lifeline continues till the end of the diagram.

</box>

How the `add_hw` command works:
1. When the user enters an `add_hw` command, `LogicManager` passes it to `AddressBookParser`.
2. `AddressBookParser` creates an `AddHomeworkCommandParser` to parse the command arguments.
3. `AddHomeworkCommandParser` validates and parses the NUSNET ID (or the keyword `all`) and the assignment ID.
4. An `AddHomeworkCommand` object is created and executed.
5. `AddHomeworkCommand` checks if the homework assignment already exists for the specified student(s).
6. If no duplicates are found, the homework is added to the target student(s)’ homework tracker(s).
7. The updated address book is saved to storage.

---

### Delete Homework Feature

The delete homework feature allows users to remove an existing homework assignment from a specific student or from all students.

The sequence diagram below illustrates the interactions within the `Logic` component for deleting homework:

<puml src="diagrams/DeleteHomeworkSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `deletehomework` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteHomeworkCommandParser` should end at the destroy marker (X), but due to a limitation of PlantUML, the lifeline continues till the end of the diagram.

</box>

How the `delete_hw` command works:
1. When the user enters a `delete_hw` command, `LogicManager` passes it to `AddressBookParser`.
2. `AddressBookParser` creates a `DeleteHomeworkCommandParser` to parse the command arguments.
3. `DeleteHomeworkCommandParser` validates and parses the NUSNET ID (or the keyword `all`) and the assignment ID.
4. A `DeleteHomeworkCommand` object is created and executed.
5. `DeleteHomeworkCommand` verifies that the homework exists for the specified student(s).
6. If found, the homework is removed from the respective homework tracker(s).
7. The updated address book is saved to storage.

---

### Mark Homework Feature

The mark homework feature allows users to update the status (e.g., `complete`, `incomplete`, `late`) of a homework assignment for a specific student.

The sequence diagram below illustrates the interactions within the `Logic` component for marking homework:

<puml src="diagrams/MarkHomeworkSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `markhomework` Command" />

<box type="info" seamless>

**Note:** The lifeline for `MarkHomeworkCommandParser` should end at the destroy marker (X), but due to a limitation of PlantUML, the lifeline continues till the end of the diagram.

</box>

How the `mark_hw` command works:
1. When the user enters a `mark_hw` command, `LogicManager` passes it to `AddressBookParser`.
2. `AddressBookParser` creates a `MarkHomeworkCommandParser` to parse the command arguments.
3. `MarkHomeworkCommandParser` validates and parses the NUSNET ID, assignment ID, and status.
4. A `MarkHomeworkCommand` object is created and executed.
5. `MarkHomeworkCommand` checks whether the specified homework exists for the student.
6. If found, the homework’s status is updated to the new value.
7. The updated address book is saved to storage.

--------------------------------------------------------------------------------------------------------------------
### Mark Attendance Feature

The mark attendance feature allows users to mark the attendance status (e.g., `present`, `absent`, `excused`) of a single student in a particular week.

The sequence diagram below illustrates the interactions within the `Logic` component for marking attendance:

<puml src="diagrams/MarkAttendanceSequenceDiagram.puml"  alt="Interactions Inside the Logic Component for the `mark_attendance` Command" />

<box type="info" seamless>

**Note:** The lifeline for `MarkAttendanceCommandParser` should end at the destroy marker (X), but due to a limitation of PlantUML, the lifeline continues till the end of the diagram.Due to limited space in the diagram, some input parameters(command text, markAttendanceMessage) are not explicitly shown in the diagram.

</box>

How the `mark_attendance` command works:
1. When the user enters a `mark_attendance` command, `LogicManager` passes it to `AddressBookParser`.
2. `AddressBookParser` creates a `MarkAttendanceCommandParser` to parse the command arguments.
3. `MarkAttendanceCommandParser` validates and parses the NUSNET ID, week number, and attendance status.
4. A `MarkAttendanceCommand` object is created and executed.
5. `MarkAttendanceCommand` checks whether the specified student exists.
6. If exists, the attendance status of the student in the specified week is updated to the status.
7. The updated address book is saved to storage.

--------------------------------------------------------------------------------------------------------------------
### Mark All Attendance Feature

The mark all attendance feature allows users to mark the attendance status (e.g., `present`, `absent`, `excused`) of a group of students in a particular week.

The sequence diagram below illustrates the interactions within the `Logic` component for marking attendance:

<puml src="diagrams/MarkAllAttendanceSequenceDiagram.puml"  alt="Interactions Inside the Logic Component for the `mark_all_attendance` Command" />

<box type="info" seamless>

**Note:** The lifeline for `MarkAllAttendanceCommandParser` should end at the destroy marker (X), but due to a limitation of PlantUML, the lifeline continues till the end of the diagram. Due to limited space in the diagram, some input parameters(command text, markAttendanceMessage) are not explicitly shown in the diagram.

</box>

How the `mark_all_attendance` command works:
1. When the user enters a `mark_all_attendance` command, `LogicManager` passes it to `AddressBookParser`.
2. `AddressBookParser` creates a `MarkAllAttendanceCommandParser` to parse the command arguments.
3. `MarkAllAttendanceCommandParser` validates and parses the GroupId, week number, and attendance status.
4. A `MarkAllAttendanceCommand` object is created and executed.
5. `MarkAllAttendanceCommand` checks whether the specified group exists.
6. If exists, the attendance status of students of the group in the specified week is updated to the status.
7. The updated address book is saved to storage.

---

### Add Consultation Feature
The add consultation feature allows users to add consultation slots for students.

The sequence diagram below illustrates the interactions within the `Logic` component for adding a consultation:
<puml src="diagrams/AddConsultationSequenceDiagram-Logic.puml" alt="Interactions inside the Logic Component for the `add_consult` Command" />

The sequence diagram below illustrates the interactions within the `Model` component for adding a consultation:
<puml src="diagrams/AddConsultationSequenceDiagram-Model.puml" alt="Interactions inside the Model Component for the `add_consult` Command" />

How the `add_consult` command works:
1. When the user enters an `add_consult` command, `LogicManager` passes it to `AddressBookParser`.
2. `AddressBookParser` creates an `AddConsultationCommandParser` to parse the command arguments.
3. `AddConsultationCommandParser` validates and parses the NUSNET ID, start time and end time.
4. An `AddConsultationCommand` object is created and executed.
5. During execution, `AddConsultationCommand` checks if the student exists in the model, if the consultation overlaps with other existing consultations in the model, and if the student already has a consultation.
6. If all checks pass, the consultation is added to the student and the model is updated.
7. The updated address book is saved to storage.
8. A success message is returned to the user.

---

### Delete Consultation Feature
The delete consultation feature allows users to delete existing consultations from students.

The sequence diagram below illustrates the interactions within the `Logic` component for deleting a consultation:
<puml src="diagrams/DeleteConsultationSequenceDiagram-Logic.puml" alt="Interactions inside the Logic Component for the `delete_consult` Command" />

The sequence diagram below illustrates the interactions within the `Model` component for deleting a consultation:
<puml src="diagrams/DeleteConsultationSequenceDiagram-Model.puml" alt="Interactions inside the Model Component for the `delete_consult` Command" />

How the `delete_consult` command works:
1. When the user enters a `delete_consult` command, `LogicManager` passes it to `AddressBookParser`.
2. `AddressBookParser` creates a `DeleteConsultationCommandParser` to parse the command arguments.
3. `DeleteConsultationCommandParser` validates and parses the NUSNET ID.
4. A `DeleteConsultationCommand` object is created and executed.
5. During execution, `DeleteConsultationCommand` checks if the student exists in the model and if the student has an existing consultation.
6. If both checks pass, the consultation is removed from the student and the model is updated.
7. The updated address book is saved to storage.
8. A success message is returned to the user.

---

### List Consultation Feature
The list consultation feature allows users to view all scheduled consultations.

The sequence diagram below illustrates the interactions within the `Logic` component for listing consultations:
<puml src="diagrams/ListConsultationSequenceDiagram-Logic.puml" alt="Interactions inside the Logic Component for the `list_consult` Command" />

The sequence diagram below illustrates the interactions within the `Model` component for listing consultations:
<puml src="diagrams/ListConsultationSequenceDiagram-Model.puml" alt="Interactions inside the Model Component for the `list_consult` Command" />

How the `list_consult` command works:
1. When the user enters a `list_consult` command, `LogicManager` passes it to `AddressBookParser`.
2. `AddressBookParser` creates a `ListConsultationCommand` object.
3. The `ListConsultationCommand` object is executed.
4. During execution, `ListConsultationCommand` updates the filtered consultation list in the model.
5. A success message is returned to the user.

### Create Group Feature

The create group feature allows users to create a new group by specifying a unique group ID.

The sequence diagram below illustrates the interactions within the `Logic` and `Model` component for creating a group:

<puml src="diagrams/CreateGroupSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `creategroup` Command" />

<box type="info" seamless>

**Note:** The lifeline for `CreateGroupCommandParser` should end at the destroy marker (X), but due to a limitation of PlantUML, the lifeline continues till the end of the diagram.

</box>

How the `create_group` command works:
1. When the user enters a `create_group` command, `LogicManager` passes it to `AddressBookParser`.
2. `AddressBookParser` creates a `CreateGroupCommandParser` to parse the command arguments.
3. `CreateGroupCommandParser` validates and parses the group ID.
4. A `CreateGroupCommand` object is created and executed.
5. `CreateGroupCommand` checks if the group ID already exists.
6. If no duplicates are found, a new group is created with the specified group ID.
7. The updated address book is saved to storage.

### Add Student to Group Feature

The add student to group feature allows users to assign a student to an existing group by specifying the student's NUSNET ID and the group ID.

The sequence diagram below illustrates the interactions within the `Logic` and `Model` component for adding a student to a group:

<puml src="diagrams/AddToGroupSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `addstudenttogroup` Command" />

<box type="info" seamless>

**Note:** The lifeline for `AddToGroupCommandParser` should end at the destroy marker (X), but due to a limitation of PlantUML, the lifeline continues till the end of the diagram.

</box>

How the `add_to_group` command works:
1. When the user enters an `add_to_group` command, `LogicManager` passes it to `AddressBookParser`.
2. `AddressBookParser` creates an `AddToGroupCommandParser` to parse the command arguments.
3. `AddToGroupCommandParser` validates and parses the NUSNET ID and group ID.
4. An `AddToGroupCommand` object is created and executed.
5. `AddToGroupCommand` checks if the specified student exist.
6. If the student exists, `AddToGroupCommand` checks if the specified group already exist.
7. Create an updated student object with the new group ID.
8. If the group exists, the student is added to the specified group.
9. Else, the group is created and the student is added to the newly created group.
10. The updated address book is saved to storage.
11. If the student does not exist, an error message is shown to the user.

### Find Student by Group Feature

The find student by group feature allows users to search for students belonging to a specific group by specifying the group ID.

The sequence diagram below illustrates the interactions within the `Logic` and `Model` component for finding students by group:

<puml src="diagrams/FindStudentByGroupSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `findstudentbygroup` Command" />

<box type="info" seamless>

**Note:** The lifeline for `FindGroupCommandParser` should end at the destroy marker (X), but due to a limitation of PlantUML, the lifeline continues till the end of the diagram.

</box>

How the `find_group` command works:
1. When the user enters a `find_group` command, `LogicManager` passes it to `AddressBookParser`.
2. `AddressBookParser` creates a `FindGroupCommandParser` to parse the command arguments.
3. `FindGroupCommandParser` validates and parses the group ID.
4. A `FindGroupCommand` object is created and executed.
5. If the group ID is valid, and the group exist in the address book, `FindGroupCommand` retrieves the list of students belonging to the specified group from the `Model`.
6. The filtered student list in the `Model` is updated to **only** include students from the specified group.
7. The updated filtered student list is displayed to the user in the UI.
8. If the group ID is invalid, an error message is shown to the user.
9. If the group does not exist, inform the user that no such group exists.
9. No change to the address book is made.

--------------------------------------------------------------------------------------------------------------------
## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

Teaching assistants (TAs) for Computer Science courses at the National University of Singapore (NUS) who
* needs to manage a group of students (e.g., a tutorial class), with the following responsibilities:
  * mark attendance
  * schedule consultations with students
  * grade homework/assignments
  * track students' progress
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: a one-stop solution for TAs to manage their students more easily than a typical mouse/GUI driven app


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​             | I want to …​                                          | So that I can…​                                                           |
|----------|---------------------|-------------------------------------------------------|---------------------------------------------------------------------------|
| `* * *`  | TA                  | add a new student                                     |                                                                           |
| `* * *`  | TA                  | delete a student                                      | remove entries I no longer need or added by mistake                       |
| `* * *`  | TA                  | mark students' attendance                             | record all students' tutorial attendance                                  |
| `* * *`  | TA                  | track each individual student's homework completeness | view their learning progress and identify students who are falling behind |                                                          |
| `* *`    | new user            | have a step-by-step usage instruction guide           | learn how to use the app                                                  |
| `* *`    | course coordinator  | view all TAs' availability                            | assign TAs to their preferred tutorial group                              |
| `* *`    | head TA             | create subgroups within the course                    | assign students and TAs to their respective tutorial groups               |
| `* *`    | head TA             | key in students' scores                               | update students' scores after every exam                                  |
| `* *`    | head TA             | view overall course feedback from students            | gather data to perform course analysis                                    |
| `* *`    | TA                  | search for a specific student                         | view his/her contact details and progress                                 |
| `* *`    | TA                  | create subgroups within the tutorial group            | assign students to their project groups                                   |
| `* *`    | TA                  | add consultation slots                                | schedule consultations with students                                      |
| `* *`    | TA                  | check students' scores                                | track students' performance                                               |
| `* *`    | TA                  | view students' feedback                               | gain insights on my teaching style and method                             |
| `* *`    | TA                  | update my availability                                | update my consultation schedule                                           |
| `*`      | TA                  | copy contact information onto my clipboard            | save time from manually copying students' contact details                 |
| `*`      | TA                  | export student list as PDF                            | print it out for marking attendance                                       |

### Use cases

(For all use cases below, the **System** is the `SocTAssist` and the **Actor** is the `user`, unless specified otherwise)


**Use case:** UC01 - Add a student

**Actor**: TA

**MSS**

1. User requests to add a student by specifying required fields: full name, NUSNET ID, email, Telegram handle and optional fields: phone number, group ID.

2. SoCTAssist validates all fields.

3. SoCTAssist adds the student into the directory.

4. SoCTAssist shows the updated student list in the UI table.

    Use case ends.

**Extensions**

* 2a. One or more required fields are missing.

    * 2a1. SoCTAssist shows error.

        Use case ends.

* 2b. Email format is invalid.

    * 2b1. SoCTAssist shows error.

        Use case ends.

* 2c. Group ID format is invalid.

    * 2c1. SoCTAssist shows error message.

        Use case ends.

* 2d. A student with the same nusnetid already exists.

    * 2d1. SoCTAssist shows error.

**Use case:** UC02 - Edit a student

**Actor**: TA

**MSS**

1. User requests to edit a student by specifying the index and updated fields.

2. SoCTAssist validates that the student exists.

3. SoCTAssist updates the student's details.

4. SoCTAssist shows confirmation message with updated student details.

    Use case ends.

**Extensions**

* 2a. Student index does not exist.

    * 2a1. SoCTAssist shows error: `The person index provided is invalid`.

      Use case ends.

* 2b. Any updated field is invalid.

    * 2b1. SoCTAssist shows corresponding validation error. (UC01 Extensions 2b, 2c).

      Use case ends.

* 2c. Try to update group id.

    * 2c1. SoCTAssist shows error and states the correct format.

      Use case ends.

**Use case:** UC03 - Delete a student

**Actor**: TA

**MSS**

1. User requests to list students.

2. SoCTAssist shows a list of students.

3. User requests to delete a specific student in the list.

4. SoCTAssist deletes the student.

5. SoCTAssist UI updated.
    Use case ends.

**Extensions**
* 1a. The list is empty.
    * 1a1. SoCTAssist shows error. 

      Use case ends.    
* 3a. Student index does not exist.
    * 3a1. SoCTAssist shows error.
  
    Use case ends.
* 3b. Student index is invalid (not a number or is not positive).
    * 3b1. SoCTAssist shows error.

      Use case ends.

**Use Case:** UC04 - Create Homework

**Actor**: TA

**MSS**

1. User enters a command to create a new homework numbered 1 to 3 for a student using their NUSNET ID.
2. SoCTAssist locates the student homework record.
3. SoCTAssist validates the assignment ID.
4. SoCTAssist creates the new assignment with an initial status of `incomplete`.
5. SoCTAssist displays a success message.

   Use case ends.

**Extensions**
* 2a. Student with the given NUSNET ID does not exist
  
    * 2a1. SoCTAssist displays an error message.

      Use case ends.
* 3a. Homework ID already exists for this student
  
  * 3a1. SoCTAssist displays an error message.

    Use case ends.
* 3b. Homework ID is invalid (not between 1–13)
  
  * 3b1. SoCTAssist displays an error message.

    Use case ends.
    
**Use case:** UC05 - Mark Homework completion

**Actor**: TA

**MSS**

1. User requests to mark a homework status for a student using their NUSNET ID.
2. SoCTAssist locates the student homework record.
3. SoCTAssist verifies the homework ID.
4. SoCTAssist updates the homework status (complete / incomplete / late).
5. SoCTAssist shows a confirmation message.

   Use case ends.

**Extensions**

* 1a. The list is empty.

    Use case ends.

* 2a. The student with the given NUSNET ID does not exist.
  
    * 2a1. SoCTAssist shows error message: `Student not found`.
  
      Use case ends.

* 3a. The given assignment ID is invalid (not between 1-13).
  
    * 3a1. SoCTAssist shows error message.
  
      Use case ends.
      
* 4a. The given status is invalid (not one of complete / incomplete / late).
    * 4a1. SoCTAssist shows error message.
  
      Use case ends.
      
* 4b. The student already has a status recorded for this assignment.
    * 4b1. SoCTAssist updates the record with the new status (last write wins).

      Use case resumes at step 5.

**Use case:** UC06 - Delete a homework

**Actor**: TA

**MSS**

1. User requests to delete a homework for a student using their NUSNET ID.  
2. SoCTAssist locates the student homework record.  
3. SoCTAssist verifies the homework ID.  
4. SoCTAssist removes the corresponding homework record from the student’s tracker.  
5. SoCTAssist shows a confirmation message.  

   Use case ends.

**Extensions**

* 1a. The list is empty.  

    Use case ends.  

* 2a. The student with the given NUSNET ID does not exist.  
    * 2a1. SoCTAssist shows error message.  

      Use case ends.  

* 3a. The given assignment ID is invalid.  
    * 3a1. SoCTAssist shows error message.  

      Use case ends.  

* 4a. The specified homework does not exist for the student.  
    * 4a1. SoCTAssist shows error message.  

      Use case ends.  


**Use case:** UC07 - Add a consultation

**Actor**: TA

**MSS**

1. User requests to add a consultation by specifying student's NUSNET ID, start date & time, and end date & time.

2. SoCTAssist validates the NUSNET ID, dates, and times.

3. SoCTAssist creates the consultation booking for the student.

4. SoCTAssist shows success message with consultation details and displays list of consultations to user.

    Use case ends.

**Extensions**

* 2a. Student NUSNET ID does not exist in the directory.

  * 2a1. SoCTAssist shows an error message.

       Use case ends.

* 2b. End time is not after start time.

    * 2b1. SoCTAssist shows an error message.

         Use case ends.

* 2c. The new consultation overlaps with an existing one.

    * 2c1. SoCTAssist shows an error message.

         Use case ends.

* 2d. A consultation with identical date and time already exists.

    * 2d1. SoCTAssist shows an error message.

         Use case ends.

* 2d. Student already has an existing consultation.

    * 2d1. SoCTAssist shows an error message.

      Use case ends.

* 4a. The consultation exceeds 3 hours in duration.

    * 4a1. SoCTAssist displays a reminder message.

      Use case ends.

* 4b. The consultation is over or ongoing.

    * 4b1. SoCTAssist displays a reminder message.

      Use case ends.

**Use case:** UC08 - Delete a consultation

**Actor**: TA

**MSS**

1. User requests to delete a consultation by specifying student's NUSNET ID.  

2. SoCTAssist validates the NUSNET ID.  

3. SoCTAssist locates the consultation record that matches the provided details.  

4. SoCTAssist deletes the consultation from the system.  

5. SoCTAssist shows success message confirming the deletion and displays list of consultations to user.  

    Use case ends.  

**Extensions**

* 2a. NUSNET ID does not exist in the directory.  

  * 2a1. SoCTAssist shows an error message.  

       Use case ends.  

* 3a. Student with specified NUSNET ID does not have a consultation.  

  * 3a1. SoCTAssist shows an error message.  

       Use case ends.

**Use case:** UC09 - Mark attendance

**Actor**: TA

**MSS**

1. User requests to mark attendance for a student by specifying student NUSNET ID, week, and attendance status.

2. SoCTAssist validates that the student exists and the week and status are valid.

3. SoCTAssist records the attendance for the student.

4. SoCTAssist shows a confirmation message with details.

    Use case ends.

**Extensions**

* 2a. Student NUSNET ID does not exist.

    * 2a1. SoCTAssist shows error: `Student not found`.

         Use case ends.

* 2b. Attendance status is invalid (not Present or Absent or Excused).

    * 2b1. SoCTAssist shows error: `Please enter present/absent/excused only`.

         Use case ends.


**Use case: Mark all attendance**

**Actor: TA**

**MSS**

1. User requests to mark attendance for a group of student by specifying GroupId, week, and attendance status.

2. SoCTAssist validates that the group exists and the week and status are valid.

3. SoCTAssist records the attendance for the student.

4. SoCTAssist shows a confirmation message with details.

    Use case ends.

**Extensions**

* 2a. GroupId does not exist.

    * 2a1. SoCTAssist shows error: `Group not found`.

         Use case ends.

* 2b. Attendance status is invalid (not Present or Absent or Excused).

    * 2b1. SoCTAssist shows error: `Please enter present/absent/excused only`.

         Use case ends.

* 2c. Week is invalid (input is not between 2 and 13 or is not an integer.").

    * 2c1. SoCTAssist shows error: `Invalid Week. Week should be between 2 and 13 and be a positive integer.`.

         Use case ends.
* 2d. Group does not have students.

    * 2d1. SoCTAssist shows error: `No students in the group.`.

         Use case ends.

**Use case:** UC10 - Create student groups

**Actor**: TA

**MSS**

1. User requests to create a new group with a specified GroupId.
2. SoCTAssist validates the GroupId.
3. SoCTAssist creates the group.
4. SoCTAssist shows confirmation message.

   Use case ends.

**Extensions**

* 2a. The GroupId is missing.
    * 2a1. SoCTAssist shows error message, saying GroupId is missing.

    Use case ends.

* 2b. The GroupId is invalid.
    * 2c1. SoCTAssist shows error message and indicates the valid format for GroupId.

      Use case ends.

* 2c. The GroupId is a duplicate.
    * 2b1. SoCTAssist shows error message, saying GroupId already exists

      Use case ends.


**Use case:** UC11 - Add student to a group

**Actor**: TA

**MSS**

1. User requests to add a student to an existing group using the student’s NUSNET ID and GroupId.
2. SoCTAssist verifies the student exists.
3. SoCTAssist checks whether the group id is the same as the student's existing group.
4. SoCTAssist checks whether the group exists.
5. SoCTAssist adds the student to the specified group.
6. SoCTAssist shows confirmation message.

   Use case ends.

**Extensions**

* 2a. The student with the NUSNET ID does not exist.
    * 2a1. SoCTAssist shows error message.

      Use case ends.

* 3a. The group id is the same as the student's existing group.
    * 3a1. SoCTAssist shows error message, saying student already in that group.

      Use case ends.

* 4a. The group does not exist.
    * 4a1. SoCTAssist creates the group.

      Use case resumes at step 5.

**Use case:** UC12 - Find students by group

**Actor**: TA

**Guarantees**: 
* If the GroupId is valid and exists, at least one student is found.

**MSS**

1. User requests to find students by specifying a GroupId.
2. SoCTAssist verifies the GroupId is valid.
3. SoCTAssist checks whether the group exists.
4. SoCTAssist retrieves the list of students in the specified group.
5. SoCTAssist displays the list of students in the UI. 
   
   Use case ends.

**Extensions**

* 2a. The GroupId is invalid.
    * 2a1. SoCTAssist shows error message and indicates the valid format for Group Id.

      Use case ends.

* 3a. The group does not exist.
    * 3a1. SoCTAssist informs the user that no such group exists.
  
      Use case ends.

### Non-Functional Requirements

#### 1. Data Requirements
##### NFR-D1: Data Size
- Maximum 500 students 
- Maximum 20 tutorial groups
- Support 12 weeks of attendance data (weeks 2-13)
- Support maximum 13 homework
- Maximum 1 consultation slot for each student

##### NFR-D2: Data Volatility
**High Volatility Data** (changes very frequently):
- Attendance records: updated every tutorial session (weekly)
- Homework completion status: updated as TAs mark assignments throughout the week
- Consultation slot bookings: students book and cancel constantly, especially before assessments

**Medium Volatility Data** (changes occasionally):
- Student contact information: might change once or twice per semester
- Group assignments: adjusted a few times during the semester

**Low Volatility Data** (rarely changes):
- Student directory (names, NUSNET IDs): mostly stable after add/drop period
- Tutorial group assignments: fixed after first few weeks

##### NFR-D3: Data Persistence
- All student data must persist between application sessions.
- Attendance, homework, and consultation records must be permanent until explicitly deleted.
- System must auto-save after every successful command.

#### 2. Environment/Technical Requirements
##### NFR-E1: Operating System Compatibility
- Must run on Windows, Linux, and OS-X platforms.
- Must work on both 32-bit and 64-bit environments.
- No OS-dependent libraries or OS-specific features allowed.
- Cross-platform compatibility without any modifications to codebase.

##### NFR-E2: Software Dependencies
- Requires Java 17 only (no other Java version required or installed).
- Must work without internet connection (offline-first design).
- No external database server required.
- Third-party libraries must be:
  - Free and open-source with permissive licenses
  - Packaged within the JAR file (no separate installation required)
  - Not require user account creation on third-party services
  - Approved by teaching team prior to use

##### NFR-E3: Hardware Requirements(To be finalized later)
- The application must operate efficiently on standard consumer-grade hardware without requiring specialized equipment.
- Must run on both desktop and laptop computers without additional hardware dependencies.


#### 3. Performance Requirements
##### NFR-P1: Response Time
- Basic commands must complete within 2 seconds.
- Find operations must return results within 2 second.

##### NFR-P2: Startup Time
- Application must launch within 5 seconds on standard hardware.

#### 4. Scalability Requirements
##### NFR-S1: User Scalability
- Supports TAs managing multiple tutorial groups.

##### NFR-S2: Data Scalability
- Performance must not degrade noticeably up to 100 students.

#### 5. Usability Requirements
##### NFR-U1: Learnability
- First-time TA users must be able to add a student and mark attendance within 10 minutes using the onboarding guide.
- The onboarding guide must be completable in under 5 minutes.
- Help command must provide examples for all commands.

##### NFR-U2: Efficiency
- Experienced users should be able to mark attendance for 30 students in under 2 minutes.
- Common tasks should require fewer than 3 commands.
- All primary functions must be accessible via keyboard commands without requiring mouse.

##### NFR-U3: Error Handling
- Error messages must be specific and actionable.
- System must provide confirmation prompts for destructive operations.
- No technical jargon in error messages - use plain language

##### NFR-U4: Consistency
- Command syntax must be consistent across all features using the same prefix style (i/, n/, e/, t/, status/, w/, a/).
- All command names follow verb-noun format: `add_student`, `mark_attendance`, `delete`.

##### NFR-U5: Visual Design
- Minimum font size: 12pt for readability
- UI must be usable on minimum resolution 1280x720.
- Clear visual separation between tabs (Students, Attendance, Homework, Groups)


#### 6.Constraints
##### NFR-C1: Constraint-Single-User
- The product should be for a single user i.e., (not a multi-user product).
- Not allowed: Application running in a shared computer and different people using it at different times.
- Not allowed: The data file created by one user being accessed by another user during regular operations (e.g., through a shared file storage mechanism).

##### NFR-C2: NoDBMS
- Do not use a DBMS to store data.


### Glossary
**Homework terms**
* **complete**: The student has submitted the homework on time.
* **incompleted**: The student has not submitted the homework yet. When the user creates the homework, it is marked as incomplete by default.
* **late**: The student has submitted the homework, but has passed the deadline of the homework.

**Attendance terms**
* **present**: The student is present for the tutorial on that week.
* **excused**: The student did not come for the tutorial, but has a valid reason (e.g., on MC, has competition)
* **absent**: The student did not come for the tutorial, and does not have a valid reason.

**Consultation terms**
* **Ongoing**: A consultation is ongoing if it has a start time before the current time and a end time after the current time.

  (E.g. if the current time is `20251010 1700`, a consultation from `20251010 1600` to `20251010 1800` is ongoing.)
* **Over**: A consultation is over if its end time is before the current time.

  (E.g. if the current time is `20251010 1700`, a consultation from `20251010 1400` to `20251010 1600` is over.)
* **Overlap**: A consultation overlaps with another consultation if its start time is before the other consultation's end time and its end time is after the other consultation's start time.

  (E.g. a consultation from `20251010 1400` to `20251010 1600` overlaps with a consultation from `20251010 1559` to `20251010 1759` but does not overlap with a consultation from `20251010 1600` to `20251010 1800`.)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

This section provides step-by-step, comprehensive instructions for performing **manual testing** of the application. These steps ensure that testers can validate the app’s functionality, usability, and reliability without the need for automated test tools.

> **Note:** These instructions serve as a foundation for manual testing. Testers are strongly encouraged to perform **exploratory testing** beyond the described cases to uncover edge cases and unexpected behaviors.

---

### **1. Launch and Shutdown Tests**

#### **1.1 Initial Launch**

1. **Download and Setup:**

   * Obtain the latest `soctassist.jar` file of the application.
   * Copy the `soctassist.jar` file into a **new, empty folder** to avoid interference from previous data files.

2. **Launch Application:**

   * Double-click the `soctassist.jar` file to start the application.
   * Open a terminal or command prompt in the folder containing the .jar file.
   * Run the following command:
     * `java -jar soctassist.jar`
   * **Expected Result:**
     * The main GUI should appear with a **set of sample data** (e.g., sample students).
     * The window size and position may initially not be optimized.
     * The app should **not crash or freeze** upon startup.

#### **1.2 Saving Window Preferences**

1. **Change and Save Window State:**

   * Resize and reposition the main application window.
   * Close the application normally.

2. **Re-launch Application:**

### Deleting a person
   * Open the `soctassist.jar` file again.
   * **Expected Result:**
     * The window should reopen **at the same size and position** as before.
     * All layout and visual preferences should persist across restarts.

---

### **2. Data Management Tests**

#### **2.1 Deleting a person (Functional Test)**

1. **Preparation:**

   * Use the `list` command to ensure that multiple persons are visible in the list.

2. **Valid Deletion Command:**
   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message.

   * Command: `delete 1`
   * **Expected Result:**

     * The first person in the list is deleted.
     * The status bar updates with the details of the deleted student.
     * The total count of persons decreases by one.

3. **Invalid Deletion Commands:**

   * Try the following commands:

     * `delete 0`
     * `delete` (no index)
     * `delete x` (where *x* is greater than the list size)
   * **Expected Result:**

     * Appropriate error messages displayed.
     * No data is deleted.
---

### **3. Saving and Loading Data**

#### **3.1 Data Persistence Check**

1. Add a few entries (new students).
2. Close and reopen the application.
3. **Expected Result:**

   * All newly added data should reappear, confirming that data was correctly saved to disk.

---

### **4. User Interface and Command Behavior**

#### **4.1 Command Validation**

* Test all supported commands (e.g., `add`, `edit`, `list`, `help`) with correct and incorrect parameters.
* **Expected Result:**

  * Correct commands execute successfully.
  * Incorrect ones show meaningful error messages (no technical jargon).

#### **4.2 Keyboard Accessibility**

* Verify that all major commands can be executed via keyboard only.
* **Expected Result:** No mouse interaction should be required for primary operations.

#### **4.3 Error Message Clarity**

* Trigger various input errors intentionally.
* **Expected Result:**

  * Messages should explain what went wrong and how to fix it.
  * Messages should use simple, clear language.

---

### **5. Exploratory Testing Suggestions**

* Try adding invalid characters.
* Test compatibility on different operating systems (Windows, macOS, Linux).

### Add homework
1. Add homework to a single student
   1. Setup: Ensure that there is at least one student in the system with NUSNET ID `E1234567`.
   2. Execute the command:
      `add_hw i/E1234567 a/1`
   3. **Expected:**
   - Success message is displayed:  
     ```
     Added assignment 1 for <STUDENT_NAME> (default incomplete).
     ```
   - The student’s homework tracker now includes homework **1**, marked as **incomplete** by default.
   - The command box is cleared and ready for the next input.

2. Add homework to all students
   1. Setup: Ensure multiple students are present in the system.
   2. Execute the command:
      `add_hw i/all a/2`
   3. **Expected:**
   - Success message is displayed:  
     ```
     Added assignment 2 for all students (default incomplete).
     ```
   - All students’ homework trackers now contain homework **2**.
  
3. Add homework (Student not found)
   1. Setup: Ensure the student with the NUSNET ID E0000000 is not present in the system.
   2. Execute the command:
      `add_hw i/E0000000 a/3`
   3. **Expected:**
   - Error message is displayed:  
     ```
     Student with NUSNET ID E0000000 does not exist.
     ```
   - No homework is added.

4. Add homework (Missing parameters)
   1. Execute the command:
      `add_hw a/1`
   2. **Expected:**
   - Error message is displayed:  
     ```
     Invalid command format! <With the rest of helping information>
     ```
   - No homework is added.

5. Add homework (Invalid homework ID: out of range)
   1. Execute the command:
      `add_hw i/E1234567 a/100`
   2. **Expected:**
   - Error message is displayed:  
     ```
     Homework id must be between 1 and 13.
     ```
   - No homework is added.

6. Add homework (Duplicate prefixes)
   1. Setup: Make sure that the person with NUSNET ID E1234567 already has homework 1
   2. Execute the command:
      `add_hw i/E1234567 a/1`
   3. **Expected:**
   - Error message is displayed:  
     ```
     Homework 1 already exists for <STUDNET_NAME>.
     ```
   - No homework is added.

**Summary of Expected Results**

| Test Case | Command | Expected Outcome |
|------------|----------|------------------|
| 1 | `add_hw i/E1234567 a/1` | ✅ Homework 1 added to student E1234567 |
| 2 | `add_hw i/all a/2` | ✅ Homework 2 added to all students |
| 3 | `add_hw i/E0000000 a/3` | ❌ Student not found |
| 4 | `add_hw a/4` | ❌ Missing NUSNET ID |
| 5 | `add_hw i/E1234567 a/14` | ❌ Homework ID out of range |
| 6 | `add_hw i/E1234567 i/E7654321 a/1` | ❌ Duplicate prefixes |

### Delete homework
1. Delete homework from a single student  
   1. Setup: Ensure that there is at least one student in the system with NUSNET ID `E1234567`, and that the student already has homework **1** assigned.  
   2. Execute the command:  
      `delete_hw i/E1234567 a/1`  
   3. **Expected:**  
   - Success message is displayed:  
     ```
     Deleted homework 1 for <STUDENT_NAME>.
     ```
   - Homework **1** is removed from the student’s homework tracker.
   - The command box is cleared and ready for the next input.

2. Delete homework from all students  
   1. Setup: Ensure multiple students are present in the system, each with homework **2** assigned.  
   2. Execute the command:  
      `delete_hw i/all a/2`  
   3. **Expected:**  
   - Success message is displayed:  
     ```
     Deleted homework 2 for all students.
     ```
   - Homework **2** is removed from every student’s homework tracker.

3. Delete homework (Student not found)  
   1. Setup: Ensure that there is no student with the NUSNET ID `E0000000` in the system.  
   2. Execute the command:  
      `delete_hw i/E0000000 a/1`  
   3. **Expected:**  
   - Error message is displayed:  
     ```
     Invalid NUSNET ID format.
     ```
   - No homework is deleted.

4. Delete homework (Missing parameters)  
   1. Execute the command:  
      `delete_hw a/1`  
   2. **Expected:**  
   - Error message is displayed:  
     ```
     Invalid command format! <With the rest of helping information>
     ```
   - No homework is deleted.

5. Delete homework (Invalid homework ID: out of range)  
   1. Execute the command:  
      `delete_hw i/E1234567 a/100`  
   2. **Expected:**  
   - Error message is displayed:  
     ```
     Homework id must be between 1 and 13.
     ```
   - No homework is deleted.

6. Delete homework (Homework not found)  
   1. Setup: Ensure that the student with NUSNET ID `E1234567` **does not** have homework **3** assigned.  
   2. Execute the command:  
      `delete_hw i/E1234567 a/3`  
   3. **Expected:**  
   - Error message is displayed:  
     ```
     Homework 3 not found for Alex Yeoh.
     ```
   - No homework is deleted.

7. Delete homework (Duplicate prefixes)  
   1. Execute the command:  
      `delete_hw i/E1234567 i/E7654321 a/1`  
   2. **Expected:**  
   - Error message is displayed:  
     ```
     Invalid command format! <With the rest of helping information>
     ```
   - No homework is deleted.

**Summary of Expected Results**

| Test Case  | Command                               | Expected Outcome                           |
|------------|---------------------------------------|--------------------------------------------|
| 1          | `delete_hw i/E1234567 a/1`            | ✅ Homework 1 deleted from student E1234567 |
| 2          | `delete_hw i/all a/2`                 | ✅ Homework 2 deleted from all students     |
| 3          | `delete_hw i/E0000000 a/1`            | ❌ Student not found                        |
| 4          | `delete_hw a/1`                       | ❌ Missing NUSNET ID                        |
| 5          | `delete_hw i/E1234567 a/100`          | ❌ Homework ID out of range                 |
| 6          | `delete_hw i/E1234567 a/3`            | ❌ Homework does not exist                  |
| 7          | `delete_hw i/E1234567 i/E7654321 a/1` | ❌ Duplicate prefixes                       |

### Mark homework
1. Mark homework as completed for a single student  
   1. Setup: Ensure that there is at least one student in the system with NUSNET ID `E1234567`, and that the student has homework **1** assigned.  
   2. Execute the command:  
      `mark_hw i/E1234567 a/1 status/complete`  
   3. **Expected:**  
   - Success message is displayed:  
     ```
    Assignment 1 for <STUDENT_NAME> marked complete.
     ```
   - Homework **1** is now marked as **completed** in the student’s homework tracker.
   - The command box is cleared and ready for the next input.

2. Mark homework as incomplete for a single student  
   1. Setup: Ensure homework **1** for student `E1234567` is currently marked as **completed**.  
   2. Execute the command:  
      `mark_hw i/E1234567 a/1 status/incomplete`  
   3. **Expected:**  
   - Success message is displayed:  
     ```
     Assignment 1 for <STUDENT_NAME> marked incomplete.
     ```
   - Homework **1** is now marked as **incomplete** in the student’s homework tracker.

3. Mark homework as late for a single student  
   1. Setup: Ensure homework **2** is assigned to student `E1234567`.  
   2. Execute the command:  
      `mark_hw i/E1234567 a/2 status/late`  
   3. **Expected:**  
   - Success message is displayed:  
     ```
     Assignment 1 for <STUDENT_NAME> marked late.
     ```
   - Homework **2** is now marked as **late** in the student’s homework tracker.

4. Mark homework (Student not found)  
   1. Execute the command:  
      `mark_hw i/E0000000 a/1 status/complete`  
   2. **Expected:**  
   - Error message is displayed:  
     ```
     Student not found
     ```
   - No homework is updated.

5. Mark homework (Homework not assigned)  
   1. Setup: Ensure that student `E1234567` **does not** have homework **3** assigned.  
   2. Execute the command:  
      `mark_hw i/E1234567 a/3 status/complete`  
   3. **Expected:**  
   - Error message is displayed:  
     ```
     Homework 3 does not exist for <STUDENT_NAME>. Add it first using 'add_hw'.
     ```
   - No homework is updated.

6. Mark homework (Invalid status)  
   1. Execute the command:  
      `mark_hw i/E1234567 a/1 status/done`  
   2. **Expected:**  
   - Error message is displayed:  
     ```
     Status must be one of: complete, incomplete, late.
     ```
   - No homework is updated.

7. Mark homework (Missing parameters)  
   1. Execute the command:  
      `mark_hw i/E1234567 a/1`  
   2. **Expected:**  
   - Error message is displayed:  
     ```
     Invalid command format! <With the rest of helping information>
     ```
   - No homework is updated.

8. Mark homework (Duplicate prefixes)  
   1. Execute the command:  
      `mark_hw i/E1234567 i/E7654321 a/1 status/complete`  
   2. **Expected:**  
   - Error message is displayed:  
     ```
     Multiple values specified for the following single-valued field(s): i/
     ```
   - No homework is updated.

**Summary of Expected Results**

| Test Case | Command                                              | Expected Outcome               |
|-----------|------------------------------------------------------|--------------------------------|
| 1         | `mark_hw i/E1234567 a/1 status/completed`            | ✅ Homework 1 marked completed  |
| 2         | `mark_hw i/E1234567 a/1 status/incomplete`           | ✅ Homework 1 marked incomplete |
| 3         | `mark_hw i/E1234567 a/2 status/late`                 | ✅ Homework 2 marked late       |
| 4         | `mark_hw i/E0000000 a/1 status/completed`            | ❌ Invalid NUSNET ID            |
| 5         | `mark_hw i/E1234567 a/3 status/completed`            | ❌ Homework does not exist      |
| 6         | `mark_hw i/E1234567 a/1 status/done`                 | ❌ Invalid status               |
| 7         | `mark_hw i/E1234567 a/1`                             | ❌ Missing status parameter     |
| 8         | `mark_hw i/E1234567 i/E7654321 a/1 status/completed` | ❌ Duplicate prefixes           |

### Mark Attendance

1. Mark attendance for a single student  
   1. Setup: Ensure that there is at least one student in the system with NUSNET ID `E1234567`.  
   2. Execute the command:  
      `mark_attendance i/E1234567 w/2 status/present`  
   3. **Expected:**  
   - Success message is displayed.

2. Mark attendance (Student not found)
   1. Setup: Ensure that there is no student with the NUSNET ID `E0000000` in the system.
   2. Execute the command:
      `mark_attendance i/E0000000 w/2 status/present`
   3. **Expected:**
   - Error message is displayed.

3. Mark attendance (Invalid week)
   1. Ensure that there is a student in the system with NUSNET ID `E1234567`.
   2. Execute the command:
      `mark_attendance i/E1234567 w/20 status/present`
   2. **Expected:**
   - Error message is displayed.

4. Mark attendance (Invalid status)
   1. Ensure that there is a student in the system with NUSNET ID `E1234567`.
   2. Execute the command:
      `mark_attendance i/E1234567 w/2 status/late`
   3. **Expected:**
   - Error message is displayed.

### Mark All Attendance
1. Mark attendance for all students in one group
    1. Setup: Ensure group T01 with students present in the system.
    2. Execute the command:  
       `mark_all_attendance g/T01 w/2 status/absent`
    3. **Expected:**
    - Success message is displayed.

2. Mark all attendance (Group not found)
    1. Setup: Ensure that there is no group with GroupID T02 in the system.
    2. Execute the command:
       `mark_all_attendance g/T02 w/2 status/present`
    3. **Expected:**
    - Error message is displayed.

3. Mark all attendance (Invalid week)
    1.  Setup: Ensure group T01 with students present in the system.
    2. Execute the command:
       `mark_all_attendance g/T01 w/20 status/present`
    2. **Expected:**
    - Error message is displayed.

4. Mark all attendance (Invalid status)
    1.  Setup: Ensure group T01 with students present in the system.
    2. Execute the command:
       `mark_all_attendance g/T01 w/2 status/late`
    3. **Expected:**
    - Error message is displayed.

5. Mark all attendance (Group has no students)
    1.  Setup: Ensure group T03 exists but has no students in the system.
    2. Execute the command:
       `mark_all_attendance g/T03 w/2 status/present`
    3. **Expected:**
    - Error message is displayed.
### Add Consultation
1. Add a consultation for a student  
   1. Setup: Ensure that there is a student in the system with NUSNET ID `E1234567` that has no consultation.  
   2. Execute the command:  
      `add_consult i/E1234567 from/20251010 1400 to/20251010 1500`  
   3. **Expected:**  
   - Success message is displayed.
   - List of consultations is updated to include the new consultation.

2. Add a consultation (Student not found)  
   1. Setup: Ensure that there is no student with the NUSNET ID `E0000000` in the system.
   2. Execute the command:  
      `add_consult i/E0000000 from/20251010 1400 to/20251010 1500`  
   3. **Expected:**  
   - Error message is displayed.

3. Add a consultation (End time before start time)  
   1. Setup: Ensure that there is a student in the system with NUSNET ID `E1234568`.
   2. Execute the command:  
      `add_consult i/E1234568 from/20251010 1500 to/20251010 1400`
   3. **Expected:**  
      - Error message is displayed.

4. Add a consultation (Overlapping consultation)  
   1. Setup: Ensure that there is a student in the system with a consultation from `20251010 1400` to `20251010 1500`. Ensure that there is a another student in the system with NUSNET ID `E1234569` without consultation.
   2. Execute the command:  
      `add_consult i/E1234569 from/20251010 1430 to/20251010 1530`  
   3. **Expected:**  
   - Error message is displayed.

5. Add a consultation (Incorrect date/time format)  
   1. Setup: Ensure that there is a student in the system with NUSNET ID `E1234567`.
   2. Execute the command:  
      `add_consult i/E1234567 from/2025-10-10 14:00 to/2025-10-10 15:00`  
   3. **Expected:**  
   - Error message is displayed.

6. Add a consultation (Invalid date)
   1. Setup: Ensure that there is a student in the system with NUSNET ID `E1234567`.
   2. Execute the command:  
      `add_consult i/E1234567 from/20251310 1400 to/20251310 1500`  
   3. **Expected:**  
   - Error message is displayed.

### Delete Consultation
1. Delete a consultation for a student  
   1. Setup: Ensure that there is a student in the system with NUSNET ID `E1234567`, and that the student has a consultation scheduled.
   2. Execute the command:  
      `delete_consult i/E1234567`
   3. **Expected:**  
   - Success message is displayed.
   - The consultation list is updated to remove the deleted consultation.

2. Delete a consultation (Student not found)  
   1. Setup: Ensure that there is no student with the NUSNET ID `E0000000` in the system.
   2. Execute the command:  
      `delete_consult i/E0000000`
   3. **Expected:**  
   - Error message is displayed.

3. Delete a consultation (No consultation found)  
   1. Setup: Ensure that there is a student in the system with NUSNET ID `E1234568`, and that the student does not have any consultation scheduled.
   2. Execute the command:  
      `delete_consult i/E1234568`
   3. **Expected:**  
      Error message is displayed.

### Create Group
1. Create a new group  
   1. Ensure that group `T01` does not exist.
   2. Execute the command:  
      `create_group g/T01`  
   2. **Expected:**  
   - Success message is displayed.

2. Create group (Group already exists)  
   1. Setup: Ensure that group `T01` already exists in the system.
   2. Execute the command:  
      `create_group g/T01`  
   3. **Expected:**  
   - Error message is displayed.

### Add Student to Group
1. Add a student to an existing group  
   1. Setup: Ensure that there is a student in the system with NUSNET ID `E1234567`.
   2. Execute the command:  
      `add_to_group i/E1234567 g/T01`  
   3. **Expected:**  
   - Success message is displayed.
   - Student is updated with the new group.

2. Add student to group (Student not found)  
   1. Setup: Ensure that there is no student with the NUSNET ID `E0000000` in the system, and that group `T01` exists.
   2. Execute the command:  
      `add_to_group i/E0000000 g/T01`  
   3. **Expected:**  
   - Error message is displayed.

3. Add student to group (Group already contains the student)
   1. Setup: Ensure that there is a student with the NUSNET ID `E1234567` that is in group `T01`.
   2. Execute the command:
      `add_to_group i/E1234567 g/T01`
   3. **Expected:**
   - Error message is displayed.

### Find Students by Group
1. Find students by group  
   1. Setup: Ensure that group `T01` exists and has students assigned to it.
   2. Execute the command:  
      `find_group g/T01`  
   3. **Expected:**  
   - Success message is displayed.
   - List of students in group `T01` is displayed.


## **Appendix: Planned Enhancement**
This section outlines potential future enhancements for the application that could improve its functionality, usability, or performance. These enhancements are not part of the current scope but may be considered for future development. 
Team size: 5


1. **Remove deprecated `create_group` command**: Since group creation is now integrated into the `add_student` and `add_to_group` command, the standalone `create_group` command can be removed to streamline the command set and reduce redundancy.
1. **Enhance `mark_attendance` and `mark_all_attendance` command with attendance status `unmark`**: Introduce an `unmark` status option to allow TAs to easily revert a student's attendance for a specific week if they accidentally marked them as present, absent, or excused. For example, if they accidentally marked a student as present in week 5, they can now use `mark_attendance i/E1234567 w/5 status/unmark` to clear the attendance for week 5 and the icon will turn grey.
1. **Improve 'find' command with advanced filters**: Enable users to search for students using partial name. For example, searching for "Alex" would return both "Alex Yeoh" and "Alexander Tan".
1. **Error message enhancements**: Show the specific field that caused the error in error messages to help users quickly identify and correct the missing/duplicated field. For example, if a duplicate email is entered, the error message could specify "Duplicate email format in field: email".
1. **Enhance `add_consult` commands by allowing one student to book multiple consultation slot**: Allow students to book multiple consultation slots instead of being limited to one. This would accommodate students with varying schedules and needs.
1. **Enhance `add_consult` commands by allowing multiple student to book the same consultation slot**: Allow multiple students to book the same consultation slot, enabling group consultations.
1. **Enhance `delete_hw i/all a/<hw id>` commands by adding confirmation prompt**: Before deleting a homework assignment for all students, prompt the user for confirmation to prevent accidental deletions. For example, after entering the delete command, the system could ask "Are you sure you want to delete homework for all student (yes/no)".
1. **Reduce command word length**: Shorten command words to make them quicker to type. For example, change `add_student` to `as`, `mark_attendance` to `ma`, `mark_all_attendance` to `maa` and `delete_hw` to `dh`.
1. **Enhance `add_to_group` command by allowing adding one student into multiple groups**: For example, a TA can teach both CS2030S and CS2040S, and the same student can be added to both groups.
