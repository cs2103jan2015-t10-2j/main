import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SaveCommandHandler implements ICommandHandler {

    private TaskData taskData;
    private String fileSavePath;
    private String fileSavePathCSV;

    private static final String KEY_SAVE_PATH = "fileSavePath";
    private static final String DONE_COMMAND_FORMAT = "^save *(?<fileSavePath>.+)?$";
    private static final String MESSAGE_FILE_SAVE = "File saved to %s\n";
    private static final String MESSAGE_SAVE_FAILED = "Cannot save to %s\n";

    private static final Pattern patternDoneCommand = Pattern.compile(
            DONE_COMMAND_FORMAT, Pattern.CASE_INSENSITIVE);

    //@author A0105886W
    public SaveCommandHandler(TaskData taskData) {
        this.taskData = taskData;
    }

    //@author A0105886W
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

    //@author A0105886W
    @Override
    public ICommand getCommand() {
        ICommand saveCommand = new NullCommand();
        try {
            saveToDat();
            saveToCsv();
            System.out.printf(MESSAGE_FILE_SAVE, fileSavePath);
            return saveCommand;
        } catch (IOException e) {
            System.out.printf(MESSAGE_SAVE_FAILED, fileSavePath);
            return null;
        }
    }

    //@author A0134704M
    public void saveToDat() throws IOException {
        if (fileSavePath.toLowerCase().endsWith(".dat")) {
        } else {
            fileSavePath = fileSavePath + ".dat";
        }
        DataManager.getInstance().setPathToSaveLoad(fileSavePath);
        fileSavePath = DataManager.getInstance().getPathToSaveLoad();
        DataManager.getInstance().saveTaskDataToFile(taskData);
    }

    //@author A0134704M
    public void saveToCsv() throws IOException {
        if (fileSavePath.toLowerCase().endsWith(".dat")) {
            fileSavePathCSV = fileSavePath.substring(0, fileSavePath.length()-3) + "csv";
        } else {
            fileSavePathCSV = fileSavePath + ".csv";
        }
        DataManager.getInstance().setPathToSaveHumanEditable(fileSavePathCSV);
        DataManager.getInstance().saveAsCsvToDisk(taskData);
    }

    //@author A0105886W
    @Override
    public boolean isExtraInputNeeded() {
        return false;
    }
}
