package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddConsultationCommand;
import seedu.address.logic.commands.AddHomeworkCommand;
import seedu.address.logic.commands.AddToGroupCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.CreateGroupCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeleteConsultationCommand;
import seedu.address.logic.commands.DeleteHomeworkCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.MarkAllAttendanceCommand;
import seedu.address.logic.commands.MarkAttendanceCommand;
import seedu.address.logic.commands.MarkHomeworkCommand;
import seedu.address.logic.commands.MutatingCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;

// @ACC-UNDOREDO-003 @ACC-UNDOREDO-004 @ACC-UNDOREDO-005 @ACC-UNDOREDO-008
// @ACC-UNDOREDO-012 @ACC-UNDOREDO-014
public class ModelManagerUndoRedoTest {

    private ModelManager modelManager;

    @BeforeEach
    public void setUp() {
        modelManager = new ModelManager();
    }

    @Test
    public void canUndo_initialState_returnsFalse() {
        assertFalse(modelManager.canUndoAddressBook());
    }

    @Test
    public void canRedo_initialState_returnsFalse() {
        assertFalse(modelManager.canRedoAddressBook());
    }

    @Test
    public void commit_canUndo_returnsTrue() {
        modelManager.addPerson(ALICE);
        modelManager.commitAddressBook();
        assertTrue(modelManager.canUndoAddressBook());
    }

    @Test
    public void undo_restoresPreviousState() {
        modelManager.addPerson(ALICE);
        modelManager.commitAddressBook();
        modelManager.undoAddressBook();
        assertFalse(modelManager.getAddressBook().getPersonList().contains(ALICE));
    }

    @Test
    public void redo_restoresUndoneState() {
        modelManager.addPerson(ALICE);
        modelManager.commitAddressBook();
        modelManager.undoAddressBook();
        modelManager.redoAddressBook();
        assertTrue(modelManager.getAddressBook().getPersonList().contains(ALICE));
    }

    @Test
    public void multipleUndo_consecutiveUndos() {
        modelManager.addPerson(ALICE);
        modelManager.commitAddressBook();
        modelManager.addPerson(BENSON);
        modelManager.commitAddressBook();

        // Undo twice to go back to initial state
        modelManager.undoAddressBook();
        modelManager.undoAddressBook();

        assertFalse(modelManager.getAddressBook().getPersonList().contains(ALICE));
        assertFalse(modelManager.getAddressBook().getPersonList().contains(BENSON));
        assertFalse(modelManager.canUndoAddressBook());
    }

    @Test
    public void multipleRedo_consecutiveRedos() {
        modelManager.addPerson(ALICE);
        modelManager.commitAddressBook();
        modelManager.addPerson(BENSON);
        modelManager.commitAddressBook();

        // Undo twice
        modelManager.undoAddressBook();
        modelManager.undoAddressBook();

        // Redo twice
        modelManager.redoAddressBook();
        modelManager.redoAddressBook();

        assertTrue(modelManager.getAddressBook().getPersonList().contains(ALICE));
        assertTrue(modelManager.getAddressBook().getPersonList().contains(BENSON));
        assertFalse(modelManager.canRedoAddressBook());
    }

    @Test
    public void undoThenNewCommit_clearsRedoStack() {
        modelManager.addPerson(ALICE);
        modelManager.commitAddressBook();
        modelManager.addPerson(BENSON);
        modelManager.commitAddressBook();

        // Undo once (back to state with just Alice)
        modelManager.undoAddressBook();
        assertTrue(modelManager.canRedoAddressBook());

        // Commit a new state
        modelManager.commitAddressBook();
        assertFalse(modelManager.canRedoAddressBook());
    }

    @Test
    public void readOnlyCommands_doNotAffectHistory() {
        modelManager.addPerson(ALICE);
        modelManager.commitAddressBook();
        assertTrue(modelManager.canUndoAddressBook());

        // Execute read-only operations (they do not call commit)
        modelManager.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        modelManager.updateFilteredConsultationList(Model.PREDICATE_SHOW_ALL_CONSULTATIONS);

        // History should still be the same
        assertTrue(modelManager.canUndoAddressBook());
        assertFalse(modelManager.canRedoAddressBook());
    }

    @Test
    public void undoRedo_doesNotChangeHistorySize() {
        modelManager.addPerson(ALICE);
        modelManager.commitAddressBook();
        modelManager.addPerson(BENSON);
        modelManager.commitAddressBook();

        VersionedAddressBook versionedAb = (VersionedAddressBook) modelManager.getAddressBook();
        int sizeBefore = versionedAb.getAddressBookStateListSize();

        modelManager.undoAddressBook();
        assertEquals(sizeBefore, ((VersionedAddressBook) modelManager.getAddressBook()).getAddressBookStateListSize());

        modelManager.redoAddressBook();
        assertEquals(sizeBefore, ((VersionedAddressBook) modelManager.getAddressBook()).getAddressBookStateListSize());
    }

    @Test
    public void allMutatingCommands_implementMutatingCommand() {
        // Verify all 13 mutating commands implement MutatingCommand interface
        assertTrue(MutatingCommand.class.isAssignableFrom(AddCommand.class));
        assertTrue(MutatingCommand.class.isAssignableFrom(EditCommand.class));
        assertTrue(MutatingCommand.class.isAssignableFrom(DeleteCommand.class));
        assertTrue(MutatingCommand.class.isAssignableFrom(ClearCommand.class));
        assertTrue(MutatingCommand.class.isAssignableFrom(CreateGroupCommand.class));
        assertTrue(MutatingCommand.class.isAssignableFrom(AddToGroupCommand.class));
        assertTrue(MutatingCommand.class.isAssignableFrom(AddHomeworkCommand.class));
        assertTrue(MutatingCommand.class.isAssignableFrom(MarkHomeworkCommand.class));
        assertTrue(MutatingCommand.class.isAssignableFrom(DeleteHomeworkCommand.class));
        assertTrue(MutatingCommand.class.isAssignableFrom(MarkAttendanceCommand.class));
        assertTrue(MutatingCommand.class.isAssignableFrom(MarkAllAttendanceCommand.class));
        assertTrue(MutatingCommand.class.isAssignableFrom(AddConsultationCommand.class));
        assertTrue(MutatingCommand.class.isAssignableFrom(DeleteConsultationCommand.class));

        // Verify non-mutating commands do NOT implement MutatingCommand
        assertFalse(MutatingCommand.class.isAssignableFrom(ListCommand.class));
        assertFalse(MutatingCommand.class.isAssignableFrom(FindCommand.class));
        assertFalse(MutatingCommand.class.isAssignableFrom(HelpCommand.class));
        assertFalse(MutatingCommand.class.isAssignableFrom(ExitCommand.class));
        assertFalse(MutatingCommand.class.isAssignableFrom(UndoCommand.class));
        assertFalse(MutatingCommand.class.isAssignableFrom(RedoCommand.class));
    }

    @Test
    public void commitAfterEachMutatingCommand_canUndo() {
        // Simulate committing after each mutating operation
        modelManager.addPerson(ALICE);
        modelManager.commitAddressBook();
        assertTrue(modelManager.canUndoAddressBook());

        modelManager.addPerson(BENSON);
        modelManager.commitAddressBook();
        assertTrue(modelManager.canUndoAddressBook());

        modelManager.deletePerson(ALICE);
        modelManager.commitAddressBook();
        assertTrue(modelManager.canUndoAddressBook());
    }

    @Test
    public void clearThenUndo_restoresAllData() {
        modelManager.addPerson(ALICE);
        modelManager.commitAddressBook();
        modelManager.addPerson(BENSON);
        modelManager.commitAddressBook();

        // Clear all data
        modelManager.setAddressBook(new AddressBook());
        modelManager.commitAddressBook();
        assertEquals(0, modelManager.getAddressBook().getPersonList().size());

        // Undo should restore all data
        modelManager.undoAddressBook();
        assertTrue(modelManager.getAddressBook().getPersonList().contains(ALICE));
        assertTrue(modelManager.getAddressBook().getPersonList().contains(BENSON));
    }
}
