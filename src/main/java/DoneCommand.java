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
        return false;
    }

    @Override
    public boolean redo() {
        return false;
    }

    @Override
    public boolean isReversible() {
        return true;
    }

}
