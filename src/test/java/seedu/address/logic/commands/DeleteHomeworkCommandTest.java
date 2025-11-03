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

public class DeleteHomeworkCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
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
        model.addPerson(alice);
        model.addPerson(bob);

        // Add homework to students for deletion tests
        model.setPerson(alice, alice.withAddedHomework(1));
        model.setPerson(bob, bob.withAddedHomework(1));
    }

    @Test
    public void execute_deleteSingleHomework_success() throws Exception {
        DeleteHomeworkCommand command = new DeleteHomeworkCommand(new Nusnetid("E1234567"), 1, false);
        String expectedMessage = String.format(DeleteHomeworkCommand.MESSAGE_DELETE_HOMEWORK_SUCCESS, 1, "Alice");
        assertEquals(expectedMessage, command.execute(model).getFeedbackToUser());

        // Alice should no longer have homework 1
        Person updatedAlice = model.getFilteredPersonList().stream()
                .filter(p -> p.getNusnetid().value.equals("E1234567")).findFirst().orElseThrow();
        assertEquals(false, updatedAlice.getHomeworkTracker().hasAssignment(1));
    }

    @Test
    public void execute_deleteHomeworkFromAll_success() throws Exception {
        DeleteHomeworkCommand command = new DeleteHomeworkCommand(null, 1, true);
        String expectedMessage = String.format(DeleteHomeworkCommand.MESSAGE_DELETE_HOMEWORK_ALL_SUCCESS, 1);
        assertEquals(expectedMessage, command.execute(model).getFeedbackToUser());

        // Both students should no longer have homework 1
        for (Person p : model.getFilteredPersonList()) {
            assertEquals(false, p.getHomeworkTracker().hasAssignment(1));
        }
    }

    @Test
    public void execute_studentNotFound_throwsCommandException() {
        DeleteHomeworkCommand command = new DeleteHomeworkCommand(new Nusnetid("E9999999"), 1, false);
        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_homeworkNotFound_throwsCommandException() {
        // Alice has homework 1, try deleting homework 2
        DeleteHomeworkCommand command = new DeleteHomeworkCommand(new Nusnetid("E1234567"), 2, false);
        assertThrows(CommandException.class, () -> command.execute(model));
    }
}
