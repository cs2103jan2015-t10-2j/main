import java.io.IOException;

public class SaveCommandHandler implements ICommandHandler {

    private TaskData taskData;
    private String fileSavePath;

    private static final String STRING_SAVE = "save";
    private static final String FILE_SAVE = "File is saved";

    //@author A0105886W
    public SaveCommandHandler(TaskData taskData) {
        this.taskData = taskData;
    }

    //@author A0105886W
    @Override
    public boolean parseCommand(String command) {
        if (command.equalsIgnoreCase(STRING_SAVE)) {
            return true;
        }
        return false;
    }

    //@author A0105886W
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

    //@author A0105886W
    @Override
    public boolean isExtraInputNeeded() {
        return false;
    }

    //@author A0134704M
    public void setFileSavePath(String fileSavePath) {
        this.fileSavePath = fileSavePath;
    }
}
