package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Group;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.event.Consultation;
import seedu.address.model.person.GroupId;
import seedu.address.model.person.Nusnetid;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class AddToGroupCommandTest {
    @Test
    public void constructor_nullGroup_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddToGroupCommand(null, null));
        assertThrows(NullPointerException.class, () -> new AddToGroupCommand(new Nusnetid("E1234567"), null));
        assertThrows(NullPointerException.class, () -> new AddToGroupCommand(null, new GroupId("T01")));
    }
    @Test
    public void execute_nullGroup_throwsNullPointerException() {
        AddToGroupCommand command = new AddToGroupCommand(new Nusnetid("E1234567"), new GroupId("T01"));
        assertThrows(NullPointerException.class, () -> command.execute(null));
    }
    @Test
    public void execute_groupDoesNotExist_createNewGroup() throws Exception {
        ModelStubWithGroup modelStub = new ModelStubWithGroup(
                new Group(new GroupId("T01"), List.of()));
        // Ensure that group T02 does not exist
        assertFalse(modelStub.hasGroup(new GroupId("T02")));
        AddToGroupCommand command = new AddToGroupCommand(new Nusnetid("E1234567"), new GroupId("T02"));
        command.execute(modelStub);
        List<Group> groupsAfter = modelStub.getGroupList();
        assertEquals(3, groupsAfter.size());
        assert groupsAfter.stream().anyMatch(g -> g.getGroupId().equals(new GroupId("T02")));
    }
    @Test
    public void execute_validGroup_addStudentToGroup() throws Exception {
        GroupId targetGroupId = new GroupId("B01");
        Nusnetid targetNusnetid = new Nusnetid("E1234567");
        ModelStubWithGroup modelStub = new ModelStubWithGroup();
        AddToGroupCommand command = new AddToGroupCommand(targetNusnetid, targetGroupId);
        command.execute(modelStub);
        Group targetGroup = modelStub.getGroup(targetGroupId);
        assert targetGroup != null;
        assert targetGroup.hasStudent(targetNusnetid);
        assert modelStub.hasGroup(targetGroupId);
    }
    @Test
    public void execute_sameGroup_throwsCommandException() throws Exception {
        GroupId targetGroupId = new GroupId("T01");
        Nusnetid targetNusnetid = new Nusnetid("E1234567");
        ModelStubWithGroup modelStub = new ModelStubWithGroup();
        AddToGroupCommand command = new AddToGroupCommand(targetNusnetid, targetGroupId);
        assertThrows(CommandException.class, AddToGroupCommand.MESSAGE_SAME_GROUP_FAIL, ()
                -> command.execute(modelStub));
    }

    /**
     * A default model stub that have all the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public boolean hasPerson(Nusnetid nusnetid) {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public Person findPerson(Nusnetid nusnetid) {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addHomework(Nusnetid nusnetId, int assignmentId) throws CommandException {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteHomework(Nusnetid nusnetId, int assignmentId) throws CommandException {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public void markHomework(Nusnetid nusnetId, int assignmentId, String status) throws CommandException {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public Person getPersonByNusnetIdFullList(Nusnetid nusnetId) throws CommandException {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateGroupWhenEditPersonId(Person oldPerson) {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public Person markAttendance(Nusnetid nusnetid, int week, seedu.address.model.person.AttendanceStatus status)
                throws CommandException {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public void markAllAttendance(GroupId groupId, int week, seedu.address.model.person.AttendanceStatus status) {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public void addConsultationToPerson(Nusnetid nusnetid, Consultation consultation) {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public Consultation deleteConsultationFromPerson(Nusnetid nusnetid) {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public List<Group> getGroupList() {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public void addGroup(Group group) {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public boolean hasConsultation(Consultation consultation) {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public boolean hasOverlappingConsultation(Consultation consultation) {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public void addConsultation(Consultation consultation) {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public void deleteConsultation(Consultation consultation) {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public ObservableList<Consultation> getFilteredConsultationList() {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public void updateFilteredConsultationList(Predicate<Consultation> predicate) {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public Group getGroup(GroupId groupId) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateConsultationsForEditedPerson(Nusnetid oldNusnetid, Nusnetid newNusnetid) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasGroup(GroupId groupId) {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public Person getPersonByNusnetId(Nusnetid nusnetid) throws CommandException {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public void updateGroupWhenAddPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public void moveStudentToNewGroup(Person student, GroupId newGroupId) throws CommandException {
            throw new AssertionError("This method should not be called.");
        }
    }
    private class ModelStubWithGroup extends ModelStub {
        private static final String MESSAGE_STUDENT_NOT_FOUND = "Student not found.";
        private final AddressBook addressBook;
        private final FilteredList<Person> filteredPersonList;
        /**
         * Creates a ModelStubWithGroup that contains group {@code T01} and {@code B01} already.
         * Also, a test user with nus net id E1234567.
         * @param group the group to be contained in the model stub.
         *              This will be added to the address book, if not already existing.
         */
        ModelStubWithGroup(Group group) {
            requireNonNull(group);
            this.addressBook = new AddressBook();
            this.addressBook.addGroup(group);
            // With default group T01
            Person person = new PersonBuilder()
                    .withNusnetid("E1234567")
                    .withName("Test User")
                    .build();
            this.addressBook.addPerson(person);
            this.addressBook.addGroup(new Group(new GroupId("B01")));
            this.filteredPersonList = new FilteredList<>(this.addressBook.getPersonList());
        }
        /**
         * Constructs a ModelStubWithGroup with default groups T01 and B01.
         */
        ModelStubWithGroup() {
            this.addressBook = new AddressBook();
            // With default group T01
            Person person = new PersonBuilder()
                    .withNusnetid("E1234567")
                    .withName("Test User")
                    .build();
            this.addressBook.addPerson(person);
            this.addressBook.addGroup(new Group(new GroupId("B01")));
            this.filteredPersonList = new FilteredList<>(this.addressBook.getPersonList());
        }
        /**
         * The group in this model stub.
         * @param groupId the group ID to be checked
         * @return true if the group ID matches the group in this model stub
         */
        @Override
        public boolean hasGroup(GroupId groupId) {
            requireNonNull(groupId);
            return this.addressBook.hasGroup(groupId);
        }
        /**
         * Returns the group in this model stub.
         * @param groupId the groupId of the group to be retrieved
         * @return the group with the given groupId
         */
        @Override
        public Group getGroup(GroupId groupId) {
            requireNonNull(groupId);
            return this.addressBook.getGroup(groupId);
        }
        @Override
        public boolean hasPerson(Nusnetid nusnetid) {
            return true;
        }
        @Override
        public Person getPersonByNusnetId(Nusnetid nusnetId) throws CommandException {
            requireNonNull(nusnetId);
            assert hasPerson(nusnetId) : "Person with given nus net Id should exist in the address book.";
            Person target = this.getFilteredPersonList()
                    .stream().filter(p -> p.getNusnetid().equals(nusnetId))
                    .findFirst().orElseThrow(() -> new CommandException(MESSAGE_STUDENT_NOT_FOUND));
            return target;
        }
        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return this.filteredPersonList;
        }
        @Override
        public void setPerson(Person target, Person editedPerson) {
            requireNonNull(target);
            requireNonNull(editedPerson);
            this.addressBook.setPerson(target, editedPerson);
        }
        @Override
        public void addGroup(Group group) {
            requireNonNull(group);
            this.addressBook.addGroup(group);
        }
        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            requireNonNull(predicate);
            this.filteredPersonList.setPredicate(predicate);
        }
        @Override
        public ObservableList<Group> getGroupList() {
            return this.addressBook.getGroupList();
        }
        @Override
        public void moveStudentToNewGroup(Person student, GroupId newGroupId) throws CommandException {
            requireAllNonNull(student, newGroupId);
            this.addressBook.moveStudentToNewGroup(student, newGroupId);
        }
        @Override
        public Person getPersonByNusnetIdFullList(Nusnetid nusnetId) throws CommandException {
            requireNonNull(nusnetId);
            assert hasPerson(nusnetId) : "Person with given nusnetId should exist in the address book.";
            Person target = this.addressBook.getUniquePersonList()
                    .stream().filter(p -> p.getNusnetid().equals(nusnetId))
                    .findFirst().orElseThrow(() -> new CommandException(MESSAGE_STUDENT_NOT_FOUND));
            return target;
        }
    }
}
