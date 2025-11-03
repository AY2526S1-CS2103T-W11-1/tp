package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ASSIGNMENT_1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NUSNETID_AMY;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.MarkHomeworkCommand;
import seedu.address.model.person.Nusnetid;

public class MarkHomeworkCommandParserTest {

    private MarkHomeworkCommandParser parser = new MarkHomeworkCommandParser();

    @Test
    public void parse_validArgs_success() {
        // Single student
        assertParseSuccess(parser, " i/" + VALID_NUSNETID_AMY + " a/" + VALID_ASSIGNMENT_1 + " status/complete",
                new MarkHomeworkCommand(new Nusnetid(VALID_NUSNETID_AMY), 1, "complete"));
    }

    @Test
    public void parse_invalidAssignmentId_failure() {
        // ID out of allowed range
        assertParseFailure(parser, " i/" + VALID_NUSNETID_AMY + " a/14" + " status/complete",
                "Homework id must be between 1 and 13.");
    }

    @Test
    public void parse_missingFields_failure() {
        // Missing assignment ID
        assertParseFailure(parser, " i/" + VALID_NUSNETID_AMY,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkHomeworkCommand.MESSAGE_USAGE));

        // Missing NUSNET ID
        assertParseFailure(parser, " a/1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkHomeworkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidNusnetId_failure() {
        // Empty or invalid nusnet ID
        assertParseFailure(parser, " i/ a/1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkHomeworkCommand.MESSAGE_USAGE));
    }
}
