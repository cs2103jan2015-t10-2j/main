import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeleteCommandHandler implements ICommandHandler {

    private TaskData taskData;
    private int taskId;
    private int actualId;
    
    private Event event;
    private boolean isConfirm;
    private boolean isProceedToConfirm;

    private static final String deleteCommandFormat = "^delete (?<taskId>[0-9]+)$";
    private static final Pattern patternDelteCommand;
    private static final Logger logger;

    static {
        patternDelteCommand = Pattern.compile(deleteCommandFormat);
        logger = Logger.getLogger("DeleteCommandHandler");
    }

    public DeleteCommandHandler(TaskData taskData) {
    	assertObjectNotNull(this);
    	assertObjectNotNull(taskData);
        this.taskData = taskData;
    }

    @Override
    public boolean parseCommand(String command) {
        if (isProceedToConfirm) {
            boolean isYes = "Y".equalsIgnoreCase(command);
            boolean isNo = "N".equalsIgnoreCase(command);
            boolean isValid = (isYes ^ isNo);

            if (isValid) {
                isConfirm = isYes;
                return true;
            } else {
                return false;
            }
        } else {
            if (command.isEmpty()) {
                return false;
            } else {
                Matcher patternMatcher = patternDelteCommand.matcher(command);
                if (patternMatcher.matches()) {
                    taskId = Integer.parseInt(patternMatcher.group("taskId"));
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    @Override
    public boolean executeCommand() {
    	assertObjectNotNull(this);
        if (this.isProceedToConfirm) {
            if (this.isConfirm) {
                taskData.getEventMap().remove(actualId);
                logger.log(Level.INFO, String.format("No. of events=%d", taskData.getEventMap().size()));
            }
            isProceedToConfirm = false;
            return true;
        } else {
            try {
                actualId = taskData.getActualId(taskId);
            } catch (Exception NoSuchElementException) {
                System.out.println("Please use \"display\" function to get the ID!");
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
        SimpleDateFormat format = new SimpleDateFormat("dd MMM, yyyy");
        System.out.printf("Delete task - %s\n", event.getTaskName());
        System.out.printf("Date: %s\n", format.format(event.getTaskDate().getTime()));
        System.out.printf("Location: %s\n", event.getTaskLocation());
        System.out.printf("Description: %s\n", event.getTaskDescription());
        System.out.printf("Confirm? (Y/N): ");
    }

    @Override
    public boolean isExtraInputNeeded() {
        return this.isProceedToConfirm;
    }
    
	private void assertObjectNotNull(Object o) {
		assert (o != null);
	}
}
