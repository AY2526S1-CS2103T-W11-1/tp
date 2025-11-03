package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.model.Model;
import seedu.address.model.person.Nusnetid;
import seedu.address.model.person.Person;

/**
 * Adds a homework assignment to a specific student or to all students.
 * <p>
 * This command supports two modes:
 * <ul>
 *     <li>Adding a homework to a single student identified by their NUSNET ID.</li>
 *     <li>Adding a homework to all students using the keyword "all".</li>
 * </ul>
 * The default status of any newly added homework is "incomplete".
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * add_hw i/E1234567 a/1    // adds assignment 1 to student E1234567
 * add_hw all a/1           // adds assignment 1 to all students
 * }</pre>
 */

public class AddHomeworkCommand extends Command {

    public static final String COMMAND_WORD = "add_hw";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a homework to a student or to all students.\n"
            + "Parameters: "
            + CliSyntax.PREFIX_NUSNETID + "NUSNET_ID or " + CliSyntax.PREFIX_NUSNETID + "all "
            + CliSyntax.PREFIX_ASSIGNMENT + "ASSIGNMENT_ID\n"
            + "Example (single): " + COMMAND_WORD + " "
            + CliSyntax.PREFIX_NUSNETID
            + "E1234567 "
            + CliSyntax.PREFIX_ASSIGNMENT + "1\n"
            + "Example (all): " + COMMAND_WORD + " "
            + CliSyntax.PREFIX_NUSNETID
            + "all "
            + CliSyntax.PREFIX_ASSIGNMENT + "1";

    public static final String MESSAGE_SUCCESS_ONE = "Added assignment %d for %s (default incomplete).";
    public static final String MESSAGE_SUCCESS_ALL = "Added assignment %d for all students (default incomplete).";
    public static final String MESSAGE_STUDENT_NOT_FOUND = "Student not found.";

    private static final Logger logger = LogsCenter.getLogger(AddHomeworkCommand.class);
    private final Nusnetid nusnetId; // can be "all" for all students
    private final int assignmentId;
    private final boolean isAll;

    /**
     * Creates an {@code AddHomeworkCommand} to add a homework to a student or all students.
     *
     * @param nusnetId the nusnetId ID of the target student, or "all" to apply to all students
     * @param assignmentId the ID of the assignment to add
     */
    public AddHomeworkCommand(Nusnetid nusnetId, int assignmentId, boolean isAll) {
        this.nusnetId = nusnetId;
        this.assignmentId = assignmentId;
        this.isAll = isAll;
    }

    /**
     * Executes the {@code AddHomeworkCommand}.
     * <p>
     * If the {@code NusnetId} is "all", the assignment is added to every student in the model.
     * Otherwise, it is added only to the student with the matching NUSNET ID.
     * </p>
     *
     * @param model the model containing the student data
     * @return a {@code CommandResult} containing a success message
     * @throws CommandException if the target student is not found
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        String target = isAll ? "all students" : nusnetId.value;
        logger.info("Executing AddHomeworkCommand for: " + target);

        if (isAll) {
            model.addHomework(null, assignmentId);
            return new CommandResult(String.format(MESSAGE_SUCCESS_ALL, assignmentId));
        } else {
            model.addHomework(nusnetId, assignmentId);
            // single student
            Person targetPerson = model.getPersonByNusnetId(nusnetId); // fetch the Person
            return new CommandResult(String.format(MESSAGE_SUCCESS_ONE, assignmentId, targetPerson.getName()));
        }
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AddHomeworkCommand)) {
            return false;
        }
        AddHomeworkCommand o = (AddHomeworkCommand) other;
        return this.assignmentId == o.assignmentId
                && this.isAll == o.isAll
                && Objects.equals(this.nusnetId, o.nusnetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nusnetId, assignmentId, isAll);
    }

}
