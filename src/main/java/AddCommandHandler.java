import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddCommandHandler implements ICommandHandler {
    private static final String addCommandName = "add";

    private static final String loggerInputCommand = "Input command - %s";

    private TaskData taskData;
    private Event event;

    private static final Logger logger = Logger.getGlobal();

    //@author A0134704M
    public AddCommandHandler(TaskData taskData) {
        assertObjectNotNull(taskData);
        this.taskData = taskData;
    }

    //@author A0134704M
    /*
     * add [name] at [time] [date] for [duration] mins @ [location] desc "[description]" setPrior [priority]
     * 
     * (non-Javadoc)
     * 
     * @see ICommandHandler#parseCommand(java.lang.String)
     */
    @Override
    public boolean parseCommand(String command) {

        logger.log(Level.INFO, String.format(loggerInputCommand, command));

        if (command.isEmpty()) {
            return false;
        } else {
            setEvent(command);
            return true;
        }
    }

    //@author A0134704M
    public void setEvent(String command) {
        event = CommandParser.getDetailFromCommand(addCommandName, command);
        assertObjectNotNull(event);
        event.setTaskID(getUniqueId());
    }

    //@author A0134704M
    @Override
    public ICommand getCommand() {
        ICommand addCommand = null;
        boolean isExist = taskData.getEventMap().containsKey(event.getTaskID());
        
        if (!isExist) {
            addCommand = new AddCommand(taskData, event);
        }        
        return addCommand;
    }

    //@author A0134704M
    @Override
    public boolean isExtraInputNeeded() {
        return false;
    }

    //@author UNKNOWN
    public int getUniqueId() {
        Random random = new Random();
        int returnVal;
        do {
            returnVal = random.nextInt(Integer.MAX_VALUE);
        } while (taskData.getEventMap().containsKey(returnVal));

        return returnVal;
    }

    //@author A0134704M
    public Event getEvent() {
        return event;
    }

    //@author A0109239A
    private void assertObjectNotNull(Object o) {
        assert (o != null);
    }
}
