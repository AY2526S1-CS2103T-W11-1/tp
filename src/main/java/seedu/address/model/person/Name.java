package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final String MESSAGE_CONSTRAINTS =
            "Names may contain letters (including accents), digits, spaces, quotes (\" and '), and commas,\n "
            + "must not be blank, must not contain '/', and must be at most 70 characters long.";

    /*
     * The first character of the name must not be a whitespace.
     * Allowed characters: Unicode letters, digits, spaces (after first char), quotes (") and ('), and comma (,).
     * Disallowed character: forward slash '/'.
     * Maximum length: 75 characters total.
     */
    public static final String VALIDATION_REGEX = "[\\p{L}\\p{N}\"',][\\p{L}\\p{N} \"',]{0,69}";

    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);
        fullName = name;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Name)) {
            return false;
        }

        Name otherName = (Name) other;
        return fullName.equals(otherName.fullName);
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}
