import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeleteCommand implements ICommand {
    private static final String STRING_TO_BE_SCHEDULED = "To be scheduled";
    private TaskData taskData;
    private int actualId;
    private Event event;

    private static final Logger logger = Logger.getGlobal();

    private static final String simpleDateFormat = "dd MMM, yyyy";

    private static final String messageNumberEvents = "No. of events=%d";

    private static final String messageDateFormat = "Date: %s\n";
    private static final String messageDescriptionFormat = "Description: %s\n";
    private static final String messageDurationFormat = "Duration: %d minutes\n";
    private static final String messageLocationFormat = "Location: %s\n";
    private static final String messagePriorityFormat = "Priority level: %s\n";
    private static final String messageDeleteTask = "Delete task - %s\n";

    //@author A0134704M
    public DeleteCommand(TaskData taskData, int actualId) {
        this.taskData = taskData;
        this.actualId = actualId;
    }

    //@author A0134704M
    @Override
    public boolean execute() {
        event = taskData.getEventMap().remove(actualId);
        logger.log(Level.INFO,
                String.format(messageNumberEvents, taskData.getEventMap().size()));
        printConfirmation(event);
        return true;
    }

    //@author A0134704M
    @Override
    public boolean undo() {
        taskData.getEventMap().put(actualId, event);
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
        SimpleDateFormat format = new SimpleDateFormat(simpleDateFormat);
        System.out.printf(messageDeleteTask, event.getTaskName());
        try {
            System.out.printf(messageDateFormat,
                    format.format(event.getTaskDate().getTime()));
        } catch (NullPointerException e) {
            System.out.printf(messageDateFormat, STRING_TO_BE_SCHEDULED);
        }
        System.out.printf(messageDurationFormat, event.getTaskDuration());
        System.out.printf(messageLocationFormat, event.getTaskLocation());
        System.out.printf(messageDescriptionFormat, event.getTaskDescription());
        System.out.printf(messagePriorityFormat, event.getTaskPriority().toString()
                .toLowerCase());
    }
}
