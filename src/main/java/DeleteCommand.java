import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeleteCommand implements ICommand {
    
    private TaskData taskData;
    private int actualId;
    private Event event;

    private static final Logger logger = Logger.getGlobal();

    private static final String simpleDateFormat = "dd MMM, yyyy";

    private static final String STRING_TO_BE_SCHEDULED = "No Date was scheduled";
    
    private static final String MESSAGE_NUMBER_OF_EVENTS = "No. of events=%d";

    private static final String MESSAGE_DATE_FORMAT = "Date: %s\n";
    private static final String MESSAGE_DUE_DATE_FORMAT = "Due Date: %s\n";
    private static final String MESSAGE_DESCRIPTION_FORMAT = "Description: %s\n";
    private static final String MESSAGE_DURATION_FORMAT = "Duration: %d minutes\n";
    private static final String MESSAGE_LOCATION_FORMAT = "Location: %s\n";
    private static final String MESSAGE_PRIORITY_FORMAT = "Priority level: %s\n";
    private static final String MESSAGE_DELETE_TASK = "Delete task - %s\n";

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
                String.format(MESSAGE_NUMBER_OF_EVENTS, taskData.getEventMap().size()));
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
        System.out.printf(MESSAGE_DELETE_TASK, event.getTaskName());
        try {
            System.out.printf(MESSAGE_DATE_FORMAT,
                    format.format(event.getTaskDate().getTime()));
        } catch (NullPointerException e) {
            System.out.printf(MESSAGE_DATE_FORMAT, STRING_TO_BE_SCHEDULED);
        }
        try {
            System.out.printf(MESSAGE_DUE_DATE_FORMAT,
                    format.format(event.getTaskDueDate().getTime()));
        } catch (NullPointerException e) {
            System.out.printf(MESSAGE_DUE_DATE_FORMAT, STRING_TO_BE_SCHEDULED);
        }
        System.out.printf(MESSAGE_DURATION_FORMAT, event.getTaskDuration());
        System.out.printf(MESSAGE_LOCATION_FORMAT, event.getTaskLocation());
        System.out.printf(MESSAGE_DESCRIPTION_FORMAT, event.getTaskDescription());
        System.out.printf(MESSAGE_PRIORITY_FORMAT, event.getTaskPriority().toString()
                .toLowerCase());
    }
}
