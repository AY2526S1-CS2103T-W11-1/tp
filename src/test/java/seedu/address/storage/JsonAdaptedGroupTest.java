package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.GEORGE;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.Group;
import seedu.address.model.person.GroupId;
import seedu.address.model.person.Person;

public class JsonAdaptedGroupTest {
    private static final GroupId VALID_GROUP_ID = new GroupId("T01");
    private static final String INVALID_GROUP_ID = "P02";
    private static final List<Person> VALID_STUDENTS =
            new ArrayList<>(List.of(BENSON, GEORGE));

    private static final Group VALID_GROUP;
    static {
        VALID_GROUP = new Group(VALID_GROUP_ID, VALID_STUDENTS);
    }
    @Test
    public void toModelType_validGroupDetails_returnsGroup() throws Exception {
        JsonAdaptedGroup jsonGroup = new JsonAdaptedGroup(VALID_GROUP);
        assertEquals(VALID_GROUP.getGroupId(), new GroupId(jsonGroup.getGroupId()));
        assertEquals(VALID_GROUP.getAllPersons().stream().map(Person::getNusnetid).collect(Collectors.toList()),
                jsonGroup.getStudentNusnetidsAsIds());
    }
    @Test
    public void toModelType_invalidGroupId_throwsIllegalValueException() {
        JsonAdaptedGroup jsonGroup =
                new JsonAdaptedGroup(
                        INVALID_GROUP_ID,
                        VALID_STUDENTS.stream().map(p -> p.getNusnetid().value).collect(Collectors.toList()));
        assertThrows(IllegalValueException.class, jsonGroup::toModelGroupId);
    }
}
