import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DoneCommandHandler implements ICommandHandler {

    private TaskData taskData;
    private int taskId;

    private Pattern patternDoneCommand;

    private static final String doneCommandFormat = "^done (?<taskId>[0-9]+)$";

    public DoneCommandHandler(TaskData taskData) {
        this.taskData = taskData;
        patternDoneCommand = Pattern.compile(doneCommandFormat);
    }

    @Override
    public boolean isValid(String command) {
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
    public boolean parseCommand(String command) {
        return true;
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
