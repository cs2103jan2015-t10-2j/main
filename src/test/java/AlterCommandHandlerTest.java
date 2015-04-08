import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Test;

public class AlterCommandHandlerTest extends StringBasedTest {

    private TaskData taskData;

    private static final String validCommandAdd = "add Homework at 4:00 11/3/2015 for 60 mins @ Tembusu College desc \"Work on CS2103 project\" setPrior HIGH";
    private static final String validCommandAddFloating = "add Lunch with Mabel @ UTown";

    private static final String validCommandDisplay = "display month";
    private static final String unusedCommandDisplay = "display week";
    private static final String invalidCommandDisplay = "display 11/30/2015";

    private static final String commandDisplayPrevious = "3";
    private static final String commandDisplayExit = "5";

    private static final String commandDisplayZero = "0";
    private static final String commandDisplayFour = "4";

    private static final String validCommandAlter = "alter 1 as time 2:00 15/3/2015 len 2 hrs @ Tembusu College desc \"This homework is very tough!\" setPrior HIGH";
    private static final String unusedCommandAlter = "alter 7 as time 2:00 15/3/2015 len 2 hrs @ Tembusu College desc \"This homework is very tough!\" setPrior HIGH";
    private static final String invalidCommandAlter = "alter 1 as 2:00 15/3/2015 for 2 in Tembusu College desc \"This\"";

    @Override
    public TaskData createTaskData() {
        taskData = new TaskData();
        return taskData;
    }
/*
    @Test
    // Boundary case for all valid inputs
    public void testExecuteCase1() {

        // Add an event successfully
        super.executeCommand(validCommandAdd);

        // There should be one item in the map
        assertEquals(1, taskData.getEventMap().size());

        int taskId = taskData.getEventMap().keySet().iterator().next();
        // Check that the task has be inputed correctly.
        testTaskBefore(taskData.getEventMap().get(taskId));

        // Cannot alter without displaying first
        assertEquals(1, taskData.getEventMap().size());
        super.executeCommand(validCommandAlter);
        assertEquals(1, taskData.getEventMap().size());

        // Display the event list successfully, and alter the event.
        super.executeCommand(validCommandDisplay);
        super.executeCommand(commandViewOptionThree);
        super.executeCommand(validCommandAlter);

        // We check that the event has been changed as needed.
        testTaskAfter(taskData.getEventMap().get(taskId));
    }*/

    @Test
    // Boundary case: valid during creation
    public void testExecuteCase2() {

        // Add a valid event, but then abort.
        super.executeCommand(validCommandAdd);

        // There should be one item in the map
        assertEquals(1, taskData.getEventMap().size());
    }

    @Test
    // BC: valid input for floating task
    public void testExecuteCase3() {

        // Add a valid event, but then abort.
        super.executeCommand(validCommandAddFloating);

        // There should be one item in the map
        assertEquals(1, taskData.getEventMap().size());
    }

    @Test
    // BC: The date is unpopulated
    public void testExecuteCase5() {

        // Add an event successfully
        super.executeCommand(validCommandAdd);

        assertEquals(1, taskData.getEventMap().size());

        int taskId = taskData.getEventMap().keySet().iterator().next();

        // Check that the task has be inputed correctly.
        testTaskBefore(taskData.getEventMap().get(taskId));

        // Display the event list using an UNUSED date and try to alter the
        // event in year view...
        super.executeCommand(unusedCommandDisplay);
        super.executeCommand(commandDisplayPrevious);
        super.executeCommand(commandDisplayExit);
        // No events will come up! We can alter nothing!

        // We check that the event has NOT been changed.
        testTaskBefore(taskData.getEventMap().get(taskId));
    }

    @Test
    // BC: The date is invalid
    public void testExecuteCase6() {

        // Add an event successfully
        super.executeCommand(validCommandAdd);

        assertEquals(1, taskData.getEventMap().size());
        int taskId = taskData.getEventMap().keySet().iterator().next();

        // Check that the task has be inputed correctly.
        testTaskBefore(taskData.getEventMap().get(taskId));

        // Display the event list using an invalid date
        super.executeCommand(invalidCommandDisplay);

        // No events will come up! We can alter nothing.

        // We check that the event has NOT been changed.
        testTaskBefore(taskData.getEventMap().get(taskId));        
    }

    @Test
    // BC to test for view choice for negative value partition
    public void testExecuteCase7() {

        super.executeCommand(validCommandAdd);

        assertEquals(1, taskData.getEventMap().size());

        int taskId = taskData.getEventMap().keySet().iterator().next();

        testTaskBefore(taskData.getEventMap().get(taskId));

        // Display the event list using a valid date, but invoke an invalid
        // "view"
        super.executeCommand(validCommandDisplay);
        super.executeCommand(commandDisplayPrevious);
        super.executeCommand(commandDisplayExit);
        // No events will come up! We can alter nothing

        // We check that the event has NOT been changed.
        testTaskBefore(taskData.getEventMap().get(taskId));
    }

    @Test
    // Boundary case to test for view choice for zero
    public void testExecuteCase8() {

        super.executeCommand(validCommandAdd);

        assertEquals(1, taskData.getEventMap().size());

        int taskId = taskData.getEventMap().keySet().iterator().next();

        testTaskBefore(taskData.getEventMap().get(taskId));

        // Display the event list using a valid date, but invoke an invalid
        // "view"
        super.executeCommand(validCommandDisplay);
        super.executeCommand(commandDisplayZero);
        super.executeCommand(commandDisplayExit);
        // No events will come up! We can alter nothing!

        // We check that the event has NOT been changed.
        testTaskBefore(taskData.getEventMap().get(taskId));
    }

    @Test
    // BC: Valid use.
    public void testExecuteCase9() {

        super.executeCommand(validCommandAdd);

        assertEquals(1, taskData.getEventMap().size());

        int taskId = taskData.getEventMap().keySet().iterator().next();

        testTaskBefore(taskData.getEventMap().get(taskId));

        // Display the event list using an valid date and alter the event using
        // a valid view
        super.executeCommand(validCommandDisplay);
        super.executeCommand(commandDisplayPrevious);
        super.executeCommand(commandDisplayExit);
        super.executeCommand(validCommandAlter);

        // Success. We check that the event has been changed as needed.
        testTaskAfter(taskData.getEventMap().get(taskId));
    }

    @Test
    // Boundary case to test for view choice of >3 partition
    public void testExecuteCase10() {

        super.executeCommand(validCommandAdd);

        assertEquals(1, taskData.getEventMap().size());

        int taskId = taskData.getEventMap().keySet().iterator().next();

        testTaskBefore(taskData.getEventMap().get(taskId));

        // Display the event list using a valid date, but invoke an invalid
        // "view"
        super.executeCommand(validCommandDisplay);
        super.executeCommand(commandDisplayFour);
        super.executeCommand(commandDisplayExit);
        // No events will come up! We can alter nothing.

        // We check that the event has NOT been changed.
        testTaskBefore(taskData.getEventMap().get(taskId));
    }

    @Test
    // Unused event ID
    public void testExecuteCase11() {

        super.executeCommand(validCommandAdd);

        assertEquals(1, taskData.getEventMap().size());

        int taskId = taskData.getEventMap().keySet().iterator().next();

        testTaskBefore(taskData.getEventMap().get(taskId));

        // Display the event list using a valid date and view, but try to alter
        // a nonexistent event...
        super.executeCommand(validCommandDisplay);
        super.executeCommand(commandDisplayPrevious);
        super.executeCommand(commandDisplayExit);
        super.executeCommand(unusedCommandAlter);
        // An error will be thrown. The event doesn't exist.

        // We check that the event has NOT been changed.
        testTaskBefore(taskData.getEventMap().get(taskId));
    }

    @Test
    // Invalid event ID
    public void testExecuteCase12() {

        super.executeCommand(validCommandAdd);

        assertEquals(1, taskData.getEventMap().size());

        int taskId = taskData.getEventMap().keySet().iterator().next();

        testTaskBefore(taskData.getEventMap().get(taskId));

        // Display the event list using a valid date and view, but use a poorly
        // formed alter command
        super.executeCommand(validCommandDisplay);
        super.executeCommand(commandDisplayPrevious);
        super.executeCommand(commandDisplayExit);
        super.executeCommand(invalidCommandAlter);
        // An error will be thrown. The alter command is messed up.

        // We check that the event has NOT been changed.
        testTaskBefore(taskData.getEventMap().get(taskId));
    }

    // This method compares the passed event to the ORIGINAL event.
    private void testTaskBefore(Event event) {
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

    // This method compares the passed event to the ALTERED event.
    private void testTaskAfter(Event event) {
        String actualTaskName = event.getTaskName();
        String actualTaskLocation = event.getTaskLocation();
        String actualTaskDescription = event.getTaskDescription();
        Calendar actualTaskDate = event.getTaskDate();
        int actualTaskDuration = event.getTaskDuration();
        String actualPriority = event.getTaskPriority().toString().toLowerCase();

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
        assertEquals("high", actualPriority);

    }
}
