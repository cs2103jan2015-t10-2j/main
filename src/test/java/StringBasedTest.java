import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;

public abstract class StringBasedTest {

    private StringInputSource inputSorurce;
    private TaskHackerProRunner taskHackerProRunner;

    public abstract TaskData createTaskData();

    @Before
    public void setUpTaskHackerRunner() {
        TaskData taskData = createTaskData();
        inputSorurce = new StringInputSource();
        taskHackerProRunner = new TaskHackerProRunner(inputSorurce, taskData, true);
        taskHackerProRunner.start();
    }

    @After
    public void exitProgram() {
        executeCommand("exit");
    }

    public String[] executeCommand(String command) {
        inputSorurce.addCommand(command);
        return taskHackerProRunner.getOutputLines();
    }

    public List<String[]> executeCommands(String[] commands) {
        List<String[]> allOutput = new ArrayList<String[]>();

        for (String command : commands) {
            allOutput.add(executeCommand(command));
        }

        return allOutput;
    }
}
