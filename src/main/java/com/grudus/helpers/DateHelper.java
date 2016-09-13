package com.grudus.helpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateHelper {

    private static final String DEFAULT_DATE_FORMAT = "dd-MM-yyyy";
    private static String dateFormat = DEFAULT_DATE_FORMAT;

    public static void setDateFormat(String dateFormat) {
        DateHelper.dateFormat = dateFormat;
    }

        public static Date tryToGetDateFromString(String parsedDate) {
        Date date = null;
        try {
            date = getDateFromString(parsedDate);
        } catch (ParseException | IllegalArgumentException e) {
           e.printStackTrace();
        }
        return date;
    }

    public static Date getDateFromString(String date) throws ParseException {
        DateFormat format = new SimpleDateFormat(
                dateFormat, Locale.ENGLISH);
        return format.parse(date);
    }

    public static String getStringFromDate(Date date) {
        DateFormat format = new SimpleDateFormat(
                dateFormat, Locale.ENGLISH);
        return format.format(date);
    }

    public static long getLongFromDate(Date date) {
        return date.getTime();
    }

    public static String getReadableDataFromLong(long milliseconds) {
        Date date = new Date(milliseconds);
        return new SimpleDateFormat(dateFormat, Locale.ENGLISH).format(date);
    }
}