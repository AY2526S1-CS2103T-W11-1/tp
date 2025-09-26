package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's remark in the address book.
 * Guarantees: immutable; is always valid
 */
public class Remark {
    public final String value;
    /**
     * Constructs a {@code Remark}.
     *
     * @param remark A valid remark.
     */
    public Remark(String remark) {
        requireNonNull(remark);
        value = remark;
    }
    /**
     * Returns true if a given string is a valid remark.
     */
    @Override
    public String toString() {
        return value;
    }
    /**
     * Returns true if both remarks have the same value.
     * This defines a weaker notion of equality between two remarks.
     */
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Remark // instanceof handles nulls
                && value.equals(((Remark) other).value)); // state check
    }

    /**
     * Returns the hash code of the remark.
     * @return the hash code of the remark.
     */
    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
