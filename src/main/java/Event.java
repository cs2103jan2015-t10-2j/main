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

    //@author A0134704M
    public Event() {
        taskDate = Calendar.getInstance();
    }

    //@author A0134704M
    public int getTaskID() {
        assertObjectNotNull(this);
        return taskID;
    }

    //@author A0134704M
    public void setTaskID(int taskID) {
        assertObjectNotNull(this);
        this.taskID = taskID;
    }

    //@author A0134704M
    public String getTaskName() {
        assertObjectNotNull(this);
        return taskName;
    }

    //@author A0134704M
    public void setTaskName(String taskName) {
        assertObjectNotNull(this);
        this.taskName = taskName;
    }

    //@author A0134704M
    public Calendar getTaskDate() {
        assertObjectNotNull(this);
        return taskDate;
    }

    //@author A0134704M
    public void setTaskDate(Calendar taskDate) {
        assertObjectNotNull(this);
        this.taskDate = taskDate;
    }

    //@author UNKNOWN
    public int getTaskDuration() {
        assertObjectNotNull(this);
        return taskDuration;
    }

    //@author UNKNOWN
    public void setTaskDuration(int taskDuration) {
        assertObjectNotNull(this);
        this.taskDuration = taskDuration;
    }

    //@author A0134704M
    public String getTaskLocation() {
        assertObjectNotNull(this);
        return taskLocation;
    }

    //@author A0134704M
    public void setTaskLocation(String taskLocation) {
        assertObjectNotNull(this);
        this.taskLocation = taskLocation;
    }

    //@author A0134704M
    public String getTaskDescription() {
        assertObjectNotNull(this);
        return taskDescription;
    }

    //@author A0134704M
    public void setTaskDescription(String taskDescription) {
        assertObjectNotNull(this);
        this.taskDescription = taskDescription;
    }

    //@author A0134704M
    public TaskPriority getTaskPriority() {
        assertObjectNotNull(this);
        return taskPriority;
    }

    //@author A0134704M
    public void setTaskPriority(TaskPriority taskPriority) {
        assertObjectNotNull(this);
        this.taskPriority = taskPriority;
    }

    //@author A0134704M
    public boolean isDone() {
        assertObjectNotNull(this);
        return isDone;
    }

    //@author A0134704M
    public void setDone(boolean isDone) {
        assertObjectNotNull(this);
        this.isDone = isDone;
    }

    //@author UNKNOWN
    public boolean isRecurring() {
        assertObjectNotNull(this);
        return isRecurring;
    }

    //@author UNKNOWN
    public void setRecurring(boolean isRecurring) {
        assertObjectNotNull(this);
        assert ((this.isRecurring));
        assert (!(this.isRecurring));
        this.isRecurring = isRecurring;
    }

    //@author A0134704M
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

    //@author A0134704M
    @Override
    public String toString() {
        assertObjectNotNull(this);
        String timeString;
        try {
            timeString = getTaskDate().getTime().toString();
        } catch (Exception e) {
            timeString = null;
        }
        return String.format(toStringFormat, getTaskID(), getTaskName(),
                getTaskLocation(), getTaskDescription(), timeString, getTaskPriority(),
                isDone(), isRecurring());
    }

    //@author UNKNOWN
    private void assertObjectNotNull(Object o) {
        assert (o != null);
    }
}
