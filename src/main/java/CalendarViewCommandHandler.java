import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

public class CalendarViewCommandHandler implements ICommandHandler {

    private TaskData taskData;
    private String command;
    private String saveCommand;
    private boolean invalidOption = false;
    private ArrayList<Integer> floatingMonthIds = new ArrayList<Integer>();

    private boolean isExtraInputNeeded = false;
    private boolean floatingChosen = false;
    private ViewOption chosenView = ViewOption.NOT_CHOSEN;

    /*
    private static final Calendar calCurrTime;
    private static final Pattern patternViewDate;
    private static final Pattern patternViewCommand;
    */
    private static final SimpleDateFormat dateFormat;

    //private static final Logger logger = Logger.getGlobal();
    //private static final String loggerIDMatchingFormat = "actualId=%d, isMatched=%b";

    //private static final String viewCommandToday = "display today";
    private static final String viewCommandWeek = "display week";
    private static final String viewCommandMonth = "display month";
    //private static final String viewCommandDate = "display (?<date>.+)";
    //private static final String viewCommandString = "display (.+)";

    private static final String dateFormatString = "d/M/y";
    private static final String simpleDateFormatTimeDayMthyr = "HH:mm dd MMM, yyyy.";

    private static final String messageInvalidOption = "Invalid option";
    private static final String messageReenterDate = "Please either enter display week or display month";
    private static final String messageIncorrectFormat = "Incorrect format";

    private static final SimpleDateFormat formatTimeDayMthYr;


    private int maxMonth = 0;
    private int maxWeek = 0;
    private int nextMonth = 0;
    private int moveWeek = 0;

    static {
        //patternViewDate = Pattern.compile(viewCommandDate);
       //patternViewCommand = Pattern.compile(viewCommandString);
        dateFormat = new SimpleDateFormat(dateFormatString);
        formatTimeDayMthYr = new SimpleDateFormat(simpleDateFormatTimeDayMthyr);
        //calCurrTime = Calendar.getInstance();
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
        invalidOption = false;
        // isExtraInputNeeded = false;
        int firstSpace = command.indexOf(' ');

        if (isExtraInputNeeded != true) {
            //Matcher dateMatcher = patternViewDate.matcher(command);
            //Matcher stringMatcher = patternViewCommand.matcher(command);

            /*
             * 
             * if (command.equals(viewCommandToday)) { this.command =
             * command.substring(firstSpace + 1);
             * System.out.println("Success for today"); this.saveCommand =
             * this.command; return true; }
             */

            if (command.equals(viewCommandWeek)) {
                this.command = command.substring(firstSpace + 1);
                // System.out.println("Success for Week");
                this.saveCommand = this.command;
                return true;
            }

            else if (command.equals(viewCommandMonth)) {
                this.command = command.substring(firstSpace + 1);
                // System.out.println("success for month");
                this.saveCommand = this.command;
                return true;
            }

            /*
            else if (dateMatcher.matches()) {
                System.out.println("It is printing date");
                try {
                    Date parsedDate = dateFormat.parse(dateMatcher.group(dateDelimiter));
                    dateViewing = Calendar.getInstance();
                    dateViewing.setTime(parsedDate);
                    return true;
                } catch (ParseException e) {
                    System.out.println(messageIncorrectFormat);
                    System.out.println(messageReenterDate);
                    if (firstSpace < 0 || stringMatcher.matches()) {
                        System.out.println("accepts anything");
                        if (command.substring(firstSpace + 1).length() < maxStringSize) {
                            this.command = null;
                            System.out.println("Day too short!");
                            return false;
                        }

                        fullDayName = hasLogicalDate(command.substring(firstSpace + 1));
                        System.out.println("fullDayName: " + fullDayName);
                        if (fullDayName != null) {
                            this.command = fullDayName;
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            }*/ 
            else {
                System.out.println(messageIncorrectFormat);
                System.out.println(messageReenterDate);

                isExtraInputNeeded = false;;
                return false;
            }
        } else {
            try {
                int chosenViewId = Integer.parseInt(command);
                if (chosenViewId > 0 && chosenViewId < ViewOption.values().length) {
                    chosenView = ViewOption.values()[chosenViewId];
                    return true;
                }
                else{
                    System.out.println("Please enter options 1 to 5");
                    return false;
                }
            } catch (NumberFormatException e) {
                System.out.println(messageInvalidOption);
                invalidOption = true;
                return true;
            }
        }

    }

    //Will be in use later. Don't delete
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
    public boolean executeCommand() {
        if (invalidOption == true) {
            System.out.println("Please key in option again. Please exit display mode if you want to add, alter, delete or display another view");
            return true;
        } else {
            invalidOption = false;
        }

        if (this.saveCommand.equals("month")) {
            if (processMonthView()) {
                return true;
            }
        }

        if (this.saveCommand.equals("week")) {
            if (processWeekView()) {
                return true;
            }
        }

        return true;
    }

    private boolean processWeekView() {
        // 1st condition
        Event event;
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
                System.out.println("1. There are more events. hit 1 to view more");
                floatingChosen = false;
            } else {
                System.out.println("1. There are no more events");
                this.maxWeek = 0; // Needed for looping results
                floatingChosen = false;
            }
            if (floatingMonthIds.size() != 0) {
                System.out.println("2. There are floating events. hit 2 to view");
            } else {
                System.out.println("2. There are no floating events");
            }

            System.out.println("3. Previous week");
            System.out.println("4. Next week");
            System.out.println("5. Exit Display mode");
        }
        // 2nd condition
        if (chosenView == ViewOption.FLOATING) {
            if (floatingMonthIds.size() != 0) {
                taskData.updateDisplayID(floatingMonthIds);
                System.out.printf("======================== Floating Events ========================\n\n");
                for (int i = 0; i < floatingMonthIds.size(); i++) {
                    event = taskData.getEventMap().get(floatingMonthIds.get(i));
                    System.out.println(taskData.getDisplayId(floatingMonthIds.get(i)) +"." + " " + event.getTaskName() +" @ " + event.getTaskLocation()
                            + " \"" + event.getTaskDescription() +"\"");
                }
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
            System.out.println("Exited Display mode");
            return true;
        }

        isExtraInputNeeded = true;
        return true;
    }

    private boolean processMonthView() {
        Event event;
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
                System.out.println("1. There are more events. Hit 1 to view more");
                floatingChosen = false;
            } else {
                System.out.println("1. There are no more events");
                this.maxMonth = 0;
                floatingChosen = false;
            }
            if (floatingMonthIds.size() != 0) {
                System.out.println("2. There are floating events. Hit 2 to view");
            } else {
                System.out.println("2. There are no floating events");
            }
            System.out.println("3. Previous month");
            System.out.println("4. Next month");
            System.out.println("5. Exit Display mode");
              
        }
        // 2nd condition
        if (chosenView == ViewOption.FLOATING) {
            if (floatingMonthIds.size() != 0) {
                taskData.updateDisplayID(floatingMonthIds);
                System.out.printf("======================== Floating Events ========================\n\n");
                for (int i = 0; i < floatingMonthIds.size(); i++) {
                    event = taskData.getEventMap().get(floatingMonthIds.get(i));
                    System.out.println(taskData.getDisplayId(floatingMonthIds.get(i)) +"." + " " + event.getTaskName() +" @ " + event.getTaskLocation()
                            + " \"" + event.getTaskDescription() +"\"");
                }
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
            System.out.println("Exited Display mode");

            return true;
        }

        isExtraInputNeeded = true;
        return true;
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
        
        System.out
        .printf("======================== Week view from from %s to %s ========================\n\n",
                dateFormat.format(beginDate), dateFormat.format(endDate));


        try {
            weekIds = taskData.searchEmptySlots(beginDate, endDate, floatingMonthIds);
            taskData.updateDisplayID(weekIds);
            if (displayWeekView(weekIds) == true) {
                return true;
            }
        } catch (NoSuchElementException e) {
            System.out.println("There are no tasks this month");
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

        System.out
                .printf("======================== Month view from from %s to %s ========================\n\n",
                        dateFormat.format(begining), dateFormat.format(end));

        try {
            monthIds = taskData.searchEmptySlots(begining, end, floatingMonthIds);
            taskData.updateDisplayID(monthIds);
            if (displayMonthView(monthIds) == true) {
                return true;
            }

        } catch (NoSuchElementException e) {
            System.out.println("No tasks this month");
            System.out.println();

        }
        return false;
    }

    public boolean displayMonthView(List<Integer> monthIds) {
        taskData.updateDisplayID(monthIds);
        Event event;

        for (int i = 0; i < monthIds.size(); i++) {
            if (i >= maxMonth) {
                System.out.printf("\n\n");
                return true;
            }
            event = taskData.getEventMap().get(monthIds.get(i));
            System.out.println(taskData.getDisplayId(monthIds.get(i)) +"." + " "
                    + formatTimeDayMthYr.format(event.getTaskDate().getTime()) +" "
                    + event.getTaskName() + " " + "@ "+ event.getTaskLocation() + " " +"\""
                    + event.getTaskDescription() + "\"" + " ");
        }
        System.out.printf("\n\n");
        return false;
    }

    public boolean displayWeekView(List<Integer> weekIds) {
        taskData.updateDisplayID(weekIds);
        Event event;

        for (int i = 0; i < weekIds.size(); i++) {
            if (i >= maxWeek) {
                System.out.printf("\n\n");
                return true;
            }
            event = taskData.getEventMap().get(weekIds.get(i));
            System.out.println(taskData.getDisplayId(weekIds.get(i)) +"." + " "
                    + formatTimeDayMthYr.format(event.getTaskDate().getTime()) +" "
                    + event.getTaskName() + " " + "@ "+ event.getTaskLocation() + " " +"\""
                    + event.getTaskDescription() + "\"" + " ");
        }
        System.out.printf("\n\n");
        return false;
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