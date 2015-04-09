import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaskHackerProRunner {

    private IInputSource inputSource;
    private Stack<Entry<ICommand, String>> undoStack;
    private Stack<Entry<ICommand, String>> redoStack;
    
    private TaskHackerPro taskHackerPro;
    private Map<String, ICommandHandler> commandHandlerMap;
    private TaskData taskData;
    
    private static final String MESSAGE_TASK_HACKER_PRO_EXITS_GRACEFULLY = "TaskHackerPro exits gracefully";
    private static final String MESSAGE_TASK_HACKER_PRO_EXITS_UNEXPECTEDLY = "TaskHackerPro exits unexpectedly";

    //@author A0134704M
    public TaskHackerProRunner(IInputSource inputSource) {
        this(inputSource, null);
    }

    //@author A0134704M
    public TaskHackerProRunner(IInputSource inputSource, TaskData taskData) {
        this(inputSource, taskData, Logger.getGlobal().getLevel());
    }

    //@author A0134704M
    public TaskHackerProRunner(IInputSource inputSource, TaskData taskData, Level logLevel) {
        this.inputSource = inputSource;
        this.taskData = taskData;
        
        this.undoStack = new Stack<Entry<ICommand, String>>();
        this.redoStack = new Stack<Entry<ICommand, String>>();
        this.taskHackerPro = new TaskHackerPro(undoStack, redoStack);
        this.commandHandlerMap = new HashMap<String, ICommandHandler>();

        Logger.getGlobal().setLevel(logLevel);
    }

    //@author A0134704M
    public void setupCommandMap(TaskData taskData) {
        commandHandlerMap.put("add", new AddCommandHandler(taskData));
        commandHandlerMap.put("delete", new DeleteCommandHandler(taskData));
        commandHandlerMap.put("done", new DoneCommandHandler(taskData));
        commandHandlerMap.put("search", new SearchCommandHandler(taskData));
        commandHandlerMap.put("display", new CalendarViewCommandHandler(taskData));
        commandHandlerMap.put("alter", new AlterCommandHandler(taskData));
        commandHandlerMap.put("undo", new UndoCommandHandler(undoStack, redoStack));
        commandHandlerMap.put("redo", new RedoCommandHandler(undoStack, redoStack)); 
        commandHandlerMap.put("history", new HistoryCommandHandler(undoStack, redoStack)); 
        commandHandlerMap.put("save", new SaveCommandHandler(taskData));
        commandHandlerMap.put("exit", new ExitCommandHandler(taskHackerPro));
    }

    //@author A0134704M
    public Thread start() {
        if (taskData == null) {
            this.taskData = new TaskData();
        }
        setupCommandMap(taskData);

        taskHackerPro.setInputSource(inputSource);
        taskHackerPro.setTaskData(taskData);
        taskHackerPro.setCommandHandlerMap(commandHandlerMap);
        
        final Thread runnerThread = Thread.currentThread();

        Thread systemThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    taskHackerPro.parseCommand();
                    Logger.getGlobal().info(MESSAGE_TASK_HACKER_PRO_EXITS_GRACEFULLY);
                } catch (Throwable e) {
                    RuntimeException re = new RuntimeException(e);
                    Logger.getGlobal().info(MESSAGE_TASK_HACKER_PRO_EXITS_UNEXPECTEDLY);
                    runnerThread.interrupt();
                    
                    re.fillInStackTrace();
                    throw re;
                }
            }
        });
        systemThread.start();

        return systemThread;
    }
}
