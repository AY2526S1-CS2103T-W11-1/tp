package seedu.address.logic;

import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.event.Consultation;
import seedu.address.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The student index provided is invalid";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d students listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; NUSNET ID: ")
                .append(person.getNusnetid())
                .append("; Telegram: ")
                .append(person.getTelegram())
                .append("; Group: ")
                .append(person.getGroupId());
        if (person.getPhone().isPresent()) {
            builder.append("; Phone: ")
                       .append(person.getPhone().get());
        }
        if (person.getEmail().isPresent()) {
            builder.append("; Email: ")
                      .append(person.getEmail().get())
                      .append(";");
        }
        return builder.toString();
    }

    /**
     * Formats the {@code consultation} for display to the user.
     */
    public static String format(Consultation consultation) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        final StringBuilder builder = new StringBuilder();
        builder.append("NUSNET ID: ")
                .append(consultation.getNusnetid())
                .append("; From: ")
                .append(consultation.getFrom().format(formatter))
                .append(" To: ")
                .append(consultation.getTo().format(formatter));
        return builder.toString();
    }
}
