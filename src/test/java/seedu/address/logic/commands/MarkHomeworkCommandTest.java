package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Nusnetid;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class MarkHomeworkCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
        // Add sample students with distinct identity fields to avoid duplicate detection
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

        // Add initial homework
        alice = alice.withAddedHomework(1);
        bob = bob.withAddedHomework(1);

        model.addPerson(alice);
        model.addPerson(bob);
    }

    @Test
    public void execute_markHomeworkComplete_success() throws Exception {
        MarkHomeworkCommand command = new MarkHomeworkCommand(new Nusnetid("E1234567"), 1, "complete");
        String expectedMessage = String.format(MarkHomeworkCommand.MESSAGE_SUCCESS,
                1, "Alice", "complete");
        assertEquals(expectedMessage, command.execute(model).getFeedbackToUser());

        // Verify that the homework status is updated
        Person updatedAlice = model.getFilteredPersonList().stream()
                .filter(p -> p.getNusnetid().value.equals("E1234567")).findFirst().orElseThrow();
        assertEquals("complete", updatedAlice.getHomeworkTracker().getStatus(1));
    }

    @Test
    public void execute_markHomeworkLate_success() throws Exception {
        MarkHomeworkCommand command = new MarkHomeworkCommand(new Nusnetid("E1234568"), 1, "late");
        String expectedMessage = String.format(MarkHomeworkCommand.MESSAGE_SUCCESS,
                1, "Bob", "late");
        assertEquals(expectedMessage, command.execute(model).getFeedbackToUser());

        // Verify that the homework status is updated
        Person updatedBob = model.getFilteredPersonList().stream()
                .filter(p -> p.getNusnetid().value.equals("E1234568")).findFirst().orElseThrow();
        assertEquals("late", updatedBob.getHomeworkTracker().getStatus(1));
    }

    @Test
    public void execute_markHomeworkNotAdded_throwsCommandException() {
        // Homework 2 is not added yet
        MarkHomeworkCommand command = new MarkHomeworkCommand(new Nusnetid("E1234567"), 2, "complete");
        assertThrows(Exception.class, () -> command.execute(model));
    }

    @Test
    public void execute_invalidStatus_throwsCommandException() {
        // Invalid status
        MarkHomeworkCommand command = new MarkHomeworkCommand(new Nusnetid("E1234567"), 1, "finished");
        assertThrows(Exception.class, () -> command.execute(model));
    }

    @Test
    public void execute_studentNotFound_throwsCommandException() {
        MarkHomeworkCommand command = new MarkHomeworkCommand(new Nusnetid("E0000000"), 1, "complete");
        assertThrows(CommandException.class, () -> command.execute(model));
    }
}
