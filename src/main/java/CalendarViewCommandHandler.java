import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class CalendarViewCommandHandler implements ICommandHandler {

    private TaskData taskData;
    private String command;
    private Calendar dateViewing;

    private boolean isExtraInputNeeded;
    private ViewOption chosenView = ViewOption.NOT_CHOSEN;

    public CalendarViewCommandHandler(TaskData taskData) {
        this.taskData = taskData;
    }

    @Override
    public boolean parseCommand(String command) {
        this.command = command;
        isExtraInputNeeded = false;

        if (dateViewing == null) {
            if (isCorrectDateFormat(command)) {
                return true;
            } else {
                System.out.println("Incorrect format");
                System.out.println("Please enter date again in dd MM YYYY format");

                isExtraInputNeeded = true;
                return false;
            }
        } else if (chosenView == ViewOption.NOT_CHOSEN) {
            try {
                int chosenViewId = Integer.parseInt(command);
                if (chosenViewId > 0 && chosenViewId < ViewOption.values().length) {
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

        switch (chosenView) {
        case NOT_CHOSEN: {
            System.out.println("With your date, you can, ");
            System.out.println("1. View your tasks in a week based on your day");
            System.out.println("2. View your tasks in a month based on your month");
            System.out.println("3. View your tasks in a year based on your year");

            isExtraInputNeeded = true;
            return true;
        }

        case WEEK: {
            Set<Integer> taskIds = getMatchedTasks(dateViewing, ViewOption.WEEK);

            System.out.println("Week view of task IDs");
            for (Integer taskId : taskIds) {
                System.out.println(taskId);
            }

            return true;
        }

        case MONTH: {
            Set<Integer> taskIds = getMatchedTasks(dateViewing, ViewOption.WEEK);

            System.out.println("Month view of task IDs");
            for (Integer taskId : taskIds) {
                System.out.println(taskId);
            }

            return true;
        }

        case YEAR: {
            Set<Integer> taskIds = getMatchedTasks(dateViewing, ViewOption.YEAR);

            System.out.println("Year view of task IDs");
            for (Integer taskId : taskIds) {
                System.out.println(taskId);
            }

            return true;
        }
        }

        return false;
    }

    @Override
    public boolean isExtraInputNeeded() {
        return isExtraInputNeeded;
    }

    public Set<Integer> getMatchedTasks(Calendar dateViewing,
            ViewOption chosenView) {
        Set<Integer> returnTaskIds = new HashSet<Integer>();

        for (Integer taskId : taskData.getEventMap().keySet()) {
            Calendar taskDate = taskData.getEventMap().get(taskId).getTaskDate();
            boolean isMatched = false;

            switch (chosenView) {
            case WEEK: {
                isMatched = (taskDate.get(Calendar.WEEK_OF_YEAR) == dateViewing.get(Calendar.WEEK_OF_YEAR) 
                        && taskDate.get(Calendar.YEAR) == dateViewing.get(Calendar.YEAR));
                break;
            }
            case MONTH: {
                isMatched = (taskDate.get(Calendar.MONTH) == dateViewing.get(Calendar.MONTH)
                        && taskDate.get(Calendar.YEAR) == dateViewing.get(Calendar.YEAR));
                break;
            }
            case YEAR: {
                isMatched = (taskDate.get(Calendar.YEAR) == dateViewing.get(Calendar.YEAR));
                break;
            }
            default:
                break;
            }

            if (isMatched) {
                returnTaskIds.add(taskId);
            }
        }

        return returnTaskIds;
    }

    public boolean isCorrectDateFormat(String date) {
        if (!(date.matches("[0-9]+") || (date.length() == 10 && date
                .contains(" ")))) {
            return false;
        }

        String[] dateParts = date.split(" ");

        int day = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);

        if (!(hasLogicalDate(day, month, year))) {
            return false;
        }

        return true;
    }

    public boolean hasLogicalDate(int date, int month, int year) {
        int[] monthDays = new int[] { 31, 28, 31, 30, 30, 30, 31, 31, 30, 31, 30, 31 };

        if (date < 0 || month < 0 || month > 12 || year < 0)
            return false;

        // Check for leap year
        if ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0)) {
            monthDays[1] = 29;
        }

        month = month - 1;

        if (date > monthDays[month])
            return false;

        return true;
    }

    public static enum ViewOption {
        NOT_CHOSEN, WEEK, MONTH, YEAR
    }
}
