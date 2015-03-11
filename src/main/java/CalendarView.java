import java.util.Scanner;
import java.util.Calendar;
import java.util.ArrayList;


public class CalendarView implements ICommandHandler {
	
		Scanner sc = new Scanner(System.in);

		// TODO Auto-generated method stub
		@Override
	    public boolean isValid(String command) {
	        if (command.isEmpty()) {
	            return false;
	        }
	        return true;
	    }

	    
	    public void viewRequest(String date){
	    	String[] dateParts = date.split(" ");
	    	int day = Integer.parseInt(dateParts[0]);
	    	int month = Integer.parseInt(dateParts[1]);
	    	int year = Integer.parseInt(dateParts[2]);
	    	
	    	Calendar cal = Calendar.getInstance();
	    	cal.set(year, month, day);
	    	
	    	System.out.println("With your date, you can, ");
	    	System.out.println("1. View your tasks in a week based on your day");
	    	System.out.println("2. View your tasks in a month based on your month");
	    	System.out.println("3. View your tasks in a year based on your year");
	    	
	    	int view = sc.nextInt();
	    	
	    	switch(view){
	    	
	    	case 1:
	    		viewWeekTasks(cal);
	    		break;
	    
	    	case 2:
	    		viewMonthTasks(cal);
	    		break;
	    	
	    	case 3:
	    		viewYearTasks(cal);
	    		break;
	    		
	    	
	    	default:
	    		System.out.println("Invalid option");
	    	}
	    }
	    
	    public ArrayList<Integer> viewYearTasks(Calendar cal){
	    	int checkYear = cal.get(Calendar.YEAR);
	    	DataManager dataManager = new DataManager();
	    	ArrayList<Event> events = new ArrayList<Event>();
	    	ArrayList<Integer> taskIDs = new ArrayList<Integer>();
	    	
	    	events = dataManager.loadEventsFromFile("TaskHackerPro.dat");
	    	
	    	for(int i = 0; i < events.size(); i++){
	    		if(events.get(i).getTaskDate().get(Calendar.YEAR) == checkYear){
	    			taskIDs.add(events.get(i).getTaskID());
	    		}
	    	}
	    	
	    	System.out.println("Year view of tasks");
	    	for(int i = 0; i < taskIDs.size(); i++){
	    		System.out.println(taskIDs.get(i));
	    	}
	    	
	    	return taskIDs;
	    }

	    
	    
	    public ArrayList<Integer> viewMonthTasks(Calendar cal){
	    	int checkMonth = cal.get(Calendar.MONTH);
	    	int checkYear = cal.get(Calendar.YEAR);
	    	System.out.println("checkmonth: " + checkMonth);
	    	System.out.println("checkYear: " + checkYear);
	    	DataManager dataManager = new DataManager();
	    	ArrayList<Event> events = new ArrayList<Event>();
	    	ArrayList<Integer> taskIDs = new ArrayList<Integer>();
	    	
	    	events = dataManager.loadEventsFromFile("TaskHackerPro.dat");
	    	
	    	for(int i = 0; i < events.size(); i++){
	    		System.out.println("Hi im for");
	    		if((events.get(i).getTaskDate().get(Calendar.MONTH) == checkMonth) && (events.get(i).getTaskDate().get(Calendar.YEAR) == checkYear)){
	    			taskIDs.add(events.get(i).getTaskID());
	    		System.out.println("Im in if");
	    		}
	    	}
	    	
	    	System.out.println("Month view of tasks");
	    	for(int i = 0; i < taskIDs.size(); i++){
	    		System.out.println(taskIDs.get(i));
	    	}
	    	
	    	return taskIDs;
	    }
	    
	    public ArrayList<Integer> viewWeekTasks(Calendar cal){
	    	
	    	boolean hasNoSimilarID = true;
	    	int checkWeek = cal.get(Calendar.WEEK_OF_YEAR);
	    	DataManager dataManager = new DataManager();
	    	ArrayList<Event> events = new ArrayList<Event>();
	    	ArrayList<Integer> taskIDs = new ArrayList<Integer>();
	    	
	    	events = dataManager.loadEventsFromFile("TaskHackerPro.dat");
	    	
	    	
	    	if(events.get(0).getTaskDate().get(Calendar.WEEK_OF_YEAR) == checkWeek)
	    		taskIDs.add(events.get(0).getTaskID());
	    	
	    	for(int i = 1; i < events.size(); i++){
	    		int taskId = events.get(i).getTaskID();
	    		for(int k = 0; k < taskIDs.size(); k++){
	    			if(taskIDs.get(k) == taskId){
	    				hasNoSimilarID = false;
	    				break;
	    			}
	    		}
	    		if(hasNoSimilarID && events.get(i).getTaskDate().get(Calendar.WEEK_OF_YEAR) == checkWeek){
    				taskIDs.add(events.get(i).getTaskID());
	    		}
	    		hasNoSimilarID = true;
	    	}
	    	
	    	System.out.println("Week view of task IDs");
	    	for(int i = 0; i < taskIDs.size(); i++){
	    		System.out.println(taskIDs.get(i));
	    	}
	    	
	    	return taskIDs;
	    }
	    
	    public void parseCommand(String command) {
	        System.out.println("View your tasks by day, month or year based on your date");
	        
	        if(isCorrectDateFormat(command)){
	        	viewRequest(command);
	        }
	        else{
	        	System.out.println("Incorrect format");
	        	System.out.println("Please enter date again in dd MM YYYY format");
	        	command = sc.nextLine();
	        	while(!(isCorrectDateFormat(command))){
	        		System.out.println("Incorrect format");
		        	System.out.println("Please enter date again in dd MM YYYY format without command");
		        	command = sc.nextLine(); 
	        	}
		        viewRequest(command);
	        }
	        
	    }
	    
	    public boolean isCorrectDateFormat(String date){
	
	    	if (!(date.matches("[0-9]+") || (date.length() == 10 && date.contains(" ")))){
	    		return false;
	    	}
	    	
	    	String[] dateParts = date.split(" ");
	    	
	    	int day = Integer.parseInt(dateParts[0]);
	    	int month = Integer.parseInt(dateParts[1]);
	    	int year = Integer.parseInt(dateParts[2]);
	    	
	    	if(!(hasLogicalDate(day, month, year)))
	    		return false;;
	    	
	    		return true;
	    }
	    
	    public boolean hasLogicalDate(int date, int month, int year){
	    	int[] monthDays = new int[]{31,28,31,30,30,30,31,31,30,31,30,31};
	    	
	    	if(date < 0 || month < 0 || month >12 || year < 0)
	    		return false;
	    	
	    	//Check for leap year
	    	if((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0)){
	    		monthDays[1] = 29;
	    	}
	    	
	    	month = month - 1;
	    	
	    	if(date > monthDays[month])
	    		return false;
	    
	    	return true;
	    }
}
