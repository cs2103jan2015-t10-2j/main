
//import java.util.*;

public class AddCommandHandler implements ICommandHandler {
	
	//public static final ArrayList<Event> events = new ArrayList<Event>();

    @Override
    public boolean isValid(String command) {
        if (command.isEmpty()) {
            return false;
        }
        else{
        	
        }
        return true;
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