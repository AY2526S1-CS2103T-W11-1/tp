package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.event.Consultation;
import seedu.address.model.person.Nusnetid;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code ListConsultationCommand}.
 */
public class ListConsultationCommandTest {

    private Model model;
    private Model expectedModel;
    private ListConsultationCommand command;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
        expectedModel = new ModelManager();

        // add some sample consultations for realism
        Consultation consult1 = new Consultation(new Nusnetid("E1234567"),
                LocalDateTime.of(2025, 10, 10, 10, 0),
                LocalDateTime.of(2025, 10, 10, 11, 0));
        Consultation consult2 = new Consultation(new Nusnetid("E7654321"),
                LocalDateTime.of(2025, 10, 11, 14, 0),
                LocalDateTime.of(2025, 10, 11, 15, 0));

        model.addConsultation(consult1);
        model.addConsultation(consult2);
        expectedModel.addConsultation(consult1);
        expectedModel.addConsultation(consult2);

        command = new ListConsultationCommand();
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        CommandResult result = command.execute(model);

        assertEquals(ListConsultationCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
        assertEquals(expectedModel.getFilteredConsultationList(), model.getFilteredConsultationList());
        assertNotNull(result);
    }

    @Test
    public void execute_listIsFiltered_showsAllConsultations() {
        // Simulate filtering — e.g., show only consultations after a certain date
        model.updateFilteredConsultationList(c -> c.getFrom().isAfter(LocalDateTime.of(2025, 10, 10, 23, 59)));

        // ensure that filter actually reduced the list before executing command
        int filteredSizeBefore = model.getFilteredConsultationList().size();
        assertEquals(1, filteredSizeBefore);

        // execute should reset the filter
        CommandResult result = command.execute(model);

        assertEquals(ListConsultationCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
        assertEquals(expectedModel.getFilteredConsultationList(), model.getFilteredConsultationList());
        assertNotNull(result);
    }

    @Test
    public void equals() {
        ListConsultationCommand listCommand1 = new ListConsultationCommand();
        ListConsultationCommand listCommand2 = new ListConsultationCommand();

        // same object -> returns true
        assertEquals(listCommand1, listCommand1);

        // different objects but same type -> returns true
        assertEquals(listCommand1, listCommand2);
    }
}
