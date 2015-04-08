public class SaveCommand implements ICommand {

    @Override
    public boolean execute() {
        return true;
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
        return false;
    }

}
