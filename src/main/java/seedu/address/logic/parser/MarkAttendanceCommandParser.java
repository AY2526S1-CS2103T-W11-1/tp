package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.MarkAttendanceCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@link MarkAttendanceCommand} object.
 * <p>
 * The expected input format is:
 * <pre>{@code
 * w/<week> <present|absent|excused> n/<nusnetId>
 * }</pre>
 * Week must be an integer between 2 and 13, and the status must be one of
 * "present", "absent", or "excused".
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * mark_attendance i/E1234567 w/3 present  // marks week 3 attendance for student E1234567 as present
 * }</pre>
 */
public class MarkAttendanceCommandParser implements Parser<MarkAttendanceCommand> {
    private static final Pattern MARK_COMMAND_FORMAT = Pattern.compile(
            "i/(?<nusnetId>\\S+)\\s+w/(?<week>\\d+)\\s+(?<status>\\S+)",
            Pattern.CASE_INSENSITIVE);
    @Override
    public MarkAttendanceCommand parse(String args) throws ParseException {
        final Matcher matcher = MARK_COMMAND_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    MarkAttendanceCommand.MESSAGE_USAGE));
        }
        int week;
        try {
            week = Integer.parseInt(matcher.group("week"));
            if (week < 2 || week > 13) {
                throw new ParseException(MarkAttendanceCommand.MESSAGE_INVALID_WEEK);
            }
        } catch (NumberFormatException e) {
            throw new ParseException(MarkAttendanceCommand.MESSAGE_INVALID_WEEK);
        }
        String nusnetId = matcher.group("nusnetId");
        String status = matcher.group("status").toLowerCase();
        if (!status.equals("present") && !status.equals("absent") && !status.equals("excused")) {
            throw new ParseException(MarkAttendanceCommand.MESSAGE_INVALID_STATUS);
        }
        return new MarkAttendanceCommand(nusnetId, week, status);
    }
}
