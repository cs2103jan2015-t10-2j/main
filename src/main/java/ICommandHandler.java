public interface ICommandHandler {
    //@author A0134704M
    public boolean parseCommand(String command);

    //@author A0134704M
    public ICommand getCommand();

    //@author A0134704M
    public boolean isExtraInputNeeded();
}
