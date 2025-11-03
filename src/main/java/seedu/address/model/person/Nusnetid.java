package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's NUSnetid in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidNusnetid(String)}
 */
public class Nusnetid {

    public static final String MESSAGE_CONSTRAINTS =
            "NUSNETID can start with E and has 7 numbers, and it should not be blank";

    /*
     * The first character of the NUSnetid must be a E and follow by 7 numbers.
     */
    private static final String VALIDATION_REGEX = "[Ee]\\d{7}";

    public final String value;

    /**
     * Constructs an {@code NUSnetid}.
     *
     * @param nusnetid A valid NUSnetid.
     */
    public Nusnetid(String nusnetid) {
        requireNonNull(nusnetid);
        checkArgument(isValidNusnetid(nusnetid), MESSAGE_CONSTRAINTS);
        value = "E" + nusnetid.substring(1);
    }

    /**
     * Returns true if a given string is a valid NUSNETID.
     */
    public static boolean isValidNusnetid(String test) {
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
        if (!(other instanceof Nusnetid)) {
            return false;
        }

        Nusnetid otherNusnetid = (Nusnetid) other;
        return value.equals(otherNusnetid.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
