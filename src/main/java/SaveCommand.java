public class SaveCommand implements ICommand {

    //@author A0134704M
    @Override
    public boolean execute() {
        return true;
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
        return false;
    }
}
