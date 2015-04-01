import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeleteCommandHandler implements ICommandHandler {

    private static final String messageNumberEvents = "No. of events=%d";
    private TaskData taskData;
    private int taskId;
    private int actualId;

    private static final Pattern patternDelteCommand;
    private static final Logger logger;

    private Event event;
    private boolean isConfirm;
    private boolean isProceedToConfirm;

    private static final String taskIdDelimiter = "taskId";
    private static final String loggerDeleteTitle = "DeleteCommandHandler";
    private static final String deleteCommandFormat = "^delete (?<taskId>[0-9]+)$";
    private static final String simpleDateFormat = "dd MMM, yyyy";

    private static final String messageUseDisplayFunction = "Please use \"display\" function to get the ID!";
    private static final String messageConfirmation = "Confirm? (Y/N): ";
    private static final String messageDateFormat = "Date: %s\n";
    private static final String messageDescriptionFormat = "Description: %s\n";
    private static final String messageDurationFormat = "Duration: %d minutes\n";
    private static final String messageLocationFormat = "Location: %s\n";
    private static final String messageDeleteTask = "Delete task - %s\n";
    private static final String no = "N";
    private static final String yes = "Y";

    static {
        patternDelteCommand = Pattern.compile(deleteCommandFormat);
        logger = Logger.getLogger(loggerDeleteTitle);
    }

    public DeleteCommandHandler(TaskData taskData) {
        assertObjectNotNull(this);
        assertObjectNotNull(taskData);
        this.taskData = taskData;
    }

    @Override
    public boolean parseCommand(String command) {
        if (isProceedToConfirm) {
            return parseProceedToConfirm(command);
        } else {
            if (command.isEmpty()) {
                return false;
            } else {
                return setTaskID(command);
            }
        }
    }

    private boolean setTaskID(String command) {
        Matcher patternMatcher = patternDelteCommand.matcher(command);
        if (patternMatcher.matches()) {
            taskId = Integer.parseInt(patternMatcher.group(taskIdDelimiter));
            return true;
        } else {
            return false;
        }
    }

    private boolean parseProceedToConfirm(String command) {
        boolean isYes = yes.equalsIgnoreCase(command);
        boolean isNo = no.equalsIgnoreCase(command);
        boolean isValid = (isYes ^ isNo);

        if (isValid) {
            isConfirm = isYes;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean executeCommand() {
        assertObjectNotNull(this);
        if (this.isProceedToConfirm) {
            if (this.isConfirm) {
                taskData.getEventMap().remove(actualId);
                logger.log(Level.INFO,
                        String.format(messageNumberEvents, taskData.getEventMap().size()));
            }
            isProceedToConfirm = false;
            return true;
        } else {
            try {
                actualId = taskData.getActualId(taskId);
            } catch (Exception NoSuchElementException) {
                System.out.println(messageUseDisplayFunction);
                return false;
            }
            boolean isExist = taskData.getEventMap().containsKey(actualId);
            if (isExist) {
                event = taskData.getEventMap().get(actualId);
                printConfirmation(event);
                this.isProceedToConfirm = true;
            }
        }
        return true;
    }

    private void printConfirmation(Event event) {
        SimpleDateFormat format = new SimpleDateFormat(simpleDateFormat);
        System.out.printf(messageDeleteTask, event.getTaskName());
        System.out.printf(messageDateFormat, format.format(event.getTaskDate().getTime()));
        System.out.printf(messageDurationFormat, event.getTaskDuration());
        System.out.printf(messageLocationFormat, event.getTaskLocation());
        System.out.printf(messageDescriptionFormat, event.getTaskDescription());
        System.out.printf(messageConfirmation);
    }

    @Override
    public boolean isExtraInputNeeded() {
        return this.isProceedToConfirm;
    }

    private void assertObjectNotNull(Object o) {
        assert (o != null);
    }
}
