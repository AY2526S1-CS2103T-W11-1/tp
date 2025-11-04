package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GROUPID_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GROUPID_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NUSNETID_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NUSNETID_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TELEGRAM_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TELEGRAM_BOB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Person ALICE;

    static {
        try {
            ALICE = new PersonBuilder().withName("Alice Pauline")
                    .withNusnetid("E1234567").withEmail("alice@u.nus.edu")
                    .withPhone("94351253")
                    .withTelegram("@alicece").withGroup("T01")
                    .withConsultation("20251010 1000", "20251010 1100").build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static final Person BENSON;

    static {
        try {
            BENSON = new PersonBuilder().withName("Benson Meier")
                    .withNusnetid("E1234568")
                    .withEmail("johnd@u.nus.edu").withPhone("98765432")
                    .withTelegram("@benson").withGroup("T01")
                    .withConsultation("20251111 1100", "20251111 1200").build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static final Person CARL = new PersonBuilder().withName("Carl Kurz").withPhone("95352563")
            .withEmail("heinz@u.nus.edu").withNusnetid("E1234569").withTelegram("@carllll").withGroup("T15").build();
    public static final Person DANIEL = new PersonBuilder().withName("Daniel Meier").withPhone("87652533")
            .withEmail("cornelia@u.nus.edu").withNusnetid("E1234566")
            .withTelegram("@ddddddanial").withGroup("T11").build();
    public static final Person ELLE = new PersonBuilder().withName("Elle Meyer").withPhone("9482224")
            .withEmail("werner@u.nus.edu").withNusnetid("E1234562").withTelegram("@_meyer").withGroup("T18").build();
    public static final Person FIONA = new PersonBuilder().withName("Fiona Kunz").withPhone("9482427")
            .withEmail("lydia@u.nus.edu").withNusnetid("E1234561").withTelegram("@KKK0").withGroup("T12").build();
    public static final Person GEORGE = new PersonBuilder().withName("George Best").withPhone("9482442")
            .withEmail("anna@u.nus.edu").withNusnetid("E1234560").withTelegram("@besttttttt").withGroup("T48").build();

    // Manually added
    public static final Person HOON = new PersonBuilder().withName("Hoon Meier").withPhone("8482424")
            .withEmail("stefan@u.nus.edu").withNusnetid("E1234565").withTelegram("@Meierddd").withGroup("T48").build();
    public static final Person IDA = new PersonBuilder().withName("Ida Mueller").withPhone("8482131")
            .withEmail("hans@u.nus.edu").withNusnetid("E1234564").withTelegram("@Mueller").withGroup("T10").build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY).withNusnetid(VALID_NUSNETID_AMY)
            .withTelegram(VALID_TELEGRAM_AMY).withGroup(VALID_GROUPID_AMY).build();
    public static final Person BOB = new PersonBuilder().withName(VALID_NAME_BOB)
            .withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB).withNusnetid(VALID_NUSNETID_BOB)
            .withTelegram(VALID_TELEGRAM_BOB).withGroup(VALID_GROUPID_BOB).build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
            ab.updateGroupWhenAddPerson(person);
            if (person.hasConsultation()) {
                ab.addConsultation(person.getConsultation().get());
            }
        }
        return ab;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
