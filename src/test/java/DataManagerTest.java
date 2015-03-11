import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Calendar;

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
        Calendar c1 = Calendar.getInstance();
        c1.set(2012, 9, 14);

        eventToSave.setTaskID(23456);
        eventToSave.setTaskName("Task Name 1");
        eventToSave.setTaskDate(c1);
        eventToSave.setTaskPriority(TaskPriority.MEDIUM);
        eventToSave.setTaskLocation("Woodlands");
        eventToSave.setTaskDescription("be in Woodlands for dinner");
        taskDataToSave.getEventMap().put(eventToSave.getTaskID(), eventToSave);

        eventToSave = new Event();
        eventToSave.setTaskID(98765);
        eventToSave.setTaskName("Task Name2");
        eventToSave.setTaskPriority(TaskPriority.HIGH);
        eventToSave.setTaskLocation("IMM");
        eventToSave.setTaskDescription("Shopping in IMM");
        taskDataToSave.getEventMap().put(eventToSave.getTaskID(), eventToSave);

        dataManager.saveTaskDataToFile(testFilePath, taskDataToSave);
        TaskData taskDataToLoad = dataManager.loadTaskDataFromFile(testFilePath);

        assertEquals(taskDataToSave, taskDataToLoad);
    }

}
