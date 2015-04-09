import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewScaleCommandHandler implements ICommandHandler {

    private TaskData taskData;
    private Calendar dateViewing;

    private boolean isExtraInputNeeded;
    private ViewOption choseView = ViewOption.NOT_CHOSEN;

    private static final String viewCommandString = "view";
    private static final Pattern patternViewCommand;
    private static final Logger logger = Logger.getLogger("CalendarViewCommandHandler");

    //@author UNKNOWN
    static {
        patternViewCommand = Pattern.compile(viewCommandString);
    }

    //@author UNKNOWN
    public ViewScaleCommandHandler(TaskData taskData) {
        assertObjectNotNull(this);
        assertObjectNotNull(taskData);
        this.taskData = taskData;
    }

    //@author UNKNOWN
    @Override
    public boolean parseCommand(String command) {
        assertObjectNotNull(this);
        isExtraInputNeeded = false;

        if (dateViewing == null) {
            Matcher matcher = patternViewCommand.matcher(command);
            if (matcher.matches()) {
                return true;
            } else {
                System.out.println("Incorrect format");
                isExtraInputNeeded = true;
                return false;
            }
        } else if (choseView == ViewOption.NOT_CHOSEN) {
            try {
                int chosenViewId = Integer.parseInt(command);
                if (chosenViewId > 0 && chosenViewId < ViewOption.values().length) {
                    choseView = ViewOption.values()[chosenViewId];
                    return true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid option");
                return false;
            }
        }
        return false;
    }

    //@author UNKNOWN
    @Override
    public ICommand getCommand() {
        ICommand viewCommand = new NullCommand();

        SimpleDateFormat format = new SimpleDateFormat("HH:mm dd MMM, yyyy");

        if (dateViewing == null) {
            dateViewing = Calendar.getInstance();
            System.out.println("month: " + dateViewing.get(Calendar.MONTH));
        }

        if (choseView == ViewOption.NOT_CHOSEN) {
            System.out.println("View for: ");
            System.out.println("1. this week");
            System.out.println("2. this month");
            System.out.println("3. next week");
            System.out.println("4. next month");
            System.out.println("5. prev week");
            System.out.println("6. prev month");
            isExtraInputNeeded = true;
            return viewCommand;
        } else {
            List<Integer> taskIds = getMatchingTaskDisplayIDs(dateViewing, choseView);
            switch (choseView) {
            case NOT_CHOSEN: {
                break;
            }

            case THIS_WEEK: {
                System.out.println("Current week view of tasks");
                break;
            }

            case THIS_MONTH: {
                System.out.println("Current month view of tasks");
                break;
            }

            case NEXT_WEEK: {
                System.out.println("Next week view of tasks");
                break;
            }

            case NEXT_MONTH: {
                System.out.println("Next week view of tasks");
                break;
            }

            case PREV_WEEK: {
                System.out.println("Previous week view of tasks");
                break;
            }

            case PREV_MONTH: {
                System.out.println("Previous month view of tasks");
                break;
            }

            }

            taskData.updateDisplayID(taskIds);
            assertObjectNotNull(taskData);
            int i = 0;
            if (taskIds.size() != 0) {
                for (Integer taskId : taskIds) {
                    System.out.printf(
                            "%d.  %s %s @ %s \"%s\"\n",
                            ++i,
                            format.format(taskData.getEventMap().get(taskId)
                                    .getTaskDate().getTime()), taskData.getEventMap()
                                    .get(taskId).getTaskName(), taskData.getEventMap()
                                    .get(taskId).getTaskLocation(), taskData
                                    .getEventMap().get(taskId).getTaskDescription());
                }
            } else {
                System.out.println("No tasks in this scale");
            }

            dateViewing = null;
            choseView = ViewOption.NOT_CHOSEN;

            return viewCommand;
        }
    }

    //@author UNKNOWN
    public List<Integer> getMatchingTaskDisplayIDs(Calendar dateViewing,
            ViewOption choseView) {
        List<Integer> returnTaskIds = new ArrayList<Integer>();

        for (Integer actualId : taskData.getEventMap().keySet()) {
            Calendar taskDate = taskData.getEventMap().get(actualId).getTaskDate();
            boolean isMatched = false;

            switch (choseView) {
            case THIS_WEEK: {
                isMatched = (taskDate.get(Calendar.WEEK_OF_YEAR) == dateViewing
                        .get(Calendar.WEEK_OF_YEAR) && taskDate.get(Calendar.YEAR) == dateViewing
                        .get(Calendar.YEAR));
                break;
            }
            case THIS_MONTH: {
                isMatched = (taskDate.get(Calendar.MONTH) == dateViewing
                        .get(Calendar.MONTH) && taskDate.get(Calendar.YEAR) == dateViewing
                        .get(Calendar.YEAR));
                break;
            }

            case NEXT_WEEK: {
                isMatched = (taskDate.get(Calendar.WEEK_OF_YEAR) == (dateViewing
                        .get(Calendar.WEEK_OF_YEAR) + 1) % 52 && taskDate
                        .get(Calendar.YEAR) == dateViewing.get(Calendar.YEAR));
                break;
            }

            case NEXT_MONTH: {
                isMatched = (taskDate.get(Calendar.MONTH) == (dateViewing
                        .get(Calendar.MONTH) + 1) % 12 && taskDate.get(Calendar.YEAR) == dateViewing
                        .get(Calendar.YEAR));
                break;
            }

            case PREV_WEEK: {
                isMatched = (taskDate.get(Calendar.WEEK_OF_YEAR) == (dateViewing
                        .get(Calendar.WEEK_OF_YEAR) - 1) && taskDate.get(Calendar.YEAR) == dateViewing
                        .get(Calendar.YEAR));
                break;
            }

            case PREV_MONTH: {
                isMatched = (taskDate.get(Calendar.MONTH) == (dateViewing
                        .get(Calendar.MONTH) - 1) && taskDate.get(Calendar.YEAR) == dateViewing
                        .get(Calendar.YEAR));
                break;
            }

            case NOT_CHOSEN: {
                isMatched = (taskDate.get(Calendar.MONTH) == dateViewing
                        .get(Calendar.MONTH) && taskDate.get(Calendar.YEAR) == dateViewing
                        .get(Calendar.YEAR));
                break;
            }

            default:
                break;
            }

            logger.info(String.format("actualId=%d, isMatched=%b", actualId, isMatched));

            if (isMatched) {
                returnTaskIds.add(actualId);
            }
        }

        return returnTaskIds;
    }

    //@author UNKNOWN
    @Override
    public boolean isExtraInputNeeded() {
        return isExtraInputNeeded;
    }

    //@author UNKNOWN
    public static enum ViewOption {
        NOT_CHOSEN, THIS_WEEK, THIS_MONTH, NEXT_WEEK, NEXT_MONTH, PREV_WEEK, PREV_MONTH;
    }

    //@author UNKNOWN
    private void assertObjectNotNull(Object o) {
        assert (o != null);
    }

}
