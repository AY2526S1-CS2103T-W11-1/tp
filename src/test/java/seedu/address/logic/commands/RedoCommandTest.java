package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;

// @ACC-UNDOREDO-002 @ACC-UNDOREDO-007
public class RedoCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
        expectedModel = new ModelManager();
    }

    @Test
    public void execute_noRedoHistory_throwsCommandException() {
        RedoCommand redoCommand = new RedoCommand();
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    @Test
    public void execute_noRedoHistoryAfterUndoAndNewCommit_throwsCommandException() {
        model.addPerson(ALICE);
        model.commitAddressBook();
        model.undoAddressBook();
        // New commit clears redo stack
        model.commitAddressBook();

        assertCommandFailure(new RedoCommand(), model, RedoCommand.MESSAGE_FAILURE);
    }

    @Test
    public void execute_redoAfterUndo_success() {
        model.addPerson(ALICE);
        model.commitAddressBook();

        // Undo
        model.undoAddressBook();

        // Redo should restore the state with Alice
        Model modelWithAlice = new ModelManager();
        modelWithAlice.addPerson(ALICE);
        modelWithAlice.commitAddressBook();

        assertCommandSuccess(new RedoCommand(), model, RedoCommand.MESSAGE_SUCCESS, modelWithAlice);
    }

    @Test
    public void equals_sameInstance_returnsTrue() {
        RedoCommand redoCommand = new RedoCommand();
        assertTrue(redoCommand.equals(redoCommand));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        RedoCommand redoCommand = new RedoCommand();
        assertFalse(redoCommand.equals(new UndoCommand()));
    }
}
