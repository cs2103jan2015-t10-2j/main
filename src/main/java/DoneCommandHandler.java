import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DoneCommandHandler implements ICommandHandler {

    private TaskData taskData;
    private int displayId;

    private static final Pattern patternDoneCommand;

    private static final String doneCommandFormat = "^done (?<taskId>[0-9]+)$";
    private static final String taskIdDelimiter = "taskId";

    //@author A0134704M
    static {
        patternDoneCommand = Pattern.compile(doneCommandFormat);
    }

    //@author A0134704M
    public DoneCommandHandler(TaskData taskData) {
        assertObjectNotNull(this);
        assertObjectNotNull(taskData);
        this.taskData = taskData;
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
            return null;
        }
        ICommand doneCommand = new DoneCommand(eventDone);
        return doneCommand;
    }

    //@author UNKNOWN
    private void assertObjectNotNull(Object o) {
        assert (o != null);
    }
}
