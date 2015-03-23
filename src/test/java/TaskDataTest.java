import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class TaskDataTest {

    private TaskData taskData;
    private Calendar taskDate1;
    private Calendar taskDate2;
    private static final String timeFormatString = "h:m d/M/y";
    private static final SimpleDateFormat timeFormat;
    
    static {
        timeFormat = new SimpleDateFormat(timeFormatString);
    }

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
    }

    @Test
    public void testSearchDescription() {
        try {
            List<Integer> searchedEvents = this.taskData.searchByKeyword("Shopping");

            assertEquals(1, searchedEvents.size());
            assertEquals(98765, (int) searchedEvents.get(0));
        }

        catch (Exception e) {
            System.out.println("Invalid input" + e.getMessage());
        }
    }

    @Test
    public void testSearch() throws Exception {

        List<Integer> searchedEvents = this.taskData.searchByKeyword("in");
        assertEquals(2, searchedEvents.size());

        assertTrue(searchedEvents.contains(23456));
        assertTrue(searchedEvents.contains(98765));
    }

    @Test
    public void testHasKeyword() {
        Event eventTextNull = null;
        String keyWordTestNull = null;
        Event eventTest = new Event();

        eventTest.setTaskID(12345);

        assertFalse(this.taskData.hasKeyWord(eventTextNull, "the"));
        assertFalse(this.taskData.hasKeyWord(eventTest, keyWordTestNull));
        assertFalse(this.taskData.hasKeyWord(eventTextNull, null));

        assertTrue(this.taskData.hasKeyWord(this.taskData.getEventMap().get(23456), "Woodland"));
        assertFalse(this.taskData.hasKeyWord(this.taskData.getEventMap().get(23456), "Clementi"));
        assertTrue(this.taskData.hasKeyWord(this.taskData.getEventMap().get(98765), "shopping"));
        assertTrue(this.taskData.hasKeyWord(this.taskData.getEventMap().get(98765), "Task Name2"));
        assertFalse(this.taskData.hasKeyWord(this.taskData.getEventMap().get(98765), "Name3"));
    }
    
    @Test
    public void testDisplaySearchResults(){
    	Event event;
    	Set<Integer> ActualIds = new HashSet<Integer>();
    	SimpleDateFormat format = new SimpleDateFormat("HH:mm dd MMM, yyyy");
    	String keyword = "woodlands";
    	TaskData taskData2 = new TaskData();
    	SearchCommandHandler searchCommand = new SearchCommandHandler(taskData2);
    	SearchCommandHandler searchCommandTest = new SearchCommandHandler(taskData);
    	
    	//testing if file is empty
    	try {
			List<Integer> searchActualIds = taskData2
					.searchByKeyword(keyword);
			searchCommand.displaySearchResults(searchActualIds, keyword);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			assertEquals("File is empty!", e.getMessage());
		}
    	
    	//testing if search results has 0 results
    	keyword = " testing";
    	try {
			List<Integer> searchActualIds = taskData.searchByKeyword(keyword);
			searchCommandTest.displaySearchResults(searchActualIds, keyword);
			
		} 
    	catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println();
			assertEquals("Your search request returned 0 results", e.getMessage());
		}
    	
    	//testing if search results has at least one item
    	keyword = "in";
    	try {
			List<Integer> searchActualIds = taskData.searchByKeyword(keyword);
			searchCommandTest.displaySearchResults(searchActualIds, keyword);
			
			//check event1 and updateDisplayid
			event = taskData.getEventMap().get(searchActualIds.get(0));
			assertEquals(1, taskData.getDisplayId(event.getTaskID()));
			String eventDetails = format.format(event.getTaskDate().getTime()) + " " + "@ " + event.getTaskLocation() + " " + "\""+ event.getTaskDescription() +"\"";
			assertEquals("04:00 11 Mar, 2015 @ Woodlands \"be in Woodlands for dinner\"", eventDetails);
			
			//check event2 and updateDispalyID
			event = taskData.getEventMap().get(searchActualIds.get(1));
			assertEquals(2, taskData.getDisplayId(event.getTaskID()));
			eventDetails = format.format(event.getTaskDate().getTime()) + " " + "@ " + event.getTaskLocation() + " " + "\""+ event.getTaskDescription() +"\"";
			assertEquals("13:00 19 Mar, 2015 @ IMM \"Shopping in IMM\"", eventDetails);
		} 
    	catch (Exception e) {
			System.out.println(e.getMessage());
			assertEquals("Your search request returned 0 results", e.getMessage());
		}
 
    }
}
