package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;


public class PairCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @BeforeEach
    void setUp() {
        // Fresh model EVERY test run
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_validIndexes_success() throws Exception {
        Index tutorIndex = INDEX_FIRST_PERSON;
        Index tuteeIndex = INDEX_SECOND_PERSON;
        PairCommand pairCommand = new PairCommand(tutorIndex, Collections.singletonList(tuteeIndex));

        Person tutor = (new Person.PersonBuilder(model.getFilteredPersonList().get(tutorIndex.getZeroBased())))
                .build();
        Person tutee = (new Person.PersonBuilder(model.getFilteredPersonList().get(tuteeIndex.getZeroBased())))
                .build();
        String expectedMessage = String.format(PairCommand.MESSAGE_EDIT_PERSON_SUCCESS, tutor.getName().toString(),
                "{" + tutee.getName().toString() + "}");

        CommandResult commandResult = pairCommand.execute(model);
        assertEquals(expectedMessage, commandResult.getFeedbackToUser());
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        ArrayList<Index> indexes = new ArrayList<>();
        indexes.add(INDEX_FIRST_PERSON);
        PairCommand pairCommand = new PairCommand(outOfBoundIndex, indexes);

        assertThrows(CommandException.class, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, () ->
                pairCommand.execute(model));
    }

    @Test
    public void execute_invalidPartnerIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        ArrayList<Index> indexes = new ArrayList<>();
        indexes.add(outOfBoundIndex);
        PairCommand pairCommand = new PairCommand(INDEX_FIRST_PERSON, indexes);

        assertThrows(CommandException.class, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, () ->
                pairCommand.execute(model));
    }

    @Test
    public void equals() {
        PairCommand pairFirstCommand = new PairCommand(INDEX_FIRST_PERSON,
                Collections.singletonList(INDEX_SECOND_PERSON));
        PairCommand pairSecondCommand = new PairCommand(INDEX_SECOND_PERSON,
                Collections.singletonList(INDEX_FIRST_PERSON));

        // same object -> returns true
        assertTrue(pairFirstCommand.equals(pairFirstCommand));

        // same values -> returns true
        PairCommand pairFirstCommandCopy = new PairCommand(INDEX_FIRST_PERSON,
                Collections.singletonList(INDEX_SECOND_PERSON));
        assertTrue(pairFirstCommand.equals(pairFirstCommandCopy));

        // different types -> returns false
        assertFalse(pairFirstCommand.equals(1));

        // null -> returns false
        assertFalse(pairFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(pairFirstCommand.equals(pairSecondCommand));
    }
}
