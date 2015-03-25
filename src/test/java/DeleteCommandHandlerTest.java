import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DeleteCommandHandlerTest extends StringBasedTest {

    private TaskData taskData;

    private static final String commandAdd = "add Homework at 4:00 11/3/2015 for 60 mins @ Tembusu College desc \"Work on CS2103 project\"";
    private static final String commandYes = "Y";
    private static final String commandDisplay = "display 11/3/2015";
    private static final String commandViewOption = "3";
    private static final String commandDelete = "delete 1";

    @Override
    public TaskData createTaskData() {
        taskData = new TaskData();
        return taskData;
    }

    @Test
    public void testExecute() {
        // Add an event
        super.executeCommand(commandAdd);
        super.executeCommand(commandYes);
        assertEquals(1, taskData.getEventMap().size());

        // Cannot delete without display first
        assertEquals(1, taskData.getEventMap().size());
        super.executeCommand(commandDelete);
        assertEquals(1, taskData.getEventMap().size());

        // Display and delete
        super.executeCommand(commandDisplay);
        super.executeCommand(commandViewOption);
        super.executeCommand(commandDelete);
        super.executeCommand(commandYes);
        assertEquals(0, taskData.getEventMap().size());
    }
}
