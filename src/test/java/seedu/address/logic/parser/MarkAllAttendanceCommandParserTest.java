package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_WEEK;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GROUP_1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_STATUS_PRESENT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WEEK_1;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.MarkAllAttendanceCommand;
import seedu.address.model.person.AttendanceStatus;
import seedu.address.model.person.GroupId;

public class MarkAllAttendanceCommandParserTest {

    private MarkAllAttendanceCommandParser parser = new MarkAllAttendanceCommandParser();

    @Test
    public void parse_validArgs_success() {
        // Single student
        assertParseSuccess(parser, " g/" + VALID_GROUP_1 + " w/" + VALID_WEEK_1 + " status/" + VALID_STATUS_PRESENT ,
                new MarkAllAttendanceCommand(new GroupId(VALID_GROUP_1),
                        2, AttendanceStatus.fromString(VALID_STATUS_PRESENT)));
    }


    @Test
    public void parse_invalidWeek_failure() {
        assertParseFailure(parser, " g/" + VALID_GROUP_1 + " w/" + INVALID_WEEK + " status/" + VALID_STATUS_PRESENT ,
                MarkAllAttendanceCommand.MESSAGE_INVALID_WEEK);
    }

    @Test
    public void parse_missingFields_failure() {
        assertParseFailure(parser, " a/1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkAllAttendanceCommand.MESSAGE_USAGE));

        assertParseFailure(parser, " g/" + VALID_GROUP_1,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkAllAttendanceCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidGroup_failure() {
        // parser only checks format, so invalid characters or empty NUSNET ID
        assertParseFailure(parser, " g/ a/1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkAllAttendanceCommand.MESSAGE_USAGE));
    }
}
