import java.util.ArrayList;

public class SearchCommandHandler implements ICommandHandler {

	//public static  ArrayList<Integer> keyWordTaskIds = new ArrayList<Integer>();
	
	 @Override
	    public boolean isValid(String command) {
	        if (command.isEmpty()) {
	            return false;
	        }
	        
	        else{
	        	DataManager dataManager = new DataManager();
	        	TaskHackerPro taskHackerPro = new TaskHackerPro();
	        	
	        	ArrayList<Integer> IdSearchedResults = new ArrayList<Integer>();
	        	
	        	IdSearchedResults = dataManager.searchKeyWords(taskHackerPro.getFileName(), command);
	        	printIdSearchedResults(IdSearchedResults);
	            
	        }
	        return true;
	    }
	 
	 	public void printIdSearchedResults(ArrayList<Integer> IdSearchedResults){
	 		
	 		System.out.println("Your returned searched IDs of your requested keyword");
            for(int i = 0; i < IdSearchedResults.size(); i++){
            	System.out.println(IdSearchedResults.get(i));
            }
	 	}

	    @Override
	    public void parseCommand(String command) {
	        System.out.println(command);
	    }
}
