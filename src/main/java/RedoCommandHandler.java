import java.util.Stack;


public class RedoCommandHandler implements ICommandHandler {

    private Stack<ICommand> undoStack;
    private Stack<ICommand> redoStack;
    
    private static final String STRING_REDO = "redo";

    public RedoCommandHandler(Stack<ICommand> undoStack, Stack<ICommand> redoStack) {
        super();
        this.undoStack = undoStack;
        this.redoStack = redoStack;
    }

    @Override
    public boolean parseCommand(String command) {
        if (!command.equalsIgnoreCase(STRING_REDO)) {
            return false;
        }
        return true;
    }

    @Override
    public ICommand getCommand() {
        if (redoStack.isEmpty()) {
            // Nothing to undo
        } else {
            while (!redoStack.isEmpty()) {
                ICommand commandToRedo = redoStack.pop();
                if (commandToRedo.redo()) {
                    undoStack.push(commandToRedo);
                    return new NullCommand();
                }
            }
        }
        return null;
    }

    @Override
    public boolean isExtraInputNeeded() {
        return false;
    }

}
