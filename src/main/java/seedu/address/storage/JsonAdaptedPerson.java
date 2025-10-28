package seedu.address.storage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.model.event.Consultation;
import seedu.address.model.person.AttendanceSheet;
import seedu.address.model.person.AttendanceStatus;
import seedu.address.model.person.Email;
import seedu.address.model.person.GroupId;
import seedu.address.model.person.Homework;
import seedu.address.model.person.HomeworkTracker;
import seedu.address.model.person.Name;
import seedu.address.model.person.Nusnetid;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Telegram;


/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String nusnetid;
    private final String groupId;
    private final String telegram;
    private final Map<Integer, JsonAdaptedHomework> homework;
    private final List<JsonAdaptedAttendance> attendanceSheet;
    private final String consultationStart;
    private final String consultationEnd;

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
            @JsonProperty("email") String email, @JsonProperty("nusnetid") String nusnetid,
            @JsonProperty("groupId") String groupId, @JsonProperty("telegram") String telegram,
            @JsonProperty("homework") Map<Integer, JsonAdaptedHomework> homework,
            @JsonProperty("attendanceSheet") List<JsonAdaptedAttendance> attendanceSheet,
            @JsonProperty("consultationStart") String consultationStart,
            @JsonProperty("consultationEnd") String consultationEnd) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.nusnetid = nusnetid;
        this.groupId = groupId;
        this.telegram = telegram;
        this.homework = homework == null ? new HashMap<>() : homework;
        this.attendanceSheet = attendanceSheet == null ? new ArrayList<>() : attendanceSheet;
        this.consultationStart = consultationStart == null ? "" : consultationStart;
        this.consultationEnd = consultationEnd == null ? "" : consultationEnd;
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().isPresent() ? source.getPhone().get().value : null;
        email = source.getEmail().isPresent() ? source.getEmail().get().value : null;
        nusnetid = source.getNusnetid().value;
        telegram = source.getTelegram().value;
        groupId = source.getGroupId().value;
        homework = new HashMap<>();
        source.getHomeworkTracker().asMap().forEach((id, hw) -> homework.put(id,
                new JsonAdaptedHomework(hw))
        );
        attendanceSheet = new ArrayList<>();
        source.getAttendanceSheet().getAttendanceList().forEach(
                att -> attendanceSheet.add(new JsonAdaptedAttendance(att)));
        consultationStart = source.getConsultation().map(Consultation::getFromInString).orElse("");
        consultationEnd = source.getConsultation().map(Consultation::getToInString).orElse("");
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        Phone modelPhone = null;
        if (phone != null) {
            if (!Phone.isValidSlot(phone)) { // kept as-is per existing implementation
                throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
            }
            modelPhone = new Phone(phone);
        }

        Email modelEmail = null;
        if (email != null) {
            if (!Email.isValidEmail(email)) {
                throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
            }
            modelEmail = new Email(email);
        }

        if (nusnetid == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Nusnetid.class.getSimpleName()));
        }
        if (!Nusnetid.isValidNusnetid(nusnetid)) {
            throw new IllegalValueException(Nusnetid.MESSAGE_CONSTRAINTS);
        }
        final Nusnetid modelNusnetid = new Nusnetid(nusnetid);

        if (groupId == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    GroupId.class.getSimpleName()));
        }
        if (!GroupId.isValidGroupId(groupId)) {
            throw new IllegalValueException(GroupId.MESSAGE_CONSTRAINTS);
        }
        final GroupId modelGroupId = new GroupId(groupId);

        if (telegram == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Telegram.class.getSimpleName()));
        }
        if (!Telegram.isValidTelegram(telegram)) {
            throw new IllegalValueException(Telegram.MESSAGE_CONSTRAINTS);
        }
        final Telegram modelTelegram = new Telegram(telegram);

        Map<Integer, Homework> homeworkMap = new HashMap<>();
        for (Map.Entry<Integer, JsonAdaptedHomework> entry : homework.entrySet()) {
            homeworkMap.put(entry.getKey(), entry.getValue().toModelType());
        }

        HomeworkTracker modelHomeworkTracker = new HomeworkTracker(homeworkMap);
        AttendanceSheet modelAttendanceSheet = new AttendanceSheet();
        for (JsonAdaptedAttendance adaptedAttendance : attendanceSheet) {
            int week = adaptedAttendance.getWeek();
            String status = adaptedAttendance.getStatus();
            AttendanceStatus status1 = AttendanceStatus.fromString(status);
            modelAttendanceSheet.markAttendance(week, status1);
        }

        if (consultationStart == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    "consultation start time"));
        } else if (consultationEnd == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    "consultation end time"));
        }

        if (consultationStart.isEmpty()) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    "consultation start time"));
        } else if (consultationEnd.isEmpty()) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    "consultation end time"));
        }

        LocalDateTime from = ParserUtil.parseDateTime(consultationStart);
        LocalDateTime to = ParserUtil.parseDateTime(consultationEnd);

        if (!Consultation.isValidConsultation(from, to)) {
            throw new IllegalValueException(Consultation.MESSAGE_CONSTRAINTS);
        }
        final Consultation modelConsultation = new Consultation(modelNusnetid, from, to);
        return new Person(modelName, modelPhone, modelEmail,
                modelNusnetid, modelTelegram, modelGroupId, modelHomeworkTracker, modelAttendanceSheet,
                modelConsultation);
    }
}
