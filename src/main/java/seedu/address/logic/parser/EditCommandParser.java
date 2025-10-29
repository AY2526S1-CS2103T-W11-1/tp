package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NUSNETID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                        PREFIX_NUSNETID, PREFIX_TELEGRAM);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_NUSNETID,
                PREFIX_TELEGRAM, PREFIX_PHONE, PREFIX_EMAIL);

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        List<String> errors = new ArrayList<>();

        // Name
        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            try {
                editPersonDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
            } catch (ParseException e) {
                errors.add("Name: " + e.getMessage());
            }
        }
        // Phone (optional)
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            try {
                editPersonDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
            } catch (ParseException e) {
                errors.add("Phone: " + e.getMessage());
            }
        }
        // Email (optional)
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            try {
                editPersonDescriptor.setEmail(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get()));
            } catch (ParseException e) {
                errors.add("Email: " + e.getMessage());
            }
        }
        // Nusnetid
        if (argMultimap.getValue(PREFIX_NUSNETID).isPresent()) {
            try {
                editPersonDescriptor.setNusnetid(ParserUtil.parseNusnetid(argMultimap.getValue(PREFIX_NUSNETID).get()));
            } catch (ParseException e) {
                errors.add("Nusnetid: " + e.getMessage());
            }
        }
        // Telegram
        if (argMultimap.getValue(PREFIX_TELEGRAM).isPresent()) {
            try {
                editPersonDescriptor.setTelegram(ParserUtil.parseTelegram(argMultimap.getValue(PREFIX_TELEGRAM).get()));
            } catch (ParseException e) {
                errors.add("Telegram: " + e.getMessage());
            }
        }

        // If there were validation errors for any fields, report them together.
        if (!errors.isEmpty()) {
            throw new ParseException(String.join(System.lineSeparator(), errors));
        }

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editPersonDescriptor);
    }

}
