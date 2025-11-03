package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's telegram handle in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidTelegram(String)}
 */
public class Telegram {


    public static final String MESSAGE_CONSTRAINTS =
            "Telegram handle should start with @ and has no whitespace in it.";
    public static final String VALIDATION_REGEX = "@\\S+";
    public final String value;

    /**
     * Constructs a {@code Telegram}.
     *
     * @param telegram A valid telegram handle.
     */
    public Telegram(String telegram) {
        requireNonNull(telegram);
        checkArgument(isValidTelegram(telegram), MESSAGE_CONSTRAINTS);
        value = telegram;
    }

    /**
     * Returns true if a given string is a valid telegram handle.
     */
    public static boolean isValidTelegram(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Telegram)) {
            return false;
        }

        Telegram otherTelegram = (Telegram) other;
        // Case-insensitive comparison so that handles differing only by case are considered equal
        return value.equalsIgnoreCase(otherTelegram.value);
    }

    @Override
    public int hashCode() {
        // Ensure consistency with equals by hashing lower-cased value
        return value.toLowerCase().hashCode();
    }

}
