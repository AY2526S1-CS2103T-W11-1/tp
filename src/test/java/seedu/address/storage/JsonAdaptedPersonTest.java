package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.event.Consultation;
import seedu.address.model.person.Email;
import seedu.address.model.person.GroupId;
import seedu.address.model.person.HomeworkTracker;
import seedu.address.model.person.Name;
import seedu.address.model.person.Nusnetid;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Telegram;

public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_NUSNETID = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_GROUP = "T00";
    private static final String INVALID_TELEGRAM = "5dfr";
    private static final String INVALID_CONSULTATION_TIME = "20251111 2500";
    private static final String INVALID_CONSULTATION_TIME_WRONG_FORMAT = "2025-11-11 16:00";
    private static final String INVALID_CONSULTATION_END_TIME = "20251111 1000";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().get().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().get().toString();
    private static final String VALID_NUSNETID = BENSON.getNusnetid().toString();
    private static final String VALID_GROUP = BENSON.getGroupId().toString();
    private static final String VALID_TELEGRAM = BENSON.getTelegram().toString();
    private static final String VALID_CONSULTATION = BENSON.getConsultation().get().toString();
    private static final String VALID_CONSULTATION_START_TIME = BENSON.getConsultation().get().getFromInString();
    private static final String VALID_CONSULTATION_END_TIME = BENSON.getConsultation().get().getToInString();

    private static final HomeworkTracker VALID_HOMEWORK_TRACKER;
    static {
        VALID_HOMEWORK_TRACKER = new HomeworkTracker()
                .addHomework(1)
                .addHomework(2);
    }

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    private static Map<Integer, JsonAdaptedHomework> convertToJsonMap(HomeworkTracker tracker) {
        Map<Integer, JsonAdaptedHomework> map = new HashMap<>();
        tracker.asMap().forEach((id, hw) -> map.put(id, new JsonAdaptedHomework(hw)));
        return map;
    }

    @Test
    public void toModelType_invalidHomework_throwsIllegalValueException() {
        Map<Integer, JsonAdaptedHomework> invalidHomework = new HashMap<>();
        invalidHomework.put(0, new JsonAdaptedHomework(0, "finished")); // invalid status
        List<JsonAdaptedAttendance> emptyAttendanceSheet = List.of();

        JsonAdaptedPerson person = new JsonAdaptedPerson(
                VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_NUSNETID,
                VALID_TELEGRAM, VALID_GROUP, invalidHomework, emptyAttendanceSheet, "", ""
        );

        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        List<JsonAdaptedAttendance> emptyAttendanceSheet = List.of();
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(INVALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_NUSNETID,
                        VALID_TELEGRAM, VALID_GROUP, convertToJsonMap(VALID_HOMEWORK_TRACKER),
                        emptyAttendanceSheet, "", "");
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        List<JsonAdaptedAttendance> emptyAttendanceSheet = List.of();
        JsonAdaptedPerson person = new JsonAdaptedPerson(null, VALID_PHONE, VALID_EMAIL, VALID_NUSNETID,
                VALID_TELEGRAM, VALID_GROUP, convertToJsonMap(VALID_HOMEWORK_TRACKER), emptyAttendanceSheet, "", "");
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        List<JsonAdaptedAttendance> emptyAttendanceSheet = List.of();
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_NUSNETID,
                        VALID_TELEGRAM, VALID_GROUP, convertToJsonMap(VALID_HOMEWORK_TRACKER),
                        emptyAttendanceSheet, "", "");
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }


    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        List<JsonAdaptedAttendance> emptyAttendanceSheet = List.of();
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, INVALID_EMAIL, VALID_NUSNETID,
                        VALID_TELEGRAM, VALID_GROUP, convertToJsonMap(VALID_HOMEWORK_TRACKER),
                        emptyAttendanceSheet, "", "");
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidNusnetid_throwsIllegalValueException() {
        List<JsonAdaptedAttendance> emptyAttendanceSheet = List.of();
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, INVALID_NUSNETID,
                        VALID_TELEGRAM, VALID_GROUP, convertToJsonMap(VALID_HOMEWORK_TRACKER),
                        emptyAttendanceSheet, "", "");
        String expectedMessage = Nusnetid.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullNusnetid_throwsIllegalValueException() {
        List<JsonAdaptedAttendance> emptyAttendanceSheet = List.of();
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, null,
                VALID_TELEGRAM, VALID_GROUP, convertToJsonMap(VALID_HOMEWORK_TRACKER), emptyAttendanceSheet, "", "");
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Nusnetid.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidGroupId_throwsIllegalValueException() {
        List<JsonAdaptedAttendance> emptyAttendanceSheet = List.of();
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_NUSNETID,
                        VALID_TELEGRAM, INVALID_GROUP, convertToJsonMap(VALID_HOMEWORK_TRACKER),
                        emptyAttendanceSheet, "", "");
        String expectedMessage = GroupId.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullSlot_throwsIllegalValueException() {
        List<JsonAdaptedAttendance> emptyAttendanceSheet = List.of();
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_NUSNETID,
                null, VALID_TELEGRAM, convertToJsonMap(VALID_HOMEWORK_TRACKER), emptyAttendanceSheet, "", "");
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, GroupId.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTelegram_throwsIllegalValueException() {
        List<JsonAdaptedAttendance> emptyAttendanceSheet = List.of();
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_NUSNETID,
                        VALID_GROUP, INVALID_TELEGRAM, convertToJsonMap(VALID_HOMEWORK_TRACKER),
                        emptyAttendanceSheet, "", "");
        String expectedMessage = Telegram.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullTelegram_throwsIllegalValueException() {
        List<JsonAdaptedAttendance> emptyAttendanceSheet = List.of();
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_NUSNETID,
                VALID_GROUP, null, convertToJsonMap(VALID_HOMEWORK_TRACKER), emptyAttendanceSheet, "", "");
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Telegram.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidConsultationStartTime_throwsIllegalValueException() {
        List<JsonAdaptedAttendance> emptyAttendanceSheet = List.of();
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_NUSNETID,
                        VALID_GROUP, VALID_TELEGRAM, convertToJsonMap(VALID_HOMEWORK_TRACKER),
                        emptyAttendanceSheet, INVALID_CONSULTATION_TIME, VALID_CONSULTATION_END_TIME);
        String expectedMessage = "Invalid date & time format. Please use yyyyMMdd HHmm format! (Eg. 20251010 1800)";
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullConsultationStartTime_throwsIllegalValueException() {
        List<JsonAdaptedAttendance> emptyAttendanceSheet = List.of();
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_NUSNETID,
                VALID_GROUP, VALID_TELEGRAM, convertToJsonMap(VALID_HOMEWORK_TRACKER), emptyAttendanceSheet,
                null, VALID_CONSULTATION_END_TIME);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "consultation start time");
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_wrongConsultationTimeFormat_throwsIllegalValueException() {
        List<JsonAdaptedAttendance> emptyAttendanceSheet = List.of();
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_NUSNETID,
                VALID_GROUP, VALID_TELEGRAM, convertToJsonMap(VALID_HOMEWORK_TRACKER), emptyAttendanceSheet,
                INVALID_CONSULTATION_TIME_WRONG_FORMAT, VALID_CONSULTATION_END_TIME);
        String expectedMessage = "Invalid date & time format. Please use yyyyMMdd HHmm format! (Eg. 20251010 1800)";
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidConsultation_throwsIllegalValueException() {
        List<JsonAdaptedAttendance> emptyAttendanceSheet = List.of();
        JsonAdaptedPerson person = new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_NUSNETID,
                VALID_GROUP, VALID_TELEGRAM, convertToJsonMap(VALID_HOMEWORK_TRACKER), emptyAttendanceSheet,
                VALID_CONSULTATION_START_TIME, INVALID_CONSULTATION_END_TIME);
        String expectedMessage = Consultation.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }
}
