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
    
    private Event event;

    private boolean isConfirm;
    private boolean isProceedToConfirm;

    private static final String updateCommandFormat = "alter (?<eventID>[0-9]+) as (?<time>.+) for (?<duration>.+) mins @ (?<location>.+) desc \"(?<description>.+)\"";
    private static final String timeFormatString = "h:m d/M/y";
    private static final Pattern patternUpdateCommand;
    private static final SimpleDateFormat timeFormat;

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
        if (isProceedToConfirm) {
            boolean isYes = "Y".equalsIgnoreCase(command);
            boolean isNo = "N".equalsIgnoreCase(command);
            boolean isValid = (isYes ^ isNo);

            if (isValid) {
                isConfirm = isYes;
                return true;
            } else {
                return false;
            }
        } else {
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
            duration = Integer.parseInt(patternMatcher.group("duration"));
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
        }

        return true;
    }

    @Override
    public boolean executeCommand() {
        
    	assertObjectNotNull(this);
        if (this.isProceedToConfirm) {
            if (this.isConfirm) {
                event.setTaskLocation(location);
                event.setTaskDescription(description);
                event.setTaskDate(taskDate);
                event.setTaskDuration(duration);
            }
            isProceedToConfirm = false;
            return true;
        } else {
            try {
                actualId = taskData.getActualId(eventId);
            } catch (Exception NoSuchElementException) {
                System.out.println("Please use \"display\" function to get the ID!");
                return false;
            }
            boolean isExist = taskData.getEventMap().containsKey(actualId);
            if (isExist) {
                event = taskData.getEventMap().get(actualId);
                printConfirmation(event, location, description, taskDate, duration);
                this.isProceedToConfirm = true;
                return true;
            } else {
                return false;
            }
        }
    }

    private void printConfirmation(Event event, String location,
                                   String description, Calendar taskDate, int duration) {
        SimpleDateFormat format = new SimpleDateFormat("dd MMM, yyyy");
        System.out.printf("Editing task - %s\n", event.getTaskName());
        System.out.printf("Before modification:\n");
        System.out.printf("Date: %s\n",
                          format.format(event.getTaskDate().getTime()));
        System.out.printf("Duration: %d minutes\n", event.getTaskDuration());
        System.out.printf("Location: %s\n", event.getTaskLocation());
        System.out.printf("Description: %s\n", event.getTaskDescription());
        System.out.printf("\nAfter modification:\n");
        System.out.printf("Date: %s\n", format.format(taskDate.getTime()));
        System.out.printf("Duration: %d minutes\n", duration);
        System.out.printf("Location: %s\n", location);
        System.out.printf("Description: %s\n", description);
        System.out.printf("\tConfirm? (Y/N): ");
    }

    @Override
    public boolean isExtraInputNeeded() {
        return this.isProceedToConfirm;
    }
    
	private void assertObjectNotNull(Object o) {
		assert (o != null);
	}
}
