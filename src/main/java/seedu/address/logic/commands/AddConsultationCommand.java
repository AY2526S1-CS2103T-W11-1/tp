package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FROM;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NUSNETID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TO;

import java.time.Duration;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.event.Consultation;

/**
 * Adds a consultation to the address book.
 */
public class AddConsultationCommand extends Command {

    public static final String COMMAND_WORD = "add_consult";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a consultation slot to the address book. "
            + "Parameters: "
            + PREFIX_NUSNETID + "NUSNETID "
            + PREFIX_FROM + "CONSULTATION START TIME "
            + PREFIX_TO + "CONSULTATION END TIME \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NUSNETID + "E1234567 "
            + PREFIX_FROM + "20251010 1400 "
            + PREFIX_TO + "20251010 1600";

    public static final String MESSAGE_SUCCESS = "New consultation added: %1$s";
    public static final String MESSAGE_OVERLAPPING_CONSULTATION =
            "Consultation timing overlaps with existing consultation";
    public static final String MESSAGE_STUDENT_DOES_NOT_EXIST = "Student does not exist";
    public static final String MESSAGE_STUDENT_ALREADY_HAS_CONSULTATION =
            "Student already has a scheduled consultation";
    public static final String MESSAGE_CONSULTATION_DURATION_TOO_LONG =
            "Friendly reminder: Consultation duration exceeds 3 hours!";

    private final Consultation toAdd;

    /**
     * Creates a AddConsultationCommand to add the specified {@code Consultation}
     */
    public AddConsultationCommand(Consultation consultation) {
        requireNonNull(consultation);
        toAdd = consultation;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (!model.hasPerson(toAdd.getNusnetid())) {
            throw new CommandException(MESSAGE_STUDENT_DOES_NOT_EXIST);
        }

        if (model.hasConsultation(toAdd) || model.hasOverlappingConsultation(toAdd)) {
            throw new CommandException(MESSAGE_OVERLAPPING_CONSULTATION);
        }

        try {
            model.addConsultationToPerson(toAdd.getNusnetid(), toAdd);
        } catch (IllegalArgumentException e) { // handle error when student already has a consultation
            throw new CommandException(e.getMessage());
        }

        model.addConsultation(toAdd);
        if (Duration.between(toAdd.getFrom(), toAdd.getTo()).toHours() > 3) {
            return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)) + "\n"
                    + MESSAGE_CONSULTATION_DURATION_TOO_LONG, false, false, true);
        } else {
            return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)), false, false, true);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddConsultationCommand)) {
            return false;
        }

        AddConsultationCommand otherAddConsultationCommand = (AddConsultationCommand) other;
        return toAdd.equals(otherAddConsultationCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
