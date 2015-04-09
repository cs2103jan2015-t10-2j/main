import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DeleteCommandHandlerTest extends StringBasedTest {

    private TaskData taskData;

    private static final String commandAdd = "add Homework at 4:00 11/3/2015 for 60 mins @ Tembusu College desc \"Work on CS2103 project\" setPrior HIGH";
    private static final String commandDisplay = "display month";
    private static final String commandDisplayPrevious = "3";
    private static final String commandDisplayExit = "5";
    private static final String commandDelete = "delete 1";

    //@author A0134704M
    @Override
    public TaskData createTaskData() {
        taskData = new TaskData();
        return taskData;
    }

    //@author A0134704M
    @Test
    public void testExecute() {
        // Add an event
        super.executeCommand(commandAdd);
        assertEquals(1, taskData.getEventMap().size());

        // Cannot delete without display first
        assertEquals(1, taskData.getEventMap().size());
        super.executeCommand(commandDelete);
        assertEquals(1, taskData.getEventMap().size());

        // Display and delete
        super.executeCommand(commandDisplay);
        super.executeCommand(commandDisplayPrevious);
        super.executeCommand(commandDisplayExit);
        super.executeCommand(commandDelete);
        assertEquals(0, taskData.getEventMap().size());
    }
}
