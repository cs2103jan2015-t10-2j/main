
public class DoneCommand implements ICommand {

    private Event event;
    
    public DoneCommand(Event event) {
        this.event = event;
    }
    
    @Override
    public boolean execute() {
        if (event.isDone()) {
            return false;
        } else {
            event.setDone(true);
            return true;
        }
        
    }

    @Override
    public boolean undo() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean redo() {
        // TODO Auto-generated method stub
        return false;
    }

}
