public class DoneCommand implements ICommand {

    private Event event;

    //@author A0134704M
    public DoneCommand(Event event) {
        this.event = event;
    }

    //@author A0134704M
    @Override
    public boolean execute() {
        if (event.isDone()) {
            return false;
        } else {
            event.setDone(true);
            return true;
        }
    }

    //@author A0134704M
    @Override
    public boolean undo() {
        return false;
    }

    //@author A0134704M
    @Override
    public boolean redo() {
        return false;
    }

    //@author A0134704M
    @Override
    public boolean isReversible() {
        return true;
    }
}
