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

    private TaskData taskData;
    private Event event;
    private boolean isConfirm;
    private boolean isProceedToConfirm;

    private String name;
    private String location;
    private String description;
    private Calendar taskDate;

    private static final String addCommandFormat = "add (?<name>.+) at (?<time>.+) @ (?<location>.+) desc \"(?<description>.+)\"";
    private static final String timeFormatString = "h:m d/M/y";
    private static final Pattern patternAddCommand;
    private static final SimpleDateFormat timeFormat;
    private static final Logger logger;

    static {
        patternAddCommand = Pattern.compile(addCommandFormat);
        timeFormat = new SimpleDateFormat(timeFormatString);
        logger = Logger.getLogger("AddCommandHandler");
    }

    public AddCommandHandler(TaskData taskData) {
        assertObjectNotNull(this);
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

        logger.log(Level.INFO, String.format("Input command - %s", command));

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
                patternMatcher = patternAddCommand.matcher(command);
                if (!patternMatcher.matches()) {
                    return false;
                }
            }

            this.name = patternMatcher.group("name");
            String time = patternMatcher.group("time");
            this.location = patternMatcher.group("location");
            this.description = patternMatcher.group("description");
            this.taskDate = Calendar.getInstance();

            try {
                Date parsedDate = timeFormat.parse(time);
                taskDate.setTime(parsedDate);
            } catch (ParseException e) {
                logger.log(Level.INFO, "Parse exception", e);
                return false;
            }

            logger.log(Level.INFO, "Parsed event - " + event);
        }

        return true;
    }

    public void setEvent(String name, String location, String description,
                         Calendar taskDate) {
        event = new Event();
        assertObjectNotNull(event);
  
        event.setTaskID(getUniqueId());
        event.setTaskName(name);
        event.setTaskLocation(location);
        event.setTaskDescription(description);
        event.setTaskDate(taskDate);
<<<<<<< HEAD
        
        assertObjectNotNull(event);
        logger.log(Level.INFO, "Parsed event - " + event);

        return true;
=======
    }

    private void printConfirmation(String name, String location,
                                   String description, Calendar taskDate) {
        SimpleDateFormat format = new SimpleDateFormat("dd MMM, yyyy");
        System.out.printf("Add this event:\n");
        System.out.printf("%s\n", name);
        System.out.printf("Location: %s\n", location);
        System.out.printf("Description: %s\n", description);
        System.out.printf("Date: %s\n", format.format(taskDate.getTime()));
        System.out.printf("Confirm? (Y/N): ");
>>>>>>> 4ee3d683203cf8800c8ed6f1879131f4f0251558
    }

    @Override
    public boolean executeCommand() {
<<<<<<< HEAD
        assertObjectNotNull(event);
        assertObjectNotNull(taskData);
        boolean isExist = taskData.getEventMap().containsKey(event.getTaskID());
=======
>>>>>>> 4ee3d683203cf8800c8ed6f1879131f4f0251558

        if (this.isProceedToConfirm) {
            if (this.isConfirm) {
                setEvent(name, location, description, taskDate);
                taskData.getEventMap().put(event.getTaskID(), event);
                logger.log(Level.INFO, String.format("No. of events=%d",
                                                     taskData.getEventMap().size()));
            }
            isProceedToConfirm = false;
            return true;
        } else {
            printConfirmation(name, location, description, taskDate);
            this.isProceedToConfirm = true;
            return true;
        }
    }

    @Override
    public boolean isExtraInputNeeded() {
        return this.isProceedToConfirm;
    }

    public int getUniqueId() {
        Random random = new Random();
        assertObjectNotNull(random);
        int returnVal = 0;
        assertObjectNotNull(taskData);
        do {
            returnVal = random.nextInt(Integer.MAX_VALUE);
        } while (taskData.getEventMap().containsKey(returnVal));

        return returnVal;
    }

    public Event getEvent() {
        assertObjectNotNull(event);
        return event;
    }
    
    private void assertObjectNotNull(Object o) {
		assert (o != null);
	}
}
