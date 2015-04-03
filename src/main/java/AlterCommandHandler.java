import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AlterCommandHandler implements ICommandHandler {

    private TaskData taskData;
    private int eventId;
    private int actualId;
    private String time;
    private String location;
    private String description;
    private Calendar taskDate;
    private int duration;
    private TaskPriority priority;

    private Event event;

    private static final String updateCommandFormat = "alter (?<eventID>[0-9]+?) as ?( time (?<time>.+?)?)?(len (?<duration>.+) mins)?( @ (?<location>.+?))?( desc \"(?<description>.+)\")?( setPrior (?<priority>.+))?$";
    private static final String timeFormatString = "h:m d/M/y";
    private static final String dateFormat = "dd MMM, yyyy";

    private static final Pattern patternUpdateCommand;
    private static final SimpleDateFormat timeFormat;

    private static final String messageDateFormat = "Date: %s\n";
    private static final String messageDescriptionFormat = "Description: %s\n";
    private static final String messageDurationFormat = "Duration: %d minutes\n";
    private static final String messageLocationFormat = "Location: %s\n";
    private static final String messagePriorityFormat = "Priority level: %s\n";
    private static final String messageUseDisplayFunction = "Please use \"display\" function to get the ID!";
    private static final String messageAfterMod = "\nAfter modification:\n";
    private static final String messageBeforeMod = "Before modification:\n";
    private static final String messageEditingFormat = "Editing task - %s\n";

    private static final String eventIDDelimiter = "eventID";
    private static final String descriptionDelimiter = "description";
    private static final String locationDelimiter = "location";
    private static final String durationDelimiter = "duration";
    private static final String timeDelimiter = "time";
    private static final String priorityDelimiter = "priority";

    static {
        patternUpdateCommand = Pattern.compile(updateCommandFormat);
        timeFormat = new SimpleDateFormat(timeFormatString);
    }

    public AlterCommandHandler(TaskData taskData) {
        assertObjectNotNull(this);
        assertObjectNotNull(taskData);
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
        setTaskDetails(patternMatcher);
        try {
            Date parsedDate = timeFormat.parse(time);
            taskDate.setTime(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void setTaskDetails(Matcher patternMatcher) {
        eventId = Integer.parseInt(patternMatcher.group(eventIDDelimiter));
        time = patternMatcher.group(timeDelimiter);
        duration = Integer.parseInt(patternMatcher.group(durationDelimiter));
        location = patternMatcher.group(locationDelimiter);
        description = patternMatcher.group(descriptionDelimiter);
        taskDate = Calendar.getInstance();
        priority = TaskPriority.valueOf(patternMatcher.group(priorityDelimiter));

    }

    @Override
    public boolean executeCommand() {

        assertObjectNotNull(this);

        try {
            actualId = taskData.getActualId(eventId);
        } catch (Exception NoSuchElementException) {
            System.out.println(messageUseDisplayFunction);
            return false;
        }
        if (eventAlreadyExists()) {
            event = extractMethod(actualId);
            printConfirmation(event, location, description, taskDate, duration, priority);
            setEventDetails();
            return true;
        } else {
            return false;
        }
    }

    private Event extractMethod(int actualId) {
        return taskData.getEventMap().get(actualId);
    }

    private boolean eventAlreadyExists() {
        return taskData.getEventMap().containsKey(actualId);
    }

    private void setEventDetails() {
        event.setTaskLocation(location);
        event.setTaskDescription(description);
        event.setTaskDate(taskDate);
        event.setTaskDuration(duration);
        event.setTaskPriority(priority);
    }

    private void printConfirmation(Event event, String location, String description,
            Calendar taskDate, int duration, TaskPriority priority) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        System.out.printf(messageEditingFormat, event.getTaskName());
        System.out.printf(messageBeforeMod);
        System.out.printf(messageDateFormat, format.format(event.getTaskDate().getTime()));
        System.out.printf(messageDurationFormat, event.getTaskDuration());
        System.out.printf(messageLocationFormat, event.getTaskLocation());
        System.out.printf(messageDescriptionFormat, event.getTaskDescription());
        System.out.printf(messageAfterMod);
        System.out.printf(messageDateFormat, format.format(taskDate.getTime()));
        System.out.printf(messageDurationFormat, duration);
        System.out.printf(messageLocationFormat, location);
        System.out.printf(messageDescriptionFormat, description);
        System.out.printf(messagePriorityFormat, priority.toString().toLowerCase());
    }

    @Override
    public boolean isExtraInputNeeded() {
        return false;
    }

    private void assertObjectNotNull(Object o) {
        assert (o != null);
    }
}
