import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TaskData implements Serializable {

    private static final long serialVersionUID = 6897919790578039077L;

    private Map<Integer, Event> eventMap;

    public TaskData() {
        eventMap = new HashMap<Integer, Event>();
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
        if(this.eventMap == null || taskData.eventMap == null) {
            return false;
        } else if(!this.eventMap.equals(taskData.eventMap)) {
            return false;
        }
        
        return true;
    }
}
