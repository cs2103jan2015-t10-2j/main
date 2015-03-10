import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.EOFException;
import java.io.File;
import java.util.ArrayList;



public class DataManager {
	
	//save events one by one to file
    public void saveEventToFile(String filePath, Event event) {
    	   File file = new File(filePath);
           ObjectOutputStream out = null;
    	
    	try {
    		if(!file.exists()){
    			out = new ObjectOutputStream (new FileOutputStream(filePath));
    		}
    		else{
    			out = new  AppendableObjectOutputStream(new FileOutputStream(filePath, true));
    		}
    			out.writeObject(event);
    			out.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    	finally{
    		try{
    			if(out != null)
    				out.close();
    		}
    			catch (Exception e){
    				e.printStackTrace();
    		}
    	}
    }
   
    public Event getEventFromFile(String filePath, int taskID){
    	File file = new File(filePath);
    	Event event;
    	
    	if(file.exists()){
    		ObjectInputStream ois = null;
    		try{
    			ois = new ObjectInputStream(new FileInputStream(filePath));
    			while(true){
    				event = (Event)ois.readObject();
    				if(taskID == event.getTaskID()){
    					return event;
    				}
    			}
    			
    		}catch (EOFException e){
    			
    		}catch(Exception e){
    		e.printStackTrace();
    		}
    		finally{
    			try{
        			if(ois != null){
        				ois.close();
        			}
        		}catch(IOException e){
        			e.printStackTrace();
        		}
        	}
    			
    		}
    		return null;
    	}
    
    
    
    public ArrayList<Integer> searchKeyWords(String filePath, String keyWord) {
        Event event;
    	File file = new File(filePath);
        ArrayList<Integer> taskIDs = new ArrayList<Integer>();
        
        if(file.exists()){
        	ObjectInputStream ois = null;
        	try{
        		ois = new ObjectInputStream(new FileInputStream(filePath));
        		while(true){
        			event = (Event)ois.readObject();
        			if(hasKeyWord(event, keyWord))
        				taskIDs.add(event.getTaskID());
        		}
        	}catch (EOFException e){
        		
        	}
        	catch (Exception e){
        		e.printStackTrace();
        	}
        	finally{
        		try{
        			if(ois != null){
        				ois.close();
        			}
        		}catch(IOException e){
        			e.printStackTrace();
        		}
        	}
        }
        return taskIDs;
	}
    
    public boolean hasKeyWord(Event event, String keyWord){
    	
    	if(event == null || keyWord == null)
    		return false;
    	
    	
    	if(Integer.toString(event.getTaskID()) != null && Integer.toString(event.getTaskID()).contains(keyWord)) {
    		return true;
    	}
    	
    	else if(event.getTaskName() != null && event.getTaskName().contains(keyWord)) {
    		return true;
    	}
    	
    	else if(event.getTaskLocation() != null && event.getTaskLocation().contains(keyWord)) {
    		return true;
    	}
    	
    	else if(event.getTaskDescription() != null && event.getTaskDescription().contains(keyWord)) {
    		return true;
    	}
    	
    	else if(event.getTaskDate() != null && event.getTaskDate().getTime().toString().contains(keyWord)) {
    		return true;
    	}
    	else if (event.getTaskPriority() != null && event.getTaskPriority().toString().contains(keyWord)) {
				return true;
    	}
    	
    	return false;
    }
   
    

	public ArrayList<Event> loadEventsFromFile(String filePath) {
        File file = new File(filePath);
        ArrayList<Event> events = new ArrayList<Event>();
        
        if(file.exists()){
        	ObjectInputStream ois = null;
        	try{
        		ois = new ObjectInputStream(new FileInputStream(filePath));
        		while(true){
        			events.add((Event)ois.readObject());
        		}
        	}catch (EOFException e){
        		
        	}
        	catch (Exception e){
        		e.printStackTrace();
        	}
        	finally{
        		try{
        			if(ois != null){
        				ois.close();
        			}
        		}catch(IOException e){
        			e.printStackTrace();
        		}
        	}
        }
        return events;
	}
}
