import java.io.IOException;

public class SaveCommandHandler implements ICommandHandler {

    private TaskData taskData;
    private String fileSavePath;

    private static final String saveDelimiter = "save";
    private static final String FILE_SAVE = "File is saved";

    //@author A0134704M
    public SaveCommandHandler(TaskData taskData) {
        this.taskData = taskData;
    }

    //@author A0134704M
    @Override
    public boolean parseCommand(String command) {
        if (!command.equalsIgnoreCase(saveDelimiter)) {
            return false;
        }
        return true;
    }

    //@author A0134704M
    @Override
    public ICommand getCommand() {
        ICommand saveCommand = new SaveCommand();
        try {
            DataManager.getInstance().setPathToSaveLoad(fileSavePath);
            DataManager.getInstance().saveTaskDataToFile(taskData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(FILE_SAVE);
        return saveCommand;
    }

    //@author A0134704M
    @Override
    public boolean isExtraInputNeeded() {
        return false;
    }

    //@author A0134704M
    public void setFileSavePath(String fileSavePath) {
        this.fileSavePath = fileSavePath;
    }
}
