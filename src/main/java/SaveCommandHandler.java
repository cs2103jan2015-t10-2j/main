import java.io.IOException;

public class SaveCommandHandler implements ICommandHandler {

    private TaskData taskData;
    private String fileSavePath;
    private static final String FILE_SAVE = "File is saved";

    public SaveCommandHandler(TaskData taskData) {
        this.taskData = taskData;
    }

    @Override
    public boolean parseCommand(String command) {
        if (!command.equalsIgnoreCase("save")) {
            return false;
        }
        return true;
    }

    @Override
    public boolean executeCommand() {
        try {
            DataManager.getInstance().setPathToSaveLoad(fileSavePath);
            DataManager.getInstance().saveTaskDataToFile(taskData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(FILE_SAVE);
        return true;
    }

    @Override
    public boolean isExtraInputNeeded() {
        return false;
    }

    public void setFileSavePath(String fileSavePath) {
        this.fileSavePath = fileSavePath;
    }
}
