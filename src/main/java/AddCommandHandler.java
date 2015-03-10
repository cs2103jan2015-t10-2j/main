import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AddCommandHandler implements ICommandHandler {

    private Event event;
    private Pattern patternAddCommand;
    private Matcher patternMatcher;
    private SimpleDateFormat timeFormat;
    
    private String addCommandFormat = "add at (?<time>.+) @ (?<location>.+) desc \"(?<description>.+)\"";
    private String timeFormatString = "h:m d/M/y";
    
    public AddCommandHandler() {
        patternAddCommand = Pattern.compile(addCommandFormat);
        timeFormat = new SimpleDateFormat(timeFormatString);
    }
    
    @Override
    public boolean isValid(String command) {
        if (command.isEmpty()) {
            return false;
        } else {
            patternMatcher = patternAddCommand.matcher(command);
            return patternMatcher.matches();
        }
    }

    /* 
     * add at [time] [date] @ [location] desc "[description]"
     * 
     * (non-Javadoc)
     * @see ICommandHandler#parseCommand(java.lang.String)
     */
    @Override
    public boolean parseCommand(String command) {
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
        event.setTaskLocation(location);
        event.setTaskDescription(description);
        event.setTaskDate(taskDate);
        
        return true;
    }

    @Override
    public boolean executeCommand() {
        return false;
    }

    public Event getEvent() {
        return event;
    }
}
