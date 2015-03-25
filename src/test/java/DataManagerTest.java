import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import org.junit.After;
<<<<<<< HEAD
import org.junit.Before;
=======
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
import org.junit.Test;

public class DataManagerTest {

<<<<<<< HEAD
    private final String testFilePath = "testDatManagerFile.dat";
    private DataManager dataManager;

    @Before
    public void setUp() {
        dataManager = new DataManager();
    }
=======
    private final String testFilePath = "bin/testDatManagerFile.dat";
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7

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

        try {
<<<<<<< HEAD
            dataManager.saveTaskDataToFile(testFilePath, taskDataToSave);
            TaskData taskDataToLoad = dataManager.loadTaskDataFromFile(testFilePath);

            assertEquals(taskDataToSave, taskDataToLoad);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

=======
            DataManager.getInstance().setPathToSaveLoad(testFilePath);
            DataManager.getInstance().saveTaskDataToFile(taskDataToSave);
            TaskData taskDataToLoad = DataManager.getInstance().loadTaskDataFromFile();

            assertEquals(taskDataToSave, taskDataToLoad);
        } catch (IOException e) {
            e.printStackTrace();
        }
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
    }

}
