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

// @ACC-UNDOREDO-001 @ACC-UNDOREDO-006 @ACC-UNDOREDO-011
public class UndoCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
        expectedModel = new ModelManager();
    }

    @Test
    public void execute_noHistory_throwsCommandException() {
        UndoCommand undoCommand = new UndoCommand();
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
    }

    @Test
    public void execute_undoAfterAdd_success() {
        model.addPerson(ALICE);
        model.commitAddressBook();

        expectedModel.addPerson(ALICE);
        // expectedModel does NOT commit, so it represents the state before the add

        // After undo, model should have empty address book
        Model emptyModel = new ModelManager();
        assertCommandSuccess(new UndoCommand(), model, UndoCommand.MESSAGE_SUCCESS, emptyModel);
    }

    @Test
    public void execute_undoAfterClear_restoresData() {
        model.addPerson(ALICE);
        model.commitAddressBook();

        // Clear all data
        model.setAddressBook(new seedu.address.model.AddressBook());
        model.commitAddressBook();

        // Undo should restore the data
        Model modelWithAlice = new ModelManager();
        modelWithAlice.addPerson(ALICE);
        modelWithAlice.commitAddressBook();

        assertCommandSuccess(new UndoCommand(), model, UndoCommand.MESSAGE_SUCCESS, modelWithAlice);
    }

    @Test
    public void execute_multipleUndo_success() {
        model.addPerson(ALICE);
        model.commitAddressBook();

        // Undo
        model.undoAddressBook();
        assertFalse(model.canUndoAddressBook());
    }

    @Test
    public void equals_sameInstance_returnsTrue() {
        UndoCommand undoCommand = new UndoCommand();
        assertTrue(undoCommand.equals(undoCommand));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        UndoCommand undoCommand = new UndoCommand();
        assertFalse(undoCommand.equals(new RedoCommand()));
    }
}
