import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddCommandHandler implements ICommandHandler {

    private TaskData taskData;
    private Event event;

    private static final String addCommandFormat = "add (?<name>.+) at (?<time>.+) @ (?<location>.+) desc \"(?<description>.+)\"";
    private static final String timeFormatString = "h:m d/M/y";
    private static final Pattern patternAddCommand;
    private static final SimpleDateFormat timeFormat;

    static {
        patternAddCommand = Pattern.compile(addCommandFormat);
        timeFormat = new SimpleDateFormat(timeFormatString);
    }

    public AddCommandHandler(TaskData taskData) {
        this.taskData = taskData;
    }
    
    /*
     * add [name] at [time] [date] @ [location] desc "[description]"
     * 
     * (non-Javadoc)
     * 
     * @see ICommandHandler#parseCommand(java.lang.String)
     */
    @Override
    public boolean parseCommand(String command) {
        Matcher patternMatcher;

        if (command.isEmpty()) {
            return false;
        } else {
            patternMatcher = patternAddCommand.matcher(command);
            if (!patternMatcher.matches()) {
                return false;
            }
        }

        String name = patternMatcher.group("name");
        String time = patternMatcher.group("time");
        String location = patternMatcher.group("location");
        String description = patternMatcher.group("description");
        Calendar taskDate = Calendar.getInstance();

        try {
            Date parsedDate = timeFormat.parse(time);
            taskDate.setTime(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        event = new Event();
        event.setTaskID(getUniqueId());
        event.setTaskName(name);
        event.setTaskLocation(location);
        event.setTaskDescription(description);
        event.setTaskDate(taskDate);

        return true;
    }
    
    @Override
    public boolean executeCommand() {
        boolean isExist = taskData.getEventMap().containsKey(event.getTaskID());

        if (isExist) {
            return false;
        } else {
            taskData.getEventMap().put(event.getTaskID(), event);
            return true;
        }
    }

    @Override
    public boolean isExtraInputNeeded() {
        return false;
    }
    
    public int getUniqueId() {
        Random random = new Random();
        int returnVal = 0;
        
        do {
            returnVal = random.nextInt(Integer.MAX_VALUE);
        } while(taskData.getEventMap().containsKey(returnVal));
        
        return returnVal;
    }


    public Event getEvent() {
        return event;
    }
}
