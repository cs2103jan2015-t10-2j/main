import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Test;

public class NaturalLanguageTest extends StringBasedTest {

    private TaskData taskData;

    private static final String COMMAND_NATURAL_1 = "add Meeting 1 @ Meeting Room 1";
    private static final String COMMAND_NATURAL_2 = "add Meeting with 1h for 1h";
    private static final String COMMAND_NATURAL_3 = "add Go to Forever 21 21Apr";
    private static final String COMMAND_NATURAL_4 = "add Assignment 2 for 2 hr";
    private static final String COMMAND_NATURAL_5 = "add this tuesday Dinner @ Ruby Tuesday 6pm";

    //@author A0134704M
    @Override
    public TaskData createTaskData() {
        taskData = new TaskData();
        return taskData;
    }

    //@author A0134704M
    @Test
    public void testDuplicateWordsInNameAndLocation() {
        super.executeCommand(COMMAND_NATURAL_1);

        assertEquals(1, taskData.getEventMap().size());
        int taskId = taskData.getEventMap().keySet().iterator().next();
        Event event = taskData.getEventMap().get(taskId);

        String actualTaskName = event.getTaskName();
        String actualTaskLocation = event.getTaskLocation();
        String actualTaskDescription = event.getTaskDescription();
        Calendar actualTaskDate = event.getTaskDate();
        int actualTaskDuration = event.getTaskDuration();
        String actualPriority = event.getTaskPriority().toString().toLowerCase();

        assertEquals("Meeting 1", actualTaskName);
        assertEquals("Meeting Room 1", actualTaskLocation);
        assertEquals(null, actualTaskDescription);
        assertEquals(null, actualTaskDate);
        assertEquals(0, actualTaskDuration);
        assertEquals("medium", actualPriority);
    }

    //@author A0134704M
    @Test
    public void testDuplicateWordsInNameAndDuration() {
        super.executeCommand(COMMAND_NATURAL_2);

        assertEquals(1, taskData.getEventMap().size());
        int taskId = taskData.getEventMap().keySet().iterator().next();
        Event event = taskData.getEventMap().get(taskId);

        String actualTaskName = event.getTaskName();
        String actualTaskLocation = event.getTaskLocation();
        String actualTaskDescription = event.getTaskDescription();
        Calendar actualTaskDate = event.getTaskDate();
        int actualTaskDuration = event.getTaskDuration();
        String actualPriority = event.getTaskPriority().toString().toLowerCase();

        assertEquals("Meeting with 1h", actualTaskName);
        assertEquals(null, actualTaskLocation);
        assertEquals(null, actualTaskDescription);
        assertEquals(null, actualTaskDate);
        assertEquals(60, actualTaskDuration);
        assertEquals("medium", actualPriority);
    }

    //@author A0134704M
    @Test
    public void testDuplicateWordsInNameAndDate() {
        super.executeCommand(COMMAND_NATURAL_3);

        assertEquals(1, taskData.getEventMap().size());
        int taskId = taskData.getEventMap().keySet().iterator().next();
        Event event = taskData.getEventMap().get(taskId);

        String actualTaskName = event.getTaskName();
        String actualTaskLocation = event.getTaskLocation();
        String actualTaskDescription = event.getTaskDescription();
        Calendar actualTaskDate = event.getTaskDate();
        int actualTaskDuration = event.getTaskDuration();
        String actualPriority = event.getTaskPriority().toString().toLowerCase();

        assertEquals("Go to Forever 21", actualTaskName);
        assertEquals(null, actualTaskLocation);
        assertEquals(null, actualTaskDescription);
        assertEquals(21, actualTaskDate.get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.APRIL, actualTaskDate.get(Calendar.MONTH));
        assertEquals(2015, actualTaskDate.get(Calendar.YEAR));
        assertEquals(0, actualTaskDuration);
        assertEquals("medium", actualPriority);
    }

    //@author A0134704M
    @Test
    public void testDuplicateWordsInNameAndTime() {
        super.executeCommand(COMMAND_NATURAL_4);

        assertEquals(1, taskData.getEventMap().size());
        int taskId = taskData.getEventMap().keySet().iterator().next();
        Event event = taskData.getEventMap().get(taskId);

        String actualTaskName = event.getTaskName();
        String actualTaskLocation = event.getTaskLocation();
        String actualTaskDescription = event.getTaskDescription();
        Calendar actualTaskDate = event.getTaskDate();
        int actualTaskDuration = event.getTaskDuration();
        String actualPriority = event.getTaskPriority().toString().toLowerCase();

        assertEquals("Assignment 2", actualTaskName);
        assertEquals(null, actualTaskLocation);
        assertEquals(null, actualTaskDescription);
        assertEquals(null, actualTaskDate);
        assertEquals(120, actualTaskDuration);
        assertEquals("medium", actualPriority);
    }
    
  //@author A0134704M
    @Test
    public void testDuplicateWordsInLocationAndDateOfWeek() {
        super.executeCommand(COMMAND_NATURAL_5);

        assertEquals(1, taskData.getEventMap().size());
        int taskId = taskData.getEventMap().keySet().iterator().next();
        Event event = taskData.getEventMap().get(taskId);

        String actualTaskName = event.getTaskName();
        String actualTaskLocation = event.getTaskLocation();
        String actualTaskDescription = event.getTaskDescription();
        Calendar actualTaskDate = event.getTaskDate();
        int actualTaskDuration = event.getTaskDuration();
        String actualPriority = event.getTaskPriority().toString().toLowerCase();

        assertEquals("Dinner", actualTaskName);
        assertEquals("Ruby Tuesday", actualTaskLocation);
        assertEquals(null, actualTaskDescription);
        assertEquals(Calendar.TUESDAY, actualTaskDate.get(Calendar.DAY_OF_WEEK));
        assertEquals(6, actualTaskDate.get(Calendar.HOUR));
        assertEquals(Calendar.PM, actualTaskDate.get(Calendar.AM_PM));
        assertEquals(0, actualTaskDuration);
        assertEquals("medium", actualPriority);
    }
}