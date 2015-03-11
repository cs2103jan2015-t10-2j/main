import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;



import java.util.ArrayList;
import java.util.Calendar;


public class CalendarViewTest {

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
    public void TestViewWeekTasks() {
    	
    	
    	Event event1 = new Event();
    	Event event2 = new Event();
    	Event event3 = new Event();
    	Event event4 = new Event();
    	Event event5 = new Event();
    	
    	Calendar c1 = Calendar.getInstance();
    	c1.set(2015, 11, 16);
    	Calendar c2 = Calendar.getInstance();
    	c2.set(2015, 11, 17);
    	Calendar c3 = Calendar.getInstance();
    	c3.set(2015, 11, 1);
    	Calendar c4 = Calendar.getInstance();
    	c4.set(2015, 2, 1);
    	Calendar c5 = Calendar.getInstance();
    	c5.set(2014, 11, 1);
    	
    	
    	
    	event1.setTaskDate(c1);
    	event1.setTaskID(1234);
    	event2.setTaskDate(c2);
    	event2.setTaskID(5678);
    	event3.setTaskDate(c3);
    	event3.setTaskID(9012);
    	
    	event4.setTaskDate(c4);
    	event4.setTaskID(5848);
    	event5.setTaskDate(c5);
    	event5.setTaskID(3955);
    	
    	dataManager.saveEventToFile("TaskHackerPro.dat", event1);
    	dataManager.saveEventToFile("TaskHackerPro.dat", event2);
    	dataManager.saveEventToFile("TaskHackerPro.dat", event3);
    	dataManager.saveEventToFile("TaskHackerPro.dat", event4);
    	dataManager.saveEventToFile("TaskHackerPro.dat", event5);  
    	
    	CalendarView calendarView = new CalendarView();
    	Calendar cal = Calendar.getInstance();
    	cal.set(2015, 11, 15);
    	ArrayList<Integer> taskIDs = new ArrayList<Integer>();
    	
    	taskIDs = calendarView.viewWeekTasks(cal);
    	
    	System.out.println("Week view of taskIDs in test");
    	for(int i = 0; i < taskIDs.size(); i++)
    		System.out.println(taskIDs.get(i));
    	
    	assertEquals("1234", Integer.toString(taskIDs.get(0)));
    	assertEquals("5678", Integer.toString(taskIDs.get(1)));
    	
    }
    
  @Test
  public void TestViewMonthTasks(){
	
	  	CalendarView calendarView = new CalendarView();
    	Calendar cal = Calendar.getInstance();
    	cal.set(2015, 11, 16);
    	ArrayList<Integer> taskIDs = new ArrayList<Integer>();
    	
    	taskIDs = calendarView.viewMonthTasks(cal);
    	
    	System.out.println("Month view of taskIDs in test");
    	for(int i = 0; i < taskIDs.size(); i++)
    		System.out.println(taskIDs.get(i));
    	
    	assertEquals("1234", Integer.toString(taskIDs.get(0)));
    	assertEquals("5678", Integer.toString(taskIDs.get(1)));
    	assertEquals("9012", Integer.toString(taskIDs.get(2)));
    }
  
  @Test
  public void TestViewYearTasks(){
		
	  	CalendarView calendarView = new CalendarView();
	  	Calendar cal = Calendar.getInstance();
	  	cal.set(2015, 11, 16);
	  	ArrayList<Integer> taskIDs = new ArrayList<Integer>();
  	
	  	System.out.println("Its in testyear");
	  	taskIDs = calendarView.viewYearTasks(cal);
  	
	  	System.out.println("Year view of taskIDs in test");
	  	for(int i = 0; i < taskIDs.size(); i++)
	  	System.out.println(taskIDs.get(i));
  	
	  	
	  	assertEquals("1234", Integer.toString(taskIDs.get(0)));
	  	assertEquals("5678", Integer.toString(taskIDs.get(1)));
	  	assertEquals("9012", Integer.toString(taskIDs.get(2)));
	  	assertEquals("5848", Integer.toString(taskIDs.get(3)));

  }
    
    
    
    
    
}