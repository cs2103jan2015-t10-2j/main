import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
        List<String> attributes = new ArrayList<String>();
        attributes.add(String.valueOf(taskID));
        attributes.add(taskName);
        if (taskDate == null) {
            attributes.add("-1");
        } else {
            attributes.add(String.valueOf(taskDate.getTimeInMillis()));
        }
        
        if (taskDueDate == null) {
            attributes.add("-1");
        } else {
            attributes.add(String.valueOf(taskDueDate.getTimeInMillis()));
        }

        attributes.add(String.valueOf(taskDuration));
        attributes.add(taskLocation);
        attributes.add(taskDescription);
        attributes.add(String.valueOf(taskPriority.name()));
        attributes.add(String.valueOf(isDone));
        attributes.add(String.valueOf(isRecurring));

        return attributes.toArray(new String[0]);
    }

    public static Event setEventDetails(String[] entry) throws Exception {
        try {
            Event event = new Event();
            
            event.setTaskID(Integer.parseInt(entry[0]));
            event.setTaskName(entry[1]);
            
            long taskDateLong = Long.valueOf(entry[2]);
            if (taskDateLong >= 0) {
                Calendar taskDate = Calendar.getInstance();
                taskDate.setTimeInMillis(taskDateLong);
                event.setTaskDate(taskDate);
            }
            
            long taskDueDateLong = Long.valueOf(entry[3]);
            if (taskDueDateLong >= 0) {
                Calendar taskDueDate = Calendar.getInstance();
                taskDueDate.setTimeInMillis(taskDateLong);
                event.setTaskDate(taskDueDate);
            }

            event.setTaskDuration(Integer.valueOf(entry[4]));
            event.setTaskLocation(entry[5]);
            event.setTaskDescription(entry[6]);
            event.setTaskPriority(TaskPriority.valueOf(entry[7]));
            event.setDone(Boolean.valueOf(entry[8]));
            event.setRecurring(Boolean.valueOf(entry[9]));            

            return event;
        } catch (Exception e) {
            throw e;
        }
    }
    
    private String getTimeString() {
        try {
            return getTaskDate().getTime().toString();
        } catch (Exception e) {
            return null;
        }
    }
}
