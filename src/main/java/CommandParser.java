import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandParser {

    private static final char CHAR_SPACE = ' ';
    private static final String STRING_NULL = "";
    private static final String PATTERN_NAMED_GROUP = "\\(\\?<([a-zA-Z][a-zA-Z0-9]*)>";
    private static final String PATTERN = "(?<%3$s>%1$s|%2$s) (((?<%4$s>\".*?\")(?=($| (%2$s)))|((?<%5$s>.*?)(?=($| (%2$s)( |$))))))";
    private static final String REGEX_OR = "|";
    private static final String KEYWORD = "keyword";
    private static final String VALUE_WITH_QUOTE = "valueQuote";
    private static final String VALUE_WITHOUT_QUOTE = "valueNoQuote";
    private static final String[] keywords = new String[] { "at", "setPrior", "@", "for", "desc" };

    private static final Logger logger = Logger.getGlobal();

    private static final Map<String, Pattern> patternCommands = new LinkedHashMap<String, Pattern>();

    private static final String priorityPattern = " ?setPrior (?<priority>(H(IGH)?|M(EDIUM)?|L(OW)?).+)";
    private static final String descPattern = " ?desc \"(?<description>.+)\"";
    private static final String durationPattern = " ?for (?<duration>[0-9]+(.[0-9]+)*) ?(?<unitDuration>mins?|h((ou)?rs*)?)";
    private static final String timePattern = " ?((at|@) )*((?<hour>[0-1]?[0-9])(:(?<minute>[0-5]?[0-9]))? ?(?<ampm>(am|pm)))|((?<hour24>[0-2]?[0-9])(:(?<minute24>[0-5]?[0-9])))";
    private static final String datePattern = "(^|[\\W]+)((?<days>((((?<prefix>this|next|previous) )(?<unit>week|month|mon(day)?|tue(sday)?|wed(nesday)?|thu(rsday)?|fri(day)?|sat(urday)?|sun(day)?))"
            + "|(on) (?<weekday>mon(day)?|tue(sday)?|wed(nesday)?|thu(rsday)?|fri(day)?|sat(urday)?|sun(day)?))"
            + "|((?<today>today|yesterday|tomorrow)"
            + "|(?<date>(?<day>([1-3])?[0-9])((((-|/)(?<month>[\\d]{1,2}))|((-|/| )?(?<monthString>(Jan(uary)?|Feb(ruary)?|Mar(ch)?|Apr(il)?|May|Jun(e)?|Jul(y)?|Aug(ust)?|Sep(tember)?|Oct(ober)?|Nov(ember)?|Dec(ember)?)))))((-|/| )?(?<year>(19|20)?[\\d]{2}))?)))($|[\\W]+))";
    private static final String duePattern = " ?due (?<dueDate>.+)";
    private static final String locationPattern = " ?@ (?<location>.+)";
    private static final String namePattern = "add (?<name>.+)";

    private static final String VALUE_HOUR = "h";
    private static final String VALUE_AM = "AM";
    private static final String VALUE_PM = "PM";

    private static final String KEY_PRIORITY = "priority";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DUEDATE = "dueDate";
    private static final String KEY_DURATION = "duration";
    private static final String KEY_UNIT_DURATION = "unitDuration";
    private static final String KEY_UNIT = "unit";
    private static final String KEY_HOUR = "hour";
    private static final String KEY_MINUTE = "minute";
    private static final String KEY_AMPM = "ampm";
    private static final String KEY_HOUR24 = "hour24";
    private static final String KEY_MINUTE24 = "minute24";
    private static final String KEY_DAYS = "days";
    private static final String KEY_PREFIX = "prefix";
    private static final String KEY_WEEKDAY = "weekday";
    private static final String KEY_TODAY = "today";
    private static final String KEY_DATE = "date";
    private static final String KEY_DAY = "day";
    private static final String KEY_MONTH = "month";
    private static final String KEY_YEAR = "year";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_NAME = "name";

    private static final Pattern patternPriorityCommand = Pattern.compile(priorityPattern, Pattern.CASE_INSENSITIVE);
    private static final Pattern patternDescCommand = Pattern.compile(descPattern, Pattern.CASE_INSENSITIVE);
    private static final Pattern patternDurationCommand = Pattern.compile(durationPattern, Pattern.CASE_INSENSITIVE);
    private static final Pattern patternDueCommand = Pattern.compile(duePattern, Pattern.CASE_INSENSITIVE);
    private static final Pattern patternTimeCommand = Pattern.compile(timePattern, Pattern.CASE_INSENSITIVE);
    private static final Pattern patternDateCommand = Pattern.compile(datePattern, Pattern.CASE_INSENSITIVE);
    private static final Pattern patternLocationCommand = Pattern.compile(locationPattern, Pattern.CASE_INSENSITIVE);
    private static final Pattern patternNameCommand = Pattern.compile(namePattern, Pattern.CASE_INSENSITIVE);

    //@author A0134704M
    static {
        patternCommands.put(priorityPattern, patternPriorityCommand);
        patternCommands.put(descPattern, patternDescCommand);
        patternCommands.put(durationPattern, patternDurationCommand);
        patternCommands.put(duePattern, patternDueCommand);
        patternCommands.put(timePattern, patternTimeCommand);
        patternCommands.put(datePattern, patternDateCommand);
        patternCommands.put(locationPattern, patternLocationCommand);
        patternCommands.put(namePattern, patternNameCommand);
    }

    //@author A0134704M
    public static List<Entry<String, String>> parse(String commandName, String command) {
        List<Entry<String, String>> resultList = new ArrayList<Entry<String, String>>();

        String patternString = String.format(PATTERN, commandName,
                String.join(REGEX_OR, keywords), KEYWORD, VALUE_WITH_QUOTE,
                VALUE_WITHOUT_QUOTE);

        Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(command);
        while (matcher.find()) {
            String keyword = matcher.group(KEYWORD);
            String valueWithQuote = matcher.group(VALUE_WITH_QUOTE);
            String valueWithoutQuote = matcher.group(VALUE_WITHOUT_QUOTE);
            Entry<String, String> entry = null;

            logger.info(String.format("%s=[%s], %s=[%s], %s=[%s]\n", KEYWORD, keyword,
                    VALUE_WITH_QUOTE, valueWithQuote, VALUE_WITHOUT_QUOTE,
                    valueWithoutQuote));

            if (valueWithQuote != null) {
                entry = new SimpleEntry<String, String>(keyword, valueWithQuote);
            } else if (valueWithoutQuote != null) {
                entry = new SimpleEntry<String, String>(keyword, valueWithoutQuote);
            } else {
                entry = new SimpleEntry<String, String>(keyword, STRING_NULL);
            }

            resultList.add(entry);
        }

        return resultList;
    }

    //@author A0134704M
    private static Set<String> getCapturingGroups(String patternString) {
        Set<String> groups = new HashSet<String>();
        Matcher m = Pattern.compile(PATTERN_NAMED_GROUP, Pattern.CASE_INSENSITIVE).matcher(patternString);

        while (m.find()) {
            groups.add(m.group(1));
        }

        return groups;
    }

    //@author A0134704M
    public static Event getDetailFromCommand(String commandName, String command) {
        Event event = new Event();
        List<Entry<String, String>> list = parse(commandName, command);
        logger.info(list.toString());
        Map<String, String> taskDetailMap = new HashMap<String, String>();

        for (Entry<String, Pattern> patternCommandEntry : patternCommands.entrySet()) {
            String patternString = patternCommandEntry.getKey();
            Pattern pattern = patternCommandEntry.getValue();
            Set<String> capturingGroups = getCapturingGroups(patternString);

            Map<String, String> groupNames = parseCommandSegment(pattern, capturingGroups, list);
            logger.info(String.format("pattern=[%s]\n\tData put=[%s]", patternCommandEntry.getKey(), groupNames));
            taskDetailMap.putAll(groupNames);
        }

        if (taskDetailMap.containsKey(KEY_DESCRIPTION)) {
            event.setTaskDescription(taskDetailMap.get(KEY_DESCRIPTION));
            taskDetailMap.remove(KEY_DESCRIPTION);
        }

        if (taskDetailMap.containsKey(KEY_DUEDATE)) {
            String due = taskDetailMap.remove(KEY_DUEDATE);
            
            Set<String> dateGroups = getCapturingGroups(datePattern);
            Set<String> timeGroups = getCapturingGroups(timePattern);
            List<Entry<String, String>> entryList = new ArrayList<Entry<String, String>>();
            Map<String, String> dateTimeMap = new HashMap<String, String>();

            entryList.add(new AbstractMap.SimpleEntry<String, String>(STRING_NULL, due));
            dateTimeMap.putAll(parseCommandSegment(patternDateCommand, dateGroups, entryList));
            logger.info(dateTimeMap.toString());
            
            dateTimeMap.putAll(parseCommandSegment(patternTimeCommand, timeGroups, entryList));
            logger.info(dateTimeMap.toString());
            
            event.setTaskDueDate(getDateTime(dateTimeMap));
            if (event.getTaskDueDate() != null) {
                logger.info(event.getTaskDueDate().toString());                
            }
        }

        String duration = taskDetailMap.remove(KEY_DURATION);
        String unit = taskDetailMap.remove(KEY_UNIT_DURATION);

        if (duration != null && unit != null) {
            event.setTaskDuration(getDuration(duration, unit));
        }

        try {
            event.setTaskDate(getDateTime(taskDetailMap));
        } catch (NullPointerException e) {
            event.setTaskDate(null);
        }
        if (taskDetailMap.containsKey(KEY_LOCATION)) {
            event.setTaskLocation(taskDetailMap.get(KEY_LOCATION));
            taskDetailMap.remove(KEY_LOCATION);
        }
        if (taskDetailMap.containsKey(KEY_PRIORITY)) {
            String newPriorityString = taskDetailMap.get(KEY_PRIORITY);
            try {
                TaskPriority newPriority = TaskPriority.valueOf(newPriorityString.toUpperCase());
                event.setTaskPriority(newPriority);
            } catch (IllegalArgumentException e) {

            }
            taskDetailMap.remove(KEY_PRIORITY);
        }

        String commandTaskName = taskDetailMap.remove(KEY_NAME);
        String taskName = STRING_NULL;
        if (commandTaskName != null) {
            taskName += commandTaskName;
        }

        List<String> remainString = new ArrayList<String>();
        remainString.add(taskName);
        for (Entry<String, String> entry : list) {
            remainString.add(entry.getKey().trim());
            remainString.add(entry.getValue().trim());
        }
        taskName = String.join(" ", remainString);
        event.setTaskName(taskName);

        return event;
    }

    //@author A0134704M
    public static Calendar getDateTime(Map<String, String> taskDetailMap) {
        Calendar taskTime = Calendar.getInstance();
        int min;

        String hourString = taskDetailMap.remove(KEY_HOUR);
        String hour24String = taskDetailMap.remove(KEY_HOUR24);

        taskTime.clear(Calendar.MILLISECOND);

        boolean hasTime = (hourString != null || hour24String != null);
        if (hasTime) {
            if (hourString != null) {
                String ampm = taskDetailMap.remove(KEY_AMPM);
                int hour = Integer.parseInt(hourString) % 12;

                if (ampm != null) {
                    if (VALUE_AM.equalsIgnoreCase(ampm)) {
                        taskTime.set(Calendar.AM_PM, Calendar.AM);
                    } else if (VALUE_PM.equalsIgnoreCase(ampm)) {
                        taskTime.set(Calendar.AM_PM, Calendar.PM);
                    }

                    taskTime.set(Calendar.HOUR, hour);
                }
            } else {
                int hour = Integer.parseInt(hour24String);
                taskTime.set(Calendar.HOUR_OF_DAY, hour);
            }

            String minuteString = taskDetailMap.remove(KEY_MINUTE);
            String minute24String = taskDetailMap.remove(KEY_MINUTE24);

            if (minuteString != null) {
                min = Integer.parseInt(minuteString);
            } else if (minute24String != null) {
                min = Integer.parseInt(minute24String);
            } else {
                min = 0;
            }

            taskTime.set(Calendar.MINUTE, min);
        } else {
            taskTime = null;
        }

        Calendar taskDate = Calendar.getInstance();
        String daysString = taskDetailMap.remove(KEY_DAYS);
        String todayString = taskDetailMap.remove(KEY_TODAY);
        String dateString = taskDetailMap.remove(KEY_DATE);
        boolean hasDate = (daysString != null || todayString != null || dateString != null);

        taskDate.clear(Calendar.MILLISECOND);

        if (hasDate) {
            if (daysString != null) {
                String weekdayString = taskDetailMap.remove(KEY_WEEKDAY);
                String prefixString = taskDetailMap.remove(KEY_PREFIX);
                String unitString = taskDetailMap.remove(KEY_UNIT);

                if (prefixString != null) {
                    if ("next".equalsIgnoreCase(prefixString)) {
                        if ("month".equalsIgnoreCase(unitString)) {
                            taskDate.add(Calendar.MONTH, 1);
                        } else {
                            taskDate.add(Calendar.WEEK_OF_MONTH, 1);
                        }
                    } else if ("previous".equalsIgnoreCase(prefixString)) {
                        if ("month".equalsIgnoreCase(unitString)) {
                            taskDate.add(Calendar.MONTH, -1);
                        } else {
                            taskDate.add(Calendar.WEEK_OF_MONTH, -1);
                        }
                    } else if (unitString != null) {
                        if ("week".equalsIgnoreCase(unitString)) {
                            //taskDate.add(Calendar.WEEK_OF_MONTH, Calendar.WEEK_OF_MONTH);
                        } else if ("month".equalsIgnoreCase(unitString)) {
                            //taskDate.set(Calendar.MONTH, Calendar.MONTH);
                        } else {
                            setWeekday(taskDate, unitString);
                        }
                    }
                } else if (weekdayString != null) {
                    setWeekday(taskDate, weekdayString);
                }
            }

            if (todayString != null) {
                if ("tomorrow".equals(todayString)) {
                    taskDate.add(Calendar.DATE, +1);
                } else if ("yesterday".equals(todayString)) {
                    taskDate.add(Calendar.DATE, -1);
                }
                taskDetailMap.remove(KEY_TODAY);
            }

            if (dateString != null) {
                String dayString = taskDetailMap.remove(KEY_DAY);
                String monthString = taskDetailMap.remove(KEY_MONTH);
                String yearString = taskDetailMap.remove(KEY_YEAR);

                if (dayString != null) {
                    taskDate.set(Calendar.DATE, Integer.parseInt(dayString));
                }

                if (monthString != null) {
                    taskDate.set(Calendar.MONTH, Integer.parseInt(monthString) - 1);
                }

                if (yearString != null) {
                    taskDate.set(Calendar.YEAR, Integer.parseInt(yearString));
                }
            }
        }

        if (hasTime) {
            taskDate.set(Calendar.HOUR_OF_DAY, taskTime.get(Calendar.HOUR_OF_DAY));
            taskDate.set(Calendar.MINUTE, taskTime.get(Calendar.MINUTE));
            taskDate.set(Calendar.SECOND, 0);
        }
        
        if (!hasDate && !hasTime) {
            taskDate = null;
        }

        return taskDate;
    }

    //@author A0134704M
    public static void setWeekday(Calendar taskDate, String weekdayString) {
        SimpleDateFormat weekDayFormat = new SimpleDateFormat("E");
        try {
            Date date = weekDayFormat.parse(weekdayString);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.clear(Calendar.MILLISECOND);

            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            taskDate.set(Calendar.DAY_OF_WEEK, dayOfWeek);
            if (taskDate.before(Calendar.getInstance())) {
                taskDate.add(Calendar.WEEK_OF_MONTH, 1);
            }
        } catch (ParseException e) {

        }
    }

    //@author A0134704M
    private static int getDuration(String duration, String unit) {
        double time = Double.parseDouble(duration);
        if (unit.contains(VALUE_HOUR)) {
            time = time * 60;
        }
        return (int) Math.round(time);
    }

    //@author A0134704M
    private static Map<String, String> parseCommandSegment(Pattern pattern,
            Set<String> groups, List<Entry<String, String>> list) {

        Matcher patternMatcher;
        Map<String, String> returnMap = new HashMap<String, String>();

        Iterator<Entry<String, String>> it = list.iterator();
        while (it.hasNext()) {
            Entry<String, String> entry = it.next();
            String keyword = entry.getKey();
            String value = entry.getValue();

            patternMatcher = pattern.matcher(keyword + CHAR_SPACE + value);
            if (patternMatcher.find()) {
                for (String group : groups) {
                    String matchedString = patternMatcher.group(group);
                    if (matchedString != null) {
                        returnMap.put(group, matchedString);
                    }
                }
                int start = Math.max(0, patternMatcher.start() - keyword.length() - 1);
                int end = patternMatcher.end() - keyword.length() - 1;
                String newValue = value.substring(0, start) + value.substring(end);

                if (STRING_NULL.equals(newValue)) {
                    it.remove();
                } else {
                    entry.setValue(newValue);
                }
            }
        }
        return returnMap;
    }
}
