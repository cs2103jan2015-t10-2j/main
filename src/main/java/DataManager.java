import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DataManager {

    public void saveTaskDataToFile(String filePath, TaskData taskData) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(taskData);

            oos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TaskData loadTaskDataFromFile(String filePath) {
        FileInputStream fis;
        try {
            fis = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fis);

            TaskData taskData = (TaskData) ois.readObject();
            ois.close();
            return taskData;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
