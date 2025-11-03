package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class HomeworkTrackerTest {

    @Test
    public void constructor_default_emptyMap() {
        HomeworkTracker tracker = new HomeworkTracker();
        assertTrue(tracker.asMap().isEmpty());
    }

    @Test
    public void constructor_withValidMap_success() {
        Map<Integer, Homework> map = new HashMap<>();
        map.put(1, new Homework(1, Homework.STATUS_COMPLETE));

        HomeworkTracker tracker = new HomeworkTracker(map);
        assertEquals(1, tracker.asMap().size());
        assertEquals(Homework.STATUS_COMPLETE, tracker.getStatus(1));
    }

    @Test
    public void constructor_nullMap_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new HomeworkTracker(null));
    }

    @Test
    public void addHomework_validId_addsSuccessfully() {
        HomeworkTracker tracker = new HomeworkTracker();
        HomeworkTracker updated = tracker.addHomework(1);

        assertTrue(updated.hasAssignment(1));
        assertEquals(Homework.STATUS_INCOMPLETE, updated.getStatus(1));
        assertFalse(tracker.hasAssignment(1)); // immutability check
    }

    @Test
    public void addHomework_existingAssignment_returnsSameTracker() {
        HomeworkTracker tracker = new HomeworkTracker().addHomework(1);
        HomeworkTracker same = tracker.addHomework(1);
        assertSame(tracker, same);
    }

    @Test
    public void addHomework_invalidId_throwsIllegalArgumentException() {
        HomeworkTracker tracker = new HomeworkTracker();
        assertThrows(IllegalArgumentException.class, () -> tracker.addHomework(0));
        assertThrows(IllegalArgumentException.class, () -> tracker.addHomework(14));
    }

    @Test
    public void removeHomework_existingId_removesSuccessfully() {
        HomeworkTracker tracker = new HomeworkTracker().addHomework(2);
        HomeworkTracker updated = tracker.removeHomework(2);

        assertFalse(updated.hasAssignment(2));
        assertTrue(tracker.hasAssignment(2)); // immutability check
    }

    @Test
    public void removeHomework_nonExistingId_throwsIllegalArgumentException() {
        HomeworkTracker tracker = new HomeworkTracker();
        assertThrows(IllegalArgumentException.class, () -> tracker.removeHomework(5));
    }

    @Test
    public void updateStatus_existingAssignment_updatesSuccessfully() {
        HomeworkTracker tracker = new HomeworkTracker().addHomework(3);
        HomeworkTracker updated = tracker.updateStatus(3, Homework.STATUS_COMPLETE);

        assertEquals(Homework.STATUS_COMPLETE, updated.getStatus(3));
        assertEquals(Homework.STATUS_INCOMPLETE, tracker.getStatus(3)); // immutability check
    }

    @Test
    public void updateStatus_invalidStatus_throwsIllegalArgumentException() {
        HomeworkTracker tracker = new HomeworkTracker().addHomework(3);
        assertThrows(IllegalArgumentException.class, () -> tracker.updateStatus(3, "done"));
    }

    @Test
    public void updateStatus_nonExistingAssignment_throwsIllegalArgumentException() {
        HomeworkTracker tracker = new HomeworkTracker();
        assertThrows(IllegalArgumentException.class, () -> tracker.updateStatus(1, Homework.STATUS_COMPLETE));
    }


    @Test
    public void getStatus_existingAssignment_returnsStatus() {
        HomeworkTracker tracker = new HomeworkTracker().addHomework(4);
        assertEquals(Homework.STATUS_INCOMPLETE, tracker.getStatus(4));
    }

    @Test
    public void getStatus_nonExistingAssignment_returnsNotMarked() {
        HomeworkTracker tracker = new HomeworkTracker();
        assertEquals("not marked", tracker.getStatus(10));
    }

    @Test
    public void isValidAssignmentId_validIds_returnTrue() {
        assertTrue(HomeworkTracker.isValidAssignmentId(1));
        assertTrue(HomeworkTracker.isValidAssignmentId(13));
    }

    @Test
    public void isValidAssignmentId_invalidIds_returnFalse() {
        assertFalse(HomeworkTracker.isValidAssignmentId(0));
        assertFalse(HomeworkTracker.isValidAssignmentId(14));
    }

    @Test
    public void isValidStatus_validStatuses_returnTrue() {
        assertTrue(HomeworkTracker.isValidStatus("complete"));
        assertTrue(HomeworkTracker.isValidStatus("incomplete"));
        assertTrue(HomeworkTracker.isValidStatus("late"));
        assertTrue(HomeworkTracker.isValidStatus("COMPLETE")); // case insensitive
    }

    @Test
    public void isValidStatus_invalidStatuses_returnFalse() {
        assertFalse(HomeworkTracker.isValidStatus("done"));
        assertFalse(HomeworkTracker.isValidStatus(null));
    }

    @Test
    public void asMap_returnsUnmodifiableMap() {
        HomeworkTracker tracker = new HomeworkTracker().addHomework(1);
        Map<Integer, Homework> map = tracker.asMap();
        assertThrows(UnsupportedOperationException.class, () -> map.put(2, new Homework(2, Homework.STATUS_COMPLETE)));
    }

    @Test
    public void contains_returnsTrueIfPresent() {
        HomeworkTracker tracker = new HomeworkTracker().addHomework(1);
        assertTrue(tracker.contains(1));
        assertFalse(tracker.contains(2));
    }

    @Test
    public void equals_sameData_returnsTrue() {
        HomeworkTracker t1 = new HomeworkTracker().addHomework(1);
        HomeworkTracker t2 = new HomeworkTracker().addHomework(1);
        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    public void equals_differentData_returnsFalse() {
        HomeworkTracker t1 = new HomeworkTracker().addHomework(1);
        HomeworkTracker t2 = new HomeworkTracker().addHomework(2);
        assertNotEquals(t1, t2);
        assertNotEquals(t1, null);
        assertNotEquals(t1, "string");
    }

    @Test
    public void toString_correctFormat() {
        HomeworkTracker tracker = new HomeworkTracker().addHomework(1).updateStatus(1, Homework.STATUS_COMPLETE);
        String result = tracker.toString();
        assertTrue(result.contains("1"));
        assertTrue(result.contains("complete"));
    }
}
