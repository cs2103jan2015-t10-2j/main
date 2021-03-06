import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

public class TaskDataTest {

    private TaskData taskData;
    private Calendar taskDate1;
    private Calendar taskDate2;
    private static final String timeFormatString = "h:m d/M/y";
    private static final SimpleDateFormat timeFormat;

    //@author A0134704M
    static {
        timeFormat = new SimpleDateFormat(timeFormatString);
    }

    //@author A0134704M
    @Before
    public void setUp() throws Exception {
        taskDate1 = Calendar.getInstance();
        this.taskData = new TaskData();
        String time = "4:00 11/3/2015";
        Date parsedDate = timeFormat.parse(time);
        taskDate1.setTime(parsedDate);

        Event eventToBeSearched = new Event();
        eventToBeSearched.setTaskID(23456);
        eventToBeSearched.setTaskName("Task Name 1");
        eventToBeSearched.setTaskPriority(TaskPriority.MEDIUM);
        eventToBeSearched.setTaskLocation("Woodlands");
        eventToBeSearched.setTaskDescription("be in Woodlands for dinner");
        eventToBeSearched.setTaskDate(taskDate1);
        this.taskData.getEventMap().put(eventToBeSearched.getTaskID(), eventToBeSearched);

        taskDate2 = Calendar.getInstance();
        time = "13:00 19/3/2015";
        parsedDate = timeFormat.parse(time);
        taskDate2.setTime(parsedDate);

        eventToBeSearched = new Event();
        eventToBeSearched.setTaskID(98765);
        eventToBeSearched.setTaskName("Task Name2");
        eventToBeSearched.setTaskPriority(TaskPriority.HIGH);
        eventToBeSearched.setTaskLocation("IMM");
        eventToBeSearched.setTaskDescription("Shopping in IMM");
        eventToBeSearched.setTaskDate(taskDate2);
        this.taskData.getEventMap().put(eventToBeSearched.getTaskID(), eventToBeSearched);
        
        Calendar tempTaskDate = Calendar.getInstance();
        tempTaskDate.add(Calendar.MONTH, -1);
        eventToBeSearched = new Event();
        eventToBeSearched.setTaskID(780970);
        eventToBeSearched.setTaskName("Task Name3");
        eventToBeSearched.setTaskPriority(TaskPriority.LOW);
        eventToBeSearched.setTaskLocation("Yale NUS");
        eventToBeSearched.setTaskDescription("Do CS2103 Project");
        eventToBeSearched.setTaskDueDate(tempTaskDate);
        this.taskData.getEventMap().put(eventToBeSearched.getTaskID(), eventToBeSearched);
        
        tempTaskDate = Calendar.getInstance();
        tempTaskDate.add(Calendar.YEAR, 1);
        eventToBeSearched = new Event();
        eventToBeSearched.setTaskID(345435);
        eventToBeSearched.setTaskName("Task Name4");
        eventToBeSearched.setTaskPriority(TaskPriority.HIGH);
        eventToBeSearched.setTaskLocation("Swimming Pool");
        eventToBeSearched.setTaskDescription("Go swimming");
        eventToBeSearched.setTaskDueDate(tempTaskDate);
        this.taskData.getEventMap().put(eventToBeSearched.getTaskID(), eventToBeSearched);
    }

    //@author A0134704M
    @Test
    public void testSearch() throws Exception {

        List<Integer> searchedEvents = this.taskData.searchByKeyword("in");
        assertEquals(3, searchedEvents.size());

        assertTrue(searchedEvents.contains(23456));
        assertTrue(searchedEvents.contains(98765));
        assertTrue(searchedEvents.contains(345435));
    }

    //@author A0134704M
    @Test
    public void testHasKeyword() {
        Event eventTextNull = null;
        String keyWordTestNull = null;
        Event eventTest = new Event();

        eventTest.setTaskID(12345);

        assertFalse(this.taskData.hasKeyWord(eventTextNull, "the"));
        assertFalse(this.taskData.hasKeyWord(eventTest, keyWordTestNull));
        assertFalse(this.taskData.hasKeyWord(eventTextNull, null));

        assertTrue(this.taskData.hasKeyWord(this.taskData.getEventMap().get(23456),
                "Woodland"));
        assertFalse(this.taskData.hasKeyWord(this.taskData.getEventMap().get(23456),
                "Clementi"));
        assertTrue(this.taskData.hasKeyWord(this.taskData.getEventMap().get(98765),
                "shopping"));
        assertTrue(this.taskData.hasKeyWord(this.taskData.getEventMap().get(98765),
                "Task Name2"));
        assertFalse(this.taskData.hasKeyWord(this.taskData.getEventMap().get(98765),
                "Name3"));
    }

    //@author A0134704M
    @Test
    public void testDisplaySearchResults() {
        Event event;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm dd MMM, yyyy");
        String keyword = "woodlands";
        TaskData taskData2 = new TaskData();
        SearchCommandHandler searchCommand = new SearchCommandHandler(taskData2);
        SearchCommandHandler searchCommandTest = new SearchCommandHandler(taskData);

        // testing if file is empty
        try {
            List<Integer> searchActualIds = taskData2.searchByKeyword(keyword);
            searchCommand.displaySearchResults(searchActualIds, keyword);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals("File is empty!", e.getMessage());
        }

        // testing if search has 0 results
        keyword = " testing";
        try {
            List<Integer> searchActualIds = taskData.searchByKeyword(keyword);
            searchCommandTest.displaySearchResults(searchActualIds, keyword);

        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
            System.out.println();
            assertEquals("Your search request returned 0 results", e.getMessage());
        }

        // testing if search results has at least one item
        keyword = "in";
        try {
            List<Integer> searchActualIds = taskData.searchByKeyword(keyword);
            searchCommandTest.displaySearchResults(searchActualIds, keyword);

            // check event1 and updateDisplayid
            event = taskData.getEventMap().get(searchActualIds.get(0));
            assertEquals(1, taskData.getDisplayId(event.getTaskID()));
            String eventDetails = format.format(event.getTaskDate().getTime()) + " "
                    + "@ " + event.getTaskLocation() + " " + "\""
                    + event.getTaskDescription() + "\"";
            assertEquals("04:00 11 Mar, 2015 @ Woodlands \"be in Woodlands for dinner\"",
                    eventDetails);

            // check event2 and updateDispalyID
            event = taskData.getEventMap().get(searchActualIds.get(1));
            assertEquals(2, taskData.getDisplayId(event.getTaskID()));
            eventDetails = format.format(event.getTaskDate().getTime()) + " " + "@ "
                    + event.getTaskLocation() + " " + "\"" + event.getTaskDescription()
                    + "\"";
            assertEquals("13:00 19 Mar, 2015 @ IMM \"Shopping in IMM\"", eventDetails);
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
            assertEquals("Your search request returned 0 results", e.getMessage());
        }
    }
    
    //@author A0134704M
    @Test
    public void testOverdueTask() {
        List<Event> selectedEvents = this.taskData.getOverdueTask();
        assertEquals(1, selectedEvents.size());

        Calendar dateBefore = selectedEvents.get(0).getTaskDueDate();
        assertTrue(Calendar.getInstance().after(dateBefore));
    }
}
