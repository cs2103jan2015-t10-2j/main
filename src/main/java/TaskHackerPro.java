import java.io.IOException;
import java.util.Map;

/**
 * 
 * @author t10-2j
 *
 */

public class TaskHackerPro {

    private static final String messageWelcome = "Welcome to TaskHackerPro!";
    private IInputSource inputSource;
    private Map<String, ICommandHandler> commandHandlerMap;
    private TaskData taskData;
    private boolean isContinue = true;

    private static final String MESSAGE_COMMAND_NOT_FOUND = "Command not found";
    private static final String MESSAGE_FORMAT_INCORRECT = "Format incorrect";
    private static final String MESSAGE_FAIL_EXECUTION = "Fail execution";

    public void printErrorMsg(String command, String message) {
        System.out.printf("%s: %s\n", command, message);
    }

    public void parseCommand() throws IOException {
        System.out.println(messageWelcome);

        while (isContinue && inputSource.hasNextLine()) {
            String inputLine = inputSource.getNextLine();
            int commandEndPosition = inputLine.indexOf(' ');
            String command;

            if (commandEndPosition >= 0) {
                command = inputLine.substring(0, commandEndPosition);
            } else {
                command = inputLine;
            }

            ICommandHandler handler = commandHandlerMap.get(command.toLowerCase());
            assertObjectNotNull(handler);

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
                    if (isExtraInputNeeded && inputSource.hasNextLine()) {
                        inputLine = inputSource.getNextLine();
                    }
                } while (isExtraInputNeeded);
            }
        }

        DataManager.getInstance().saveTaskDataToFile(taskData);
        inputSource.closeSource();
    }

    public TaskData getTaskData() {
        assertObjectNotNull(taskData);
        return taskData;
    }

    public void setCommandHandlerMap(Map<String, ICommandHandler> commandHandlerMap) {
        assertObjectNotNull(this);
        this.commandHandlerMap = commandHandlerMap;
    }

    public void setInputSource(IInputSource inputSource) {
        assertObjectNotNull(this);
        this.inputSource = inputSource;
    }

    public void setTaskData(TaskData taskData) {
        assertObjectNotNull(this);
        this.taskData = taskData;
    }

    public void setContinue(boolean isContinue) {
        assertObjectNotNull(this);
        this.isContinue = isContinue;
    }

    public static void main(String[] args) {
        IInputSource inputSource = new ConsoleInputSource(System.in);
        TaskData taskData = DataManager.getInstance().loadTaskDataFromFile();
        new TaskHackerProRunner(inputSource, taskData).start();
    }

    private void assertObjectNotNull(Object o) {
        assert (o != null);
    }
}
