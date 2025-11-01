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

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
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

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

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

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

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

How the `addhomework` command works:
1. When the user enters an `addhomework` command, `LogicManager` passes it to `AddressBookParser`.
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

How the `deletehomework` command works:
1. When the user enters a `deletehomework` command, `LogicManager` passes it to `AddressBookParser`.
2. `AddressBookParser` creates a `DeleteHomeworkCommandParser` to parse the command arguments.
3. `DeleteHomeworkCommandParser` validates and parses the NUSNET ID (or the keyword `all`) and the assignment ID.
4. A `DeleteHomeworkCommand` object is created and executed.
5. `DeleteHomeworkCommand` verifies that the homework exists for the specified student(s).
6. If found, the homework is removed from the respective homework tracker(s).
7. The updated address book is saved to storage.

---

### Mark Homework Feature

The mark homework feature allows users to update the status (e.g., `done`, `pending`) of a homework assignment for a specific student.

The sequence diagram below illustrates the interactions within the `Logic` component for marking homework:

<puml src="diagrams/MarkHomeworkSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `markhomework` Command" />

<box type="info" seamless>

**Note:** The lifeline for `MarkHomeworkCommandParser` should end at the destroy marker (X), but due to a limitation of PlantUML, the lifeline continues till the end of the diagram.

</box>

How the `markhomework` command works:
1. When the user enters a `markhomework` command, `LogicManager` passes it to `AddressBookParser`.
2. `AddressBookParser` creates a `MarkHomeworkCommandParser` to parse the command arguments.
3. `MarkHomeworkCommandParser` validates and parses the NUSNET ID, assignment ID, and status.
4. A `MarkHomeworkCommand` object is created and executed.
5.`MarkHomeworkCommand` checks whether the specified homework exists for the student.
6. If found, the homework’s status is updated to the new value.
7. The updated address book is saved to storage.

--------------------------------------------------------------------------------------------------------------------
### Mark Attendance Feature

The mark attendance feature allows users to mark the attendance status (e.g., `present`, `absent`, `excused`) of a single student in a particular week.

The sequence diagram below illustrates the interactions within the `Logic` component for marking attendance:

<puml src="diagrams/MarkAttendanceSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `markhomework` Command" />

<box type="info" seamless>

**Note:** The lifeline for `MarkAttendanceCommandParser` should end at the destroy marker (X), but due to a limitation of PlantUML, the lifeline continues till the end of the diagram.

</box>

How the `markAttendance` command works:
1. When the user enters a `markAttendance` command, `LogicManager` passes it to `AddressBookParser`.
2. `AddressBookParser` creates a `MarkAttendanceCommandParser` to parse the command arguments.
3. `MarkAttendanceCommandParser` validates and parses the NUSNET ID, week number, and attendance status.
4. A `MarkAttendanceCommand` object is created and executed.
5. `MarkAttendanceCommand` checks whether the specified student exits.
6. If exits, the attendance status of the student in the specified week is updated to the status.
7. The updated address book is saved to storage.

--------------------------------------------------------------------------------------------------------------------
### Mark All Attendance Feature

The mark all attendance feature allows users to mark the attendance status (e.g., `present`, `absent`, `excused`) of a group of students in a particular week.

The sequence diagram below illustrates the interactions within the `Logic` component for marking attendance:

<puml src="diagrams/MarkAllAttendanceSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `markhomework` Command" />

<box type="info" seamless>

**Note:** The lifeline for `MarkAllAttendanceCommandParser` should end at the destroy marker (X), but due to a limitation of PlantUML, the lifeline continues till the end of the diagram.

</box>

How the `markAllAttendance` command works:
1. When the user enters a `markAllAttendance` command, `LogicManager` passes it to `AddressBookParser`.
2. `AddressBookParser` creates a `MarkAllAttendanceCommandParser` to parse the command arguments.
3. `MarkAllAttendanceCommandParser` validates and parses the GroupId, week number, and attendance status.
4. A `MarkAllAttendanceCommand` object is created and executed.
5. `MarkAllAttendanceCommand` checks whether the specified group exits.
6. If exits, the attendance status of students of the group in the specified week is updated to the status.
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

How the `creategroup` command works:
1. When the user enters a `creategroup` command, `LogicManager` passes it to `AddressBookParser`.
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

**Note:** The lifeline for `AddStudentToGroupCommandParser` should end at the destroy marker (X), but due to a limitation of PlantUML, the lifeline continues till the end of the diagram.

</box>

How the `addstudenttogroup` command works:
1. When the user enters an `addstudenttogroup` command, `LogicManager` passes it to `AddressBookParser`.
2. `AddressBookParser` creates an `AddStudentToGroupCommandParser` to parse the command arguments.
3. `AddStudentToGroupCommandParser` validates and parses the NUSNET ID and group ID.
4. An `AddStudentToGroupCommand` object is created and executed.
5. `AddStudentToGroupCommand` checks if the specified student exist.
6. If the student exists, `AddStudentToGroupCommand` checks if the specified group already exist.
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

**Note:** The lifeline for `FindStudentByGroupCommandParser` should end at the destroy marker (X), but due to a limitation of PlantUML, the lifeline continues till the end of the diagram.

</box>

How the `find_group` command works:
1. When the user enters a `find_group` command, `LogicManager` passes it to `AddressBookParser`.
2. `AddressBookParser` creates a `FindStudentByGroupCommandParser` to parse the command arguments.
3. `FindStudentByGroupCommandParser` validates and parses the group ID.
4. A `FindStudentByGroupCommand` object is created and executed.
5. If the group ID is valid, and the group exist in the address book, `FindStudentByGroupCommand` retrieves the list of students belonging to the specified group from the `Model`.
6. The filtered student list in the `Model` is updated to only include students from the specified group.
7. The updated filtered student list is displayed to the user in the UI.
8. If the group ID is invalid or the group does not exist, an error message is shown to the user.
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

1. User requests to add a student by specifying required fields: full name, NUSNET ID, email, Telegram handle ann optional fields: phone number, group ID.

2. AddressBook validates all fields.

3. AddressBook adds the student into the directory.

4. AddressBook shows the updated student list in the UI table.

    Use case ends.

**Extensions**

* 2a. One or more required fields are missing.

    * 2a1. AddressBook shows error: `Invalid command format!`.

        Use case ends.

* 2b. Email format is invalid.

    * 2b1. AddressBook shows error: `Invalid email format`.

        Use case ends.

* 2c. Group ID format is invalid.

    * 2d1. AddressBook shows error message: `Invaid Group ID`.

        Use case ends.

* 2d. A student with the same nusnetid already exists.

    * 2e1. AddressBook shows error: `Student with this nusnetid already exists`.

       Use case ends.

**Use case:** UC02 - Edit a student
**Actor**: TA

**MSS**

1. User requests to edit a student by specifying the index and updated fields.

2. AddressBook validates that the student exists.

3. AddressBook updates the student's details.

4. AddressBook shows confirmation message with updated student details.

    Use case ends.

**Extensions**

* 2a. Student index does not exist.

    * 2a1. AddressBook shows error: `The person index provided is invalid`.

      Use case ends.

* 2b. Any updated field is invalid.

    * 2c1. AddressBook shows corresponding validation error. (UC01 Extensions 2b, 2c).

      Use case ends.

* 2c. Try to update group id.

    * 2c1. AddressBook shows error: `Group ID cannot be edited directly. Use add_to_group commands instead.`

      Use case ends.

**Use case:** UC03 - Delete a student
**Actor**: TA

**MSS**

1. User requests to list students.

2. AddressBook shows a list of students.

3. User requests to delete a specific student in the list.

4. AddressBook deletes the student.

5. AddressBook UI updated.
    Use case ends.

**Use Case:** UC04 - Create Homework
**Actor**: TA

**MSS**

1. User enters a command to create a new homework numbered 1 to 3 for a student using their NUSNET ID.
2. Homework Tracker locates the student record.
3. Homework Tracker validates the assignment ID.
4. Homework Tracker creates the new assignment with an initial status of `incomplete`.
5. Homework Tracker displays a success message.

   Use case ends.

**Extensions**
* 2a. Student with the given NUSNET ID does not exist
  
    * 2a1. Homework Tracker displays an error: `Student not found`.

      Use case ends.
* 3a. Homework ID already exists for this student
  
  * 3a1. Homework Tracker displays an error: `Assignment ID already exists`.

    Use case ends.
* 3b. Homework ID is invalid (not between 1–3)
  
  * 3b1. Homework Tracker displays an error: `Assignment ID must be between 1 and 3`.

    Use case ends.
    
**Use case:** UC05 - Mark Homework completion
**Actor**: TA

**MSS**

1. User requests to mark a homework status for a student using their NUSNET ID.
2. Homework Tracker locates the student record.
3. Homework Tracker verifies the homework ID.
4. Homework Tracker updates the homework status (complete / incomplete / late).
5. Homework Tracker shows a confirmation message.

   Use case ends.

**Extensions**

* 1a. The list is empty.

    Use case ends.

* 2a. The student with the given NUSNET ID does not exist.
  
    * 2a1. Homework Tracker shows error message: `Student not found`.
  
      Use case ends.

* 3a. The given assignment ID is invalid (not between 1-3).
  
    * 3a1. Homework Tracker shows error message: `Assignment ID must be between 1 and 3`.
  
      Use case ends.
* 4a. The given status is invalid (not one of complete / incomplete / late).
    * 4a1. Homework Tracker shows error message: `Please enter complete/incomplete/late only`.
  
      Use case ends.
* 4b. The student already has a status recorded for this assignment.
    * 4b1. Homework Tracker updates the record with the new status (last write wins).

      Use case resumes at step 5.

**Use case:** UC06 - Delete a homework
**Actor**: TA

**MSS**

1. User requests to delete a homework for a student using their NUSNET ID.  
2. Homework Tracker locates the student record.  
3. Homework Tracker verifies the homework ID.  
4. Homework Tracker removes the corresponding homework record from the student’s tracker.  
5. Homework Tracker shows a confirmation message.  

   Use case ends.

**Extensions**

* 1a. The list is empty.  

    Use case ends.  

* 2a. The student with the given NUSNET ID does not exist.  
    * 2a1. Homework Tracker shows error message: `Student not found`.  

      Use case ends.  

* 3a. The given assignment ID is invalid.  
    * 3a1. Homework Tracker shows error message: `Assignment ID must be between 1 and 3`.  

      Use case ends.  

* 4a. The specified homework does not exist for the student.  
    * 4a1. Homework Tracker shows error message: `Assignment not found for this student`.  

      Use case ends.  

* 1b. User requests to delete homework for all students.  
    * 1b1. Homework Tracker iterates through all student records and removes the specified homework from each valid record.  
    * 1b2. Homework Tracker shows a confirmation message summarizing the deletions.  

      Use case ends.  

**Use case:** UC07 - Add a consultation
**User**: TA

**MSS**

1. User requests to add a consultation by specifying student NUSNET ID, date_start_time, and date_end_time.

2. AddressBook validates the student NUSNET ID, date, and times.

3. AddressBook creates the consultation booking for the student.

4. AddressBook shows success message with consultation details.

    Use case ends.

**Extensions**

* 2a. Student NUSNET ID does not exist in the directory.

  * 2a1. AddressBook shows error: `Student not found`.

       Use case ends.

* 2b. End time is not after start time.

    * 2b1. AddressBook shows error: `End time must be after start time`.

         Use case ends.

* 2c. The new consultation overlaps with an existing one.

    * 2c1. AddressBook shows error: `Time conflict with existing booking`.

         Use case ends.

* 2d. A consultation with identical date and time already exists.

    * 2d1. AddressBook shows error: `Duplicate consultation booking`.

         Use case ends.

**Use case:** UC08 - Delete a consultation
**User**: TA

**MSS**

1. User requests to delete a consultation by specifying student NUSNET ID, date_start_time, and date_end_time.  

2. AddressBook validates the student NUSNET ID, date, and times.  

3. AddressBook locates the consultation record that matches the provided details.  

4. AddressBook deletes the consultation booking from the system.  

5. AddressBook shows success message confirming the deletion.  

    Use case ends.  

**Extensions**

* 2a. Student NUSNET ID does not exist in the directory.  

  * 2a1. AddressBook shows error: `Student not found`.  

       Use case ends.  

* 3a. Consultation record with the specified details does not exist.  

  * 3a1. AddressBook shows error: `Consultation not found`.  

       Use case ends.  

* 3b. Consultation list is empty.  

  * 3b1. AddressBook shows error: `No consultations available to delete`.  

       Use case ends.  

**Use case:** UC09 - Mark attendance
**User**: TA

**MSS**

1. User requests to mark attendance for a student by specifying student NUSNET ID, week, and attendance status.

2. AddressBook validates that the student exists and the week and status are valid.

3. AddressBook records the attendance for the student.

4. AddressBook shows a confirmation message with details.

    Use case ends.

**Extensions**

* 2a. Student NUSNET ID does not exist.

    * 2a1. AddressBook shows error: `Student not found`.

         Use case ends.

* 2b. Attendance status is invalid (not Present or Absent or Excused).

    * 2b1. AddressBook shows error: `Please enter present/absent/excused only`.

         Use case ends.


**Use case: Mark all attendance**

**MSS**

1. User requests to mark attendance for a group of student by specifying GroupId, week, and attendance status.

2. AddressBook validates that the group exists and the week and status are valid.

3. AddressBook records the attendance for the student.

4. AddressBook shows a confirmation message with details.

    Use case ends.

**Extensions**

* 2a. GroupId does not exist.

    * 2a1. AddressBook shows error: `Group not found`.

         Use case ends.

* 2b. Attendance status is invalid (not Present or Absent or Excused).

    * 2b1. AddressBook shows error: `Please enter present/absent/excused only`.

         Use case ends.

**Use case:** UC10 - Create student groups
**User**: TA

**MSS**

1. User requests to create a new group with a specified GroupName.
2. Address Book validates the GroupId.
3. System creates the group.
4. System shows confirmation message.

   Use case ends.

**Extensions**

* 2a. The GroupId is missing.
    * 2a1. System shows error message, saying Group Id is missing.

    Use case ends.

* 2b. The GroupId is invalid.
    * 2c1. System shows error message and indicates the valid format for Group Id.

      Use case ends.

* 2c. The GroupId is a duplicate.
    * 2b1. System shows error message, saying Group Id already exists

      Use case ends.


**Use case:** UC11 - Add student to a group
**User**: TA

**MSS**

1. User requests to add a student to an existing group using the student’s NUSNET ID and GroupId.
2. System verifies the student exists.
3. System checks whether the group id is the same as the student's existing group.
4. System checks whether the group exists.
5. System adds the student to the specified group.
6. System shows confirmation message.

   Use case ends.

**Extensions**

* 2a. The student with the NUSNET ID does not exist.
    * 2a1. System shows error message.

      Use case ends.

* 3a. The group id is the same as the student's existing group.
    * 3a1. System shows error message, saying student already in that group.

      Use case ends.

* 4a. The group does not exist.
    * 4a1. System creates the group.

      Use case resumes at step 5.

**Use case:** UC12 - Find students by group
**User**: TA

**Guarantees**: 
* If the GroupId is valid and exists, at least one student is found.

**MSS**

1. User requests to find students by specifying a GroupId.
2. System verifies the GroupId is valid.
3. System checks whether the group exists.
4. System retrieves the list of students in the specified group.
5. System displays the list of students in the UI. 
   
   Use case ends.

**Extensions**

* 2a. The GroupId is invalid.
    * 2a1. System shows error message and indicates the valid format for Group Id.

      Use case ends.

* 3a. The group does not exist.
    * 3a1. System shows error message, saying group does not exist.
  
      Use case ends.

### Non-Functional Requirements

#### 1. Data Requirements
##### NFR-D1: Data Size
- Maximum 500 students 
- Maximum 20 tutorial groups
- Support 12 weeks of attendance data (weeks 2-13)
- Support maximum 3 assignments
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
- Supports TAs managing multiple tutorial slots.

##### NFR-S2: Data Scalability
- Performance must not degrade noticeably up to 100 students.
- Supports unlimited consultation bookings per student.

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

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Private contact detail**: A contact detail that is not meant to be shared with others

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
