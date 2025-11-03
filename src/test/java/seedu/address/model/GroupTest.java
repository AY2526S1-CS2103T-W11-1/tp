package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.GroupId;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class GroupTest {
    @Test
    public void constructor() {
        Group group = new Group(new GroupId("T01"));
        assertEquals(new GroupId("T01"), group.getGroupId());
        assertEquals(Collections.emptyList(), group.getAllPersons());
    }
    @Test
    public void constructor_withStudents() {
        Person student1 = new PersonBuilder().build();
        Person student2 = new PersonBuilder().withName("Sky")
                .withEmail("yu@u.nus.edu").withPhone("3457")
                .withTelegram("@cheru").withNusnetid("E1234321").build();
        ArrayList<Person> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        Group group = new Group(new GroupId("T01"), students);
        assertEquals(new GroupId("T01"), group.getGroupId());
        assertEquals(students, group.getAllPersons());
    }
    @Test
    public void addStudent_withValidStudent_success() {
        Group group = new Group(new GroupId("T01"));
        Person toAdd = new PersonBuilder().build();
        group.addStudent(toAdd);
        assertEquals(toAdd, group.getAllPersons().get(0));
    }
    @Test
    public void setStudents_withValidStudents_success() {
        Person student1 = new PersonBuilder().build();
        Person student2 = new PersonBuilder().withName("Sky")
                .withEmail("yu@u.nus.edu").withPhone("3457")
                .withTelegram("@cheru").withNusnetid("E1234321").build();
        ArrayList<Person> students = new ArrayList<>();
        students.add(student1);
        Group group = new Group(new GroupId("T01"), students);
        group.setPerson(student1, student2);
        assertEquals(new GroupId("T01"), group.getGroupId());
        ArrayList<Person> updatedStudents = new ArrayList<>();
        updatedStudents.add(student2);
        assertEquals(updatedStudents, group.getAllPersons());
    }
    @Test
    public void isSameGroup_withSameGroupId_returnsTrue() {
        Group group = new Group(new GroupId("T01"));
        assertEquals(true, group.isSameGroup(new GroupId("T01")));
    }
    @Test
    public void isSameGroup_withDifferentGroupId_returnsFalse() {
        Group group = new Group(new GroupId("T01"));
        assertEquals(false, group.isSameGroup(new GroupId("B02")));
    }
    @Test
    public void toString_validGroupId_success() {
        Group group = new Group(new GroupId("T01"));
        assertEquals("T01", group.getGroupId().toString());
    }
    @Test
    public void hasStudent_withExistingStudent_returnsTrue() {
        Person student1 = new PersonBuilder().build();
        ArrayList<Person> students = new ArrayList<>();
        students.add(student1);
        Group group = new Group(new GroupId("T01"), students);
        assertEquals(true, group.hasStudent(student1.getNusnetid()));
    }
    @Test
    public void hasStudent_withNonExistingStudent_returnsFalse() {
        Person student1 = new PersonBuilder().build();
        ArrayList<Person> students = new ArrayList<>();
        students.add(student1);
        Group group = new Group(new GroupId("T01"), students);
        assertEquals(false, group.hasStudent(new PersonBuilder().withNusnetid("E9999999").build().getNusnetid()));
    }
}
