import java.util.Stack;
import java.util.Map.Entry;

public class HistoryCommandHandler implements ICommandHandler {

    private Stack<Entry<ICommand, String>> undoStack;
    private Stack<Entry<ICommand, String>> redoStack;

    public HistoryCommandHandler(Stack<Entry<ICommand, String>> undoStack,
            Stack<Entry<ICommand, String>> redoStack) {
        this.undoStack = undoStack;
        this.redoStack = redoStack;
    }

    @Override
    public boolean parseCommand(String command) {
        return "history".equalsIgnoreCase(command);
    }

    @Override
    public ICommand getCommand() {

        for (Entry<ICommand, String> undoEntry : undoStack) {
            System.out.printf("--  %s\n", undoEntry.getValue());
        }

        System.out.println("=== Current ===");

        for (int i=redoStack.size()-1; i>=0; i--) {
            Entry<ICommand, String> redoEntry = redoStack.get(i);
            System.out.printf("++  %s\n", redoEntry.getValue());
        }

        return new NullCommand();
    }

    @Override
    public boolean isExtraInputNeeded() {
        // TODO Auto-generated method stub
        return false;
    }

}
