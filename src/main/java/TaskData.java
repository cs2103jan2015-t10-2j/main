import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class TaskData implements Serializable {

    private static final long serialVersionUID = 6897919790578039077L;

    private Map<Integer, Event> eventMap;
    private Map<Integer, Integer> displayIDToActualIDMap;
    private Map<Integer, Integer> actualIDToDisplayIDMap;
    SearchCommandHandler search;

    public TaskData() {
        assertObjectNotNull(this);
        this.eventMap = new HashMap<Integer, Event>();
        this.displayIDToActualIDMap = new HashMap<Integer, Integer>();
        this.actualIDToDisplayIDMap = new HashMap<Integer, Integer>();
    }

    public ArrayList<Integer> searchByKeyword(String keyword) throws Exception {
        ArrayList<Integer> matchedTaskIds = new ArrayList<Integer>();
        assertObjectNotNull(this);
        if (this.eventMap.isEmpty()) {
            throw new Exception("File is empty!");
        }

        else {

            for (Integer taskId : this.eventMap.keySet()) {
                Event event = this.eventMap.get(taskId);

                if (this.hasKeyWord(event, keyword)) {
                    matchedTaskIds.add(taskId);
                }
            }
        }

        if (matchedTaskIds.size() == 0) {
            throw new Exception("Your search request returned 0 results");
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

    public void updateDisplayID(Set<Integer> actualIDs) {
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
