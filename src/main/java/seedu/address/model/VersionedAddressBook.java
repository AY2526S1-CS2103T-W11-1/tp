package seedu.address.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A versioned address book that supports undo/redo operations.
 * Maintains a history of address book states with a maximum of {@value MAX_HISTORY} states.
 */
public class VersionedAddressBook extends AddressBook {

    public static final int MAX_HISTORY = 20;

    private final List<ReadOnlyAddressBook> addressBookStateList;
    private int currentStatePointer;

    /**
     * Creates a VersionedAddressBook with the given initial state.
     * The initial state is recorded as the first entry in the history.
     */
    public VersionedAddressBook(ReadOnlyAddressBook initialState) {
        super(initialState);
        addressBookStateList = new ArrayList<>();
        addressBookStateList.add(new AddressBook(initialState));
        currentStatePointer = 0;
    }

    /**
     * Saves the current address book state to the history.
     * Any states after the current pointer (redo stack) are discarded.
     * If the history exceeds {@value MAX_HISTORY} states, the oldest state is removed.
     */
    public void commit() {
        // Discard states after current pointer (truncate redo stack)
        for (int i = addressBookStateList.size() - 1; i > currentStatePointer; i--) {
            addressBookStateList.remove(i);
        }
        // Add new snapshot
        addressBookStateList.add(new AddressBook(this));
        currentStatePointer++;
        // Enforce history limit
        while (addressBookStateList.size() > MAX_HISTORY + 1) {
            addressBookStateList.remove(0);
            currentStatePointer--;
        }
    }

    /**
     * Restores the address book to its previous state.
     * @throws NoUndoableStateException if there is no state to undo to.
     */
    public void undo() {
        if (!canUndo()) {
            throw new NoUndoableStateException();
        }
        currentStatePointer--;
        resetData(addressBookStateList.get(currentStatePointer));
    }

    /**
     * Restores the address book to the state that was undone.
     * @throws NoRedoableStateException if there is no state to redo to.
     */
    public void redo() {
        if (!canRedo()) {
            throw new NoRedoableStateException();
        }
        currentStatePointer++;
        resetData(addressBookStateList.get(currentStatePointer));
    }

    /**
     * Returns true if there is a previous state to undo to.
     */
    public boolean canUndo() {
        return currentStatePointer > 0;
    }

    /**
     * Returns true if there is a next state to redo to.
     */
    public boolean canRedo() {
        return currentStatePointer < addressBookStateList.size() - 1;
    }

    /**
     * Returns the current state pointer index.
     */
    public int getCurrentStatePointer() {
        return currentStatePointer;
    }

    /**
     * Returns the number of states in the history list.
     */
    public int getAddressBookStateListSize() {
        return addressBookStateList.size();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof VersionedAddressBook)) {
            return false;
        }
        VersionedAddressBook otherVersioned = (VersionedAddressBook) other;
        return super.equals(otherVersioned)
                && currentStatePointer == otherVersioned.currentStatePointer;
    }

    @Override
    public int hashCode() {
        return super.hashCode() * 31 + currentStatePointer;
    }
}
