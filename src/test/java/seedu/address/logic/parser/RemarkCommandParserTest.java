package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.RemarkCommand;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

public class RemarkCommandParserTest {

    private final RemarkCommandParser parser = new RemarkCommandParser();
    private final String nonEmptyRemark = "Likes to swim";

    @Test
    public void parse_indexSpecified_success() {
        // with remark
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_REMARK + nonEmptyRemark;
        RemarkCommand expected = new RemarkCommand(INDEX_FIRST_PERSON, nonEmptyRemark);
        assertParseSuccess(parser, userInput, expected);

        // no remark (empty) → allowed
        userInput = targetIndex.getOneBased() + " " + PREFIX_REMARK;
        expected = new RemarkCommand(INDEX_FIRST_PERSON, "");
        assertParseSuccess(parser, userInput, expected);
    }

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expected = String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE);

        // no parameters at all
        assertParseFailure(parser, "", expected);

        // no index
        assertParseFailure(parser, PREFIX_REMARK + nonEmptyRemark, expected);
    }
}