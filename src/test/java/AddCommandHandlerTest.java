import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Test;

public class AddCommandHandlerTest extends StringBasedTest {

    private TaskData taskData;

    private static final String commandAdd = "add Homework at 4:00 11/3/2015 @ Tembusu College desc \"Work on CS2103 project\"";
    private static final String commandYes = "Y";

    @Override
    public TaskData createTaskData() {
        taskData = new TaskData();
        return taskData;
    }

    @Test
    public void testExecuteCommand() {

        super.executeCommand(commandAdd);
        super.executeCommand(commandYes);

        assertEquals(1, taskData.getEventMap().size());
        int taskId = taskData.getEventMap().keySet().iterator().next();

        testTask(taskData.getEventMap().get(taskId));
    }

    private void testTask(Event event) {
        String actualTaskName = event.getTaskName();
        String actualTaskLocation = event.getTaskLocation();
        String actualTaskDescription = event.getTaskDescription();
        Calendar actualTaskDate = event.getTaskDate();

        assertEquals("Homework", actualTaskName);
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
