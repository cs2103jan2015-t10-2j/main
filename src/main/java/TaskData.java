import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskData implements Serializable {

    private static final long serialVersionUID = 6897919790578039077L;

    private Map<Integer, Event> eventMap;

    public TaskData() {
        eventMap = new HashMap<Integer, Event>();
    }

    public ArrayList<Integer> searchByKeyword(String keyword) {
        ArrayList<Integer> matchedTaskIds = new ArrayList<Integer>();

        for (Integer taskId : eventMap.keySet()) {
            Event event = eventMap.get(taskId);

            if (hasKeyWord(event, keyword)) {
                matchedTaskIds.add(taskId);
            }
        }

        return matchedTaskIds;
    }

    public boolean hasKeyWord(Event event, String keyWord) {

        if (event == null || keyWord == null)
            return false;

        if (Integer.toString(event.getTaskID()) != null
                && Integer.toString(event.getTaskID()).contains(keyWord)) {
            return true;
        } else if (event.getTaskName() != null
                && event.getTaskName().contains(keyWord)) {
            return true;
        } else if (event.getTaskLocation() != null
                && event.getTaskLocation().contains(keyWord)) {
            return true;
        } else if (event.getTaskDescription() != null
                && event.getTaskDescription().contains(keyWord)) {
            return true;
        } else if (event.getTaskDate() != null
                && event.getTaskDate().getTime().toString().contains(keyWord)) {
            return true;
        } else if (event.getTaskPriority() != null
                && event.getTaskPriority().toString().contains(keyWord)) {
            return true;
        }

        return false;
    }

    public Map<Integer, Event> getEventMap() {
        return eventMap;
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
}
