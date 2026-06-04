# SoCTAssist — Project Harness

## Project Overview

**SoCTAssist** (CTA) is a desktop application for NUS School of Computing TAs to manage student information, attendance, homework, and consultations. Built on AddressBook-Level3 (se-edu), it uses JavaFX for GUI, Gradle for builds, and targets Java 17.

- **Version**: 0.2.2
- **Main Class**: `seedu.address.Main`
- **Package Root**: `seedu.address`
- **Build Output**: `soctassist.jar`
- **GitHub Org**: `AY2526S1-CS2103T-W11-1`

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 17 |
| GUI | JavaFX 17.0.7 (FXML) |
| Build | Gradle 7.x + Shadow JAR |
| Testing | JUnit 5.4.0, TestFX (GUI tests) |
| Code Quality | Checkstyle 11.0.0, JaCoCo |
| Serialization | Jackson 2.7.0 |
| CI | GitHub Actions (Gradle + Docs) |

---

## Build & Run Commands

```bash
# Build and run all tests (default task)
./gradlew

# Clean + test
./gradlew clean test

# Build shadow JAR
./gradlew shadowJar
# Output: build/libs/soctassist.jar

# Run the application
./gradlew run
# or
java -jar build/libs/soctassist.jar

# Run with assertions
./gradlew run  # assertions enabled in build.gradle

# Run specific test class
./gradlew test --tests "seedu.address.logic.commands.AddCommandTest"

# Run with coverage report
./gradlew check coverage
# Output: build/reports/jacoco/coverage/html/index.html

# Checkstyle
./gradlew checkstyleMain checkstyleTest

# Verify Gradle wrapper (CI)
./gradlew wrapper --gradle-version=7.6
```

---

## Project Structure

```
src/
├── main/
│   ├── java/seedu/address/
│   │   ├── Main.java                          # Entry point
│   │   ├── MainApp.java                       # JavaFX Application init
│   │   ├── AppParameters.java
│   │   ├── commons/                           # Shared utilities
│   │   │   ├── core/                          # Config, GuiSettings, LogsCenter, Version
│   │   │   ├── exceptions/                    # DataLoadingException, IllegalValueException
│   │   │   └── util/                          # AppUtil, CollectionUtil, ConfigUtil, FileUtil, JsonUtil, StringUtil, ToStringBuilder
│   │   ├── logic/                             # Command parsing & execution
│   │   │   ├── Logic.java                     # API interface
│   │   │   ├── LogicManager.java              # Implementation: parse → execute → save
│   │   │   ├── Messages.java                  # User feedback formatting
│   │   │   ├── commands/                      # 19 command classes (see below)
│   │   │   └── parser/                        # AddressBookParser + per-command parsers
│   │   ├── model/                             # Domain data
│   │   │   ├── Model.java                     # API interface
│   │   │   ├── ModelManager.java              # Implementation
│   │   │   ├── AddressBook.java               # Top-level data container
│   │   │   ├── ReadOnlyAddressBook.java
│   │   │   ├── person/                        # Person, UniquePersonList, fields (Name, NusnetId, Telegram, etc.)
│   │   │   ├── group/                         # Group, UniqueGroupList, GroupId
│   │   │   ├── consultation/                  # Consultation, DateTimeSlot
│   │   │   ├── homework/                      # HomeworkTracker, Homework, HomeworkStatus
│   │   │   ├── attendance/                    # AttendanceTracker, Attendance, AttendanceStatus
│   │   │   └── tag/                           # Tag (legacy)
│   │   ├── storage/                           # JSON persistence
│   │   │   ├── Storage.java, StorageManager.java
│   │   │   ├── AddressBookStorage.java, JsonAddressBookStorage.java
│   │   │   ├── JsonAdaptedPerson.java, JsonAdaptedGroup.java, JsonAdaptedConsultation.java
│   │   │   ├── JsonSerializableAddressBook.java
│   │   │   └── UserPrefsStorage.java, JsonUserPrefsStorage.java
│   │   └── ui/                                # JavaFX UI components
│   │       ├── Ui.java, UiManager.java
│   │       ├── MainWindow.java
│   │       ├── CommandBox.java, ResultDisplay.java
│   │       ├── PersonListPanel.java, PersonCard.java
│   │       ├── ConsultationListPanel.java, ConsultationCard.java
│   │       ├── StatusBarFooter.java, HelpWindow.java
│   │       └── UiPart.java
│   └── resources/view/                        # FXML layouts + CSS
└── test/
    ├── java/seedu/address/                    # Mirror of main structure
    │   ├── logic/                             # LogicManagerTest, command tests, parser tests
    │   ├── model/                             # ModelManagerTest, AddressBookTest, field tests
    │   ├── storage/                           # StorageManagerTest, JsonAdapted* tests
    │   ├── ui/                                # GUI test stubs (GuiUnitTest, TestFxmlObject)
    │   └── testutil/                          # Assert, SerializableTestClass, TestUtil, TypicalPersons, TypicalAddressBook
    └── data/                                  # JSON test fixtures (18 files)
```

---

## Commands Reference

| Command | Format | Description |
|---------|--------|-------------|
| `help` | `help` | Show help message |
| `list` | `list` | List all students |
| `list_consult` | `list_consult` | List all consultations |
| `add_student` | `add_student n/NAME i/NUSNETID t/TELEGRAM g/GROUPID [p/PHONE] [e/EMAIL]` | Add a student |
| `edit_student` | `edit_student INDEX [n/NAME] [i/NUSNETID] [t/TELEGRAM] [p/PHONE] [e/EMAIL]` | Edit a student |
| `delete` | `delete INDEX` | Delete a student by index |
| `find` | `find KEYWORD [MORE_KEYWORDS]` | Find students by name (case-insensitive, OR, full-word) |
| `add_hw` | `add_hw i/NUSNETID a/ASSIGNMENT` | Add homework (1-13). Use `i/all` for all students |
| `mark_hw` | `mark_hw i/NUSNETID a/ASSIGNMENT status/STATUS` | Mark homework (complete/incomplete/late) |
| `delete_hw` | `delete_hw i/NUSNETID a/ASSIGNMENT` | Delete homework. Use `i/all` for all students |
| `mark_attendance` | `mark_attendance i/NUSNETID w/WEEK status/STATUS` | Mark attendance (week 2-13, present/absent/excused) |
| `mark_all_attendance` | `mark_all_attendance g/GROUPID w/WEEK status/STATUS` | Mark attendance for a group |
| `add_consult` | `add_consult i/NUSNETID from/YYYYMMDD HHmm to/YYYYMMDD HHmm` | Add consultation (1 per student, no overlap) |
| `delete_consult` | `delete_consult i/NUSNETID` | Delete a student's consultation |
| `create_group` | `create_group g/GROUPID` | Create a new group (Txx/Bxx) |
| `add_to_group` | `add_to_group i/NUSNETID g/GROUPID` | Move student to group (creates group if not exists) |
| `find_group` | `find_group g/GROUPID` | Find students by group |
| `clear` | `clear` | Clear all data |
| `exit` | `exit` | Exit application |

---

## Data Model

### Person
- **Required**: Name, NusnetId (E + 7 digits), Telegram (@ + alphanumeric), GroupId (T/B + 2 digits)
- **Optional**: Phone (3-30 digits, can start with +), Email (must be @u.nus.edu)
- **Embedded**: HomeworkTracker, AttendanceTracker
- **Unique constraints**: NusnetId, Telegram, Phone, Email must each be unique across all persons

### Group
- GroupId (T/B + exactly 2 digits), must be unique
- Auto-created when students are added via `add_to_group`

### Consultation
- Linked to a Person (one-to-one)
- DateTimeSlot: start + end time (yyyyMMdd HHmm format)
- No overlap allowed across all consultations
- One consultation per student

### Homework
- Identified by assignment number (1-13)
- Status: incomplete (default), complete, late
- Stored per student in HomeworkTracker

### Attendance
- Week number: 2-13
- Status: present, absent, excused
- Stored per student in AttendanceTracker

---

## Architecture

**MVC Pattern** with 4 main components:

1. **UI** (`Ui` interface → `UiManager`): JavaFX, FXML-based, observes Model data
2. **Logic** (`Logic` interface → `LogicManager`): Parses commands via `AddressBookParser`, executes `Command` objects
3. **Model** (`Model` interface → `ModelManager`): Holds `AddressBook` data, exposes filtered lists
4. **Storage** (`Storage` interface → `StorageManager`): JSON read/write for addressbook + user prefs

**Data flow**: UI → LogicManager → AddressBookParser → Command → Model → Storage → auto-save

---

## Testing Conventions

- Test package mirrors source: `seedu.address.*`
- Test utilities in `testutil/`: `TypicalPersons`, `TypicalAddressBook`, `Assert`, `TestUtil`
- JSON test fixtures in `test/data/`
- GUI tests use TestFX
- Model stubs in `model/stubs/` for unit testing commands
- Coverage target: `./gradlew coverage` generates JaCoCo reports

---

## Key Constraints & Validation Rules

| Field | Rule |
|-------|------|
| Name | Letters (incl. accents), digits, spaces, quotes; no `/`; max 70 chars; not blank |
| NUSNET ID | `E` + 7 digits (case-insensitive E) |
| Telegram | `@` + alphanumeric + underscores, at least 1 char after `@` |
| Phone | 3-30 digits, optional `+` prefix |
| Email | Must match `*@u.nus.edu` format |
| Group ID | `T` or `B` (case-insensitive) + exactly 2 digits |
| Homework number | Integer 1-13 |
| Homework status | `complete`, `incomplete`, `late` |
| Attendance week | Integer 2-13 |
| Attendance status | `present`, `absent`, `excused` |
| Date/Time | `yyyyMMdd HHmm` format |
| Index | Positive integer |

---

## CI/CD

- **Java CI** (`.github/workflows/gradle.yml`): Runs on push/PR across ubuntu/macos/windows; validates Gradle wrapper, builds, runs tests + coverage, uploads to Codecov
- **Docs CI** (`.github/workflows/docs.yml`): Deploys MarkBind site on push to master

---

## Common Development Patterns

1. **Adding a new command**: Create `XYZCommand extends Command`, `XYZCommandParser implements Parser<XYZCommand>`, register in `AddressBookParser`, add test class
2. **Adding a new model field**: Add field class in `model/person/`, update `Person`, `JsonAdaptedPerson`, FXML, `AddCommandParser`/`EditCommandParser`, CLI prefixes in `CliSyntax`
3. **Auto-save**: Every mutating command triggers `storage.saveAddressBook(model.getAddressBook())`
4. **Duplicate checking**: Person uniqueness checked on NusnetId, Telegram, Phone, Email
5. **Consultation overlap**: Checked across all persons' consultations in `ModelManager`
