package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.event.Consultation;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {
    // Identity fields
    private final Name name;
    private final Optional<Phone> phone;
    private final Optional<Email> email;
    private final Nusnetid nusnetid;
    private final Telegram telegram;
    private final GroupId groupId;
    private final HomeworkTracker homeworkTracker;
    private final AttendanceSheet attendanceSheet;
    private final Optional<Consultation> consultation;

    /**
     * Initializes a Person object with no consultation as default.
     * Some field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Nusnetid nusnetid, Telegram telegram, GroupId groupId,
                  HomeworkTracker homeworkTracker) {
        requireAllNonNull(name, nusnetid, telegram, groupId, homeworkTracker);
        this.name = name;
        this.phone = Optional.ofNullable(phone);
        this.email = Optional.ofNullable(email);
        this.nusnetid = nusnetid;
        this.telegram = telegram;
        this.groupId = groupId;
        this.homeworkTracker = homeworkTracker;
        this.attendanceSheet = new AttendanceSheet();
        this.consultation = Optional.ofNullable(null);
    }

    /**
     * Initializes a Person object with no consultation as default.
     * Some field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Nusnetid nusnetid, Telegram telegram, GroupId groupId,
                  HomeworkTracker homeworkTracker, AttendanceSheet attendanceSheet) {
        requireAllNonNull(name, nusnetid, telegram, groupId, homeworkTracker);
        this.name = name;
        this.phone = Optional.ofNullable(phone);
        this.email = Optional.ofNullable(email);
        this.nusnetid = nusnetid;
        this.telegram = telegram;
        this.groupId = groupId;
        this.homeworkTracker = homeworkTracker;
        this.attendanceSheet = attendanceSheet;
        this.consultation = Optional.ofNullable(null);
    }

    /**
     * Initializes a Person object with given consultation.
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Nusnetid nusnetid, Telegram telegram, GroupId groupId,
                  HomeworkTracker homeworkTracker, AttendanceSheet attendanceSheet, Consultation consultation) {
        requireAllNonNull(name, nusnetid, telegram, groupId, homeworkTracker);
        this.name = name;
        this.phone = Optional.ofNullable(phone);
        this.email = Optional.ofNullable(email);
        this.nusnetid = nusnetid;
        this.telegram = telegram;
        this.groupId = groupId;
        this.homeworkTracker = homeworkTracker;
        this.attendanceSheet = attendanceSheet;
        this.consultation = Optional.ofNullable(consultation);
    }

    /**
     * Some field must be present and not null.
     * Different from the other constructor as this one takes in Optional phone and email.
     */
    public Person(Name name, Optional<Phone> phone, Optional<Email> email,
                  Nusnetid nusnetid, Telegram telegram, GroupId groupId,
                  HomeworkTracker homeworkTracker, AttendanceSheet attendanceSheet,
                  Optional<Consultation> consultation) {
        requireAllNonNull(name, phone, email, nusnetid, telegram, groupId, homeworkTracker);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.nusnetid = nusnetid;
        this.telegram = telegram;
        this.groupId = groupId;
        this.homeworkTracker = homeworkTracker;
        this.attendanceSheet = attendanceSheet;
        this.consultation = consultation;
    }


    public Name getName() {
        return name;
    }

    public Optional<Phone> getPhone() {
        return phone;
    }

    public Optional<Email> getEmail() {
        return email;
    }

    public Nusnetid getNusnetid() {
        return nusnetid;
    }

    public Telegram getTelegram() {
        return telegram;
    }

    public GroupId getGroupId() {
        return groupId;
    }
    public AttendanceSheet getAttendanceSheet() {
        return attendanceSheet;
    }

    public Optional<Consultation> getConsultation() {
        return consultation;
    }

    /**
     * Returns the {@link HomeworkTracker} associated with this student.
     *
     * @return the {@code HomeworkTracker} object containing this student's homework statuses
     */
    public HomeworkTracker getHomeworkTracker() {
        return homeworkTracker;
    }

    /**
     * Returns a new {@code Person} instance with a new homework added to the homework tracker.
     * <p>
     * The new homework is added with the specified assignment ID. The original {@code Person} object
     * remains unchanged because {@link HomeworkTracker} follows an immutable design.
     * </p>
     *
     * @param assignmentId the ID of the assignment to add (usually 1–3)
     * @return a new {@code Person} object with the updated {@link HomeworkTracker}
     */
    public Person withAddedHomework(int assignmentId) {
        HomeworkTracker updated = homeworkTracker.addHomework(assignmentId);
        requireNonNull(homeworkTracker);
        if (homeworkTracker.contains(assignmentId)) {
            return this;
        }
        HomeworkTracker updatedTracker = homeworkTracker.addHomework(assignmentId);
        return new Person(name, phone, email, nusnetid, telegram, groupId, updatedTracker, this.attendanceSheet,
                this.consultation);
    }

    /**
     * Returns a new Person with a homework removed from the homework tracker.
     *
     * @param assignmentId the ID of the homework to remove
     * @return a new Person object with the updated HomeworkTracker
     * @throws IllegalArgumentException if the assignment does not exist
     */
    public Person withDeletedHomework(int assignmentId) {
        if (!homeworkTracker.contains(assignmentId)) {
            throw new IllegalArgumentException("Homework with this ID does not exist for this student.");
        }
        HomeworkTracker updatedTracker = homeworkTracker.removeHomework(assignmentId);
        return new Person(
                this.name,
                this.phone,
                this.email,
                this.nusnetid,
                this.telegram,
                this.groupId,
                updatedTracker,
                this.attendanceSheet,
                this.consultation
        );
    }




    /** Returns a new Person with updated homework status for the given assignment. */
    public Person withUpdatedHomework(int assignmentId, String status) {
        HomeworkTracker updated = this.homeworkTracker.updateStatus(assignmentId, status);
        return new Person(this.name, this.phone, this.email, this.nusnetid, this.telegram, this.groupId, updated,
                this.attendanceSheet, this.consultation);
    }

    /**
     * Returns a new Person with updated GroupId.
     * @param newGroupId the new GroupId to be set.
     * @return Person with the updated GroupId.
     */
    public Person withUpdatedGroup(GroupId newGroupId) {
        return new Person(this.name, this.phone, this.email, this.nusnetid, this.telegram, newGroupId,
                this.homeworkTracker, this.attendanceSheet, this.consultation);
    }

    /**
     * Returns a new Person with added consultation.
     * @param consultation Consultation to be added.
     * @return Person with the added consultation.
     */
    public Person addConsultation(Consultation consultation) {
        return new Person(this.name, this.phone, this.email, this.nusnetid, this.telegram, this.groupId,
                this.homeworkTracker, this.attendanceSheet, Optional.ofNullable(consultation));
    }

    /**
     * Returns a new Person with deleted consultation.
     * @return Person without consultation.
     */
    public Person deleteConsultation() {
        return new Person(this.name, this.phone, this.email, this.nusnetid, this.telegram, this.groupId,
                this.homeworkTracker, this.attendanceSheet, Optional.ofNullable(null));
    }

    /**
     * Returns true if both persons have the same nusnetid.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }
        return otherPerson != null
                && (otherPerson.getNusnetid().equals(getNusnetid())
                || otherPerson.getTelegram().equals(getTelegram())
                || otherPerson.getPhone().flatMap(g1 -> getPhone().map(g1::equals)).orElse(false)
                || otherPerson.getEmail().flatMap(e1 -> getEmail().map(e1::equals)).orElse(false));
    }

    /**
     * Returns true if person has same nusnetId as the given nusnetId.
     */
    public boolean hasSameNusnetId(Nusnetid nusnetid) {
        return nusnetid != null
                && nusnetid.equals(this.getNusnetid());
    }

    /**
     * Returns true if the person has a consultation scheduled.
     */
    public boolean hasConsultation() {
        return consultation.isPresent();
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Person otherPerson)) {
            return false;
        }
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && nusnetid.equals(otherPerson.nusnetid)
                && telegram.equals(otherPerson.telegram)
                && groupId.equals(otherPerson.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone, email, nusnetid, telegram, groupId, homeworkTracker);
    }
    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this)
                .add("name", name)
                .add("NUSnetid", nusnetid)
                .add("telegram", telegram)
                .add("groupId", groupId);
        if (this.phone.isPresent()) {
            builder.add("phone", phone.get());
        }
        if (this.email.isPresent()) {
            builder.add("email", email.get());
        }
        return builder.toString();
    }

}
