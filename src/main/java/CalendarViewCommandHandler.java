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

    private static final String viewCommandString = "display (?<date>.+)";
    private static final String dateFormatString = "d/M/y";
    private static final Pattern patternViewCommand;
    private static final SimpleDateFormat dateFormat;
    private static final Logger logger = Logger.getLogger("CalendarViewCommandHandler");

    static {
        patternViewCommand = Pattern.compile(viewCommandString);
        dateFormat = new SimpleDateFormat(dateFormatString);
    }

    public CalendarViewCommandHandler(TaskData taskData) {
        this.taskData = taskData;
    }

    @Override
    public boolean parseCommand(String command) {
        this.command = command;
        isExtraInputNeeded = false;

        if (dateViewing == null) {
            Matcher matcher = patternViewCommand.matcher(command);

            if (matcher.matches()) {
                try {
                    Date parsedDate = dateFormat.parse(matcher.group("date"));
                    dateViewing = Calendar.getInstance();
                    dateViewing.setTime(parsedDate);
                } catch (ParseException e) {
                    System.out.println("Incorrect format");
                    System.out.println("Please enter date again in dd/MM/YYYY format");
                    return false;
                }
                return true;
            } else {
                System.out.println("Incorrect format");
                System.out.println("Please enter date again in dd/MM/YYYY format");

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
                System.out.println("Invalid option");
                return false;
            }
        }

        return false;
    }

    @Override
    public boolean executeCommand() {
        isExtraInputNeeded = false;

        if (dateViewing == null) {
            String[] dateParts = command.split(" ");
            int day = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int year = Integer.parseInt(dateParts[2]);

            dateViewing = Calendar.getInstance();
            dateViewing.set(year, month, day);
        }

        
        if (chosenView == ViewOption.NOT_CHOSEN) {
            System.out.println("With your date, you can, ");
            System.out.println("1. View your tasks in a week based on your day");
            System.out.println("2. View your tasks in a month based on your month");
            System.out.println("3. View your tasks in a year based on your year");

            isExtraInputNeeded = true;
            return true;           
        } else {
            Set<Integer> taskIds = getMatchedTaskDisplayIDs(dateViewing, chosenView);
            
            switch (chosenView) {
                case NOT_CHOSEN: {
                    break;
                }

                case WEEK: {
                    System.out.println("Week view of task IDs");
                    break;
                }

                case MONTH: {
                    System.out.println("Month view of task IDs");
                    break;
                }

                case YEAR: {
                    System.out.println("Year view of task IDs");
                    break;
                }
            }


            taskData.updateDisplayID(taskIds);
            int i=0;
            for (Integer taskId : taskIds) {
                System.out.printf("%d %s\n", ++i, taskData.getEventMap().get(taskId).getTaskName());
            }
            
            dateViewing = null;
            chosenView = ViewOption.NOT_CHOSEN;
            return true;
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

            logger.info(String.format("actualId=%d, isMatched=%b",
                                      actualId,
                                      isMatched));

            if (isMatched) {
                returnTaskIds.add(actualId);
            }
        }

        return returnTaskIds;
    }

    public static enum ViewOption {
        NOT_CHOSEN, WEEK, MONTH, YEAR
    }
}
