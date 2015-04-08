
public interface ICommand {
    public boolean execute();
    
    public boolean undo();
    
    public boolean redo();
}
