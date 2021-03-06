import java.util.Arrays;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;

public class StringInputSource implements IInputSource {

    private LinkedBlockingDeque<String> lines;
    private final Semaphore outputLinesAvailableMutex;

    private static final String stringSplitFormat = "[\\r\\n]+";

    //@author A0134704M
    public StringInputSource(Semaphore outputLinesAvailableMutex) {
        this.outputLinesAvailableMutex = outputLinesAvailableMutex;
        lines = new LinkedBlockingDeque<String>(Integer.MAX_VALUE);
    }

    //@author A0134704M
    public void addCommand(String s) {
        lines.addAll(Arrays.asList(s.split(stringSplitFormat)));
    }

    //@author A0134704M
    public void addLine(String[] inputs) {
        for (String input : inputs) {
            lines.addAll(Arrays.asList(input.split(stringSplitFormat)));
        }
    }

    //@author A0134704M
    @Override
    public boolean hasNextLine() {
        try {
            // Notify observers only when all commands are executed
            if (lines.size() <= 0) {
                outputLinesAvailableMutex.release();
                System.out.printf(TaskHackerPro.MESSAGE_COMMAND_PROMPT);
            }

            lines.putFirst(lines.takeFirst());
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

    //@author A0134704M
    @Override
    public String getNextLine() {
        try {
            return lines.takeFirst().trim();
        } catch (InterruptedException e) {
            return null;
        }
    }

    //@author A0134704M
    @Override
    public void closeSource() {
        lines.clear();
    }

}
