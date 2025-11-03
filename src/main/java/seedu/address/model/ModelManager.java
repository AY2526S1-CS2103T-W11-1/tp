package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.AddHomeworkCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.event.Consultation;
import seedu.address.model.person.Attendance;
import seedu.address.model.person.AttendanceSheet;
import seedu.address.model.person.AttendanceStatus;
import seedu.address.model.person.GroupId;
import seedu.address.model.person.HomeworkTracker;
import seedu.address.model.person.Nusnetid;
import seedu.address.model.person.Person;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);
    private static final String MESSAGE_STUDENT_NOT_FOUND = "Student not found.";
    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final FilteredList<Consultation> filteredConsultations;
    private final SortedList<Consultation> sortedConsultations;
    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        filteredConsultations = new FilteredList<>(this.addressBook.getConsultationList());
        sortedConsultations = new SortedList<>(filteredConsultations);
        // Set comparator to sort by start time
        sortedConsultations.setComparator(Comparator.comparing(Consultation::getFrom));
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public boolean hasPerson(Nusnetid nusnetid) {
        requireNonNull(nusnetid);
        return addressBook.hasPerson(nusnetid);
    }

    @Override
    public Person findPerson(Nusnetid nusnetid) {
        requireNonNull(nusnetid);
        return addressBook.getPerson(nusnetid);
    }

    @Override
    public boolean hasGroup(GroupId groupId) {
        requireNonNull(groupId);
        return addressBook.hasGroup(groupId);
    }

    @Override
    public void deletePerson(Person target) {
        addressBook.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);
        addressBook.setPerson(target, editedPerson);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }
    /**
     * Updates the group information when a person is added.
     * @param person the person that was added
     * @throws CommandException if duplicate person exception occurs
     */
    @Override
    public void updateGroupWhenAddPerson(Person person) {
        requireNonNull(person);
        this.addressBook.updateGroupWhenAddPerson(person);
    }
    @Override
    public void updateGroupWhenEditPersonId(Person oldPerson) {
        requireAllNonNull(oldPerson);
        this.addressBook.updateGroupWhenEditPerson(oldPerson);
    }

    @Override
    public void updateConsultationsForEditedPerson(Nusnetid oldNusnetid, Nusnetid newNusnetid) {
        requireAllNonNull(oldNusnetid, newNusnetid);
        this.addressBook.updateConsultationsForEditedPerson(oldNusnetid, newNusnetid);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }
    /**
     * Marks attendance for a student identified by their nusnetId.
     * @param nusnetId the nusnetId of the student
     * @param week the week number for which attendance is to be marked
     * @param status the attendance status to be marked
     * @return the updated Person object with modified attendance
     * @throws CommandException if the student with the given nusnetId is not found
     */
    @Override
    public Person markAttendance(Nusnetid nusnetId, int week, AttendanceStatus status) throws CommandException {
        requireAllNonNull(nusnetId, status);
        Person targetStudent = findPerson(nusnetId);
        AttendanceSheet updatedSheet = new AttendanceSheet();
        for (Attendance attendance : targetStudent.getAttendanceSheet().getAttendanceList()) {
            updatedSheet.markAttendance(attendance.getWeek(), attendance.getAttendanceStatus());
        }
        updatedSheet.markAttendance(week, status);
        Person updatedStudent = new Person(
               targetStudent.getName(),
               targetStudent.getPhone(),
               targetStudent.getEmail(),
               targetStudent.getNusnetid(),
               targetStudent.getTelegram(),
               targetStudent.getGroupId(),
               targetStudent.getHomeworkTracker(),
               updatedSheet,
               targetStudent.getConsultation());

        setPerson(targetStudent, updatedStudent);
        this.getGroup(targetStudent.getGroupId()).setPerson(targetStudent, updatedStudent);
        Predicate<Person> predicate = person -> true;
        updateFilteredPersonList(predicate);
        return updatedStudent;
    }
    /**
     * Marks attendance for all students in a group for a specific week and status.
     * @param groupId the ID of the group
     * @param week the week number for which attendance is to be marked
     * @param status the attendance status to be marked
     * @throws CommandException if the group with the given groupId is not found
     */
    @Override
    public void markAllAttendance(GroupId groupId, int week, AttendanceStatus status) throws CommandException {
        requireAllNonNull(groupId, status);
        Group targetGroup = getGroup(groupId);
        ArrayList<Person> studentsInGroup = targetGroup.getAllPersons();
        if (studentsInGroup.isEmpty()) {
            throw new CommandException("No students in the group.");
        }
        for (Person targetStudent: studentsInGroup) {
            AttendanceSheet updatedSheet = new AttendanceSheet();
            for (Attendance attendance : targetStudent.getAttendanceSheet().getAttendanceList()) {
                updatedSheet.markAttendance(attendance.getWeek(), attendance.getAttendanceStatus());
            }
            updatedSheet.markAttendance(week, status);
            Person updatedStudent = new Person(
                    targetStudent.getName(),
                    targetStudent.getPhone(),
                    targetStudent.getEmail(),
                    targetStudent.getNusnetid(),
                    targetStudent.getTelegram(),
                    targetStudent.getGroupId(),
                    targetStudent.getHomeworkTracker(),
                    updatedSheet,
                    targetStudent.getConsultation());

            setPerson(targetStudent, updatedStudent);
            targetGroup.setPerson(targetStudent, updatedStudent);
        }

        Predicate<Person> predicate = person -> person.getGroupId().equals(groupId);
        updateFilteredPersonList(predicate);
    }
    /**
     * Retrieves a person by their nusnetId.
     * @param nusnetId the nusnetId of the person to be retrieved
     * @return the person with the specified nusnetId
     * @throws CommandException if no person with the given nusnetId is found
     */
    @Override
    public Person getPersonByNusnetId(Nusnetid nusnetId) throws CommandException {
        requireNonNull(nusnetId);
        assert hasPerson(nusnetId) : "Person with given nusnetId should exist in the address book.";
        Person target = this.addressBook.getPerson(nusnetId);
        return target;
    }
    /**
     * Retrieves a person by their nusnetId in the Unique Person List
     * @param nusnetId the nusnetId of the person to be retrieved
     * @return the person with the specified nusnetId
     * @throws CommandException if no person with the given nusnetId is found
     */
    @Override
    public Person getPersonByNusnetIdFullList(Nusnetid nusnetId) throws CommandException {
        requireNonNull(nusnetId);
        if (hasPerson(nusnetId)) {
            Person target = this.addressBook.getUniquePersonList()
                    .stream().filter(p -> p.getNusnetid().equals(nusnetId))
                    .findFirst().orElseThrow(() -> new CommandException(MESSAGE_STUDENT_NOT_FOUND));
            return target;
        } else {
            throw new CommandException(MESSAGE_STUDENT_NOT_FOUND);
        }
    }
    @Override
    public boolean hasConsultation(Consultation consultation) {
        requireNonNull(consultation);
        return addressBook.hasConsultation(consultation);
    }

    @Override
    public boolean hasOverlappingConsultation(Consultation consultation) {
        requireNonNull(consultation);
        return addressBook.hasOverlappingConsultation(consultation);
    }

    @Override
    public void addConsultation(Consultation consultation) {
        addressBook.addConsultation(consultation);
        updateFilteredConsultationList(PREDICATE_SHOW_ALL_CONSULTATIONS);
    }

    @Override
    public void deleteConsultation(Consultation consultation) {
        addressBook.deleteConsultation(consultation);
        updateFilteredConsultationList(PREDICATE_SHOW_ALL_CONSULTATIONS);
    }

    @Override
    public void addConsultationToPerson(Nusnetid nusnetid, Consultation consultation) {
        requireAllNonNull(nusnetid, consultation);
        addressBook.addConsultationToPerson(nusnetid, consultation);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public Consultation deleteConsultationFromPerson(Nusnetid nusnetid) {
        requireAllNonNull(nusnetid);
        Consultation deletedConsultation = addressBook.deleteConsultationFromPerson(nusnetid);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return deletedConsultation;
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public void addHomework(Nusnetid nusnetId, int assignmentId) throws CommandException {
        List<String> errors = new ArrayList<>();
        if (assignmentId < 1 || assignmentId > 13) {
            errors.add("Homework ID must be between 1 and 13.");
        }

        if (nusnetId != null) {
            if (!hasPerson(nusnetId)) {
                errors.add(String.format("Student with NUSNET ID %s does not exist.", nusnetId.value));
            }
        }
        if (!errors.isEmpty()) {
            throw new CommandException(String.join(" ", errors));
        }

        if (nusnetId == null) {
            List<Person> allStudents = addressBook.getUniquePersonList();
            boolean allHaveHomework = true;

            // Check if every student already has this homework
            for (Person p : allStudents) {
                if (!p.getHomeworkTracker().contains(assignmentId)) {
                    allHaveHomework = false;
                    break;
                }
            }

            if (allHaveHomework) {
                throw new CommandException(
                        String.format("All students already have homework %d.", assignmentId)
                );
            }

            // Otherwise, add homework for everyone (even those who already have it)
            for (Person p : allStudents) {
                setPerson(p, p.withAddedHomework(assignmentId));
            }

            return;
        }


        Person target;
        try {
            target = getPersonByNusnetIdFullList(nusnetId); // may throw AssertionError currently
        } catch (AssertionError e) {
            throw new CommandException(AddHomeworkCommand.MESSAGE_STUDENT_NOT_FOUND);
        }
        if (target.getHomeworkTracker().contains(assignmentId)) {
            throw new CommandException(
                    String.format("Homework %d already exists for %s.", assignmentId, target.getName())
            );
        }

        setPerson(target, target.withAddedHomework(assignmentId));
    }

    @Override
    public void deleteHomework(Nusnetid nusnetId, int assignmentId) throws CommandException {
        if (nusnetId == null) {
            // delete homework for all students
            boolean allDontHave = true;
            for (Person p : addressBook.getUniquePersonList()) {
                if (p.getHomeworkTracker().contains(assignmentId)) {
                    allDontHave = false;
                    break;
                }
            }
            if (allDontHave) {
                throw new CommandException(String.format("No student has homework %d.", assignmentId));
            }
            for (Person p : addressBook.getUniquePersonList()) {
                if (p.getHomeworkTracker().contains(assignmentId)) {
                    setPerson(p, p.withDeletedHomework(assignmentId));
                }
            }
            return;
        }

        // single student
        Person target;
        try {
            target = getPersonByNusnetIdFullList(nusnetId); // may throw AssertionError currently
        } catch (AssertionError e) {
            throw new CommandException(AddHomeworkCommand.MESSAGE_STUDENT_NOT_FOUND);
        }

        if (!target.getHomeworkTracker().contains(assignmentId)) {
            throw new CommandException(
                    String.format("Homework %d not found for %s.", assignmentId, target.getName())
            );
        }

        setPerson(target, target.withDeletedHomework(assignmentId));
    }


    @Override
    public void markHomework(Nusnetid nusnetId, int assignmentId, String status) throws CommandException {
        requireNonNull(nusnetId);
        requireNonNull(status);

        Person target;
        try {
            target = getPersonByNusnetIdFullList(nusnetId); // may throw AssertionError currently
        } catch (AssertionError e) {
            throw new CommandException(AddHomeworkCommand.MESSAGE_STUDENT_NOT_FOUND);
        }

        if (!HomeworkTracker.isValidAssignmentId(assignmentId)) {
            throw new CommandException("Invalid assignment ID.");
        }
        if (!HomeworkTracker.isValidStatus(status)) {
            throw new CommandException("Invalid status: use complete/incomplete/late.");
        }
        if (!target.getHomeworkTracker().hasAssignment(assignmentId)) {
            throw new CommandException(
                    String.format("Homework %d not found for %s. Add it first using 'add_hw'.",
                            assignmentId, target.getName().fullName));
        }

        Person updatedPerson = target.withUpdatedHomework(assignmentId, status);
        setPerson(target, updatedPerson);
        this.getGroup(target.getGroupId()).setPerson(target, updatedPerson);
    }

    /**
     * Adds a group to the model.
     * @param group the group to be added
     */
    @Override
    public void addGroup(Group group) {
        requireNonNull(group);
        addressBook.addGroup(group);
    }

    @Override
    public List<Group> getGroupList() {
        return addressBook.getGroupList();
    }

    //=========== Filtered Consultation List Accessors =============================================================
    /**
     * Returns an unmodifiable view of the list of {@code Consultation} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Consultation> getFilteredConsultationList() {
        return sortedConsultations;
    }

    @Override
    public void updateFilteredConsultationList(Predicate<Consultation> predicate) {
        requireNonNull(predicate);
        filteredConsultations.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager otherModelManager)) {
            return false;
        }

        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons);
    }

    @Override
    public Group getGroup(GroupId groupId) {
        requireNonNull(groupId);
        return addressBook.getGroup(groupId);
    }

    @Override
    public void moveStudentToNewGroup(Person student, GroupId newGroupId) throws CommandException {
        requireAllNonNull(student, newGroupId);
        this.addressBook.moveStudentToNewGroup(student, newGroupId);
    }
}
