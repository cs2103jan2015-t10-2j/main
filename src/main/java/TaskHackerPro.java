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

    public TaskHackerPro() {
        taskData = new TaskData();

        commandHandlerMap = new HashMap<String, ICommandHandler>();
        commandHandlerMap.put("add", new AddCommandHandler(taskData));
        commandHandlerMap.put("exit", new ExitCommandHandler(this));
        commandHandlerMap.put("done", new DoneCommandHandler(taskData));
    }

    public void printErrorMsg() {
        System.out.println("Error!");
    }

    public void parseCommand(InputStream is) {
        Scanner scanner = new Scanner(is);

        do {
            String command = scanner.next();
            String inputLine = scanner.nextLine().trim();
            ICommandHandler handler = commandHandlerMap.get(command.toLowerCase());
            if (handler == null) {
                printErrorMsg();
            } else if (handler.parseCommand(inputLine)) {
                if (!handler.executeCommand()) {
                    printErrorMsg();
                }
            } else {
                printErrorMsg();
            }
        } while (isContinue);

        scanner.close();
    }

    public static void main(String[] args) {
        System.out.println("Welcome to TaskHackerPro!");
        new TaskHackerPro().parseCommand(System.in);
    }

    public void setContinue(boolean isContinue) {
        this.isContinue = isContinue;
    }
}
