package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ASSIGNMENT_1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NUSNETID_AMY;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteHomeworkCommand;
import seedu.address.model.person.Nusnetid;

public class DeleteHomeworkCommandParserTest {

    private DeleteHomeworkCommandParser parser = new DeleteHomeworkCommandParser();

    @Test
    public void parse_validArgs_success() {
        // Single student
        assertParseSuccess(parser, " i/" + VALID_NUSNETID_AMY + " a/" + VALID_ASSIGNMENT_1,
                new DeleteHomeworkCommand(new Nusnetid(VALID_NUSNETID_AMY), 1, false));
    }

    @Test
    public void parse_validArgsAllStudents_success() {
        // All students
        assertParseSuccess(parser, " i/all a/2",
                new DeleteHomeworkCommand(null, 2, true));
    }

    @Test
    public void parse_invalidAssignmentId_failure() {
        assertParseFailure(parser, " i/" + VALID_NUSNETID_AMY + " a/100",
                "Homework id must be between 1 and 13.");
    }

    @Test
    public void parse_missingFields_failure() {
        assertParseFailure(parser, " a/1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteHomeworkCommand.MESSAGE_USAGE));

        assertParseFailure(parser, " i/" + VALID_NUSNETID_AMY,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteHomeworkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidNusnetId_failure() {
        assertParseFailure(parser, " i/ a/1",
                Nusnetid.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_boundaryAssignmentId_success() {
        assertParseSuccess(parser, "i/" + VALID_NUSNETID_AMY + " a/1",
                new DeleteHomeworkCommand(new Nusnetid(VALID_NUSNETID_AMY), 1, false));
        assertParseSuccess(parser, "i/all a/3",
                new DeleteHomeworkCommand(null, 3, true));
    }

    @Test
    public void parse_caseInsensitiveAll_success() {
        assertParseSuccess(parser, "i/ALL a/2",
                new DeleteHomeworkCommand(null, 2, true));
    }

    @Test
    public void parse_extraWhitespace_success() {
        assertParseSuccess(parser, "   i/" + VALID_NUSNETID_AMY + "   a/2   ",
                new DeleteHomeworkCommand(new Nusnetid(VALID_NUSNETID_AMY), 2, false));
    }

    @Test
    public void parse_invalidKeyword_failure() {
        assertParseFailure(parser, "every a/1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteHomeworkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_duplicateAssignment_failure() {
        assertParseFailure(parser, "i/" + VALID_NUSNETID_AMY + " a/1 a/2",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteHomeworkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_extraArguments_failure() {
        assertParseFailure(parser, "i/" + VALID_NUSNETID_AMY + " a/1 extra",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteHomeworkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingPrefixes_failure2() {
        assertParseFailure(parser, VALID_NUSNETID_AMY + " 1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteHomeworkCommand.MESSAGE_USAGE));
    }
}
