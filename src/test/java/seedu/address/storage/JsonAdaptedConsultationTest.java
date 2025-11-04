package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedConsultation.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import org.junit.jupiter.api.Test;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.event.Consultation;

public class JsonAdaptedConsultationTest {
    private static final String INVALID_NUSNETID = " ";
    private static final String INVALID_CONSULTATION_TIME = "20251111 1000";
    private static final String INVALID_CONSULTATION_TIME_WRONG_FORMAT = "2025-11-11 16:00";
    private static final String INVALID_CONSULTATION_END_TIME = "20250229 1000";
    private static final String NULL_CONSULTATION_END_TIME = null;
    private static final String VALID_NUSNETID = BENSON.getNusnetid().toString();
    private static final Consultation VALID_CONSULTATION = BENSON.getConsultation().get();
    private static final String VALID_CONSULTATION_START_TIME = BENSON.getConsultation().get().getFromInString();
    private static final String VALID_CONSULTATION_END_TIME = BENSON.getConsultation().get().getToInString();

    @Test
    public void toModelType_validConsultationDetails_returnsConsultation() throws Exception {
        JsonAdaptedConsultation consultation = new JsonAdaptedConsultation(VALID_CONSULTATION);
        assertEquals(VALID_CONSULTATION, consultation.toModelType());
    }

    @Test
    public void toModelType_invalidNusNetId_throwsIllegalValueException() {
        JsonAdaptedConsultation consultation = new JsonAdaptedConsultation(INVALID_NUSNETID,
                VALID_CONSULTATION_START_TIME, VALID_CONSULTATION_END_TIME);
        assertThrows(IllegalValueException.class, consultation::toModelType);
    }

    @Test
    public void toModelType_invalidEndTime_throwsIllegalValueException() {
        JsonAdaptedConsultation consultation =
                new JsonAdaptedConsultation(VALID_NUSNETID,
                        VALID_CONSULTATION_START_TIME, INVALID_CONSULTATION_END_TIME);
        String expectedMessage = "Invalid date or time. Please ensure the date exists and time is valid!";
        assertThrows(IllegalValueException.class, expectedMessage, consultation::toModelType);
    }

    @Test
    public void toModelType_invalidTimeFormat_throwsIllegalValueException() {
        JsonAdaptedConsultation consultation =
                new JsonAdaptedConsultation(VALID_NUSNETID, VALID_CONSULTATION_START_TIME, INVALID_CONSULTATION_TIME_WRONG_FORMAT);
        String expectedMessage = "Incorrect date & time format. Please use yyyyMMdd HHmm format! (Eg. 20251010 1800)";
        assertThrows(IllegalValueException.class, expectedMessage, consultation::toModelType);
    }

    @Test
    public void toModelType_nullEndTime_throwsIllegalValueException() {
        JsonAdaptedConsultation consultation = new JsonAdaptedConsultation(VALID_NUSNETID,
                VALID_CONSULTATION_START_TIME, NULL_CONSULTATION_END_TIME);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "end time");
        assertThrows(IllegalValueException.class, expectedMessage, consultation::toModelType);
    }

    @Test
    public void toModelType_invalidConsultation_throwsIllegalValueException() {
        JsonAdaptedConsultation consultation =
                new JsonAdaptedConsultation(VALID_NUSNETID, VALID_CONSULTATION_START_TIME, INVALID_CONSULTATION_TIME);
        String expectedMessage = Consultation.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, consultation::toModelType);
    }
}
