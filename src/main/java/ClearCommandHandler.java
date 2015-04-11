
public class ClearCommandHandler implements ICommandHandler {

    private static final String STRING_CLEAR = "clear";

    //@author A0134704M
    @Override
    public boolean parseCommand(String command) {
        if (STRING_CLEAR.equalsIgnoreCase(command)) {
            return true;
        } else {
            return false;
        }
    }

    //@author A0134704M
    @Override
    public ICommand getCommand() {
        ConsoleUtility.clearScreen();;
        ICommand clearCommand = new NullCommand(); 
        return clearCommand;
    }

    //@author A0134704M
    @Override
    public boolean isExtraInputNeeded() {
        return false;
    }

}
