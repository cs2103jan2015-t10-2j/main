import java.io.IOException;
import java.text.DateFormat;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TaskHackerPro {

    private IInputSource inputSource;
    private Stack<Entry<ICommand, String>> undoStack;
    private Stack<Entry<ICommand, String>> redoStack;
    private Map<String, ICommandHandler> commandHandlerMap;
    private TaskData taskData;
    private boolean isContinue = true;

    private static final Logger logger = Logger.getGlobal();
    
    private static final String COMMAND_ADDED_TO_HISTORY = "%s: command added to history";

    private static final String WELCOME_MESSAGE = "Welcome to TaskHackerPro!\nEnter \"help\" for system usage\n";
    private static final String MESSAGE_COMMAND_PROMPT = "\nEnter command: ";
    private static final String MESSAGE_COMMAND_NOT_FOUND = "Command not found";
    private static final String MESSAGE_FORMAT_INCORRECT = "Format incorrect";
    private static final String MESSAGE_FAIL_EXECUTION = "Fail execution";
    private static final String MESSAGE_OVERDUE_COUNT = "\nThere are %d overdue task(s).\n";
    private static final String MESSAGE_OVERDUE_TASK_DISPLAY = "%3d [%s] %s\n";
    
    //@author A0134704M
    public TaskHackerPro(Stack<Entry<ICommand, String>> undoStack,
            Stack<Entry<ICommand, String>> redoStack) {
        this.undoStack = undoStack;
        this.redoStack = redoStack;
    }

    //@author A0134704M
    public void printErrorMsg(String command, String message) {
        System.out.printf("%s: %s\n", command, message);
    }

    //@author A0134704M
    private boolean printPromptAndWaitForNewLine() {
        System.out.printf(MESSAGE_COMMAND_PROMPT);
        return inputSource.hasNextLine();
    }

    //@author A0134704M
    private void showWelcomeMessage() {
        System.out.println(WELCOME_MESSAGE);
        printLogo();
        List<Event> overdueTasks = taskData.getOverdueTask();
        List<Integer> selectedIds = overdueTasks.stream().map(event -> event.getTaskID())
                .collect(Collectors.toList());
        taskData.updateDisplayID(selectedIds);

        System.out.printf(MESSAGE_OVERDUE_COUNT, overdueTasks.size());
        for (Event overdueTask : overdueTasks) {
            int displayId = taskData.getDisplayId(overdueTask.getTaskID());
            String dueTime = DateFormat.getDateTimeInstance().format(
                    overdueTask.getTaskDueDate().getTime());
            System.out.printf(MESSAGE_OVERDUE_TASK_DISPLAY, displayId, dueTime,
                    overdueTask.getTaskName());
        }
    }

    private void printLogo() {
        System.out.println("████████╗ █████╗ ███████╗██╗  ██╗██╗  ██╗ █████╗  ██████╗██╗  ██╗███████╗██████╗ ██████╗ ██████╗ ██████╗ ");
        System.out.println("╚══██╔══╝██╔══██╗██╔════╝██║ ██╔╝██║  ██║██╔══██╗██╔════╝██║ ██╔╝██╔════╝██╔══██╗██╔══██╗██╔══██╗██╔═══██╗");
        System.out.println("   ██║   ███████║███████╗█████╔╝ ███████║███████║██║     █████╔╝ █████╗  ██████╔╝██████╔╝██████╔╝██║   ██║");
        System.out.println("   ██║   ██╔══██║╚════██║██╔═██╗ ██╔══██║██╔══██║██║     ██╔═██╗ ██╔══╝  ██╔══██╗██╔═══╝ ██╔══██╗██║   ██║");
        System.out.println("   ██║   ██║  ██║███████║██║  ██╗██║  ██║██║  ██║╚██████╗██║  ██╗███████╗██║  ██║██║     ██║  ██║╚██████╔╝");
        System.out.println("   ╚═╝   ╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝╚═╝     ╚═╝  ╚═╝ ╚═════╝ ");
    }

    //@author A0134704M
    public void parseCommand() throws IOException {
        showWelcomeMessage();
        
        while (isContinue && printPromptAndWaitForNewLine()) {
            String inputLine = inputSource.getNextLine();
            int commandEndPosition = inputLine.indexOf(' ');
            String command;

            if (commandEndPosition >= 0) {
                command = inputLine.substring(0, commandEndPosition);
            } else {
                command = inputLine;
            }

            ICommandHandler handler = commandHandlerMap.get(command.toLowerCase());

            if (handler == null) {
                printErrorMsg(command, MESSAGE_COMMAND_NOT_FOUND);
            } else {
                boolean isExtraInputNeeded = performCommandLifeCycle(handler, inputLine, command);

                while (isExtraInputNeeded && printPromptAndWaitForNewLine()) {
                    inputLine = inputSource.getNextLine();
                    isExtraInputNeeded = performCommandLifeCycle(handler, inputLine, command);
                }
            }
        }

        DataManager.getInstance().saveTaskDataToFile(taskData);
        inputSource.closeSource();
    }

    //@author A0134704M
    private boolean performCommandLifeCycle(ICommandHandler handler, String inputLine, String command) {
        boolean isCommandFormatCorrect = handler.parseCommand(inputLine);
        boolean isExtraInputNeeded = handler.isExtraInputNeeded();
        ICommand commandReady = handler.getCommand();
        
        logger.info(String.format("Input Command: %s", inputLine));

        if (isCommandFormatCorrect) {
            if (commandReady != null) {
                if (commandReady.execute()) {
                    Entry<ICommand, String> commandEntry = new AbstractMap.SimpleEntry<ICommand, String>(
                            commandReady, inputLine);
                    logger.info(String.format(COMMAND_ADDED_TO_HISTORY, command));

                    // Clear redo stack if data is changed
                    if (commandReady.isReversible()) {
                        undoStack.push(commandEntry);
                        redoStack.clear();
                    }
                    
                    logger.info("===========");
                    logger.info(String.format(
                            "undo: size=%d, redo: size=%d, last command=[%s]",
                            undoStack.size(), redoStack.size(), command));
                    
                    for (int i=undoStack.size()-1; i>=0; i--) {
                        logger.info(String.format("History %3d: %s", undoStack.size() - i 
                                + 1, undoStack.get(i).getValue()));
                    }
                    logger.info("===========");
                    
                } else {
                    printErrorMsg(command, MESSAGE_FAIL_EXECUTION);
                }
            }
        } else {
            printErrorMsg(command, MESSAGE_FORMAT_INCORRECT);
        }

        return isExtraInputNeeded;
    }

    //@author A0134704M
    public TaskData getTaskData() {
        assertObjectNotNull(taskData);
        return taskData;
    }

    //@author A0134704M
    public void setCommandHandlerMap(Map<String, ICommandHandler> commandHandlerMap) {
        assertObjectNotNull(this);
        this.commandHandlerMap = commandHandlerMap;
    }

    //@author A0134704M
    public void setInputSource(IInputSource inputSource) {
        assertObjectNotNull(this);
        this.inputSource = inputSource;
    }

    //@author A0134704M
    public void setTaskData(TaskData taskData) {
        assertObjectNotNull(this);
        this.taskData = taskData;
    }

    //@author A0134704M
    public void setContinue(boolean isContinue) {
        assertObjectNotNull(this);
        this.isContinue = isContinue;
    }

    //@author A0134704M
    public static void main(String[] args) {
        ConsoleUtility.clearScreen();

        IInputSource inputSource = new ConsoleInputSource(System.in);
        TaskData taskData = DataManager.getInstance().loadTaskDataFromFile();
        new TaskHackerProRunner(inputSource, taskData, Level.OFF).start();
    }

    //@author UNKNOWN
    private void assertObjectNotNull(Object o) {
        assert (o != null);
    }
}
