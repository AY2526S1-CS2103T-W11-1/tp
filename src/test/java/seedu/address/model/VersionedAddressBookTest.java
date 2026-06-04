package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.AddressBookBuilder;

// @ACC-UNDOREDO-009 @ACC-UNDOREDO-012
public class VersionedAddressBookTest {

    private final ReadOnlyAddressBook emptyAddressBook = new AddressBook();

    @Test
    public void constructor_initialState() {
        VersionedAddressBook versionedAb = new VersionedAddressBook(emptyAddressBook);
        assertFalse(versionedAb.canUndo());
        assertFalse(versionedAb.canRedo());
        assertEquals(1, versionedAb.getAddressBookStateListSize());
        assertEquals(0, versionedAb.getCurrentStatePointer());
    }

    @Test
    public void commit_appendsNewState() {
        VersionedAddressBook versionedAb = new VersionedAddressBook(emptyAddressBook);
        AddressBook abWithAlice = new AddressBookBuilder().withPerson(ALICE).build();
        versionedAb.resetData(abWithAlice);
        versionedAb.commit();

        assertTrue(versionedAb.canUndo());
        assertFalse(versionedAb.canRedo());
        assertEquals(2, versionedAb.getAddressBookStateListSize());
        assertEquals(1, versionedAb.getCurrentStatePointer());
    }

    @Test
    public void commit_truncatesRedoStack() {
        VersionedAddressBook versionedAb = new VersionedAddressBook(emptyAddressBook);
        // Add Alice and commit
        versionedAb.addPerson(ALICE);
        versionedAb.commit();
        // Add Benson and commit
        versionedAb.addPerson(BENSON);
        versionedAb.commit();

        // Undo back to initial state
        versionedAb.undo();
        versionedAb.undo();
        assertTrue(versionedAb.canRedo());

        // Commit new state - should truncate redo stack
        versionedAb.addPerson(ALICE);
        versionedAb.commit();
        assertFalse(versionedAb.canRedo());
        assertEquals(2, versionedAb.getAddressBookStateListSize());
    }

    @Test
    public void undo_restoresPreviousState() {
        VersionedAddressBook versionedAb = new VersionedAddressBook(emptyAddressBook);
        versionedAb.addPerson(ALICE);
        versionedAb.commit();

        // Undo should restore empty state
        versionedAb.undo();
        assertEquals(0, versionedAb.getPersonList().size());
        assertFalse(versionedAb.canUndo());
    }

    @Test
    public void redo_restoresUndoneState() {
        VersionedAddressBook versionedAb = new VersionedAddressBook(emptyAddressBook);
        versionedAb.addPerson(ALICE);
        versionedAb.commit();

        versionedAb.undo();
        assertFalse(versionedAb.canUndo());
        assertTrue(versionedAb.canRedo());

        versionedAb.redo();
        assertTrue(versionedAb.getPersonList().contains(ALICE));
        assertFalse(versionedAb.canRedo());
    }

    @Test
    public void canUndo_atInitialState_returnsFalse() {
        VersionedAddressBook versionedAb = new VersionedAddressBook(emptyAddressBook);
        assertFalse(versionedAb.canUndo());
    }

    @Test
    public void canRedo_atLatestState_returnsFalse() {
        VersionedAddressBook versionedAb = new VersionedAddressBook(emptyAddressBook);
        versionedAb.addPerson(ALICE);
        versionedAb.commit();
        assertFalse(versionedAb.canRedo());
    }

    @Test
    public void undo_atInitialState_throwsNoUndoableStateException() {
        VersionedAddressBook versionedAb = new VersionedAddressBook(emptyAddressBook);
        assertThrows(NoUndoableStateException.class, versionedAb::undo);
    }

    @Test
    public void redo_atLatestState_throwsNoRedoableStateException() {
        VersionedAddressBook versionedAb = new VersionedAddressBook(emptyAddressBook);
        assertThrows(NoRedoableStateException.class, versionedAb::redo);
    }

    @Test
    public void commit_exceedsMaxHistory_evictsOldestState() {
        VersionedAddressBook versionedAb = new VersionedAddressBook(emptyAddressBook);
        // Commit MAX_HISTORY + 1 states (total = MAX_HISTORY + 2 including initial, but cap is MAX_HISTORY + 1)
        for (int i = 0; i < VersionedAddressBook.MAX_HISTORY; i++) {
            versionedAb.commit();
        }
        // After 20 commits, should have 21 states (initial + 20 commits)
        assertEquals(VersionedAddressBook.MAX_HISTORY + 1, versionedAb.getAddressBookStateListSize());

        // One more commit should evict the oldest
        versionedAb.commit();
        assertEquals(VersionedAddressBook.MAX_HISTORY + 1, versionedAb.getAddressBookStateListSize());
        // Pointer should be at the end
        assertEquals(VersionedAddressBook.MAX_HISTORY, versionedAb.getCurrentStatePointer());
    }

    @Test
    public void undoRedo_doesNotChangeHistorySize() {
        VersionedAddressBook versionedAb = new VersionedAddressBook(emptyAddressBook);
        versionedAb.addPerson(ALICE);
        versionedAb.commit();
        versionedAb.addPerson(BENSON);
        versionedAb.commit();
        int sizeBefore = versionedAb.getAddressBookStateListSize();

        versionedAb.undo();
        assertEquals(sizeBefore, versionedAb.getAddressBookStateListSize());
        versionedAb.redo();
        assertEquals(sizeBefore, versionedAb.getAddressBookStateListSize());
    }

    @Test
    public void equals_sameStateAndPointer() {
        VersionedAddressBook ab1 = new VersionedAddressBook(emptyAddressBook);
        VersionedAddressBook ab2 = new VersionedAddressBook(emptyAddressBook);
        assertEquals(ab1, ab2);

        ab1.addPerson(ALICE);
        ab1.commit();
        ab2.addPerson(ALICE);
        ab2.commit();
        assertEquals(ab1, ab2);
    }

    @Test
    public void equals_differentPointer() {
        VersionedAddressBook ab1 = new VersionedAddressBook(emptyAddressBook);
        VersionedAddressBook ab2 = new VersionedAddressBook(emptyAddressBook);

        ab1.addPerson(ALICE);
        ab1.commit();
        // ab2 has not committed, so pointers differ
        assertFalse(ab1.equals(ab2));
    }
}
