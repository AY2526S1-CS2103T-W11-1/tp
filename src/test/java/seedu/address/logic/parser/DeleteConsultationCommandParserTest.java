package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteConsultationCommand;
import seedu.address.model.person.Nusnetid;

public class DeleteConsultationCommandParserTest {
    private DeleteConsultationCommandParser parser = new DeleteConsultationCommandParser();

    @Test
    public void parse_validArgs_success() {
        assertParseSuccess(parser, " i/E1234567", new DeleteConsultationCommand(new Nusnetid("E1234567")));
    }

    @Test
    public void parse_invalidNusNetId_failure() {
        assertParseFailure(parser, " i/" + "E12345678", Nusnetid.MESSAGE_CONSTRAINTS);
    }
}
