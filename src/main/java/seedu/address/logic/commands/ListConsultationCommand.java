package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_CONSULTATIONS;

import seedu.address.model.Model;

/**
 * Lists all consultations in the address book to the user.
 */
public class ListConsultationCommand extends Command {

    public static final String COMMAND_WORD = "list_consult";

    public static final String MESSAGE_SUCCESS = "Listed all consultations";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredConsultationList(PREDICATE_SHOW_ALL_CONSULTATIONS);
        return new CommandResult(MESSAGE_SUCCESS, false, false, true);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ListConsultationCommand)) {
            return false;
        } else {
            return true;
        }
    }
}
