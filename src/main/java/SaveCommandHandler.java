//import java.util.List;
//import java.lang.Exception;

//testing jerome 4 branch


public class SaveCommandHandler implements ICommandHandler {

    private TaskData taskData;

    public SaveCommandHandler(TaskData taskData) {
        this.taskData = taskData;
    }

    @Override
    public boolean parseCommand(String command) {

        if (!command.equalsIgnoreCase("save")) {
            return false;
        }
        
        System.out.println("Suceesfully in save class");

        return true;
    }

    @Override
    public boolean executeCommand() {

    
    	return true;
    }

    @Override
    public boolean isExtraInputNeeded() {
        return false;
    }
}