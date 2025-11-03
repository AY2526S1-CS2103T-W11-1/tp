package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Group;
import seedu.address.model.Model;
import seedu.address.model.person.GroupId;

/**
 * Creates a group with a given groupId (used as group identifier).
 */
public class CreateGroupCommand extends Command {
    public static final String COMMAND_WORD = "create_group";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Creates a new group identified by the given group id.\n"
            + "Parameters: " + PREFIX_GROUP + "GROUPID \n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_GROUP + "T12";
    public static final String MESSAGE_DUPLICATE_GROUP = "Group %s already exists!";
    public static final String MESSAGE_SUCCESS = "New group %s created";
    private final GroupId groupId;

    /**
     * Creates a CreateGroupCommand to add the specified {@code groupId}
     */
    public CreateGroupCommand(GroupId groupId) {
        requireNonNull(groupId);
        assert GroupId.isValidGroupId(groupId);
        this.groupId = groupId;
    }
    /**
     * Executes the command and returns the result message.
     * @param model {@code Model} which the command should operate on.
     * @return the command result message.
     * @throws CommandException if the group already exists.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (model.hasGroup(groupId)) {
            throw new CommandException(String.format(MESSAGE_DUPLICATE_GROUP, groupId));
        } else {
            Group newGroup = new Group(groupId);
            model.addGroup(newGroup);
            return new CommandResult(String.format(MESSAGE_SUCCESS, groupId));
        }
    }
}
