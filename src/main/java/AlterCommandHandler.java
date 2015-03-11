import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateCommandHandler implements ICommandHandler {

    private TaskData taskData;
    private Event event;

    // for V0.2, we shall require that users pass us all the fields again.
    private static final String updateCommandFormat = "alter (?<eventID>.+) as (?<time>.+) @ (?<location>.+) desc \"(?<description>.+)\"";
    private static final String timeFormatString = "h:m d/M/y";
    private static final Pattern patternUpdateCommand;
    private static final SimpleDateFormat timeFormat;

    static {
        patternUpdateCommand = Pattern.compile(updateCommandFormat);
        timeFormat = new SimpleDateFormat(timeFormatString);
    }

    public UpdateCommandHandler(TaskData taskData) {
        this.taskData = taskData;
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
            patternMatcher = patternUpdateCommand.matcher(command);
            if (!patternMatcher.matches()) {
                return false;
            }
        }

        int eventID = patternMatcher.group("eventID");
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

        event = getEventFromID(eventID); //retieving the event
        event.setTaskLocation(location);
        event.setTaskDescription(description);
        event.setTaskDate(taskDate);

        return true;
    }

    public Event getEventFromID(int eventID) {
        String filePath = getFilePathFromID(eventID); //I need help with this function!!!
        TaskData taskData = DataManager.loadTaskDataFromFile(filePath);
        Event myEvent = getEventFromTaskData(taskData); //I don't quite understand how this works, but I believe it's simple retrieval...
        return myEvent;
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
