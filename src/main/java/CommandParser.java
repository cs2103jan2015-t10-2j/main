import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    private static final String PATTERN_NAMED_GROUP = "\\(\\?<([a-zA-Z][a-zA-Z0-9]*)>";
    private static final String PATTERN = "(?<%3$s>%1$s|%2$s) (((?<%4$s>\".*?\")(?=($| (%2$s)))|((?<%5$s>.*?)(?=($| (%2$s))))))";
    private static final String REGEX_OR = "|";
    private static final String KEYWORD = "keyword";
    private static final String VALUE_WITH_QUOTE = "valueQuote";
    private static final String VALUE_WITHOUT_QUOTE = "valueNoQuote";
    private static final String[] keywords = new String[] { "at", "setPrior", "@", "for", "desc" };

    private static final Logger logger = Logger.getGlobal();

    private static final Map<String, Pattern> patternCommands = new LinkedHashMap<String, Pattern>();

    private static final String priorityPattern = " ?setPrior (?<priority>.+)";
    private static final String descPattern = " ?desc \"(?<description>.+)\"";
    private static final String durationPattern = " ?for (?<duration>[0-9]+(.[0-9]+)*) ?(?<unit>mins?|h((ou)?rs*)?)";
    private static final String timePattern = " ?((at|@) )*((?<hour>[0-1]?[0-9])(:(?<minute>[0-5]?[0-9]))? ?(?<ampm>(am|pm)))|((?<hour24>[0-2]?[0-9])(:(?<minute24>[0-5]?[0-9])))";
    private static final String datePattern = "(^|[\\W]+)((?<days>((((?<prefix>this|next) )(?<unit>week|month|mon(day)?)))|(on) (?<weekday>mon(day)?))"
            + "|((?<today>today|yesterday|tomorrow)"
            + "|(?<date>(?<day>([1-3])?[0-9])(-|/| )?(?<month>([\\d]{1,2}|(Jan(uary)?|Mar)))((-|/| )?(?<year>(19|20)?[\\d]{2}))?)))($|[\\W]+)";
    private static final String locationPattern = " ?@ (?<location>.+)";
    private static final String namePattern = "(?<name>.+)";

    private static final String VALUE_HOUR = "h";
    private static final String VALUE_AM = "AM";
    private static final String VALUE_PM = "PM";

    private static final String KEY_PRIORITY = "priority";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DURATION = "duration";
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

    private static final Pattern patternPriorityCommand = Pattern.compile(priorityPattern);
    private static final Pattern patternDescCommand = Pattern.compile(descPattern);
    private static final Pattern patternDurationCommand = Pattern.compile(durationPattern);
    private static final Pattern patternTimeCommand = Pattern.compile(timePattern);
    private static final Pattern patternDateCommand = Pattern.compile(datePattern);
    private static final Pattern patternLocationCommand = Pattern.compile(locationPattern);
    private static final Pattern patternNameCommand = Pattern.compile(namePattern);

    static {
        patternCommands.put(priorityPattern, patternPriorityCommand);
        patternCommands.put(descPattern, patternDescCommand);
        patternCommands.put(durationPattern, patternDurationCommand);
        patternCommands.put(timePattern, patternTimeCommand);
        patternCommands.put(datePattern, patternDateCommand);
        patternCommands.put(locationPattern, patternLocationCommand);
        patternCommands.put(namePattern, patternNameCommand);
    }

    public static List<Entry<String, String>> parse(String commandName, String command) {
        List<Entry<String, String>> resultList = new ArrayList<Entry<String, String>>();

        String patternString = String.format(PATTERN, commandName,
                String.join(REGEX_OR, keywords), KEYWORD, VALUE_WITH_QUOTE,
                VALUE_WITHOUT_QUOTE);

        logger.info(patternString);

        Pattern pattern = Pattern.compile(patternString);
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
                entry = new SimpleEntry<String, String>(keyword, "");
            }

            resultList.add(entry);
        }

        return resultList;
    }

    public static Event getDetailFromCommand(String commandName, String command) {
        Event event = new Event();
        List<Entry<String, String>> list = parse(commandName, command);
        logger.info(list.toString());
        Set<String> groups = new HashSet<String>();
        Map<String, String> taskDetailMap = new HashMap<String, String>();

        for (Entry<String, Pattern> patternCommandEntry : patternCommands.entrySet()) {
            String patternString = patternCommandEntry.getKey();
            Pattern pattern = patternCommandEntry.getValue();

            groups.clear();
            Matcher m = Pattern.compile(PATTERN_NAMED_GROUP).matcher(patternString);
            while (m.find()) {
                groups.add(m.group(1));
            }

            Map<String, String> groupNames = parseCommandSegment(pattern, groups, list);
            taskDetailMap.putAll(groupNames);
        }
        if (taskDetailMap.containsKey(KEY_DESCRIPTION)) {
            event.setTaskDescription(taskDetailMap.get(KEY_DESCRIPTION));
            taskDetailMap.remove(KEY_DESCRIPTION);
        }

        String duration = taskDetailMap.remove(KEY_DURATION);
        String unit = taskDetailMap.remove(KEY_UNIT);

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
                TaskPriority newPriority = TaskPriority.valueOf(newPriorityString);
                event.setTaskPriority(newPriority);
            } catch (IllegalArgumentException e) {

            }
            taskDetailMap.remove(KEY_PRIORITY);
        }

        String commandTaskName = taskDetailMap.remove(KEY_NAME);
        String taskName = "";
        if (commandTaskName != null) {
            int start = commandTaskName.indexOf(commandName + " ");

            if (start == 0) {
                taskName += commandTaskName.substring(commandName.length() + 1);
            }
        }

        taskName += String.join(" ", taskDetailMap.values());
        event.setTaskName(taskName);

        return event;
    }

    public static Calendar getDateTime(Map<String, String> taskDetailMap) {
        Calendar taskTime = Calendar.getInstance();
        int min;

        String hourString = taskDetailMap.remove(KEY_HOUR);
        String hour24String = taskDetailMap.remove(KEY_HOUR24);

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

        if (hasDate) {
            if (daysString != null) {
                String weekdayString = taskDetailMap.remove(KEY_WEEKDAY);
                String prefixString = taskDetailMap.remove(KEY_PREFIX);
                String unitString = taskDetailMap.remove(KEY_UNIT);

                if (weekdayString != null) {
                    SimpleDateFormat weekDayFormat = new SimpleDateFormat("E");
                    try {
                        Date date = weekDayFormat.parse(weekdayString);
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);

                        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                        taskDate.set(Calendar.DAY_OF_WEEK, dayOfWeek);
                        if (taskDate.before(Calendar.getInstance())) {
                            taskDate.add(Calendar.WEEK_OF_MONTH, 1);
                        }
                    } catch (ParseException e) {

                    }
                } else if (prefixString != null && unitString != null) {
                    // TODO: next/this/previous week/month/Monday...
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
        } else {
            taskDate = null;
        }

        if (hasTime) {
            taskDate.set(Calendar.HOUR_OF_DAY, taskTime.get(Calendar.HOUR_OF_DAY));
            taskDate.set(Calendar.MINUTE, taskTime.get(Calendar.MINUTE));
            taskDate.set(Calendar.SECOND, 0);
        }

        return taskDate;
    }

    private static int getDuration(String duration, String unit) {
        double time = Double.parseDouble(duration);
        if (unit.contains(VALUE_HOUR)) {
            time = time * 60;
        }
        return (int) Math.round(time);
    }

    private static Map<String, String> parseCommandSegment(Pattern pattern,
            Set<String> groups, List<Entry<String, String>> list) {

        Matcher patternMatcher;
        Map<String, String> returnMap = new HashMap<String, String>();

        Iterator<Entry<String, String>> it = list.iterator();
        while (it.hasNext()) {
            Entry<String, String> entry = it.next();
            String keyword = entry.getKey();
            String value = entry.getValue();

            patternMatcher = pattern.matcher(keyword + ' ' + value);
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

                if ("".equals(newValue)) {
                    it.remove();
                } else {
                    entry.setValue(newValue);
                }
            }
        }
        return returnMap;
    }
}
