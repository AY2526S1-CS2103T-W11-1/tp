package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GROUPID_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NUSNETID_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TELEGRAM_BOB;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonTest {


    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // same name, all other attributes different -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withNusnetid(VALID_NUSNETID_BOB).withGroup(VALID_GROUPID_BOB).withTelegram(VALID_TELEGRAM_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // same nusnetid, all other attributes different -> returns true
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withGroup(VALID_GROUPID_BOB).withTelegram(VALID_TELEGRAM_BOB).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // same phone, all other attributes different -> returns true
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB)
                .withNusnetid(VALID_NUSNETID_BOB).withEmail(VALID_EMAIL_BOB)
                .withGroup(VALID_GROUPID_BOB).withTelegram(VALID_TELEGRAM_BOB).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // same email, all other attributes different -> returns true
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withNusnetid(VALID_NUSNETID_BOB)
                .withGroup(VALID_GROUPID_BOB).withTelegram(VALID_TELEGRAM_BOB).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // same telegram, all other attributes different -> returns true
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withGroup(VALID_GROUPID_BOB).withNusnetid(VALID_NUSNETID_BOB).build();
        assertTrue(ALICE.isSamePerson(editedAlice));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different nusnetid -> returns false
        editedAlice = new PersonBuilder(ALICE).withNusnetid(VALID_NUSNETID_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different slot -> returns false
        editedAlice = new PersonBuilder(ALICE).withGroup(VALID_GROUPID_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different telegram -> returns false
        editedAlice = new PersonBuilder(ALICE).withTelegram(VALID_TELEGRAM_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName() + "{name=" + ALICE.getName()
                + ", NUSnetid=" + ALICE.getNusnetid()
                + ", telegram=" + ALICE.getTelegram()
                + ", groupId=" + ALICE.getGroupId()
                + ", phone=" + ALICE.getPhone().get()
                + ", email=" + ALICE.getEmail().get() + "}";
        assertEquals(expected, ALICE.toString());
    }
}
