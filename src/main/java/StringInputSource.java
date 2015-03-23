import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;

public class StringInputSource implements IInputSource {

    private ArrayBlockingQueue<String> lines;

    public StringInputSource() {
        lines = new ArrayBlockingQueue<String>(Integer.MAX_VALUE);
    }

    public void addLine(String s) {
        lines.addAll(Arrays.asList(s.split("\\r?\\n")));
    }

    public void addLine(String[] inputs) {
        for (String input : inputs) {
            lines.addAll(Arrays.asList(input.split("\\r?\\n")));
        }
    }

    @Override
    public boolean hasNextLine() {
        return lines.size() > 0;
    }

    @Override
    public String getNextLine() {
        return lines.poll().trim();
    }

    @Override
    public void closeSource() {
        lines.clear();
    }

}
