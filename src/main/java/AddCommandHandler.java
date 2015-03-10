
public class AddCommandHandler implements ICommandHandler {

    @Override
    public boolean isValid(String command) {
        if (command.isEmpty()) {
            return false;
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
    public boolean parseCommand(String command) {
        return true;
    }

}
