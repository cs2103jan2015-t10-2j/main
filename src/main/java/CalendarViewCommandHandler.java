import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class CalendarViewCommandHandler implements ICommandHandler {

    private static final String MESSAGE_KEY_IN_OPTION_AGAIN = "Please key in option 1 to 5 again. Please exit display mode if you want to add, alter, delete or display another view";
    private TaskData taskData;
    private String command;
    private String saveCommand;
    private Calendar dateViewing;
    private boolean reEnter = false;
    private ArrayList<Integer> floatingMonthIds = new ArrayList<Integer>();

    private boolean isExtraInputNeeded;
    private boolean floatingChosen = false;
    private ViewOption chosenView = ViewOption.NOT_CHOSEN;

    private static final Pattern patternViewDate;
    private static final SimpleDateFormat dateFormat;

    private static final String viewCommandDisplay = "display";
    private static final String viewCommandWeek = "display week";
    private static final String viewCommandMonth = "display month";
    private static final String viewCommandDate = "display (?<date>.+)";

    private static final String dateFormatString = "d/M/y";
    private static final String simpleDateFormatEMD = "EEE MMM dd";
    private static final String simpleDateFormatEEE = "EEEEEEEEE";
    private static final String simpleDateFormatHHColonMM = "HH:mm";

    private static final String messageReenterDate = "Please either enter display week or display month";
    private static final String messageIncorrectFormat = "Incorrect format";
    private static final String dateDelimiter = "date";
    private static final String messageMonthView = "======================== Month view from %s to %s ========================\n\n\n";
    private static final String messageWeekView = "======================== Week view from %s to %s ========================\n\n\n";
    private static final String messageDateView = "=============================== Date View of %s ===============================\n\n\n";
    private static final String messagePrintDate = "[%s]=============================================================\n\n";
    private static final String messageFloating = "======================== Floating Events ========================\n\n";
    private static final String messageMoreEvents = "1. There are more tasks. Hit 1 to view more";
    private static final String messageNoMoreEvents = "1. There are no more tasks";
    private static final String messageHasFloating = "2. There are floating tasks. Hit 2 to view";
    private static final String messageHasNoFloating = "2. There are no floating tasks";
    private static final String messageExitDisplay =  "5. Exit display mode" ;
    
    
    private static final SimpleDateFormat dateFormatEMD;
    private static final SimpleDateFormat dateFormatEEE;
    private static final SimpleDateFormat formatHHColonMM;

    private int maxMonth = 0;
    private int maxWeek = 0;
    private int maxDate = 0;
    private int nextMonth = 0;
    private int moveWeek = 0;
    private int moveDate = 0;

    static {
        patternViewDate = Pattern.compile(viewCommandDate);
        dateFormat = new SimpleDateFormat(dateFormatString);
        dateFormatEMD = new SimpleDateFormat(simpleDateFormatEMD);
        dateFormatEEE = new SimpleDateFormat(simpleDateFormatEEE);
        formatHHColonMM = new SimpleDateFormat(simpleDateFormatHHColonMM);
    }

    public CalendarViewCommandHandler(TaskData taskData) {
        assertObjectNotNull(taskData);
        this.taskData = taskData;
    }

    @Override
    public boolean parseCommand(String command) {
        this.command = command;
        int firstSpace = command.indexOf(' ');
        boolean isCommandFormatCorrect = false;
        boolean isDisplayCommand = !isExtraInputNeeded;
        isExtraInputNeeded = true;

        Matcher dateMatcher = patternViewDate.matcher(command);
        if (isDisplayCommand) {

            if (viewCommandWeek.equalsIgnoreCase(command) || viewCommandDisplay.equalsIgnoreCase(command)) {
                this.command = command.substring(firstSpace + 1);
                // System.out.println("Success for Week");
                this.saveCommand = this.command;
                isCommandFormatCorrect = true;
            } else if (viewCommandMonth.equalsIgnoreCase(command)) {
                this.command = command.substring(firstSpace + 1);
                // System.out.println("success for month");
                this.saveCommand = this.command;
                isCommandFormatCorrect = true;
            }

            else if (dateMatcher.matches()) {
                System.out.println("It is printing date");
                try {
                    Date parsedDate = dateFormat.parse(dateMatcher.group(dateDelimiter));
                    dateViewing = Calendar.getInstance();
                    dateViewing.setTime(parsedDate);
                    this.saveCommand = "preciseDate";
                    isCommandFormatCorrect = true;
                } catch (ParseException e) {
                    System.out.println(messageIncorrectFormat);
                    System.out.println(messageReenterDate);
                    return false;
                }
                return true;
            } else {
                System.out.println(messageIncorrectFormat);
                System.out.println(messageReenterDate);

                isExtraInputNeeded = false;
                isCommandFormatCorrect = false;
            }
        } else {
            try {
                int chosenViewId = Integer.parseInt(command);
                if (chosenViewId > 0 && chosenViewId < ViewOption.values().length) {
                    chosenView = ViewOption.values()[chosenViewId];
                    reEnter = false;
                    isCommandFormatCorrect = true;
                    if (chosenView == ViewOption.EXIT) {
                        isExtraInputNeeded = false;
                    } else {
                        isExtraInputNeeded = true;
                    }
                } else {
                    System.out.println("Please enter options 1 to 5");
                    isExtraInputNeeded = true;
                    reEnter = true;
                    isCommandFormatCorrect = false;
                }
            } catch (NumberFormatException e) {
                System.out.println(MESSAGE_KEY_IN_OPTION_AGAIN);
                isExtraInputNeeded = true;
                reEnter = true;
                isCommandFormatCorrect = false;
            }
        }

        return isCommandFormatCorrect;
    }

    // Will be in use later. Don't delete
    public String hasLogicalDate(String command) {
        int length = command.length();
        String subString;

        if (length <= 6) {
            subString = "Monday".substring(0, length);
            if (subString.toLowerCase().equals(command)) {
                System.out.println("Monday");
                return "monday";
            }
            subString = "Friday".substring(0, length);
            if (subString.toLowerCase().equals(command)) {
                System.out.println("Friday");
                return "friday";
            }

            subString = "Sunday".substring(0, length);
            if (subString.toLowerCase().equals(command)) {
                System.out.println("Sunday");
                return "sunday";
            }
        }

        if (length <= 7) {
            subString = "Tuesday".substring(0, length);
            if (subString.toLowerCase().equals(command)) {
                System.out.println("Tueday");
                return "Tuesday";
            }
        }

        if (length <= 8) {
            subString = "Thursday".substring(0, length);
            if (subString.toLowerCase().equals(command)) {
                System.out.println("Thurday");
                return "thursday";
            }

            subString = "Saturday".substring(0, length);
            if (subString.toLowerCase().equals(command)) {
                System.out.println("Saturday");
                return "saturday";
            }
        }

        if (length <= 9) {
            subString = "wednesday".substring(0, length);
            if (subString.toLowerCase().equals(command)) {
                System.out.println("Wednesday");
                return "wedneday";
            }
        }
        return null;

    }

    @Override
    public ICommand getCommand() {
        ICommand displayCommand = new NullCommand();
        if (reEnter == true) {
            return displayCommand;
        }
        if ("month".equalsIgnoreCase(this.saveCommand)) {
            if (processMonthView()) {
                return displayCommand;
            }
        }

        if ("week".equalsIgnoreCase(this.saveCommand) || "display".equalsIgnoreCase(this.saveCommand)) {
            if (processWeekView()) {
                return displayCommand;
            }
        }

        if ("preciseDate".equalsIgnoreCase(this.saveCommand)) {
            if (processDateView()) {
                return displayCommand;
            }
        }

        return null;
    }

    private boolean processDateView() {

        // 1st condition
        if (chosenView == ViewOption.VIEW_MORE || chosenView == ViewOption.NOT_CHOSEN
                || chosenView == ViewOption.NEXT || chosenView == ViewOption.PREV) {
            if (chosenView == ViewOption.VIEW_MORE && floatingChosen != true) {
                this.maxDate += 5;
            } else {
                this.maxDate = 5;
            }

            if (chosenView == ViewOption.NEXT) {
                moveDate = 1;
            }

            if (chosenView == ViewOption.PREV) {
                moveDate = -1;
            }

            floatingMonthIds = new ArrayList<Integer>();
            if (getDateViewing(maxDate)) {
                System.out.println(messageMoreEvents);
                floatingChosen = false;
            } else {
                System.out.println(messageNoMoreEvents);
                this.maxDate = 0; // Needed for looping results
                floatingChosen = false;
            }
            if (floatingMonthIds.size() != 0) {
                System.out.println(messageHasFloating);
            } else {
                System.out.println(messageHasNoFloating);
            }

            System.out.println("3. Previous day");
            System.out.println("4. Next day");
            System.out.println(messageExitDisplay);
        }
        // 2nd condition
        if (chosenView == ViewOption.FLOATING) {
            if (floatingMonthIds.size() != 0) {
                taskData.updateDisplayID(floatingMonthIds);
                displayFloatingTasks(floatingMonthIds);
                floatingChosen = true;
            } else {
                System.out.println("There are no floating events");
            }
            System.out.println();
            System.out.println("1. Hit 1 to display tasks again");
            System.out.println("2. Hit 5 to exit display mode");
            chosenView = ViewOption.NOT_CHOSEN;
        }

        if (chosenView == ViewOption.EXIT) {
            isExtraInputNeeded = false; // means wont repeat
            chosenView = ViewOption.NOT_CHOSEN;
            saveCommand = null;
            moveDate = 0;
            dateViewing = null;
            System.out.println("Exited display mode");
            return true;
        }

        isExtraInputNeeded = true;
        return true;
    }

    private boolean processWeekView() {
        // 1st condition
        if (chosenView == ViewOption.VIEW_MORE || chosenView == ViewOption.NOT_CHOSEN
                || chosenView == ViewOption.NEXT || chosenView == ViewOption.PREV) {
            if (chosenView == ViewOption.VIEW_MORE && floatingChosen != true) {
                this.maxWeek += 5;
            } else {
                this.maxWeek = 5;
            }

            if (chosenView == ViewOption.NEXT) {
                moveWeek++;
            }

            if (chosenView == ViewOption.PREV) {
                moveWeek--;
            }

            floatingMonthIds = new ArrayList<Integer>();
            if (getWeekViewing(maxWeek)) {
                System.out.println(messageMoreEvents);
                floatingChosen = false;
            } else {
                System.out.println(messageNoMoreEvents);
                this.maxWeek = 0; // Needed for looping results
                floatingChosen = false;
            }
            if (floatingMonthIds.size() != 0) {
                System.out.println(messageHasFloating);
            } else {
                System.out.println(messageHasNoFloating);
            }

            System.out.println("3. Previous week");
            System.out.println("4. Next week");
            System.out.println(messageExitDisplay);
        }
        // 2nd condition
        if (chosenView == ViewOption.FLOATING) {
            if (floatingMonthIds.size() != 0) {
                taskData.updateDisplayID(floatingMonthIds);
                displayFloatingTasks(floatingMonthIds);
                floatingChosen = true;
            } else {
                System.out.println("There are no floating events");
            }
            System.out.println();
            System.out.println("1. Hit 1 to display tasks again");
            System.out.println("2. Hit 5 to exit display mode");
            chosenView = ViewOption.NOT_CHOSEN;
        }

        if (chosenView == ViewOption.EXIT) {
            isExtraInputNeeded = false; // means wont repeat
            chosenView = ViewOption.NOT_CHOSEN;
            saveCommand = null;
            moveWeek = 0;
            System.out.println("Exited display mode");
            return true;
        }

        isExtraInputNeeded = true;
        return true;
    }

    private boolean processMonthView() {
        if (chosenView == ViewOption.VIEW_MORE || chosenView == ViewOption.NOT_CHOSEN
                || chosenView == ViewOption.NEXT || chosenView == ViewOption.PREV) {
            if (chosenView == ViewOption.VIEW_MORE && floatingChosen != true) {
                this.maxMonth += 5;
            } else {
                this.maxMonth = 5;
            }

            if (chosenView == ViewOption.NEXT) {
                nextMonth++;
            }

            if (chosenView == ViewOption.PREV) {
                nextMonth--;
            }
            floatingMonthIds = new ArrayList<Integer>();
            if (getMonthViewing(maxMonth)) {
                System.out.println(messageMoreEvents);
                floatingChosen = false;
            } else {
                System.out.println(messageNoMoreEvents);
                this.maxMonth = 0;
                floatingChosen = false;
            }
            if (floatingMonthIds.size() != 0) {
                System.out.println(messageHasFloating);
            } else {
                System.out.println(messageHasNoFloating);
            }
            System.out.println("3. Previous month");
            System.out.println("4. Next month");
            System.out.println(messageExitDisplay);

        }
        // 2nd condition
        if (chosenView == ViewOption.FLOATING) {
            if (floatingMonthIds.size() != 0) {
                taskData.updateDisplayID(floatingMonthIds);
                displayFloatingTasks(floatingMonthIds);
                floatingChosen = true;
            } else {
                System.out.println("There are no floating events");
            }
            System.out.println();
            System.out.println("1. Hit 1 to display tasks again");
            System.out.println("2. Hit 5 to exit display mode");
            chosenView = ViewOption.NOT_CHOSEN;
        }

        // 3rd condition
        if (chosenView == ViewOption.EXIT) {
            isExtraInputNeeded = false; // means wont repeat
            chosenView = ViewOption.NOT_CHOSEN;
            saveCommand = null;
            nextMonth = 0;
            System.out.println("Exited display mode");

            return true;
        }

        isExtraInputNeeded = true;
        return true;
    }

   
    private boolean getDateViewing(int maxDate) {
        Date beginDate;
        List<Integer> dateIds;

        if (moveDate != 0) {
            dateViewing.add(Calendar.DAY_OF_YEAR, moveDate);
        }

        beginDate = dateViewing.getTime();

        System.out.println("current date: " + dateFormat.format(beginDate));

        try {
            dateIds = taskData.getDateTasks(beginDate, floatingMonthIds);
            taskData.updateDisplayID(dateIds);
            if (displayDateView(dateIds, beginDate) == true) {
                return true;
            }
        } catch (NoSuchElementException e) {
            System.out.println();
            System.out.println("There are no tasks this Day");
            System.out.println();
        }
        return false;

    }

    private boolean getWeekViewing(int maxWeek) {
        Calendar calStart = Calendar.getInstance();
        Date beginDate, endDate;
        List<Integer> weekIds;

        if (moveWeek != 0) {
            calStart.add(Calendar.WEEK_OF_MONTH, moveWeek);
        }
        calStart.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        beginDate = calStart.getTime();
        for (int i = 0; i < 9; i++) {
            calStart.add(Calendar.DATE, 1);
        }
        endDate = calStart.getTime();

        try {
            weekIds = taskData.searchEmptySlots(beginDate, endDate, floatingMonthIds);
            taskData.updateDisplayID(weekIds);
            if (displayWeekView(weekIds, beginDate, endDate) == true) {
                return true;
            }
        } catch (NoSuchElementException e) {
            System.out.println();
            System.out.println("No tasks from " + dateFormat.format(beginDate) + " to "+ dateFormat.format(endDate));
            System.out.println();
        }
        return false;
    }

    private boolean getMonthViewing(int maxMonth) {
        Date begining, end;
        List<Integer> monthIds;

        {
            Calendar calendar = getCalendarForNow();
            if (nextMonth != 0) {
                calendar.add(Calendar.MONTH, nextMonth);
            }
            calendar.set(Calendar.DAY_OF_MONTH,
                    calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            setTimeToBeginningOfDay(calendar);
            begining = calendar.getTime();
        }

        {
            Calendar calendar = getCalendarForNow();
            if (nextMonth != 0) {
                calendar.add(Calendar.MONTH, nextMonth);
            }
            calendar.set(Calendar.DAY_OF_MONTH,
                    calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            setTimeToEndofDay(calendar);
            end = calendar.getTime();
        }

        try {
            monthIds = taskData.searchEmptySlots(begining, end, floatingMonthIds);
            taskData.updateDisplayID(monthIds);
            if (displayMonthView(monthIds, begining, end) == true) {
                return true;
            }

        } catch (NoSuchElementException e) {
            System.out.println("No tasks from " + dateFormat.format(begining) + " to "
                    + dateFormat.format(end));
            System.out.println();

        }
        return false;
    }

    public boolean displayDateView(List<Integer> dateIds, Date date) {
        taskData.updateDisplayID(dateIds);
        String taskName, taskLocation, taskDescription;
        String printDate;
        Event event;

        System.out.printf(messageDateView, dateFormat.format(date));

        event = taskData.getEventMap().get(dateIds.get(0));
        printDate = dateFormatEEE.format(event.getTaskDate().getTime());
        System.out.printf(messagePrintDate, printDate);

        for (int i = 0; i < dateIds.size(); i++) {
            if (i >= maxDate) {
                System.out.printf("\n\n");
                return true;
            }
            event = taskData.getEventMap().get(dateIds.get(i));

            // 1st condition
            if (event.getTaskName() == null) {
                taskName = "\"task name unspecified\"";
            } else {
                taskName = event.getTaskName();
            }

            // 2nd condition
            if (event.getTaskLocation() == null) {
                taskLocation = "\"location unspecified\"";
            } else {
                taskLocation = event.getTaskLocation();
            }

            // 3rd condition
            if (event.getTaskDescription() == null) {
                taskDescription = "description unspecified";
            } else {
                taskDescription = event.getTaskDescription();
            }

            System.out.println(taskData.getDisplayId(dateIds.get(i)) + "." + " "
                    + formatHHColonMM.format(event.getTaskDate().getTime()) + " "
                    + taskName + " " + "@ " + taskLocation + " " + "desc " + "\""
                    + taskDescription + "\"" + " " + event.getTaskPriority().toString());
        }
        System.out.printf("\n\n");
        return false;
    }

    public boolean displayMonthView(List<Integer> monthIds, Date begining, Date end) {
        taskData.updateDisplayID(monthIds);
        Event event;
        String taskName, taskLocation, taskDescription;
        String printDate;

        System.out.printf(messageMonthView, dateFormat.format(begining),
                dateFormat.format(end));

        event = taskData.getEventMap().get(monthIds.get(0));
        printDate = dateFormatEMD.format(event.getTaskDate().getTime());
        System.out.printf(messagePrintDate, printDate);

        for (int i = 0; i < monthIds.size(); i++) {
            if (i >= maxMonth) {
                System.out.printf("\n");
                return true;
            }
            event = taskData.getEventMap().get(monthIds.get(i));
            if (!(printDate.equals(dateFormatEMD.format(event.getTaskDate().getTime())))) {
                System.out.println();
                printDate = dateFormatEMD.format(event.getTaskDate().getTime());
                System.out.printf(messagePrintDate, printDate);
            }

            // 1st condition
            if (event.getTaskName() == null) {
                taskName = "\"task name unspecified\"";
            } else {
                taskName = event.getTaskName();
            }

            // 2nd condition
            if (event.getTaskLocation() == null) {
                taskLocation = "\"location unspecified\"";
            } else {
                taskLocation = event.getTaskLocation();
            }

            // 3rd condition
            if (event.getTaskDescription() == null) {
                taskDescription = "description unspecified";
            } else {
                taskDescription = event.getTaskDescription();
            }

            System.out.println(taskData.getDisplayId(monthIds.get(i)) + "." + " "
                    + formatHHColonMM.format(event.getTaskDate().getTime()) + " "
                    + taskName + " " + "@ " + taskLocation + " " + "desc " + "\""
                    + taskDescription + "\"" + " " + event.getTaskPriority().toString());
        }
        System.out.printf("\n");
        return false;
    }

    public boolean displayWeekView(List<Integer> weekIds, Date begining, Date end) {
        taskData.updateDisplayID(weekIds);
        Event event;
        String printDate;
        String taskName, taskLocation, taskDescription;

        System.out.printf(messageWeekView, dateFormat.format(begining),
                dateFormat.format(end));

        event = taskData.getEventMap().get(weekIds.get(0));
        printDate = dateFormatEMD.format(event.getTaskDate().getTime());
        System.out.printf(messagePrintDate, printDate);

        for (int i = 0; i < weekIds.size(); i++) {
            if (i >= maxWeek) {
                System.out.printf("\n\n");
                return true;
            }

            event = taskData.getEventMap().get(weekIds.get(i));
            if (!(printDate.equals(dateFormatEMD.format(event.getTaskDate().getTime())))) {
                System.out.println();
                printDate = dateFormatEMD.format(event.getTaskDate().getTime());
                System.out.printf(messagePrintDate, printDate);
            }

            // 1st condition
            if (event.getTaskName() == null) {
                taskName = "\"task name unspecified\"";
            } else {
                taskName = event.getTaskName();
            }

            // 2nd condition
            if (event.getTaskLocation() == null) {
                taskLocation = "\"location unspecified\"";
            } else {
                taskLocation = event.getTaskLocation();
            }

            // 3rd condition
            if (event.getTaskDescription() == null) {
                taskDescription = "description unspecified";
            } else {
                taskDescription = event.getTaskDescription();
            }

            System.out.println(taskData.getDisplayId(weekIds.get(i)) + "." + " "
                    + formatHHColonMM.format(event.getTaskDate().getTime()) + " "
                    + taskName + " " + "@ " + taskLocation + " " + "desc " + "\""
                    + taskDescription + "\"" + " " + event.getTaskPriority().toString());
        }
        System.out.printf("\n\n");
        return false;
    }
    
    public void displayFloatingTasks(ArrayList<Integer> floatingIds) {
        Event event;
        String taskName, taskLocation, taskDescription;

        System.out.printf(messageFloating);

        for (int i = 0; i < floatingIds.size(); i++) {
            event = taskData.getEventMap().get(floatingIds.get(i));

            event = taskData.getEventMap().get(floatingIds.get(i));

            // 1st condition
            if (event.getTaskName() == null) {
                taskName = "\"task name unspecified\"";
            } else {
                taskName = event.getTaskName();
            }

            // 2nd condition
            if (event.getTaskLocation() == null) {
                taskLocation = "\"location unspecified\"";
            } else {
                taskLocation = event.getTaskLocation();
            }

            // 3rd condition
            if (event.getTaskDescription() == null) {
                taskDescription = "description unspecified";
            } else {
                taskDescription = event.getTaskDescription();
            }

            System.out.println(taskData.getDisplayId(floatingMonthIds.get(i)) + "." + " "
                    + taskName + " @ " + taskLocation + " \""
                    + taskDescription + "\"" + " " + event.getTaskPriority().toString());
        }

    }


    private static void setTimeToBeginningOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private static void setTimeToEndofDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }

    private static Calendar getCalendarForNow() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar;
    }

    @Override
    public boolean isExtraInputNeeded() {
        return isExtraInputNeeded;
    }

    public static enum ViewOption {
        NOT_CHOSEN, VIEW_MORE, FLOATING, PREV, NEXT, EXIT
    }

    private void assertObjectNotNull(Object o) {
        assert (o != null);
    }
}