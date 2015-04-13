import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalendarViewCommandHandler implements ICommandHandler {

    private static final String MESSAGE_KEY_IN_OPTION_AGAIN = "Please key in valid commands";
    private TaskData taskData;
    private IInputSource inputSource;
    private String command;
    private String firstWord;
    private String saveCommand;
    private Calendar dateViewing;
    private boolean exit = false;
    private boolean reEnter = false;

    private boolean isExtraInputNeeded;
    private ViewOption chosenView = ViewOption.NOT_CHOSEN;

    private static final Pattern patternViewDate;
    private static final Pattern patternViewDay;
    private static final SimpleDateFormat dateFormat;

    private static final String viewCommandDisplay = "display";
    private static final String viewCommandCheckList = "display checklist"; // for
                                                                            // floating
    private static final String viewCommandWeek = "display week";
    private static final String viewCommandMonth = "display month";
    private static final String viewCommandToday = "display today";
    private static final String viewCommandTomorrow = "display tomorrow";
    private static final String viewCommandDone = "display done";
    private static final String viewCommandPri = "display priority";
    private static final String viewCommadDay = "display (.+)";
    private static final String viewCommandDate = "display (?<date>.+)";

    private static final String dateFormatString = "d/M/y";
    private static final String simpleDateFormatEMD = "EEE MMM dd";
    private static final String simpleDateFormatEEE = "EEEEEEEEE";
    private static final String simpleDateFormatMMM = "MMMMMMMMMMMMMMMM YYYY";
    private static final String simpleDateFormatTimeDayMthyr = "HH:mm dd MMM, yyyy";

    private static final String messageInvalidDate = "If you are entering date. Please enter in for e.g. display 11/3/2015";
    private static final String messageReenterDate = "Please either enter display week or display month";
    private static final String messageIncorrectFormat = "Incorrect format. please enter valid display commands.";
    private static final String dateDelimiter = "date";
    private static final String messageNoFloating = "============== No tasks without time or date ==============\n\n\n\n";
    private static final String messageNoMonthView = "============== No tasks for the month of %s ===============\n\n";
    private static final String messageNoWeekView = "======== No tasks for the week of %s from %s to %s ========\n\n";
    private static final String messageNoDone = "====================== No done tasks ======================\n\n\n\n";
    private static final String messageNoDayView = "==================== No tasks for %s %s ===================\n\n";
    private static final String messageNoPri = "==================== No priority tasks ====================\n\n";

    private static final String messageMonthView = "====================== Month view of %s =================\n\n";
    private static final String messageWeekView = "================= Week view from %s to %s =================\n\n";
    private static final String messageDateView = "===================== Date View of %s =====================\n\n";
    private static final String messagePrintDate = "[%s]===============================\n\n";
    private static final String messagePrintPri = "[%s]===============================\n\n";
    private static final String messageFloating = "=============== Tasks without time and date ===============\n\n";
    private static final String messageDone = "======================== Done Tasks =======================\n\n";
    private static final String messagePri = "====================== Priority tasks =====================\n\n";
    private static final String messageMoreEvents = "1. There are more tasks. Hit 1 to view more";
    private static final String messageNoMoreEvents = "1. Hit 1 to refresh scale";

    private static final SimpleDateFormat dateFormatEMD;
    private static final SimpleDateFormat dateFormatEEE;
    private static final SimpleDateFormat dateFormatMMM;
    private static final SimpleDateFormat formatTimeDayMthYr;

    private int maxMonth = 0;
    private int maxWeek = 0;
    private int maxDate = 0;
    private int maxFloat = 0;
    private int maxToday = 0;

    private int nextMonth = 0;
    private int moveWeek = 0;
    private int moveDate = 0;
    private int moveToday = 0;

    // @author A0105886W
    static {
        patternViewDate = Pattern.compile(viewCommandDate);
        patternViewDay = Pattern.compile(viewCommadDay);
        dateFormat = new SimpleDateFormat(dateFormatString);
        dateFormatEMD = new SimpleDateFormat(simpleDateFormatEMD);
        dateFormatEEE = new SimpleDateFormat(simpleDateFormatEEE);
        dateFormatMMM = new SimpleDateFormat(simpleDateFormatMMM);
        formatTimeDayMthYr = new SimpleDateFormat(simpleDateFormatTimeDayMthyr);
    }

    // @author A0105886W
    public CalendarViewCommandHandler(TaskData taskData, IInputSource inputSource) {
        assertObjectNotNull(taskData);
        this.taskData = taskData;
        this.inputSource = inputSource;
    }

    // @author A0105886W
    @Override
    public boolean parseCommand(String command) {
        this.command = command;
        int firstSpace = command.indexOf(' ');
        boolean isCommandFormatCorrect = false;
        boolean isDisplayCommand = !isExtraInputNeeded;
        isExtraInputNeeded = true;

        Matcher dateMatcher = patternViewDate.matcher(command);
        Matcher dayMatcher = patternViewDay.matcher(command);
        if (isDisplayCommand) {

            if (viewCommandWeek.equalsIgnoreCase(command.trim())
                    || viewCommandDisplay.equalsIgnoreCase(command.trim())) {
                this.command = command.substring(firstSpace + 1).trim();
                if (viewCommandDisplay.equalsIgnoreCase(command.trim())) {
                    this.saveCommand = "display week again";
                } else {
                    this.saveCommand = this.command;
                }
                isCommandFormatCorrect = true;
            }

            else if (viewCommandMonth.equalsIgnoreCase(command.trim())) {
                this.command = command.substring(firstSpace + 1).trim();
                this.saveCommand = this.command;
                isCommandFormatCorrect = true;
            }

            else if (viewCommandToday.equalsIgnoreCase(command.trim())) {
                Date date;
                this.command = command.substring(firstSpace + 1).trim();
                this.saveCommand = this.command;
                dateViewing = Calendar.getInstance();
                String getDate = dateFormat.format(dateViewing.getTime());
                try {
                    date = dateFormat.parse(getDate);
                    dateViewing.setTime(date);
                } catch (ParseException e) {

                }

                isCommandFormatCorrect = true;
            }

            else if (viewCommandTomorrow.equals(command.trim())) {
                Date date;
                this.command = command.substring(firstSpace + 1).trim();
                this.saveCommand = this.command;
                dateViewing = Calendar.getInstance();
                dateViewing.add(Calendar.DAY_OF_YEAR, 1);
                String getDate = dateFormat.format(dateViewing.getTime());
                try {
                    date = dateFormat.parse(getDate);
                    dateViewing.setTime(date);
                } catch (ParseException e) {

                }

                isCommandFormatCorrect = true;
            }

            else if (viewCommandDone.equals(command.trim())) {
                this.command = command.substring(firstSpace + 1).trim();
                this.saveCommand = this.command;
                isCommandFormatCorrect = true;

            }

            else if (viewCommandPri.equals(command.trim())) {
                this.command = command.substring(firstSpace + 1).trim();
                this.saveCommand = this.command;
                isCommandFormatCorrect = true;
            }

            else if (viewCommandCheckList.equalsIgnoreCase(command.trim())) {
                this.command = command.substring(firstSpace + 1).trim();
                this.saveCommand = this.command;
                isCommandFormatCorrect = true;
            }

            else if (dayMatcher.matches()
                    && hasLogicalDate(command.substring(firstSpace + 1).trim())
                    && command.substring(firstSpace + 1).trim().length() > 2) {
                this.saveCommand = "dayName";
                isCommandFormatCorrect = true;
            }

            else if (dateMatcher.matches()) {
                try {
                    Date parsedDate = dateFormat.parse(dateMatcher.group(dateDelimiter));
                    dateViewing = Calendar.getInstance();
                    dateViewing.setTime(parsedDate);
                    this.saveCommand = "preciseDate";
                    isCommandFormatCorrect = true;
                } catch (ParseException e) {
                    System.out.println(messageIncorrectFormat);
                    System.out.println(messageInvalidDate);
                    return false;
                }
                return true;
            }

            else {
                System.out.println(messageIncorrectFormat);
                System.out.println(messageReenterDate);

                isExtraInputNeeded = false;
                isCommandFormatCorrect = false;
            }
        }

        // In display mode already
        else {
            String myString = command;
            String arr[] = myString.split(" ");
            firstWord = arr[0];

            if (firstWord.equals("add")) {
                isExtraInputNeeded = false;
                inputSource.addCommand(command);
                exit = true;
                return true;
            }

            else if (firstWord.equals("delete")) {
                isExtraInputNeeded = false;
                inputSource.addCommand(command);
                exit = true;
                return true;
            }

            else if (firstWord.equals("done")) {
                isExtraInputNeeded = false;
                inputSource.addCommand(command);
                exit = true;
                return true;
            }

            else if (firstWord.equals("alter")) {
                isExtraInputNeeded = false;
                inputSource.addCommand(command);
                exit = true;
                return true;
            }

            else if (firstWord.equalsIgnoreCase("save")) {
                isExtraInputNeeded = false;
                inputSource.addCommand(command);
                exit = true;
                return true;
            }

            else if (firstWord.equalsIgnoreCase("search")) {
                isExtraInputNeeded = false;
                inputSource.addCommand(command);
                exit = true;
                return true;
            }

            else if (firstWord.equalsIgnoreCase("exit")) {
                isExtraInputNeeded = false;
                inputSource.addCommand(command);
                exit = true;
                return true;
            }

            else if (firstWord.equalsIgnoreCase("display")) {
                isExtraInputNeeded = false;
                inputSource.addCommand(command);
                exit = true;
                reEnter = false;
                return true;
            }

            else if (firstWord.equalsIgnoreCase("undo")) {
                isExtraInputNeeded = false;
                inputSource.addCommand(command);
                exit = true;
                reEnter = false;
                return true;
            }

            else if (firstWord.equalsIgnoreCase("redo")) {
                isExtraInputNeeded = false;
                inputSource.addCommand(command);
                exit = true;
                reEnter = false;
                return true;
            }

            else if (firstWord.equalsIgnoreCase("history")) {
                isExtraInputNeeded = false;
                inputSource.addCommand(command);
                exit = true;
                reEnter = false;
                return true;
            }

            try {
                int chosenViewId = Integer.parseInt(command);
                if (chosenViewId > 0 && chosenViewId < ViewOption.values().length) {
                    chosenView = ViewOption.values()[chosenViewId];
                    reEnter = false;
                    isCommandFormatCorrect = true;

                } else {
                    System.out.println("Please enter valid options given");
                    isExtraInputNeeded = true;
                    reEnter = true;
                }
            } catch (NumberFormatException e) {
                System.out.println(MESSAGE_KEY_IN_OPTION_AGAIN);
                isExtraInputNeeded = true;
                reEnter = true;
            }
        }

        return isCommandFormatCorrect;

    }

    // @author A0105886W
    public boolean hasLogicalDate(String command) {
        int length = command.length();
        String subString;
        dateViewing = Calendar.getInstance();
        Date date;

        if (length <= 6) {
            subString = "Monday".substring(0, length);
            if (subString.toLowerCase().equals(command)) {
                dateViewing.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                String getDate = dateFormat.format(dateViewing.getTime());
                try {
                    date = dateFormat.parse(getDate);
                    dateViewing.setTime(date);
                } catch (ParseException e) {

                }
                return true;
            }
            subString = "Friday".substring(0, length);
            if (subString.toLowerCase().equals(command)) {
                dateViewing.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                String getDate = dateFormat.format(dateViewing.getTime());
                try {
                    date = dateFormat.parse(getDate);
                    dateViewing.setTime(date);
                } catch (ParseException e) {

                }
                return true;
            }

            subString = "Sunday".substring(0, length);
            if (subString.toLowerCase().equals(command)) {
                dateViewing.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                String getDate = dateFormat.format(dateViewing.getTime());
                try {
                    date = dateFormat.parse(getDate);
                    dateViewing.setTime(date);
                } catch (ParseException e) {

                }
                return true;
            }
        }

        if (length <= 7) {
            subString = "Tuesday".substring(0, length);
            if (subString.toLowerCase().equals(command)) {
                dateViewing.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                String getDate = dateFormat.format(dateViewing.getTime());
                try {
                    date = dateFormat.parse(getDate);
                    dateViewing.setTime(date);
                } catch (ParseException e) {

                }
                return true;
            }
        }

        if (length <= 8) {
            subString = "Thursday".substring(0, length);
            if (subString.toLowerCase().equals(command)) {
                dateViewing.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                String getDate = dateFormat.format(dateViewing.getTime());
                try {
                    date = dateFormat.parse(getDate);
                    dateViewing.setTime(date);
                } catch (ParseException e) {

                }
                return true;
            }

            subString = "Saturday".substring(0, length);
            if (subString.toLowerCase().equals(command)) {
                dateViewing.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                String getDate = dateFormat.format(dateViewing.getTime());
                try {
                    date = dateFormat.parse(getDate);
                    dateViewing.setTime(date);
                } catch (ParseException e) {

                }
                return true;
            }
        }

        if (length <= 9) {
            subString = "wednesday".substring(0, length);
            if (subString.toLowerCase().equals(command)) {
                dateViewing.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                String getDate = dateFormat.format(dateViewing.getTime());
                try {
                    date = dateFormat.parse(getDate);
                    dateViewing.setTime(date);
                } catch (ParseException e) {

                }
                return true;
            }
        }
        return false;

    }

    // @author A0105886W
    @Override
    public ICommand getCommand() {
        ICommand displayCommand = new NullCommand();

        if (exit == true) {
            chosenView = ViewOption.NOT_CHOSEN;
            saveCommand = null;
            exit = false;
            nextMonth = 0;
            moveWeek = 0;
            moveDate = 0;
            return displayCommand;
        }

        if (reEnter == true) {
            return displayCommand;
        }
        if ("month".equalsIgnoreCase(this.saveCommand)) {
            if (processMonthView()) {
                return displayCommand;
            }
        }

        if ("week".equalsIgnoreCase(this.saveCommand)
                || "display week again".equals(this.saveCommand)) {
            if (processWeekView()) {
                return displayCommand;
            }
        }

        if ("preciseDate".equalsIgnoreCase(this.saveCommand)) {
            if (processDateView()) {
                return displayCommand;
            }
        }

        if ("today".equals(this.saveCommand) || "tomorrow".equals(this.saveCommand)
                || "dayName".equals(this.saveCommand)) {
            if (processToday()) {
                return displayCommand;
            }
        }

        if ("done".equals(this.saveCommand)) {
            if (processDone()) {
                return displayCommand;
            }
        }

        if ("checklist".equals(this.saveCommand)) {
            if (processFloating()) {
                return displayCommand;
            }
        }

        if ("priority".equals(this.saveCommand)) {
            if (processPriority()) {
                return displayCommand;
            }
        }

        return null;
    }

    // @author A0105886W
    private boolean processPriority() {
        System.out.println();
        ArrayList<Integer> sortedPriIds = new ArrayList<Integer>();

        if (chosenView == ViewOption.NEXT || chosenView == ViewOption.PREV) {
            System.out.println("Please enter option 1 only");
        }

        else {

            try {
                sortedPriIds = taskData.getSortedPriIds();
                if (chosenView == ViewOption.VIEW_MORE) {
                    this.maxFloat += 5;
                } else {
                    this.maxFloat = 5;
                }

                System.out.println(messagePri);
                if (displayPriority(sortedPriIds, maxFloat)) {
                    System.out.println(messageMoreEvents);
                } else {
                    System.out.println(messageNoMoreEvents);
                    this.maxFloat = 0; // Needed for looping results
                }

            } catch (NoSuchElementException e) {
                System.out.printf(messageNoPri);
                System.out.println("Note: Please add tasks");
            }
        }

        System.out.println();

        return true;
    }

    // @author A0105886W
    private boolean processDone() {
        System.out.println();
        ArrayList<Integer> doneIds = new ArrayList<Integer>();
        if (chosenView == ViewOption.NEXT || chosenView == ViewOption.PREV) {
            System.out.println("Please enter option 1 only");
        }

        else {

            try {
                doneIds = taskData.getDoneIds(doneIds);
                if (chosenView == ViewOption.VIEW_MORE) {
                    this.maxFloat += 5;
                } else {
                    this.maxFloat = 5;
                }

                System.out.println(messageDone);
                if (displayFloating(doneIds, maxFloat)) {
                    System.out.println(messageMoreEvents);
                } else {
                    System.out.println(messageNoMoreEvents);
                    this.maxFloat = 0; // Needed for looping results
                }

            } catch (NoSuchElementException e) {
                System.out.printf(messageNoDone);
                System.out
                        .println("Tip: Please display any view except done and choose a task you want it as done first");
                System.out.println("For example: display checklist then done 1");
            }
        }

        return true;
    }

    // @author A0105886W
    private boolean processToday() {

        System.out.println();
        // 1st condition
        if (chosenView == ViewOption.VIEW_MORE || chosenView == ViewOption.NOT_CHOSEN
                || chosenView == ViewOption.NEXT || chosenView == ViewOption.PREV) {
            if (chosenView == ViewOption.VIEW_MORE) {
                this.maxToday += 5;
            } else {
                this.maxToday = 5;
            }

            if (chosenView == ViewOption.NEXT) {
                moveToday = 1;
            }

            if (chosenView == ViewOption.PREV) {
                moveToday = -1;
            }

            if (getTodayViewing(maxDate)) {
                System.out.println(messageMoreEvents);
            } else {
                System.out.println(messageNoMoreEvents);
                this.maxToday = 0; // Needed for looping results
            }

            System.out.println("2. Previous day");
            System.out.println("3. Next day");
            System.out.println();

            if (taskData.hasFloatingTasks()) {
                System.out
                        .println("Note: There are tasks in checklist. Type \"display checklist\" to view them");
            } else {
                System.out.println("Note: There are no floating tasks");
            }

            if (taskData.hasDonetasks()) {
                System.out
                        .println("Note: There are tasks that are done. Please type \"display done\" to view them");
            } else {
                System.out.println("Note: There are no tasks that is done");
            }

        }
        // 2nd condition

        moveToday = 0;
        isExtraInputNeeded = true;
        return true;
    }

    // @author A0105886W
    private boolean getTodayViewing(int maxToday) {
        Date beginDate;
        List<Integer> TodayIds;

        if (moveToday != 0) {
            dateViewing.add(Calendar.DAY_OF_YEAR, moveToday);
        }

        beginDate = dateViewing.getTime();

        try {
            TodayIds = taskData.getDateTasks(beginDate);
            taskData.updateDisplayID(TodayIds);
            if (displayTodayView(TodayIds, beginDate) == true) {
                return true;
            }
        } catch (NoSuchElementException e) {
            System.out.printf(messageNoDayView, dateFormatEEE.format(beginDate),
                    dateFormat.format(beginDate));
        }
        System.out.println();
        return false;

    }

    // @author A0105886W
    public boolean displayTodayView(List<Integer> dateIds, Date date) {
        taskData.updateDisplayID(dateIds);
        String taskName, taskLocation, taskDescription, taskDate, taskDuration;
        String printDate;
        Event event;

        System.out.printf(messageDateView, dateFormat.format(date));

        event = taskData.getEventMap().get(dateIds.get(0));
        printDate = dateFormatEEE.format(event.getTaskDate().getTime());
        System.out.printf(messagePrintDate, printDate);

        for (int i = 0; i < dateIds.size(); i++) {
            if (i >= maxToday) {
                System.out.printf("\n\n");
                return true;
            }
            event = taskData.getEventMap().get(dateIds.get(i));

            // 1st condition
            if (event.getTaskName() == null) {
                taskName = "";
            } else {
                taskName = event.getTaskName() + " ";
            }

            if (event.getTaskDate() != null) {
                taskDate = formatTimeDayMthYr.format(event.getTaskDate().getTime()) + " ";
            } else {
                taskDate = "";
            }

            if (event.getTaskDuration() < 1) {
                taskDuration = "";
            } else {
                taskDuration = "for " + event.getTaskDuration() + "mins. ";
            }

            if (event.getTaskDescription() == null) {
                taskDescription = "";
            } else {
                taskDescription = "desc " + event.getTaskDescription() + " ";
            }

            if (event.getTaskLocation() == null) {
                taskLocation = "";
            } else {
                taskLocation = "@ " + event.getTaskLocation() + " ";
            }

            System.out.println(taskData.getDisplayId(dateIds.get(i)) + "." + taskDate
                    + taskDuration + taskName + taskLocation + taskDescription
                    + event.getTaskPriority().toString());
        }
        System.out.printf("\n\n");
        return false;
    }

    // @author A0105886W
    private boolean processFloating() {
        System.out.println();
        ArrayList<Integer> floatingIds = new ArrayList<Integer>();
        if (chosenView == ViewOption.NEXT || chosenView == ViewOption.PREV) {
            System.out.println("Please enter option 1 only");
        }

        else {

            try {
                floatingIds = taskData.getFloatingIds(floatingIds);
                if (chosenView == ViewOption.VIEW_MORE) {
                    this.maxFloat += 5;
                } else {
                    this.maxFloat = 5;
                }

                System.out.println(messageFloating);
                if (displayFloating(floatingIds, maxFloat)) {
                    System.out.println(messageMoreEvents);
                    System.out.println("Tip: You can also display other options");
                } else {
                    System.out.println(messageNoMoreEvents);
                    System.out.println("Tip: You can also display other options");
                    this.maxFloat = 0; // Needed for looping results
                }

            } catch (NoSuchElementException e) {
                System.out.printf(messageNoFloating);
                System.out
                        .println("Tip: You can add a task without time or date which will end up in the checklist");
                System.out.println();
            }

        }

        return true;
    }

    // @author A0105886W
    public boolean displayPriority(ArrayList<Integer> floatingIds, int maxPri) {
        Event event;
        String taskName, taskLocation, taskDescription, taskDate, taskDuration;
        String displayPri;
        taskData.updateDisplayID(floatingIds);

        displayPri = taskData.getEventMap().get(floatingIds.get(0)).getTaskPriority()
                .toString();
        System.out.printf(messagePrintPri, displayPri);

        for (int i = 0; i < floatingIds.size(); i++) {

            if (i >= maxFloat) {
                System.out.printf("\n\n");
                return true;
            }

            if (!(displayPri.equals(taskData.getEventMap().get(floatingIds.get(i))
                    .getTaskPriority().toString()))) {
                System.out.println();
                displayPri = taskData.getEventMap().get(floatingIds.get(i))
                        .getTaskPriority().toString();
                System.out.printf(messagePrintPri, displayPri);
            }

            event = taskData.getEventMap().get(floatingIds.get(i));

            displayPri = event.getTaskPriority().toString();

            if (event.getTaskName() == null) {
                taskName = "";
            } else {
                taskName = event.getTaskName() + " ";
            }

            if (event.getTaskDate() != null) {
                taskDate = formatTimeDayMthYr.format(event.getTaskDate().getTime()) + " ";
            } else {
                taskDate = "";
            }

            if (event.getTaskDuration() < 1) {
                taskDuration = "";
            } else {
                taskDuration = "for " + event.getTaskDuration() + "mins. ";
            }

            if (event.getTaskDescription() == null) {
                taskDescription = "";
            } else {
                taskDescription = "desc " + event.getTaskDescription() + " ";
            }

            if (event.getTaskLocation() == null) {
                taskLocation = "";
            } else {
                taskLocation = "@ " + event.getTaskLocation() + " ";
            }

            System.out.println(taskData.getDisplayId(floatingIds.get(i)) + "." + taskDate
                    + taskDuration + taskName + taskLocation + taskDescription);
        }
        System.out.printf("\n\n");

        return false;
    }

    // @author A0105886W
    public boolean displayFloating(ArrayList<Integer> floatingIds, int maxFloat) {
        Event event;
        String taskName, taskLocation, taskDescription, taskDate, taskDuration;
        taskData.updateDisplayID(floatingIds);

        for (int i = 0; i < floatingIds.size(); i++) {

            if (i >= maxFloat) {
                System.out.printf("\n\n");
                return true;
            }
            event = taskData.getEventMap().get(floatingIds.get(i));

            if (event.getTaskName() == null) {
                taskName = "";
            } else {
                taskName = event.getTaskName() + " ";
            }

            if (event.getTaskDate() != null) {
                taskDate = formatTimeDayMthYr.format(event.getTaskDate().getTime()) + " ";
            } else {
                taskDate = "";
            }

            if (event.getTaskDuration() < 1) {
                taskDuration = "";
            } else {
                taskDuration = "for " + event.getTaskDuration() + "mins. ";
            }

            if (event.getTaskDescription() == null) {
                taskDescription = "";
            } else {
                taskDescription = "desc " + event.getTaskDescription() + " ";
            }

            if (event.getTaskLocation() == null) {
                taskLocation = "";
            } else {
                taskLocation = "@ " + event.getTaskLocation() + " ";
            }

            System.out.println(taskData.getDisplayId(floatingIds.get(i)) + "." + taskDate
                    + taskDuration + taskName + taskLocation + taskDescription
                    + event.getTaskPriority().toString());
        }
        System.out.printf("\n\n");

        return false;
    }

    // @author A0105886W
    private boolean processDateView() {
        System.out.println();
        // 1st condition
        if (chosenView == ViewOption.VIEW_MORE || chosenView == ViewOption.NOT_CHOSEN
                || chosenView == ViewOption.NEXT || chosenView == ViewOption.PREV) {
            if (chosenView == ViewOption.VIEW_MORE) {
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

            if (getDateViewing(maxDate)) {
                System.out.println(messageMoreEvents);
            } else {
                System.out.println(messageNoMoreEvents);
                this.maxDate = 0; // Needed for looping results
            }

            System.out.println("2. Previous day");
            System.out.println("3. Next day");
            System.out.println();

            if (taskData.hasFloatingTasks()) {
                System.out
                        .println("Note 1: There tasks in checklist. Type \"display checklist\" to view them");
            } else {
                System.out.println("Note 1: There are no floating tasks");
            }

            if (taskData.hasDonetasks()) {
                System.out
                        .println("Note 2: There are tasks that are done. Please type \"display done\" to view them");
            } else {
                System.out.println("Note 2: There are no tasks that is done");
            }

        }
        // 2nd condition

        moveDate = 0;
        isExtraInputNeeded = true;
        return true;
    }

    // @author A0105886W
    private boolean processWeekView() {
        // 1st condition
        System.out.println();
        if (chosenView == ViewOption.VIEW_MORE || chosenView == ViewOption.NOT_CHOSEN
                || chosenView == ViewOption.NEXT || chosenView == ViewOption.PREV) {
            if (chosenView == ViewOption.VIEW_MORE) {
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

            if (getWeekViewing(maxWeek)) {
                System.out.println(messageMoreEvents);
            } else {
                System.out.println(messageNoMoreEvents);
                this.maxWeek = 0; // Needed for looping results
            }

            System.out.println("2. Previous week");
            System.out.println("3. Next week");
            System.out.println();

            if (taskData.hasFloatingTasks()) {
                System.out
                        .println("Note 1: There are tasks in checklist. Type \"display checklist\" to view them");
            } else {
                System.out.println("Note 1: There are no floating tasks");
            }

            if (taskData.hasDonetasks()) {
                System.out
                        .println("Note 2: There are tasks that are done. Please type \"display done\" to view them");
            } else {
                System.out.println("Note 2: There are no tasks that is done");
            }

        }
        // 2nd condition

        isExtraInputNeeded = true;
        return true;
    }

    // @author A0105886W
    private boolean processMonthView() {
        System.out.println();
        if (chosenView == ViewOption.VIEW_MORE || chosenView == ViewOption.NOT_CHOSEN
                || chosenView == ViewOption.NEXT || chosenView == ViewOption.PREV) {
            if (chosenView == ViewOption.VIEW_MORE) {
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
            if (getMonthViewing(maxMonth)) {
                System.out.println(messageMoreEvents);
            } else {
                System.out.println(messageNoMoreEvents);
                this.maxMonth = 0;
            }

            System.out.println("2. Previous month");
            System.out.println("3. Next month");
            System.out.println();
        }

        if (taskData.hasFloatingTasks()) {
            System.out
                    .println("Note 1: There are tasks in checklist. Please type \"display checklist\" to view them");
        } else {
            System.out.println("Note 1: There are no floating tasks");
        }

        if (taskData.hasDonetasks()) {
            System.out
                    .println("Note 2: There are tasks that are done. Please type \"display done\" to view them");
        } else {
            System.out.println("Note 2: There are no tasks that is done");
        }

        isExtraInputNeeded = true;
        return true;
    }

    // @author A0105886W
    private boolean getDateViewing(int maxDate) {
        Date beginDate;
        List<Integer> dateIds;

        if (moveDate != 0) {
            dateViewing.add(Calendar.DAY_OF_YEAR, moveDate);
        }

        beginDate = dateViewing.getTime();

        try {
            dateIds = taskData.getDateTasks(beginDate);
            taskData.updateDisplayID(dateIds);
            if (displayDateView(dateIds, beginDate) == true) {
                return true;
            }
        } catch (NoSuchElementException e) {
            System.out.printf(messageNoDayView, dateFormatEEE.format(beginDate),
                    dateFormat.format(beginDate));
        }

        System.out.println();
        return false;

    }

    // @author A0105886W
    private boolean getWeekViewing(int maxWeek) {
        Calendar calStart = Calendar.getInstance();
        Date beginDate, endDate;
        List<Integer> weekIds;

        if (moveWeek != 0) {
            calStart.add(Calendar.WEEK_OF_MONTH, moveWeek);
        }
        calStart.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        beginDate = calStart.getTime();
        for (int i = 0; i < 7; i++) {
            calStart.add(Calendar.DATE, 1);
        }
        endDate = calStart.getTime();

        try {
            weekIds = taskData.searchEmptySlots(beginDate, endDate);
            taskData.updateDisplayID(weekIds);
            if (displayWeekView(weekIds, beginDate, endDate) == true) {
                return true;
            }
        } catch (NoSuchElementException e) {
            System.out.printf(messageNoWeekView, dateFormatMMM.format(beginDate),
                    dateFormat.format(beginDate), dateFormat.format(endDate));
        }

        System.out.println();
        return false;
    }

    // @author A0105886W
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
            monthIds = taskData.searchEmptySlots(begining, end);
            taskData.updateDisplayID(monthIds);
            if (displayMonthView(monthIds, begining, end) == true) {
                return true;
            }

        } catch (NoSuchElementException e) {
            System.out.printf(messageNoMonthView,
                    dateFormatMMM.format(begining.getTime()));
            System.out.println();

        }
        System.out.println();
        return false;
    }

    // @author A0105886W
    public boolean displayDateView(List<Integer> dateIds, Date date) {
        taskData.updateDisplayID(dateIds);
        String taskName, taskLocation, taskDescription, taskDuration, taskDate;
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
                taskName = "";
            } else {
                taskName = event.getTaskName() + " ";
            }

            if (event.getTaskDate() != null) {
                taskDate = formatTimeDayMthYr.format(event.getTaskDate().getTime()) + " ";
            } else {
                taskDate = "";
            }

            if (event.getTaskDuration() < 1) {
                taskDuration = "";
            } else {
                taskDuration = "for " + event.getTaskDuration() + "mins. ";
            }

            if (event.getTaskDescription() == null) {
                taskDescription = "";
            } else {
                taskDescription = "desc " + event.getTaskDescription() + " ";
            }

            if (event.getTaskLocation() == null) {
                taskLocation = "";
            } else {
                taskLocation = "@ " + event.getTaskLocation() + " ";
            }

            System.out.println(taskData.getDisplayId(dateIds.get(i)) + "." + taskDate
                    + taskDuration + taskName + taskLocation + taskDescription
                    + event.getTaskPriority().toString());
        }
        System.out.printf("\n\n");
        return false;
    }

    // @author A0105886W
    public boolean displayMonthView(List<Integer> monthIds, Date begining, Date end) {
        taskData.updateDisplayID(monthIds);
        Event event;
        String taskName, taskLocation, taskDescription, taskDate, taskDuration;
        String printDate;

        System.out.printf(messageMonthView, dateFormatMMM.format(begining));

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

            if (event.getTaskName() == null) {
                taskName = "";
            } else {
                taskName = event.getTaskName() + " ";
            }

            if (event.getTaskDate() != null) {
                taskDate = formatTimeDayMthYr.format(event.getTaskDate().getTime()) + " ";
            } else {
                taskDate = "";
            }

            if (event.getTaskDuration() < 1) {
                taskDuration = "";
            } else {
                taskDuration = "for " + event.getTaskDuration() + "mins. ";
            }

            if (event.getTaskDescription() == null) {
                taskDescription = "";
            } else {
                taskDescription = "desc " + event.getTaskDescription() + " ";
            }

            if (event.getTaskLocation() == null) {
                taskLocation = "";
            } else {
                taskLocation = "@ " + event.getTaskLocation() + " ";
            }

            System.out.println(taskData.getDisplayId(monthIds.get(i)) + "." + taskDate
                    + taskDuration + taskName + taskLocation + taskDescription
                    + event.getTaskPriority().toString());
        }
        System.out.printf("\n");
        return false;
    }

    // @author A0105886W
    public boolean displayWeekView(List<Integer> weekIds, Date begining, Date end) {
        taskData.updateDisplayID(weekIds);
        Event event;
        String printDate;
        String taskName, taskLocation, taskDescription, taskDate, taskDuration;

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
            if (event.getTaskName() == null) {
                taskName = "";
            } else {
                taskName = event.getTaskName() + " ";
            }

            if (event.getTaskDate() != null) {
                taskDate = formatTimeDayMthYr.format(event.getTaskDate().getTime()) + " ";
            } else {
                taskDate = "";
            }

            if (event.getTaskDuration() < 1) {
                taskDuration = "";
            } else {
                taskDuration = "for " + event.getTaskDuration() + "mins. ";
            }

            if (event.getTaskDescription() == null) {
                taskDescription = "";
            } else {
                taskDescription = "desc " + event.getTaskDescription() + " ";
            }

            if (event.getTaskLocation() == null) {
                taskLocation = "";
            } else {
                taskLocation = "@ " + event.getTaskLocation() + " ";
            }

            System.out.println(taskData.getDisplayId(weekIds.get(i)) + "." + taskDate
                    + taskDuration + taskName + taskLocation + taskDescription
                    + event.getTaskPriority().toString());
        }
        System.out.printf("\n\n");
        return false;
    }

    // @author A0105886W
    private static void setTimeToBeginningOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    // @author A0105886W
    private static void setTimeToEndofDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }

    // @author A0105886W
    private static Calendar getCalendarForNow() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar;
    }

    // @author A0105886W
    @Override
    public boolean isExtraInputNeeded() {
        return isExtraInputNeeded;
    }

    // @author A0105886W
    public static enum ViewOption {
        NOT_CHOSEN, VIEW_MORE, PREV, NEXT;
    }

    // @author A0105886W
    private void assertObjectNotNull(Object o) {
        assert (o != null);
    }
}