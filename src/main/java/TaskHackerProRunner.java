import java.util.HashMap;
import java.util.Map;

public class TaskHackerProRunner {

    private IInputSource inputSorurce;
    private TaskHackerPro taskHackerPro;
    private Map<String, ICommandHandler> commandHandlerMap;
    private TaskData taskData;

    public TaskHackerProRunner(IInputSource inputSorurce) {
        this(inputSorurce, null);
    }

    public TaskHackerProRunner(IInputSource inputSorurce, TaskData taskData) {
        this.inputSorurce = inputSorurce;
        this.taskData = taskData;
        this.taskHackerPro = new TaskHackerPro();
        this.commandHandlerMap = new HashMap<String, ICommandHandler>();
    }

    public void setupCommandMap(TaskData taskData) {
        commandHandlerMap.put("add", new AddCommandHandler(taskData));
        commandHandlerMap.put("delete", new DeleteCommandHandler(taskData));
        commandHandlerMap.put("done", new DoneCommandHandler(taskData));
        commandHandlerMap.put("search", new SearchCommandHandler(taskData));
        commandHandlerMap.put("display", new CalendarViewCommandHandler(taskData));
        commandHandlerMap.put("view", new ViewScaleCommandHandler(taskData));
        commandHandlerMap.put("alter", new AlterCommandHandler(taskData));
        commandHandlerMap.put("save", new SaveCommandHandler(taskData));
        commandHandlerMap.put("exit", new ExitCommandHandler(taskHackerPro));
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

        return t;
    }
}
