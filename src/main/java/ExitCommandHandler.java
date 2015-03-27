public class ExitCommandHandler implements ICommandHandler {

	private TaskHackerPro taskHackerPro;

    private static final String messageExiting = "Thank you for using TaskHackerPro!";
	private static final String exitDelimiter = "exit";
	
    public ExitCommandHandler(TaskHackerPro taskHackerPro) {
    	assertObjectNotNull(this);
        this.taskHackerPro = taskHackerPro;
    }

    @Override
    public boolean parseCommand(String command) {
        if (exitDelimiter.equals(command)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean executeCommand() {
    	assertObjectNotNull(taskHackerPro);
        taskHackerPro.setContinue(false);
        System.out.println(messageExiting);
        return true;
    }

    @Override
    public boolean isExtraInputNeeded() {
        return false;
    }
    
    private void assertObjectNotNull(Object o) {
		assert (o != null);
	}
}
