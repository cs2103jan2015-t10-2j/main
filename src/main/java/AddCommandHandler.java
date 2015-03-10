import java.util.Random;

public class AddCommandHandler implements ICommandHandler {

    private static final TaskPriority DEFAULT_PRIORITY = MEDIUM;
    private static final boolean DEFAULT_RECURRING = false;
    private static final boolean DEFAULT_DONE = false;

    @Override
    public boolean isValid(String command) {
        if (command.isEmpty()) {
            return false;
        }
        return true;
    }

    /*
     * This method just accepts details from a parser and uses the inbuilt functions from Event to set all the fields.
     * Some defaults and silly stubs are used right now; will improve in V0.2.
     */    
    public Event addEventDateTimeLocDesc (String taskName, Calendar taskDate, String taskLocation, String taskDescription) {
        private Event event;
        event.setTaskID(generateID());
        event.setTaskName(taskName);
        event.setTaskDate(taskDate);
        event.setTaskLocation(taskLocation);
        event.setTaskDescription(taskDescription);
        event.setTaskPriority(DEFAULT_PRIORITY);
        event.setDone(DEFAULT_DONE);
        event.setRecurring(DEFAULT_RECURRING);
        return event; //[URGENT] Should probably find a way to pass this to DataManager and store it right away. Generating filepath?
    }

    /*
     * Returns a 5-digit long integer ID.
     */
    public static int generateID () {
        return randInt(10000, 99999);
    }

    /*
     * Returns a random number between two supplied limits.
     */
    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    /* 
     * add at [time] [date] @ [location] desc "[decription]"
     * 
     * (non-Javadoc)
     * @see ICommandHandler#parseCommand(java.lang.String)
     */
    @Override
    public void parseCommand(String command) {
        System.out.println(command);
    }

}
