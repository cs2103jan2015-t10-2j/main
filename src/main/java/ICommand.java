public interface ICommand {
    //@author A0134704M
    public boolean execute();

    //@author A0134704M
    public boolean undo();

    //@author A0134704M
    public boolean redo();

    //@author A0134704M
    public boolean isReversible();
}
