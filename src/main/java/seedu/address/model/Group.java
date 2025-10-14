package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.Iterator;

import seedu.address.model.person.GroupId;
import seedu.address.model.person.Nusnetid;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;

/**
 * Represents a Tutorial session which contains an AttendanceSheet.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Group {
    private final GroupId groupId;
    private final UniquePersonList students = new UniquePersonList();

    /**
     * Constructs a {@code Tutorial} with an empty AttendanceSheet.
     *
     * @param groupId A valid slot number.
     */
    public Group(GroupId groupId) {
        requireNonNull(groupId);
        this.groupId = groupId;
    }

    /** Returns the GroupId used to identify this group. */
    public GroupId getGroupId() { return groupId; }

    /**
     * Returns the list of students in this tutorial.
     */
    public UniquePersonList getStudents() {
        return students;
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
}
