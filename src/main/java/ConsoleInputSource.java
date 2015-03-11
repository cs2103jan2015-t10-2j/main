import java.io.InputStream;
import java.util.Scanner;

public class ConsoleInputSource implements IInputSource {

    private Scanner scanner;

    public ConsoleInputSource(InputStream is) {
        scanner = new Scanner(is);
    }

    @Override
    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    @Override
    public String getNextLine() {
        return scanner.nextLine();
    }

    @Override
    public void closeSource() {
        scanner.close();
    }    
}
