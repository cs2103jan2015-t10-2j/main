import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class DataManager {

    private String pathToSaveLoad;
    private String pathToSaveHumanEditable;

    private static DataManager instance;
    private static final String DEFAULT_PATH_TO_LOAD_SAVE = "bin/TaskHackerPro.dat";
    private static final String DEFAULT_PATH_FOR_HUMAN_EDITABLE = "bin/TaskHackerPro.csv";

    private static final String MESSAGE_DATA_FILE_NOT_FOUND = "Data file is created\n";
    private static final String MESSAGE_DATA_FILE_LOADED = "Data file loaded successfully with %d event(s)!\n";
    private static final String MESSAGE_DATA_FILE_FAIL_TO_LOAD = "Data file cannot be loaded. New data file is created\n";

    //@author A0134704M
    private DataManager() {
    }

    //@author A0109239A
    //Writes all the task information to the desired location in the disk.
    public void saveTaskDataToFile(TaskData taskData) throws IOException {
        File file = setUpFile();
        writeObjectToDisk(taskData, file);
    }

    //Writes all the task information to the desired location in the disk in human-readable form.    
    public void saveAsCsvToDisk(TaskData taskData) throws IOException {
        List<String[]> allEventDetails = HumanReadable.getDetailsAllEvents(taskData);
        writeListToDisk(allEventDetails);
    }

    //Find the desired .csv file and returns its contents.
    public List<String[]> loadCSVFromDisk() throws IOException {
        List<String[]> allEventDetails = readListFromDisk();
        return allEventDetails;
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

    //@author A0109239A
    //[External library OpenCSV used] writes a given list of String arrays to a .csv file.
    private void writeListToDisk(List<String[]> allEventDetails) throws IOException {
        CSVWriter writer = setUpWriter();
        for (String[] entry : allEventDetails) {
            writer.writeNext(entry);
        }
        writer.close();
    }

    //[External library OpenCSV used] finds a .csv file and returns its contencts.
    private List<String[]> readListFromDisk() throws IOException {
        CSVReader reader = setUpReader();
        List<String[]> myEntries = reader.readAll();
        reader.close();
        return myEntries;
    }
   
    //Writes an object to disk. Not human readable.
    private void writeObjectToDisk(TaskData taskData, File file) throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(taskData);
        oos.close();
    }
    
    private CSVWriter setUpWriter() throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(getPathHumanReadable()));
        return writer;
    }
    
    private CSVReader setUpReader() throws IOException {
        CSVReader reader = new CSVReader(new FileReader(getPathHumanReadable()));
        return reader;
    }
    
    private File setUpFile() {
        File file = new File (getPathToSaveLoad());
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        return file;
    }
    
    //Figures out where the user wants to save.
    private String getPathHumanReadable() {
        if (pathToSaveHumanEditable == null) {
            return DEFAULT_PATH_FOR_HUMAN_EDITABLE;
        } else {
            return pathToSaveHumanEditable ;
        }
    }
    
    //@author A0134704M
    public String getPathToSaveLoad() {
        if (pathToSaveLoad == null) {
            return DEFAULT_PATH_TO_LOAD_SAVE;
        } else {
            return pathToSaveLoad;   
        }
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

    //@author A0134704M
    public void setPathToSaveHumanEditable(String pathToSaveHumanEditable) {
            this.pathToSaveHumanEditable = pathToSaveHumanEditable;
    }
}
