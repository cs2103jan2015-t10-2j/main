import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TaskData implements Serializable {

    private static final String messageNoResults = "Your search request returned 0 results";
    private static final String messageEmptyFile = "File is empty!";
    private static final String simpleDateFormatDayMthYr = "d/M/y";
    private static final String simpleDateFormatHHmm = "HHmm";

    private static final long serialVersionUID = 6897919790578039077L;

    private static final SimpleDateFormat formatHHmm;

    private Map<Integer, Event> eventMap;
    private Map<Integer, Integer> displayIDToActualIDMap;
    private Map<Integer, Integer> actualIDToDisplayIDMap;

    private static final SimpleDateFormat formatDayMthYr;

    //@author A0134704M
    static {
        formatHHmm = new SimpleDateFormat(simpleDateFormatHHmm);
        formatDayMthYr = new SimpleDateFormat(simpleDateFormatDayMthYr);
    }

    //@author A0134704M
    public TaskData() {
        eventMap = new LinkedHashMap<Integer, Event>();
        this.displayIDToActualIDMap = new LinkedHashMap<Integer, Integer>();
        this.actualIDToDisplayIDMap = new LinkedHashMap<Integer, Integer>();
    }


    //@author A0134704M
    public boolean isDisplayIdMapEmpty() {
        return displayIDToActualIDMap.size() == 0;
    }    
    

    //@author A0105886W
    public ArrayList<Integer> getSortedPriIds() throws NoSuchElementException{
        ArrayList<Integer> tasksId = new ArrayList<Integer>();

        int i;
        int limit;
        int currPriLevel = 0;
        int nextPriLevel = 0;
        String currPri;
        String nextPri;
        Event event;

        if (this.eventMap.isEmpty()) {
            throw new NoSuchElementException(messageEmptyFile);
        }

        // get all the ids from map
        for (Integer taskId : this.eventMap.keySet()) {
            tasksId.add(taskId);
        }

        if (tasksId.size() == 0) {
            throw new NoSuchElementException(messageNoResults);
        }

        int size = tasksId.size();

        for (limit = size - 2; limit >= 0; limit--) {
            for (i = 0; i <= limit; i++) {
                event = getEventMap().get(tasksId.get(i));
                currPri = event.getTaskPriority().toString().toLowerCase();
                if (currPri.equals("low")) {
                    currPriLevel = 1;
                } else if (currPri.equalsIgnoreCase("medium")) {
                    currPriLevel = 2;
                } else if (currPri.equals("high")) {
                    currPriLevel = 3;
                }

                event = getEventMap().get(tasksId.get(i + 1));
                nextPri = event.getTaskPriority().toString().toLowerCase();

                if (nextPri.equals("low")) {
                    nextPriLevel = 1;
                } else if (nextPri.equalsIgnoreCase("medium")) {
                    nextPriLevel = 2;
                } else if (nextPri.equals("high")) {
                    nextPriLevel = 3;
                }

                if (currPriLevel < nextPriLevel) {
                    tasksId.add(i, tasksId.remove(i + 1));
                }
            }
        }

        return tasksId;
    }

    //@author A0105886W
    public boolean hasDonetasks() {
        Event event;

        for (Integer taskId : this.eventMap.keySet()) {
            event = this.eventMap.get(taskId);
            if (event.isDone()) {
                return true;
            }
        }

        return false;
    }

    //@author a0105886W
    public boolean hasFloatingTasks() {

        Event event;
        Calendar cal = Calendar.getInstance();

        for (Integer taskId : this.eventMap.keySet()) {
            event = this.eventMap.get(taskId);
            cal = event.getTaskDate();
            if (cal == null) {
                return true;
            }
        }

        return false;

    }

    //@author a0105886W
    public ArrayList<Integer> getDoneIds(ArrayList<Integer> doneIds)
            throws NoSuchElementException {
        Event event;

        if (this.eventMap.isEmpty()) {
            throw new NoSuchElementException(messageEmptyFile);
        }

        for (Integer taskId : this.eventMap.keySet()) {
            event = this.eventMap.get(taskId);
            if (event.isDone()) {
                doneIds.add(taskId);
            }
        }

        if (doneIds.size() == 0) {
            throw new NoSuchElementException(messageNoResults);
        }

        return doneIds;
    }

    //@author A0105886W
    public ArrayList<Integer> getFloatingIds(ArrayList<Integer> floatingIds)
            throws NoSuchElementException {
        Event event;
        Calendar cal = Calendar.getInstance();

        if (this.eventMap.isEmpty()) {
            throw new NoSuchElementException(messageEmptyFile);
        }

        for (Integer taskId : this.eventMap.keySet()) {
            event = this.eventMap.get(taskId);
            cal = event.getTaskDate();
            if (cal == null) {
                floatingIds.add(taskId);
            }
        }

        if (floatingIds.size() == 0) {
            throw new NoSuchElementException(messageNoResults);
        }

        return floatingIds;

    }

    //@author A0105886W
    public ArrayList<Integer> getDateTasks(Date date) throws NoSuchElementException {
        Calendar cal = Calendar.getInstance();
        ArrayList<Integer> rangeTaskIds = new ArrayList<Integer>();
        Event event;
        Date TestDate;

        if (this.eventMap.isEmpty()) {
            throw new NoSuchElementException(messageEmptyFile);
        }

        for (Integer taskId : this.eventMap.keySet()) {
            event = this.eventMap.get(taskId);
            cal = event.getTaskDate();

            if (cal != null) {
                try {
                    TestDate = formatDayMthYr.parse(formatDayMthYr.format(cal.getTime()));
                    if (TestDate.equals(date)) {
                        rangeTaskIds.add(taskId);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        if (rangeTaskIds.size() == 0) {
            throw new NoSuchElementException(messageNoResults);
        }

        // For dubugging
        /*
         * 
         * for (int i = 0; i < rangeTaskIds.size(); i++) { event =
         * getEventMap().get(rangeTaskIds.get(i));
         * System.out.println("task date and times: " +
         * formatDayMthYr.format(event.getTaskDate().getTime())); }
         */
        sortDatesIncreasingOrder(rangeTaskIds);
        return rangeTaskIds;
    }

    //@author A0134704M
    public List<Event> getTaskInDateRange(Calendar startTime, Calendar endTime,
            boolean isDueDate) {
        Stream<Event> eventStream = eventMap.values().stream();
        eventStream = eventStream.filter(getTaskWithinDatePredicate(startTime, endTime, isDueDate));
        eventStream = eventStream.sorted(getDateComparator(false));
        List<Event> selectedList = eventStream.collect(Collectors.toList());

        return selectedList;
    }

    //@author A0134704M
    public List<Event> getOverdueTask() {
        List<Event> selectedList = eventMap.values().stream()
                .filter(getOverduePredicate()).filter(getIsDonePredicate(false))
                .sorted(getDateComparator(true)).collect(Collectors.toList());

        return selectedList;
    }

    //@author A0134704M
    public Predicate<Event> getTaskWithinDatePredicate(Calendar startSearchTime,
            Calendar endSearchTime, boolean isDueDate) {
        Predicate<Event> predicate = (event -> {
            if (isDueDate) {
                Calendar dueDate = event.getTaskDueDate();
                return dueDate != null && !dueDate.before(startSearchTime)
                        && !dueDate.after(endSearchTime);
            } else {
                Calendar startEventTime = event.getTaskDate();

                if (startEventTime == null) {
                    return false;
                } else {
                    int duration = event.getTaskDuration();
                    assert (duration >= 0);

                    Calendar endEventTime = Calendar.getInstance();
                    endEventTime.setTimeInMillis(startEventTime.getTimeInMillis());
                    endEventTime.add(Calendar.MINUTE, duration);

                    boolean isEventStartAfterEndSearchTime = startEventTime.after(endSearchTime);
                    boolean isEventEndBeforeStartSearchTime = endEventTime.before(startSearchTime);

                    Logger.getGlobal().info(
                            String.format("search: [%s,%s], event: [%s,%s], isEventStartAfterEndSearchTime=%b, isEventEndBeforeStartSearchTime=%b",
                                    DateFormat.getDateTimeInstance().format(startSearchTime.getTime()),
                                    DateFormat.getDateTimeInstance().format(endSearchTime.getTime()),
                                    DateFormat.getDateTimeInstance().format(startEventTime.getTime()),
                                    DateFormat.getDateTimeInstance().format(endEventTime.getTime()),
                                            isEventEndBeforeStartSearchTime,
                                            isEventEndBeforeStartSearchTime));

                    return !isEventStartAfterEndSearchTime && !isEventEndBeforeStartSearchTime;   
                }
            }
        });

        return predicate;
    }

    //@author A0134704M
    private Predicate<Event> getOverduePredicate() {
        Calendar currentTime = Calendar.getInstance();

        Predicate<Event> predicate = (event -> {
            Calendar eventDate = event.getTaskDueDate();
            return eventDate != null && eventDate.before(currentTime);
        });

        return predicate;
    }

    //@author A0134704M
    private Predicate<Event> getIsDonePredicate(boolean isDone) {
        return (event -> (event.isDone() == isDone));
    }

    //@author A0134704M
    private Comparator<? super Event> getDateComparator(boolean isDueDate) {
        return (e1, e2) -> {
            Calendar e1Time;
            Calendar e2Time;
            
            if (isDueDate) {
                e1Time = e1.getTaskDueDate();
                e2Time = e2.getTaskDueDate();
            } else {
                e1Time = e1.getTaskDate();
                e2Time = e2.getTaskDate();
            }
            
            if (e1Time == null) {
                return -1;
            } else if (e2Time == null) {
                return 1;
            } else {
                boolean before = e1Time.before(e2Time);
                boolean after = e1Time.after(e2Time);

                if (before) {
                    return -1;
                } else if (after) {
                    return 1;
                } else {
                    return 0;
                }                
            }
        };
    }

    //@author A0134704M
    public List<Integer> getTaskWithinDateRange(Calendar startTime, Calendar endTime,
            boolean isDueDate, TaskType type) {
        ArrayList<Integer> taskIds = new ArrayList<Integer>();

        for (Entry<Integer, Event> entry : eventMap.entrySet()) {
            Calendar eventDate;
            if (isDueDate) {
                eventDate = entry.getValue().getTaskDueDate();
            } else {
                eventDate = entry.getValue().getTaskDate();
            }

            boolean isOnOrAfterStartTime = !startTime.before(eventDate);
            boolean isOnOrBeforeEndTime = !startTime.after(eventDate);
            boolean isSameTaskType = (entry.getValue().getTaskType() == type);
            if (isOnOrAfterStartTime && isOnOrBeforeEndTime && isSameTaskType) {
                taskIds.add(entry.getKey());
            }
        }

        return taskIds;
    }

    //@author A0105886W
    public ArrayList<Integer> searchEmptySlots(Date parsedDateStart, Date parsedDateEnd)
            throws NoSuchElementException {
        ArrayList<Integer> rangeTaskIds = new ArrayList<Integer>();
        ArrayList<Integer> sortedTaskIds = new ArrayList<Integer>();
        Calendar cal = Calendar.getInstance();

        if (this.eventMap.isEmpty()) {
            throw new NoSuchElementException(messageEmptyFile);
        }

        for (Integer taskId : this.eventMap.keySet()) {
            Event event = this.eventMap.get(taskId);
            cal = event.getTaskDate();
            if (cal != null) {
                Date TestDate = cal.getTime();
                if (TestDate.after(parsedDateStart) && TestDate.before(parsedDateEnd)) {
                    rangeTaskIds.add(taskId);
                }
            }
        }

        if (rangeTaskIds.size() == 0) {
            throw new NoSuchElementException(messageNoResults);
        }

        sortDatesIncreasingOrder(rangeTaskIds);
        sortedTaskIds = processCommonDateTimes(rangeTaskIds);
        return sortedTaskIds;
    }

    //@author A0105886W
    public void sortDatesIncreasingOrder(ArrayList<Integer> rangeTaskIds) {
        int limit;
        int size = rangeTaskIds.size();
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        for (limit = size - 2; limit >= 0; limit--) {
            for (int i = 0; i <= limit; i++) {
                Event event1 = this.eventMap.get(rangeTaskIds.get(i));
                cal1 = event1.getTaskDate();
                Date Date1 = cal1.getTime();

                Event event2 = this.eventMap.get(rangeTaskIds.get(i + 1));
                cal2 = event2.getTaskDate();
                Date Date2 = cal2.getTime();
                if (Date1.after(Date2)) {
                    rangeTaskIds.add(i, rangeTaskIds.remove(i + 1));
                }
            }
        }
    }

    //@author A0105886W
    public ArrayList<Integer> processCommonDateTimes(ArrayList<Integer> rangeTaskIds) {
        ArrayList<Integer> tempArrayIds = new ArrayList<Integer>();
        ArrayList<Integer> sortedArrayIds = new ArrayList<Integer>();
        String dateInitial;
        String dateCurrent;
        Event event;

        event = getEventMap().get(rangeTaskIds.get(0));
        dateInitial = formatDayMthYr.format(event.getTaskDate().getTime());
        tempArrayIds.add(rangeTaskIds.get(0));

        for (int i = 1; i < rangeTaskIds.size(); i++) {
            event = getEventMap().get(rangeTaskIds.get(i));
            dateCurrent = formatDayMthYr.format(event.getTaskDate().getTime());
            if (dateInitial.equals(dateCurrent)) {
                tempArrayIds.add(rangeTaskIds.get(i));
            } else if (!dateInitial.equals(dateCurrent)) {
                event = getEventMap().get(rangeTaskIds.get(i));
                dateInitial = formatDayMthYr.format(event.getTaskDate().getTime());
                sortTimeIncreasingOrder(tempArrayIds);
                for (int j = 0; j < tempArrayIds.size(); j++) {
                    sortedArrayIds.add(tempArrayIds.get(j));
                }
                tempArrayIds = new ArrayList<Integer>();
                tempArrayIds.add(rangeTaskIds.get(i));
            }

        }

        if (tempArrayIds.size() > 0) {
            sortTimeIncreasingOrder(tempArrayIds);
            for (int j = 0; j < tempArrayIds.size(); j++) {
                sortedArrayIds.add(tempArrayIds.get(j));
            }

        }

        return sortedArrayIds;
    }

    //@author A0105886W
    public void sortTimeIncreasingOrder(ArrayList<Integer> rangeTaskIds) {
        int i;
        int limit;
        int currTime;
        int nextTime;
        int size = rangeTaskIds.size();
        Event event;

        for (limit = size - 2; limit >= 0; limit--) {
            for (i = 0; i <= limit; i++) {
                event = getEventMap().get(rangeTaskIds.get(i));
                currTime = Integer.parseInt(formatHHmm.format(event.getTaskDate()
                        .getTime()));
                event = getEventMap().get(rangeTaskIds.get(i + 1));
                nextTime = Integer.parseInt(formatHHmm.format(event.getTaskDate()
                        .getTime()));
                if (currTime > nextTime) {
                    rangeTaskIds.add(i, rangeTaskIds.remove(i + 1));
                }
            }
        }
    }

    //@author A0134704M
    public ArrayList<Integer> searchByKeyword(String keyword)
            throws NoSuchElementException {
        ArrayList<Integer> matchedTaskIds = new ArrayList<Integer>();
        if (this.eventMap.isEmpty()) {
            throw new NoSuchElementException(messageEmptyFile);
        } else {
            matchedTaskIds = findMatchedIds(keyword, matchedTaskIds);
        }
        return matchedTaskIds;
    }

    //@author A0134704M
    private ArrayList<Integer> findMatchedIds(String keyword,
            ArrayList<Integer> matchedTaskIds) {
        for (Integer taskId : this.eventMap.keySet()) {
            Event event = this.eventMap.get(taskId);

            if (this.hasKeyWord(event, keyword)) {
                matchedTaskIds.add(taskId);
            }
        }

        if (matchedTaskIds.size() == 0) {
            throw new NoSuchElementException(messageNoResults);
        }
        return matchedTaskIds;
    }

    //@author A0134704M
    public int getActualId(int displayId) throws NoSuchElementException {
        Integer actualId = this.displayIDToActualIDMap.get(displayId);

        if (actualId == null) {
            throw new NoSuchElementException();
        } else {
            return actualId;
        }
    }

    //@author A0134704M
    public int getDisplayId(int actualId) throws NoSuchElementException {
        Integer displayId = this.actualIDToDisplayIDMap.get(actualId);

        if (displayId == null) {
            throw new NoSuchElementException();
        } else {
            return displayId;
        }
    }

    //@author A0134704M
    public void updateDisplayID(List<Integer> actualIDs) {
        int displayID = 1;
        this.displayIDToActualIDMap.clear();
        this.actualIDToDisplayIDMap.clear();

        for (Integer actualID : actualIDs) {
            this.displayIDToActualIDMap.put(displayID, actualID);
            this.actualIDToDisplayIDMap.put(actualID, displayID);
            displayID++;
        }
    }

    //@author A0134704M
    public boolean hasKeyWord(Event event, String keyWord) {

        if (event == null || keyWord == null) {
            return false;
        }

        keyWord = keyWord.toLowerCase();

        if (Integer.toString(event.getTaskID()) != null
                && Integer.toString(event.getTaskID()).contains(keyWord)) {
            return true;
        } else if (event.getTaskName() != null
                && event.getTaskName().toLowerCase().contains(keyWord)) {
            return true;
        } else if (event.getTaskLocation() != null
                && event.getTaskLocation().toLowerCase().contains(keyWord)) {
            return true;
        } else if (event.getTaskDescription() != null
                && event.getTaskDescription().toLowerCase().contains(keyWord)) {
            return true;
        } else if (event.getTaskPriority() != null
                && event.getTaskPriority().toString().toLowerCase().contains(keyWord)) {
            return true;
        }

        return false;
    }

    //@author A0134704M
    public Map<Integer, Event> getEventMap() {
        return eventMap;
    }

    //@author A0109239A
    public void setEventMap(Map<Integer, Event> userEventMap) {
        this.eventMap = userEventMap;
    }

    //@author A0134704M
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof TaskData)) {
            return false;
        }

        TaskData taskData = (TaskData) obj;
        if (eventMap == null || taskData.eventMap == null) {
            return false;
        } else if (!eventMap.equals(taskData.eventMap)) {
            return false;
        }

        return true;
    }
}
