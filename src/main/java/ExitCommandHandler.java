public class ExitCommandHandler implements ICommandHandler {

    private TaskHackerPro taskHackerPro;

    private static final String messageExiting = "Thank you for using TaskHackerPro!";
    private static final String exitDelimiter = "exit";

    //@author A0134704M
    public ExitCommandHandler(TaskHackerPro taskHackerPro) {
        assertObjectNotNull(this);
        this.taskHackerPro = taskHackerPro;
    }

    //@author A0134704M
    @Override
    public boolean parseCommand(String command) {
        if (exitDelimiter.equals(command)) {
            return true;
        }
        return false;
    }

    //@author A0134704M
    @Override
    public boolean isExtraInputNeeded() {
        return false;
    }

    //@author A0134704M
    @Override
    public ICommand getCommand() {
        assertObjectNotNull(taskHackerPro);
        taskHackerPro.setContinue(false);
        System.out.println(messageExiting);
        ICommand exitCommand = new NullCommand(); 
        return exitCommand;
    }

    //@author UNKNOWN
    private void assertObjectNotNull(Object o) {
        assert (o != null);
    }

}
