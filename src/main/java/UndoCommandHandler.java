import java.util.Map.Entry;
import java.util.Stack;
import java.util.logging.Logger;

public class UndoCommandHandler implements ICommandHandler {

    private Stack<Entry<ICommand, String>> undoStack;
    private Stack<Entry<ICommand, String>> redoStack;

    private static final Logger logger = Logger.getGlobal();
    private static final String STRING_UNDO = "undo";
    private static final String MESSAGE_UNDO_SUCCESSFULLY = "Undo successfully!\n";
    private static final String MESSAGE_NOTHING_TO_UNDO = "Nothing to undo.\n";

    //@author A0134704M
    public UndoCommandHandler(Stack<Entry<ICommand, String>> undoStack,
            Stack<Entry<ICommand, String>> redoStack) {
        this.undoStack = undoStack;
        this.redoStack = redoStack;
    }

    //@author A0134704M
    @Override
    public boolean parseCommand(String command) {
        if (!command.equalsIgnoreCase(STRING_UNDO)) {
            return false;
        }
        return true;
    }

    //@author A0134704M
    @Override
    public ICommand getCommand() {
        ICommand command = null;

        if (undoStack.isEmpty()) {
            System.out.printf(MESSAGE_NOTHING_TO_UNDO);
        } else {
            while (!undoStack.isEmpty()) {
                Entry<ICommand, String> commandEntryToUndo = undoStack.pop();
                String rawCommand = commandEntryToUndo.getValue();

                System.out.println(rawCommand);
                if (commandEntryToUndo.getKey().undo()) {
                    if (commandEntryToUndo.getKey().isReversible()) {
                        redoStack.push(commandEntryToUndo);
                    }

                    System.out.printf(MESSAGE_UNDO_SUCCESSFULLY);
                    command = new NullCommand();
                    break;
                }
            }
        }

        logger.info(String.format("undo: size=%d, redo: size=%d", undoStack.size(),
                redoStack.size()));
        return command;
    }

    //@author A0134704M
    @Override
    public boolean isExtraInputNeeded() {
        return false;
    }

}
