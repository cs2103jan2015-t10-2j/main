import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddCommandHandler implements ICommandHandler {

    private TaskData taskData;
    private Pattern patternAddCommand;
    private Event event;

    private SimpleDateFormat timeFormat;

    private String addCommandFormat = "add at (?<time>.+) @ (?<location>.+) desc \"(?<description>.+)\"";
    private String timeFormatString = "h:m d/M/y";

    public AddCommandHandler(TaskData taskData) {
        this.taskData = taskData;

        patternAddCommand = Pattern.compile(addCommandFormat);
        timeFormat = new SimpleDateFormat(timeFormatString);
    }

    /*
     * add at [time] [date] @ [location] desc "[description]"
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
            if(!patternMatcher.matches()) {
                return false;
            }
        }

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
        boolean isExist = taskData.getEventMap().containsKey(event.getTaskID());

        if (isExist) {
            return false;
        } else {
            taskData.getEventMap().put(event.getTaskID(), event);
            return true;
        }
    }

    public Event getEvent() {
        return event;
    }
}
