package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class HomeworkTest {

    @Test
    public void constructor_validInputs_success() {
        Homework hw = new Homework(1, Homework.STATUS_COMPLETE);
        assertEquals(1, hw.getId());
        assertEquals(Homework.STATUS_COMPLETE, hw.getStatus());
    }

    @Test
    public void constructor_nullStatus_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Homework(1, null));
    }

    @Test
    public void constructor_invalidId_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Homework(0, Homework.STATUS_COMPLETE));
        assertThrows(IllegalArgumentException.class, () -> new Homework(14, Homework.STATUS_COMPLETE));
    }

    @Test
    public void constructor_invalidStatus_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Homework(1, "done"));
        assertThrows(IllegalArgumentException.class, () -> new Homework(1, ""));
    }


    @Test
    public void isValidStatus_validStatuses_returnsTrue() {
        assertTrue(Homework.isValidStatus(Homework.STATUS_COMPLETE));
        assertTrue(Homework.isValidStatus(Homework.STATUS_INCOMPLETE));
        assertTrue(Homework.isValidStatus(Homework.STATUS_LATE));
    }

    @Test
    public void isValidStatus_invalidStatuses_returnsFalse() {
        assertFalse(Homework.isValidStatus("done"));
        assertFalse(Homework.isValidStatus(" "));
        assertFalse(Homework.isValidStatus("COMPLETED"));
        assertFalse(Homework.isValidStatus("in progress"));
    }

    @Test
    public void withStatus_validNewStatus_returnsNewHomework() {
        Homework hw1 = new Homework(2, Homework.STATUS_INCOMPLETE);
        Homework hw2 = hw1.withStatus(Homework.STATUS_COMPLETE);

        assertEquals(2, hw2.getId());
        assertEquals(Homework.STATUS_COMPLETE, hw2.getStatus());
        assertNotSame(hw1, hw2); // new object
    }

    @Test
    public void withStatus_invalidStatus_throwsIllegalArgumentException() {
        Homework hw = new Homework(3, Homework.STATUS_LATE);
        assertThrows(IllegalArgumentException.class, () -> hw.withStatus("submitted"));
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        Homework hw1 = new Homework(1, Homework.STATUS_COMPLETE);
        Homework hw2 = new Homework(1, Homework.STATUS_COMPLETE);
        assertEquals(hw1, hw2);
        assertEquals(hw1.hashCode(), hw2.hashCode());
    }

    @Test
    public void equals_differentValues_returnsFalse() {
        Homework hw1 = new Homework(1, Homework.STATUS_COMPLETE);
        Homework hw2 = new Homework(2, Homework.STATUS_COMPLETE);
        Homework hw3 = new Homework(1, Homework.STATUS_LATE);

        assertNotEquals(hw1, hw2);
        assertNotEquals(hw1, hw3);
        assertNotEquals(hw1, null);
        assertNotEquals(hw1, "some string");
    }

    @Test
    public void toString_correctFormat() {
        Homework hw = new Homework(5, Homework.STATUS_LATE);
        assertEquals("Assignment 5: late", hw.toString());
    }
}
