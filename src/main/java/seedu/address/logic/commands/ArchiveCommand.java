package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ACTIVE_PERSONS;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Archives one or more persons identified by their indexes in the displayed list.
 */
public class ArchiveCommand extends Command {

    public static final String COMMAND_WORD = "archive";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Archives one or more persons identified by their indexes in the displayed person list.\n"
        + "Parameters: INDEX[,INDEX]... (each must be a positive integer)\n"
        + "Example (single): " + COMMAND_WORD + " 2\n"
        + "Example (multiple): " + COMMAND_WORD + " 1,3,5";

    public static final String MESSAGE_ALREADY_ARCHIVED = "One or more selected persons are already "
        + "archived: %s";
    public static final String MESSAGE_SUCCESS = "Archived: %s";

    private final List<Index> targetIndexes;

    /**
     * Constructs an {@code ArchiveCommand} to archive one or more persons in the displayed list.
     * Duplicate indexes are removed while preserving the original order.
     *
     * @param targetIndexes The list of one-based indexes of persons to archive.
     * @throws NullPointerException if {@code targetIndexes} is null.
     */
    public ArchiveCommand(List<Index> targetIndexes) {
        requireNonNull(targetIndexes);
        // remove duplicates while preserving order
        Set<Integer> seenIndexes = new LinkedHashSet<>();
        this.targetIndexes = targetIndexes.stream()
            .filter(index -> seenIndexes.add(index.getZeroBased()))
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> displayedPersons = model.getFilteredPersonList();

        // pre validation
        List<Person> personsToArchive = new ArrayList<>(targetIndexes.size());
        List<String> alreadyArchivedNames = new ArrayList<>();

        for (Index targetIndex : targetIndexes) {
            int zeroBasedIndex = targetIndex.getZeroBased();
            if (zeroBasedIndex >= displayedPersons.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }

            Person person = displayedPersons.get(zeroBasedIndex);
            if (person.isArchived()) {
                alreadyArchivedNames.add(person.getName().toString());
            }
            personsToArchive.add(person);
        }

        if (!alreadyArchivedNames.isEmpty()) {
            throw new CommandException(String.format(MESSAGE_ALREADY_ARCHIVED,
                String.join(", ", alreadyArchivedNames)));
        }

        // apply updates
        List<String> archivedNames = new ArrayList<>(personsToArchive.size());
        for (Person originalPerson : personsToArchive) {
            Person archivedPerson = originalPerson.withArchived(true);
            model.setPerson(originalPerson, archivedPerson);
            archivedNames.add(archivedPerson.getName().toString());
        }

        model.updateFilteredPersonList(PREDICATE_SHOW_ACTIVE_PERSONS);

        return new CommandResult(String.format(MESSAGE_SUCCESS, String.join(", ", archivedNames)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ArchiveCommand)) {
            return false;
        }
        ArchiveCommand otherCommand = (ArchiveCommand) other;
        return targetIndexes.equals(otherCommand.targetIndexes);
    }
}
