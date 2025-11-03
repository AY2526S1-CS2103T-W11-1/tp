package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.AddToGroupCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.event.Consultation;
import seedu.address.model.event.UniqueConsultationList;
import seedu.address.model.person.GroupId;
import seedu.address.model.person.Nusnetid;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.exceptions.DuplicatePersonException;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;
    private final UniqueConsultationList consultations;
    private final UniqueGroupList groups;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        consultations = new UniqueConsultationList();
        groups = new UniqueGroupList();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Replaces the contents of the consultation list with {@code consultations}.
     * {@code consultations} must not contain duplicate consultations.
     */
    public void setConsultations(List<Consultation> consultations) {
        this.consultations.setConsultations(consultations);
    }
    /**
     * Returns true if a group with the same identity as {@code groupId} exists in the address book.
     */
    public boolean hasGroup(GroupId groupId) {
        requireNonNull(groupId);
        return groups.contains(groupId);
    }
    /**
     * Replaces the contents of the group list with {@code groups}.
     * {@code groups} must not contain duplicate groups.
     */
    public void setGroupList(List<Group> groups) {
        this.groups.setGroups(groups);
    }
    /**
     * Adds a group to the address book.
     * The group must not already exist in the address book.
     */
    public void addGroup(Group g) {
        requireNonNull(g);
        this.groups.add(g);
    }
    /**
     * Gets a group by GroupId, or null if not present.
     */
    public Group getGroup(GroupId groupId) {
        requireNonNull(groupId);
        return groups.getGroup(groupId);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);
        setPersons(newData.getPersonList());
        setConsultations(newData.getConsultationList());
        setGroupList(newData.getGroupList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Returns true if a person with the same nusnetid as {@code nusnetid} exists in the address book.
     */
    public boolean hasPerson(Nusnetid nusnetid) {
        requireNonNull(nusnetid);
        return persons.contains(nusnetid);
    }
    /**
     * Returns the person with the given nusnetid.
     * Returns null if no such person exists.
     */
    public Person getPerson(Nusnetid nusnetid) {
        requireNonNull(nusnetid);
        return persons.find(nusnetid);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        this.updateGroupWhenAddPerson(p);
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);
        persons.setPerson(target, editedPerson);
    }
    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
        this.removePersonFromExistingGroup(key);
    }
    /**
     * Updates the group when a new person is added.
     * If the group does not exist, create a new group and add the person to it.
     * If the group exists, add the person to the existing group.
     * @param person the person to be added
     */
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
    private void addPersonToExistingGroup(Person person) {
        Group group = groups.getGroup(person.getGroupId());
        group.addStudent(person);
    }
    /**
     * Removes a person from their existing group.
     * This is used when a person is deleted or their group is changed.
     * @param person student to be removed from their existing group
     */
    public void removePersonFromExistingGroup(Person person) {
        Group group = groups.getGroup(person.getGroupId());
        group.removeStudent(person.getNusnetid());
    }
    /**
     * Updates the group when a person's details are edited.
     * @param oldPerson the person before editing
     */
    public void updateGroupWhenEditPerson(Person oldPerson) {
        requireNonNull(oldPerson);
        removePersonFromExistingGroup(oldPerson);
    }
    /**
     * Moves a student to a new group. Also update the address book person list.
     * @param student the student to be moved
     * @param newGroupId the new group ID
     */
    public void moveStudentToNewGroup(Person student, GroupId newGroupId) throws CommandException {
        requireNonNull(student);
        requireNonNull(newGroupId);
        if (student.getGroupId().equals(newGroupId)) {
            throw new CommandException(AddToGroupCommand.MESSAGE_SAME_GROUP_FAIL);
        }
        // Remove from old group
        Group oldGroup = groups.getGroup(student.getGroupId());
        assert oldGroup != null : "Old group should exist when moving student to new group.";
        oldGroup.removeStudent(student.getNusnetid());
        // Add to new group
        Person updatedStudent = student.withUpdatedGroup(newGroupId);
        try {
            assert this.persons.contains(student);
            this.setPerson(student, updatedStudent);
            // Update in address book person list
            // This may throw DuplicatePersonException or PersonNotFoundException
            // Here we assume that the student exists and no duplicates will be created
        } catch (DuplicatePersonException e) {
            throw new CommandException(e.getMessage());
        }
        // If new group does not exist, create it
        // Else, add student to existing group
        if (!groups.contains(newGroupId)) {
            Group newGroup = new Group(newGroupId);
            this.addGroup(newGroup);
            newGroup.addStudent(updatedStudent);
        } else {
            Group newGroup = groups.getGroup(newGroupId);
            newGroup.addStudent(updatedStudent);
        }
    }

    /**
     * Adds the given {@code consultation} to the person identified by {@code nusnetid}.
     * The person must exist in the address book.
     */
    public void addConsultationToPerson(Nusnetid nusnetid, Consultation consultation) {
        persons.addConsultationToPerson(nusnetid, consultation);
    }

    /**
     * Deletes the consultation from the person identified by {@code nusnetid}.
     * The person must exist in the address book.
     * @return the deleted Consultation.
     */
    public Consultation deleteConsultationFromPerson(Nusnetid nusnetid) {
        return persons.deleteConsultationFromPerson(nusnetid);
    }

    //// consultation-level operations

    /**
     * Returns true if a consultations equivalent to {@code consultation} exists in the address book.
     */
    public boolean hasConsultation(Consultation consultation) {
        requireNonNull(consultation);
        return consultations.contains(consultation);
    }

    /**
     * Returns true if a consultation overlapping with {@code consultation} exists in the address book.
     */
    public boolean hasOverlappingConsultation(Consultation consultation) {
        requireNonNull(consultation);
        return consultations.hasOverlappingConsultation(consultation);
    }

    /**
     * Adds a consultation to the address book.
     * The consultation must not already exist in the address book.
     */
    public void addConsultation(Consultation c) {
        consultations.add(c);
    }

    /**
     * Deletes the given consultation from the address book.
     * The consultation must exist in the address book.
     */
    public void deleteConsultation(Consultation c) {
        consultations.remove(c);
    }

    //// util methods
    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }
    public List<Person> getUniquePersonList() {
        return persons.toList();
    }
    @Override
    public ObservableList<Consultation> getConsultationList() {
        return consultations.asUnmodifiableObservableList();
    }
    @Override
    public ObservableList<Group> getGroupList() {
        return this.groups.asUnmodifiableObservableList();
    }
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBook)) {
            return false;
        }

        AddressBook otherAddressBook = (AddressBook) other;
        return this.getPersonList().equals(otherAddressBook.getPersonList())
            && this.getConsultationList().equals(otherAddressBook.getConsultationList())
            && this.groups.equals(otherAddressBook.groups);
    }
    @Override
    public int hashCode() {
        return Objects.hash(getPersonList(), getConsultationList(), getGroupList());
    }
    /**
     * Updates consultations stored in the address book when a person's nusnetid is edited.
     * Any consultation that references {@code oldNusnetid} will be replaced with a new Consultation
     * using {@code newNusnetid} but keeping the same time range. If the updated consultation would
     * conflict with existing consultations (by same time range), it will replace the old one.
     */
    public void updateConsultationsForEditedPerson(Nusnetid oldNusnetid, Nusnetid newNusnetid) {
        requireNonNull(oldNusnetid);
        requireNonNull(newNusnetid);
        // Update consultations stored at the address book level
        List<Consultation> toReplace = this.consultations.asUnmodifiableObservableList().stream()
                .filter(c -> c.getNusnetid().equals(oldNusnetid))
                .collect(Collectors.toList());
        for (Consultation oldConsult : toReplace) {
            Consultation newConsult = new Consultation(newNusnetid, oldConsult.getFrom(), oldConsult.getTo());
            try {
                // Replace the old consultation with the new nusnetid but same times.
                // setConsultation uses identity by time; replacing is allowed because identity is unchanged.
                consultations.setConsultation(oldConsult, newConsult);
            } catch (RuntimeException e) {
                // If not found (or other edge), try remove and add to ensure presence under new nusnetid.
                try {
                    consultations.remove(oldConsult);
                } catch (RuntimeException ex) {
                    // ignore if already removed
                }
                try {
                    consultations.add(newConsult);
                } catch (RuntimeException ex) {
                    // ignore if add fails due to uniqueness; in that case, an equivalent group already exists.
                }
            }
        }

        // Also update any consultation stored within persons (in UniquePersonList)
        // For each person that had the old nusnetid, find and update their consultation as well
        // Note: UniquePersonList manages person-level consultations
        // via addConsultationToPerson/deleteConsultationFromPerson
        // We will iterate through persons and for matching nusnetid update the person entry.
        List<Person> personsToUpdate = persons.toList().stream()
                .filter(p -> p.getNusnetid().equals(oldNusnetid) && p.hasConsultation())
                .collect(Collectors.toList());
        for (Person p : personsToUpdate) {
            p.getConsultation().ifPresent(oldConsult -> {
                Consultation newConsult = new Consultation(newNusnetid, oldConsult.getFrom(), oldConsult.getTo());
                Person updatedPerson = p.deleteConsultation();
                updatedPerson = updatedPerson.addConsultation(newConsult);
                persons.setPerson(p, updatedPerson);
            });
        }
    }
    @Override
    public String toString() {
        List<Group> groupsString = groups.asUnmodifiableObservableList().stream()
                .sorted(Comparator.comparing(g -> g.getGroupId().toString()))
                .collect(Collectors.toList());
        return new ToStringBuilder(this)
                .add("persons", persons)
                .add("consultations", consultations)
                .add("groups", groupsString)
                .toString();
    }
}
