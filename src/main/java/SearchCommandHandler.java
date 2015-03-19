import java.util.List;
import java.lang.Exception;


public class SearchCommandHandler implements ICommandHandler {

    private String keyword;

    private TaskData taskData;

    public SearchCommandHandler(TaskData taskData) {
        this.taskData = taskData;
    }

    @Override
    public boolean parseCommand(String command) {
        int firstSpace = command.indexOf(' ');

        if (firstSpace < 0 || !command.matches("search (.+)")) {
            return false;
        }

        keyword = command.substring(firstSpace + 1);
        return true;
    }

    @Override
    public boolean executeCommand() {

        //Doing Exception
    	try{
    		
    		List<Integer> taskIds = taskData.searchByKeyword(keyword);
    		System.out.println("Your returned searched IDs of your requested keyword");
            for (Integer taskId : taskIds) {
                System.out.println(taskId);
            }
            return true;
    		
    	}
    	
    	catch (Exception e) {
    		System.out.println(e.getMessage());
    	}
    	return true;
    }

    @Override
    public boolean isExtraInputNeeded() {
        return false;
    }

}
