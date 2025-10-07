package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class PhoneTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Phone(null));
    }

    @Test
    public void constructor_invalidPhone_throwsIllegalArgumentException() {
        String invalidPhone = "";
        assertThrows(IllegalArgumentException.class, () -> new Phone(invalidPhone));
    }

    @Test
    public void isValidPhone() {
        // null phone number
        assertThrows(NullPointerException.class, () -> Phone.isValidSlot(null));

        // invalid phone numbers
        assertFalse(Phone.isValidSlot("")); // empty string
        assertFalse(Phone.isValidSlot(" ")); // spaces only
        assertFalse(Phone.isValidSlot("91")); // less than 3 numbers
        assertFalse(Phone.isValidSlot("phone")); // non-numeric
        assertFalse(Phone.isValidSlot("9011p041")); // alphabets within digits
        assertFalse(Phone.isValidSlot("9312 1534")); // spaces within digits

        // valid phone numbers
        assertTrue(Phone.isValidSlot("911")); // exactly 3 numbers
        assertTrue(Phone.isValidSlot("93121534"));
        assertTrue(Phone.isValidSlot("124293842033123")); // long phone numbers
    }

    @Test
    public void equals() {
        Phone phone = new Phone("999");

        // same values -> returns true
        assertTrue(phone.equals(new Phone("999")));

        // same object -> returns true
        assertTrue(phone.equals(phone));

        // null -> returns false
        assertFalse(phone.equals(null));

        // different types -> returns false
        assertFalse(phone.equals(5.0f));

        // different values -> returns false
        assertFalse(phone.equals(new Phone("995")));
    }
}
