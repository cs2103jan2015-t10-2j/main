
public class DoneCommandHandler implements ICommandHandler {

    private Event event;

    public DoneCommandHandler(Event event) {
        this.event = event;
    }
    
    @Override
    public boolean isValid(String command) {
        try {
            if (Integer.parseInt(command)==(event.getTaskID())){
                return true;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return false;
    }

    @Override
    public boolean parseCommand(String command) {
        return true;
    }

}
