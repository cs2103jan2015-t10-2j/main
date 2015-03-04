
public interface ICommandHandler {
    public boolean isValid(String command);
    public void parseCommand(String command);
}
