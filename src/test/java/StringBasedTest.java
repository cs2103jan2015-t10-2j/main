import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.junit.After;
import org.junit.Before;

/**
 * This class helps virtually enter command line by line. Console output and
 * data behind can be checked between commands. It helps debugging by directly
 * entering command to check if the system matches the expectation.
 *
 * @author Lily Yung
 */
public abstract class StringBasedTest {

    private Semaphore outputLinesAvailableMutex;
    private StringInputSource inputSorurce;
    private TaskHackerProRunner taskHackerProRunner;
    private ByteArrayOutputStream baos;

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

        outputLinesAvailableMutex = new Semaphore(0, true);
        inputSorurce = new StringInputSource(outputLinesAvailableMutex);
        taskHackerProRunner = new TaskHackerProRunner(inputSorurce, taskData);

        MultiOutputStream mos = new MultiOutputStream(System.out, true);
        baos = new ByteArrayOutputStream();
        mos.addOutputStream(baos);
        System.setOut(new PrintStream(mos));

        taskHackerProRunner.start();

        // Erase welcome message
        this.getOutputLines();
    }

    /**
     * Exit the system. This method should not be called explicitly
     */
    @After
    public void exitProgram() {
        outputLinesAvailableMutex.release();
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
        return this.getOutputLines();
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

    private String[] getOutputLines() {
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
}
