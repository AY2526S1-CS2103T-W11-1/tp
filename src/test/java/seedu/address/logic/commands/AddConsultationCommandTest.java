package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
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

public class AddConsultationCommandTest {

    @Test
    public void constructor_nullConsultation_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddConsultationCommand(null));
    }

    @Test
    public void execute_consultationAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingConsultationAdded modelStub = new ModelStubAcceptingConsultationAdded();
        Person validPerson = new PersonBuilder().withNusnetid(BENSON.getNusnetid().value).build();
        modelStub.addPerson(validPerson);
        Consultation validConsultation = new Consultation(BENSON.getNusnetid(),
                BENSON.getConsultation().get().getFrom(), BENSON.getConsultation().get().getTo());

        CommandResult commandResult = new AddConsultationCommand(validConsultation).execute(modelStub);

        assertEquals(String.format(AddConsultationCommand.MESSAGE_SUCCESS, Messages.format(validConsultation)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validConsultation), modelStub.filteredConsultations);
    }

    @Test
    public void execute_personDoesNotExist_throwsCommandException() {
        Consultation validConsultation = BENSON.getConsultation().get();
        AddConsultationCommand addConsultationCommand = new AddConsultationCommand(validConsultation);
        ModelStub modelStub = new ModelStubWithoutPersons();

        assertThrows(CommandException.class,
                AddConsultationCommand.MESSAGE_STUDENT_DOES_NOT_EXIST, () -> addConsultationCommand.execute(modelStub));
    }

    @SuppressWarnings("checkstyle:SeparatorWrap")
    @Test
    public void execute_overlappingConsultation_throwsCommandException() throws ParseException {
        Consultation validConsultation = BENSON.getConsultation().get();
        AddConsultationCommand addConsultationCommand = new AddConsultationCommand(validConsultation);
        ModelStub modelStub = new ModelStubWithConsultation(validConsultation);

        assertThrows(CommandException.class, AddConsultationCommand.MESSAGE_OVERLAPPING_CONSULTATION,
                () -> addConsultationCommand.execute(modelStub));
    }

    @SuppressWarnings("checkstyle:SeparatorWrap")
    @Test
    public void execute_personHasConsultation_throwsCommandException() {
        Person validPerson = BENSON;
        Consultation validConsultation = BENSON.getConsultation().get();
        AddConsultationCommand addConsultationCommand = new AddConsultationCommand(validConsultation);
        ModelStub modelStub = new ModelStubWithPerson(validPerson);

        assertThrows(CommandException.class, AddConsultationCommand.MESSAGE_STUDENT_ALREADY_HAS_CONSULTATION,
                () -> addConsultationCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        AddConsultationCommand addAliceConsultationCommand = new AddConsultationCommand(ALICE.getConsultation().get());
        AddConsultationCommand addBensonConsultationCommand =
                new AddConsultationCommand(BENSON.getConsultation().get());

        // same object -> returns true
        assertTrue(addAliceConsultationCommand.equals(addAliceConsultationCommand));

        // same values -> returns true
        AddConsultationCommand addAliceConsultationCommandCopy =
                new AddConsultationCommand(ALICE.getConsultation().get());
        assertTrue(addAliceConsultationCommand.equals(addAliceConsultationCommandCopy));

        // different types -> returns false
        assertFalse(addAliceConsultationCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceConsultationCommand.equals(null));

        // different person -> returns false
        assertFalse(addAliceConsultationCommand.equals(addBensonConsultationCommand));
    }

    @Test
    public void toStringMethod() {
        AddConsultationCommand addConsultationCommand = new AddConsultationCommand(ALICE.getConsultation().get());
        String expected = AddConsultationCommand.class.getCanonicalName()
                + "{toAdd=" + ALICE.getConsultation().get() + "}";
        assertEquals(expected, addConsultationCommand.toString());
    }

    /**
     * A default model stub that have all of the methods failing.
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
        public void updateGroupWhenEditPersonId(Person oldPerson) throws CommandException {
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
        public void addConsultationToPerson(Nusnetid nusnetid, Consultation consultation) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Consultation deleteConsultationFromPerson(Nusnetid nusnetid) {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public void updateGroupWhenAddPerson(Person person) throws CommandException {
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
        public Person getPersonByNusnetId(Nusnetid nusnetid) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that does not contain any persons.
     */
    private class ModelStubWithoutPersons extends ModelStub {
        private final AddressBook addressBook = new AddressBook();

        @Override
        public boolean hasPerson(Nusnetid nusnetid) {
            requireNonNull(nusnetid);
            return addressBook.hasPerson(nusnetid);
        }
    }

    /**
     * A Model stub that contains a person.
     */
    private class ModelStubWithPerson extends ModelStub {
        private final AddressBook addressBook = new AddressBook();

        ModelStubWithPerson(Person person) {
            requireNonNull(person);
            this.addPerson(person);
        }

        @Override
        public boolean hasPerson(Nusnetid nusnetid) {
            requireNonNull(nusnetid);
            return addressBook.hasPerson(nusnetid);
        }

        @Override
        public void addPerson(Person person) {
            addressBook.addPerson(person);
            // No need to update filtered person list for this test stub
        }

        @Override
        public boolean hasConsultation(Consultation consultation) {
            requireNonNull(consultation);
            return false; // No duplicate consultations
        }

        @Override
        public boolean hasOverlappingConsultation(Consultation consultation) {
            requireNonNull(consultation);
            return false; // No overlapping consultations
        }

        @Override
        public void addConsultationToPerson(Nusnetid nusnetid, Consultation consultation) {
            requireAllNonNull(nusnetid, consultation);
            addressBook.addConsultationToPerson(nusnetid, consultation);
        }
    }

    /**
     * A Model stub that contains a single consultation.
     */
    private class ModelStubWithConsultation extends ModelStub {
        private final Consultation consultation;
        private final Person person;

        ModelStubWithConsultation(Consultation consultation) throws ParseException {
            requireNonNull(consultation);
            this.consultation = consultation;
            this.person = new PersonBuilder().withNusnetid(consultation.getNusnetid().value)
                    .withConsultation(consultation.getFromInString(), consultation.getToInString()).build();
        }

        @Override
        public boolean hasPerson(Nusnetid nusnetid) {
            requireNonNull(nusnetid);
            return this.person.hasSameNusnetId(nusnetid);
        }

        @Override
        public boolean hasConsultation(Consultation consultation) {
            requireNonNull(consultation);
            return this.consultation.isSameConsultation(consultation);
        }
    }

    /**
     * A Model stub that always accept the consultation being added.
     */
    private class ModelStubAcceptingConsultationAdded extends ModelStub {
        private final AddressBook addressBook = new AddressBook();
        private final FilteredList<Consultation> filteredConsultations =
                new FilteredList<>(addressBook.getConsultationList());

        @Override
        public boolean hasPerson(Nusnetid nusnetid) {
            requireNonNull(nusnetid);
            return addressBook.hasPerson(nusnetid);
        }

        @Override
        public void addPerson(Person person) {
            addressBook.addPerson(person);
            // No need to update filtered person list for this test stub
        }

        @Override
        public boolean hasConsultation(Consultation consultation) {
            requireNonNull(consultation);
            return false; // No duplicate consultations
        }

        @Override
        public boolean hasOverlappingConsultation(Consultation consultation) {
            requireNonNull(consultation);
            return false; // No overlapping consultations
        }

        @Override
        public void addConsultationToPerson(Nusnetid nusnetid, Consultation consultation) {
            requireAllNonNull(nusnetid, consultation);
            addressBook.addConsultationToPerson(nusnetid, consultation);
        }

        @Override
        public void addConsultation(Consultation consultation) {
            requireNonNull(consultation);
            addressBook.addConsultation(consultation);
            updateFilteredConsultationList(PREDICATE_SHOW_ALL_CONSULTATIONS);
        }

        @Override
        public void updateFilteredConsultationList(Predicate<Consultation> predicate) {
            requireNonNull(predicate);
            filteredConsultations.setPredicate(predicate);
        }
    }
}
