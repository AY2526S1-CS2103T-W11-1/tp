package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NUSNETID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEEK;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.AttendanceStatus;
import seedu.address.model.person.Nusnetid;
import seedu.address.model.person.Person;

/**
 * Marks a student's attendance status.
 * This command updates the attendance status for a specific week for a student identified by their NUSNETID.
 * Valid statuses are "present", "absent", or "excused".
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * mark_attendance  i/E1234567 w/3 present
 * }</pre>
 * This marks week 3 attendance for student E1234567 as present.
 */
public class MarkAttendanceCommand extends Command {
    public static final String COMMAND_WORD = "mark_attendance";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Marks the attendance of a student identified "
            + "by their their NUSNETID. "
            + "Parameters: i/<NET id> w/<week> status/<present|absent|excused> \n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_NUSNETID + "E1234567 " + PREFIX_WEEK + "3 "
            + PREFIX_STATUS + "present";

    public static final String MESSAGE_MARK_ATTENDANCE_SUCCESS = "Marked attendance for %1$s: %2$s in week %3$d.";
    public static final String MESSAGE_STUDENT_NOT_FOUND = "Student not found.";
    public static final String MESSAGE_INVALID_WEEK = "Invalid Week. "
          + "Week should be between 2 and 13 and be a positive integer.";
    public static final String MESSAGE_INVALID_STATUS = "Please enter present/absent/excused only.";

    private final Nusnetid nusnetId;
    private final int week;
    private final AttendanceStatus attendanceStatus;


    /**
     * Creates a {@code MarkAttendanceCommand} to update a student's attendance status.
     *
     * @param nusnetId the NUSNET ID of the target student
     * @param week the week number to update
     * @param attendanceStatus the new attendance status ("present", "absent", or "excused")
     */
    public MarkAttendanceCommand(Nusnetid nusnetId, int week, AttendanceStatus attendanceStatus) {
        this.week = week;
        this.nusnetId = nusnetId;
        this.attendanceStatus = attendanceStatus;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (!model.hasPerson(nusnetId)) {
            throw new CommandException(MESSAGE_STUDENT_NOT_FOUND);
        }
        Person updatedStudent = model.markAttendance(this.nusnetId, this.week, attendanceStatus);
        return new CommandResult(String.format(MESSAGE_MARK_ATTENDANCE_SUCCESS,
                updatedStudent.getName(), attendanceStatus.getStatus(), week));
    }
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MarkAttendanceCommand)) {
            return false;
        }
        MarkAttendanceCommand otherCommand = (MarkAttendanceCommand) other;
        return nusnetId.equals(otherCommand.nusnetId)
                && week == otherCommand.week
                && attendanceStatus.equals(otherCommand.attendanceStatus);
    }

}
