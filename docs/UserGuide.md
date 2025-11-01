---
  layout: default.md
  title: "User Guide"
  pageNav: 3
---

# SoCTAssist User Guide

SoCTAssist is a desktop app designed specifically to help Teaching Assistants manage their students' information,
homework, attendance, and consultation sessions more efficiently.

If you are a Teaching Assistance who can type fast, SoCTAssist can get your contact management tasks
done faster than traditional GUI app and 
<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

# Quick Start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/AY2526S1-CS2103T-W11-1/tp/releases/tag/v1.4).

1. Copy the file to the folder you want to use as the _home folder_ for your SoCTAssist.

1. Open a command terminal, using command `cd` + the path to get into the folder you put the jar file in, and use the `java -jar soctassist.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. <br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add_student n/John Doe  i/E1234567 t/@john g/T01 p/98765432 e/johnd@@u.nus.edu` : Adds a contact named `John Doe` to the address book.

   * `delete 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------
# Command Summary

| Description                 | Format                                                                                     | Example                                                                              |
|-----------------------------|--------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------|
| **Display help message**    | `help`                                                                                     | `help`                                                                               |
| **List students**           | `list`                                                                                     | `list`                                                                               |
| **List consultations**      | `list_consult`                                                                             | `list_consult`                                                                       |
| **Add student**             | `add_student n/NAME i/NUSNETID t/TELEGRAM g/GROUPID [p/PHONE_NUMBER] [e/EMAIL]`            | `add_student n/James Ho i/E1234567 t/@jame g/T02 [p/22224444] [e/jamesho@u.nus.edu]` |
| **Edit student**            | `edit_student INDEX [n/NAME] [i/NUSNETID] [t/TELEGRAM] [p/PHONE_NUMBER] [e/EMAIL]`         | `edit_student 2 n/James Lee e/jameslee@u.nus.edu`                                    |
| **Delete student**          | `delete INDEX`                                                                             | `delete 3`                                                                           |
| **Find students by name**   | `find KEYWORD [MORE_KEYWORDS]`                                                             | `find James Jake`                                                                    |
| **Add homework**            | `add_hw i/NUSNETID a/ASSIGNMENT`<br>(use `i/all` for all students)                         | `add_hw i/E1234567 a/1`                                                              |
| **Mark homework**           | `mark_hw i/NUSNETID a/ASSIGNMENT status/STATUS`<br>(STATUS: complete, incomplete, late)    | `mark_hw i/E1234567 a/1 status/complete`                                                    |
| **Delete homework**         | `delete_hw i/NUSNETID a/ASSIGNMENT`<br>(use `i/all` for all students)                      | `delete_hw i/E1234567 a/1`                                                           |
| **Mark attendance**         | `mark_attendance i/NUSNETID w/WEEK status/ATTENDANCE_STATUS`<br>(STATUS: present, absent, excused)    | `mark_attendance i/E1234567 w/3 status/present`                                      |
| **Mark attendance for all** | `mark_all_attendance g/GROUPID w/WEEK status/ATTENDANCE_STATUS`<br>(STATUS: present, absent, excused) | `mark_all_attendance g/T01 w/3 status/present`                                       |
| **Add consultation**        | `add_consult i/NUSNETID from/DATE_TIME to/DATE_TIME`                                       | `add_consult i/E1234567 from/20240915 1400 to/20240915 1500`                         |
| **Delete consultation**     | `delete_consult i/NUSNETID`                                                                | `delete_consult i/E1234567`                                                          |
| **Create group**            | `create_group g/GROUPID`                                                                   | `create_group g/T03`                                                                 |
| **Add student to group**    | `add_to_group i/NUSNETID g/GROUPID`                                                        | `add_to_group i/E1234567 g/T03`                                                      |
| **Find students by group**  | `find_group g/GROUPID`                                                                     | `find_group g/T03`                                                                   |
| **Clear address book**      | `clear`                                                                                    | `clear`                                                                              |
| **Exit application**        | `exit`                                                                                     | `exit`                                                                               |

--------------------------------------------------------------------------------------------------------------------

# Features

<box type="info" seamless>

**Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add_student n/NAME`, `NAME` is a parameter which can be used as `add_student n/John Doe`.

* Items in square brackets are **optional**.<br>
  e.g `n/NAME [p/PHONE]` can be used as `n/John Doe p/87415612` or as `n/John Doe`.

* Parameters can be in **any** order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable. \
  But the index parameter must always come directly after the command word for commands that require an index.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.

* A student is considered as duplicate if both their NUSNET ID or Telegram handle or Phone Number or Email is the same as another existing student in the address book.

* Parameter Constraints:

  * NUSNET ID: An `E` (case-insensitive) followed by 7 digits, e.g. `E1234567`.

  * Telegram handle: Starts with `@` followed by at least 1 alphanumeric characters (underscores allowed), e.g. `@john_doe123`.

  * Phone number: A string of 3 to 15 digits, e.g. `98765432`.

  * Email: A valid NUS email address in the format `<NUSNETID>@u.nus.edu`, e.g. `
</box>

## Viewing help : `help`

Shows a message summarising all commands and displays a URL link that directs user to the help page.

![help message](images/helpMessage.png)

Format: `help`

---
## List Commands

### Listing all persons : `list`

Shows a list of all persons in the address book.

Format: `list`


### Listing all consultations : `list_consult`

Displays list of all consultations in the address book.

Format: `list_consult`

* Consultations will be sorted according to their start time, with the earliest consultation on top.

Note:
* After using `list_consult` command, index in `edit_student` and `delete` commands will refer to the global index of the student (index displayed after `list` command).
* Users are highly recommended to use `list` command to find out the global index of the student before using `edit_student` or `delete` commands.
* Users can use `list` command to return to the student list view.

---
## Person Commands

### Adding a person: `add_student`

Adds a person to the address book.

Format: `add_student n/NAME i/NUSNETID t/TELEGRAM g/GROUPID  [p/PHONE_NUMBER] [e/EMAIL]`

<box type="tip" seamless>

**Tip:** Phone and email are optional. You can omit either or both when adding a person.
* For duplicate checking, NUSNET ID, Telegram handle, Phone Number and Email must be unique across all persons in the address book.
</box>

Examples:
* `add_student n/John Doe i/E1234567 t/@handle g/T01` (no phone or email)
* `add_student n/John Doe i/E1234567 t/@handle g/T01  p/98765432 e/johnd@u.nus.edu`
* `add_student n/Betsy Crow i/E1234562 p/1234567 t/@betsy g/T02  e/betsycrowe@u.nus.edu`


### Editing a person : `edit_student`

Edits an existing person in the address book.

Format: `edit_student INDEX [n/NAME] [i/NUSNETID]  [t/TELEGRAM] [p/PHONE] [e/EMAIL]`

* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the fields is provided to change the person's details.
* Existing values will be updated to the input values.
* You can not use this command to change a person's tutorial group. Use the `add_to_group` command instead.
* For duplicate checking, NUSNET ID, Telegram handle, Phone Number and Email must be unique across all persons in the address book.


Examples:
*  `edit_student 1 p/91234567 e/johndoe@u.nus.edu` Edits the phone number and email address of the 1st person to be `91234567` and `johndoe@u.nus.edu` respectively.
*  `edit_student 2 n/Betsy Crower` Edits the name of the 2nd person to be `Betsy Crower`.


### Deleting a person : `delete`

Deletes the specified person from the address book.

Format: `delete INDEX`

* Deletes the person at the specified `INDEX`.
* The index refers to the index number shown in the **displayed** person list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `delete 2` deletes the 2nd person in the displayed person list.
* `find Betsy` followed by `delete 1` deletes the 1st person in the results of the `find` command.


### Finding students by name: `find`

Finds persons whose names contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only the name is searched.
* Only full words will be matched e.g. `Han` will not match `Hans`
* Persons matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `find Doe` returns `Doe` and `John Doe`
  ![result for 'find alex david'](images/findDoeResult.png)

---
## Homework Commands

### Adding homework : `add_hw`

Adds a homework assignment for the specified student or for all students.

Format: `add_hw i/NUSNETID (use 'i/all' for all students) a/ASSIGNMENT`

* Adds the homework with the given assignment number for the specified student.
* If `i/all` is used, the homework is added for all students.
* The NUSNET ID **must be valid** and the assignment identifier **must be specified**.
* The newly added homework will have a default status of `incomplete`.
* The assignment number should be a positive integer between 1 to 3.
* Homework number must be between 1 to 3.
* If adding homework for a specific student, NUSNET ID is used, which starts with E and has 7 numbers, and it should not be blank.
* The NUSNET ID and homework number **must be valid**.
* The system will check the validity of command format, followed by validity of input, and lastly the existence of the student.

Examples:
* `add_hw i/E1234567 a/1` adds assignment 1 for the student with NUSNET ID `E1234567`.
* `add_hw i/all a/2` adds assignment 2 for all students.


### Marking homework : `mark_hw`

Marks the homework status for the specified student.

Format: `mark_hw i/NUSNETID a/ASSIGNMENT status/STATUS`

* Marks the specified assignment for the given student.
* The assignment number should be a positive integer between 1 to 3.
* The assignment must exist for the student.
* The `STATUS` can be one of the following: `complete`, `incomplete`, or `late`.
* The NUSNET ID, homework number and status **must be valid**.
* The system will check the validity of command format, followed by validity of input, and lastly the existence of the student.

Examples:
* `mark_hw i/E1234567 a/1 status/complete` marks assignment 1 as complete for student `E1234567`.
* `mark_hw i/E2345678 a/2 status/late` marks assignment 2 as late for student `E2345678`.


### Deleting homework : `delete_hw`

Deletes the homework for the specified student or for all students.

Format: `delete_hw i/NUSNETID (use 'i/all' for all students) a/ASSIGNMENT`

* Deletes the homework with the given assignment number for the specified student. 
* The assignment number should be a positive integer between 1 to 3.
* The assignment must exist for the student.
* If `i/all` is used, the homework is deleted for all students.
* The NUSNET ID and homework number **must be valid**.
* The system will check the validity of command format, followed by validity of input, and lastly the existence of the student.

Examples:
* `delete_hw i/E1234567 a/1` deletes assignment 1 for the student with NUSNET ID `E1234567`.
* `delete_hw i/all a/2` deletes assignment 2 for all students.

---
## Attendance Commands

### Marking attendance for one student: `mark_attendance`

Marks the attendance status for the specified student and week.

Format: `mark_attendance i/NUSNETID w/WEEK status/ATTENDANCE_STATUS`

* Marks attendance for the given student and week.
* week number must between 2 to 13.
* NUSNET ID can start with E and has 7 numbers, and it should not be blank.
* The `ATTENDANCE_STATUS` can be one of the following: `present`, `absent`, or `excused`.
* The NUSNET ID, week number and status **must be valid**.
* The system will check the validity of command format, followed by validity of input, and lastly the existence of the student.

Examples:
* `mark_attendance i/E1234567 w/3 status/present` marks student `E1234567` as present for week 3.
* `mark_attendance i/E2345678 w/5 status/absent` marks student `E2345678` as absent for week 5.

---

### Marking attendance for one group of students : `mark_all_attendance`

Marks the attendance status for all the students in one tutorial group in a specified week.

Format: `mark_all_attendance g/GROUPID w/WEEK status/ATTENDANCE_STATUS`

* Marks attendance for the given tutorial group of student and week.
* week number must between 2 to 13.
* Group IDs should start with T or B (case-insensitive) and be followed by strictly 2 digits.
* The `ATTENDANCE_STATUS` can be one of the following: `present`, `absent`, or `excused`.
* The groupId, week number and status **must be valid**.
* The system will check the validity of command format, followed by validity of input, and lastly the existence of the group.

Examples:
* `mark_all_attendance g/T01 w/3 status/present` marks all students in group T01 as present for week 3.
* `mark_all_attendance g/B04 w/5 status/absent` marks all students in group B04 as absent for week 5.

---
## Consultation Commands

### Adding a consultation : `add_consult`

Adds a consultation session for the specified student.

Format: `add_consult i/NUSNETID from/DATE_TIME to/DATE_TIME`

* Both start (`from`) and end (`to`) times **must be in `YYYYMMDD HHmm` format**.
* The start time must be **earlier** than the end time**.
* The NUSNET ID, start time and end time **must be valid**.
* If a consultation already exists for the student, it will be unavailable to add a new consultation to the student.
* If the consultation time overlaps with an existing consultation for another student, it will be unavailable to add the new consultation.

Examples:
* `add_consult i/E1234567 from/20240915 1400 to/20240915 1500` adds a consultation from 2–3PM on 15 Sep 2024 for student `E1234567`.
* `add_consult i/E2345678 from/20240920 1000 to/20240920 1100` adds a consultation from 10–11AM on 20 Sep 2024 for student `E2345678`.
  
Note:
* After using `add_consult` command, index in `edit_student` and `delete` commands will refer to the global index of the student (index displayed after `list` command).
* Users are highly recommended to use `list` command to find out the global index of the student before using `edit_student` or `delete` commands.

### Deleting a consultation : `delete_consult`

Deletes a consultation session for the specified student.

Format: `delete_consult i/NUSNETID`

* Deletes the consultation for the specified student.
* The NUSNET ID **must be valid**.

Examples:
* `delete_consult i/E1234567` deletes consultation for student `E1234567`.
* `delete_consult i/E2345678` deletes consultation for student `E2345678`.

Note:
* After using `delete_consult` command, index in `edit_student` and `delete` commands will refer to the global index of the student (index displayed after `list` command).
* Users are highly recommended to use `list` command to find out the global index of the student before using `edit_student` or `delete` commands.

---
## Group Commands

### Creating a group : `create_group`

Creates a new tutorial group.

Format: `create_group g/GROUPID`

* Creates a new group with the specified group ID.
* The group ID **must be unique**. If a group with the same ID already exists, an error message will be shown.
* Group IDs typically follow the format `TXX` (for tutorial) or `BXX` (for lab), where `XX` are exactly 2 digits (from 0 to 9).

Examples:
* `create_group g/T03` creates a new group with ID `T03`.
* `create_group g/B05` creates a new group with ID `B05`.

### Adding a student to a group : `add_to_group`

Adds a student to a tutorial group.

Format: `add_to_group i/NUSNETID g/GROUPID`

* move a student with the specified NUSNET ID to a group with the specified group ID.
* If the specified group does not exist, it will be created.
* If the group exists, the student will be added to that group.
* Student cannot be moved to the same group they are already in; an error message will be shown in such cases.
* Since a student can only belong to one group at a time, adding them to a new group will remove them from their previous group.
* The NUSNET ID and group ID **must be valid**. For group ID, refer [here](#creating-a-group--create_group).

Examples:
* `add_to_group i/E1234567 g/T03` move student with NUSNET ID `E1234567` from current group to group `T03`.
* `add_to_group i/E2345678 g/B05` (assume group B05 does not exist initially) create group `B05` and move student `E2345678` to it.

### Finding group members : `find_group`
Finds all members in a specified tutorial group.

Format: `find_group g/GROUPID`

* Displays all students belonging to the specified group.
* The group ID **must be valid**.

Examples:
* `find_group g/T03` displays all members in group `T03`.
* `find_group g/B05` displays all members in group `B05`.

---

## Clearing all entries : `clear`

Clears all entries from the address book.

* Deletes all students, groups and consultations from the address book.

Format: `clear`

## Exiting the program : `exit`

Exits the program.

Format: `exit`

## Saving the data

AddressBook data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

## Editing the data file

AddressBook data are saved automatically as a JSON file in JAR `file_location/data/addressbook.json`. \
Users are **NOT** recommended to edit the data file directly, but if you need to do so, please follow these guidelines:
1. Ensure that the JSON format is valid after editing.
2. Ensure that all fields have valid values according to the specifications of AddressBook.


<box type="warning" seamless>

**Caution:**
If your changes to the data file makes its format invalid, AddressBook will discard all data and start with an empty data file at the next run.  Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the AddressBook to behave in unexpected ways (e.g., if a value entered is outside the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</box>
--------------------------------------------------------------------------------------------------------------------

# FAQs

**Q**: How do I back up or transfer my data to another computer?<br>
**A**: Copy the data file at `file_location/data/addressbook.json` from your current machine and replace the same file on the other machine. Close the app before copying to avoid partial writes.

**Q**: Are phone and email mandatory when adding a student?<br>
**A**: No. `p/PHONE` and `e/EMAIL` are optional. Example: `add_student n/John i/E1234567 t/@john g/T01` (no phone/email).

**Q**: What is a valid NUSNET ID format?<br>
**A**: An `E` (case-insensitive) followed by 7 digits, e.g. `E1234567`.

**Q**: Can I target all students at once for homework commands?<br>
**A**: Yes. Use `i/all` with homework commands, e.g. `add_hw i/all a/2` or `delete_hw i/all a/2`.

**Q**: Do homework/attendance/consultation commands use index or NUSNET ID?<br>
**A**: They use NUSNET ID. For example, `mark_hw i/E1234567 a/1 status/complete` and `mark_attendance i/E1234567 w/3 status/present`.

**Q**: What values are valid for assignment numbers?<br>
**A**: Positive integers from 1 to 3.

**Q**: What is the valid range for attendance week?<br>
**A**: Weeks 2 to 13 inclusive.

**Q**: What’s the valid format for group IDs?<br>
**A**: Start with `T` or `B` (case-insensitive) followed by exactly two digits, e.g., `T01`, `B04`.

**Q**: How do I mark attendance for an entire tutorial group?<br>
**A**: Use `mark_all_attendance g/GROUPID w/WEEK status/STATUS`, e.g. `mark_all_attendance g/T01 w/3 status/present`.

**Q**: How do consultation times work and what conflicts are checked?<br>
**A**: Use `YYYYMMDD HHmm` for `from/` and `to/`, and ensure start is earlier than end. A student can have at most one consultation, and consultation times cannot overlap with another student’s consultation.

**Q**: After running `list_consult`, why do some indices not match what I see?<br>
**A**: `list_consult` shows consultations; student edit/delete still use the global student index from `list`. Run `list` to return to the student list view and use those indices.

**Q**: Can I edit the data file manually?<br>
**A**: Not recommended. If you must, ensure the JSON stays valid and values follow the app rules. Invalid edits can cause the app to reset to an empty dataset on next start.

**Q**: Where is the data file stored exactly?<br>
**A**: By default at `file_location/data/addressbook.json` relative to the app’s working directory (same folder as the JAR unless configured otherwise).

**Q**: How do I update SoCTAssist to a newer version?<br>
**A**: Download the new JAR and replace the old one. Your data in `file_location/data/addressbook.json` will be reused automatically. If there are breaking changes, follow any migration instructions in the release notes.

**Q**: Where can I find application logs for troubleshooting?<br>
**A**: In the app folder as rotating files like `addressbook.log`, `addressbook.log.0`, etc. Share the latest log when reporting issues.

**Q**: If I change a student’s NUSNET ID, will their homework/attendance/consultation be preserved?<br>
**A**: Yes. These records are linked to the student and will follow the updated NUSNET ID.

**Q**: Can I delete a tutorial group?<br>
**A**: There is no explicit delete command for groups. Groups are created automatically when needed. Move students to other groups as required.

**Q**: Can I mark attendance for all students across all groups?
**A**: Not directly. Use `mark_all_attendance` per group (`g/GROUPID`). There is no global "all students" attendance command.

**Q**: How can I see a student’s homework and attendance quickly?
**A**: Use `list` to show students; details appear in the student panel. There is no separate `list_hw` command.

**Q**: What if I want to add another consultation to a student who already has one?<br>
**A**: A student can only have one consultation at a time. If you want to add another consultation, please delete the existing consultation first using the `delete_consult` command.

--------------------------------------------------------------------------------------------------------------------

# Known Issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen.

   **Remedy:** Delete the `preferences.json` file created by the application before running the application again.

2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear.
   
   **Remedy:** Manually restore the minimized Help Window.
3. **When using `edit_student` or `delete` commands on the consultation view page**, the index of student to be edited or deleted cannot be seen.
   
   **Remedy:** Use the `list` command to return to the student view page to obtain the target student's index before using the `edit_student` or `delete` command.

--------------------------------------------------------------------------------------------------------------------

# Glossary
* **NUSNETID**: A unique identifier assigned to each student by the National University of Singapore (NUS) during matriculation. It is used for logging into various NUS systems.
* **NUS email**: The official email address assigned to each student by NUS, typically in the format `<NUSNETID>@u.nus.edu`.
* **Tutorial Group**: A smaller group of students within a course. Group IDs usually follow the format `TXX` or `BXX`, where `XX` represent 2 digits.
* **CLI**: Command Line Interface. A text-based interface used to interact with software applications by typing commands.
* **Week**: There are 13 weeks in each academic semester in NUS, and tutorial starts in Week 3.
* **Assessment/Homework**: Work that needs to be done and submitted by mentees, graded by tutors.
* **Consultation**: A session where mentees can seek help from tutors regarding their academic work or other related matters.
* **Teaching Assistant (TA)**: A Teaching Assistant (TA) is a senior student who provides guidance and support to a junior student, known as a student.
* **GUI**: Graphical User Interface. A visual interface that allows users to interact with software applications using graphical elements such as windows, icons, and buttons.
* **JSON**: JavaScript Object Notation. A lightweight data interchange format that is easy for humans to read and write, and easy for machines to parse and generate.
* **JDK**: Java Development Kit. A software development environment used for developing Java applications.
* **Jar file**: A Java ARchive file. A package file format used to aggregate many Java class files and associated metadata and resources into one file for distribution.
* **CD**: Command Directory. The current directory in which the command terminal is operating.
