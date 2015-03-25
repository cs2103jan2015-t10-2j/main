import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DataManager {

<<<<<<< HEAD
    public void saveTaskDataToFile(String filePath, TaskData taskData) throws IOException
             {
        FileOutputStream fos = new FileOutputStream(filePath);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

=======
    private String pathToSaveLoad;

    private static DataManager instance;
    private static final String DEFAULT_PATH_TO_LOAD_SAVE = "bin/TaskHackerPro.dat";

    private static final String MESSAGE_DATA_FILE_NOT_FOUND = "Data file is created\n";
    private static final String MESSAGE_DATA_FILE_LOADED = "Data file loaded successfully with %d event(s)!\n";
    private static final String MESSAGE_DATA_FILE_FAIL_TO_LOAD = "Data file cannot be loaded. New data file is created\n";

    private DataManager() {

    }

    public void saveTaskDataToFile(TaskData taskData) throws IOException {
        FileOutputStream fos = null;

        if (pathToSaveLoad == null) {
            fos = new FileOutputStream(DEFAULT_PATH_TO_LOAD_SAVE);
        } else {
            fos = new FileOutputStream(pathToSaveLoad);
        }

        ObjectOutputStream oos = new ObjectOutputStream(fos);
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
        oos.writeObject(taskData);
        oos.close();
    }

<<<<<<< HEAD
    public TaskData loadTaskDataFromFile(String filePath) 
            throws ClassNotFoundException, IOException {
        FileInputStream fis = new FileInputStream(filePath);
        ObjectInputStream ois = new ObjectInputStream(fis);

        TaskData taskData = (TaskData) ois.readObject();
        ois.close();
        
        return taskData;
    }
=======
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

    public String getPathToSaveLoad() {
        return pathToSaveLoad;
    }

    public void setPathToSaveLoad(String pathToSaveLoad) {
        this.pathToSaveLoad = pathToSaveLoad;
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
}
