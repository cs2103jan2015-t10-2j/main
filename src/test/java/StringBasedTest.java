import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;

/**
 * This class helps virtually enter command line by line. Console output and
 * data behind can be checked between commands. It helps debugging by directly
 * entering command to check if the system matches the expectation..
 *
 * @author Lily Yung
 */
public abstract class StringBasedTest {

    private StringInputSource inputSorurce;
    private TaskHackerProRunner taskHackerProRunner;

    /**
     * Create a {@code TaskData} object that is used by {@code StringBasedTest}
     * subclass. There are 2 ways to get a {@code TaskData} object <blockquote>
     * 
     * <ol>
     * <li>DataManager.getInstance().loadTaskDataFromFile()
     * <li>new TaskData()
     * </ol>
     * 
     * </blockquote>
     * 
     * @return a {@code TaskData} object that is used by {@code StringBasedTest}
     *         subclass.
     */
    public abstract TaskData createTaskData();

    /**
     * Set up TaskHackerRunner. This method should not be called explicitly
     */
    @Before
    public void setUpTaskHackerRunner() {
        TaskData taskData = createTaskData();
        inputSorurce = new StringInputSource();
        taskHackerProRunner = new TaskHackerProRunner(inputSorurce, taskData, true);
        taskHackerProRunner.start();
    }

    /**
     * Exit the system. This method should not be called explicitly
     */
    @After
    public void exitProgram() {
        executeCommand("exit");
    }

    /**
     * Execute a command just like in console. Exception thrown by the system
     * will not be caught. It is blocked until the command is executed and all
     * output is printed to the console.
     * 
     * @param command
     *            command defined by the system
     * @return lines of output produced by the system after command is executed
     */
    public String[] executeCommand(String command) {
        inputSorurce.addCommand(command);
        return taskHackerProRunner.getOutputLines();
    }

    /**
     * Execute a list of command just like in console. Exception thrown by the
     * system will not be caught. It is blocked until all commands are executed
     * and all output is printed to the console.
     * 
     * @param commands
     *            list of commands defined by the system
     * @return list of lines of output produced by the system after all commands
     *         are executed
     */
    public List<String[]> executeCommands(String[] commands) {
        List<String[]> allOutput = new ArrayList<String[]>();

        for (String command : commands) {
            allOutput.add(executeCommand(command));
        }

        return allOutput;
    }
}
