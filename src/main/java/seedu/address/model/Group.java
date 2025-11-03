package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import seedu.address.model.person.GroupId;
import seedu.address.model.person.Nusnetid;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;

/**
 * Represents a Tutorial session.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Group {
    private final GroupId groupId;
    private final UniquePersonList students;

    /**
     * Construct a group
     * @param groupId A valid group id.
     */
    public Group(GroupId groupId) {
        requireNonNull(groupId);
        this.groupId = groupId;
        this.students = new UniquePersonList();
    }

    /**
     * Construct a group
     * @param groupId A valid group id.
     * @param students list of students in this tutorial.
     */
    public Group(GroupId groupId, List<Person> students) {
        requireNonNull(groupId);
        requireNonNull(students);
        this.groupId = groupId;
        this.students = new UniquePersonList();
        for (Person p : students) {
            this.students.add(p);
        }
    }

    /** Returns the GroupId used to identify this group. */
    public GroupId getGroupId() {
        return groupId;
    }
    /**
     * Returns true if both groups have the same groupId.
     * @param otherGroupId The other group to be compared to.
     * @return boolean
     */
    public boolean isSameGroup(GroupId otherGroupId) {
        return this.groupId.equals(otherGroupId);
    }
    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * @param target old person
     * @param editedPerson new person
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);
        students.setPerson(target, editedPerson);
    }
    /**
     * Returns all persons in this tutorial.
     * @return ArrayList of persons in this tutorial.
     */
    public ArrayList<Person> getAllPersons() {
        return this.students.toArrayList();
    }
    /**
     * adds a student to this tutorial.
     */
    public void addStudent(Person student) {
        requireNonNull(student);
        this.students.add(student);
    }
    /**
     * removes a student from this tutorial by Nusnetid.
     */
    public void removeStudent(Nusnetid nusnetid) {
        requireNonNull(nusnetid);
        Iterator<Person> iterator = students.iterator();
        while (iterator.hasNext()) {
            Person person = iterator.next();
            if (person.getNusnetid().equals(nusnetid)) {
                iterator.remove();
                break;
            }
        }
    }
    /**
     * Checks if a student with the given NUSNET ID exists in this tutorial.
     *
     * @param nusnetid The NUSNET ID to check.
     * @return true if student exists in this tutorial, false otherwise.
     */
    public boolean hasStudent(Nusnetid nusnetid) {
        requireNonNull(nusnetid);
        for (Person p : students) {
            if (p.getNusnetid().equals(nusnetid)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Group)) {
            return false;
        }
        Group otherGroup = (Group) other;
        return this.groupId.equals(otherGroup.groupId);
    }

    @Override
    public int hashCode() {
        return groupId.hashCode();
    }
    @Override
    public String toString() {
        return String.format("Group: %s", groupId);
    }
}
