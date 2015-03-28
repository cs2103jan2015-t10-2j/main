import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SearchCommandHandler implements ICommandHandler {

	private static final String messageDispResultsFormat = "%-15s %s %s %s \"%s\"\n";
	private static final String messageTitleFormat = "%-15s %s\n";
	private static final String simpleDateFormat = "HH:mm dd MMM, yyyy";
	
	private static final String loggerExecuteException = "execute exception";
	private static final String loggerParsedWord = "Parsed keyword - ";
	private static final String loggerInputCommand = "Input command - %s";

	private static final String messageTotalResults = "Total results: %d\n\n";
	private static final String messageSearchResults = "--------------Search results based on keyWord \"%s\"-------------\n";
	private static final String messageTaskDetailsHeader = "Task Details";
	private static final String messageTaskIdHeader = "Task ID";
    private static final String atDelimiter = "@";
	private static final String searchDelimiter = "search (.+)";
	
	private String keyword;

    private TaskData taskData;
    private static final Logger logger = Logger.getLogger("SearchCommandHandler");

    public SearchCommandHandler(TaskData taskData) {
        assertObjectNotNull(this);
        assertObjectNotNull(taskData);
        this.taskData = taskData;
    }

    @Override
    public boolean parseCommand(String command) {
        logger.log(Level.INFO, String.format(loggerInputCommand, command));
        int firstSpace = command.indexOf(' ');

        if (firstSpace < 0 || !command.matches(searchDelimiter)) {
            return false;
        }

        assertObjectNotNull(this);
        this.keyword = command.substring(firstSpace + 1);
        logger.log(Level.INFO, loggerParsedWord + keyword);
        return true;
    }

    @Override
    public boolean executeCommand() {
        try {
            List<Integer> searchActualIds = this.taskData.searchByKeyword(this.keyword);
            displaySearchResults(searchActualIds, keyword);
            return true;
        } catch (Exception e) {
            logger.log(Level.INFO, loggerExecuteException, e);
            System.out.println(e.getMessage());
        }
        return true;
    }

    public void displaySearchResults(List<Integer> searchActualIds,
            String keyword) {
        SimpleDateFormat format = new SimpleDateFormat(simpleDateFormat);
        Event event;

        taskData.updateDisplayID(searchActualIds);

        System.out.printf(messageSearchResults, keyword);
        System.out.printf(messageTotalResults, searchActualIds.size());
        System.out.printf(messageTitleFormat, messageTaskIdHeader, messageTaskDetailsHeader);

        for (Integer searchId : searchActualIds) {
            event = taskData.getEventMap().get(searchId);
            System.out.printf(messageDispResultsFormat,
                    taskData.getDisplayId(searchId),
                    format.format(event.getTaskDate().getTime()), atDelimiter,
                    event.getTaskLocation(), event.getTaskDescription());
        }
    }

    @Override
    public boolean isExtraInputNeeded() {
        return false;
    }
    
    private void assertObjectNotNull(Object o) {
        assert (o != null);
    }
}
