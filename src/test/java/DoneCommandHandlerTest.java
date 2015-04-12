import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DoneCommandHandlerTest extends StringBasedTest {

    private TaskData taskData;

    private static final String commandAdd = "add Homework at 4:00 11/3/2015 for 60 mins @ Tembusu College desc \"Work on CS2103 project\" setPrior HIGH";
    private static final String commandDisplay = "display month";
    private static final String commandDisplayPrevious = "3";
    private static final String commandDisplayExit = "5";
    private static final String commandDone = "done 1";

    //@author A0134704M
    @Override
    public TaskData createTaskData() {
        taskData = new TaskData();
        return taskData;
    }

    //@author A0134704M
    @Test
    public void testExexcuteCommand() {
        // Add an event
        super.executeCommand(commandAdd);
        assertEquals(1, taskData.getEventMap().size());
        Event event = taskData.getEventMap().values().iterator().next();

        // Cannot mark done without display first
        assertFalse(event.isDone());
        super.executeCommand(commandDone);
        assertFalse(event.isDone());

        // Display and mark done
        //super.executeCommand(commandDisplay);
        //super.executeCommand(commandDisplayPrevious);
        //super.executeCommand(commandDisplayExit);
       // super.executeCommand(commandDone);
        //assertTrue(event.isDone());
    }
}
