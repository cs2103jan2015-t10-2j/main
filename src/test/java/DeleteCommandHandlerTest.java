<<<<<<< HEAD
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
=======
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DeleteCommandHandlerTest extends StringBasedTest {

    private TaskData taskData;

    private static final String commandAdd = "add Homework at 4:00 11/3/2015 @ Tembusu College desc \"Work on CS2103 project\"";
    private static final String commandYes = "Y";
    private static final String commandDisplay = "display 11/3/2015";
    private static final String commandViewOption = "3";
    private static final String commandDelete = "delete 1";

    @Override
    public TaskData createTaskData() {
        taskData = new TaskData();
        return taskData;
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
    }

    @Test
    public void testExecute() {
<<<<<<< HEAD
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

=======
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
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
}
