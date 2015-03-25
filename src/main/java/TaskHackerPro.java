import java.io.IOException;
<<<<<<< HEAD
import java.util.HashMap;
import java.util.Map;
=======
import java.util.Map;
import java.util.concurrent.Semaphore;
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7

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

<<<<<<< HEAD
    private static final String PATH_TO_LOAD_AND_SAVE_DATA = "bin/TaskHackerPro.dat";
=======
    private final Semaphore outputLinesAvailableMutex;

>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
    private static final String MESSAGE_COMMAND_NOT_FOUND = "Command not found";
    private static final String MESSAGE_FORMAT_INCORRECT = "Format incorrect";
    private static final String MESSAGE_FAIL_EXECUTION = "Fail execution";

<<<<<<< HEAD
    private static final String MESSAGE_DATA_FILE_NOT_FOUND = "Data file is created\n";
    private static final String MESSAGE_DATA_FILE_LOADED = "Data file loaded successfully with %d event(s)!\n";
    private static final String MESSAGE_DATA_FILE_FAIL_TO_LOAD = "Data file cannot be loaded. New data file is created\n";
=======
    public TaskHackerPro(Semaphore outputLinesAvailableMutex) {
        this.outputLinesAvailableMutex = outputLinesAvailableMutex;
    }
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7

    public void printErrorMsg(String command, String message) {
        System.out.printf("%s: %s\n", command, message);
    }

    public void parseCommand() {
<<<<<<< HEAD
=======
        System.out.println("Welcome to TaskHackerPro!");

        outputLinesAvailableMutex.release();
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
        while (isContinue && inputSource.hasNextLine()) {
            String inputLine = inputSource.getNextLine();
            int commandEndPosition = inputLine.indexOf(' ');
            String command;

            if (commandEndPosition >= 0) {
                command = inputLine.substring(0, commandEndPosition);
            } else {
                command = inputLine;
            }

            ICommandHandler handler = commandHandlerMap.get(command.toLowerCase());
<<<<<<< HEAD
        	assertObjectNotNull(handler);
        	
=======
            assertObjectNotNull(handler);

>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
            boolean isExtraInputNeeded = false;

            if (handler == null) {
                printErrorMsg(command, MESSAGE_COMMAND_NOT_FOUND);
            } else {
                do {
                    if (handler.parseCommand(inputLine)) {
                        if (!handler.executeCommand()) {
                            printErrorMsg(command, MESSAGE_FAIL_EXECUTION);
                        }
                    } else {
                        printErrorMsg(command, MESSAGE_FORMAT_INCORRECT);
                    }

                    isExtraInputNeeded = handler.isExtraInputNeeded();
<<<<<<< HEAD
                    if (isExtraInputNeeded) {
=======
                    outputLinesAvailableMutex.release();
                    if (isExtraInputNeeded && inputSource.hasNextLine()) {
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
                        inputLine = inputSource.getNextLine();
                    }
                } while (isExtraInputNeeded);
            }
        }

<<<<<<< HEAD
=======
        try {
            DataManager.getInstance().saveTaskDataToFile(taskData);
        } catch (IOException e) {
            e.printStackTrace();
        }
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
        inputSource.closeSource();
    }

    public TaskData getTaskData() {
<<<<<<< HEAD
    	assertObjectNotNull(taskData);
=======
        assertObjectNotNull(taskData);
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
        return taskData;
    }

    public void setCommandHandlerMap(Map<String, ICommandHandler> commandHandlerMap) {
<<<<<<< HEAD
    	assertObjectNotNull(this);
=======
        assertObjectNotNull(this);
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
        this.commandHandlerMap = commandHandlerMap;
    }

    public void setInputSource(IInputSource inputSource) {
<<<<<<< HEAD
    	assertObjectNotNull(this);
=======
        assertObjectNotNull(this);
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
        this.inputSource = inputSource;
    }

    public void setTaskData(TaskData taskData) {
<<<<<<< HEAD
    	assertObjectNotNull(this);
=======
        assertObjectNotNull(this);
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
        this.taskData = taskData;
    }

    public void setContinue(boolean isContinue) {
<<<<<<< HEAD
    	assertObjectNotNull(this);
=======
        assertObjectNotNull(this);
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
        this.isContinue = isContinue;
    }

    public static void main(String[] args) {
<<<<<<< HEAD
        System.out.println("Welcome to TaskHackerPro!");

        TaskHackerPro taskHackerPro = new TaskHackerPro();
        IInputSource inputSorurce = new ConsoleInputSource(System.in);
        Map<String, ICommandHandler> commandHandlerMap = new HashMap<String, ICommandHandler>();
        TaskData taskData = null;
        DataManager dataManager = new DataManager();

        try {
            taskData = dataManager.loadTaskDataFromFile(PATH_TO_LOAD_AND_SAVE_DATA);
            System.out.printf(MESSAGE_DATA_FILE_LOADED, taskData.getEventMap().size());
        } catch (IOException e) {
            System.out.printf(MESSAGE_DATA_FILE_NOT_FOUND);
        } catch (ClassNotFoundException e) {
            System.out.printf(MESSAGE_DATA_FILE_FAIL_TO_LOAD);
        }

        if (taskData == null) {
            taskData = new TaskData();
        }

        commandHandlerMap.put("add", new AddCommandHandler(taskData));
        commandHandlerMap.put("delete", new DeleteCommandHandler(taskData));
        commandHandlerMap.put("done", new DoneCommandHandler(taskData));
        commandHandlerMap.put("search", new SearchCommandHandler(taskData));
        commandHandlerMap.put("display", new CalendarViewCommandHandler(taskData));
        commandHandlerMap.put("alter", new AlterCommandHandler(taskData));
        commandHandlerMap.put("save", new SaveCommandHandler(taskData, dataManager,
                PATH_TO_LOAD_AND_SAVE_DATA));
        commandHandlerMap.put("exit", new ExitCommandHandler(taskHackerPro));

        taskHackerPro.setInputSource(inputSorurce);
        taskHackerPro.setTaskData(taskData);
        taskHackerPro.setCommandHandlerMap(commandHandlerMap);
        taskHackerPro.parseCommand();

        try {
            dataManager.saveTaskDataToFile(PATH_TO_LOAD_AND_SAVE_DATA, taskData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void assertObjectNotNull(Object o) {
		assert (o != null);
	}
=======
        IInputSource inputSource = new ConsoleInputSource(System.in);
        TaskData taskData = DataManager.getInstance().loadTaskDataFromFile();
        new TaskHackerProRunner(inputSource, taskData).start();
    }

    private void assertObjectNotNull(Object o) {
        assert (o != null);
    }
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
}
