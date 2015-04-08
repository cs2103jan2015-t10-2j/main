import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Test;

public class AlterCommandHandlerTest extends StringBasedTest {

    private TaskData taskData;

    private static final String validCommandAddTimed = "add Homework at 4:00 20/4/2015 for 60 mins @ Tembusu College desc \"Work on CS2103 project\" setPrior HIGH";
    private static final String validCommandAddFloating = "add Lunch with Mabel @ UTown";

    private static final String validCommandDisplay = "display month";
    private static final String commandExitDisplayMode = "5";
    private static final String commandDisplayFloating = "2";

    @Override
    public TaskData createTaskData() {
        taskData = new TaskData();
        return taskData;
    }

    @Test
    public void testAlterChangeTime() {

        // Add an event successfully
        super.executeCommand(validCommandAddTimed);
        int taskId = taskData.getEventMap().keySet().iterator().next();
        Event event = taskData.getEventMap().get(taskId);
        testTimedTaskBefore(event);

        // Display the event list successfully, and alter the event.
        super.executeCommand(validCommandDisplay);
        super.executeCommand(commandExitDisplayMode);
        super.executeCommand("alter 1 as time 7:00 12/12/2016");
        
        String actualTaskName = event.getTaskName();
        String actualTaskLocation = event.getTaskLocation();
        String actualTaskDescription = event.getTaskDescription();
        Calendar actualTaskDate = event.getTaskDate();
        int actualTaskDuration = event.getTaskDuration();
        String actualPriority = event.getTaskPriority().toString().toLowerCase();

        assertEquals("Homework", actualTaskName);
        assertEquals("Tembusu College", actualTaskLocation);
        assertEquals("Work on CS2103 project", actualTaskDescription);
        assertEquals(7, actualTaskDate.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, actualTaskDate.get(Calendar.MINUTE));
        assertEquals(Calendar.AM, actualTaskDate.get(Calendar.AM_PM));
        assertEquals(12, actualTaskDate.get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.DECEMBER, actualTaskDate.get(Calendar.MONTH));
        assertEquals(2016, actualTaskDate.get(Calendar.YEAR));
        assertEquals(60, actualTaskDuration);
        assertEquals("high", actualPriority);
        
    }
    
    @Test
    public void testAlterChangeDuration() {

        // Add an event successfully
        super.executeCommand(validCommandAddTimed);
        int taskId = taskData.getEventMap().keySet().iterator().next();
        Event event = taskData.getEventMap().get(taskId);
        testTimedTaskBefore(event);

        // Display the event list successfully, and alter the event.
        super.executeCommand(validCommandDisplay);
        super.executeCommand(commandExitDisplayMode);
        super.executeCommand("alter 1 as len 4 hrs");
        
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
        assertEquals(20, actualTaskDate.get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.APRIL, actualTaskDate.get(Calendar.MONTH));
        assertEquals(2015, actualTaskDate.get(Calendar.YEAR));
        assertEquals(240, actualTaskDuration);
        assertEquals("high", actualPriority);
    }

    @Test
    public void testAlterChangeLocation() {

        // Add an event successfully
        super.executeCommand(validCommandAddTimed);
        int taskId = taskData.getEventMap().keySet().iterator().next();
        Event event = taskData.getEventMap().get(taskId);
        testTimedTaskBefore(event);

        // Display the event list successfully, and alter the event.
        super.executeCommand(validCommandDisplay);
        super.executeCommand(commandExitDisplayMode);
        super.executeCommand("alter 1 as @ RC4");
        
        String actualTaskName = event.getTaskName();
        String actualTaskLocation = event.getTaskLocation();
        String actualTaskDescription = event.getTaskDescription();
        Calendar actualTaskDate = event.getTaskDate();
        int actualTaskDuration = event.getTaskDuration();
        String actualPriority = event.getTaskPriority().toString().toLowerCase();

        assertEquals("Homework", actualTaskName);
        assertEquals("RC4", actualTaskLocation);
        assertEquals("Work on CS2103 project", actualTaskDescription);
        assertEquals(4, actualTaskDate.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, actualTaskDate.get(Calendar.MINUTE));
        assertEquals(Calendar.AM, actualTaskDate.get(Calendar.AM_PM));
        assertEquals(20, actualTaskDate.get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.APRIL, actualTaskDate.get(Calendar.MONTH));
        assertEquals(2015, actualTaskDate.get(Calendar.YEAR));
        assertEquals(60, actualTaskDuration);
        assertEquals("high", actualPriority);
    }
    
    @Test
    public void testAlterChangeDescription() {

        // Add an event successfully
        super.executeCommand(validCommandAddTimed);
        int taskId = taskData.getEventMap().keySet().iterator().next();
        Event event = taskData.getEventMap().get(taskId);
        testTimedTaskBefore(event);

        // Display the event list successfully, and alter the event.
        super.executeCommand(validCommandDisplay);
        super.executeCommand(commandExitDisplayMode);
        super.executeCommand("alter 1 as desc \"foo\"");
        
        String actualTaskName = event.getTaskName();
        String actualTaskLocation = event.getTaskLocation();
        String actualTaskDescription = event.getTaskDescription();
        Calendar actualTaskDate = event.getTaskDate();
        int actualTaskDuration = event.getTaskDuration();
        String actualPriority = event.getTaskPriority().toString().toLowerCase();

        assertEquals("Homework", actualTaskName);
        assertEquals("Tembusu College", actualTaskLocation);
        assertEquals("foo", actualTaskDescription);
        assertEquals(4, actualTaskDate.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, actualTaskDate.get(Calendar.MINUTE));
        assertEquals(Calendar.AM, actualTaskDate.get(Calendar.AM_PM));
        assertEquals(20, actualTaskDate.get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.APRIL, actualTaskDate.get(Calendar.MONTH));
        assertEquals(2015, actualTaskDate.get(Calendar.YEAR));
        assertEquals(60, actualTaskDuration);
        assertEquals("high", actualPriority);
    }
    
    @Test
    public void testAlterChangePriority() {

        // Add an event successfully
        super.executeCommand(validCommandAddTimed);
        int taskId = taskData.getEventMap().keySet().iterator().next();
        Event event = taskData.getEventMap().get(taskId);
        testTimedTaskBefore(event);

        // Display the event list successfully, and alter the event.
        super.executeCommand(validCommandDisplay);
        super.executeCommand(commandExitDisplayMode);
        super.executeCommand("alter 1 as setPrior LOW");
        
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
        assertEquals(20, actualTaskDate.get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.APRIL, actualTaskDate.get(Calendar.MONTH));
        assertEquals(2015, actualTaskDate.get(Calendar.YEAR));
        assertEquals(60, actualTaskDuration);
        assertEquals("low", actualPriority);
    }
    
    @Test
    public void testAlterSetSnooze() {

        // Add an event successfully
        super.executeCommand(validCommandAddTimed);
        int taskId = taskData.getEventMap().keySet().iterator().next();
        Event event = taskData.getEventMap().get(taskId);
        testTimedTaskBefore(event);

        // Display the event list successfully, and alter the event.
        super.executeCommand(validCommandDisplay);
        super.executeCommand(commandExitDisplayMode);
        super.executeCommand("alter 1 as snooze 40 days");
        
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
        assertEquals(20, actualTaskDate.get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.APRIL, actualTaskDate.get(Calendar.MONTH));
        assertEquals(2015, actualTaskDate.get(Calendar.YEAR));
        assertEquals(60, actualTaskDuration);
        assertEquals("high", actualPriority);
    }
    
    @Test
    public void testAlterAllFields() {

        // Add an event successfully
        super.executeCommand(validCommandAddTimed);
        int taskId = taskData.getEventMap().keySet().iterator().next();
        Event event = taskData.getEventMap().get(taskId);
        testTimedTaskBefore(event);

        // Display the event list successfully, and alter the event.
        super.executeCommand(validCommandDisplay);
        super.executeCommand(commandExitDisplayMode);
        super.executeCommand("alter 1 as time 6:00 12/12/2016 len 4 hrs @ RC4 desc \"hello\" setPrior LOW");
        
        String actualTaskName = event.getTaskName();
        String actualTaskLocation = event.getTaskLocation();
        String actualTaskDescription = event.getTaskDescription();
        Calendar actualTaskDate = event.getTaskDate();
        int actualTaskDuration = event.getTaskDuration();
        String actualPriority = event.getTaskPriority().toString().toLowerCase();

        assertEquals("Homework", actualTaskName);
        assertEquals("RC4", actualTaskLocation);
        assertEquals("hello", actualTaskDescription);
        assertEquals(6, actualTaskDate.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, actualTaskDate.get(Calendar.MINUTE));
        assertEquals(Calendar.AM, actualTaskDate.get(Calendar.AM_PM));
        assertEquals(12, actualTaskDate.get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.DECEMBER, actualTaskDate.get(Calendar.MONTH));
        assertEquals(2016, actualTaskDate.get(Calendar.YEAR));
        assertEquals(240, actualTaskDuration);
        assertEquals("low", actualPriority);
    }
    
    @Test
    public void testAlterChangeFloating() {

        // Add an event successfully
        super.executeCommand(validCommandAddFloating);
        int taskId = taskData.getEventMap().keySet().iterator().next();
        Event event = taskData.getEventMap().get(taskId);
        
        String actualTaskName = event.getTaskName();
        String actualTaskLocation = event.getTaskLocation();
        assertEquals("Lunch with Mabel", actualTaskName);
        assertEquals("UTown", actualTaskLocation);

        // Display the event list successfully, and alter the event.
        super.executeCommand(validCommandDisplay);
        super.executeCommand(commandDisplayFloating);
        super.executeCommand(commandExitDisplayMode);
        super.executeCommand("alter 1 as time 20:00 25/12/2015 len 2 hrs @ RC4 desc \"I will cook\" setPrior HIGH");
        
        actualTaskName = event.getTaskName();
        actualTaskLocation = event.getTaskLocation();
        String actualTaskDescription = event.getTaskDescription();
        Calendar actualTaskDate = event.getTaskDate();
        int actualTaskDuration = event.getTaskDuration();
        String actualPriority = event.getTaskPriority().toString().toLowerCase();

        assertEquals("Lunch with Mabel", actualTaskName);
        assertEquals("RC4", actualTaskLocation);
        assertEquals("I will cook", actualTaskDescription);
        assertEquals(20, actualTaskDate.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, actualTaskDate.get(Calendar.MINUTE));
        assertEquals(Calendar.PM, actualTaskDate.get(Calendar.AM_PM));
        assertEquals(25, actualTaskDate.get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.DECEMBER, actualTaskDate.get(Calendar.MONTH));
        assertEquals(2015, actualTaskDate.get(Calendar.YEAR));
        assertEquals(120, actualTaskDuration);
        assertEquals("high", actualPriority);
    }
    

    // This method compares the passed event to the original timed event.
    private void testTimedTaskBefore(Event event) {
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
        assertEquals(20, actualTaskDate.get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.APRIL, actualTaskDate.get(Calendar.MONTH));
        assertEquals(2015, actualTaskDate.get(Calendar.YEAR));
        assertEquals(60, actualTaskDuration);
        assertEquals("high", actualPriority);
    }

}
