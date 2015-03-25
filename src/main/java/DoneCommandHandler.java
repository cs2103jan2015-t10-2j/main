<<<<<<< HEAD
=======
import java.util.NoSuchElementException;
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DoneCommandHandler implements ICommandHandler {

    private TaskData taskData;
<<<<<<< HEAD
    private int taskId;
=======
    private int displayId;
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7

    private static final String doneCommandFormat = "^done (?<taskId>[0-9]+)$";
    private static final Pattern patternDoneCommand;

    static {
        patternDoneCommand = Pattern.compile(doneCommandFormat);
    }

    public DoneCommandHandler(TaskData taskData) {
<<<<<<< HEAD
    	assertObjectNotNull(this);
    	assertObjectNotNull(taskData);
=======
        assertObjectNotNull(this);
        assertObjectNotNull(taskData);
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
        this.taskData = taskData;
    }

    @Override
    public boolean parseCommand(String command) {
        try {
            Matcher matcher = patternDoneCommand.matcher(command);
            if (matcher.matches()) {
<<<<<<< HEAD
                taskId = Integer.parseInt(matcher.group("taskId"));
=======
                displayId = Integer.parseInt(matcher.group("taskId"));
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
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
<<<<<<< HEAD
    	assertObjectNotNull(taskData);
        boolean isExist = (taskData.getEventMap() != null 
                && taskData.getEventMap().containsKey(taskId));

        if (isExist) {
            Event eventDone = taskData.getEventMap().get(taskId);
            eventDone.setDone(true);

            return true;
        } else {
=======
        assertObjectNotNull(taskData);

        try {
            int actualId = taskData.getActualId(displayId);
            Event eventDone = taskData.getEventMap().get(actualId);
            eventDone.setDone(true);
            return true;
        } catch (NoSuchElementException e) {
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
            return false;
        }
    }

    @Override
    public boolean isExtraInputNeeded() {
        return false;
    }
<<<<<<< HEAD
    
    private void assertObjectNotNull(Object o) {
		assert (o != null);
	}
=======

    private void assertObjectNotNull(Object o) {
        assert (o != null);
    }
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
}
