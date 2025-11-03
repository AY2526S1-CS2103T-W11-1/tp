package seedu.address.model.person;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Represents a homework tracker for a single person.
 * <p>
 * Each {@code HomeworkTracker} instance maintains a mapping between assignment IDs
 * and their corresponding completion statuses.
 * The class is designed in an <b>immutable style</b> — any update (such as changing
 * a homework's status) results in the creation of a new {@code HomeworkTracker} object,
 * leaving the original instance unchanged.
 * </p>
 *
 * <p>Usage example:</p>
 * <pre>{@code
 * HomeworkTracker tracker = new HomeworkTracker();
 * tracker = tracker.updateStatus(0, "complete");
 * System.out.println(tracker.getStatus(0)); // "complete"
 * }</pre>
 */
public class HomeworkTracker {

    /** The maximum number of assignments a person can have (IDs range from 1 to MAX_ASSIGNMENTS). */
    public static final int MAX_ASSIGNMENTS = 13; // 1..13

    /** Internal map storing assignment IDs and their corresponding statuses. */
    private final Map<Integer, Homework> statuses;

    /**
     * Constructs an empty {@code HomeworkTracker} with no recorded homework statuses.
     */
    public HomeworkTracker() {
        this.statuses = new TreeMap<>();
    }

    /**
     * Constructs a {@code HomeworkTracker} with the given map of assignment statuses.
     * <p>
     * The map is defensively copied to preserve immutability.
     * </p>
     *
     * @param statuses a map of assignment IDs to their statuses
     * @throws NullPointerException if {@code statuses} is {@code null}
     */
    public HomeworkTracker(Map<Integer, Homework> statuses) {
        this.statuses = new TreeMap<>(Objects.requireNonNull(statuses));
    }

    /** Add a new homework with status incomplete by default. */
    public HomeworkTracker addHomework(int assignmentId) {
        if (assignmentId < 0 || assignmentId > MAX_ASSIGNMENTS) {
            throw new IllegalArgumentException("Assignment ID must be between 1 and 13.");
        }
        if (statuses.containsKey(assignmentId)) {
            return this; // already exists
        }
        Map<Integer, Homework> updated = new HashMap<>(statuses);
        updated.put(assignmentId, new Homework(assignmentId, Homework.STATUS_INCOMPLETE));
        return new HomeworkTracker(updated);
    }

    /** Remove a homework with the given student number or remove for all students */
    public HomeworkTracker removeHomework(int assignmentId) {
        if (!statuses.containsKey(assignmentId)) {
            throw new IllegalArgumentException("Homework not found.");
        }

        Map<Integer, Homework> updatedMap = new HashMap<>(statuses);
        updatedMap.remove(assignmentId);
        return new HomeworkTracker(updatedMap);
    }

    /**
     * Returns a new {@code HomeworkTracker} instance with the specified assignment updated to the given status.
     * <p>
     * If the assignment ID or status is invalid, an {@link IllegalArgumentException} is thrown.
     * </p>
     *
     * @param assignmentId the assignment ID to update (must be between 1 and {@link #MAX_ASSIGNMENTS})
     * @param status       the new status to set ("complete", "incomplete", or "late")
     * @return a new {@code HomeworkTracker} with the updated status
     * @throws IllegalArgumentException if the assignment ID or status is invalid
     */
    public HomeworkTracker updateStatus(int assignmentId, String status) {
        if (!isValidStatus(status)) {
            throw new IllegalArgumentException("Please enter complete/incomplete/late only");
        }
        HashMap<Integer, Homework> copy = new HashMap<>(statuses);
        Homework existing = copy.get(assignmentId);
        if (existing == null) {
            throw new IllegalArgumentException("Assignment not found. Add it first using 'addhw'.");
        }
        copy.put(assignmentId, existing.withStatus(status));
        return new HomeworkTracker(copy);
    }

    /**
     * Retrieves the status of the given assignment ID.
     *
     * @param assignmentId the assignment ID to query
     * @return the homework status if recorded, or {@code "not marked"} if none exists
     */
    public String getStatus(int assignmentId) {
        Homework hw = statuses.get(assignmentId);
        return hw == null ? "not marked" : hw.getStatus();
    }

    /**
     * Returns an unmodifiable view of the internal assignment-status map.
     *
     * @return an unmodifiable map of assignment IDs to statuses
     */
    public Map<Integer, Homework> asMap() {
        return Collections.unmodifiableMap(statuses);
    }

    /**
     * Returns {@code true} if the given assignment ID is within the valid range.
     *
     * @param id the assignment ID to check
     * @return {@code true} if valid, {@code false} otherwise
     */
    public static boolean isValidAssignmentId(int id) {
        return id > 0 && id <= MAX_ASSIGNMENTS;
    }

    /**
     * Returns {@code true} if the given status is valid (i.e., one of "complete", "incomplete", or "late").
     *
     * @param s the status string to check
     * @return {@code true} if valid, {@code false} otherwise
     */
    public static boolean isValidStatus(String s) {
        if (s == null) {
            return false;
        }
        String[] validStatuses = {Homework.STATUS_COMPLETE, Homework.STATUS_INCOMPLETE, Homework.STATUS_LATE};
        for (String v : validStatuses) {
            if (v.equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAssignment(int assignmentId) {
        return statuses.containsKey(assignmentId);
    }



    /**
     * Returns a string representation of this homework tracker.
     * Example: <code>{0=complete, 1=incomplete}</code>
     *
     * @return a string representation of the assignment statuses
     */
    @Override
    public String toString() {
        return statuses.toString();
    }

    /**
     * Returns {@code true} if this tracker is equal to the specified object.
     * Two trackers are equal if their internal status maps are identical.
     *
     * @param o the object to compare with
     * @return {@code true} if both trackers have the same statuses, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HomeworkTracker)) {
            return false;
        }
        HomeworkTracker other = (HomeworkTracker) o;
        return statuses.equals(other.statuses);
    }

    public boolean contains(int assignmentId) {
        return statuses.containsKey(assignmentId);
    }

    /**
     * Returns the hash code of this tracker based on its status map.
     *
     * @return the hash code value
     */
    @Override
    public int hashCode() {
        return statuses.hashCode();
    }
}
