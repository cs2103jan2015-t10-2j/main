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

    public TaskHackerPro() {
        commandHandlerMap = new HashMap<String, ICommandHandler>();
        commandHandlerMap.put("add", new AddCommandHandler());
    }

    public void parseCommand(InputStream is) {
        Scanner scanner = new Scanner(is);
        String command = scanner.next();
        String inputLine = scanner.nextLine().trim();

        for (String key : commandHandlerMap.keySet()) {
            if (key.equalsIgnoreCase(command)) {
                commandHandlerMap.get(key).parseCommand(inputLine);
            }
        }

        scanner.close();
    }

    public static void main(String[] args) {
        System.out.println("Welcome to TaskHackerPro!");
        new TaskHackerPro().parseCommand(System.in);
    }
}
