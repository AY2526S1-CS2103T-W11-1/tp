package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NUSNETID_BOB;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.event.Consultation;
import seedu.address.model.person.GroupId;
import seedu.address.model.person.Nusnetid;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.testutil.PersonBuilder;

public class AddressBookTest {

    private final AddressBook addressBook = new AddressBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), addressBook.getPersonList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        List<Person> newPersons = Arrays.asList(ALICE, editedAlice);
        AddressBookStub newData = new AddressBookStub(newPersons);

        assertThrows(DuplicatePersonException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasPerson((Person) null));
    }

    @Test
    public void hasPerson_nullNusNetId_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasPerson((Nusnetid) null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        assertTrue(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInAddressBook_returnsFalse() {
        addressBook.addPerson(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withNusnetid(VALID_NUSNETID_BOB).build();
        assertFalse(addressBook.hasPerson(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getPersonList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = AddressBook.class.getCanonicalName() + "{persons=" + addressBook.getPersonList()
                + ", consultations=" + addressBook.getConsultationList()
                + ", groups=" + addressBook.getGroupList() + "}";
        assertEquals(expected, addressBook.toString());
    }

    /**
     * A stub ReadOnlyAddressBook whose persons list can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();
        private final ObservableList<Consultation> consultations = FXCollections.observableArrayList();
        private final ObservableList<Group> groups = FXCollections.observableArrayList();

        AddressBookStub(Collection<Person> persons) {
            this.persons.setAll(persons);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }

        @Override
        public ObservableList<Consultation> getConsultationList() {
            return consultations;
        }
        @Override
        public ObservableList<Group> getGroupList() {
            return groups;
        }
        @Override
        public List<Person> getUniquePersonList() {
            return persons;
        }
        public void addGroup(Group g) {
            requireNonNull(g);
            this.groups.add(g);
        }
        private void addPersonToExistingGroup(Person person) {
            GroupId groupId = person.getGroupId();
            Group group = groups.stream()
                    .filter(g -> g.getGroupId().equals(groupId))
                    .findFirst().orElse(null);
            group.addStudent(person);
        }
        @Override
        public void updateGroupWhenAddPerson(Person person) {
            requireNonNull(person);
            if (!groups.contains(person.getGroupId())) {
                Group newGroup = new Group(person.getGroupId());
                this.addGroup(newGroup);
                newGroup.addStudent(person);
            } else {
                addPersonToExistingGroup(person);
            }
        }
    }
}
