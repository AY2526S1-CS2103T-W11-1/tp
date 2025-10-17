package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
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
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a homework to a student or all students.\n"
            + "Parameters: i/NETID a/ASSIGNMENT_ID or all a/ASSIGNMENT_ID\n"
            + "Example: " + COMMAND_WORD + " i/E1234567 a/1\n"
            + "Example (all): " + COMMAND_WORD + " all a/1";;

    public static final String MESSAGE_SUCCESS_ONE = "Added assignment %d for %s (default incomplete).";
    public static final String MESSAGE_SUCCESS_ALL = "Added assignment %d for all students (default incomplete).";
    public static final String MESSAGE_STUDENT_NOT_FOUND = "Student not found.";

    private final String nusnetId; // can be "all" for all students
    private final int assignmentId;

    /**
     * Creates an {@code AddHomeworkCommand} to add a homework to a student or all students.
     *
     * @param nusnetId the nusnetId ID of the target student, or "all" to apply to all students
     * @param assignmentId the ID of the assignment to add
     */
    public AddHomeworkCommand(String nusnetId, int assignmentId) {
        this.nusnetId = nusnetId;
        this.assignmentId = assignmentId;
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

        if (nusnetId.equalsIgnoreCase("all")) {
            // Check duplicates BEFORE modifying any student
            for (Person p : model.getFilteredPersonList()) {
                if (p.getHomeworkTracker().contains(assignmentId)) {
                    throw new CommandException(
                            String.format("Assignment %d already exists for %s.", assignmentId, p.getName())
                    );
                }
            }
            // add homework for every student
            for (Person p : model.getFilteredPersonList()) {
                model.setPerson(p, p.withAddedHomework(assignmentId));
            }
            return new CommandResult(String.format(MESSAGE_SUCCESS_ALL, assignmentId));
        } else {
            Person target = model.getFilteredPersonList().stream()
                    .filter(p -> p.getNusnetid().value.equalsIgnoreCase(nusnetId))
                    .findFirst()
                    .orElse(null);

            if (target == null) {
                throw new CommandException(MESSAGE_STUDENT_NOT_FOUND);
            }

            if (target.getHomeworkTracker().contains(assignmentId)) {
                throw new CommandException(
                        String.format("Assignment %d already exists for %s.", assignmentId, target.getName())
                );
            }

            Person updated = target.withAddedHomework(assignmentId);
            model.setPerson(target, updated);

            return new CommandResult(String.format(MESSAGE_SUCCESS_ONE, assignmentId, target.getName()));
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
        AddHomeworkCommand otherCommand = (AddHomeworkCommand) other;
        return this.nusnetId.equals(otherCommand.nusnetId)
                && this.assignmentId == otherCommand.assignmentId;
    }

    @Override
    public int hashCode() {
        return nusnetId.hashCode() * 31 + assignmentId;
    }

}
