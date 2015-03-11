import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DoneCommandHandler implements ICommandHandler {

    private TaskData taskData;
    private int taskId;

    private static final String doneCommandFormat = "^done (?<taskId>[0-9]+)$";
    private static final Pattern patternDoneCommand;

    static {
        patternDoneCommand = Pattern.compile(doneCommandFormat);
    }

    public DoneCommandHandler(TaskData taskData) {
        this.taskData = taskData;
    }

    @Override
    public boolean parseCommand(String command) {
        try {
            Matcher matcher = patternDoneCommand.matcher(command);
            if (matcher.matches()) {
                taskId = Integer.parseInt(matcher.group("taskId"));
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean executeCommand() {
        boolean isExist = (taskData.getEventMap() != null 
                && taskData.getEventMap().containsKey(taskId));

        if (isExist) {
            Event eventDone = taskData.getEventMap().get(taskId);
            eventDone.setDone(true);

            return true;
        } else {
            return false;
        }
    }
}
