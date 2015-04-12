import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchCommandHandler implements ICommandHandler {

    private static final String messageDispResultsFormat = "%-15s %s %s %s %s %s \"%s\"\n";
    private static final String messageTitleFormat = "%-15s %s\n";
    private static final String simpleDateFormatTimeDayMthyr = "HH:mm dd MMM, yyyy";
    private static final String simpleDateFormatHHmm = "HHmm";
    private static final String simpleDateFormatDayMthYr = "d/M/y";
    private static final String simpleDateFormatHHColonMM = "HH:mm";

    // private static final String loggerExecuteException = "execute exception";
    // private static final String loggerParsedWord = "Parsed keyword - ";
    // private static final String loggerInputCommand = "Input command - %s";

    private static final String messageTotalResults = "Total results: %d\n\n";
    private static final String messageSearchResults = "--------------------------------Search results based on keyWord \"%s\"--------------------------------\n";
    private static final String messageEmptySlotsResults = "-------------------------------------------Empty slot results------------------------------------------\n\n";
    private static final String messageTaskDetailsHeader = "Task Details";
    private static final String messageTaskIdHeader = "Task ID";
    private static final String messageEmptyText = "%-15s %-15s %s\n";
    private static final String messageEmptyFinal = "%-15s %s\n";
    private static final String messageEmptyDescriptions = "%-15s %-15s %s %s %s %s %s %s %s \"%s\"\n";
    private static final String atDelimiter = "@";
    private static final String forDelimeter = "for";
    private static final String minsDelimeter = "mins.";
    private static final String searchDelimiter1 = "search (.+)";
    private static final String searchDelimiter2 = "search (?<time1>.+) to (?<time2>.+)";

    private String keyword;
    private String tomorrowDate = null;
    private Date parsedDateStart;
    private Date parsedDateEnd;
    private Date startTime;
    private Date EndTime;
    private Date initialDate;
    private Date finalDate;
    private int excessDuration;
    private static final SimpleDateFormat formatTimeDayMthYr;
    private static final SimpleDateFormat formatDayMthYr;
    private static final SimpleDateFormat formatHHmm;
    private static final SimpleDateFormat formatHHColonMM;
    private static final Pattern patternSearchCommand;

    private TaskData taskData;
    // private static final Logger logger =
    // Logger.getLogger("SearchCommandHandler");

    //@author UNKNOWN
    static {
        patternSearchCommand = Pattern.compile(searchDelimiter2);
        formatTimeDayMthYr = new SimpleDateFormat(simpleDateFormatTimeDayMthyr);
        formatDayMthYr = new SimpleDateFormat(simpleDateFormatDayMthYr);
        formatHHmm = new SimpleDateFormat(simpleDateFormatHHmm);
        formatHHColonMM = new SimpleDateFormat(simpleDateFormatHHColonMM);
    }

    //@author UNKNOWN
    public SearchCommandHandler(TaskData taskData) {
        assertObjectNotNull(this);
        assertObjectNotNull(taskData);
        this.taskData = taskData;
    }

    //@author UNKNOWN
    @Override
    public boolean parseCommand(String command) {
        keyword = null;
        Matcher patternMatch;
        // logger.log(Level.INFO, String.format(loggerInputCommand, command));
        int firstSpace = command.indexOf(' ');
        patternMatch = patternSearchCommand.matcher(command);

        if (firstSpace < 0 || command.matches(searchDelimiter2)) {

            if (patternMatch.find()) {
                String timeStart = patternMatch.group(1);
                String timeEnd = patternMatch.group(2);

                try {
                    this.parsedDateStart = formatDayMthYr.parse(timeStart);
                    this.parsedDateEnd = formatDayMthYr.parse(timeEnd);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }

        else if (firstSpace < 0 || command.matches(searchDelimiter1)) {
            this.keyword = command.substring(firstSpace + 1);
            // logger.log(Level.INFO, loggerParsedWord + keyword);
        }

        else {
            return false;
        }

        assertObjectNotNull(this);
        return true;
    }

    //@author UNKNOWN
    @Override
    public ICommand getCommand() {
        ICommand searchCommand = new NullCommand();
        List<Integer> searchActualIds;

        try {
            if (keyword != null) {
                searchActualIds = this.taskData.searchByKeyword(this.keyword);
                displaySearchResults(searchActualIds, keyword);
            } else {
                searchActualIds = this.taskData.searchEmptySlots(this.parsedDateStart,
                        this.parsedDateEnd);
                System.out.printf("%s", messageEmptySlotsResults);
                processEmptySlots(searchActualIds, this.parsedDateStart,
                        this.parsedDateEnd);
            }
        } catch (NoSuchElementException e) {
            // logger.log(Level.INFO, loggerExecuteException, e);
            System.out.println(e.getMessage());
        }
        return searchCommand;
    }

    //@author UNKNOWN
    /*
     * This method stores similar dates together for every iteration. It will
     * then sort its similar dates's timings in increasing order. Finally it
     * extracts the empty slots timings from each similar dates and prints it's
     * timings.
     */
    public void processEmptySlots(List<Integer> searchActualIds, Date StartDate,
            Date EndDate) {
        String currDate;
        String nextDate;
        Event eventCurr;
        Event eventNext;

        for (int i = 0; i < searchActualIds.size(); i++) {
            ArrayList<Event> commonDateIds = new ArrayList<Event>();
            eventCurr = taskData.getEventMap().get(searchActualIds.get(i));
            if (eventCurr != null) {
                currDate = formatDayMthYr.format(eventCurr.getTaskDate().getTime());
                commonDateIds.add(eventCurr);
                for (int j = i + 1; j < searchActualIds.size(); j++) {

                    eventNext = taskData.getEventMap().get(searchActualIds.get(j));
                    if (eventNext != null) {
                        nextDate = formatDayMthYr.format(eventNext.getTaskDate()
                                .getTime());

                        if (currDate.equals(nextDate)) {
                            commonDateIds.add(eventNext);
                            searchActualIds.remove(j);
                            searchActualIds.add(j, null);
                        }
                    }
                }
                if (commonDateIds.size() != 0)
                    sortEachDateTime(commonDateIds);
                extractEmptySlots(commonDateIds);
            }
        }
    }

    //@author UNKNOWN
    // Once similar dates are found, their timings for each occupied task will
    // be sorted in increasing order.
    public void sortEachDateTime(ArrayList<Event> commonDateIds) {
        int size = commonDateIds.size();
        int currTime;
        int nextTime;
        int i, limit;

        currTime = Integer.parseInt(formatHHmm.format(commonDateIds.get(0).getTaskDate()
                .getTime()));

        for (limit = size - 2; limit >= 0; limit--) {
            for (i = 0; i <= limit; i++) {
                currTime = Integer.parseInt(formatHHmm.format(commonDateIds.get(i)
                        .getTaskDate().getTime()));
                nextTime = Integer.parseInt(formatHHmm.format(commonDateIds.get(i + 1)
                        .getTaskDate().getTime()));
                if (currTime > nextTime) {
                    commonDateIds.add(i, commonDateIds.remove(i + 1));
                }
            }
        }
    }

    //@author UNKNOWN
    // Extract empty slot timings from a date and prints them in the display
    // function
    public void extractEmptySlots(ArrayList<Event> commonDateIds) {
        Calendar calStartTime = Calendar.getInstance();
        Calendar calEndTime = Calendar.getInstance();
        Calendar calCurrTime = Calendar.getInstance();

        Date currTime;
        String currentTime;
        String startingTime;

        try {
            this.startTime = formatHHColonMM.parse("00:00");
            this.EndTime = formatHHColonMM.parse("00:00");
            this.initialDate = formatDayMthYr.parse(formatDayMthYr.format(commonDateIds
                    .get(0).getTaskDate().getTime()));
            calStartTime.setTime(startTime);
            calEndTime.setTime(EndTime);
            System.out.println("Date: " + formatDayMthYr.format(initialDate));
            System.out
                    .printf(messageEmptyText, "Empty From:", "To:", "Task Occupied At:");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (Event event : commonDateIds) {
            currTime = event.getTaskDate().getTime();
            calCurrTime.setTime(currTime);

            if (tomorrowDate != null
                    && tomorrowDate.equals(formatDayMthYr.format(calCurrTime.getTime()))) {
                calStartTime.add(Calendar.MINUTE, excessDuration);
                tomorrowDate = null;
            }
            currentTime = formatHHColonMM.format(calCurrTime.getTime());
            startingTime = formatHHColonMM.format(calStartTime.getTime());
            if (!(startingTime.equals(currentTime))) {
                displayEmptyAndOccupiedResults(calStartTime, calEndTime, calCurrTime,
                        event);
            }

            calCurrTime.add(Calendar.MINUTE, event.getTaskDuration());

            try {
                this.startTime = formatHHColonMM.parse(formatHHColonMM.format(calCurrTime
                        .getTime()));
                this.finalDate = formatDayMthYr.parse(formatDayMthYr.format(calCurrTime
                        .getTime()));
                calStartTime.setTime(startTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (finalDate.after(initialDate)) {
            this.excessDuration = calCurrTime.get(Calendar.HOUR) * 60
                    + calCurrTime.get(Calendar.MINUTE);
            this.tomorrowDate = formatDayMthYr.format(calCurrTime.getTime());
            System.out.println();

        } else {
            displayEmptyAndOccupiedResults(calStartTime, calEndTime, calCurrTime, null);
        }
    }

    //@author UNKNOWN
    public void displayEmptyAndOccupiedResults(Calendar calStartTime,
            Calendar calEndTime, Calendar calCurrTime, Event event) {

        if (event != null) {
            System.out.printf(messageEmptyDescriptions,
                    formatHHColonMM.format(calStartTime.getTime()),
                    formatHHColonMM.format(calCurrTime.getTime()),
                    formatHHColonMM.format(event.getTaskDate().getTime()), forDelimeter,
                    Integer.toString(event.getTaskDuration()), minsDelimeter,
                    event.getTaskName(), atDelimiter, event.getTaskLocation(),
                    event.getTaskDescription());
        }

        else {
            System.out.printf(messageEmptyFinal,
                    formatHHColonMM.format(calStartTime.getTime()),
                    formatHHColonMM.format(calEndTime.getTime()));
            System.out.println();
        }
    }

    //@author UNKNOWN
    public void displaySearchResults(List<Integer> searchActualIds, String keyword) {
        Event event;

        taskData.updateDisplayID(searchActualIds);

        System.out.printf(messageSearchResults, keyword);
        System.out.printf(messageTotalResults, searchActualIds.size());
        System.out.printf(messageTitleFormat, messageTaskIdHeader,
                messageTaskDetailsHeader);

        for (Integer searchId : searchActualIds) {
            
            String taskName, taskLocation, taskDescription;
            
            event = taskData.getEventMap().get(searchId);
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
                taskDescription = "\"description unspecified\"";
            } else {
                taskDescription = event.getTaskDescription();
            }
            
            System.out.printf(messageDispResultsFormat, taskData.getDisplayId(searchId),
                    formatTimeDayMthYr.format(event.getTaskDate().getTime()), taskName,
                    atDelimiter, taskLocation, taskDescription, event.getTaskPriority().toString());
        }
    }

    //@author UNKNOWN
    @Override
    public boolean isExtraInputNeeded() {
        return false;
    }

    //@author UNKNOWN
    private void assertObjectNotNull(Object o) {
        assert (o != null);
    }
}