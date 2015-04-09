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
    
    //@author A0134704M
    static {
        patternDelteCommand = Pattern.compile(deleteCommandFormat);
    }

    //@author A0134704M
    public DeleteCommandHandler(TaskData taskData) {
        assertObjectNotNull(this);
        assertObjectNotNull(taskData);
        this.taskData = taskData;
    }

    //@author A0134704M
    @Override
    public boolean parseCommand(String command) {
        if (command.isEmpty()) {
            return false;
        } else {
            return setTaskID(command);
        }
    }

    //@author A0134704M
    private boolean setTaskID(String command) {
        Matcher patternMatcher = patternDelteCommand.matcher(command);
        if (patternMatcher.matches()) {
            taskId = Integer.parseInt(patternMatcher.group(taskIdDelimiter));
            return true;
        } else {
            return false;
        }
    }

    //@author A0134704M
    @Override
    public ICommand getCommand() {
        try {
            actualId = taskData.getActualId(taskId);
        } catch (Exception NoSuchElementException) {
            System.out.println(messageUseDisplayFunction);
            return null;
        }
        boolean isExist = taskData.getEventMap().containsKey(actualId);
        if (isExist) {
            ICommand deleteCommand = new DeleteCommand(taskData, actualId);
            return deleteCommand;
        }
        return null;
    }

    //@author A0134704M
    @Override
    public boolean isExtraInputNeeded() {
        return false;
    }

    //@author UNKNOWN
    private void assertObjectNotNull(Object o) {
        assert (o != null);
    }
}
