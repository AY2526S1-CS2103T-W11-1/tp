package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.AttendanceStatus;
import seedu.address.model.person.Nusnetid;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class MarkAttendanceCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
        // Add sample students
        Person alice = new PersonBuilder().withNusnetid("E1234567").withName("Alice").build();
        Person bob = new PersonBuilder().withNusnetid("E1234568").withName("Bob").build();
        model.addPerson(alice);
        model.addPerson(bob);
    }

    @Test
    public void execute_markAttendance_success() throws Exception {
        MarkAttendanceCommand command = new MarkAttendanceCommand(new Nusnetid("E1234567"),
                2, AttendanceStatus.fromString("present"));
        String expectedMessage = String.format(MarkAttendanceCommand.MESSAGE_MARK_ATTENDANCE_SUCCESS,
                "Alice", "present", 2);
        assertEquals(expectedMessage, command.execute(model).getFeedbackToUser());

        // check that Alice now has attendance for week 2 marked as present
        Person updatedAlice = model.getFilteredPersonList().stream()
                .filter(p -> p.getNusnetid().value.equals("E1234567")).findFirst().orElseThrow();
        assertEquals(true, updatedAlice.getAttendanceSheet().getAttendanceForWeek(2).isPresent());
        assertEquals("present", updatedAlice
                .getAttendanceSheet()
                .getAttendanceForWeek(2)
                .get().getAttendanceStatus().getStatus());
    }


    @Test
    public void execute_studentNotFound_throwsCommandException() {
        MarkAttendanceCommand command = new MarkAttendanceCommand(new Nusnetid("E0000000"),
                1, AttendanceStatus.fromString("present"));
        assertThrows(Exception.class, () -> command.execute(model));
    }
}
