
public class HelpCommandHandler implements ICommandHandler {

    private static final String CONTENT_2_COLUMNS = "＊ %-10s%-70s ＊\n";
    private static final String CONTENT_2_COLUMNS_EXAMPLE = "＊ %-19s%-61s ＊\n";
    private static final String LINE_SEPARATION = "*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*\n";
    private static final String LINE_COMMAND_USAGE = "\n*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*COMMAND USAGE*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*\n";
    private static final Object helpDelimiter = "help";

    //@author A0134704M
    @Override
    public boolean parseCommand(String command) {
        if (helpDelimiter.equals(command)) {

            return true;
        }
        return false;
    }

    //@author A0134704M
    @Override
    public ICommand getCommand() {
        ConsoleUtility.clearScreen();
        printHelpMessage();
        ICommand helpCommand = new NullCommand(); 
        return helpCommand;
    }

    //@author A0134704M
    private void printHelpMessage() {
        System.out.printf(LINE_COMMAND_USAGE);
        System.out.printf(CONTENT_2_COLUMNS, "COMMAND", "USAGE");
        System.out.printf(CONTENT_2_COLUMNS, "", "");
        System.out.printf(CONTENT_2_COLUMNS, "add", "Add task details");
        System.out.printf(CONTENT_2_COLUMNS, "", "Example: add Dinner with Lily 7pm tomorrow");
        System.out.printf(CONTENT_2_COLUMNS_EXAMPLE, "", "add Asg 1 due next Monday desc \"via CodeCrunch\" setPrior High");
        System.out.printf(CONTENT_2_COLUMNS, "", "");
        System.out.printf(CONTENT_2_COLUMNS, "display", "Show tasks' details and get tasks' ID");
        System.out.printf(CONTENT_2_COLUMNS, "", "Example: display");
        System.out.printf(CONTENT_2_COLUMNS_EXAMPLE, "", "display week");
        System.out.printf(CONTENT_2_COLUMNS_EXAMPLE, "", "display 15/4/2015");
        System.out.printf(CONTENT_2_COLUMNS_EXAMPLE, "", "display floating");
        System.out.printf(CONTENT_2_COLUMNS, "", "");
        System.out.printf(CONTENT_2_COLUMNS, "alter", "Modify details of a task with its ID");
        System.out.printf(CONTENT_2_COLUMNS, "Example: ", "alter 1 as 3:00 pm 4 May");
        System.out.printf(CONTENT_2_COLUMNS_EXAMPLE, "", "alter 2 as @ ABC Restaurant");
        System.out.printf(CONTENT_2_COLUMNS, "", "");
        System.out.printf(CONTENT_2_COLUMNS, "search", "Find a task with keywords");
        System.out.printf(CONTENT_2_COLUMNS, "Example: ", "search meeting (for finding task with the word \"meeting\"");
        System.out.printf(CONTENT_2_COLUMNS, "", "");
        System.out.printf(CONTENT_2_COLUMNS, "save", "Save the changes made. If no path is specified, the default path");
        System.out.printf(CONTENT_2_COLUMNS, "", "is where the program saved.");
        System.out.printf(CONTENT_2_COLUMNS, "Example: ", "save \\User\\Documents\\");
        System.out.printf(CONTENT_2_COLUMNS, "", "");
        System.out.printf(CONTENT_2_COLUMNS, "delete", "Remove a task from data with its ID");
        System.out.printf(CONTENT_2_COLUMNS, "", "");
        System.out.printf(CONTENT_2_COLUMNS, "done", "Mark a task as finished with its ID");
        System.out.printf(CONTENT_2_COLUMNS, "", "");
        System.out.printf(CONTENT_2_COLUMNS, "undo", "Undo last change");
        System.out.printf(CONTENT_2_COLUMNS, "", "");
        System.out.printf(CONTENT_2_COLUMNS, "redo", "Redo last change");
        System.out.printf(CONTENT_2_COLUMNS, "", "");
        System.out.printf(CONTENT_2_COLUMNS, "history", "Show detail steps of changes");
        System.out.printf(CONTENT_2_COLUMNS, "", "");
        System.out.printf(CONTENT_2_COLUMNS, "exit", "Quit the program");
        System.out.printf(CONTENT_2_COLUMNS, "", "");
        System.out.printf(LINE_SEPARATION);

    }

    //@author A0134704M
    @Override
    public boolean isExtraInputNeeded() {
        // TODO Auto-generated method stub
        return false;
    }

}
