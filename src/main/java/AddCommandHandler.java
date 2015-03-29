import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddCommandHandler implements ICommandHandler {

    private static final String addCommandFormat = "add (?<name>.+) at (?<time>.+) for (?<duration>.+) mins @ (?<location>.+) desc \"(?<description>.+)\"";
    private static final String timeFormatString = "h:m d/M/y";
    private static final String dateFormat = "dd MMM, yyyy";

    private static final String messageDateFormat = "Date: %s\n";
    private static final String messageDescriptionFormat = "Description: %s\n";
    private static final String messageDurationFormat = "Duration: %d minutes\n";
    private static final String messageLocationFormat = "Location: %s\n";
    private static final String messageNameFormat = "%s\n";
    private static final String messageAddEventFormat = "Added this event:\n";

    private static final String loggerNumberOfEvents = "No. of events=%d";
    private static final String loggerParsedEvent = "Parsed event - ";
    private static final String loggerParseException = "Parse exception";
    private static final String loggerInputCommand = "Input command - %s";

    private static final String descriptionDelimiter = "description";
    private static final String locationDelimiter = "location";
    private static final String durationDelimiter = "duration";
    private static final String timeDelimiter = "time";
    private static final String nameDelimiter = "name";

    private TaskData taskData;
    private Event event;

    private String name;
    private String location;
    private String description;
    private Calendar taskDate;
    private int duration;

    private static final Pattern patternAddCommand;
    private static final SimpleDateFormat timeFormat;
    private static final Logger logger;

    static {
        patternAddCommand = Pattern.compile(addCommandFormat);
        timeFormat = new SimpleDateFormat(timeFormatString);
        logger = Logger.getLogger("AddCommandHandler");
    }

    public AddCommandHandler(TaskData taskData) {
        this.taskData = taskData;
        assertObjectNotNull(this);
    }

    /*
     * add [name] at [time] [date] for [duration] mins @ [location] desc "[description]"
     * 
     * (non-Javadoc)
     * 
     * @see ICommandHandler#parseCommand(java.lang.String)
     */
    @Override
    public boolean parseCommand(String command) {
        Matcher patternMatcher;

        logger.log(Level.INFO, String.format(loggerInputCommand, command));

        if (command.isEmpty()) {
            return false;
        } else {
            patternMatcher = patternAddCommand.matcher(command);
            if (!patternMatcher.matches()) {
                return false;
            }
        }
        assertObjectNotNull(this);
        this.name = patternMatcher.group(nameDelimiter);
        String time = patternMatcher.group(timeDelimiter);
        this.duration = Integer.parseInt(patternMatcher.group(durationDelimiter));
        this.location = patternMatcher.group(locationDelimiter);
        this.description = patternMatcher.group(descriptionDelimiter);
        this.taskDate = Calendar.getInstance();
        assertObjectNotNull(this);

        try {
            Date parsedDate = timeFormat.parse(time);
            taskDate.setTime(parsedDate);
        } catch (ParseException e) {
            logger.log(Level.INFO, loggerParseException, e);
            return false;
        }

        logger.log(Level.INFO, loggerParsedEvent + event);

        return true;
    }

    public void setEvent(String name, String location, String description,
            Calendar taskDate, int duration) {
        event = new Event();
        event.setTaskID(getUniqueId());
        event.setTaskName(name);
        event.setTaskLocation(location);
        event.setTaskDescription(description);
        event.setTaskDate(taskDate);
        event.setTaskDuration(duration);
        assertObjectNotNull(event);
    }

    private void printConfirmation(String name, String location, String description,
            Calendar taskDate, int duration) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        System.out.printf(messageAddEventFormat);
        System.out.printf(messageNameFormat, name);
        System.out.printf(messageLocationFormat, location);
        System.out.printf(messageDurationFormat, duration);
        System.out.printf(messageDescriptionFormat, description);
        System.out.printf(messageDateFormat, format.format(taskDate.getTime()));
    }

    @Override
    public boolean executeCommand() {
        assertObjectNotNull(this);
        setEvent(name, location, description, taskDate, duration);
        taskData.getEventMap().put(event.getTaskID(), event);
        printConfirmation(name, location, description, taskDate, duration);
        logger.log(Level.INFO,
                String.format(loggerNumberOfEvents, taskData.getEventMap().size()));
        return true;
    }

    @Override
    public boolean isExtraInputNeeded() {
        return false;
    }

    public int getUniqueId() {
        Random random = new Random();
        int returnVal;
        do {
            returnVal = random.nextInt(Integer.MAX_VALUE);
        } while (taskData.getEventMap().containsKey(returnVal));

        return returnVal;
    }

    public Event getEvent() {
        return event;
    }

    private void assertObjectNotNull(Object o) {
        assert (o != null);
    }
}