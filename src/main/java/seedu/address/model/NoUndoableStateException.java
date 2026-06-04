package seedu.address.model;

/**
 * Signals that the current state pointer is at the start of the address book state list,
 * and therefore undo cannot be performed.
 */
public class NoUndoableStateException extends RuntimeException {
    public NoUndoableStateException() {
        super("Current state pointer at start of addressBookStateList, unable to undo.");
    }
}
