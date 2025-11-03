package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.model.Model;
import seedu.address.model.person.Nusnetid;
import seedu.address.model.person.Person;

/**
 * Deletes a homework entry for a specific student or for all students.
 */
public class DeleteHomeworkCommand extends Command {

    public static final String COMMAND_WORD = "delete_hw";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes a homework entry for a student or all students.\n"
            + "Parameters: "
            + CliSyntax.PREFIX_NUSNETID + "NUSNET_ID or " + CliSyntax.PREFIX_NUSNETID + "all "
            + CliSyntax.PREFIX_ASSIGNMENT + "ASSIGNMENT_ID\n"
            + "Example (single): " + COMMAND_WORD + " "
            + CliSyntax.PREFIX_NUSNETID + "E1234567 "
            + CliSyntax.PREFIX_ASSIGNMENT + "1\n"
            + "Example (all): " + COMMAND_WORD + " "
            + CliSyntax.PREFIX_NUSNETID + "all "
            + CliSyntax.PREFIX_ASSIGNMENT + "1";

    public static final String MESSAGE_DELETE_HOMEWORK_SUCCESS = "Deleted homework %d for %s";
    public static final String MESSAGE_DELETE_HOMEWORK_ALL_SUCCESS = "Deleted homework %d for ALL students";
    public static final String MESSAGE_PERSON_NOT_FOUND = "No person with NUSNET ID '%s' found.";
    public static final String MESSAGE_HOMEWORK_NOT_FOUND = "Homework %d does not exist.";

    private final Nusnetid nusnetId; // null when isAll == true
    private final int assignmentId;
    private final boolean isAll;

    /**
     * Creates a {@code DeleteHomeworkCommand} to delete the specified homework assignment
     * from the person identified by the given NUSNET ID, or from all students when isAll is true.
     */
    public DeleteHomeworkCommand(Nusnetid nusnetId, int assignmentId, boolean isAll) {
        requireNonNull(assignmentId);
        this.nusnetId = nusnetId;
        this.assignmentId = assignmentId;
        this.isAll = isAll;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        model.deleteHomework(isAll ? null : nusnetId, assignmentId);

        if (isAll) {
            return new CommandResult(String.format(MESSAGE_DELETE_HOMEWORK_ALL_SUCCESS, assignmentId));
        } else {
            // safely fetch the student after deletion succeeded
            Person targetPerson = model.getPersonByNusnetId(nusnetId);
            return new CommandResult(String.format(MESSAGE_DELETE_HOMEWORK_SUCCESS, assignmentId,
                    targetPerson.getName()));
        }
    }


    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof DeleteHomeworkCommand)) {
            return false;
        }
        DeleteHomeworkCommand o = (DeleteHomeworkCommand) other;
        return this.assignmentId == o.assignmentId
                && this.isAll == o.isAll
                && Objects.equals(this.nusnetId, o.nusnetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nusnetId, assignmentId, isAll);
    }
}
