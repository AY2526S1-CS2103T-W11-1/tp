package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NUSNETID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;

import seedu.address.logic.commands.MarkHomeworkCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Nusnetid;

/**
 * Parses input arguments and creates a new {@link MarkHomeworkCommand} object.
 * <p>
 * The expected input format is:
 * <pre>{@code
 * i/<nusnetId> a/<assignmentId> status/<complete|incomplete|late>
 * }</pre>
 * Homework IDs must be integers between 0 and 2, and the status must be one of
 * "complete", "incomplete", or "late".
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * mark i/E1234567 a/1 status/complete    // marks assignment 0 for student E1234567 as complete
 * }</pre>
 */
public class MarkHomeworkCommandParser implements Parser<MarkHomeworkCommand> {

    @Override
    public MarkHomeworkCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
                PREFIX_NUSNETID, PREFIX_ASSIGNMENT, PREFIX_STATUS);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NUSNETID, PREFIX_ASSIGNMENT, PREFIX_STATUS);

        if (!arePrefixesPresent(argMultimap, PREFIX_NUSNETID, PREFIX_ASSIGNMENT, PREFIX_STATUS)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkHomeworkCommand.MESSAGE_USAGE));
        }

        String nusnetIdRaw = argMultimap.getValue(PREFIX_NUSNETID)
                .orElseThrow(() -> new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkHomeworkCommand.MESSAGE_USAGE)))
                .trim();
        Nusnetid nusnetId;
        try {
            nusnetId = new Nusnetid(nusnetIdRaw);
        } catch (IllegalArgumentException e) {
            throw new ParseException(Nusnetid.MESSAGE_CONSTRAINTS);
        }

        String assignmentRaw = argMultimap.getValue(PREFIX_ASSIGNMENT)
                .orElseThrow(() -> new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkHomeworkCommand.MESSAGE_USAGE)))
                .trim();
        int assignmentId;
        try {
            assignmentId = Integer.parseInt(assignmentRaw);
        } catch (NumberFormatException e) {
            throw new ParseException("Homework id must be an integer between 1 and 13.");
        }
        if (assignmentId < 1 || assignmentId > 13) {
            throw new ParseException("Homework id must be between 1 and 13.");
        }

        String status = argMultimap.getValue(PREFIX_STATUS)
                .orElseThrow(() -> new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkHomeworkCommand.MESSAGE_USAGE)))
                .trim().toLowerCase();

        if (!status.equals("complete") && !status.equals("incomplete") && !status.equals("late")) {
            throw new ParseException("Status must be one of: complete, incomplete, late.");
        }

        return new MarkHomeworkCommand(nusnetId, assignmentId, status);
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
