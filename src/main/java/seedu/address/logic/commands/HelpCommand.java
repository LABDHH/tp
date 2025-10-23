package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_HELP_MESSAGE = "Opened help window.";

    /**
     * Executes the Help command and returns a {@code CommandResult} containing
     * a formatted list of available sample commands and their usage examples.
     */
    @Override
    public CommandResult execute(Model model) {
        StringBuilder sb = new StringBuilder();

        sb.append("Here are some sample commands you can try:\n\n")
                .append("Legend:\n")
                .append("  [ ]  → Optional field (you can include or skip it)\n")
                .append("  …    → Can be repeated multiple times\n")
                .append("  Note: Names are not case-sensitive, and you can add people with the same name\n")
                .append("        as long as either their phone number or email address is different.\n\n")
                .append("1. addstu n/NAME [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/TAG]…\n")
                .append("   → Adds a new student with the specified details.\n")
                .append("   Example: addstu n/John Tan p/91234567 "
                        + "e/john.tan@example.com a/12 Kent Ridge Rd t/Year2\n\n")
                .append("2. addvol n/NAME [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/TAG]…\n")
                .append("   → Adds a new volunteer with the specified details.\n")
                .append("   Example: addvol n/Emily Wong e/emily.wong@example.com t/Logistics\n\n")
                .append("3. list\n")
                .append("   → Lists all persons (students and volunteers) currently in the system.\n")
                .append("   Example: list\n\n")
                .append("4. edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]…\n")
                .append("   → Edits the details of the person at the specified index for atleast 1 value.\n")
                .append("   Example: edit 2 p/98765432 e/new.email@example.com\n\n")
                .append("5. find KEYWORD [MORE_KEYWORDS]\n")
                .append("   → Finds persons whose names contain any of the given keywords.\n")
                .append("   Example: find John Emily\n\n")
                .append("6. delete INDEX\n")
                .append("   → Deletes the person at the specified index from the address book.\n")
                .append("   Example: delete 3\n\n")
                .append("7. clear\n")
                .append("   → Deletes all entries from the address book.\n")
                .append("   Example: clear\n\n")
                .append("8. exit\n")
                .append("   → Exits the program.\n")
                .append("   Example: exit\n")
                .append("9. findtag TAG\n")
                .append("   → Finds persons with the specified tag.\n")
                .append("   Format: findtag TAG\n")
                .append("   Example: findtag Math\n")
                .append("   (Case-insensitive; matches any person with that tag.)\n\n")
                .append("10. pair INDEX PARTNER_INDEX...\n")
                .append("   → Pairs a person (student or volunteer) with others by their indices.\n")
                .append("   Format: pair INDEX PARTNER_INDEX...\n")
                .append("   Example: pair 2 1 3\n\n")
                .append("11. unpair INDEX PARTNER_INDEX...\n")
                .append("   → Removes existing pairings between persons.\n")
                .append("   Format: unpair INDEX PARTNER_INDEX...\n")
                .append("   Example: unpair 2 1 3\n\n");



        return new CommandResult(sb.toString(), true, false);
    }
}
