import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.NoSuchElementException;

import org.junit.Test;

public class SearchCommandHandlerTest extends StringBasedTest {

    private TaskData taskData;
    private ArrayList<Integer> searchKeywordIds;
    private ArrayList<Integer> searchDateIds;
    private Date parsedDateStart;
    private Date parsedDateEnd;
    private static final SimpleDateFormat formatDayMthYr;
    private static final String simpleDateFormatDayMthYr = "d/M/y";

    private static final String validCommandAdd1 = "add Homework at 4:00 11/3/2015 for 60 mins @ Tembusu College desc \"Work on CS2103 project\"";
    private static final String validCommandAdd2 = "add Dinner at 16:00 11/3/2015 for 60 mins @ Tembusu College desc \"Work on CS2103 project\"";
    private static final String validCommandAdd3 = "add Homework at 13:00 12/3/2015 for 120 mins @ NUS desc \"Work on EE2024 Project\"";
    private static final String validCommandAdd4 = "add Sports at 4:00 20/3/2015 for 120 mins @ NUS desc \"Soccer with mates\"";
    private static final String validCommandAdd5 = "add Homework at 23:00 11/3/2015 for 240 mins @ Home desc \"Work on sleeping project\"";
    private static final String validCommandAdd6 = "add CommunityService at 10:00 13/3/2015 for 120 mins @ Home desc \"Help the needy\"";
    private static final String validCommandAdd7 = "add Shopping at 14:00 13/3/2015 for 180 mins @ IMM desc \"Spend my hard earn money!!\"";
    private static final String validCommandAdd8 = "add Sports at 23:00 13/3/2015 for 90 mins @ Home desc \"Watch Arsenal vs Manchester united\"";
    private static final String validCommandAdd9 = "add Dating at 17:00 12/3/2015 for 140 mins @ CausewayPoint desc \"Watch movie with ?????\"";
    private static final String validCommandAdd10 = "search 9/3/2015 to 15/3/2015";
    private static final String validCommandDisplay = "display 11/3/2015";
    private static final String commandViewOptionThree = "3";
    private static final String HasSearchKeyword = "Homework";
    private static final String NoSearchKeyword = "Sports";

    static {
        formatDayMthYr = new SimpleDateFormat(simpleDateFormatDayMthYr);
    }

    @Override
    public TaskData createTaskData() {
        taskData = new TaskData();
        return taskData;
    }

    @Test
    // Boundary case for all valid inputs
    public void testExecuteCase1() {

        // Add three events successfully
        super.executeCommand(validCommandAdd1);
        super.executeCommand(validCommandAdd2);
        super.executeCommand(validCommandAdd3);

        // There should be one item in the map
        assertEquals(3, taskData.getEventMap().size());

        int i = 0;
        for (Integer taskId : taskData.getEventMap().keySet()) {
            // Check that a task has be inputed correctly.
            testSearchKeyWordTaskBefore(taskData.getEventMap().get(taskId), i);
            ++i;
        }

        // Display the event list successfully
        super.executeCommand(validCommandDisplay);
        super.executeCommand(commandViewOptionThree);

        // We check the search class has successfully found two key words
        this.searchKeywordIds = taskData.searchByKeyword(HasSearchKeyword);
        testSearchKeyWordTaskAfter(searchKeywordIds);
    }

    // BC: The file is empty
    @Test
    public void testExecuteCase4() {

        try {
            taskData.searchByKeyword(HasSearchKeyword);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals("File is empty!", e.getMessage());
        }
    }

    // BC: Search results returned 0
    @Test
    public void testExecuteCase5() {

        // Add three events successfully
        super.executeCommand(validCommandAdd1);
        super.executeCommand(validCommandAdd2);
        super.executeCommand(validCommandAdd3);

        try {
            taskData.searchByKeyword(NoSearchKeyword);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertEquals("Your search request returned 0 results", e.getMessage());
        }
    }

    // This method compares the passed event to the ORIGINAL event.
    private void testSearchKeyWordTaskBefore(Event event, int i) {
        if (i == 0) {

            String actualTaskName = event.getTaskName();
            String actualTaskLocation = event.getTaskLocation();
            String actualTaskDescription = event.getTaskDescription();
            Calendar actualTaskDate = event.getTaskDate();
            int actualTaskDuration = event.getTaskDuration();

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

        }

        else if (i == 1) {

            String actualTaskName = event.getTaskName();
            String actualTaskLocation = event.getTaskLocation();
            String actualTaskDescription = event.getTaskDescription();
            Calendar actualTaskDate = event.getTaskDate();
            int actualTaskDuration = event.getTaskDuration();

            assertEquals("Dinner", actualTaskName);
            assertEquals("Tembusu College", actualTaskLocation);
            assertEquals("Work on CS2103 project", actualTaskDescription);
            assertEquals(16, actualTaskDate.get(Calendar.HOUR_OF_DAY));
            assertEquals(0, actualTaskDate.get(Calendar.MINUTE));
            assertEquals(Calendar.PM, actualTaskDate.get(Calendar.AM_PM));
            assertEquals(11, actualTaskDate.get(Calendar.DAY_OF_MONTH));
            assertEquals(Calendar.MARCH, actualTaskDate.get(Calendar.MONTH));
            assertEquals(2015, actualTaskDate.get(Calendar.YEAR));
            assertEquals(60, actualTaskDuration);
        }

    }

    // Boundary case for 2 task that has keywords
    private void testSearchKeyWordTaskAfter(ArrayList<Integer> searchKeywordIds) {
        assertEquals(2, searchKeywordIds.size());
        assertEquals(HasSearchKeyword, taskData.getEventMap()
                .get(searchKeywordIds.get(0)).getTaskName());
        assertEquals(HasSearchKeyword, taskData.getEventMap()
                .get(searchKeywordIds.get(1)).getTaskName());
    }

    @Test
    // Boundary case for all valid inputs
    public void testExecuteCase6() {

        // Add seven events successfully
        super.executeCommand(validCommandAdd1);
        super.executeCommand(validCommandAdd2);
        super.executeCommand(validCommandAdd3);
        super.executeCommand(validCommandAdd4);
        super.executeCommand(validCommandAdd5);
        super.executeCommand(validCommandAdd6);
        super.executeCommand(validCommandAdd7);
        super.executeCommand(validCommandAdd8);
        super.executeCommand(validCommandAdd9);

        try {
            this.parsedDateStart = formatDayMthYr.parse("9/3/2015");
            this.parsedDateEnd = formatDayMthYr.parse("15/3/2015");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            searchDateIds = taskData.searchEmptySlots(parsedDateStart, parsedDateEnd, null);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }

        // There should be a total 7 items displayed for empty slots instead of
        // 8 because one is out of date range
        assertEquals(8, searchDateIds.size());
        // Display empty slot results
        super.executeCommand(validCommandAdd10);
    }
}
