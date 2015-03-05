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
        Event eventToSave = new Event();
        eventToSave.setTaskID(23456);
        eventToSave.setTaskName("Task Name");

        dataManager.saveEventToFile(testFilePath, eventToSave);
        Event eventLoaded = dataManager.loadEventFromFile(testFilePath);
        
        assertEquals(eventToSave, eventLoaded);
    }

}
