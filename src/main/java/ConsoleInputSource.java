import java.io.InputStream;
import java.util.Scanner;

public class ConsoleInputSource implements IInputSource {

    private Scanner scanner;

    //@author A0134704M
    public ConsoleInputSource(InputStream is) {
        scanner = new Scanner(is);
    }

    //@author A0134704M
    @Override
    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    //@author A0134704M
    @Override
    public String getNextLine() {
        return scanner.nextLine();
    }

    //@author A0134704M
    @Override
    public void closeSource() {
        scanner.close();
    }
}
