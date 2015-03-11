import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 
 * @author t10-2j
 *
 */

public class TaskHackerPro {

    private Map<String, ICommandHandler> commandHandlerMap;
    private TaskData taskData;
    private boolean isContinue = true;

    public void printErrorMsg() {
        System.out.println("Error!");
    }

    public void parseCommand(InputStream is) {
        Scanner scanner = new Scanner(is);

        do {
            String command = scanner.next();
            ICommandHandler handler = commandHandlerMap.get(command.toLowerCase());

            if (handler == null) {
                printErrorMsg();
            } else {
                do {
                    String inputLine = (command + scanner.nextLine()).trim();

                    if (handler.parseCommand(inputLine)) {
                        if (!handler.executeCommand()) {
                            printErrorMsg();
                        }
                    } else {
                        printErrorMsg();
                    }
                } while (!handler.isExtraInputNeeded());
            }
        } while (isContinue);

        scanner.close();
    }

    public TaskData getTaskData() {
        return taskData;
    }

    public void setCommandHandlerMap(
            Map<String, ICommandHandler> commandHandlerMap) {
        this.commandHandlerMap = commandHandlerMap;
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
        Map<String, ICommandHandler> commandHandlerMap = new HashMap<String, ICommandHandler>();
        TaskData taskData = new TaskData();

        commandHandlerMap.put("add", new AddCommandHandler(taskData));
        commandHandlerMap.put("exit", new ExitCommandHandler(taskHackerPro));
        commandHandlerMap.put("done", new DoneCommandHandler(taskData));

        taskHackerPro.setCommandHandlerMap(commandHandlerMap);
        taskHackerPro.parseCommand(System.in);
    }
}
