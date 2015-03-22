import static org.junit.Assert.assertFalse;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class DeleteCommandHandlerTest {

    private TaskData taskData;
    private DeleteCommandHandler deleteCommandHandler;

    private final int taskId = 24356;
    private final String commandDelete = "delete 1";
    private final String commandYes = "Y";

    @Before
    public void setUp() throws Exception {
        taskData = new TaskData();
        deleteCommandHandler = new DeleteCommandHandler(taskData);
    }

    @Test
    public void testExecute() {
        Event event = new Event();
        Set<Integer> actualIds = new HashSet<Integer>();
        actualIds.add(taskId);
        taskData.updateDisplayID(actualIds);
        event.setTaskID(taskData.getDisplayId(taskId));
        taskData.getEventMap().put(event.getTaskID(), event);
        
        assertTrue(taskData.getEventMap().containsKey(taskId));
        assertTrue(deleteCommandHandler.parseCommand(commandDelete));
        assertTrue(deleteCommandHandler.executeCommand());
        assertTrue(deleteCommandHandler.parseCommand(commandYes));
        assertTrue(deleteCommandHandler.executeCommand());
        assertFalse(taskData.getEventMap().containsKey(taskId));
    }

    private void assertTrue(boolean b) {
        // TODO Auto-generated method stub
        
    }

}
