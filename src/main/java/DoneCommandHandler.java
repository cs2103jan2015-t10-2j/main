import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DoneCommandHandler implements ICommandHandler {

    private TaskData taskData;
    private IInputSource inputSource;
    private int displayId;

    private static final Pattern patternDoneCommand;

    private static final String doneCommandFormat = "^done (?<taskId>[0-9]+)$";
    private static final String taskIdDelimiter = "taskId";

    private static final String COMMAND_DISPLAY_MONTH = "display month";
    private static final String MESSAGE_EVENT_NOT_FOUND = "Event not found.";

    //@author A0134704M
    static {
        patternDoneCommand = Pattern.compile(doneCommandFormat, Pattern.CASE_INSENSITIVE);
    }

    //@author A0134704M
    public DoneCommandHandler(TaskData taskData, IInputSource inputSource) {
        assert (taskData != null);
        assert (inputSource != null);
        this.taskData = taskData;
        this.inputSource = inputSource;
    }

    //@author A0134704M
    @Override
    public boolean parseCommand(String command) {
        try {
            Matcher matcher = patternDoneCommand.matcher(command);
            if (matcher.matches()) {
                displayId = Integer.parseInt(matcher.group(taskIdDelimiter));
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //@author A0134704M
    @Override
    public boolean isExtraInputNeeded() {
        return false;
    }

    //@author A0134704M
    @Override
    public ICommand getCommand() {
        Event eventDone;
        try {
            int actualId = taskData.getActualId(displayId);
            eventDone = taskData.getEventMap().get(actualId);
        } catch (NoSuchElementException e) {
            if (taskData.isDisplayIdMapEmpty()) {
                inputSource.addCommand(COMMAND_DISPLAY_MONTH);
            } else {
                System.out.println(MESSAGE_EVENT_NOT_FOUND);
            }
            return null;
        }
        ICommand doneCommand = new DoneCommand(eventDone);
        return doneCommand;
    }
}
