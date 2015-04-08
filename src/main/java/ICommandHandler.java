public interface ICommandHandler {
    public boolean parseCommand(String command);

    public ICommand getCommand();

    public boolean isExtraInputNeeded();
}
