import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class CalendarViewCommandHandlerTest {

    private TaskData taskData;
    private CalendarViewCommandHandler calendarViewCommandHandler;

    @Before
    public void setUp() throws Exception {
        taskData = new TaskData();
        calendarViewCommandHandler = new CalendarViewCommandHandler(taskData);

        Event[] events = new Event[5];
        Calendar[] dates = new Calendar[5];
        int[] ids = new int[] { 1234, 5678, 9012, 5848, 3955 };

        dates[0] = Calendar.getInstance();
        dates[0].set(2015, 11, 16);
        dates[1] = Calendar.getInstance();
        dates[1].set(2015, 11, 17);
        dates[2] = Calendar.getInstance();
        dates[2].set(2015, 11, 1);
        dates[3] = Calendar.getInstance();
        dates[3].set(2015, 2, 1);
        dates[4] = Calendar.getInstance();
        dates[4].set(2014, 11, 1);

        for (int i = 0; i < events.length; i++) {
            events[i] = new Event();
            events[i].setTaskID(ids[i]);
            events[i].setTaskDate(dates[i]);
            taskData.getEventMap().put(events[i].getTaskID(), events[i]);
        }
        taskData.updateDisplayID();
    }
    
    @Test
    public void testExecuteCommand() {
        assertTrue(calendarViewCommandHandler.parseCommand("display 15/11/2015"));
        assertTrue(calendarViewCommandHandler.executeCommand());
        assertTrue(calendarViewCommandHandler.parseCommand("1"));
        assertTrue(calendarViewCommandHandler.executeCommand());
    }

    @Test
    public void testWeekTasks() {
        Calendar dateViewing = Calendar.getInstance();
        dateViewing.set(2015, 11, 15);
        Set<Integer> taskIDs = calendarViewCommandHandler.getMatchedTaskDisplayIDs(
                dateViewing, CalendarViewCommandHandler.ViewOption.WEEK);

        Integer[] actualIDs = new Integer[] {1234, 5678};
        Set<Integer> displaySet = new HashSet<Integer>();
        for (int actualID : actualIDs) {
            displaySet.add(taskData.getDisplayId(actualID));
        }
        assertEquals(2, displaySet.size());
        
        for (int displayID : displaySet) {
            assertTrue(taskIDs.contains(displayID));
        }
    }

    @Test
    public void testMonthTasks() {
        Calendar dateViewing = Calendar.getInstance();
        dateViewing.set(2015, 11, 16);
        Set<Integer> taskIDs = calendarViewCommandHandler.getMatchedTaskDisplayIDs(
                dateViewing, CalendarViewCommandHandler.ViewOption.MONTH);

        Integer[] actualIDs = new Integer[] {1234, 5678, 9012};
        Set<Integer> displaySet = new HashSet<Integer>();
        for (int actualID : actualIDs) {
            displaySet.add(taskData.getDisplayId(actualID));
        }
        assertEquals(3, displaySet.size());

        for (int displayID : displaySet) {
            assertTrue(taskIDs.contains(displayID));
        }
    }

    @Test
    public void testYearTasks() {
        Calendar dateViewing = Calendar.getInstance();
        dateViewing.set(2015, 11, 16);
        Set<Integer> taskIDs = calendarViewCommandHandler.getMatchedTaskDisplayIDs(
                dateViewing, CalendarViewCommandHandler.ViewOption.YEAR);

        Integer[] actualIDs = new Integer[] {1234, 5678, 9012, 5848};
        Set<Integer> displaySet = new HashSet<Integer>();
        for (int actualID : actualIDs) {
            displaySet.add(taskData.getDisplayId(actualID));
        }
        assertEquals(4, displaySet.size());
        
        for (int displayID : displaySet) {
            assertTrue(taskIDs.contains(displayID));
        }
    }
}
