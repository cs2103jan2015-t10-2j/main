import java.io.Serializable;
import java.util.Calendar;

public class Event implements Serializable {

    private static final long serialVersionUID = -6301813687015638579L;

    private int taskID;
    private String taskName;
    private Calendar taskDate;
    private String taskLocation;
    private String taskDescription;
    private TaskPriority taskPriority = TaskPriority.MEDIUM;
    private boolean isDone;
    private boolean isRecurring;

    private static final String toStringFormat = "ID=%d, Name=\"%s\", Location=\"%s\", Description=\"%s\", Date=%s, Priority=%s, Done=%b, Recurring=%b";

<<<<<<< HEAD
    public int getTaskID() {
    	assertObjectNotNull(this);
=======
    public Event() {
        taskDate = Calendar.getInstance();
    }

    public int getTaskID() {
        assertObjectNotNull(this);
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
        return taskID;
    }

    public void setTaskID(int taskID) {
<<<<<<< HEAD
    	assertObjectNotNull(this);
=======
        assertObjectNotNull(this);
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
        this.taskID = taskID;
    }

    public String getTaskName() {
<<<<<<< HEAD
    	assertObjectNotNull(this);
=======
        assertObjectNotNull(this);
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
        return taskName;
    }

    public void setTaskName(String taskName) {
<<<<<<< HEAD
    	assertObjectNotNull(this);
=======
        assertObjectNotNull(this);
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
        this.taskName = taskName;
    }

    public Calendar getTaskDate() {
<<<<<<< HEAD
    	assertObjectNotNull(this);
=======
        assertObjectNotNull(this);
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
        return taskDate;
    }

    public void setTaskDate(Calendar taskDate) {
<<<<<<< HEAD
    	assertObjectNotNull(this);
=======
        assertObjectNotNull(this);
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
        this.taskDate = taskDate;
    }

    public String getTaskLocation() {
<<<<<<< HEAD
    	assertObjectNotNull(this);
=======
        assertObjectNotNull(this);
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
        return taskLocation;
    }

    public void setTaskLocation(String taskLocation) {
<<<<<<< HEAD
    	assertObjectNotNull(this);
=======
        assertObjectNotNull(this);
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
        this.taskLocation = taskLocation;
    }

    public String getTaskDescription() {
<<<<<<< HEAD
    	assertObjectNotNull(this);
=======
        assertObjectNotNull(this);
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
<<<<<<< HEAD
    	assertObjectNotNull(this);
=======
        assertObjectNotNull(this);
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
        this.taskDescription = taskDescription;
    }

    public TaskPriority getTaskPriority() {
<<<<<<< HEAD
    	assertObjectNotNull(this);
=======
        assertObjectNotNull(this);
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
        return taskPriority;
    }

    public void setTaskPriority(TaskPriority taskPriority) {
<<<<<<< HEAD
    	assertObjectNotNull(this);
=======
        assertObjectNotNull(this);
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
        this.taskPriority = taskPriority;
    }

    public boolean isDone() {
<<<<<<< HEAD
    	assertObjectNotNull(this);
=======
        assertObjectNotNull(this);
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
        return isDone;
    }

    public void setDone(boolean isDone) {
<<<<<<< HEAD
    	assertObjectNotNull(this);
=======
        assertObjectNotNull(this);
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
        this.isDone = isDone;
    }

    public boolean isRecurring() {
<<<<<<< HEAD
    	assertObjectNotNull(this);
=======
        assertObjectNotNull(this);
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
        return isRecurring;
    }

    public void setRecurring(boolean isRecurring) {
<<<<<<< HEAD
    	assertObjectNotNull(this);
    	assert ((this.isRecurring));
    	assert (!(this.isRecurring));
=======
        assertObjectNotNull(this);
        assert ((this.isRecurring));
        assert (!(this.isRecurring));
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
        this.isRecurring = isRecurring;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Event)) {
            return false;
        }

        Event e = (Event) o;
<<<<<<< HEAD
    	assertObjectNotNull(this);
=======
        assertObjectNotNull(this);
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
        boolean isSame = (this.taskID == e.taskID);

        return isSame;
    }

    @Override
    public String toString() {
<<<<<<< HEAD
    	assertObjectNotNull(this);
        String timeString = getTaskDate().getTime().toString();
        return String.format(toStringFormat, getTaskID(), getTaskName(),
                getTaskLocation(), getTaskDescription(), timeString,
                getTaskPriority(), isDone(), isRecurring());
    }
    
	private void assertObjectNotNull(Object o) {
		assert (o != null);
	}
=======
        assertObjectNotNull(this);
        String timeString = getTaskDate().getTime().toString();
        return String.format(toStringFormat, getTaskID(), getTaskName(),
                getTaskLocation(), getTaskDescription(), timeString, getTaskPriority(),
                isDone(), isRecurring());
    }

    private void assertObjectNotNull(Object o) {
        assert (o != null);
    }
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
}
