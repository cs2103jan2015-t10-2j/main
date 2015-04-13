import org.fusesource.jansi.Ansi.Color;


public class HelpCommandHandler implements ICommandHandler {

    private static final Color CONTENT2_COLOUR = Color.BLUE;
    private static final Color CONTENT1_COLOUR = Color.CYAN;
    private static final String CONTENT_2_COLUMNS_FORMAT = "%-10s%-70s";
    private static final String CONTENT_2_COLUMNS_EXAMPLE_FORMAT = "%-19s%-61s";
    private static final String LINE_SEPARATION = "*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*\n";
    private static final String LINE_COMMAND_USAGE = "\n*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*COMMAND USAGE*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*'*\n";
    private static final Object STRING_HELP = "help";

    //@author A0134704M
    @Override
    public boolean parseCommand(String command) {
        return (STRING_HELP.equals(command));
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
        ConsoleUtility.printf(Color.RED, LINE_COMMAND_USAGE);
        printContent(Color.YELLOW, CONTENT_2_COLUMNS_FORMAT, "COMMAND", "USAGE");
        printContent(CONTENT1_COLOUR, CONTENT_2_COLUMNS_FORMAT, "", "");
        printContent(CONTENT1_COLOUR, CONTENT_2_COLUMNS_FORMAT, "add", "Add task details");
        printContent(CONTENT1_COLOUR, CONTENT_2_COLUMNS_FORMAT, "", "Example: add Dinner with Lily 7pm tomorrow");
        printContent(CONTENT1_COLOUR, CONTENT_2_COLUMNS_EXAMPLE_FORMAT, "", "add Asg 1 due next Monday desc \"via CodeCrunch\" setPrior High");
        printContent(CONTENT1_COLOUR, CONTENT_2_COLUMNS_FORMAT, "", "");
        printContent(CONTENT2_COLOUR, CONTENT_2_COLUMNS_FORMAT, "display", "Show tasks details and get tasks ID");
        printContent(CONTENT2_COLOUR, CONTENT_2_COLUMNS_FORMAT, "", "Example: display");
        printContent(CONTENT2_COLOUR, CONTENT_2_COLUMNS_EXAMPLE_FORMAT, "", "display week");
        printContent(CONTENT2_COLOUR, CONTENT_2_COLUMNS_EXAMPLE_FORMAT, "", "display 15/4/2015");
        printContent(CONTENT2_COLOUR, CONTENT_2_COLUMNS_EXAMPLE_FORMAT, "", "display checklist");
        printContent(CONTENT1_COLOUR, CONTENT_2_COLUMNS_FORMAT, "", "");
        printContent(CONTENT1_COLOUR, CONTENT_2_COLUMNS_FORMAT, "alter", "Modify details of a task with its ID");
        printContent(CONTENT1_COLOUR, CONTENT_2_COLUMNS_FORMAT, "", "Example: alter 1 as 3:00 pm 4 May");
        printContent(CONTENT1_COLOUR, CONTENT_2_COLUMNS_EXAMPLE_FORMAT, "", "alter 2 as @ ABC Restaurant");
        printContent(CONTENT1_COLOUR, CONTENT_2_COLUMNS_FORMAT, "", "");
        printContent(CONTENT2_COLOUR, CONTENT_2_COLUMNS_FORMAT, "search", "Find a task with keywords");
        printContent(CONTENT2_COLOUR, CONTENT_2_COLUMNS_FORMAT, "", "Example: search meeting (for finding task with the word \"meeting\"");
        printContent(CONTENT2_COLOUR, CONTENT_2_COLUMNS_FORMAT, "", "");
        printContent(CONTENT1_COLOUR, CONTENT_2_COLUMNS_FORMAT, "save", "Save the changes made. If no path is specified, the default path");
        printContent(CONTENT1_COLOUR, CONTENT_2_COLUMNS_FORMAT, "", "is where the program saved.");
        printContent(CONTENT1_COLOUR, CONTENT_2_COLUMNS_FORMAT, "", "Example: save ..\\Documents\\FileName.dat");
        printContent(CONTENT1_COLOUR, CONTENT_2_COLUMNS_FORMAT, "", "");
        printContent(CONTENT2_COLOUR, CONTENT_2_COLUMNS_FORMAT, "delete", "Remove a task from data with its ID");
        printContent(CONTENT2_COLOUR, CONTENT_2_COLUMNS_FORMAT, "", "");
        printContent(CONTENT1_COLOUR, CONTENT_2_COLUMNS_FORMAT, "done", "Mark a task as finished with its ID");
        printContent(CONTENT1_COLOUR, CONTENT_2_COLUMNS_FORMAT, "", "");
        printContent(CONTENT2_COLOUR, CONTENT_2_COLUMNS_FORMAT, "clear", "Clear the screen");
        printContent(CONTENT2_COLOUR, CONTENT_2_COLUMNS_FORMAT, "", "");
        printContent(CONTENT1_COLOUR, CONTENT_2_COLUMNS_FORMAT, "undo", "Undo last change");
        printContent(CONTENT1_COLOUR, CONTENT_2_COLUMNS_FORMAT, "", "");
        printContent(CONTENT2_COLOUR, CONTENT_2_COLUMNS_FORMAT, "redo", "Redo last change");
        printContent(CONTENT2_COLOUR, CONTENT_2_COLUMNS_FORMAT, "", "");
        printContent(CONTENT1_COLOUR, CONTENT_2_COLUMNS_FORMAT, "history", "Show detail steps of changes");
        printContent(CONTENT1_COLOUR, CONTENT_2_COLUMNS_FORMAT, "", "");
        printContent(CONTENT2_COLOUR, CONTENT_2_COLUMNS_FORMAT, "exit", "Quit the program");
        printContent(CONTENT2_COLOUR, CONTENT_2_COLUMNS_FORMAT, "", "");
        ConsoleUtility.printf(Color.RED, LINE_SEPARATION);

    }

    public void printContent(Color color, String format, Object... content) {
        ConsoleUtility.printf(Color.RED, "＊ ");
        ConsoleUtility.printf(color, format, content);
        ConsoleUtility.printf(Color.RED, " ＊\n");
    }

    //@author A0134704M
    @Override
    public boolean isExtraInputNeeded() {
        return false;
    }

}
