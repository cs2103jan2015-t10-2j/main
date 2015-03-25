import java.io.Serializable;
import java.util.Calendar;

public class Event implements Serializable {

    private static final long serialVersionUID = -6301813687015638579L;

    private int taskID;
    private String taskName;
    private Calendar taskDate;
    private int taskDuration;
    private String taskLocation;
    private String taskDescription;
    private TaskPriority taskPriority = TaskPriority.MEDIUM;
    private boolean isDone;
    private boolean isRecurring;

    private static final String toStringFormat = "ID=%d, Name=\"%s\", Location=\"%s\", Description=\"%s\", Date=%s, Priority=%s, Done=%b, Recurring=%b";

    public Event() {
        taskDate = Calendar.getInstance();
    }

    public int getTaskID() {
        assertObjectNotNull(this);
        return taskID;
    }

    public void setTaskID(int taskID) {
        assertObjectNotNull(this);
        this.taskID = taskID;
    }

    public String getTaskName() {
        assertObjectNotNull(this);
        return taskName;
    }

    public void setTaskName(String taskName) {
        assertObjectNotNull(this);
        this.taskName = taskName;
    }

    public Calendar getTaskDate() {
        assertObjectNotNull(this);
        return taskDate;
    }

    public void setTaskDate(Calendar taskDate) {
        assertObjectNotNull(this);
        this.taskDate = taskDate;
    }
    
    public int getTaskDuration () {
    	assertObjectNotNull(this);
        return taskDuration;
    }
    
    public void setTaskDuration (int taskDuration) {
    	assertObjectNotNull(this);
        this.taskDuration = taskDuration;
    }

    public String getTaskLocation() {
        assertObjectNotNull(this);
        return taskLocation;
    }

    public void setTaskLocation(String taskLocation) {
        assertObjectNotNull(this);
        this.taskLocation = taskLocation;
    }

    public String getTaskDescription() {
        assertObjectNotNull(this);
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        assertObjectNotNull(this);
        this.taskDescription = taskDescription;
    }

    public TaskPriority getTaskPriority() {
        assertObjectNotNull(this);
        return taskPriority;
    }

    public void setTaskPriority(TaskPriority taskPriority) {
        assertObjectNotNull(this);
        this.taskPriority = taskPriority;
    }

    public boolean isDone() {
        assertObjectNotNull(this);
        return isDone;
    }

    public void setDone(boolean isDone) {
        assertObjectNotNull(this);
        this.isDone = isDone;
    }

    public boolean isRecurring() {
        assertObjectNotNull(this);
        return isRecurring;
    }

    public void setRecurring(boolean isRecurring) {
        assertObjectNotNull(this);
        assert ((this.isRecurring));
        assert (!(this.isRecurring));
        this.isRecurring = isRecurring;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Event)) {
            return false;
        }

        Event e = (Event) o;
        assertObjectNotNull(this);
        boolean isSame = (this.taskID == e.taskID);

        return isSame;
    }

    @Override
    public String toString() {
        assertObjectNotNull(this);
        String timeString = getTaskDate().getTime().toString();
        return String.format(toStringFormat, getTaskID(), getTaskName(),
                getTaskLocation(), getTaskDescription(), timeString, getTaskPriority(),
                isDone(), isRecurring());
    }

    private void assertObjectNotNull(Object o) {
        assert (o != null);
    }
}
