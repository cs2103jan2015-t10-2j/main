import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TaskDataTest {

    private TaskData taskData;

    @Before
    public void setUp() throws Exception {
        taskData = new TaskData();

        Calendar c1 = Calendar.getInstance();
        c1.set(2012, 9, 14);

        Event eventToBeSearched = new Event();
        eventToBeSearched.setTaskID(23456);
        eventToBeSearched.setTaskName("Task Name 1");
        eventToBeSearched.setTaskDate(c1);
        eventToBeSearched.setTaskPriority(TaskPriority.MEDIUM);
        eventToBeSearched.setTaskLocation("Woodlands");
        eventToBeSearched.setTaskDescription("be in Woodlands for dinner");
        taskData.getEventMap().put(eventToBeSearched.getTaskID(),
                eventToBeSearched);

        eventToBeSearched = new Event();
        eventToBeSearched.setTaskID(98765);
        eventToBeSearched.setTaskName("Task Name2");
        eventToBeSearched.setTaskPriority(TaskPriority.HIGH);
        eventToBeSearched.setTaskLocation("IMM");
        eventToBeSearched.setTaskDescription("Shopping in IMM");
        taskData.getEventMap().put(eventToBeSearched.getTaskID(),
                eventToBeSearched);
    }

    @Test
    public void testSearchDescription() {
        List<Integer> searchedEvents = taskData.searchByKeyword("Shopping");

        assertEquals(1, searchedEvents.size());
        assertEquals(98765, (int) searchedEvents.get(0));
    }

    @Test
    public void testSearch() {
        List<Integer> searchedEvents = taskData.searchByKeyword("in");
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

        assertFalse(taskData.hasKeyWord(eventTextNull, "the"));
        assertFalse(taskData.hasKeyWord(eventTest, keyWordTestNull));
        assertFalse(taskData.hasKeyWord(eventTextNull, null));

        assertTrue(taskData.hasKeyWord(taskData.getEventMap().get(23456), "Woodland"));
        assertFalse(taskData.hasKeyWord(taskData.getEventMap().get(23456), "Clementi"));
        assertTrue(taskData.hasKeyWord(taskData.getEventMap().get(98765), "Task Name2"));
        assertFalse(taskData.hasKeyWord(taskData.getEventMap().get(98765), "Name3"));
    }
}
