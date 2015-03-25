
import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Test;

public class AlterCommandHandlerTest extends StringBasedTest {

    private TaskData taskData;

    private static final String commandAdd = "add Homework at 4:00 11/3/2015 for 60 mins @ Tembusu College desc \"Work on CS2103 project\"";
    private static final String commandYes = "Y";
    private static final String commandDisplay = "display 11/3/2015";
    private static final String commandViewOption = "3";
    private static final int taskId = 1;
    private static final String commandAlter = "alter 1 as 2:00 15/3/2015 for 120 mins @ Tembusu College desc \"This homework is very tough!\"";

    private int actualId;
    
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

        // Cannot alter without display first
        assertEquals(1, taskData.getEventMap().size());
        super.executeCommand(commandAlter);
        assertEquals(1, taskData.getEventMap().size());

        // Display and alter
        super.executeCommand(commandDisplay);
        super.executeCommand(commandViewOption);
        super.executeCommand(commandAlter);
        super.executeCommand(commandYes);
        
        actualId = taskData.getActualId(taskId);        
        testTask(taskData.getEventMap().get(actualId));
    }
    
    private void testTask(Event event) {
        String actualTaskName = event.getTaskName();
        String actualTaskLocation = event.getTaskLocation();
        String actualTaskDescription = event.getTaskDescription();
        Calendar actualTaskDate = event.getTaskDate();
        int actualTaskDuration = event.getTaskDuration();

        assertEquals("Homework", actualTaskName);
        assertEquals("Tembusu College", actualTaskLocation);
        assertEquals("This homework is very tough!", actualTaskDescription);
        assertEquals(2, actualTaskDate.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, actualTaskDate.get(Calendar.MINUTE));
        assertEquals(Calendar.AM, actualTaskDate.get(Calendar.AM_PM));
        assertEquals(15, actualTaskDate.get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.MARCH, actualTaskDate.get(Calendar.MONTH));
        assertEquals(2015, actualTaskDate.get(Calendar.YEAR));
        assertEquals(120, actualTaskDuration);
    }

}
