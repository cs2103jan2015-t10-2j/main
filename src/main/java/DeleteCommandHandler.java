import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeleteCommandHandler implements ICommandHandler {

    private TaskData taskData;
    private int taskId;

    private static final String deleteCommandFormat = "^delete (?<taskId>[0-9]+)$";
    private static final Pattern patternDelteCommand;
    private static final Logger logger;

    static {
        patternDelteCommand = Pattern.compile(deleteCommandFormat);
        logger = Logger.getLogger("DeleteCommandHandler");
    }

    public DeleteCommandHandler(TaskData taskData) {
        this.taskData = taskData;
    }

    @Override
    public boolean parseCommand(String command) {
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

    @Override
    public boolean executeCommand() {
        if (taskData.getEventMap().containsKey(taskId)) {
            taskData.getEventMap().remove(taskId);
            logger.log(Level.INFO, String.format("No. of events=%d", taskData.getEventMap().size()));
            return true;
        }

        return false;
    }

    @Override
    public boolean isExtraInputNeeded() {
        return false;
    }
}
