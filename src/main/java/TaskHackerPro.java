import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.fusesource.jansi.Ansi.Color;

public class TaskHackerPro {

    
    private IInputSource inputSource;
    private Stack<Entry<ICommand, String>> undoStack;
    private Stack<Entry<ICommand, String>> redoStack;
    private Map<String, ICommandHandler> commandHandlerMap;
    private TaskData taskData;
    private boolean isContinue = true;

    private static final int DAY_VISIBLE_COMING_EVENT = 2;

    private static final Logger logger = Logger.getGlobal();
    
    private static final String COMMAND_ADDED_TO_HISTORY = "%s: command added to history";

    private static final String MESSAGE_WELCOME = "Welcome to TaskHackerPro!\nEnter \"help\" for system usage\n";
    public static final String MESSAGE_COMMAND_PROMPT = "\nEnter command: ";
    private static final String MESSAGE_COMMAND_NOT_FOUND = "Command not found";
    private static final String MESSAGE_FORMAT_INCORRECT = "Format incorrect";
    private static final String MESSAGE_FAIL_EXECUTION = "Fail execution";
    private static final String MESSAGE_DUE_TASK_COUNT = "\nThere are %d overdue task(s) and %d coming task(s) in coming %d day(s)\n";
    private static final String MESSAGE_COMING_EVENT_COUNT = "\nThere are %d event(s) in coming %d day(s)\n";
    private static final String MESSAGE_OVERDUE_TASK_DISPLAY = "%3d [%s] %s\n";
    private static final String MESSAGE_COMING_EVENT_DISPLAY = "%3d [%s] %s\n";
    private static final String MESSAGE_CANNOT_LOAD_FROM_CSV = "Cannot load from CSV";
    private static final String MESSAGE_DAMAGE_CSV = "CSV file is damaged. Loaded from the last record.\n";
    
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
        return inputSource.hasNextLine();
    }

    //@author A0134704M
    private void showWelcomeMessage() {
        ConsoleUtility.printLogo();
        System.out.println(MESSAGE_WELCOME);

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm a");
        List<Integer> allActualIds = new ArrayList<Integer>();
        List<Event> allDueTasks = new ArrayList<Event>();

        // Retrieve list of overdue tasks
        List<Event> overdueTasks = taskData.getOverdueTask();
        allDueTasks.addAll(overdueTasks);

        // Retrieve list of due tasks in near future
        Calendar currentTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();

        endTime.add(Calendar.DAY_OF_MONTH, DAY_VISIBLE_COMING_EVENT);
        List<Event> comingDueTasks = taskData.getTaskInDateRange(currentTime, endTime, true);
        allDueTasks.addAll(comingDueTasks);

        List<Integer> dueTaskIds = allDueTasks.stream().map(event -> event.getTaskID())
                .collect(Collectors.toList());
        allActualIds.addAll(dueTaskIds);
        
        // Retrieve list of events in near future
        List<Event> comingEvents = taskData.getTaskInDateRange(currentTime, endTime, false);
        List<Integer> comingEventsIds = comingEvents.stream().map(event -> event.getTaskID())
                .collect(Collectors.toList());
        comingEventsIds.removeAll(dueTaskIds);
        allActualIds.addAll(comingEventsIds);

        taskData.updateDisplayID(allActualIds);

        // Print current time
        System.out.printf("Current time is %s\n",
                dateFormat.format(Calendar.getInstance().getTime()));

        // Print overdue tasks
        System.out.printf(MESSAGE_DUE_TASK_COUNT, overdueTasks.size(),
                comingDueTasks.size(), DAY_VISIBLE_COMING_EVENT);
        for (Event dueTask : allDueTasks) {
            int displayId = taskData.getDisplayId(dueTask.getTaskID());
            Calendar dueTime = dueTask.getTaskDueDate();
            String dueTimeString = dateFormat.format(dueTime.getTime());
            Color color;

            if (currentTime.before(dueTask.getTaskDueDate())) {
                color = Color.GREEN;
            } else {
                color = Color.RED;
            }

            ConsoleUtility.printf(color, MESSAGE_OVERDUE_TASK_DISPLAY, displayId,
                    dueTimeString, dueTask.getTaskName());
        }

        // Print coming events
        System.out.printf(MESSAGE_COMING_EVENT_COUNT, comingEvents.size(),
                DAY_VISIBLE_COMING_EVENT);
        for (Event comingEvent : comingEvents) {
            int displayId = taskData.getDisplayId(comingEvent.getTaskID());
            String timeString = dateFormat.format(comingEvent.getTaskDate().getTime());

            ConsoleUtility.printf(Color.BLUE, MESSAGE_COMING_EVENT_DISPLAY,
                    displayId, timeString, comingEvent.getTaskName());

        }
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
        TaskData taskData;
        
        try {
            taskData = HumanReadable.setDetailsAllEvents(DataManager.getInstance().loadCSVFromDisk());
        } catch (Exception e) {
            taskData = DataManager.getInstance().loadTaskDataFromFile();
            System.out.printf(MESSAGE_DAMAGE_CSV);
            logger.info(MESSAGE_CANNOT_LOAD_FROM_CSV);
        }
        
        new TaskHackerProRunner(inputSource, taskData, Level.OFF).start();
    }

    //@author A0134704M
    private void assertObjectNotNull(Object o) {
        assert (o != null);
    }
}
