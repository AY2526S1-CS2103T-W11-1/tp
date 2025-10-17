package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.HomeworkTracker;
import seedu.address.model.person.Person;


/**
 * Marks a student's assignment completion status.
 * <p>
 * This command updates the status of an existing assignment for a single student.
 * Valid statuses are "complete", "incomplete", or "late".
 * The assignment must already exist for the student; otherwise, a {@link CommandException} is thrown.
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * mark_hw i/E1234567 a/0 status/complete
 * }</pre>
 * This marks assignment 0 for student E1234567 as complete.
 */
public class MarkHomeworkCommand extends Command {

    public static final String COMMAND_WORD = "mark_hw";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks a student's assignment completeness.\n"
            + "Parameters: i/<nusnetId> a/<assignmentId> status/<complete|incomplete|late>\n"
            + "Example: " + COMMAND_WORD + " i/E1234567 a/0 status/complete";

    public static final String MESSAGE_SUCCESS = "Assignment %d for %s marked %s.";
    public static final String MESSAGE_STUDENT_NOT_FOUND = "Student not found.";
    public static final String MESSAGE_INVALID_ASSIGNMENT = "Assignment not found.";
    public static final String MESSAGE_INVALID_STATUS = "Please enter complete/incomplete/late only.";

    private final String nusnetId;
    private final int assignmentId;
    private final String status;

    /**
     * Creates a {@code MarkHomeworkCommand} to update a student's assignment status.
     *
     * @param nusnetId the NUSNET ID of the target student
     * @param assignmentId the assignment ID to update
     * @param status the new status ("complete", "incomplete", or "late")
     */
    public MarkHomeworkCommand(String nusnetId, int assignmentId, String status) {
        this.nusnetId = nusnetId;
        this.assignmentId = assignmentId;
        this.status = status;
    }

    /**
     * Executes the command to mark the homework status.
     * <p>
     * If the student is not found, the assignment ID is invalid, or the status is invalid,
     * a {@link CommandException} is thrown.
     * </p>
     *
     * @param model the model containing student data
     * @return a {@code CommandResult} containing a success message
     * @throws CommandException if the student is not found, the assignment does not exist, or the status is invalid
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> list = model.getFilteredPersonList();

        // find by nusnetId — adapt depending on your Person fields.
        Person target = list.stream()
                .filter(p -> {
                    // prefer using a dedicated nusnetId field if you have one:
                    try {
                        // If you have getNetId(), uncomment next line and remove the fallback
                        // return p.getNetId().value.equalsIgnoreCase(netId);
                    } catch (Exception e) {
                        System.out.println("Unable to mark the homework");
                    }
                    // fallback: match by email or name
                    return p.getNusnetid().value.equalsIgnoreCase(nusnetId);
                })
                .findFirst()
                .orElse(null);

        if (target == null) {
            throw new CommandException(MESSAGE_STUDENT_NOT_FOUND);
        }

        if (!HomeworkTracker.isValidAssignmentId(assignmentId)) {
            throw new CommandException(MESSAGE_INVALID_ASSIGNMENT);
        }

        if (!HomeworkTracker.isValidStatus(status)) {
            throw new CommandException(MESSAGE_INVALID_STATUS);
        }

        // check if assignment exists first
        if (!target.getHomeworkTracker().hasAssignment(assignmentId)) {
            throw new CommandException(
                    String.format("Assignment %d not found for %s. Add it first using 'addhw'.",
                            assignmentId, target.getName().fullName));
        }

        Person updated = target.withUpdatedHomework(assignmentId, status);
        model.setPerson(target, updated);

        return new CommandResult(String.format(MESSAGE_SUCCESS, assignmentId, updated.getName().fullName, status));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof MarkHomeworkCommand
                && nusnetId.equals(((MarkHomeworkCommand) other).nusnetId)
                && assignmentId == ((MarkHomeworkCommand) other).assignmentId
                && status.equals(((MarkHomeworkCommand) other).status));
    }
}
