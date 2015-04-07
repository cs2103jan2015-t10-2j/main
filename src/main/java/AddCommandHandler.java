import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddCommandHandler implements ICommandHandler {
    private static final String addCommandName = "add";
    private static final String dateFormat = "dd MMM, yyyy EEE h:mm a";

    private static final String messageDateFormat = "Date: %s\n";
    private static final String messageDateFormatFloating = "Date: To be scheduled\n";
    private static final String messageDescriptionFormat = "Description: %s\n";
    private static final String messageDurationFormat = "Duration: %d minutes\n";
    private static final String messageLocationFormat = "Location: %s\n";
    private static final String messageNameFormat = "%s\n";
    private static final String messageAddEventFormat = "Added this event:\n";
    private static final String messagePriorityFormat = "Priority level: %s\n";

    private static final String loggerNumberOfEvents = "No. of events=%d";
    private static final String loggerInputCommand = "Input command - %s";

    private TaskData taskData;
    private Event event;

    private static final Logger logger = Logger.getGlobal();

    public AddCommandHandler(TaskData taskData) {
        assertObjectNotNull(taskData);
        this.taskData = taskData;
    }

    /*
     * add [name] at [time] [date] for [duration] mins @ [location] desc "[description]"
     * 
     * (non-Javadoc)
     * 
     * @see ICommandHandler#parseCommand(java.lang.String)
     */
    @Override
    public boolean parseCommand(String command) {

        logger.log(Level.INFO, String.format(loggerInputCommand, command));

        if (command.isEmpty()) {
            return false;
        } else {
            setEvent(command);
            return true;
        }
    }

    public void setEvent(String command) {
        event = CommandParser.getDetailFromCommand(addCommandName, command);
        assertObjectNotNull(event);
        event.setTaskID(getUniqueId());
    }

    private void printConfirmation(Event event) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        System.out.printf(messageAddEventFormat);
        System.out.printf(messageNameFormat, event.getTaskName());
        System.out.printf(messageLocationFormat, event.getTaskLocation());
        System.out.printf(messageDurationFormat, event.getTaskDuration());
        System.out.printf(messageDescriptionFormat, event.getTaskDescription());
        try {
            System.out.printf(messageDateFormat, format.format(event.getTaskDate().getTime()));
        } catch (NullPointerException e) {
            System.out.printf(messageDateFormatFloating);
        }
        System.out.printf(messagePriorityFormat, event.getTaskPriority().toString().toLowerCase());
    }

    @Override
    public boolean executeCommand() {
        taskData.getEventMap().put(event.getTaskID(), event);
        printConfirmation(event);
        logger.log(Level.INFO,
                String.format(loggerNumberOfEvents, taskData.getEventMap().size()));
        return true;
    }

    @Override
    public boolean isExtraInputNeeded() {
        return false;
    }

    @Override
    public boolean isCommandReady() {
        return true;
    }

    public int getUniqueId() {
        Random random = new Random();
        int returnVal;
        do {
            returnVal = random.nextInt(Integer.MAX_VALUE);
        } while (taskData.getEventMap().containsKey(returnVal));

        return returnVal;
    }

    public Event getEvent() {
        return event;
    }

    private void assertObjectNotNull(Object o) {
        assert (o != null);
    }
}
