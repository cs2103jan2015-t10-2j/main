import java.util.Arrays;
import java.util.concurrent.LinkedBlockingDeque;

public class StringInputSource implements IInputSource {

	private LinkedBlockingDeque<String> lines;
	
    private static final String stringSplitFormat = "[\\r\\n]+";

    public StringInputSource() {
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
