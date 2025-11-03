package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ASSIGNMENT_1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NUSNETID_AMY;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddHomeworkCommand;
import seedu.address.model.person.Nusnetid;

public class AddHomeworkCommandParserTest {

    private AddHomeworkCommandParser parser = new AddHomeworkCommandParser();

    @Test
    public void parse_validArgs_success() {
        // Single student
        assertParseSuccess(parser, " i/" + VALID_NUSNETID_AMY + " a/" + VALID_ASSIGNMENT_1,
                new AddHomeworkCommand(new Nusnetid(VALID_NUSNETID_AMY), 1, false));
    }

    @Test
    public void parse_validArgsAllStudents_success() {
        // All students must use i/all
        assertParseSuccess(parser, " i/all a/2",
                new AddHomeworkCommand(null, 2, true));
    }

    @Test
    public void parse_invalidAssignmentId_failure() {
        assertParseFailure(parser, " i/" + VALID_NUSNETID_AMY + " a/100",
                "Homework id must be between 1 and 13.");
    }

    @Test
    public void parse_missingFields_failure() {
        assertParseFailure(parser, " a/1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddHomeworkCommand.MESSAGE_USAGE));

        assertParseFailure(parser, " i/" + VALID_NUSNETID_AMY,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddHomeworkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidNusnetId_failure() {
        // Empty NUSNET ID should be rejected by the parser
        assertParseFailure(parser, " i/ a/1",
                Nusnetid.MESSAGE_CONSTRAINTS);
    }
}
