import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeleteCommandHandler implements ICommandHandler {

    private TaskData taskData;
    private IInputSource inputSource;
    private int taskId;
    private int actualId;

    private static final Pattern patternDelteCommand;

    private static final String deleteCommandFormat = "^delete (?<taskId>[0-9]+)$";
    private static final String STRING_TASK_ID = "taskId";

    private static final String COMMAND_DISPLAY_MONTH = "display month";
    private static final String MESSAGE_EVENT_NOT_FOUND = "Event not found.";

    //@author A0134704M
    static {
        patternDelteCommand = Pattern.compile(deleteCommandFormat, Pattern.CASE_INSENSITIVE);
    }

    //@author A0134704M
    public DeleteCommandHandler(TaskData taskData, IInputSource inputSource) {
        assert (taskData != null);
        assert (inputSource != null);
        this.taskData = taskData;
        this.inputSource = inputSource;
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
            taskId = Integer.parseInt(patternMatcher.group(STRING_TASK_ID));
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
            if (taskData.isDisplayIdMapEmpty()) {
                inputSource.addCommand(COMMAND_DISPLAY_MONTH);
            } else {
                System.out.println(MESSAGE_EVENT_NOT_FOUND);
            }
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
}
