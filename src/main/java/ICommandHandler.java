public interface ICommandHandler {
    public boolean parseCommand(String command);

    public boolean executeCommand();

    public boolean isCommandReady();

    public boolean isExtraInputNeeded();
}
