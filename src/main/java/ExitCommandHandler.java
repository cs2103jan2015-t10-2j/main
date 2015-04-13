public class ExitCommandHandler implements ICommandHandler {

    private TaskHackerPro taskHackerPro;

    private static final String MESSAGE_EXITING = "Thank you for using TaskHackerPro!";
    private static final String STRING_EXIT = "exit";

    //@author A0134704M
    public ExitCommandHandler(TaskHackerPro taskHackerPro) {
        assertObjectNotNull(this);
        this.taskHackerPro = taskHackerPro;
    }

    //@author A0134704M
    @Override
    public boolean parseCommand(String command) {
        if (STRING_EXIT.equalsIgnoreCase(command)) {
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
        System.out.println(MESSAGE_EXITING);
        ICommand exitCommand = new NullCommand(); 
        return exitCommand;
    }

    //@author A0109239A
    private void assertObjectNotNull(Object o) {
        assert (o != null);
    }

}
