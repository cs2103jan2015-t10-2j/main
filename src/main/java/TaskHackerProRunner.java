import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaskHackerProRunner {

    private IInputSource inputSource;
    private TaskHackerPro taskHackerPro;
    private Map<String, ICommandHandler> commandHandlerMap;
    private TaskData taskData;
    private Throwable uncaughtException;

    public TaskHackerProRunner(IInputSource inputSource) {
        this(inputSource, null);
    }

    public TaskHackerProRunner(IInputSource inputSource, TaskData taskData) {
        this(inputSource, taskData, Logger.getGlobal().getLevel());
    }

    public TaskHackerProRunner(IInputSource inputSource, TaskData taskData, Level logLevel) {
        this.inputSource = inputSource;
        this.taskData = taskData;
        this.taskHackerPro = new TaskHackerPro();
        this.commandHandlerMap = new HashMap<String, ICommandHandler>();

        Logger.getGlobal().setLevel(logLevel);
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

        taskHackerPro.setInputSource(inputSource);
        taskHackerPro.setTaskData(taskData);
        taskHackerPro.setCommandHandlerMap(commandHandlerMap);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    taskHackerPro.parseCommand();
                } catch (IOException e) {
                    TaskHackerProRunner.this.uncaughtException = e;
                }
            }
        });
        t.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                TaskHackerProRunner.this.uncaughtException = e;
            }
        });
        t.start();

        return t;
    }

    public Throwable getUncaughtException() {
        return uncaughtException;
    }
}
