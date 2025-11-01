package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Email;
import seedu.address.model.person.GroupId;
import seedu.address.model.person.Name;
import seedu.address.model.person.Nusnetid;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Telegram;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }
    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     * Empty string will be treated as clearing the value by returning null for callers that support it.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        String trimmedPhone = phone.trim();
        if (trimmedPhone.isEmpty()) {
            return null; // signal clear to EditCommand
        }
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }
    /**
     * Parses a {@code String nusnetid} into an {@code NUSnetid}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code nusnetid} is invalid.
     */
    public static Nusnetid parseNusnetid(String nusnetid) throws ParseException {
        requireNonNull(nusnetid);
        String trimmedNusnetid = nusnetid.trim();
        if (!Nusnetid.isValidNusnetid(trimmedNusnetid)) {
            throw new ParseException(Nusnetid.MESSAGE_CONSTRAINTS);
        }
        return new Nusnetid(trimmedNusnetid);
    }
    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     * Empty string will be treated as clearing the value by returning null for callers that support it.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        String trimmedEmail = email.trim();
        if (trimmedEmail.isEmpty()) {
            return null; // signal clear to EditCommand
        }
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }
    /**
     * Parses a {@code String Telegram} into an {@code Telegram}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code Telegram} is invalid.
     */
    public static Telegram parseTelegram(String telegram) throws ParseException {
        requireNonNull(telegram);
        String trimmedTelegram = telegram.trim();
        if (!Telegram.isValidTelegram(trimmedTelegram)) {
            throw new ParseException(Telegram.MESSAGE_CONSTRAINTS);
        }
        return new Telegram(trimmedTelegram);
    }
    /**
     * Parses a {@code String input} into a {@code LocalDateTime}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code input} is invalid.
     */
    public static LocalDateTime parseDateTime(String input) throws ParseException {
        requireNonNull(input);
        String trimmedInput = input.trim();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuuMMdd HHmm")
                    .withResolverStyle(ResolverStyle.STRICT);
            LocalDateTime dateTime = LocalDateTime.parse(trimmedInput, formatter);
            return dateTime;
        } catch (DateTimeParseException e) {
            String errorMessage = e.getMessage().toLowerCase();
            if (errorMessage.contains("invalid")) { // Invalid date or time (e.g., 30 Feb, Month 13, Hour 25)
                throw new ParseException("Invalid date or time. Please ensure the date exists and time is valid!");
            } else { // Wrong format
                throw new ParseException(
                        "Incorrect date & time format. Please use yyyyMMdd HHmm format! (Eg. 20251010 1800)");
            }
        }
    }
    /**
     * Parses a {@code String groupId} into a {@code GroupId}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code groupId} is invalid.
     */
    public static GroupId parseGroupId(String groupId) throws ParseException {
        requireNonNull(groupId);
        String trimmed = groupId.trim();
        if (!GroupId.isValidGroupId(trimmed)) {
            throw new ParseException(GroupId.MESSAGE_CONSTRAINTS);
        }
        return new GroupId(trimmed);
    }
}
