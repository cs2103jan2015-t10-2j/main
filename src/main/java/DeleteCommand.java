import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeleteCommand implements ICommand {
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

    public DeleteCommand(TaskData taskData, int actualId) {
        this.taskData = taskData;
        this.actualId = actualId;
    }

    @Override
    public boolean execute() {
        event = taskData.getEventMap().remove(actualId);
        logger.log(Level.INFO,
                String.format(messageNumberEvents, taskData.getEventMap().size()));
        printConfirmation(event);
        return true;
    }

    @Override
    public boolean undo() {
        taskData.getEventMap().put(actualId, event);
        return true;
    }

    @Override
    public boolean redo() {
        return this.execute();
    }

    private void printConfirmation(Event event) {
        SimpleDateFormat format = new SimpleDateFormat(simpleDateFormat);
        System.out.printf(messageDeleteTask, event.getTaskName());
        System.out
                .printf(messageDateFormat, format.format(event.getTaskDate().getTime()));
        System.out.printf(messageDurationFormat, event.getTaskDuration());
        System.out.printf(messageLocationFormat, event.getTaskLocation());
        System.out.printf(messageDescriptionFormat, event.getTaskDescription());
        System.out.printf(messagePriorityFormat, event.getTaskPriority().toString()
                .toLowerCase());
    }
}
