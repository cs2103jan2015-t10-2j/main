import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class TimeConflictAlert {
    private TaskData taskData;
    private Event event;

    private static final String dateFormatString = "dd MMM, yyyy EEE h:mm a";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
            dateFormatString);

    //@author A0134704M
    public TimeConflictAlert(TaskData taskData, Event event) {
        assert (taskData != null);
        assert (event != null);

        this.taskData = taskData;
        this.event = event;
    }

    //@author A0134704M
    public void alertTimeConflict() {
        if (event.getTaskType() == TaskType.SCHEDULED) {
            List<Event> timeConflictTasks = extractConflictTasks();
            printConflictTasks(timeConflictTasks);
        }
    }

    //@author A0134704M
    private List<Event> extractConflictTasks() {
        assert (event.getTaskDate() != null);
        assert (event.getTaskDuration() >= 0);

        Calendar startTime = event.getTaskDate();
        Calendar endTime = Calendar.getInstance();

        endTime.setTimeInMillis(startTime.getTimeInMillis());
        endTime.add(Calendar.MINUTE, event.getTaskDuration());

        List<Event> timeConflictTasks = taskData.getTaskInDateRange(startTime, endTime,
                false);
        return timeConflictTasks;
    }

    //@author A0134704M
    private void printConflictTasks(List<Event> timeConflictTasks) {
        System.out.printf("ALERT: There are %d event(s) during the event.\n",
                timeConflictTasks.size());
        for (Event timeConflictTask : timeConflictTasks) {
            System.out.printf(" %8s [%s] - %s\n", timeConflictTask.getTaskPriority(),
                    dateFormat.format(timeConflictTask.getTaskDate().getTime()),
                    timeConflictTask.getTaskName());
        }
    }
}
