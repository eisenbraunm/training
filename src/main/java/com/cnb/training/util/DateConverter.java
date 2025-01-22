package com.cnb.training.util;

import java.sql.Timestamp;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateConverter {

    public static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";

    /**
     * converts a string into a date using the default format.
     * @param date a String to be converted into a Date object.
     * @return The resulting Date object, or null if the parameter cannot be parsed
     * into a date.
     */
    public static Date convertStringToDate(String date) {
        SimpleDateFormat informatter = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        ParsePosition pos = new ParsePosition(0);
        return informatter.parse(date, pos);
    }

    /**
     * converts a string into a date using the supplied format.
     * @param date a String to be converted into a Date object.
     * @param format a String format see java.text.SimpleDateFormatter for example eg. "MM/dd/yyyy"
     * @return The resulting Date object, or null if the parameter cannot be parsed
     * into a date.
     */
    public static Date convertStringToDate(String date, String format) {
        SimpleDateFormat informatter = new SimpleDateFormat(format);
        informatter.setLenient(false);
        ParsePosition pos = new ParsePosition(0);
        return informatter.parse(date, pos);
    }

    /**
     * converts a string into a Calendar using the default date format.
     * @param dateString a String to be converted into a Calendar object.
     * @return The resulting Calendar object, or null if the parameter cannot be parsed
     * into a date.
     */
    public static Calendar convertStringToCalendar(String dateString) {
        SimpleDateFormat informatter = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        ParsePosition pos = new ParsePosition(0);
        Calendar cal = Calendar.getInstance();
        cal.setTime(informatter.parse(dateString, pos));
        return cal;
    }

    /**
     * converts a string into a Calendar using the supplied date format.
     * @param dateString a String to be converted into a Calendar object.
     * @param format a String format see java.text.SimpleDateFormatter for example eg. "MM/dd/yyyy"
     * @return The resulting Calendar object, or null if the parameter cannot be parsed
     * into a date.
     */
    public static Calendar convertStringToCalendar(String dateString, String format) {
        SimpleDateFormat informatter = new SimpleDateFormat(format);
        informatter.setLenient(false);
        ParsePosition pos = new ParsePosition(0);
        Calendar cal = Calendar.getInstance();
        Date aDate = informatter.parse(dateString, pos);
        cal.setTime(aDate);
        return cal;
    }

    /**
     * converts a Calendar to a java.sql.Date
     * @param calendar a calendar to be converted into a java.sql.Date object.
     * @return The resulting java.sql.Date object, or null if the parameter was null.
     */
    public static final java.sql.Date convertCalendarToDate(Calendar calendar) {
        if (calendar != null)
            return new java.sql.Date(calendar.getTime().getTime());
        else
            return null;
    }

    /**
     * converts a java.sql.Date into a Calendar
     * @param date a java.sql.Date to be converted into a Calendar.
     * @return The resulting Calendar object, or null if the parameter was null.
     */
    public static final Calendar convertDateToCalendar(java.sql.Date date) {
        Calendar cal = null;
        if (date != null) {
            cal = Calendar.getInstance();
            cal.setTime(new Date(date.getTime()));
        }
        return cal;

    }

    /**
     * converts a date to a String in the default format
     * @param date the date to be converted
     * @return The resulting String
     */
    public static final String convertDateToString(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat();
        formatter.applyPattern(DEFAULT_DATE_FORMAT);
        return formatter.format(date);
    }

    /**
     * converts a date to a String in the supplied format
     * @param date the date to be converted
     * @param format a String format see java.text.SimpleDateFormatter for example eg. "MM/dd/yyyy"
     * @return The resulting String
     */
    public static final String convertDateToString(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat();
        formatter.applyPattern(format);
        return formatter.format(date);
    }

    /**
     * converts a Calendar to a String in the supplied format
     * @param calendar the Calendar to be converted
     * @param format a String format see java.text.SimpleDateFormatter for example eg. "MM/dd/yyyy"
     * @return The resulting String
     */
    public static final String formatCalendarToString(Calendar calendar, String format) {
        return convertDateToString(convertCalendarToDate(calendar), format);
    }

    /**
     * Converts a Calendar to a String using the default format
     * @param calendar the Calendar to be converted
     * @return The resulting String
     */
    public static final String convertCalendarToString(Calendar calendar) {
        if (calendar == null)
            return "";
        return convertDateToString(convertCalendarToDate(calendar), DEFAULT_DATE_FORMAT);
    }

    /**
     * Converts a Calendar to a Timestamp
     * @param calendar the Calendar to be converted
     * @return The resulting Timestamp
     */
    public static final Timestamp convertCalendarToTimestamp(Calendar calendar) {
        if (calendar == null)
            return new Timestamp(Calendar.getInstance().getTime().getTime());

        return (new Timestamp(calendar.getTime().getTime()));
    }

    /**
     * Converts a Timestamp to a Calendar
     * @param timestamp the Timestamp to be converted
     * @return The resulting Timestamp
     */
    public static final Calendar convertTimestampToCalendar(Timestamp timestamp) {
        if (timestamp == null)
            return null;
        Date date = new Date(timestamp.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return (cal);
    }

    /**
     * converts a java.util.Date to a java.sql.Date
     * @param utilDate the java.util.Date to be converted
     * @return The resulting java.sql.Date
     */
    public static java.sql.Date convertDateToSqlDate(Date utilDate) {
        java.sql.Date D = new java.sql.Date(utilDate.getTime());
        return D;
    }


    /**
     * converts a java.util.Date to a java.sql.Date
     * @param sqlDate the java.sql.Date to be converted
     * @return The resulting java.util.Date
     */
    public static Date convertSqlDateToDate(java.sql.Date sqlDate) {
        Date date = null;
        if (sqlDate != null)
            date = new Date(sqlDate.getTime());
        return date;
    }

    /**
     * converts a String to a java.sql.Date using the default date format
     * @param-date the String to be converted
     * @return The resulting java.sql.Date
     */
    public static java.sql.Date convertStringToSqlDate(String dateString) {
        java.sql.Date D = new java.sql.Date(convertStringToDate(dateString, DEFAULT_DATE_FORMAT).getTime());
        return D;
    }

    /**
     * converts a String to a java.sql.Date
     * @param-date the String to be converted
     * @param format the format to use for the conversion
     * @return The resulting java.sql.Date
     */
    public static java.sql.Date convertStringToSqlDate(String dateString, String format) {
        java.sql.Date D = new java.sql.Date(convertStringToDate(dateString, format).getTime());
        return D;
    }

}
