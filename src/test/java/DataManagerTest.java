import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DataManagerTest {

    private final String testFilePath = "testDatManagerFile.dat";
    private DataManager dataManager;

    @Before
    public void setUp() {
        dataManager = new DataManager();
    }

    @After
    public void tearDown() {
        File file = new File(testFilePath);
        file.delete();
    }

    @Test
    public void testSaveAndLoad() {
        TaskData taskDataToSave = new TaskData();
        Event eventToSave = new Event();

        eventToSave.setTaskID(23456);
        eventToSave.setTaskName("Task Name");
        taskDataToSave.getEventMap().put(eventToSave.getTaskID(), eventToSave);

        eventToSave = new Event();
        eventToSave.setTaskID(45678);
        eventToSave.setTaskName("Task Name 2");
        taskDataToSave.getEventMap().put(eventToSave.getTaskID(), eventToSave);

        dataManager.saveTaskDataToFile(testFilePath, taskDataToSave);
        TaskData taskDataToLoad = dataManager.loadTaskDataFromFile(testFilePath);

        assertEquals(taskDataToSave, taskDataToLoad);
    }

}
