import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class DeleteCommandHandlerTest {

    private TaskData taskData;
    private DeleteCommandHandler deleteCommandHandler;

    private final int taskId = 24356;
    private final String commandDelete = "delete 24356";

    @Before
    public void setUp() throws Exception {
        taskData = new TaskData();
        deleteCommandHandler = new DeleteCommandHandler(taskData);
    }

    @Test
    public void testExecute() {
        Event event = new Event();

        event.setTaskID(taskId);
        taskData.getEventMap().put(event.getTaskID(), event);

        assertTrue(taskData.getEventMap().containsKey(taskId));
        assertTrue(deleteCommandHandler.isValid(commandDelete));
        assertTrue(deleteCommandHandler.parseCommand(commandDelete));
        assertTrue(deleteCommandHandler.executeCommand());
        assertFalse(taskData.getEventMap().containsKey(taskId));
    }

}
