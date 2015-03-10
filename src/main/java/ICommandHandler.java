
public interface ICommandHandler {
    public boolean isValid(String command);
    public boolean parseCommand(String command);
}
