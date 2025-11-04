package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.event.Consultation;
import seedu.address.model.person.Nusnetid;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteConsultationCommand}.
 */
public class DeleteConsultationCommandTest {

    private Model model;
    private Person alice;
    private Person bob;
    private final Nusnetid aliceId = new Nusnetid("E1234567");
    private final Nusnetid bobId = new Nusnetid("E1234568");

    @BeforeEach
    public void setUp() throws ParseException {
        model = new ModelManager();

        // Alice has a consultation
        LocalDateTime from = LocalDateTime.of(2025, 10, 10, 10, 0);
        LocalDateTime to = LocalDateTime.of(2025, 10, 10, 12, 0);
        Consultation consultation = new Consultation(aliceId, from, to);
        alice = new PersonBuilder().withName("Alice").withNusnetid("E1234567")
                .withConsultation("20251010 1000", "20251010 1200").build();

        // Bob does not have a consultation
        bob = new PersonBuilder().withName("Bob").withNusnetid("E1234568").build();

        model.addPerson(alice);
        model.addPerson(bob);
        model.addConsultation(alice.getConsultation().get()); // keep model consistent
    }


    @Test
    public void execute_validNusNetId_success() throws Exception {
        DeleteConsultationCommand command = new DeleteConsultationCommand(aliceId);

        CommandResult result = command.execute(model);

        String expectedMessage = String.format(DeleteConsultationCommand.MESSAGE_SUCCESS,
               Messages.format(alice.getConsultation().get()));

        assertEquals(expectedMessage, result.getFeedbackToUser());
        // confirm consultation was deleted
        assertEquals(false, model.hasConsultation(alice.getConsultation().get()));
        assertEquals(true, model.findPerson(aliceId).getConsultation().isEmpty());
    }

    @Test
    public void execute_studentDoesNotExist_throwsCommandException() {
        DeleteConsultationCommand command = new DeleteConsultationCommand(new Nusnetid("E0000000"));

        assertThrows(CommandException.class,
                DeleteConsultationCommand.MESSAGE_STUDENT_DOES_NOT_EXIST, () -> command.execute(model));
    }

    @Test
    public void execute_studentHasNoConsultation_throwsCommandException() {
        model.addPerson(bob);
        DeleteConsultationCommand command = new DeleteConsultationCommand(bobId);

        assertThrows(CommandException.class,
                DeleteConsultationCommand.MESSAGE_STUDENT_DOES_NOT_HAVE_CONSULTATION, () -> command.execute(model));
    }

    @Test
    public void equals() {
        DeleteConsultationCommand deleteConsultationAliceCommand =
                new DeleteConsultationCommand(aliceId);
        DeleteConsultationCommand deleteConsultationBobCommand =
                new DeleteConsultationCommand(bobId);

        // same object -> returns true
        assertTrue(deleteConsultationAliceCommand.equals(deleteConsultationAliceCommand));

        // same values -> returns true
        DeleteConsultationCommand deleteConsultationAliceCommandCopy = new DeleteConsultationCommand(aliceId);
        assertTrue(deleteConsultationAliceCommand.equals(deleteConsultationAliceCommandCopy));

        // different types -> returns false
        assertFalse(deleteConsultationAliceCommand.equals(1));

        // null -> returns false
        assertFalse(deleteConsultationAliceCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteConsultationAliceCommand.equals(deleteConsultationBobCommand));
    }

    @Test
    public void toStringMethod() {
        DeleteConsultationCommand deleteConsultationCommand = new DeleteConsultationCommand(aliceId);
        String expected = DeleteConsultationCommand.class.getCanonicalName() + "{toDelete=" + aliceId + "}";
        assertEquals(expected, deleteConsultationCommand.toString());
    }
}
