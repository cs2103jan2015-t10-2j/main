import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class TaskHackerProRunner {

    private IInputSource inputSorurce;
    private TaskHackerPro taskHackerPro;
    private Map<String, ICommandHandler> commandHandlerMap;
    private TaskData taskData;

    private ByteArrayOutputStream baos;

    public TaskHackerProRunner(IInputSource inputSorurce) {
        this(inputSorurce, null);
    }

    public TaskHackerProRunner(IInputSource inputSorurce, TaskData taskData) {
        this(inputSorurce, taskData, false);
    }

    public TaskHackerProRunner(IInputSource inputSorurce, TaskData taskData,
            boolean isPrintToString) {
        this.inputSorurce = inputSorurce;
        this.taskData = taskData;
        this.taskHackerPro = new TaskHackerPro();
        this.commandHandlerMap = new HashMap<String, ICommandHandler>();

        if (isPrintToString) {
            MultiOutputStream mos = new MultiOutputStream(System.out, true);
            this.baos = new ByteArrayOutputStream();

            mos.addOutputStream(baos, true);
            System.setOut(new PrintStream(mos));
        }
    }

    public void setupCommandMap(TaskData taskData) {
        commandHandlerMap.put("add", new AddCommandHandler(taskData));
        commandHandlerMap.put("delete", new DeleteCommandHandler(taskData));
        commandHandlerMap.put("done", new DoneCommandHandler(taskData));
        commandHandlerMap.put("search", new SearchCommandHandler(taskData));
        commandHandlerMap.put("display", new CalendarViewCommandHandler(taskData));
        commandHandlerMap.put("alter", new AlterCommandHandler(taskData));
        commandHandlerMap.put("save", new SaveCommandHandler(taskData));
        commandHandlerMap.put("exit", new ExitCommandHandler(taskHackerPro));
    }

    public String[] getOutputString() {
        try {
            String[] returnString;

            baos.flush();
            returnString = baos.toString("UTF-8").split("[\\r\\n]+");
            baos.reset();
            return returnString;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void start() {
        System.out.println("Welcome to TaskHackerPro!");

        if (taskData == null) {
            this.taskData = new TaskData();
        }
        setupCommandMap(taskData);

        taskHackerPro.setInputSource(inputSorurce);
        taskHackerPro.setTaskData(taskData);
        taskHackerPro.setCommandHandlerMap(commandHandlerMap);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                taskHackerPro.parseCommand();
            }
        });
        t.start();
    }
}
