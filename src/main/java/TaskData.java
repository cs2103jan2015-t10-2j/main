import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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

    //@author UNKNOWN
    public ArrayList<Integer> getDateTasks(Date date, ArrayList<Integer> floatingIds)
            throws NoSuchElementException {
        Calendar cal = Calendar.getInstance();
        ArrayList<Integer> rangeTaskIds = new ArrayList<Integer>();
        Event event;
        Date TestDate;

        if (eventMap.isEmpty()) {
            throw new NoSuchElementException(messageEmptyFile);
        }

        for (Integer taskId : eventMap.keySet()) {
            event = eventMap.get(taskId);
            cal = event.getTaskDate();
            if (cal == null) {
                floatingIds.add(taskId);
            } else {
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
        
        //For dubugging
        /*

        for (int i = 0; i < rangeTaskIds.size(); i++) {
            event = getEventMap().get(rangeTaskIds.get(i));
            System.out.println("task date and times: "
                    + formatDayMthYr.format(event.getTaskDate().getTime()));
        }

        */
        sortDatesIncreasingOrder(rangeTaskIds);
        return rangeTaskIds;
    }

    //@author UNKNOWN
    public ArrayList<Integer> searchEmptySlots(Date parsedDateStart, Date parsedDateEnd,
            ArrayList<Integer> floatingIds) throws NoSuchElementException {
        ArrayList<Integer> rangeTaskIds = new ArrayList<Integer>();
        ArrayList<Integer> sortedTaskIds = new ArrayList<Integer>();
        Calendar cal = Calendar.getInstance();

        if (eventMap.isEmpty()) {
            throw new NoSuchElementException(messageEmptyFile);
        }

        for (Integer taskId : eventMap.keySet()) {
            Event event = eventMap.get(taskId);
            cal = event.getTaskDate();
            if (cal == null) {
                floatingIds.add(taskId);
            } else {
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

    //@author UNKNOWN
    public void sortDatesIncreasingOrder(ArrayList<Integer> rangeTaskIds) {
        int limit;
        int size = rangeTaskIds.size();
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        for (limit = size - 2; limit >= 0; limit--) {
            for (int i = 0; i <= limit; i++) {
                Event event1 = eventMap.get(rangeTaskIds.get(i));
                cal1 = event1.getTaskDate();
                Date Date1 = cal1.getTime();

                Event event2 = eventMap.get(rangeTaskIds.get(i + 1));
                cal2 = event2.getTaskDate();
                Date Date2 = cal2.getTime();
                if (Date1.after(Date2)) {
                    rangeTaskIds.add(i, rangeTaskIds.remove(i + 1));
                }
            }
        }
    }

    //@author UNKNOWN
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

    //@author UNKNOWN
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
        if (eventMap.isEmpty()) {
            throw new NoSuchElementException(messageEmptyFile);
        } else {
            matchedTaskIds = findMatchedIds(keyword, matchedTaskIds);
        }
        return matchedTaskIds;
    }

    //@author A0134704M
    private ArrayList<Integer> findMatchedIds(String keyword,
            ArrayList<Integer> matchedTaskIds) {
        for (Integer taskId : eventMap.keySet()) {
            Event event = eventMap.get(taskId);

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
        } else if (event.getTaskDate() != null
                && event.getTaskDate().getTime().toString().contains(keyWord)) {
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
    
    //@author A0109239
    public void setEventMap (Map<Integer,Event> userEventMap) {
        this.eventMap = userEventMap;
    }

    //@author A0134704M
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof TaskData)) {
            return false;
        }

        TaskData taskData = (TaskData) obj;
        if (eventMap == null || eventMap == null) {
            return false;
        } else if (!eventMap.equals(eventMap)) {
            return false;
        }

        return true;
    }
}
