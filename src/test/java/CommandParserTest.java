import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Locale;

import org.junit.Test;

public class CommandParserTest {

    private static final String ADD_NAME = "add";

    private static final String addCommand1 = "add Go Swim with Cindy for 2h tomorrow 10am";
    private static final String addCommand1ExpectedName = "Go Swim with Cindy";

    private static final String addCommand2a = "add CS3343 Lab 12:00am on monday";
    private static final String addCommand2b = "add CS3343 Lab 12:00pm on monday";
    private static final String addCommand2c = "add CS3343 Lab 11:00am on monday";
    private static final String addCommand2d = "add CS3343 Lab 11:00pm on monday";
    private static final String addCommand2ExpectedName = "CS3343 Lab";

    @Test
    public void testAddCommand1() {
        Event actualEvent = CommandParser.getDetailFromCommand(ADD_NAME, addCommand1);
        Calendar expectedTime = Calendar.getInstance(Locale.US);
        expectedTime.add(Calendar.DAY_OF_MONTH, 1);
        expectedTime.set(Calendar.HOUR_OF_DAY, 10);
        expectedTime.set(Calendar.MINUTE, 0);
        expectedTime.set(Calendar.SECOND, 0);

        assertEvent(actualEvent, addCommand1ExpectedName, null, null, 120,
                TaskPriority.MEDIUM, expectedTime);
    }

    @Test
    public void testAddCommandParsingHour() {
        Event actualEvent = CommandParser.getDetailFromCommand(ADD_NAME, addCommand2a);
        Calendar expectedTime = Calendar.getInstance(Locale.US);
        expectedTime.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        expectedTime.set(Calendar.HOUR_OF_DAY, 0);
        expectedTime.set(Calendar.MINUTE, 0);
        expectedTime.set(Calendar.SECOND, 0);
        if (expectedTime.before(Calendar.getInstance())) {
            expectedTime.add(Calendar.WEEK_OF_MONTH, 1);
        }

        assertEvent(actualEvent, addCommand2ExpectedName, null, null, 0,
                TaskPriority.MEDIUM, expectedTime);

        actualEvent = CommandParser.getDetailFromCommand(ADD_NAME, addCommand2b);
        expectedTime.set(Calendar.HOUR_OF_DAY, 12);
        assertEvent(actualEvent, addCommand2ExpectedName, null, null, 0,
                TaskPriority.MEDIUM, expectedTime);

        actualEvent = CommandParser.getDetailFromCommand(ADD_NAME, addCommand2c);
        expectedTime.set(Calendar.HOUR_OF_DAY, 11);
        assertEvent(actualEvent, addCommand2ExpectedName, null, null, 0,
                TaskPriority.MEDIUM, expectedTime);

        actualEvent = CommandParser.getDetailFromCommand(ADD_NAME, addCommand2d);
        expectedTime.set(Calendar.HOUR_OF_DAY, 23);
        assertEvent(actualEvent, addCommand2ExpectedName, null, null, 0,
                TaskPriority.MEDIUM, expectedTime);
    }

    public void assertEvent(Event actualEvent, String expectedName,
            String expectedDescription, String expectedLocation, int expectedDuration,
            TaskPriority expectedPriority, Calendar expectedTime) {
        assertEquals(expectedDescription, actualEvent.getTaskDescription());
        assertEquals(expectedLocation, actualEvent.getTaskLocation());
        assertEquals(expectedDuration, actualEvent.getTaskDuration());
        assertEquals(expectedPriority, actualEvent.getTaskPriority());

        assertEquals(expectedTime != null, actualEvent.getTaskDate() != null);
        if (expectedTime != null) {
            String expectedTimeString = expectedTime.getTime().toString();
            String actualTimeString = actualEvent.getTaskDate().getTime().toString();
            assertEquals(expectedTimeString, actualTimeString);
        }

        assertEquals(expectedName, actualEvent.getTaskName());
    }
}
