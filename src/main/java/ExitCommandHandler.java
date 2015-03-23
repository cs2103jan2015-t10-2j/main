public class ExitCommandHandler implements ICommandHandler {

    private TaskHackerPro taskHackerPro;

    public ExitCommandHandler(TaskHackerPro taskHackerPro) {
    	assertObjectNotNull(this);
        this.taskHackerPro = taskHackerPro;
    }

    @Override
    public boolean parseCommand(String command) {
        if ("exit".equals(command)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean executeCommand() {
    	assertObjectNotNull(taskHackerPro);
        taskHackerPro.setContinue(false);
        System.out.println("Bye!");
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
