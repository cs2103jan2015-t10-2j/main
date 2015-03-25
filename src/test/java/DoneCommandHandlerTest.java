<<<<<<< HEAD
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DoneCommandHandlerTest {

    private TaskData taskData;
    private DoneCommandHandler doneCommandHandler;

    private int taskId = 24356;
    private String commandDone = "done 24356";

    @Before
    public void setUp() throws Exception {
        taskData = new TaskData();
        doneCommandHandler = new DoneCommandHandler(taskData);
=======
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DoneCommandHandlerTest extends StringBasedTest {

    private TaskData taskData;

    private static final String commandAdd = "add Homework at 4:00 11/3/2015 @ Tembusu College desc \"Work on CS2103 project\"";
    private static final String commandYes = "Y";
    private static final String commandDisplay = "display 11/3/2015";
    private static final String commandViewOption = "3";
    private static final String commandDone = "done 1";

    @Override
    public TaskData createTaskData() {
        taskData = new TaskData();
        return taskData;
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
    }

    @Test
    public void testExexcuteCommand() {
<<<<<<< HEAD
        Event event = new Event();

        event.setTaskID(taskId);
        taskData.getEventMap().put(event.getTaskID(), event);

        assertFalse(taskData.getEventMap().get(taskId).isDone());
        assertTrue(doneCommandHandler.parseCommand(commandDone));
        assertTrue(doneCommandHandler.executeCommand());
        assertTrue(taskData.getEventMap().get(taskId).isDone());
    }

=======
        // Add an event
        super.executeCommand(commandAdd);
        super.executeCommand(commandYes);
        assertEquals(1, taskData.getEventMap().size());
        Event event = taskData.getEventMap().values().iterator().next();

        // Cannot mark done without display first
        assertFalse(event.isDone());
        super.executeCommand(commandDone);
        assertFalse(event.isDone());

        // Display and make done
        super.executeCommand(commandDisplay);
        super.executeCommand(commandViewOption);
        super.executeCommand(commandDone);
        assertTrue(event.isDone());
    }
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
}
