import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fusesource.jansi.Ansi.Color;

public class AddCommand implements ICommand {

    private TaskData taskData;
    private Event event;
    private TimeConflictAlert timeConflictAlert;

    private static final Logger logger = Logger.getGlobal();
    private static final String loggerNumberOfEvents = "No. of events=%d";

    private static final String dateFormatString = "dd MMM, yyyy EEE h:mm a";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
            dateFormatString);

    private static final String MESSAGE_DATE_FORMAT = "Date: %s\n";
    private static final String MESSAGE_NO_DATE_FORMAT = "Date: To be scheduled\n";
    private static final String MESSAGE_DUE_DATE_FORMAT = "Due Date: %s\n";
    private static final String MESSAGE_NO_DUE_DATE_FORMAT = "Due Date: To be scheduled\n";
    private static final String MESSAGE_DESCRIPTION_FORMAT = "Description: %s\n";
    private static final String MESSAGE_DURATION_FORMAT = "Duration: %d minutes\n";
    private static final String MESSAGE_LOCATION_FORMAT = "Location: %s\n";
    private static final String MESSAGE_NAME_FORMAT = "%s\n";
    private static final String MESSAGE_ADD_EVENT_FORMAT = "Added this event:\n";
    private static final String MESSAGE_PRIORITY_FORMAT = "Priority level: %s\n";

    private static final Color COLOUR_CONFIRM_MESSAGE = Color.BLUE;

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
        printConfirmFormat(MESSAGE_ADD_EVENT_FORMAT);
        printConfirmFormat(MESSAGE_NAME_FORMAT, event.getTaskName());
        printConfirmFormat(MESSAGE_LOCATION_FORMAT, event.getTaskLocation());
        printConfirmFormat(MESSAGE_DURATION_FORMAT, event.getTaskDuration());
        printConfirmFormat(MESSAGE_DESCRIPTION_FORMAT, event.getTaskDescription());
        try {
            printConfirmFormat(MESSAGE_DATE_FORMAT,
                    dateFormat.format(event.getTaskDate().getTime()));
        } catch (NullPointerException e) {
            printConfirmFormat(MESSAGE_NO_DATE_FORMAT);
        }
        try {
            printConfirmFormat(MESSAGE_DUE_DATE_FORMAT,
                    dateFormat.format(event.getTaskDueDate().getTime()));
        } catch (NullPointerException e) {
            printConfirmFormat(MESSAGE_NO_DUE_DATE_FORMAT);
        }
        printConfirmFormat(MESSAGE_PRIORITY_FORMAT, event.getTaskPriority().toString()
                .toLowerCase());
    }

    public void printConfirmFormat(String format, Object... content) {
        ConsoleUtility.printf(COLOUR_CONFIRM_MESSAGE, format, content);
    }
}
