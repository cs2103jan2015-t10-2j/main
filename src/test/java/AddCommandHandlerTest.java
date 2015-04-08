import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Test;

public class AddCommandHandlerTest extends StringBasedTest {

    private TaskData taskData;

    private static final String commandAdd1 = "add Homework at 4:00 11/3/2015 for 60 mins @ Tembusu College desc \"Work on CS2103 project\" setPrior HIGH";
    private static final String commandAdd2 = "add do homework for ever";
    private static final String commandAdd3 = "add 123";

    @Override
    public TaskData createTaskData() {
        taskData = new TaskData();
        return taskData;
    }

    @Test
    public void testExecuteCommand1() {
        super.executeCommand(commandAdd1);

        assertEquals(1, taskData.getEventMap().size());
        int taskId = taskData.getEventMap().keySet().iterator().next();

        Event event = taskData.getEventMap().get(taskId);

        String actualTaskName = event.getTaskName();
        String actualTaskLocation = event.getTaskLocation();
        String actualTaskDescription = event.getTaskDescription();
        Calendar actualTaskDate = event.getTaskDate();
        int actualTaskDuration = event.getTaskDuration();
        String actualPriority = event.getTaskPriority().toString().toLowerCase();

        assertEquals("Homework", actualTaskName);
        assertEquals("Tembusu College", actualTaskLocation);
        assertEquals("Work on CS2103 project", actualTaskDescription);
        assertEquals(4, actualTaskDate.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, actualTaskDate.get(Calendar.MINUTE));
        assertEquals(Calendar.AM, actualTaskDate.get(Calendar.AM_PM));
        assertEquals(11, actualTaskDate.get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.MARCH, actualTaskDate.get(Calendar.MONTH));
        assertEquals(2015, actualTaskDate.get(Calendar.YEAR));
        assertEquals(60, actualTaskDuration);
        assertEquals("high", actualPriority);
    }

    @Test
    public void testExecuteCommand2() {
        super.executeCommand(commandAdd2);

        assertEquals(1, taskData.getEventMap().size());
        int taskId = taskData.getEventMap().keySet().iterator().next();

        Event event = taskData.getEventMap().get(taskId);

        String actualTaskName = event.getTaskName();
        assertEquals("do homework for ever", actualTaskName);
    }

    @Test
    public void testExecuteCommand3() {
        super.executeCommand(commandAdd3);

        assertEquals(1, taskData.getEventMap().size());
        int taskId = taskData.getEventMap().keySet().iterator().next();

        Event event = taskData.getEventMap().get(taskId);

        String actualTaskName = event.getTaskName();
        assertEquals("123", actualTaskName);
    }
}
