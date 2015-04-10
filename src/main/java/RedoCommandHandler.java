import java.util.Map.Entry;
import java.util.Stack;
import java.util.logging.Logger;

public class RedoCommandHandler implements ICommandHandler {

    private Stack<Entry<ICommand, String>> undoStack;
    private Stack<Entry<ICommand, String>> redoStack;

    private static final Logger logger = Logger.getGlobal();
    private static final String STRING_REDO = "redo";
    private static final String MESSAGE_REDO_SUCCESSFULLY = "Redo successfully";

    //@author A0134704M
    public RedoCommandHandler(Stack<Entry<ICommand, String>> undoStack,
            Stack<Entry<ICommand, String>> redoStack) {
        this.undoStack = undoStack;
        this.redoStack = redoStack;
    }

    //@author A0134704M
    @Override
    public boolean parseCommand(String command) {
        if (!command.equalsIgnoreCase(STRING_REDO)) {
            return false;
        }
        return true;
    }

    //@author A0134704M
    @Override
    public ICommand getCommand() {
        if (redoStack.isEmpty()) {
            // Nothing to undo
        } else {
            while (!redoStack.isEmpty()) {
                Entry<ICommand, String> commandEntryToRedo = redoStack.pop();
                if (commandEntryToRedo.getKey().redo()) {
                    undoStack.push(commandEntryToRedo);
                    
                    System.out.println(MESSAGE_REDO_SUCCESSFULLY);
                    logger.info(String.format("undo: size=%d, redo: size=%d",
                            undoStack.size(), redoStack.size()));
                    return new NullCommand();
                }
            }
        }
        return null;
    }

    //@author A0134704M
    @Override
    public boolean isExtraInputNeeded() {
        return false;
    }
}
