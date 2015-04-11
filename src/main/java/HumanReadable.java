import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class HumanReadable {
    
    
    //@author A0109239
    public static List<String[]> getDetailsAllEvents(TaskData taskData) { 
        List<String[]> allEventsAllDetails = new ArrayList<String[]>();
        String[] eventDetails;
        try {   
            for (Event event : taskData.getEventMap().values()) {
                eventDetails = event.getEventDetails();
                allEventsAllDetails.add(eventDetails);
            }
            return allEventsAllDetails;
        } catch (Exception e) {
            return null;
        }
    }
    
    //@author A0109239
    public static boolean setDetailsAllEvents(List<String[]> allEvents) {
        TaskData taskData = new TaskData();
        Map<Integer, Event> eventMap = taskData.getEventMap();
        Event tempEvent = new Event();
        int tempEventID;
        try {
            for (String[] entry : allEvents) {
                tempEvent = Event.setEventDetails(entry);
                tempEventID = tempEvent.getTaskID();
                eventMap.put(tempEventID, tempEvent);
                taskData.setEventMap(eventMap);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
