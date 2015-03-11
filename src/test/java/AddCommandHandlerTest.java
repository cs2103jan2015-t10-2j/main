import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

public class AddCommandHandlerTest {

    private TaskData taskData;
    private AddCommandHandler addCommandHandler;

    private String commandAdd = "add at 4:00 11/3/2015 @ Tembusu College desc \"Work on CS2103 project\"";

    @Before
    public void setUp() throws Exception {
        taskData = new TaskData();
        addCommandHandler = new AddCommandHandler(taskData);
    }

    @Test
    public void testParseCommand() {
        assertTrue(addCommandHandler.parseCommand(commandAdd));

        Event event = addCommandHandler.getEvent();

        testTask(event);
    }

    @Test
    public void testExecuteCommand() {
        assertTrue(addCommandHandler.parseCommand(commandAdd));
        assertTrue(addCommandHandler.executeCommand());

        assertEquals(1, taskData.getEventMap().size());

        testTask(taskData.getEventMap().get(0));
    }

    private void testTask(Event event) {
        String actualTaskLocation = event.getTaskLocation();
        String actualTaskDescription = event.getTaskDescription();
        Calendar actualTaskDate = event.getTaskDate();

        assertEquals("Tembusu College", actualTaskLocation);
        assertEquals("Work on CS2103 project", actualTaskDescription);
        assertEquals(4, actualTaskDate.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, actualTaskDate.get(Calendar.MINUTE));
        assertEquals(Calendar.AM, actualTaskDate.get(Calendar.AM_PM));
        assertEquals(11, actualTaskDate.get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.MARCH, actualTaskDate.get(Calendar.MONTH));
        assertEquals(2015, actualTaskDate.get(Calendar.YEAR));
    }
}
