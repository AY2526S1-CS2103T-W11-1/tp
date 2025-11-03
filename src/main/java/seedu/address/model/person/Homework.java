package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a single homework assignment for a student.
 * <p>
 * Each {@code Homework} object contains an assignment ID and its completion status.
 * The class is immutable; updating the status creates a new {@code Homework} instance.
 * </p>
 *
 * <p>Valid statuses are defined by {@link #STATUS_COMPLETE}, {@link #STATUS_INCOMPLETE}, and {@link #STATUS_LATE}.</p>
 * Assignment IDs are integers from 1 to 13.
 */
public class Homework {
    public static final String STATUS_COMPLETE = "complete";
    public static final String STATUS_INCOMPLETE = "incomplete";
    public static final String STATUS_LATE = "late";

    private final int id;
    private final String status;

    /**
     * Constructs a {@code Homework} object with the specified ID and status.
     *
     * @param id the assignment ID (1 to 13)
     * @param status the homework status ("complete", "incomplete", or "late")
     * @throws NullPointerException if {@code status} is null
     * @throws IllegalArgumentException if {@code id} is not between 1 and 13,
     *                                  or {@code status} is invalid
     */
    public Homework(int id, String status) {
        requireNonNull(status);
        // homework id is form 1 to 13 (assume only 13 homeworks)
        if (id < 1 || id > 13) {
            throw new IllegalArgumentException("Assignment ID must be between 1 and 13.");
        }
        if (!isValidStatus(status)) {
            throw new IllegalArgumentException("Please enter complete/incomplete/late only.");
        }
        this.id = id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    /**
     * Returns the status of this homework.
     *
     * @return the homework status: complete, incomplete, late
     */
    public String getStatus() {
        return status;
    }

    /**
     * Returns a new {@code Homework} object with the same ID but an updated status.
     *
     * @param newStatus the new status for the assignment
     * @return a new {@code Homework} instance with updated status
     */
    public Homework withStatus(String newStatus) {
        return new Homework(this.id, newStatus);
    }

    /**
     * Checks whether the given status is valid.
     *
     * @param status the status to check
     * @return {@code true} if the status is one of "complete", "incomplete", or "late"; {@code false} otherwise
     */
    public static boolean isValidStatus(String status) {
        return status.equals(STATUS_COMPLETE)
                || status.equals(STATUS_INCOMPLETE)
                || status.equals(STATUS_LATE);
    }

    @Override
    public String toString() {
        return String.format("Assignment %d: %s", id, status);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Homework)) {
            return false;
        }
        Homework o = (Homework) other;
        return id == o.id && status.equals(o.status);
    }

    @Override
    public int hashCode() {
        return id * 31 + status.hashCode();
    }
}
