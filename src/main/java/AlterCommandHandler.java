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
    private IInputSource inputSource;
    private Event eventToAlter;

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

    private static final String updateCommandFormat = "alter (?<eventID>[0-9]+?) as ?( time (?<time>.+?))??(len (?<duration>[0-9]+?\\.??[0-9]??[0-9]??) hrs)?( @ (?<location>.+?))??( desc \"(?<description>.+)\")??( setPrior (?<priority>.+?))??( snooze (((?<snooze1>[0-9]+?) hrs)|((?<snooze2>[0-9]+?) days)))??$";
    private static final String timeFormatString = "h:m d/M/y";
    private static final String dateFormat = "dd MMM, yyyy";

    private static final Pattern patternUpdateCommand;
    private static final SimpleDateFormat timeFormat;
    private static final Logger logger;

    private static final String COMMAND_DISPLAY_MONTH = "display month";
    private static final String MESSAGE_EVENT_NOT_FOUND = "Event not found.";

    private static final String messageDateFormat = "\tDate: %s\n";
    private static final String messageDescriptionFormat = "\tDescription: %s\n";
    private static final String messageDurationFormat = "\tDuration: %.1f hours\n";
    private static final String messageLocationFormat = "\tLocation: %s\n";
    private static final String messagePriorityFormat = "\tPriority level: %s\n";
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
    private static final String snoozeHrsDelimiter = "snooze1";
    private static final String snoozeDaysDelimiter = "snooze2";

    private static final float minsInHour = 60;

    //@author A0134704M
    static {
        patternUpdateCommand = Pattern.compile(updateCommandFormat, Pattern.CASE_INSENSITIVE);
        timeFormat = new SimpleDateFormat(timeFormatString);
        logger = Logger.getGlobal();
    }

    //@author A0109239A
    public AlterCommandHandler(TaskData taskData, IInputSource inputSource) {
        this.taskData = taskData;
        this.inputSource = inputSource;
    }

    //@author A0109239A
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

    //@author A0109239A
    private void setTaskDetails(Matcher patternMatcher) {
        eventId = Integer.parseInt(patternMatcher.group(eventIDDelimiter));
        newTime = patternMatcher.group(timeDelimiter);
        newTaskDate = Calendar.getInstance();
        try {
            newDuration = hrsToMins(Float.parseFloat(patternMatcher
                    .group(durationDelimiter)));
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
            String priority = (patternMatcher.group(priorityDelimiter)).toUpperCase();
            newPriority = TaskPriority.valueOf(priority);
            isPriorityChanged = true;
        } catch (NullPointerException e) {
            isPriorityChanged = false;
        }
        isSnoozeRequested = false;
        try {
            snoozeLen = Integer.parseInt(patternMatcher.group(snoozeHrsDelimiter));
            isSnoozeRequested = true;
        } catch (NumberFormatException e) {
        }
        try {
            snoozeLen = 24 * (Integer.parseInt(patternMatcher.group(snoozeDaysDelimiter)));
            isSnoozeRequested = true;
        } catch (NumberFormatException e) {
        }
    }

    //@author UNKNOWN
    @Override
    public ICommand getCommand() {
        try {
            actualId = taskData.getActualId(eventId);
        } catch (Exception NoSuchElementException) {
            if (taskData.isDisplayIdMapEmpty()) {
                inputSource.addCommand(COMMAND_DISPLAY_MONTH);
            } else {
                System.out.println(MESSAGE_EVENT_NOT_FOUND);
            }
            return null;
        }
        
        boolean isEventAlreadyExists = taskData.getEventMap().containsKey(actualId);;
        if (isEventAlreadyExists) {
            Event eventWithUpdatedData = new Event();
            eventToAlter = taskData.getEventMap().get(actualId);
            System.out.printf(messageEditingFormat, eventToAlter.getTaskName());
            System.out.printf(messageBeforeMod);
            printEventDetails(eventToAlter);
            updateNewValues(eventToAlter, eventWithUpdatedData);
            
            ICommand alterCommand = new AlterCommand(taskData, eventToAlter, eventWithUpdatedData);
            
            System.out.printf(messageAfterMod);
            printEventDetails(eventWithUpdatedData);
            logger.log(Level.INFO,
                    String.format(loggerNumberOfEvents, taskData.getEventMap().size()));
            return alterCommand;
        } else {
            return null;
        }
    }

    //@author A0109239A
    private void updateNewValues(Event from, Event to) {
        
        to.setTaskName(from.getTaskName());
        
        if (isLocationChanged) {
            to.setTaskLocation(newLocation);
        } else {
            to.setTaskLocation(from.getTaskLocation());
        }

        if (isDescChanged) {
            to.setTaskDescription(newDescription);
        } else {
            to.setTaskDescription(from.getTaskDescription());
        }
        
        if (isTaskDateChanged) {
            if (isSnoozeRequested) {
                newTaskDate.add(Calendar.HOUR_OF_DAY, snoozeLen);
            }
            to.setTaskDate(newTaskDate);
        } else {
            to.setTaskDate(from.getTaskDate());
        }
        
        if (isDurationChanged) {
            to.setTaskDuration(newDuration);
        } else {
            to.setTaskDuration(from.getTaskDuration());
        }
        
        if (isPriorityChanged) {
            to.setTaskPriority(newPriority);
        } else {
            to.setTaskPriority(from.getTaskPriority());
        }
    }

    //@author A0109239A
    private void printEventDetails(Event event) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
            System.out.printf(messageDateFormat,
                    format.format(event.getTaskDate().getTime()));
        } catch (Exception e) {
            System.out.printf(messageDateFormat, "unspecified");
        }
        System.out.printf(messageDurationFormat, minsToHrs(event.getTaskDuration()));
        System.out.printf(messageLocationFormat, event.getTaskLocation());
        System.out.printf(messageDescriptionFormat, event.getTaskDescription());
        System.out.printf(messagePriorityFormat, event.getTaskPriority());
    }

    //@author A0109239A
    private int hrsToMins(float hours) {
        return (int) (minsInHour * hours);
    }

    //@author A0109239A
    private float minsToHrs(int mins) {
        return (float) (mins / minsInHour);
    }

    //@author UNKNOWN
    @Override
    public boolean isExtraInputNeeded() {
        return false;
    }
}
