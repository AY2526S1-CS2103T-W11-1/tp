package seedu.address.model;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.event.Consultation;
import seedu.address.model.person.AttendanceStatus;
import seedu.address.model.person.GroupId;
import seedu.address.model.person.Nusnetid;
import seedu.address.model.person.Person;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /** {@code Predicate} that evaluates to true if a person has a consultation scheduled */
    Predicate<Person> PREDICATE_SHOW_PERSONS_WITH_CONSULTATIONS = person -> person.getConsultation().isPresent();

    /** {@code Predicate} that always evaluate to true */
    Predicate<Consultation> PREDICATE_SHOW_ALL_CONSULTATIONS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Person person);

    /**
     * Returns true if a person with the same nusnetid as {@code nusnetid} exists in the address book.
     */
    boolean hasPerson(Nusnetid nusnetid);

    /**
     * Returns the person with the given nusnetid.
     * Returns null if no such person exists.
     */
    Person findPerson(Nusnetid nusnetid);

    /**
     * Return true if a group with the same groupId as {@code groupId} exists in the address book.
     */
    boolean hasGroup(GroupId groupId);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Person person);

    /**
     * Gets a person by their nusnetId.
     * @param nusnetId the nusnetId of the person to be retrieved
     * @return the person with the specified nusnetId
     * @throws CommandException if no person with the given nusnetId exists in the address book
     */
    Person getPersonByNusnetId(Nusnetid nusnetId) throws CommandException;

    /**
     * Retrieves a person by their nusnetId in the Unique Person List
     * @param nusnetId the nusnetId of the person to be retrieved
     * @return the person with the specified nusnetId
     * @throws CommandException if no person with the given nusnetId is found
     */
    Person getPersonByNusnetIdFullList(Nusnetid nusnetId) throws CommandException;
    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void setPerson(Person target, Person editedPerson);

    /**
     * Adds a homework assignment to a student or all students.
     *
     * @param nusnetId the nusnetId of the target student, or "all" to apply to all students
     * @param assignmentId the ID of the assignment to add
     * @throws CommandException if student not found or homework already exists
     */
    void addHomework(Nusnetid nusnetId, int assignmentId) throws CommandException;

    /**
     * Deletes a homework assignment for a student or all students.
     *
     * @param nusnetId the NUSNET ID of the target student, or "all" to apply to all students
     * @param assignmentId the ID of the assignment to delete
     * @throws CommandException if student not found or homework does not exist
     */
    void deleteHomework(Nusnetid nusnetId, int assignmentId) throws CommandException;

    /**
     * Marks a homework assignment for a student with the given status.
     *
     * @param nusnetId the NUSNET ID of the target student
     * @param assignmentId the ID of the assignment to mark
     * @param status the new status ("complete", "incomplete", or "late")
     * @throws CommandException if student not found, assignment does not exist, or status is invalid
     */
    void markHomework(Nusnetid nusnetId, int assignmentId, String status) throws CommandException;

    /**
     * Moves a student to a new group.
     * @param target the person to be moved
     * @param newGroupId the new group ID
     * @throws CommandException if an error occurs during the move
     */
    void moveStudentToNewGroup(Person target, GroupId newGroupId) throws CommandException;
    /**
     * Updates the groups to include the newly added person.
     * @param person the person that was added
     */
    void updateGroupWhenAddPerson(Person person) throws CommandException;
    void updateGroupWhenEditPersonId(Person oldPerson) throws CommandException;

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);
    /**
     * Marks attendance for the person with the given nusnetId for the specified week and status.
     * @param nusnetId the nusnetId of the person whose attendance is to be marked
     * @param week the week number for which attendance is to be marked
     * @param status the attendance status to be marked
     * @return the updated person with the marked attendance
     * @throws CommandException if no person with the given nusnetId exists in the address book
     */
    Person markAttendance(Nusnetid nusnetId, int week, AttendanceStatus status) throws CommandException;
    /**
     * Marks attendance for all persons in the specified group for the given week and status.
     * @param groupId the groupId of the group whose members' attendance is to be marked
     * @param week the week number for which attendance is to be marked
     * @param status the attendance status to be marked
     * @throws CommandException if no group with the given groupId exists in the address book
     */
    void markAllAttendance(GroupId groupId, int week, AttendanceStatus status) throws CommandException;
    /**
     * Returns true if a consultation equivalent to {@code consultation} exists in the address book.
     */
    boolean hasConsultation(Consultation consultation);

    /**
     * Returns true if a consultation that overlaps with {@code consultation} exists in the address book.
     */
    boolean hasOverlappingConsultation(Consultation consultation);

    /**
     * Adds the given consultation.
     * {@code consultation} must not already exist in the address book.
     */
    void addConsultation(Consultation consultation);

    /**
     * Deletes the given consultation.
     * The {@code consultation} must exist in the address book.
     */
    void deleteConsultation(Consultation consultation);

    /** Returns an unmodifiable view of the filtered consultation list */
    ObservableList<Consultation> getFilteredConsultationList();

    /**
     * Updates the filter of the filtered consultation list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredConsultationList(Predicate<Consultation> predicate);

    /**
     * Adds the given consultation to the person identified by the given nusnetid.
     */
    void addConsultationToPerson(Nusnetid nusnetid, Consultation consultation);

    /**
     * Deletes the consultation from the person identified by the given nusnetid.
     * @return the consultation that was deleted
     */
    Consultation deleteConsultationFromPerson(Nusnetid nusnetid);

    /**
     * Adds a group to the model.
     */
    void addGroup(Group group);

    /**
     * Returns the list of groups in the model.
     */
    List<Group> getGroupList();

    /**
     * Gets a group by its groupId.
     * @param groupId the groupId of the group to be retrieved
     * @return the group with the specified groupId
     */
    Group getGroup(GroupId groupId);

    /**
     * Updates consultations stored in the address book when a person's nusnetid changes.
     * The implementation should update any Consultation objects that reference the old nusnetid to use the new one.
     */
    void updateConsultationsForEditedPerson(Nusnetid oldNusnetid, Nusnetid newNusnetid);
}
