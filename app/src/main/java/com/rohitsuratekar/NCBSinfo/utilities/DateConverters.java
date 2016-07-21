package com.rohitsuratekar.NCBSinfo.utilities;

import android.util.Log;

import com.rohitsuratekar.NCBSinfo.constants.DateFormats;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * All date conversions are handled through this class.
 * Standard timings are given in DateFormats enum
 */
public class DateConverters {

    private final String TAG = getClass().getSimpleName();


    public String convertToString(Date dateFormat, DateFormats format) {
        return new SimpleDateFormat(format.getFormat(), Locale.getDefault()).format(dateFormat);
    }

    public String convertToString(Calendar dateFormat, DateFormats format) {
        return new SimpleDateFormat(format.getFormat(), Locale.getDefault()).format(dateFormat.getTime());
    }

    /**
     * @param stringFormat : Input format
     * @return : Formatted Date (uses current time if default field is missing)
     */
    public Date convertToDate(String stringFormat) {
        String[] extracted = completeFormat(stringFormat);
        if (extracted != null) {

            try {
                return new SimpleDateFormat(extracted[1], Locale.getDefault()).parse(extracted[0]);
            } catch (ParseException e) {
                e.printStackTrace();
                Log.e(TAG, "Unable to parse date in convertToDate() :" + stringFormat);
            }

        }
        Log.e(TAG, "No format found convertToDate()");
        return null;
    }


    /**
     * @param stringFormat : Uses this format
     * @return : Returns calendar (uses current time if default field is missing)
     */

    public Calendar convertToCalendar(String stringFormat) {

        String[] extracted = completeFormat(stringFormat);
        if (extracted != null) {
            try {
                return DateToCalender(new SimpleDateFormat(extracted[1], Locale.getDefault()).parse(extracted[0]));
            } catch (ParseException e) {
                e.printStackTrace();
                Log.e(TAG, "Unable to parse date in convertToCalendar() :" + stringFormat);
            }

        }
        Log.e(TAG, "No format found convertToCalendar() :" + stringFormat);
        return null;
    }

    /**
     * @param input        : Input String
     * @param targetFormat : Target format you want
     * @return : It will add current time or date if it is not present in given input
     */
    public String convertFormat(String input, DateFormats targetFormat) {
        String[] detectedString = completeFormat(input);
        if (detectedString != null) {

            try {
                Date date = new SimpleDateFormat(detectedString[1], Locale.getDefault()).parse(detectedString[0]);
                return new SimpleDateFormat(targetFormat.getFormat(), Locale.getDefault()).format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                Log.e(TAG, "Unable to parse date in convertFormat() :" + input);
            }
        }
        Log.e(TAG, "No format found convertFormat()");
        return null;
    }

    /**
     * Converting 3 line code into single liner
     *
     * @param date : Input
     * @return : Returns calender
     */
    public static Calendar DateToCalender(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    private static final String ONLY_TIME = "onlyTime";
    private static final String ONLY_DATE = "onlyDate";
    private static final String BOTH = "both";

    private static final String[][] patternList = {
            {"^\\d{1,2}:\\d{2}$", "HH:mm", ONLY_TIME},
            {"^\\d{1,2}:\\d{2}\\s(?i)(am|pm)$", "hh:mm a", ONLY_TIME},
            {"^\\d{1,2}/\\d{1,2}/\\d{4}$", "dd/mm/yyyy", ONLY_DATE},
            {"^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}$", "dd/mm/yyyy HH:mm", BOTH},
            {"^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd/mm/yyyy HH:mm:ss", BOTH},
            {"^\\d{1,2}:\\d{2}:\\d{2}\\s(?i)(am|pm)\\s\\d{1,2}\\s[a-z]{3}\\s\\d{2}$", "hh:mm:ss a dd MMM yy", BOTH},
            {"^\\d{1,2}\\s[a-z]{3}\\s\\d{4}$", "dd MMM yyyy", ONLY_DATE},
            {"([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$", "HH:mm:ss", ONLY_TIME},
    };


    private static String[] extractFormat(String input) {
        for (String[] pair : patternList) {
            if (input.trim().toLowerCase().matches(pair[0])) {
                return pair;
            }
        }
        Log.e("ERROR", "Unable to extract in extractFormat() :" + input);
        return null;
    }

    public static String[] completeFormat(String string) {
        String[] format = extractFormat(string);
        String timeString = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        String dateString = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        if (format != null) {
            String FormatforTime = "dd/MM/yyyy " + format[1];
            String FormatforDate = format[1] + " HH:mm";
            switch (format[2]) {
                case ONLY_DATE:
                    return new String[]{string + " " + timeString, FormatforDate};
                case ONLY_TIME:
                    return new String[]{dateString + " " + string, FormatforTime};
                case BOTH:
                    return new String[]{string, format[1]};
            }
        }
        Log.e("ERROR", "No format found completeFormat() :" + string);
        return null;
    }

    /**
     * Following code is copy pasted from http://stackoverflow.com/questions/3389348/parse-any-date-in-java
     */
    private static final Map<String, String> DATE_FORMAT_REGEXPS = new HashMap<String, String>() {{
        put("^\\d{8}$", "yyyyMMdd");
        put("^\\d{1,2}-\\d{1,2}-\\d{4}$", "dd-MM-yyyy");
        put("^\\d{4}-\\d{1,2}-\\d{1,2}$", "yyyy-MM-dd");
        put("^\\d{1,2}/\\d{1,2}/\\d{4}$", "MM/dd/yyyy");
        put("^\\d{4}/\\d{1,2}/\\d{1,2}$", "yyyy/MM/dd");
        put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}$", "dd MMM yyyy");
        put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}$", "dd MMMM yyyy");
        put("^\\d{12}$", "yyyyMMddHHmm");
        put("^\\d{8}\\s\\d{4}$", "yyyyMMdd HHmm");
        put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}$", "dd-MM-yyyy HH:mm");
        put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy-MM-dd HH:mm");
        put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}$", "MM/dd/yyyy HH:mm");
        put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy/MM/dd HH:mm");
        put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMM yyyy HH:mm");
        put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMMM yyyy HH:mm");
        put("^\\d{14}$", "yyyyMMddHHmmss");
        put("^\\d{8}\\s\\d{6}$", "yyyyMMdd HHmmss");
        put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd-MM-yyyy HH:mm:ss");
        put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy-MM-dd HH:mm:ss");
        put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "MM/dd/yyyy HH:mm:ss");
        put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy/MM/dd HH:mm:ss");
        put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMM yyyy HH:mm:ss");
        put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMMM yyyy HH:mm:ss");
    }};

    /**
     * Determine SimpleDateFormat pattern matching with the given date string. Returns null if
     * format is unknown. You can simply extend DateUtil with more formats if needed.
     *
     * @param dateString The date string to determine the SimpleDateFormat pattern for.
     * @return The matching SimpleDateFormat pattern, or null if format is unknown.
     * @see SimpleDateFormat
     */
    public static String determineDateFormat(String dateString) {
        for (String regexp : DATE_FORMAT_REGEXPS.keySet()) {
            if (dateString.toLowerCase().matches(regexp)) {
                return DATE_FORMAT_REGEXPS.get(regexp);
            }
        }
        return null; // Unknown format.
    }

}
