package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.AddressBook;
import seedu.address.model.Group;
import seedu.address.model.event.Consultation;
import seedu.address.model.person.GroupId;
import seedu.address.model.person.Nusnetid;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.TypicalPersons;

public class JsonSerializableAddressBookTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableAddressBookTest");
    private static final Path TYPICAL_PERSONS_FILE = TEST_DATA_FOLDER.resolve("typicalPersonsAddressBook.json");
    private static final Path INVALID_PERSON_FILE = TEST_DATA_FOLDER.resolve("invalidPersonAddressBook.json");
    private static final Path DUPLICATE_PERSON_FILE = TEST_DATA_FOLDER.resolve("duplicatePersonAddressBook.json");
    private static final Path TYPICAL_GROUPS_FILE = TEST_DATA_FOLDER.resolve("typicalGroupAddressBook.json");
    private static final Path EMPTY_GROUP_FILE = TEST_DATA_FOLDER
            .resolve("emptyGroupAddressBook.json");
    private static final Path VALID_CONSULTATION_FILE = TEST_DATA_FOLDER
            .resolve("typicalConsultationAddressBook.json");
    private static final Path INVALID_CONSULTATION_FILE = TEST_DATA_FOLDER
            .resolve("duplicateConsultationAddressBook.json");
    private static final Person JOHN = new PersonBuilder().withName("John Doe")
            .withNusnetid("E1234567").withEmail("johnd@u.nus.edu")
            .withPhone("98765432")
            .withTelegram("@handle").withGroup("T01").build();
    private static final Person SEB = new PersonBuilder().withName("Seb Henz")
            .withNusnetid("E1234568").withEmail("s.hex.z@u.nus.edu")
            .withPhone("85066519")
            .withTelegram("@Fisher").withGroup("T01").build();
    private static final GroupId GROUP_ID = new GroupId("T01");
    private static final Group GROUP_T01 = new Group(GROUP_ID);
    @Test
    public void toModelType_typicalPersonsFile_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_PERSONS_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();
        AddressBook typicalPersonsAddressBook = TypicalPersons.getTypicalAddressBook();
        assertTrue(addressBookFromFile.equals(typicalPersonsAddressBook));
    }

    @Test
    public void toModelType_invalidPersonFile_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(INVALID_PERSON_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicatePersons_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_PERSON_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableAddressBook.MESSAGE_DUPLICATE_PERSON,
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicatePersons_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_GROUPS_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();
        AddressBook expectedAddressBook = new AddressBook();
        expectedAddressBook.addPerson(JOHN);
        expectedAddressBook.addPerson(SEB);
        Group group = GROUP_T01;
        group.addStudent(JOHN);
        group.addStudent(SEB);
        assertEquals(addressBookFromFile, expectedAddressBook);
    }
    @Test
    public void toModelType_emptyGroups_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(EMPTY_GROUP_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();
        assertEquals(addressBookFromFile.getGroupList().size(), 1);
    }
    @Test
    public void toModelType_validConsultationFile_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(VALID_CONSULTATION_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();
        //    "nusnetId" : "E1234563",
        //    "from" : "20240915 1400",
        //    "to" : "20240915 1500"
        Nusnetid id = new Nusnetid("E1234563");
        LocalDateTime startTime = LocalDateTime.parse("2024-09-15T14:00:00");
        LocalDateTime endTime = LocalDateTime.parse("2024-09-15T15:00:00");
        assertEquals(addressBookFromFile.getConsultationList().size(), 1);
        assertEquals(addressBookFromFile.getConsultationList().get(0), new Consultation(id, startTime, endTime));
    }
    @Test
    public void toModelType_duplicateConsultations_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(INVALID_CONSULTATION_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableAddressBook.MESSAGE_DUPLICATE_CONSULTATION,
                dataFromFile::toModelType);
    }
}
