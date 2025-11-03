package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Group;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.AttendanceStatus;
import seedu.address.model.person.GroupId;
import seedu.address.model.person.Nusnetid;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class MarkAllAttendanceCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
        // Add a sample group
        GroupId groupId = new GroupId("T01");
        Group group = new Group(groupId);
        model.addGroup(group);
        // Add sample students
        Person alice = new PersonBuilder()
                .withNusnetid("E1234567")
                .withName("Alice")
                .withTelegram("@alice")
                .withPhone(null)
                .withEmail(null)
                .build();
        Person bob = new PersonBuilder()
                .withNusnetid("E1234568")
                .withName("Bob")
                .withTelegram("@bob")
                .withPhone(null)
                .withEmail(null)
                .build();
        group.addStudent(alice);
        group.addStudent(bob);
        model.addPerson(alice);
        model.addPerson(bob);
    }

    @Test
    public void execute_markAllAttendance_success() throws Exception {
        MarkAllAttendanceCommand command = new MarkAllAttendanceCommand(new GroupId("T01"),
                2, AttendanceStatus.fromString("present"));
        String expectedMessage = String.format(MarkAllAttendanceCommand.MESSAGE_MARK_ATTENDANCE_SUCCESS,
                "T01", "present", 2);
        assertEquals(expectedMessage, command.execute(model).getFeedbackToUser());
        Person updatedAlice = model.getFilteredPersonList().stream()
                .filter(p -> p.getNusnetid().equals(new Nusnetid("E1234567"))).findFirst().orElse(null);
        assertEquals(true, updatedAlice.getAttendanceSheet().getAttendanceForWeek(2).isPresent());
        assertEquals("present", updatedAlice
                .getAttendanceSheet()
                .getAttendanceForWeek(2)
                .get().getAttendanceStatus().getStatus());
        // check that Bob now has attendance for week 2 marked as present
        Person updatedBob = model.getFilteredPersonList().stream()
                .filter(p -> p.getNusnetid().value.equals("E1234568")).findFirst().orElseThrow();
        assertEquals(true, updatedBob.getAttendanceSheet().getAttendanceForWeek(2).isPresent());
        assertEquals("present", updatedBob
                .getAttendanceSheet()
                .getAttendanceForWeek(2)
                .get().getAttendanceStatus().getStatus());
    }


    @Test
    public void execute_groupNotFound_throwsCommandException() {
        MarkAllAttendanceCommand command = new MarkAllAttendanceCommand(new GroupId("T02"), 1,
                AttendanceStatus.fromString("present"));
        assertThrows(Exception.class, () -> command.execute(model));
    }
}
