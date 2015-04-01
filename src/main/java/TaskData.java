import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Date;

public class TaskData implements Serializable {

    private static final String messageNoResults = "Your search request returned 0 results";
    private static final String messageEmptyFile = "File is empty!";

    private static final long serialVersionUID = 6897919790578039077L;
    
    private Map<Integer, Event> eventMap;
    private Map<Integer, Integer> displayIDToActualIDMap;
    private Map<Integer, Integer> actualIDToDisplayIDMap;
    SearchCommandHandler search;

    public TaskData() {
        assertObjectNotNull(this);
        this.eventMap = new LinkedHashMap<Integer, Event>();
        this.displayIDToActualIDMap = new LinkedHashMap<Integer, Integer>();
        this.actualIDToDisplayIDMap = new LinkedHashMap<Integer, Integer>();
    }
    
    public ArrayList<Integer> searchEmptySlots(Date parsedDateStart, Date parsedDateEnd) throws Exception{
    	ArrayList<Integer> rangeTaskIds = new ArrayList<Integer>();
    	Calendar cal = Calendar.getInstance();
    	
    	if (this.eventMap.isEmpty()) {
            throw new Exception(messageEmptyFile);
        }
    	
    	for (Integer taskId : this.eventMap.keySet()){
             Event event = this.eventMap.get(taskId);
             cal = event.getTaskDate();
             Date TestDate = cal.getTime();
             if(TestDate.after(parsedDateStart) && TestDate.before(parsedDateEnd)){
            	 rangeTaskIds.add(taskId);
             }
    	 }
    	
    	if (rangeTaskIds.size() == 0) {
            throw new Exception(messageNoResults);
        }
    	
    	sortDatesIncreasingOrder(rangeTaskIds);
    	return rangeTaskIds;
    }
    
    public void sortDatesIncreasingOrder(ArrayList<Integer> rangeTaskIds){
    	int limit;
    	int size = rangeTaskIds.size();
    	Calendar cal1 = Calendar.getInstance();
    	Calendar cal2 = Calendar.getInstance();
    	
    	for (limit = size - 2; limit >= 0; limit--) {
			for (int i = 0; i <= limit; i++) {
				 Event event1 = this.eventMap.get(rangeTaskIds.get(i));
	             cal1 = event1.getTaskDate();
	             Date Date1 = cal1.getTime();
	             
	             Event event2 = this.eventMap.get(rangeTaskIds.get(i+1));
	             cal2 = event2.getTaskDate();
	             Date Date2 = cal2.getTime();
				if (Date1.after(Date2)) {
					rangeTaskIds.add(i, rangeTaskIds.remove(i + 1));
				}
			}
		}	
    }

    public ArrayList<Integer> searchByKeyword(String keyword) throws Exception {
        ArrayList<Integer> matchedTaskIds = new ArrayList<Integer>();
        assertObjectNotNull(this);
        if (this.eventMap.isEmpty()) {
            throw new Exception(messageEmptyFile);
        } else {
            matchedTaskIds = findMatchedIds(keyword, matchedTaskIds);
        }
        return matchedTaskIds;
    }
    

    private ArrayList<Integer> findMatchedIds(String keyword, ArrayList<Integer> matchedTaskIds) {
        for (Integer taskId : this.eventMap.keySet()) {
            Event event = this.eventMap.get(taskId);

            if (this.hasKeyWord(event, keyword)) {
                matchedTaskIds.add(taskId);
            }
        }
        return matchedTaskIds;
    }

    public int getActualId(int displayId) throws NoSuchElementException {
        assertObjectNotNull(this);
        Integer actualId = this.displayIDToActualIDMap.get(displayId);

        if (actualId == null) {
            throw new NoSuchElementException();
        } else {
            return actualId;
        }
    }

    public int getDisplayId(int actualId) throws NoSuchElementException {
        Integer displayId = this.actualIDToDisplayIDMap.get(actualId);

        if (displayId == null) {
            throw new NoSuchElementException();
        } else {
            return displayId;
        }
    }

    public void updateDisplayID(List<Integer> actualIDs) {
        int displayID = 1;
        this.displayIDToActualIDMap.clear();
        this.actualIDToDisplayIDMap.clear();

        for (Integer actualID : actualIDs) {
            this.displayIDToActualIDMap.put(displayID, actualID);
            this.actualIDToDisplayIDMap.put(actualID, displayID);
            displayID++;
        }
    }

    public boolean hasKeyWord(Event event, String keyWord) {

        if (event == null || keyWord == null) {
            return false;
        }

        keyWord = keyWord.toLowerCase();

        if (Integer.toString(event.getTaskID()) != null
                && Integer.toString(event.getTaskID()).contains(keyWord)) {
            return true;
        } else if (event.getTaskName() != null
                && event.getTaskName().toLowerCase().contains(keyWord)) {
            return true;
        } else if (event.getTaskLocation() != null
                && event.getTaskLocation().toLowerCase().contains(keyWord)) {
            return true;
        } else if (event.getTaskDescription() != null
                && event.getTaskDescription().toLowerCase().contains(keyWord)) {
            return true;
        } else if (event.getTaskDate() != null
                && event.getTaskDate().getTime().toString().contains(keyWord)) {
            return true;
        } else if (event.getTaskPriority() != null
                && event.getTaskPriority().toString().toLowerCase().contains(keyWord)) {
            return true;
        }

        return false;
    }

    public Map<Integer, Event> getEventMap() {
        return this.eventMap;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof TaskData)) {
            return false;
        }

        TaskData taskData = (TaskData) obj;
        if (this.eventMap == null || taskData.eventMap == null) {
            return false;
        } else if (!this.eventMap.equals(taskData.eventMap)) {
            return false;
        }

        return true;
    }

    private void assertObjectNotNull(Object o) {
        assert (o != null);
    }
}
