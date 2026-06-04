package seedu.address.model;

/**
 * Signals that the current state pointer is at the end of the address book state list,
 * and therefore redo cannot be performed.
 */
public class NoRedoableStateException extends RuntimeException {
    public NoRedoableStateException() {
        super("Current state pointer at end of addressBookStateList, unable to redo.");
    }
}
