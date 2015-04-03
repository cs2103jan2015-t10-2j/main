import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AlterCommandHandler implements ICommandHandler {

    private TaskData taskData;
    private Event oldEvent;

    private int eventId;
    private int actualId;
    private String newTime;
    private String newLocation;
    private String newDescription;
    private Calendar newTaskDate;
    private int newDuration;
    private TaskPriority newPriority;
    private int snoozeLen;

    private boolean isLocationChanged;
    private boolean isDescChanged;
    private boolean isTaskDateChanged;
    private boolean isDurationChanged;
    private boolean isPriorityChanged;
    private boolean isSnoozeRequested;
    
    private static final String updateCommandFormat = "alter (?<eventID>[0-9]+?) as ?( time (?<time>.+?))?(len (?<duration>[0-9]+?\\.??[0-9]??[0-9]??) hrs)?( @ (?<location>.+?))?( desc \"(?<description>.+)\")?( setPrior (?<priority>.+))?( snooze (?<snooze>[0-9]+?) hrs)?$";
    private static final String timeFormatString = "h:m d/M/y";
    private static final String dateFormat = "dd MMM, yyyy";

    private static final Pattern patternUpdateCommand;
    private static final SimpleDateFormat timeFormat;
    private static final Logger logger;

    private static final String messageDateFormat = "\tDate: %s\n";
    private static final String messageDescriptionFormat = "\tDescription: %s\n";
    private static final String messageDurationFormat = "\tDuration: %.1f hours\n";
    private static final String messageLocationFormat = "\tLocation: %s\n";
    private static final String messagePriorityFormat = "\tPriority level: %s\n";
    private static final String messageUseDisplayFunction = "Please use \"display\" function to get the ID!";
    private static final String messageAfterMod = "  After modification:\n";
    private static final String messageBeforeMod = "  Before modification:\n";
    private static final String messageEditingFormat = "Editing task - %s\n";

    private static final String loggerNumberOfEvents = "No. of events=%d";
    private static final String loggerParseException = "Parse exception";
    private static final String loggerInputCommand = "Input command - %s";

    private static final String eventIDDelimiter = "eventID";
    private static final String descriptionDelimiter = "description";
    private static final String locationDelimiter = "location";
    private static final String durationDelimiter = "duration";
    private static final String timeDelimiter = "time";
    private static final String priorityDelimiter = "priority";
    private static final String snoozeDelimiter = "snooze";
    
    private static final float minsInHour = 60;

    static {
        patternUpdateCommand = Pattern.compile(updateCommandFormat);
        timeFormat = new SimpleDateFormat(timeFormatString);
        logger = Logger.getGlobal();
    }

    public AlterCommandHandler(TaskData taskData) {
        assertObjectNotNull(this);
        this.taskData = taskData;
    }

    @Override
    public boolean parseCommand(String command) {
        Matcher patternMatcher;
        
        logger.log(Level.INFO, String.format(loggerInputCommand, command));

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
            if (newTime != null) {
                Date parsedDate = timeFormat.parse(newTime);
                newTaskDate.setTime(parsedDate);   
                isTaskDateChanged = true;
            } else {
                isTaskDateChanged = false;
            }
        } catch (ParseException e) {
            logger.log(Level.INFO, loggerParseException, e);
            return false;
        }
        return true;
    }

    private void setTaskDetails(Matcher patternMatcher) {
        eventId = Integer.parseInt(patternMatcher.group(eventIDDelimiter));
        newTime = patternMatcher.group(timeDelimiter);
        newTaskDate = Calendar.getInstance();
        try {
            float lenInHrs = Float.parseFloat(patternMatcher.group(durationDelimiter));
            newDuration = hrsToMins(lenInHrs);
            isDurationChanged = true;
        } catch (Exception e) {
            isDurationChanged = false;
        }
        if (patternMatcher.group(locationDelimiter) != null) {
            newLocation = patternMatcher.group(locationDelimiter);
            isLocationChanged = true;
        } else {
            isLocationChanged = false;
        }
        if (patternMatcher.group(descriptionDelimiter) != null) {
            newDescription = patternMatcher.group(descriptionDelimiter);
            isDescChanged = true;
        } else {
            isDescChanged = false;
        }
        try {
            newPriority = TaskPriority.valueOf(patternMatcher.group(priorityDelimiter));
            isPriorityChanged = true;
        } catch (NullPointerException e) {
            isPriorityChanged = false;
        }
        try {
            snoozeLen = Integer.parseInt(patternMatcher.group(snoozeDelimiter));
            isSnoozeRequested = true;
        } catch (NumberFormatException e) {
            isSnoozeRequested = false;
        }
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
            displaySuccess();
            logger.log(Level.INFO,
                    String.format(loggerNumberOfEvents, taskData.getEventMap().size()));
            return true;
        } else {
            return false;
        }
    }
    
    public void displaySuccess() {
        oldEvent = extractMethod();
        System.out.printf(messageEditingFormat, oldEvent.getTaskName());
        System.out.printf(messageBeforeMod);
        printEventDetails();
        updateNewValues();
        setEventDetails();
        System.out.printf(messageAfterMod);
        printEventDetails();
    }
    
    private void updateNewValues() {
        if (!(isLocationChanged)) {
            newLocation = oldEvent.getTaskLocation();
        }
        if (!(isDescChanged)) {
            newDescription = oldEvent.getTaskDescription();
        }
        if (!(isTaskDateChanged)) {
            newTaskDate = oldEvent.getTaskDate();
        }
        if (!(isDurationChanged)) {
            newDuration = oldEvent.getTaskDuration();
        }
        if (!(isPriorityChanged)) {
            newPriority = oldEvent.getTaskPriority();
        }
    }
    
    private void setEventDetails() {
        oldEvent.setTaskLocation(newLocation);
        oldEvent.setTaskDescription(newDescription);
        oldEvent.setTaskDuration(newDuration);
        oldEvent.setTaskPriority(newPriority);
        if (isSnoozeRequested) {
            newTaskDate.add(Calendar.HOUR_OF_DAY, snoozeLen);
        }
        oldEvent.setTaskDate(newTaskDate);
    }
    
    private Event extractMethod() {
        return taskData.getEventMap().get(actualId);
    }

    private boolean eventAlreadyExists() {
        return taskData.getEventMap().containsKey(actualId);
    }

    private void printEventDetails() {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        System.out.printf(messageDateFormat, format.format(oldEvent.getTaskDate().getTime()));
        System.out.printf(messageDurationFormat, minsToHrs(oldEvent.getTaskDuration()));
        System.out.printf(messageLocationFormat, oldEvent.getTaskLocation());
        System.out.printf(messageDescriptionFormat, oldEvent.getTaskDescription());
        System.out.printf(messagePriorityFormat, oldEvent.getTaskPriority());
    }
    
    private int hrsToMins(float hours) {
        return (int) (minsInHour * hours);
    }

    private float minsToHrs(int mins) {
        return (float) (mins/minsInHour);
    }

    @Override
    public boolean isExtraInputNeeded() {
        return false;
    }

    private void assertObjectNotNull(Object o) {
        assert (o != null);
    }
}
