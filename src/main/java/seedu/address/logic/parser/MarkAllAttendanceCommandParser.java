package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEEK;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import seedu.address.logic.commands.MarkAllAttendanceCommand;
import seedu.address.logic.commands.MarkAttendanceCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.AttendanceStatus;
import seedu.address.model.person.GroupId;

/**
 * Parses input arguments and creates a new {@link MarkAllAttendanceCommand} object.
 * <p>
 * The expected input format is:
 * <pre>{@code
 * g/<group> w/<week> status/<present|absent|excused>
 * }</pre>
 * Week must be an integer between 2 and 13, and the status must be one of
 * "present", "absent", or "excused".
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * mark_all_attendance g/T02 w/3 status/present  // marks week 3 attendance for all students in group T02 as present
 * }</pre>
 */
public class MarkAllAttendanceCommandParser implements Parser<MarkAllAttendanceCommand> {

    @Override
    public MarkAllAttendanceCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_GROUP, PREFIX_WEEK,
                PREFIX_STATUS);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_GROUP, PREFIX_WEEK, PREFIX_STATUS);

        if (!arePrefixesPresent(argMultimap, PREFIX_GROUP, PREFIX_WEEK, PREFIX_STATUS)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    MarkAllAttendanceCommand.MESSAGE_USAGE));
        }

        String groupIdRaw = argMultimap.getValue(PREFIX_GROUP).get().trim();
        String weekRaw = argMultimap.getValue(PREFIX_WEEK).get().trim();
        String statusRaw = argMultimap.getValue(PREFIX_STATUS).get().trim();
        List<String> errors = new ArrayList<>();
        int week = 0;
        try {
            week = Integer.parseInt(weekRaw);
            if (week < 2 || week > 13) {
                errors.add(MarkAttendanceCommand.MESSAGE_INVALID_WEEK);
            }
        } catch (NumberFormatException e) {
            errors.add(MarkAttendanceCommand.MESSAGE_INVALID_WEEK);
        }
        AttendanceStatus status = null;
        try {
            status = AttendanceStatus.fromString(statusRaw);
        } catch (IllegalArgumentException e) {
            errors.add(MarkAllAttendanceCommand.MESSAGE_INVALID_STATUS);
        }
        GroupId targetGroupId = null;
        if (GroupId.isValidGroupId(groupIdRaw)) {
            targetGroupId = new GroupId(groupIdRaw);
        } else {
            errors.add(GroupId.MESSAGE_CONSTRAINTS);
        }

        if (!errors.isEmpty()) {
            throw new ParseException(String.join(System.lineSeparator(), errors));
        }
        return new MarkAllAttendanceCommand(targetGroupId, week, status);
    }
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
