import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

public class ConsoleUtility {

    //@author A0134704M
    public static void clearScreen() {
        AnsiConsole.systemInstall();
        System.out.print(Ansi.ansi().eraseScreen());
        AnsiConsole.systemUninstall();
    }
}
