import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author t10-2j
 *
 */

public class TaskHackerPro {

    private IInputSource inputSource;
    private Map<String, ICommandHandler> commandHandlerMap;
    private TaskData taskData;
    private boolean isContinue = true;

    private static final String PATH_TO_LOAD_AND_SAVE_DATA = "bin/TaskHackerPro.dat";
    private static final String MESSAGE_COMMAND_NOT_FOUND = "Command not found";
    private static final String MESSAGE_FORMAT_INCORRECT = "Format incorrect";
    private static final String MESSAGE_FAIL_EXECUTION = "Fail execution";

    public void printErrorMsg(String command, String message) {
        System.out.printf("%s: %s\n", command, message);
    }

    public void parseCommand() {
        while (isContinue && inputSource.hasNextLine()) {
            String inputLine = inputSource.getNextLine();
            int commandEndPosition = inputLine.indexOf(' ');
            String command;

            if (commandEndPosition >= 0) {
                command = inputLine.substring(0, commandEndPosition);
            } else {
                command = inputLine;
            }

            ICommandHandler handler = commandHandlerMap.get(command
                    .toLowerCase());
            boolean isExtraInputNeeded = false;

            if (handler == null) {
                printErrorMsg(command, MESSAGE_COMMAND_NOT_FOUND);
            } else {
                do {
                    if (handler.parseCommand(inputLine)) {
                        if (!handler.executeCommand()) {
                            printErrorMsg(command, MESSAGE_FAIL_EXECUTION);
                        }
                    } else {
                        printErrorMsg(command, MESSAGE_FORMAT_INCORRECT);
                    }

                    isExtraInputNeeded = handler.isExtraInputNeeded();
                    if (isExtraInputNeeded) {
                        inputLine = inputSource.getNextLine();
                    }
                } while (isExtraInputNeeded);
            }
        }

        inputSource.closeSource();
    }

    public TaskData getTaskData() {
        return taskData;
    }

    public void setCommandHandlerMap(
            Map<String, ICommandHandler> commandHandlerMap) {
        this.commandHandlerMap = commandHandlerMap;
    }

    public void setInputSource(IInputSource inputSource) {
        this.inputSource = inputSource;
    }

    public void setTaskData(TaskData taskData) {
        this.taskData = taskData;
    }

    public void setContinue(boolean isContinue) {
        this.isContinue = isContinue;
    }

    public static void main(String[] args) {
        System.out.println("Welcome to TaskHackerPro!");

        TaskHackerPro taskHackerPro = new TaskHackerPro();
        IInputSource inputSorurce = new ConsoleInputSource(System.in);
        Map<String, ICommandHandler> commandHandlerMap = new HashMap<String, ICommandHandler>();
        TaskData taskData = null;
        DataManager dataManager = new DataManager();

        try {
            taskData = dataManager.loadTaskDataFromFile(PATH_TO_LOAD_AND_SAVE_DATA);
            System.out.printf("Data file loaded successfully with %d events!\n",
                    taskData.getEventMap().size());
        } catch (IOException e) {
            System.out.println("Data file not found");
        } catch (ClassNotFoundException e) {
            System.out.println("Data file cannot be loaded");
        }

        if (taskData == null) {
            taskData = new TaskData();
        }

        commandHandlerMap.put("add", new AddCommandHandler(taskData));
        commandHandlerMap.put("delete", new DeleteCommandHandler(taskData));
        commandHandlerMap.put("done", new DoneCommandHandler(taskData));
        commandHandlerMap.put("search", new SearchCommandHandler(taskData));
        commandHandlerMap.put("view_diff_time", new CalendarViewCommandHandler(taskData));
        commandHandlerMap.put("alter", new AlterCommandHandler(taskData));
        commandHandlerMap.put("exit", new ExitCommandHandler(taskHackerPro));

        taskHackerPro.setInputSource(inputSorurce);
        taskHackerPro.setTaskData(taskData);
        taskHackerPro.setCommandHandlerMap(commandHandlerMap);
        taskHackerPro.parseCommand();

        try {
            dataManager.saveTaskDataToFile(PATH_TO_LOAD_AND_SAVE_DATA, taskData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
