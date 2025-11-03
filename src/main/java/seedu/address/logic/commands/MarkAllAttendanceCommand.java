package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEEK;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.AttendanceStatus;
import seedu.address.model.person.GroupId;


/**
 * Marks the attendance of all students in a specified group for a given week.
 * This command allows batch updating of attendance status (present, absent, or excused)
 * for all members of a tutorial group at once.
 * <p>
 * The command validates that the week number is between 2 and 13, and that the
 * attendance status is one of the valid values. After marking attendance, the
 * filtered person list is updated to show only students in the specified group.
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * mark_all_attendance g/T02 w/3 present
 * }</pre>
 * This marks week 3 attendance for all students in group T02 as present.
 */

public class MarkAllAttendanceCommand extends Command {
    public static final String COMMAND_WORD = "mark_all_attendance";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Marks the attendance of all the students in one group "
            + "by their group number. "
            + "Parameters: g/<group> w/<week> status/<present|absent|excused> \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_GROUP + "T02 " + PREFIX_WEEK + "3 " + PREFIX_STATUS + "present";

    public static final String MESSAGE_MARK_ATTENDANCE_SUCCESS = "Marked attendance for Group %1$s: "
            + "%2$s in week %3$d.\n";
    public static final String MESSAGE_GROUP_NOT_FOUND = "Group not found.";
    public static final String MESSAGE_INVALID_WEEK = "Invalid Week. "
            + "Week should be between 2 and 13 and be a positive integer.";
    public static final String MESSAGE_INVALID_STATUS = "Please enter present/absent/excused only.";

    private final GroupId groupId;
    private final int week;
    private final AttendanceStatus attendanceStatus;


    /**
     * Creates a {@code MarkAttendanceCommand} to update a student's attendance status.
     *
     * @param groupId the group ID of the target students
     * @param week the week number to update
     * @param attendanceStatus the new attendance status ("present", "absent", or "excused")
     */
    public MarkAllAttendanceCommand(GroupId groupId, int week, AttendanceStatus attendanceStatus) {
        this.week = week;
        this.groupId = groupId;
        this.attendanceStatus = attendanceStatus;
    }

    /**
     * Executes the command to mark attendance for all students in the specified group.
     * @param model {@code Model} which the command should operate on.
     * @return
     * @throws CommandException
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (!model.hasGroup(groupId)) {
            throw new CommandException(MESSAGE_GROUP_NOT_FOUND);
        }
        model.markAllAttendance(groupId, week, attendanceStatus);
        return new CommandResult(String.format(MESSAGE_MARK_ATTENDANCE_SUCCESS,
                    groupId, attendanceStatus.getStatus(), week));
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MarkAllAttendanceCommand)) {
            return false;
        }
        MarkAllAttendanceCommand otherCommand = (MarkAllAttendanceCommand) other;
        return groupId.equals(otherCommand.groupId)
                && week == otherCommand.week
                && attendanceStatus.equals(otherCommand.attendanceStatus);
    }

}
