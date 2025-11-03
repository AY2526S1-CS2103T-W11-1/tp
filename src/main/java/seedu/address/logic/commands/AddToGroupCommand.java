package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NUSNETID;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.GroupId;
import seedu.address.model.person.Nusnetid;
import seedu.address.model.person.Person;

/**
 * Adds a person to a group.
 */
public class AddToGroupCommand extends Command {
    public static final String COMMAND_WORD = "add_to_group";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a existing student to a group.\n"
            + "Parameters: " + PREFIX_NUSNETID + "NETID " + PREFIX_GROUP + "GROUPID \n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_NUSNETID + "E1234567 " + PREFIX_GROUP + "T01 \n";
    public static final String MESSAGE_SAME_GROUP_FAIL = "Cannot move students to the same group they are already in.";
    private static final String MESSAGE_SUCCESS = "Student %s added to Group %s.";
    private final GroupId groupId;
    private final Nusnetid nusnetId;
    /**
     * Creates an {@code AddToGroupCommand} to add a person to a group.
     *
     * @param nusnetId the nus netId of the target student
     * @param groupId the ID of the group to add the student to
     */
    public AddToGroupCommand(Nusnetid nusnetId, GroupId groupId) {
        requireNonNull(nusnetId);
        requireNonNull(groupId);
        this.nusnetId = nusnetId;
        this.groupId = groupId;
    }
    /**
     * Executes the command and returns the result message.
     * @param model {@code Model} which the command should operate on.
     * @return the command result message.
     * @throws CommandException if the person is duplicate.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Person target = model.getPersonByNusnetIdFullList(nusnetId);
        model.moveStudentToNewGroup(target, groupId);
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_SUCCESS, nusnetId, groupId));
    }
    /**
     * Checks equality between this AddToGroupCommand and another object.
     * @param other the other object to compare with.
     * @return true if both are AddToGroupCommand instances with the same nus net Id and groupId, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AddToGroupCommand otherCommand)) {
            return false;
        }
        return nusnetId.equals(otherCommand.nusnetId)
                && groupId.equals(otherCommand.groupId);
    }
}
