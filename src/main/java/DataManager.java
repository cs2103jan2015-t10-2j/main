import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DataManager {

    private String pathToSaveLoad;

    private static DataManager instance;
    private static final String DEFAULT_PATH_TO_LOAD_SAVE = "bin/TaskHackerPro.dat";

    private static final String MESSAGE_DATA_FILE_NOT_FOUND = "Data file is created\n";
    private static final String MESSAGE_DATA_FILE_LOADED = "Data file loaded successfully with %d event(s)!\n";
    private static final String MESSAGE_DATA_FILE_FAIL_TO_LOAD = "Data file cannot be loaded. New data file is created\n";

    //@author A0134704M
    private DataManager() {

    }

    //@author A0134704M
    public void saveTaskDataToFile(TaskData taskData) throws IOException {
        FileOutputStream fos = null;
        File file;
        if (pathToSaveLoad == null) {
            file = new File(DEFAULT_PATH_TO_LOAD_SAVE);
        } else {
            file = new File(pathToSaveLoad);
        }

        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(taskData);
        oos.close();
    }

    //@author A0134704M
    public TaskData loadTaskDataFromFile() {
        FileInputStream fis;
        TaskData taskData = null;

        try {
            if (pathToSaveLoad == null) {
                fis = new FileInputStream(DEFAULT_PATH_TO_LOAD_SAVE);
            } else {
                fis = new FileInputStream(pathToSaveLoad);
            }

            ObjectInputStream ois = new ObjectInputStream(fis);
            taskData = (TaskData) ois.readObject();
            System.out.printf(MESSAGE_DATA_FILE_LOADED, taskData.getEventMap().size());

            ois.close();
        } catch (IOException e) {
            System.out.printf(MESSAGE_DATA_FILE_NOT_FOUND);
        } catch (ClassNotFoundException e) {
            System.out.printf(MESSAGE_DATA_FILE_FAIL_TO_LOAD);
        }

        return taskData;
    }

    //@author A0134704M
    public String getPathToSaveLoad() {
        return pathToSaveLoad;
    }

    //@author A0134704M
    public void setPathToSaveLoad(String pathToSaveLoad) {
        this.pathToSaveLoad = pathToSaveLoad;
    }

    //@author A0134704M
    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }
}
