import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalendarViewCommandHandler implements ICommandHandler {
	
	private TaskData taskData;
    private String command;
    private Calendar dateViewing;

    private boolean isExtraInputNeeded;
    private ViewOption chosenView = ViewOption.NOT_CHOSEN;
    
    private static final Pattern patternViewCommand;
    private static final SimpleDateFormat dateFormat;
    
    private static final Logger logger = Logger.getLogger("CalendarViewCommandHandler");
    private static final String loggerIDMatchingFormat = "actualId=%d, isMatched=%b";
    
    private static final String viewCommandString = "display (?<date>.+)";
    private static final String dateFormatString = "d/M/y";
	private static final String displayListFormat = "%d %s\n";
	
	private static final String messageYearViewAnswer = "Year view of task IDs";
	private static final String messageMonthViewAnswer = "Month view of task IDs";
	private static final String messageWeekViewAnswer = "Week view of task IDs";
	private static final String messageDisplayMenu = "With your date, you can \n 1. View your tasks in a week based on your day\n2. View your tasks in a month based on your month\n3. View your tasks in a year based on your year";
	private static final String messageInvalidOption = "Invalid option";
	private static final String messageReenterDate = "Please enter date again in dd/MM/YYYY format";
	private static final String messageIncorrectFormat = "Incorrect format";
	
	private static final String blankSpace = " ";
	private static final String dateDelimiter = "date";
    
    static {
        patternViewCommand = Pattern.compile(viewCommandString);
        dateFormat = new SimpleDateFormat(dateFormatString);
    }

    public CalendarViewCommandHandler(TaskData taskData) {
    	assertObjectNotNull(this);
    	assertObjectNotNull(taskData);
        this.taskData = taskData;
    }

    @Override
    public boolean parseCommand(String command) {
    	assertObjectNotNull(this);
        this.command = command;
        isExtraInputNeeded = false;

        if (dateViewing == null) {
            Matcher matcher = patternViewCommand.matcher(command);

            if (matcher.matches()) {
                try {
                    Date parsedDate = dateFormat.parse(matcher.group(dateDelimiter));
                    dateViewing = Calendar.getInstance();
                    dateViewing.setTime(parsedDate);
                } catch (ParseException e) {
                    System.out.println(messageIncorrectFormat);
                    System.out.println(messageReenterDate);
                    return false;
                }
                return true;
            } else {
                System.out.println(messageIncorrectFormat);
                System.out.println(messageReenterDate);

                isExtraInputNeeded = true;
                return false;
            }
        } else if (chosenView == ViewOption.NOT_CHOSEN) {
            try {
                int chosenViewId = Integer.parseInt(command);
                if (chosenViewId > 0 &&
                    chosenViewId < ViewOption.values().length) {
                    chosenView = ViewOption.values()[chosenViewId];
                    return true;
                }
            } catch (NumberFormatException e) {
                System.out.println(messageInvalidOption);
                return false;
            }
        }

        return false;
    }

    @Override
    public boolean executeCommand() {
        isExtraInputNeeded = false;

        setDateViewing();

        if (chosenView == ViewOption.NOT_CHOSEN) {
            System.out.println(messageDisplayMenu);
            isExtraInputNeeded = true;
            return true;           
            
        } else {
            Set<Integer> taskIds = getMatchedTaskDisplayIDs(dateViewing, chosenView);
            
            displayChosenView();

            taskData.updateDisplayID(taskIds);
        	assertObjectNotNull(taskData);
            int i=0;
            for (Integer taskId : taskIds) {
                System.out.printf(displayListFormat, ++i, taskData.getEventMap().get(taskId).getTaskName());
            }
            
            dateViewing = null;
            chosenView = ViewOption.NOT_CHOSEN;
            return true;
        }

    }

	private void setDateViewing() {
		if (dateViewing == null) {
            String[] dateParts = command.split(blankSpace);
            int day = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int year = Integer.parseInt(dateParts[2]);

            dateViewing = Calendar.getInstance();
            dateViewing.set(year, month, day);
        }
	}

	private void displayChosenView() {
		switch (chosenView) {
		    case NOT_CHOSEN: {
		        break;
		    }
		    case WEEK: {
		        System.out.println(messageWeekViewAnswer);
		        break;
		    }
		    case MONTH: {
		        System.out.println(messageMonthViewAnswer);
		        break;
		    }
		    case YEAR: {
		        System.out.println(messageYearViewAnswer);
		        break;
		    }
		}
	}

    @Override
    public boolean isExtraInputNeeded() {
        return isExtraInputNeeded;
    }

    public Set<Integer> getMatchedTaskDisplayIDs(Calendar dateViewing,ViewOption chosenView) {
        Set<Integer> returnTaskIds = new HashSet<Integer>();
        
        for (Integer actualId : taskData.getEventMap().keySet()) {
            Calendar taskDate = taskData.getEventMap().get(actualId).getTaskDate();
            boolean isMatched = checkIfMatched(dateViewing, chosenView, taskDate);

            logger.info(String.format(loggerIDMatchingFormat, actualId, isMatched));

            if (isMatched) {
                returnTaskIds.add(actualId);
            }
        }
        return returnTaskIds;
    }

	private boolean checkIfMatched(Calendar dateViewing, ViewOption chosenView, Calendar taskDate) {
		boolean isMatched = false;
		switch (chosenView) {
		    case WEEK: {
		        isMatched = (taskDate.get(Calendar.WEEK_OF_YEAR) == dateViewing.get(Calendar.WEEK_OF_YEAR) && taskDate.get(Calendar.YEAR) == dateViewing.get(Calendar.YEAR));
		        break;
		    }
		    case MONTH: {
		        isMatched = (taskDate.get(Calendar.MONTH) == dateViewing.get(Calendar.MONTH) && taskDate.get(Calendar.YEAR) == dateViewing.get(Calendar.YEAR));
		        break;
		    }
		    case YEAR: {
		        isMatched = (taskDate.get(Calendar.YEAR) == dateViewing.get(Calendar.YEAR));
		        break;
		    }
		    default:
		        break;
		}
		return isMatched;
	}

    public static enum ViewOption {
        NOT_CHOSEN, WEEK, MONTH, YEAR
    }
	
    private void assertObjectNotNull(Object o) {
		assert (o != null);
	}
}
