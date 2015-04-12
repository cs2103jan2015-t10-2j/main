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

    //@author A0134704M
    public static void printLogo() {
        System.out.println();
        printSeparationLine(Color.GREEN);
        ConsoleUtility.printf(Color.RED, "\n████████╗ █████╗ ███████╗██╗  ██╗██╗  ██╗ █████╗  ██████╗██╗  ██╗███████╗██████╗ ██████╗ ██████╗ ██████╗ \n");
        ConsoleUtility.printf(Color.YELLOW, "╚══██╔══╝██╔══██╗██╔════╝██║ ██╔╝██║  ██║██╔══██╗██╔════╝██║ ██╔╝██╔════╝██╔══██╗██╔══██╗██╔══██╗██╔═══██╗\n");
        ConsoleUtility.printf(Color.WHITE, "   ██║   ███████║███████╗█████╔╝ ███████║███████║██║     █████╔╝ █████╗  ██████╔╝██████╔╝██████╔╝██║   ██║\n");
        ConsoleUtility.printf(Color.CYAN, "   ██║   ██╔══██║╚════██║██╔═██╗ ██╔══██║██╔══██║██║     ██╔═██╗ ██╔══╝  ██╔══██╗██╔═══╝ ██╔══██╗██║   ██║\n");
        ConsoleUtility.printf(Color.BLUE, "   ██║   ██║  ██║███████║██║  ██╗██║  ██║██║  ██║╚██████╗██║  ██╗███████╗██║  ██║██║     ██║  ██║╚██████╔╝\n");
        ConsoleUtility.printf(Color.MAGENTA, "   ╚═╝   ╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝╚═╝     ╚═╝  ╚═╝ ╚═════╝ \n");
        printSeparationLine(Color.GREEN);
        System.out.println();
    }

    //@author A0134704M
    public static void printSeparationLine(Color color) {
        ConsoleUtility.printf(color, "\n  ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ \n");
        ConsoleUtility.printf(color, " |___|___|___|___|___|___|___|___|___|___|___|___|___|___|___|___|___|___|___|___|___|___|___|___|___|___|\n\n");
    }
}
