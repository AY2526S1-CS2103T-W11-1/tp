package seedu.address.testutil;

import java.time.LocalDateTime;

import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.event.Consultation;
import seedu.address.model.person.AttendanceSheet;
import seedu.address.model.person.Email;
import seedu.address.model.person.GroupId;
import seedu.address.model.person.HomeworkTracker;
import seedu.address.model.person.Name;
import seedu.address.model.person.Nusnetid;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Telegram;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@u.nus.edu";
    public static final String DEFAULT_NUSNETID = "E1234567";
    public static final String DEFAULT_TELEGRAM = "@amyy";
    public static final String DEFAULT_GROUP = "T01";

    private Name name;
    private Phone phone;
    private Email email;
    private Nusnetid nusnetid;
    private Telegram telegram;
    private GroupId groupId;
    private HomeworkTracker homeworkTracker;
    private AttendanceSheet attendanceSheet;
    private Consultation consultation;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        nusnetid = new Nusnetid(DEFAULT_NUSNETID);
        telegram = new Telegram(DEFAULT_TELEGRAM);
        groupId = new GroupId(DEFAULT_GROUP);
        homeworkTracker = new HomeworkTracker();
        attendanceSheet = new AttendanceSheet();
        consultation = null;
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone().orElse(null);
        email = personToCopy.getEmail().orElse(null);
        nusnetid = personToCopy.getNusnetid();
        telegram = personToCopy.getTelegram();
        groupId = personToCopy.getGroupId();
        homeworkTracker = personToCopy.getHomeworkTracker();
        attendanceSheet = personToCopy.getAttendanceSheet();
        consultation = personToCopy.getConsultation().orElse(null);
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Sets the {@code NUSnetid} of the {@code Person} that we are building.
     */
    public PersonBuilder withNusnetid(String nusnetid) {
        this.nusnetid = new Nusnetid(nusnetid);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = phone == null ? null : new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = email == null ? null : new Email(email);
        return this;
    }

    /**
     * Sets the {@code Telegram} of the {@code Person} that we are building.
     */
    public PersonBuilder withTelegram(String telegram) {
        this.telegram = new Telegram(telegram);
        return this;
    }

    /**
     * Sets the {@code GroupId} of the {@code Person} that we are building.
     */
    public PersonBuilder withGroup(String groupId) {
        this.groupId = new GroupId(groupId);
        return this;
    }

    /**
     * Sets the {@code GroupId} to null of the {@code Person} that we are building.
     */
    public PersonBuilder withNoGroup() {
        this.groupId = null;
        return this;
    }

    /**
     * Sets the {@code HomeworkTracker} of the {@code Person} that we are building.
     */
    public PersonBuilder withHomeworkTracker(HomeworkTracker homeworkTracker) {
        this.homeworkTracker = homeworkTracker;
        return this;
    }

    /**
     * Convenience method to add a homework assignment to the {@code Person} being built.
     */
    public PersonBuilder withAddedHomework(int assignmentId) {
        this.homeworkTracker = this.homeworkTracker.addHomework(assignmentId);
        return this;
    }
    /**
     * Sets the {@code AttendanceSheet} of the {@code Person} that we are building.
     */
    public PersonBuilder withAttendanceSheet(AttendanceSheet attendanceSheet) {
        this.attendanceSheet = attendanceSheet;
        return this;
    }

    /**
     * Sets the {@code Consultation} of the {@code Person} that we are building.
     */
    public PersonBuilder withConsultation(String start, String end) throws ParseException {
        LocalDateTime from = ParserUtil.parseDateTime(start);
        LocalDateTime to = ParserUtil.parseDateTime(end);
        this.consultation = new Consultation(this.nusnetid, from, to);
        return this;
    }
    /**
     * Builds the Person object.
     */
    public Person build() {
        return new Person(name, phone, email, nusnetid, telegram, groupId,
                homeworkTracker, attendanceSheet, consultation);
    }

}
