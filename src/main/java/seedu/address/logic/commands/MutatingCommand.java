package seedu.address.logic.commands;

/**
 * Marker interface for commands that modify the AddressBook state.
 * Commands implementing this interface will trigger a state commit after successful execution,
 * enabling undo/redo support.
 *
 * @see UndoCommand
 * @see RedoCommand
 */
public interface MutatingCommand {
}
