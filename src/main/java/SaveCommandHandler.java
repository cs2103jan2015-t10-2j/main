import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SaveCommandHandler implements ICommandHandler {

    private TaskData taskData;
    private String fileSavePath;

    private static final String KEY_SAVE_PATH = "fileSavePath";
    private static final String DONE_COMMAND_FORMAT = "^save *(?<fileSavePath>.+)?$";
    private static final String MESSAGE_FILE_SAVE = "File saved to %s\n";
    private static final String MESSAGE_SAVE_FAILED = "Cannot save to %s\n";

    private static final Pattern patternDoneCommand = Pattern.compile(
            DONE_COMMAND_FORMAT, Pattern.CASE_INSENSITIVE);

    //@author A0134704M
    public SaveCommandHandler(TaskData taskData) {
        this.taskData = taskData;
    }

    //@author A0134704M
    @Override
    public boolean parseCommand(String command) {
        Matcher m = patternDoneCommand.matcher(command);
        if (m.matches()) {
            this.fileSavePath = m.group(KEY_SAVE_PATH);
            return true;
        } else {
            return false;
        }
    }

    //@author A0134704M
    @Override
    public ICommand getCommand() {
        ICommand saveCommand = new NullCommand();
        try {
            DataManager.getInstance().setPathToSaveLoad(fileSavePath);
            fileSavePath = DataManager.getInstance().getPathToSaveLoad();

            DataManager.getInstance().saveTaskDataToFile(taskData);
            System.out.printf(MESSAGE_FILE_SAVE, fileSavePath);
            return saveCommand;
        } catch (IOException e) {
            System.out.printf(MESSAGE_SAVE_FAILED, fileSavePath);
            return null;
        }
    }

    //@author A0134704M
    @Override
    public boolean isExtraInputNeeded() {
        return false;
    }
}
