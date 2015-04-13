import java.text.SimpleDateFormat;
import java.util.List;
import java.util.NoSuchElementException;

public class SearchCommandHandler implements ICommandHandler {

    // private static final String loggerExecuteException = "execute exception";
    // private static final String loggerParsedWord = "Parsed keyword - ";
    // private static final String loggerInputCommand = "Input command - %s";

    private static final String messageTotalResults = "Total results: %d\n\n";
    private static final String messageSearchResults = "=========== Search results based on keyWord \"%s\" =========== \n";
    private static final String searchDelimiter1 = "search (.+)";
    private static final String simpleDateFormatTimeDayMthyr = "HH:mm dd MMM, yyyy";

    private static final SimpleDateFormat formatTimeDayMthYr;

    private String keyword;

    private TaskData taskData;

    static {
        formatTimeDayMthYr = new SimpleDateFormat(simpleDateFormatTimeDayMthyr);
    }

    // private static final Logger logger =
    // Logger.getLogger("SearchCommandHandler");

    // @author A0105886
    public SearchCommandHandler(TaskData taskData) {
        assertObjectNotNull(this);
        assertObjectNotNull(taskData);
        this.taskData = taskData;
    }

    // @author A0105886
    @Override
    public boolean parseCommand(String command) {
        // logger.log(Level.INFO, String.format(loggerInputCommand, command));
        int firstSpace = command.indexOf(' ');

        if (firstSpace < 0 || command.matches(searchDelimiter1)) {
            this.keyword = command.substring(firstSpace + 1);
            // logger.log(Level.INFO, loggerParsedWord + keyword);
        }

        else {
            return false;
        }

        assertObjectNotNull(this);
        return true;
    }

    // @author A0105886
    @Override
    public ICommand getCommand() {
        ICommand searchCommand = new NullCommand();
        List<Integer> searchActualIds;
        System.out.println();

        try {
            searchActualIds = this.taskData.searchByKeyword(this.keyword);
            displaySearchResults(searchActualIds, keyword);
        } catch (NoSuchElementException e) {
            // logger.log(Level.INFO, loggerExecuteException, e);
            System.out.println(e.getMessage());
        }
        return searchCommand;
    }

    // @author A0105886
    public void displaySearchResults(List<Integer> searchActualIds, String keyword) {
        Event event;
        String taskName, taskLocation, taskDescription, taskDuration, taskDate;

        taskData.updateDisplayID(searchActualIds);

        System.out.printf(messageSearchResults, keyword);
        System.out.printf(messageTotalResults, searchActualIds.size());
        for (Integer searchId : searchActualIds) {

            event = taskData.getEventMap().get(searchId);
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

            System.out.println(taskData.getDisplayId(searchId) + "." + taskDate
                    + taskDuration + taskName + taskLocation + taskDescription
                    + event.getTaskPriority().toString());
            System.out.println();
        }
    }

    // @author A0105886
    @Override
    public boolean isExtraInputNeeded() {
        return false;
    }

    // @author A0105886
    private void assertObjectNotNull(Object o) {
        assert (o != null);
    }
}