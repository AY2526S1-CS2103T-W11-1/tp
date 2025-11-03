package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NUSNETID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Email;
import seedu.address.model.person.GroupId;
import seedu.address.model.person.HomeworkTracker;
import seedu.address.model.person.Name;
import seedu.address.model.person.Nusnetid;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Telegram;


/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_NUSNETID,
                        PREFIX_TELEGRAM, PREFIX_GROUP, PREFIX_PHONE, PREFIX_EMAIL);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_NUSNETID,
                PREFIX_TELEGRAM, PREFIX_GROUP)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_NUSNETID,
                PREFIX_TELEGRAM, PREFIX_GROUP);

        // Collect errors from parsing each individual field so multiple invalid inputs are reported together.
        List<String> errors = new ArrayList<>();

        Name name = null;
        Phone phone = null;
        Email email = null;
        Nusnetid nusnetid = null;
        Telegram telegram = null;
        GroupId groupId = null;

        try {
            name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        } catch (ParseException e) {
            errors.add("Name: " + e.getMessage());
        }

        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            try {
                phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
            } catch (ParseException e) {
                errors.add("Phone: " + e.getMessage());
            }
        }

        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            try {
                email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
            } catch (ParseException e) {
                errors.add("Email: " + e.getMessage());
            }
        }

        try {
            nusnetid = ParserUtil.parseNusnetid(argMultimap.getValue(PREFIX_NUSNETID).get());
        } catch (ParseException e) {
            errors.add("NUSNET ID: " + e.getMessage());
        }

        try {
            telegram = ParserUtil.parseTelegram(argMultimap.getValue(PREFIX_TELEGRAM).get());
        } catch (ParseException e) {
            errors.add("Telegram: " + e.getMessage());
        }

        try {
            groupId = ParserUtil.parseGroupId(argMultimap.getValue(PREFIX_GROUP).get());
        } catch (ParseException e) {
            errors.add("Group: " + e.getMessage());
        }

        if (!errors.isEmpty()) {
            String combined = errors.stream().collect(Collectors.joining(System.lineSeparator()));
            throw new ParseException(combined);
        }

        Person person = new Person(name, phone, email, nusnetid, telegram, groupId, new HomeworkTracker());

        return new AddCommand(person);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
