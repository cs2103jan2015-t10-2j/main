import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Test;

public class AddCommandHandlerTest extends StringBasedTest {

    private TaskData taskData;

    private static final String commandAdd1 = "add Homework at 4:00 11/3/2015 for 60 mins @ Tembusu College desc \"Work on CS2103 project\" setPrior HIGH";
    private static final String commandAdd2 = "add do homework for ever";
    private static final String commandAdd3 = "add 123";

    //@author A0134704M
    @Override
    public TaskData createTaskData() {
        taskData = new TaskData();
        return taskData;
    }

    //@author UNKNOWN
    @Test
    public void testAddSimpleFloating() {
        super.executeCommand("add foo");

        assertEquals(1, taskData.getEventMap().size());
        int taskId = taskData.getEventMap().keySet().iterator().next();
        Event event = taskData.getEventMap().get(taskId);

        String actualTaskName = event.getTaskName();
        String actualTaskLocation = event.getTaskLocation();
        String actualTaskDescription = event.getTaskDescription();
        int actualTaskDuration = event.getTaskDuration();
        String actualPriority = event.getTaskPriority().toString().toLowerCase();

        assertEquals("foo", actualTaskName);
        assertEquals(null, actualTaskLocation);
        assertEquals(null, actualTaskDescription);
        assertEquals(0, actualTaskDuration);
        assertEquals("medium", actualPriority);
    }

    //@author UNKNOWN
    @Test
    public void testAddTodayTomorrow() {
        super.executeCommand("add foo today");
        assertEquals(1, taskData.getEventMap().size());
        // it's kinda complicated to test...
        super.executeCommand("add foo today");
        assertEquals(2, taskData.getEventMap().size());
    }

    //@author UNKNOWN
    @Test
    public void testAddDateTime() {
        super.executeCommand("add foo 5pm 11/11/2015");

        assertEquals(1, taskData.getEventMap().size());
        int taskId = taskData.getEventMap().keySet().iterator().next();
        Event event = taskData.getEventMap().get(taskId);

        String actualTaskName = event.getTaskName();
        Calendar actualTaskDate = event.getTaskDate();

        assertEquals("foo", actualTaskName);
        assertEquals(17, actualTaskDate.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, actualTaskDate.get(Calendar.MINUTE));
        assertEquals(Calendar.PM, actualTaskDate.get(Calendar.AM_PM));
        assertEquals(11, actualTaskDate.get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.NOVEMBER, actualTaskDate.get(Calendar.MONTH));
        assertEquals(2015, actualTaskDate.get(Calendar.YEAR));
    }

    //@author UNKNOWN
    @Test
    public void testAddTomorrowWithDuration() {
        super.executeCommand("add foo tomorrow for 60 mins");
        assertEquals(1, taskData.getEventMap().size());
        int taskId = taskData.getEventMap().keySet().iterator().next();
        Event event = taskData.getEventMap().get(taskId);
        String actualTaskName = event.getTaskName();
        int actualTaskDuration = event.getTaskDuration();
        assertEquals("foo", actualTaskName);
        assertEquals(60, actualTaskDuration);
    }

    //@author UNKNOWN
    @Test
    public void testAddTomorrowWithFancyDuration() {
        super.executeCommand("add foo tomorrow for 1.5 hours");
        assertEquals(1, taskData.getEventMap().size());
        int taskId = taskData.getEventMap().keySet().iterator().next();
        Event event = taskData.getEventMap().get(taskId);
        String actualTaskName = event.getTaskName();
        int actualTaskDuration = event.getTaskDuration();
        assertEquals("foo", actualTaskName);
        assertEquals(90, actualTaskDuration);
    }

    //@author UNKNOWN
    @Test
    public void testAddWithLocation() {
        super.executeCommand("add foo @ rc4");
        assertEquals(1, taskData.getEventMap().size());
        int taskId = taskData.getEventMap().keySet().iterator().next();
        Event event = taskData.getEventMap().get(taskId);
        String actualTaskName = event.getTaskName();
        String actualTaskLocation = event.getTaskLocation();
        assertEquals("foo", actualTaskName);
        assertEquals("rc4", actualTaskLocation);
    }

    //@author UNKNOWN
    @Test
    public void testAddWithDayREMEMBER_TO_CHANGE() {
        super.executeCommand("add foo on monday");
        assertEquals(1, taskData.getEventMap().size());
        int taskId = taskData.getEventMap().keySet().iterator().next();
        Event event = taskData.getEventMap().get(taskId);
        String actualTaskName = event.getTaskName();
        Calendar actualTaskDate = event.getTaskDate();
        assertEquals("foo", actualTaskName);
        assertEquals(20, actualTaskDate.get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.APRIL, actualTaskDate.get(Calendar.MONTH));
        assertEquals(2015, actualTaskDate.get(Calendar.YEAR));
    }

    //@author UNKNOWN
    @Test
    public void testAddWithDayLocationPriorityREMEMBER_TO_CHANGE() {
        super.executeCommand("add foo on monday @ RC4 setPrior HIGH");
        assertEquals(1, taskData.getEventMap().size());
        int taskId = taskData.getEventMap().keySet().iterator().next();
        Event event = taskData.getEventMap().get(taskId);
        String actualTaskName = event.getTaskName();
        Calendar actualTaskDate = event.getTaskDate();
        String actualTaskLocation = event.getTaskLocation();
        String actualPriority = event.getTaskPriority().toString().toLowerCase();

        assertEquals("foo", actualTaskName);
        assertEquals(20, actualTaskDate.get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.APRIL, actualTaskDate.get(Calendar.MONTH));
        assertEquals(2015, actualTaskDate.get(Calendar.YEAR));
        assertEquals("high", actualPriority);
        assertEquals("RC4", actualTaskLocation);
    }

    //@author UNKNOWN
    @Test
    public void testAddWithDayLocationTimeDurationMessedUpOrderREMEMBER_TO_CHANGE() {
        super.executeCommand("add foo on monday for 60 mins @ RC4 5pm");
        assertEquals(1, taskData.getEventMap().size());
        int taskId = taskData.getEventMap().keySet().iterator().next();
        Event event = taskData.getEventMap().get(taskId);
        String actualTaskName = event.getTaskName();
        Calendar actualTaskDate = event.getTaskDate();
        String actualTaskLocation = event.getTaskLocation();
        String actualPriority = event.getTaskPriority().toString().toLowerCase();
        int actualTaskDuration = event.getTaskDuration();

        assertEquals(60, actualTaskDuration);
        assertEquals("foo", actualTaskName);
        assertEquals(20, actualTaskDate.get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.APRIL, actualTaskDate.get(Calendar.MONTH));
        assertEquals(2015, actualTaskDate.get(Calendar.YEAR));
        assertEquals("medium", actualPriority);
        assertEquals("RC4", actualTaskLocation);
    }

    //@author UNKNOWN
    @Test
    public void testAddWithAllFieldsMessedUpOrderREMEMBER_TO_CHANGE() {
        super.executeCommand("add foo on monday desc \"work\" 5pm @ RC4 setPrior HIGH for 1.5 hours");
        assertEquals(1, taskData.getEventMap().size());
        int taskId = taskData.getEventMap().keySet().iterator().next();
        Event event = taskData.getEventMap().get(taskId);

        String actualTaskName = event.getTaskName();
        String actualTaskLocation = event.getTaskLocation();
        String actualTaskDescription = event.getTaskDescription();
        Calendar actualTaskDate = event.getTaskDate();
        int actualTaskDuration = event.getTaskDuration();
        String actualPriority = event.getTaskPriority().toString().toLowerCase();

        assertEquals("foo", actualTaskName);
        assertEquals("work", actualTaskDescription);
        assertEquals(17, actualTaskDate.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, actualTaskDate.get(Calendar.MINUTE));
        assertEquals(Calendar.PM, actualTaskDate.get(Calendar.AM_PM));
        assertEquals(90, actualTaskDuration);
        assertEquals("high", actualPriority);
        assertEquals(20, actualTaskDate.get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.APRIL, actualTaskDate.get(Calendar.MONTH));
        assertEquals(2015, actualTaskDate.get(Calendar.YEAR));
        assertEquals("high", actualPriority);
        assertEquals("RC4", actualTaskLocation);
    }

    //@author A0134704M
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

    //@author A0134704M
    @Test
    public void testExecuteCommand2() {
        super.executeCommand(commandAdd2);

        assertEquals(1, taskData.getEventMap().size());
        int taskId = taskData.getEventMap().keySet().iterator().next();

        Event event = taskData.getEventMap().get(taskId);

        String actualTaskName = event.getTaskName();
        assertEquals("do homework for ever", actualTaskName);
    }

    //@author A0134704M
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
