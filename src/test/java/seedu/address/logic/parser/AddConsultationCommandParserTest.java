package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddConsultationCommand;
import seedu.address.model.event.Consultation;
import seedu.address.model.person.Nusnetid;

public class AddConsultationCommandParserTest {

    private AddConsultationCommandParser parser = new AddConsultationCommandParser();

    @Test
    public void parse_validArgs_success() {
        LocalDateTime from = LocalDateTime.of(2025, 10, 10, 14, 0);
        LocalDateTime to = LocalDateTime.of(2025, 10, 10, 16, 0);
        Consultation consultation = new Consultation(new Nusnetid("E1234567"), from, to);
        assertParseSuccess(parser, " i/E1234567 " + "from/20251010 1400 " + "to/20251010 1600",
                new AddConsultationCommand(consultation));
    }

    @Test
    public void parse_invalidNusNetId_failure() {
        assertParseFailure(parser, " i/" + "E12345678 " + "from/20251010 1400 " + "to/20251010 1600",
                Nusnetid.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidDate_failure() {
        assertParseFailure(parser, " i/" + "E1234567 " + "from/20250229 1400 " + "to/20250229 1600",
                "Invalid date or time. Please ensure the date exists and time is valid!");
    }

    @Test
    public void parse_invalidDateFormat_failure() {
        assertParseFailure(parser, " i/" + "E1234567 " + "from/2025-10-10 14:00 " + "to/2025-10-10 16:00",
                "Incorrect date & time format. Please use yyyyMMdd HHmm format! (Eg. 20251010 1800)");
    }

    @Test
    public void parse_invalidConsultation_failure() {
        assertParseFailure(parser, " i/" + "E1234567 " + "from/20251010 1400 " + "to/20251010 1200",
                Consultation.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_missingFields_failure() {
        assertParseFailure(parser, " i/E1234567",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddConsultationCommand.MESSAGE_USAGE));

        assertParseFailure(parser, " i/E1234567 " + "from/20251010 1000",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddConsultationCommand.MESSAGE_USAGE));
    }
}
