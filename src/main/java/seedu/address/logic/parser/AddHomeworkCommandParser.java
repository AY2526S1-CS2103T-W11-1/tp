package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NUSNETID;

import seedu.address.logic.commands.AddHomeworkCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Nusnetid;

/**
 * Parses input arguments and creates a new {@link AddHomeworkCommand} object.
 * <p>
 * The expected format of the input is either:
 * <ul>
 *     <li>{@code i/<nusnetId> a/<assignmentId>} to add homework to a specific student</li>
 *     <li>{@code i/all a/<assignmentId>} to add homework to all students</li>
 * </ul>
 * Homework IDs must be integers between 1 and 13.
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * addhw i/E1234567 a/1    // adds assignment 1 to student E1234567
 * addhw i/all a/2           // adds assignment 2 to all students
 * }</pre>
 */
public class AddHomeworkCommandParser implements Parser<AddHomeworkCommand> {

    @Override
    public AddHomeworkCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NUSNETID, PREFIX_ASSIGNMENT);
        if (!arePrefixesPresent(argMultimap, PREFIX_NUSNETID, PREFIX_ASSIGNMENT)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddHomeworkCommand.MESSAGE_USAGE));
        }

        try {
            argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NUSNETID, PREFIX_ASSIGNMENT);
        } catch (ParseException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddHomeworkCommand.MESSAGE_USAGE));
        }

        String assignmentRaw = argMultimap.getValue(PREFIX_ASSIGNMENT)
                .orElseThrow(() -> new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddHomeworkCommand.MESSAGE_USAGE)))
                .trim();
        if (!assignmentRaw.matches("\\d+")) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddHomeworkCommand.MESSAGE_USAGE));
        }

        int assignmentId;
        try {
            assignmentId = Integer.parseInt(assignmentRaw);
        } catch (NumberFormatException e) {
            throw new ParseException("Homework id must be an integer between 1 and 13.");
        }
        if (assignmentId < 1 || assignmentId > 13) {
            throw new ParseException("Homework id must be between 1 and 13.");
        }

        String nusnetIdRaw = argMultimap.getValue(PREFIX_NUSNETID)
                .orElseThrow(() -> new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddHomeworkCommand.MESSAGE_USAGE)))
                .trim();

        if (nusnetIdRaw.equalsIgnoreCase("all")) {
            return new AddHomeworkCommand(null, assignmentId, true);
        }

        try {
            Nusnetid nusnetid = new Nusnetid(nusnetIdRaw);
            return new AddHomeworkCommand(nusnetid, assignmentId, false);
        } catch (IllegalArgumentException e) {
            throw new ParseException(Nusnetid.MESSAGE_CONSTRAINTS);
        }
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        for (Prefix prefix : prefixes) {
            if (argumentMultimap.getValue(prefix).isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
