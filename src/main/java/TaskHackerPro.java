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

    public void printErrorMsg() {
        System.out.println("Error!");
    }

    public void parseCommand() {
        while (isContinue && inputSource.hasNextLine()) {
            String inputLine = inputSource.getNextLine();
            int commandEndPosition = inputLine.indexOf(' ');
            String command;
            
            if(commandEndPosition >= 0) {
                command = inputLine.substring(0, commandEndPosition);
            } else {
                command = inputLine;
            }
            
            ICommandHandler handler = commandHandlerMap.get(command.toLowerCase());
            boolean isExtraInputNeeded = false;

            if (handler == null) {
                printErrorMsg();
            } else {
                do {
                    if (handler.parseCommand(inputLine)) {
                        if (!handler.executeCommand()) {
                            printErrorMsg();
                        }
                    } else {
                        printErrorMsg();
                    }

                    isExtraInputNeeded = handler.isExtraInputNeeded();
                    if (isExtraInputNeeded) {
                        inputLine = inputSource.getNextLine();
                    }
                } while (isExtraInputNeeded);
            }
        }
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
        TaskData taskData = new TaskData();

        commandHandlerMap.put("add", new AddCommandHandler(taskData));
        commandHandlerMap.put("exit", new ExitCommandHandler(taskHackerPro));
        commandHandlerMap.put("done", new DoneCommandHandler(taskData));

        taskHackerPro.setInputSource(inputSorurce);
        taskHackerPro.setCommandHandlerMap(commandHandlerMap);
        taskHackerPro.parseCommand();
    }
}
