
public class NullCommand implements ICommand{

    @Override
    public boolean execute() {
        return true;
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
