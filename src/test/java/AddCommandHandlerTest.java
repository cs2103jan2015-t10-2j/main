import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

public class AddCommandHandlerTest {

    private AddCommandHandler addCommandHandler;
    
    
    @Before
    public void setUp() throws Exception {
        addCommandHandler = new AddCommandHandler();
    }

    @Test
    public void testParseCommand() {
        String commandAdd = "add at 4:00 11/3/2015 @ Tembusu College desc \"Work on CS2103 project\"";
        
        assertTrue(addCommandHandler.isValid(commandAdd));

        addCommandHandler.parseCommand(commandAdd);
        Event event = addCommandHandler.getEvent();
        String actualTaskLocation = event.getTaskLocation();
        String actualTaskDescription = event.getTaskDescription();
        Calendar actualTaskDate = event.getTaskDate();
        
        assertEquals("Tembusu College", actualTaskLocation);
        assertEquals("Work on CS2103 project", actualTaskDescription);        
        assertEquals(4, actualTaskDate.get(Calendar.HOUR_OF_DAY)); 
        assertEquals(0, actualTaskDate.get(Calendar.MINUTE)); 
        assertEquals(Calendar.AM, actualTaskDate.get(Calendar.AM_PM)); 
        assertEquals(11, actualTaskDate.get(Calendar.DAY_OF_MONTH)); 
        assertEquals(Calendar.MARCH, actualTaskDate.get(Calendar.MONTH)); 
        assertEquals(2015, actualTaskDate.get(Calendar.YEAR)); 
    }

}
