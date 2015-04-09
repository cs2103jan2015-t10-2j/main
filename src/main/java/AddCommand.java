import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddCommand implements ICommand {

    private TaskData taskData;
    private Event event;

    private static final Logger logger = Logger.getGlobal();
    private static final String loggerNumberOfEvents = "No. of events=%d";

    private static final String dateFormat = "dd MMM, yyyy EEE h:mm a";

    private static final String messageDateFormat = "Date: %s\n";
    private static final String messageDateFormatFloating = "Date: To be scheduled\n";
    private static final String messageDescriptionFormat = "Description: %s\n";
    private static final String messageDurationFormat = "Duration: %d minutes\n";
    private static final String messageLocationFormat = "Location: %s\n";
    private static final String messageNameFormat = "%s\n";
    private static final String messageAddEventFormat = "Added this event:\n";
    private static final String messagePriorityFormat = "Priority level: %s\n";

    //@author A0134704M
    public AddCommand(TaskData taskData, Event event) {
        this.taskData = taskData;
        this.event = event;
    }

    //@author A0134704M
    @Override
    public boolean execute() {
        Event previousEvent = taskData.getEventMap().put(event.getTaskID(), event);
        assert (previousEvent == null);

        printConfirmation(event);
        logger.log(Level.INFO,
                String.format(loggerNumberOfEvents, taskData.getEventMap().size()));

        return true;
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
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        System.out.printf(messageAddEventFormat);
        System.out.printf(messageNameFormat, event.getTaskName());
        System.out.printf(messageLocationFormat, event.getTaskLocation());
        System.out.printf(messageDurationFormat, event.getTaskDuration());
        System.out.printf(messageDescriptionFormat, event.getTaskDescription());
        try {
            System.out.printf(messageDateFormat,
                    format.format(event.getTaskDate().getTime()));
        } catch (NullPointerException e) {
            System.out.printf(messageDateFormatFloating);
        }
        System.out.printf(messagePriorityFormat, event.getTaskPriority().toString()
                .toLowerCase());
    }
}
