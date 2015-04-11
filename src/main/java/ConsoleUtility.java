import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Color;
import org.fusesource.jansi.AnsiConsole;

public class ConsoleUtility {

    //@author A0134704M
    public static void clearScreen() {
        AnsiConsole.systemInstall();
        System.out.print(Ansi.ansi().eraseScreen());
        AnsiConsole.systemUninstall();
    }
    
    //@author A0134704M
    public static void printf(Color color, String format, Object... args) {
        AnsiConsole.systemInstall();
        System.out.print(Ansi.ansi().fgBright(color));
        System.out.printf(format, args);
        System.out.print(Ansi.ansi().reset());
        AnsiConsole.systemUninstall();
    }
}
