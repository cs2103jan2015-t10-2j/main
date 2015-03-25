import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class TaskHackerProRunner {

    private IInputSource inputSorurce;
    private TaskHackerPro taskHackerPro;
    private Map<String, ICommandHandler> commandHandlerMap;
    private TaskData taskData;

    private final Semaphore outputLinesAvailableMutex;

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
        this.outputLinesAvailableMutex = new Semaphore(0, true);
        this.taskHackerPro = new TaskHackerPro(outputLinesAvailableMutex);
        this.commandHandlerMap = new HashMap<String, ICommandHandler>();

        if (isPrintToString) {
            MultiOutputStream mos = new MultiOutputStream(System.out, true);
            baos = new ByteArrayOutputStream();
            mos.addOutputStream(baos);
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

    public String[] getOutputLines() {
        try {
            outputLinesAvailableMutex.acquire();

            if (baos != null) {
                try {
                    baos.flush();
                    String outputLines = baos.toString("UTF-8");
                    String[] returnValue = outputLines.split("[\\r\\n]+");
                    baos.reset();

                    return returnValue;
                } catch (IOException e) {

                }
            }
        } catch (InterruptedException e) {

        }
        return null;
    }

    public Thread start() {
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

        // Erase welcome message
        this.getOutputLines();

        return t;
    }
}
