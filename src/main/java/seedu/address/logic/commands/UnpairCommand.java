package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Changes the remark of an existing person in the address book.
 */
public class UnpairCommand extends Command {

    public static final String COMMAND_WORD = "unpair";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Unpairs person identified "
            + "by the index number used in the displayed person list. "
            + "to other persons by the index number used in the displayed person list\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "<INDEXES>\n"
            + "Example: " + COMMAND_WORD + " 1 3 4 5 ";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Unpaired: %s to %s";
    //public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This pairing doesn't even exist in the address book yet.";

    private final Index index;
    private final List<Index> indicesToUnpair;

    /**
     * @param index of the person in the filtered person list
     * @param indicesToUnpair of the person(s) in the filtered person list to pair them to
     */
    public UnpairCommand(Index index, List<Index> indicesToUnpair) {
        requireAllNonNull(index, indicesToUnpair);

        this.index = index;
        this.indicesToUnpair = indicesToUnpair;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person person = lastShownList.get(index.getZeroBased());

        Set<Person> personsToUnpair = new HashSet<>();
        for (Index indexToUnpair : indicesToUnpair) {
            if (indexToUnpair.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            Person personToUnpair = lastShownList.get(indexToUnpair.getZeroBased());
            if ((personToUnpair == person) || !person.getPairedPersons().contains(personToUnpair)) {
                assert false; // should already have been caught by PairCommandParser
            }
            personsToUnpair.add(personToUnpair);
        }

        for (Person personToUnpair : personsToUnpair) {
            try {
                person.removePerson(personToUnpair);
                model.setPerson(personToUnpair, personToUnpair);
            } catch (IllegalValueException e) {
                throw new CommandException(MESSAGE_DUPLICATE_PERSON);
            }
        }
        model.setPerson(person, person);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);


        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, person.getName().toString(),
                "{" + indicesToUnpair.stream().map(
                        index -> lastShownList.get(index.getZeroBased()).getName().toString()
                ).collect(Collectors.joining(", ")) + "}"));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UnpairCommand)) {
            return false;
        }

        // state check
        UnpairCommand e = (UnpairCommand) other;
        return index.equals(e.index)
                && indicesToUnpair.equals(e.indicesToUnpair);
    }
}
