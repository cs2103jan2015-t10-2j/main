import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Calendar;


public class DataManagerTest {

    private final String testFilePath = "testDatManagerFile.dat";
    private DataManager dataManager;
    
    @Before
    public void setUp() {
        dataManager = new DataManager();
    }
    
    @After
    public void tearDown() {
       File file = new File(testFilePath);
       file.delete();
    }
    
    @Test
    public void testSaveAndLoad() {
    	int i = 0;
    	ArrayList<Event> events = new ArrayList<Event>();
    	Calendar c1 = Calendar.getInstance();
    	c1.set(2012, 9, 14);
        Event event1 = new Event();
        event1.setTaskID(23456);
        event1.setTaskName("Task Name 1");
        event1.setTaskDate(c1);
        event1.setTaskPriority(TaskPriority.MEDIUM);
        event1.setTaskLocation("Woodlands");
        event1.setTaskDescription("be in Woodlands for dinner");
        dataManager.saveEventToFile(testFilePath, event1);
        events = dataManager.loadEventsFromFile(testFilePath);
        
        if(i < events.size()){
        assertEquals("23456", Integer.toString(events.get(0).getTaskID()));
        assertEquals("Task Name 1", events.get(0).getTaskName());
        assertEquals("MEDIUM", events.get(0).getTaskPriority().toString());
        assertEquals("be in Woodlands for dinner", events.get(0).getTaskDescription());
        assertEquals("Woodlands", events.get(0).getTaskLocation());
        }
        
        i++;
        
        Event event2 = new Event();
        event2.setTaskID(98765);
        event2.setTaskName("Task Name2");
        event2.setTaskPriority(TaskPriority.HIGH);
        event2.setTaskLocation("IMM");
        event2.setTaskDescription("Shopping in IMM");
        dataManager.saveEventToFile(testFilePath, event2);
        events = dataManager.loadEventsFromFile(testFilePath);
        
        if(i < events.size()){
        assertEquals("98765", Integer.toString(events.get(1).getTaskID()));
        assertEquals("Task Name2", events.get(1).getTaskName());
        assertEquals("HIGH", events.get(1).getTaskPriority().toString());
        assertEquals("Shopping in IMM", events.get(1).getTaskDescription());
        assertEquals("IMM", events.get(1).getTaskLocation());
        }
    }
    
    @Test
    public void testSearch(){
    	int i = 0;
    	//ArrayList<Event> events = new ArrayList<Event>();
    	ArrayList<Integer> searchedEvents = new ArrayList<Integer>(); 
    	Calendar c1 = Calendar.getInstance();
    	c1.set(2012, 9, 14);
        Event event1 = new Event();
        event1.setTaskID(23456);
        event1.setTaskName("Task Name 1");
        event1.setTaskDate(c1);
        event1.setTaskPriority(TaskPriority.MEDIUM);
        event1.setTaskLocation("Woodlands");
        event1.setTaskDescription("be in Woodlands for dinner");
        dataManager.saveEventToFile(testFilePath, event1);
        
        Event event2 = new Event();
        event2.setTaskID(98765);
        event2.setTaskName("Task Name2");
        event2.setTaskPriority(TaskPriority.HIGH);
        event2.setTaskLocation("IMM");
        event2.setTaskDescription("Shopping in IMM");
        dataManager.saveEventToFile(testFilePath, event2);
        //events = dataManager.loadEventsFromFile(testFilePath);
        
        searchedEvents = dataManager.searchKeyWords(testFilePath,"Shopping");
        if(i < searchedEvents.size()){
        assertEquals("98765", Integer.toString(searchedEvents.get(0)));
        }
        
       
        searchedEvents = dataManager.searchKeyWords(testFilePath, "in");
        
        for(int k = 0; k < searchedEvents.size(); k++)
        	System.out.println("searched results: " + searchedEvents.get(k));
        if(i < searchedEvents.size()){
        	assertEquals("23456", Integer.toString(searchedEvents.get(0)));	
        }
        
        if(i < searchedEvents.size()){
        	assertEquals("98765", Integer.toString(searchedEvents.get(1)));
        }
    }
    
    @Test
    public void TestgetEvent(){
    	Event event; 
    	
        Event event1 = new Event();
        event1.setTaskID(23456);
        event1.setTaskName("Task name 1");
        dataManager.saveEventToFile(testFilePath, event1);
        
        Event event2 = new Event();
        event2.setTaskID(98765);
        event2.setTaskName("Task name 2");
        dataManager.saveEventToFile(testFilePath, event2);
        
        Event event3 = new Event();
        event3.setTaskID(56789);
        event3.setTaskName("Task name 3");
        dataManager.saveEventToFile(testFilePath, event3);
          
        event = dataManager.getEventFromFile(testFilePath, 98765);
        
        assertEquals("Task name 2", event.getTaskName()); 
    }
    
    @Test
    public void TestHasKeyWord(){
    	Event eventTextNull = null;
    	String keyWordTestNull = null;
    	Event eventTest = new Event();
    	eventTest.setTaskID(12345);
    	
    	assertFalse(dataManager.hasKeyWord(eventTextNull, "the"));
    	
    	assertFalse(dataManager.hasKeyWord(eventTest, keyWordTestNull));
    	
    	assertFalse(dataManager.hasKeyWord(eventTextNull, null));
    	
    	
    	Calendar c1 = Calendar.getInstance();
    	c1.set(2012, 9, 14);
        Event event1 = new Event();
        event1.setTaskID(23456);
        event1.setTaskName("Task Name 1");
        event1.setTaskDate(c1);
        event1.setTaskPriority(TaskPriority.MEDIUM);
        event1.setTaskLocation("Woodlands");
        event1.setTaskDescription("be in Woodlands for dinner");
        
        Event event2 = new Event();
        event2.setTaskID(98765);
        event2.setTaskName("Task Name2");
        event2.setTaskPriority(TaskPriority.HIGH);
        event2.setTaskLocation("IMM");
        event2.setTaskDescription("Shopping in IMM");
        
        assertTrue(dataManager.hasKeyWord(event1, "Woodland"));
        assertFalse(dataManager.hasKeyWord(event1, "Clementi"));
        assertTrue(dataManager.hasKeyWord(event2, "Task Name2"));
        assertFalse(dataManager.hasKeyWord(event2, "Name3"));
    	
    }
}