import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DataManager {

    public void saveTaskDataToFile(String filePath, TaskData taskData) throws IOException
             {
        FileOutputStream fos = new FileOutputStream(filePath);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        oos.writeObject(taskData);
        oos.close();
    }

    public TaskData loadTaskDataFromFile(String filePath) 
            throws ClassNotFoundException, IOException {
        FileInputStream fis = new FileInputStream(filePath);
        ObjectInputStream ois = new ObjectInputStream(fis);

        TaskData taskData = (TaskData) ois.readObject();
        ois.close();
        
        return taskData;
    }
}
