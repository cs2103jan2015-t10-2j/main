import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Test;

public class CalendarViewCommandHandlerTest extends StringBasedTest {

    private TaskData taskData;

    private static final String validCommandAdd1 = "add Homework at 4:00 20/3/2015 for 60 mins @ Tembusu College desc \"Work on CS2103 project\" setPrior HIGH";
    private static final String validCommandAdd2 = "add Homework at 4:00 14/4/2015 for 60 mins @ Tembusu College desc \"Work on CS2103 project\" setPrior MEDIUM";
    private static final String validCommandAdd3 = "add Homework at 4:00 14/4/2015 for 60 mins @ Tembusu College desc \"Work on CS2103 project\" setPrior MEDIUM";
    private static final String validCommandAdd4 = "add Homework at 4:00 14/4/2015 for 60 mins @ Tembusu College desc \"Work on CS2103 project\" setPrior LOW";
    private static final String validCommandFloat = "add event 1";

    private static final String validCommandDisplayFloat = "display checklist";
    private static final String validCommandDisplayPri = "display priority";
    private static final String validCommandDisplayTuesday = "display tues";
    private static final String validCommandDisplayToday = "display today";
    private static final String validCommandDisplayWeek = "display week";
    private static final String validCommandDisplayMonth = "display month";
    private static final String validCommandDisplayDate = "display 12/12/2016";
    private static final String validCommandDisplayDate2 = "display 14/4/2015";

    private static final String validCommandprev = "2";
    private static final String commandDelete = "delete 1";
    private static final String commandDone = "done 1";

    // @author A0105886W
    @Override
    public TaskData createTaskData() {
        taskData = new TaskData();
        return taskData;
    }

    // @author A0105886W
    @Test
    public void testExecuteCOmmand0() {
        super.executeCommand(validCommandAdd1);
        super.executeCommand(validCommandAdd2);
        super.executeCommand(validCommandAdd3);
        super.executeCommand(validCommandAdd4);
        super.executeCommand(validCommandFloat);

        super.executeCommand(validCommandDisplayToday);
        super.executeCommand(validCommandDisplayWeek);
        super.executeCommand(validCommandDisplayTuesday);
        super.executeCommand(validCommandDisplayDate2);
        super.executeCommand(validCommandDisplayFloat);
        super.executeCommand(validCommandDisplayPri);

        assertEquals(5, taskData.getEventMap().size());
    }

    // @author A0105886W
    @Test
    public void testExecuteCommand1() {

        // add tasks and display them sucessfully
        super.executeCommand(validCommandAdd1);
        super.executeCommand(validCommandDisplayMonth);
        super.executeCommand(validCommandprev);

        int taskId = taskData.getEventMap().keySet().iterator().next();
        Event event = taskData.getEventMap().get(taskId);
        testTimedTaskBefore(event);

        // test after displaying, the user can alter
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

        super.executeCommand(validCommandDisplayDate);

    }

    //@author A0134704M
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
        assertEquals(Calendar.MARCH, actualTaskDate.get(Calendar.MONTH));
        assertEquals(2015, actualTaskDate.get(Calendar.YEAR));
        assertEquals(60, actualTaskDuration);
        assertEquals("high", actualPriority);
    }

    //@author A0134704M
    @Test
    // @author A0105886
    public void testExecuteCommand2() {

        super.executeCommand(validCommandAdd1);
        assertEquals(1, taskData.getEventMap().size());

        // Cannot delete without display first
        assertEquals(1, taskData.getEventMap().size());
        super.executeCommand(commandDelete);
        assertEquals(1, taskData.getEventMap().size());

        // Display and delete
        super.executeCommand(validCommandDisplayMonth);
        super.executeCommand(validCommandprev);
        super.executeCommand(commandDelete);
        assertEquals(0, taskData.getEventMap().size());
    }

    // @author A0105886W
    @Test
    public void testExecuteCommand3() {

        super.executeCommand(validCommandAdd1);
        assertEquals(1, taskData.getEventMap().size());

        // Cannot delete without display first
        assertEquals(1, taskData.getEventMap().size());
        super.executeCommand(commandDelete);
        assertEquals(1, taskData.getEventMap().size());

        // Display and delete
        super.executeCommand(validCommandDisplayMonth);
        super.executeCommand(validCommandprev);
        super.executeCommand(commandDelete);
        assertEquals(0, taskData.getEventMap().size());
    }

    // @author A0105886W
    @Test
    public void testExecuteCommand4() {
        super.executeCommand(validCommandAdd1);
        assertEquals(1, taskData.getEventMap().size());
        Event event = taskData.getEventMap().values().iterator().next();

        // Cannot mark done without display first
        assertFalse(event.isDone());
        super.executeCommand(commandDone);
        assertFalse(event.isDone());

        // Display and mark done
        super.executeCommand(validCommandDisplayMonth);
        super.executeCommand(validCommandprev);
        super.executeCommand(commandDone);
        assertTrue(event.isDone());
    }

}
