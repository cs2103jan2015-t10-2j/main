public class ExitCommandHandler implements ICommandHandler {

    private TaskHackerPro taskHackerPro;

    public ExitCommandHandler(TaskHackerPro taskHackerPro) {
        this.taskHackerPro = taskHackerPro;
    }

    @Override
    public boolean isValid(String command) {
        if (command.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean parseCommand(String command) {
        taskHackerPro.setContinue(false);
        System.out.println("Bye!");
        return true;
    }

}
