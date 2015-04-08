import java.util.Stack;


public class UndoCommandHandler implements ICommandHandler {

    private Stack<ICommand> undoStack;
    private Stack<ICommand> redoStack;
    
    private static final String STRING_UNDO = "undo";
    
  

    public UndoCommandHandler(Stack<ICommand> undoStack, Stack<ICommand> redoStack) {
        this.undoStack = undoStack;
        this.redoStack = redoStack;
    }

    @Override
    public boolean parseCommand(String command) {
        if (!command.equalsIgnoreCase(STRING_UNDO)) {
            return false;
        }
        return true;
    }

    @Override
    public ICommand getCommand() {        
        if (undoStack.isEmpty()) {
            // Nothing to undo
        } else {
            while (!undoStack.isEmpty()) {
                ICommand commandToUndo = undoStack.pop();
                if (commandToUndo.undo()) {
                    redoStack.push(commandToUndo);
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