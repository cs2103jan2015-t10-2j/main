import java.io.IOException;

public class SaveCommandHandler implements ICommandHandler {

    private TaskData taskData;
    private DataManager dataManager;
    private String fileSavePath;
    private static final String FILE_SAVE = "File is saved";

    public SaveCommandHandler(TaskData taskData, DataManager dataManager,
            String fileSavePath) {
        this.taskData = taskData;
        this.dataManager = dataManager;
        this.fileSavePath = fileSavePath;
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
            dataManager.saveTaskDataToFile(fileSavePath, taskData);
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
}
