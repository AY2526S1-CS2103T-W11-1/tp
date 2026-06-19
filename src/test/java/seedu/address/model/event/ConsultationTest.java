package seedu.address.model.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Nusnetid;

public class ConsultationTest {
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
    public void isSameConsultation() {
        // same object -> returns true
        assertTrue(consultation1.isSameConsultation(consultation1));

        // null -> returns false
        assertFalse(consultation1.isSameConsultation(null));

        // same NusNetId, different start and end times -> returns false
        assertFalse(consultation1.isSameConsultation(consultation3));

        // different NusNetId, same start and end times -> returns true
        assertTrue(consultation2.isSameConsultation(consultation3));

        // different NusNetId, different start and end times -> returns false
        assertFalse(consultation1.isSameConsultation(consultation2));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Consultation consultation1copy = new Consultation(new Nusnetid("E1234567"), from1, to1);
        assertTrue(consultation1.equals(consultation1copy));

        // same object -> returns true
        assertTrue(consultation1.equals(consultation1));

        // null -> returns false
        assertFalse(consultation1.equals(null));

        // different type -> returns false
        assertFalse(consultation1.equals(5));

        // different consultation times, different NusNetId -> returns false
        assertFalse(consultation1.equals(consultation2));

        // different consultation times, same NusNetId -> returns false
        assertFalse(consultation1.equals(consultation3));

        // same consultation times, different NusNetId -> returns false
        assertFalse(consultation2.equals(consultation3));
    }

    @Test
    public void toStringMethod() {
        String expected = Consultation.class.getCanonicalName() + "{NUSnetid=" + consultation1.getNusnetid()
                + ", from=" + consultation1.getFrom()
                + ", to=" + consultation1.getTo() + "}";
        assertEquals(expected, consultation1.toString());
    }
}
