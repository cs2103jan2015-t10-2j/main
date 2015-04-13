import java.io.InputStream;
import java.util.Scanner;

public class ConsoleInputSource implements IInputSource {

    private Scanner scanner;
    private String addCommand = null;

    //@author A0134704M
    public ConsoleInputSource(InputStream is) {
        scanner = new Scanner(is);
    }

    //@author A0134704M
    @Override
    public boolean hasNextLine() {
        if(addCommand != null){
            return true;
        }
        
        System.out.printf(TaskHackerPro.MESSAGE_COMMAND_PROMPT);
        return scanner.hasNextLine();
    }

    //@author A0134704M
    @Override
    public String getNextLine() {
        if(addCommand != null){
            String tempString = addCommand;
            addCommand = null;
            return tempString;
        }
        
        return scanner.nextLine();
    }
    
    //@author A0105886W
    public void addCommand(String s){
        this.addCommand = s; 
    }

    //@author A0134704M
    @Override
    public void closeSource() {
        scanner.close();
    }
}
