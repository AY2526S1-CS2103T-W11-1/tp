---
layout: default.md
title: "Developer Guide"
pageNav: 3
---

# AB-3 Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

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

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


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

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


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
| `* *`    | course coordinator  | view all TAs' availability                            | assign TAs to their preferred tutorial slot                               |
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


**Use case: Add a student**

**MSS**

1. User requests to add a student by specifying full name, preferred name, email, Telegram handle, and slot ID.

2. AddressBook validates all fields.

3. AddressBook adds the student into the directory.

4. AddressBook shows the updated student list in the UI table.

    Use case ends.

**Extensions**

* 2a. One or more required fields are missing.

    * 2a1. AddressBook shows error: Missing required field: <field>.

        Use case ends.

* 2b. Email format is invalid.

    * 2b1. AddressBook shows error: Invalid email format. Use RFC-5322 pattern.

        Use case ends.

* 2c. Telegram handle format is invalid.

    * 2c1. AddressBook shows error message.

        Use case ends.

* 2d. Slot ID format is invalid.

    * 2d1. AddressBook shows error message.

        Use case ends.

* 2e. A student with the same nusnetid already exists.

    * 2e1. AddressBook shows error: Student with this nusnetid already exists.

       Use case ends.

**Use case: Edit a student**

**MSS**

1. User requests to edit a student by specifying the email and updated fields.

2. AddressBook validates that the student exists.

3. AddressBook updates the student's details.

4. AddressBook shows confirmation message with updated student details.

    Use case ends.

**Use case: Delete a student**

**MSS**

1. User requests to list students.

2. AddressBook shows a list of students.

3. User requests to delete a specific student in the list.

4. AddressBook deletes the student.

5. AddressBook UI updated.
    
    Use case ends.

**Extensions**

* 2a. Student email does not exist.

    * 2a1. AddressBook shows error: Student not found.

      Use case ends.

* 2b. Edited email duplicates another existing student’s nusnetid.

    * 2b1. AddressBook shows error: nusnetid already in use.

      Use case ends.

* 2c. Any updated field is invalid.

    * 2c1. AddressBook shows corresponding validation error.

      Use case ends.

**Use Case: Create Assignment**

**MSS**

1. User enters a command to create a new assignment numbered 1 to 3 for a student using their NUSNET ID.
2. Homework Tracker locates the student record.
3. Homework Tracker validates the assignment ID.
4. Homework Tracker creates the new assignment with an initial status of `incomplete`.
5. Homework Tracker displays a success message.

   Use case ends.

**Extensions**
* 2a. Student with the given NUSNET ID does not exist
  
    * 2a1. Homework Tracker displays an error: `Student not found`.

      Use case ends.
      
* 3a. Assignment ID already exists for this student
  
  * 3a1. Homework Tracker displays an error: `Assignment ID already exists`.

    Use case ends.
    
* 3b. Assignment ID is invalid (not between 1–3)
  
  * 3b1. Homework Tracker displays an error: `Assignment ID must be between 1 and 3`.

    Use case ends.


**Use case: Mark assignment completion**

**MSS**

1. User requests to mark an assignment status for a student using their NUSNET ID.
2. Homework Tracker locates the student record.
3. Homework Tracker verifies the assignment ID.
4. Homework Tracker updates the assignment status (complete / incomplete / late).
5. Homework Tracker shows a confirmation message.

   Use case ends.

**Extensions**

* 1a. The list is empty.

    Use case ends.

* 2a. The student with the given NUSNET ID does not exist.
  
    * 2a1. Homework Tracker shows error message: `Student not found`.
  
      Use case ends.

* 3a. The given assignment ID is invalid (not between 0–2).
  
    * 3a1. Homework Tracker shows error message: `Assignment not found`.
  
      Use case ends.


* 4a. The given status is invalid (not one of complete / incomplete / late).
    * 4a1. Homework Tracker shows error message: `Please enter complete/incomplete/late only`.
  
      Use case ends.
* 4b. The student already has a status recorded for this assignment.
    * 4b1. Homework Tracker updates the record with the new status (last write wins).

      Use case resumes at step 5.

**Use case: Add a consultation**

**MSS**

1. User requests to add a consultation by specifying student email, date, start time, and end time.

2. AddressBook validates the student email, date, and times.

3. AddressBook creates the consultation booking for the student.

4. AddressBook shows success message with consultation details.

    Use case ends.

**Extensions**

* 2a. Student nusnetid does not exist in the directory.

  * 2a1. AddressBook shows error: Student not found.

       Use case ends.

* 2b. End time is not after start time.

    * 2b1. AddressBook shows error: End time must be after start time.

         Use case ends.

* 2c. The new consultation overlaps with an existing one.

    * 2c1. AddressBook shows error: Time conflict with existing booking.

         Use case ends.

* 2d. A consultation with identical date and time already exists.

    * 2d1. AddressBook shows error: Duplicate consultation booking.

         Use case ends.


**Use case: Mark attendance**

**MSS**

1. User requests to mark attendance for a student by specifying student nusnetid, date, and attendance status.

2. AddressBook validates that the student exists and the date/status are valid.

3. AddressBook records the attendance for the student.

4. AddressBook shows confirmation message with details.

    Use case ends.

**Extensions**

* 2a. Student nusnetid does not exist.

    * 2a1. AddressBook shows error: Student not found.

         Use case ends.

* 2b. Attendance status is invalid (not Present or Absent or Excused).

    * 2b1. AddressBook shows error: Invalid attendance status.

         Use case ends.

**Use case: Create and manage student groups**

**MSS**

1. User requests to create a new group with a specified GroupName.
2. Homework Tracker validates the GroupName.
3. System creates the group.
4. System shows confirmation message: `Group <GroupName> is created.`

   Use case ends.

**Extensions**

* 2a. The GroupName is missing.
    * 2a1. System shows error message: `Missing required field: GroupName`.

      Use case ends.

* 2b. The GroupName is a duplicate.
    * 2b1. System shows error message: `Invalid Team Name`.

      Use case ends.


**Use case: Add student to a group**

**MSS**

1. User requests to add a student to an existing group using the student’s email and GroupName.
2. System verifies the group exists.
3. System verifies the student exists.
4. System checks whether the student is already in the group.
5. System adds the student to the group.
6. System shows confirmation message: `Alice is added to Group <GroupName>.`

   Use case ends.

**Extensions**

* 2a. The GroupName is missing or invalid.
    * 2a1. System shows error message: `Missing required field: GroupName` or `Invalid Team Name`.

      Use case ends.

* 3a. The student's email is missing or invalid.
    * 3a1. System shows error message: `Missing required field: Email` or `Student does not exist`.

      Use case ends.

* 4a. The student is already in the group.
    * 4a1. System shows error message: `Student already in this group`.

      Use case ends.


### Non-Functional Requirements

#### 1. Data Requirements
##### NFR-D1: Data Size
- Maximum 500 students per course
- Maximum 50 tutorial slots per course
- Support 12 weeks of attendance data (weeks 2-13)
- Support at least 10 assignments per course
- Store consultation history for entire semester

##### NFR-D2: Data Volatility
**High Volatility Data** (changes very frequently):
- Attendance records: updated every tutorial session (weekly)
- Homework completion status: updated as TAs mark assignments throughout the week
- Consultation slot bookings: students book and cancel constantly, especially before assessments

**Medium Volatility Data** (changes occasionally):
- Student contact information: might change once or twice per semester
- Group assignments: adjusted a few times during the semester
- TA availability: changes periodically but not daily

**Low Volatility Data** (rarely changes):
- Student directory (names, NusNET IDs): mostly stable after add/drop period
- Tutorial slot assignments: fixed after first few weeks

##### NFR-D3: Data Persistence
- All student data must persist between application sessions
- Attendance, homework, and consultation records must be permanent until explicitly deleted
- System must auto-save after every successful command
- Historical data (eg.assessment performance) must persist across semesters

#### 2. Environment/Technical Requirements
##### NFR-E1: Operating System Compatibility
- Must run on Windows, Linux, and OS-X platforms
- Must work on both 32-bit and 64-bit environments
- No OS-dependent libraries or OS-specific features allowed
- Cross-platform compatibility without any modifications to codebase

##### NFR-E2: Software Dependencies
- Requires Java 17 only (no other Java version required or installed)
- Must work without internet connection (offline-first design)
- No external database server required
- Third-party libraries must be:
  - Free and open-source with permissive licenses
  - Packaged within the JAR file (no separate installation required)
  - Not require user account creation on third-party services
  - Approved by teaching team prior to use

##### NFR-E3: Hardware Requirements(To be finalized later)


#### 3. Performance Requirements
##### NFR-P1: Response Time
- Basic commands (add, delete, mark) must complete within 2 seconds
- Search and filter operations must return results within 1 second
- Tab switching must occur within 1 second
- PDF export must complete within 5 seconds for up to 200 students

##### NFR-P2: Startup Time
- Application must launch within 3 seconds on standard hardware
- Onboarding guide must appear within 1 second of first launch


#### 4. Scalability Requirements
##### NFR-S1: User Scalability
- Support TAs managing multiple tutorial slots simultaneously

##### NFR-S2: Data Scalability
- Performance must not degrade noticeably up to 100 students
- Support unlimited consultation bookings per student


#### 5. Usability Requirements
##### NFR-U1: Learnability
- First-time TA users must be able to add a student and mark attendance within 10 minutes using the onboarding guide
- The onboarding guide must be completable in under 5 minutes
- Help command must provide examples for all commands

##### NFR-U2: Efficiency
- Experienced users should be able to mark attendance for 30 students in under 2 minutes
- Common tasks should require fewer than 10 commands
- All primary functions must be accessible via keyboard commands without requiring mouse

##### NFR-U3: Error Handling
- Error messages must be specific and actionable (e.g., "Missing required field: email" not "Error 404")
- System must provide confirmation prompts for destructive operations (delete student, bulk delete)
- No technical jargon in error messages - use plain language

##### NFR-U4: Consistency
- Command syntax must be consistent across all features using the same prefix style (i/, n/, e/, t/, s/, w/, a/)
- All command names follow verb-noun format: `add_student`, `mark_attendance`, `delete_student`
- Parameter handling behavior must be consistent (e.g., last occurrence wins for duplicate prefixes)

##### NFR-U5: Visual Design
- Minimum font size: 12pt for readability
- UI must be usable on minimum resolution 1280x720
- Clear visual separation between tabs (Students, Attendance, Homework, Groups)
- Tables must have alternating row colors for scannabilityRetryClaude can make mistakes. Please double-check responses.Research Sonnet 4.5


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
