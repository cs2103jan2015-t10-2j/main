<<<<<<< HEAD
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
=======
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingDeque;

public class StringInputSource implements IInputSource {

    private LinkedBlockingDeque<String> lines;

    public StringInputSource() {
        lines = new LinkedBlockingDeque<String>(Integer.MAX_VALUE);
    }

    public void addCommand(String s) {
        lines.addAll(Arrays.asList(s.split("[\\r\\n]+")));
    }

    public void addLine(String[] inputs) {
        for (String input : inputs) {
            lines.addAll(Arrays.asList(input.split("[\\r\\n]+")));
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
        }
    }

    @Override
    public boolean hasNextLine() {
<<<<<<< HEAD
        return currentLine < lines.size();
=======
        try {
            lines.putFirst(lines.takeFirst());
            return true;
        } catch (InterruptedException e) {
            return false;
        }
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
    }

    @Override
    public String getNextLine() {
<<<<<<< HEAD
        String returnString = lines.get(currentLine).trim();
        currentLine++;
        return returnString;
=======
        try {
            return lines.takeFirst().trim();
        } catch (InterruptedException e) {
            return null;
        }
>>>>>>> 62921b25f52b056ebc85bf70b983ffab11e44fb7
    }

    @Override
    public void closeSource() {
        lines.clear();
    }

}
