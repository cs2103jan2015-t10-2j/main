import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringInputSource implements IInputSource {

    private List<String> lines;
    private int currentLine;

    public StringInputSource(String s) {
        lines = Arrays.asList(s.split("\\r?\\n"));
    }

    public StringInputSource(String[] inputs) {
        lines = new ArrayList<String>();
        for (String input : inputs) {
            lines.addAll(Arrays.asList(input.split("\\r?\\n")));
        }
    }

    @Override
    public boolean hasNextLine() {
        return currentLine < lines.size();
    }

    @Override
    public String getNextLine() {
        String returnString = lines.get(currentLine).trim();
        currentLine++;
        return returnString;
    }

    @Override
    public void closeSource() {
        lines.clear();
    }

}
