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
    }

    @Test
    public void testExexcuteCommand() {
        Event event = new Event();

        event.setTaskID(taskId);
        taskData.getEventMap().put(event.getTaskID(), event);

        assertFalse(taskData.getEventMap().get(taskId).isDone());
        assertTrue(doneCommandHandler.isValid(commandDone));
        assertTrue(doneCommandHandler.parseCommand(commandDone));
        assertTrue(doneCommandHandler.executeCommand());
        assertTrue(taskData.getEventMap().get(taskId).isDone());
    }

}
