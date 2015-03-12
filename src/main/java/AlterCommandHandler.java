import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AlterCommandHandler implements ICommandHandler {

    private TaskData taskData;
    private int eventId;
    private String time;
    private String location;
    private String description;
    private Calendar taskDate;

    // for V0.2, we shall require that users pass us all the fields again.
    private static final String updateCommandFormat = "alter (?<eventID>[0-9]+) as (?<time>.+) @ (?<location>.+) desc \"(?<description>.+)\"";
    private static final String timeFormatString = "h:m d/M/y";
    private static final Pattern patternUpdateCommand;
    private static final SimpleDateFormat timeFormat;

    static {
        patternUpdateCommand = Pattern.compile(updateCommandFormat);
        timeFormat = new SimpleDateFormat(timeFormatString);
    }

    public AlterCommandHandler(TaskData taskData) {
        this.taskData = taskData;
    }

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

        eventId = Integer.parseInt(patternMatcher.group("eventID"));
        time = patternMatcher.group("time");
        location = patternMatcher.group("location");
        description = patternMatcher.group("description");
        taskDate = Calendar.getInstance();

        try {
            Date parsedDate = timeFormat.parse(time);
            taskDate.setTime(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean executeCommand() {
        boolean isExist = taskData.getEventMap().containsKey(eventId);

        if (isExist) {
            return false;
        } else {
            Event event = taskData.getEventMap().get(eventId);
            event.setTaskLocation(location);
            event.setTaskDescription(description);
            event.setTaskDate(taskDate);
            return true;
        }
    }

    @Override
    public boolean isExtraInputNeeded() {
        return false;
    }
}