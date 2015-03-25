import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SearchCommandHandler implements ICommandHandler {

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
        logger.log(Level.INFO, String.format("Input command - %s", command));
        int firstSpace = command.indexOf(' ');

        if (firstSpace < 0 || !command.matches("search (.+)")) {
            return false;
        }

        assertObjectNotNull(this);
        this.keyword = command.substring(firstSpace + 1);
        logger.log(Level.INFO, "Parsed keyword - " + keyword);
        return true;
    }

    @Override
    public boolean executeCommand() {
        try {
            List<Integer> searchActualIds = this.taskData.searchByKeyword(this.keyword);
            displaySearchResults(searchActualIds, keyword);
            return true;
        } catch (Exception e) {
            logger.log(Level.INFO, "execute exception", e);
            System.out.println(e.getMessage());
        }
        return true;
    }

    public void displaySearchResults(List<Integer> searchActualIds,
            String keyword) {
        Set<Integer> ActualIds = new HashSet<Integer>();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm dd MMM, yyyy");
        Event event;

        for (Integer Ids : searchActualIds)
            ActualIds.add(Ids);

        taskData.updateDisplayID(ActualIds);

        System.out.printf("--------------Search results based on keyWord \"%s\"-------------\n",keyword);
        System.out.printf("Total results: %d\n\n", searchActualIds.size());
        System.out.printf("%-15s %s\n", "Task ID", "Task Details");

        for (Integer searchId : searchActualIds) {
            event = taskData.getEventMap().get(searchId);
            System.out.printf("%-15s %s %s %s \"%s\"\n",
                    taskData.getDisplayId(searchId),
                    format.format(event.getTaskDate().getTime()), "@",
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
