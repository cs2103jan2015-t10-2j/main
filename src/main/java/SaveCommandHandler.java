import java.io.IOException;

public class SaveCommandHandler implements ICommandHandler {

    private TaskData taskData;
<<<<<<< HEAD
    private DataManager dataManager;
    private String fileSavePath;
    private static final String FILE_SAVE = "File is saved";

    public SaveCommandHandler(TaskData taskData, DataManager dataManager,
            String fileSavePath) {
        this.taskData = taskData;
        this.dataManager = dataManager;
        this.fileSavePath = fileSavePath;
=======
    private String fileSavePath;
    private static final String FILE_SAVE = "File is saved";

    public SaveCommandHandler(TaskData taskData) {
        this.taskData = taskData;
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
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
<<<<<<< HEAD
            dataManager.saveTaskDataToFile(fileSavePath, taskData);
=======
            DataManager.getInstance().setPathToSaveLoad(fileSavePath);
            DataManager.getInstance().saveTaskDataToFile(taskData);
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
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
<<<<<<< HEAD
=======

    public void setFileSavePath(String fileSavePath) {
        this.fileSavePath = fileSavePath;
    }
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
}
