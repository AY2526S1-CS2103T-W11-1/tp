package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Attendance;
import seedu.address.model.person.AttendanceSheet;
import seedu.address.model.person.AttendanceStatus;
import seedu.address.model.person.Person;

/**
 * Marks a student's attendance status.
 * This command updates the attendance status for a specific week for a student identified by their NUSNET ID.
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

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Marks the attendance of a person identified "
            + "by their index number in the displayed person list. "
            + "Parameters: i/<NET id> w/<week> <present|absent|excused> \n"
            + "Example: " + COMMAND_WORD + " i/E1234567 w/3" + " present";

    public static final String MESSAGE_MARK_ATTENDANCE_SUCCESS = "Marked attendance for %1$s: %2$s in week %3$d.";
    public static final String MESSAGE_STUDENT_NOT_FOUND = "Student not found.";
    public static final String MESSAGE_INVALID_WEEK = "Invalid Week.";
    public static final String MESSAGE_INVALID_STATUS = "Please enter present/absent/excused only.";
    public static final String MESSAGE_MISSING_FIELD = "Missing required field: %s.";

    private final String nusnetId;
    private final int week;
    private final String attendanceStatus;


    /**
     * Creates a {@code MarkAttendanceCommand} to update a student's attendance status.
     *
     * @param nusnetId the NUSNET ID of the target student
     * @param week the week number to update
     * @param attendanceStatus the new attendance status ("present", "absent", or "excused")
     */
    public MarkAttendanceCommand(String nusnetId, int week, String attendanceStatus) {
        this.week = week;
        this.nusnetId = nusnetId;
        this.attendanceStatus = attendanceStatus;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> list = model.getFilteredPersonList();
        if (week < 2 || week > 13) {
            throw new CommandException(MESSAGE_INVALID_WEEK);
        }
        AttendanceStatus status = AttendanceStatus.fromString(attendanceStatus);
        List<Person> students = model.getFilteredPersonList();
        Person targetStudent = list.stream()
                .filter(student -> student.getNusnetid().value.equalsIgnoreCase(nusnetId))
                .findFirst()
                .orElse(null);
        if (targetStudent == null) {
            throw new CommandException(MESSAGE_STUDENT_NOT_FOUND);
        }

        if (status == null) {
            throw new CommandException(MESSAGE_INVALID_STATUS);
        }

        AttendanceSheet updatedSheet = new AttendanceSheet();
        for (Attendance attendance : targetStudent.getAttendanceSheet().getAttendanceList()) {
            updatedSheet.markAttendance(attendance.getWeek(), attendance.getAttendanceStatus());
        }
        updatedSheet.markAttendance(week, status);

        Person updatedStudent = new Person(
                targetStudent.getName(),
                targetStudent.getPhone(),
                targetStudent.getEmail(),
                targetStudent.getNusnetid(),
                targetStudent.getTelegram(),
                targetStudent.getGroupId(),
                targetStudent.getHomeworkTracker(),
                updatedSheet,
                targetStudent.getConsultation());

        model.setPerson(targetStudent, updatedStudent);
        return new CommandResult(String.format(MESSAGE_MARK_ATTENDANCE_SUCCESS,
                updatedStudent.getName(), status.getStatus(), week));
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
