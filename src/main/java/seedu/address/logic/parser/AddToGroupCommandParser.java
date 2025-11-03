package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.stream.Stream;

import seedu.address.logic.commands.AddToGroupCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.GroupId;
import seedu.address.model.person.Nusnetid;

/**
 * Parses input arguments and creates a new AddToGroupCommand object
 */
public class AddToGroupCommandParser implements Parser<AddToGroupCommand> {
    @Override
    public AddToGroupCommand parse(String args) throws ParseException {
        ArgumentMultimap argumentMultimap = ArgumentTokenizer.tokenize(args, CliSyntax.PREFIX_GROUP,
                CliSyntax.PREFIX_NUSNETID);
        if (!arePrefixesPresent(argumentMultimap, CliSyntax.PREFIX_GROUP, CliSyntax.PREFIX_NUSNETID)
                || !argumentMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddToGroupCommand.MESSAGE_USAGE));
        }

        argumentMultimap.verifyNoDuplicatePrefixesFor(CliSyntax.PREFIX_GROUP, CliSyntax.PREFIX_NUSNETID);

        GroupId groupId = ParserUtil.parseGroupId(argumentMultimap.getValue(CliSyntax.PREFIX_GROUP).get());
        Nusnetid nusnetId = ParserUtil.parseNusnetid(argumentMultimap.getValue(CliSyntax.PREFIX_NUSNETID).get());

        return new AddToGroupCommand(nusnetId, groupId);
    }
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
