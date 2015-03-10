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
    private boolean isContinue = true;
    private Event event;

    public TaskHackerPro() {
        commandHandlerMap = new HashMap<String, ICommandHandler>();
        commandHandlerMap.put("add", new AddCommandHandler());
        commandHandlerMap.put("exit", new ExitCommandHandler(this));
        commandHandlerMap.put("done", new DoneCommandHandler(event));
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
            } else if (handler.isValid(inputLine)) {
                if(handler.parseCommand(inputLine)) {
                    if(!handler.executeCommand()) {
                        printErrorMsg();
                    }
                } else {
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
