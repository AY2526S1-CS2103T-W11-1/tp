package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FROM;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NUSNETID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TO;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.logging.Logger;

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

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a consultation to the address book. "
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
    public static final String MESSAGE_CONSULTATION_IS_OVER = "Friendly reminder: Consultation has ended!";
    public static final String MESSAGE_CONSULTATION_IS_ONGOING = "Friendly reminder: Consultation is ongoing!";

    private static final Logger logger = Logger.getLogger(AddConsultationCommand.class.getName());
    private final Consultation toAdd;

    /**
     * Creates a AddConsultationCommand to add the specified {@code Consultation}
     */
    public AddConsultationCommand(Consultation consultation) {
        requireNonNull(consultation);
        toAdd = consultation;
    }

    @SuppressWarnings("checkstyle:Regexp")
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        logger.info("Executing AddConsultationCommand for consultation: " + toAdd);

        if (!model.hasPerson(toAdd.getNusnetid())) {
            logger.warning("Attempted to add duplicate consultation: " + toAdd);
            throw new CommandException(MESSAGE_STUDENT_DOES_NOT_EXIST);
        }

        if (model.hasConsultation(toAdd) || model.hasOverlappingConsultation(toAdd)) {
            logger.warning("Attempted to add overlapping consultation: " + toAdd);
            throw new CommandException(MESSAGE_OVERLAPPING_CONSULTATION);
        }

        try {
            model.addConsultationToPerson(toAdd.getNusnetid(), toAdd);
        } catch (IllegalArgumentException e) { // handle error when student already has a consultation
            logger.warning("Attempted to add consultation to student with existing consultation: " + toAdd);
            throw new CommandException(e.getMessage());
        }

        model.addConsultation(toAdd);
        logger.info("Successfully added new consultation: " + toAdd);
        String commandResult = String.format(MESSAGE_SUCCESS, Messages.format(toAdd));
        if (Duration.between(toAdd.getFrom(), toAdd.getTo()).toMinutes() > 3 * 60) {
            commandResult = commandResult + "\n" + MESSAGE_CONSULTATION_DURATION_TOO_LONG;
        }
        LocalDateTime now = LocalDateTime.now();
        if (!toAdd.getTo().isAfter(now)) {
            commandResult = commandResult + "\n" + MESSAGE_CONSULTATION_IS_OVER;
        } else if (!toAdd.getFrom().isAfter(now)) {
            commandResult = commandResult + "\n" + MESSAGE_CONSULTATION_IS_ONGOING;
        }

        return new CommandResult(commandResult, false, false, true);
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
