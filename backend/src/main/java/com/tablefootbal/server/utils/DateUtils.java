package com.tablefootbal.server.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public static String stringFromDate(Date date) {
        return dateFormat.format(date);
    }

    public static Date dateFromString(String dateString) throws ParseException {
        return dateFormat.parse(dateString);
    }
}
