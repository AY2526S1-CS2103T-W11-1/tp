package seedu.address.model.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import seedu.address.model.event.exceptions.ConsultationNotFoundException;
import seedu.address.model.event.exceptions.DuplicateConsultationException;
import seedu.address.model.person.Nusnetid;

public class UniqueConsultationListTest {
    private final UniqueConsultationList uniqueConsultationList = new UniqueConsultationList();

    private LocalDateTime from1 = LocalDateTime.of(2025, 10, 10, 10, 0);
    private LocalDateTime to1 = LocalDateTime.of(2025, 10, 10, 12, 0);
    private Consultation consultation1 = new Consultation(new Nusnetid("E1234567"), from1, to1);
    private LocalDateTime from2 = LocalDateTime.of(2025, 11, 11, 14, 0);
    private LocalDateTime to2 = LocalDateTime.of(2025, 11, 11, 16, 0);
    private Consultation consultation2 = new Consultation(new Nusnetid("E1234568"), from2, to2);
    private LocalDateTime from3 = LocalDateTime.of(2025, 11, 11, 14, 0);
    private LocalDateTime to3 = LocalDateTime.of(2025, 11, 11, 16, 0);
    private Consultation consultation3 = new Consultation(new Nusnetid("E1234567"), from3, to3);

    @Test
    public void contains_nullConsultation_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueConsultationList.contains((Consultation) null));
    }

    @Test
    public void contains_consultationNotInList_returnsFalse() {
        assertFalse(uniqueConsultationList.contains(consultation1));
    }

    @Test
    public void contains_consultationInList_returnsTrue() {
        uniqueConsultationList.add(consultation1);
        assertTrue(uniqueConsultationList.contains(consultation1));
    }

    @Test
    public void contains_consultationWithSameStartAndEndTimes_returnsTrue() {
        uniqueConsultationList.add(consultation2);
        assertTrue(uniqueConsultationList.contains(consultation3));
    }

    @Test
    public void add_nullConsultation_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueConsultationList.add(null));
    }

    @Test
    public void add_duplicateConsultation_throwsDuplicateConsultationException() {
        uniqueConsultationList.add(consultation2);
        assertThrows(DuplicateConsultationException.class, () -> uniqueConsultationList.add(consultation3));
    }

    @Test
    public void setConsultation_nullTargetConsultation_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueConsultationList.setConsultation(null, consultation1));
    }

    @Test
    public void setConsultation_nullEditedConsultation_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueConsultationList.setConsultation(consultation1, null));
    }

    @Test
    public void setConsultation_targetConsultationNotInList_throwsConsultationNotFoundException() {
        assertThrows(ConsultationNotFoundException.class, () -> uniqueConsultationList
                .setConsultation(consultation2, consultation2));
    }

    @Test
    public void setConsultation_editedConsultationIsSameConsultation_success() {
        uniqueConsultationList.add(consultation1);
        uniqueConsultationList.setConsultation(consultation1, consultation1);
        UniqueConsultationList expectedUniqueConsultationList = new UniqueConsultationList();
        expectedUniqueConsultationList.add(consultation1);
        assertEquals(expectedUniqueConsultationList, uniqueConsultationList);
    }

    @Test
    public void setConsultation_editedConsultationHasDifferentIdentity_success() {
        uniqueConsultationList.add(consultation1);
        uniqueConsultationList.setConsultation(consultation1, consultation2);
        UniqueConsultationList expectedUniqueConsultationList = new UniqueConsultationList();
        expectedUniqueConsultationList.add(consultation2);
        assertEquals(expectedUniqueConsultationList, uniqueConsultationList);
    }

    @Test
    public void setConsultation_editedConsultationHasNonUniqueIdentity_throwsDuplicateConsultationException() {
        uniqueConsultationList.add(consultation1);
        uniqueConsultationList.add(consultation2);
        assertThrows(DuplicateConsultationException.class, () -> uniqueConsultationList
                .setConsultation(consultation1, consultation2));
    }

    @Test
    public void remove_nullConsultation_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueConsultationList.remove(null));
    }

    @Test
    public void remove_consultationDoesNotExist_throwsConsultationNotFoundException() {
        assertThrows(ConsultationNotFoundException.class, () -> uniqueConsultationList.remove(consultation1));
    }

    @Test
    public void remove_existingConsultation_success() {
        uniqueConsultationList.add(consultation1);
        uniqueConsultationList.remove(consultation1);
        UniqueConsultationList expectedUniqueConsultationList = new UniqueConsultationList();
        assertEquals(expectedUniqueConsultationList, uniqueConsultationList);
    }

    @Test
    public void setConsultations_nullUniqueConsultationList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueConsultationList
                .setConsultations((UniqueConsultationList) null));
    }

    @Test
    public void setConsultations_uniqueConsultationList_replacesOwnListWithProvidedUniqueConsultationList() {
        uniqueConsultationList.add(consultation1);
        UniqueConsultationList expectedUniqueConsultationList = new UniqueConsultationList();
        expectedUniqueConsultationList.add(consultation2);
        uniqueConsultationList.setConsultations(expectedUniqueConsultationList);
        assertEquals(expectedUniqueConsultationList, uniqueConsultationList);
    }

    @Test
    public void setConsultations_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueConsultationList
                .setConsultations((List<Consultation>) null));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
                -> uniqueConsultationList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniqueConsultationList.asUnmodifiableObservableList().toString(),
                uniqueConsultationList.toString());
    }
}
