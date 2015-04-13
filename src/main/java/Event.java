import java.util.Calendar;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Event implements Serializable {



    private static final long serialVersionUID = -6301813687015638579L;

    private int taskID;
    private String taskName;
    private Calendar taskDate;
    private Calendar taskDueDate;
    private int taskDuration;
    private String taskLocation;
    private String taskDescription;
    private TaskPriority taskPriority = TaskPriority.MEDIUM;
    private boolean isDone;
    private boolean isRecurring;

    private static final String SIMPLE_DATE_FORMAT = "dd MMM, yyyy EEE h:mm a";
    private static final String toStringFormat = "ID: %d, Name: \"%s\", Location: \"%s\", Description: \"%s\", Date: %s, Duration: %d, Priority: %s, Done: %b, Recurring: %b";
    private static final String TO_STRING_FIELDS_FORMAT = "%d~//~, \"%s\"~//~, \"%s\"~//~, \"%s\"~//~, %s~//~, %d~//~, %s~//~, %b~//~, %b";
    private static final String SPLITTER_FOR_STRING_ARRAY = "~//~, ";

    //@author A0134704M
    public Event() {
        taskDate = Calendar.getInstance();
    }
    
    //@author A0134704M
    public TaskType getTaskType() {
        if (taskDate != null) {
            return TaskType.SCHEDULED;
        } else if (taskDueDate != null) {
            return TaskType.DUE;
        } else {
            return TaskType.FLOATING;
        }
    }

    //@author A0134704M
    public int getTaskID() {
        return taskID;
    }

    //@author A0134704M
    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    //@author A0134704M
    public String getTaskName() {
        return taskName;
    }

    //@author A0134704M
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    //@author A0134704M
    public Calendar getTaskDate() {
        return taskDate;
    }

    //@author A0134704M
    public void setTaskDate(Calendar taskDate) {
        this.taskDate = taskDate;
    }

    //@author A0134704M
    public Calendar getTaskDueDate() {
        return taskDueDate;
    }

    //@author A0134704M
    public void setTaskDueDate(Calendar taskDueDate) {
        this.taskDueDate = taskDueDate;
    }

    //@author A0109239A
    public int getTaskDuration() {
        return taskDuration;
    }

    public void setTaskDuration(int taskDuration) {
        this.taskDuration = taskDuration;
    }

    //@author A0134704M
    public String getTaskLocation() {
        return taskLocation;
    }

    //@author A0134704M
    public void setTaskLocation(String taskLocation) {
        this.taskLocation = taskLocation;
    }

    //@author A0134704M
    public String getTaskDescription() {
        return taskDescription;
    }

    //@author A0134704M
    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    //@author A0134704M
    public TaskPriority getTaskPriority() {
        return taskPriority;
    }

    //@author A0134704M
    public void setTaskPriority(TaskPriority taskPriority) {
        this.taskPriority = taskPriority;
    }

    //@author A0134704M
    public boolean isDone() {
        return isDone;
    }

    //@author A0134704M
    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    //@author UNKNOWN
    public boolean isRecurring() {
        return isRecurring;
    }

    //@author UNKNOWN
    public void setRecurring(boolean isRecurring) {
        this.isRecurring = isRecurring;
    }

    //@author A0134704M
    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Event)) {
            return false;
        }

        Event e = (Event) o;
        boolean isSame = (this.taskID == e.taskID);

        return isSame;
    }

    //@author A0134704M
    @Override
    public String toString() {
        return String.format(toStringFormat, getTaskID(), getTaskName(),
                getTaskLocation(), getTaskDescription(), getTimeString(), getTaskDuration(), getTaskPriority(),
                isDone(), isRecurring());
    }
    
    //@author A0109239A
    public String[] getEventDetails() {
        //Casting all items into 'String' in one go.
        String fieldsString = String.format(TO_STRING_FIELDS_FORMAT, getTaskID(), getTaskName(),
                getTaskLocation(), getTaskDescription(), getTimeString(), getTaskDuration(), getTaskPriority(),
                isDone(), isRecurring());
        String[] fieldsArray = fieldsString.split(SPLITTER_FOR_STRING_ARRAY);
        return fieldsArray;
    }
    
    private String getTimeString() {
        try {
            return getTaskDate().getTime().toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static Event setEventDetails(String[] entry) throws Exception {
        try {
            Event event = new Event();
            event.setTaskID(Integer.parseInt(entry[0]));
            event.setTaskName(entry[1]);
            event.setTaskLocation(entry[2]);
            event.setTaskDescription(entry[3]);
            event.setTaskDate(getEventTime(entry[4]));
            event.setTaskDuration(Integer.parseInt(entry[5]));
            event.setTaskPriority(TaskPriority.valueOf(entry[6]));
            event.setDone(Boolean.parseBoolean(entry[7]));
            event.setRecurring(Boolean.parseBoolean(entry[8]));
            return event;
        } catch (Exception e) {
            throw e;
        }
    }

    private static Calendar getEventTime(String timeString) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(SIMPLE_DATE_FORMAT); 
        Calendar cal=Calendar.getInstance();
        cal.setTime(format.parse(timeString));
        return cal;
    }
    
}
