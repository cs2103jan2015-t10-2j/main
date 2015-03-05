import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DataManager {

    public void saveEventToFile(String filePath, Event event) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(event);

            oos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Event loadEventFromFile(String filePath) {
        FileInputStream fis;
        try {
            fis = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fis);

            Event e = (Event) ois.readObject();
            ois.close();
            return e;
        } catch (IOException | ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        return null;
    }

}
