import java.util.Arrays;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;

public class StringInputSource implements IInputSource {

    private LinkedBlockingDeque<String> lines;
    private final Semaphore outputLinesAvailableMutex;

    private static final String stringSplitFormat = "[\\r\\n]+";

    public StringInputSource(Semaphore outputLinesAvailableMutex) {
        this.outputLinesAvailableMutex = outputLinesAvailableMutex;
        lines = new LinkedBlockingDeque<String>(Integer.MAX_VALUE);
    }

    public void addCommand(String s) {
        lines.addAll(Arrays.asList(s.split(stringSplitFormat)));
    }

    public void addLine(String[] inputs) {
        for (String input : inputs) {
            lines.addAll(Arrays.asList(input.split(stringSplitFormat)));
        }
    }

    @Override
    public boolean hasNextLine() {
        try {
            outputLinesAvailableMutex.release();
            lines.putFirst(lines.takeFirst());
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public String getNextLine() {
        try {
            return lines.takeFirst().trim();
        } catch (InterruptedException e) {
            return null;
        }
    }

    @Override
    public void closeSource() {
        lines.clear();
    }

}
