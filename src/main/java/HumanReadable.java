import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class HumanReadable {

    //@author A0109239A
    public static List<String[]> getDetailsAllEvents(TaskData taskData) { 
        try {   
            List<String[]> allEventsAllDetails = extractAllEvents(taskData);
            return allEventsAllDetails;
        } catch (Exception e) {
            return null;
        }
    }
    
    public static boolean setDetailsAllEvents(List<String[]> allEvents) {
        TaskData taskData = new TaskData();
        Map<Integer, Event> eventMap = taskData.getEventMap();
        try{
            eventMap = addEventsFromList(allEvents, eventMap);
            taskData.setEventMap(eventMap);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    //Iterates through the map of events, and finds all the values. Returns a list of those values.
    private static List<String[]> extractAllEvents(TaskData taskData) {
        List<String[]> allEventsDetails = new ArrayList<String[]>();
        String[] tempEventDetails;
        for (Event event : taskData.getEventMap().values()) {
            tempEventDetails = event.getEventDetails();
            allEventsDetails.add(tempEventDetails);
        }
        return allEventsDetails;
    }

    //Accepts a list and a map. Adds the list's items to the map, as suitable. Returns the new map.
    private static Map<Integer, Event> addEventsFromList(List<String[]> allEvents, Map<Integer, Event> eventMap) throws Exception {
        Event tempEvent = new Event();
        int tempEventID;
        try {
            for (String[] entry : allEvents) {
                tempEvent = Event.setEventDetails(entry);
                tempEventID = tempEvent.getTaskID();
                eventMap.put(tempEventID, tempEvent);
            }
            return eventMap;
        } catch (Exception e) {
            throw e;
        }
    }
}
