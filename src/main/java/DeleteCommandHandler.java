import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeleteCommandHandler implements ICommandHandler {

    private TaskData taskData;
    private int taskId;
    private int actualId;

    private static final Pattern patternDelteCommand;
    
    private static final String taskIdDelimiter = "taskId";
    private static final String deleteCommandFormat = "^delete (?<taskId>[0-9]+)$";
    
    private static final String messageUseDisplayFunction = "Please use \"display\" function to get the ID!";
    
    static {
        patternDelteCommand = Pattern.compile(deleteCommandFormat);
    }

    public DeleteCommandHandler(TaskData taskData) {
        assertObjectNotNull(this);
        assertObjectNotNull(taskData);
        this.taskData = taskData;
    }

    @Override
    public boolean parseCommand(String command) {
        if (command.isEmpty()) {
            return false;
        } else {
            return setTaskID(command);
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

    @Override
    public ICommand getCommand() {
        ICommand deleteCommand = new DeleteCommand(taskData, actualId);
        try {
            actualId = taskData.getActualId(taskId);
        } catch (Exception NoSuchElementException) {
            System.out.println(messageUseDisplayFunction);
            return null;
        }
        boolean isExist = taskData.getEventMap().containsKey(actualId);
        if (isExist) {
            return deleteCommand;
        }
        return null;
    }

    @Override
    public boolean isExtraInputNeeded() {
        return false;
    }

    private void assertObjectNotNull(Object o) {
        assert (o != null);
    }
}
