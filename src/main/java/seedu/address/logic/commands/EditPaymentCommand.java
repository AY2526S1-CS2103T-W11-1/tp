package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.commons.util.CollectionUtil.isAnyNonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.payment.Amount;
import seedu.address.model.payment.Payment;
import seedu.address.model.person.Person;

public class EditPaymentCommand extends Command {
    public static final String COMMAND_WORD = "editpayment";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits one payment of a person.\n"
            + "Parameters: PERSON_INDEX p/PAYMENT_INDEX [a/AMOUNT] [d/DATE] [r/REMARKS]\n"
            + "Examples:\n"
            + "  " + COMMAND_WORD + " 1 p/1 a/10.50\n"
            + "  " + COMMAND_WORD + " 2 p/3 d/2025-10-15 r/late fee waived";

    public static final String MESSAGE_NO_FIELDS = "At least one of a/, d/, r/ must be provided.";
    public static final String MESSAGE_INVALID_PAYMENT_INDEX = "Payment index is invalid for this person.";
    public static final String MESSAGE_SUCCESS = "Edited payment p/%d for %s";

    private final Index personIndex;   // 1-based
    private final int paymentOneBased; // 1-based
    private final EditPaymentDescriptor descriptor;

    public EditPaymentCommand(Index personIndex, int paymentOneBased, EditPaymentDescriptor descriptor) {
        requireNonNull(personIndex);
        requireNonNull(descriptor);
        this.personIndex = personIndex;
        this.paymentOneBased = paymentOneBased;
        this.descriptor = descriptor;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownList = model.getFilteredPersonList();
        if (personIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person target = lastShownList.get(personIndex.getZeroBased());
        int zeroBased = paymentOneBased - 1;
        if (zeroBased < 0 || zeroBased >= target.getPayments().size()) {
            throw new CommandException(MESSAGE_INVALID_PAYMENT_INDEX);
        }

        Payment original = target.getPayments().get(zeroBased);
        Payment edited = createEditedPayment(original, descriptor);

        Person updated = target.withEditedPayment(zeroBased, edited);
        model.setPerson(target, updated);
        return new CommandResult(String.format(MESSAGE_SUCCESS, paymentOneBased, updated.getName()));
    }

    private static Payment createEditedPayment(Payment original, EditPaymentDescriptor d) {
        Amount amount = d.getAmount().orElse(original.getAmount());
        LocalDate date = d.getDate().orElse(original.getDate());
        String remarks = d.getRemarks().orElse(original.getRemarks()); // may be null per your model

        // preserve original recordedAt for auditability
        return new Payment(amount, date, remarks, original.getRecordedAt());
    }

    // -------- descriptor --------
    public static class EditPaymentDescriptor {
        private Amount amount;
        private LocalDate date;
        private String remarks;

        public boolean isAnyFieldEdited() { return isAnyNonNull(amount, date, remarks); }

        public void setAmount(Amount a) { this.amount = a; }
        public void setDate(LocalDate d) { this.date = d; }
        public void setRemarks(String r) { this.remarks = r; }

        public Optional<Amount> getAmount() { return Optional.ofNullable(amount); }
        public Optional<LocalDate> getDate() { return Optional.ofNullable(date); }
        public Optional<String> getRemarks() { return Optional.ofNullable(remarks); }

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof EditPaymentDescriptor)) return false;
            EditPaymentDescriptor that = (EditPaymentDescriptor) o;
            return Objects.equals(amount, that.amount)
                    && Objects.equals(date, that.date)
                    && Objects.equals(remarks, that.remarks);
        }
        @Override public int hashCode() {
            return Objects.hash(amount, date, remarks);
        }
    }

    @Override public boolean equals(Object other) {
        if (other == this) return true;
        if (!(other instanceof EditPaymentCommand)) return false;
        EditPaymentCommand o = (EditPaymentCommand) other;
        return personIndex.equals(o.personIndex)
                && paymentOneBased == o.paymentOneBased
                && Objects.equals(descriptor, o.descriptor);
    }
}
