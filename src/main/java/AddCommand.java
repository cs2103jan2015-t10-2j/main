import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddCommand implements ICommand {

    private TaskData taskData;
    private Event event;
    private TimeConflictAlert timeConflictAlert;

    private static final Logger logger = Logger.getGlobal();
    private static final String loggerNumberOfEvents = "No. of events=%d";

    private static final String dateFormatString = "dd MMM, yyyy EEE h:mm a";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
            dateFormatString);

    private static final String messageDateFormat = "Date: %s\n";
    private static final String messageNoDateFormat = "Date: To be scheduled\n";
    private static final String messageDueDateFormat = "Due Date: %s\n";
    private static final String messageNoDueDateFormat = "Due Date: To be scheduled\n";
    private static final String messageDescriptionFormat = "Description: %s\n";
    private static final String messageDurationFormat = "Duration: %d minutes\n";
    private static final String messageLocationFormat = "Location: %s\n";
    private static final String messageNameFormat = "%s\n";
    private static final String messageAddEventFormat = "Added this event:\n";
    private static final String messagePriorityFormat = "Priority level: %s\n";

    //@author A0134704M
    public AddCommand(TaskData taskData, Event event) {
        assert (taskData != null);
        assert (event != null);

        this.taskData = taskData;
        this.event = event;
        timeConflictAlert = new TimeConflictAlert(taskData, event);
    }

    //@author A0134704M
    @Override
    public boolean execute() {
        printConfirmation(event);
        timeConflictAlert.alertTimeConflict();
        addEventToRecord();

        return true;
    }

    //@author A0134704M
    private void addEventToRecord() {
        Event previousEvent = taskData.getEventMap().put(event.getTaskID(), event);
        assert (previousEvent == null);
        logger.log(Level.INFO,
                String.format(loggerNumberOfEvents, taskData.getEventMap().size()));
    }

    //@author A0134704M
    @Override
    public boolean undo() {
        boolean isRemoved = taskData.getEventMap().remove(event.getTaskID(), event);
        assert (isRemoved);
        return true;
    }

    //@author A0134704M
    @Override
    public boolean redo() {
        return this.execute();
    }

    //@author A0134704M
    @Override
    public boolean isReversible() {
        return true;
    }

    //@author A0134704M
    private void printConfirmation(Event event) {
        System.out.printf(messageAddEventFormat);
        System.out.printf(messageNameFormat, event.getTaskName());
        System.out.printf(messageLocationFormat, event.getTaskLocation());
        System.out.printf(messageDurationFormat, event.getTaskDuration());
        System.out.printf(messageDescriptionFormat, event.getTaskDescription());
        try {
            System.out.printf(messageDateFormat,
                    dateFormat.format(event.getTaskDate().getTime()));
        } catch (NullPointerException e) {
            System.out.printf(messageNoDateFormat);
        }
        try {
            System.out.printf(messageDueDateFormat,
                    dateFormat.format(event.getTaskDueDate().getTime()));
        } catch (NullPointerException e) {
            System.out.printf(messageNoDueDateFormat);
        }
        System.out.printf(messagePriorityFormat, event.getTaskPriority().toString()
                .toLowerCase());
    }
}
