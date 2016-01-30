package fr.upem.journal.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateParser {

    private static DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
    private static DateFormat displayDateFormat = new SimpleDateFormat("EEE, dd MMM");

    public static Date parse(String dateString) throws ParseException {
        return dateFormat.parse(dateString);
    }
}
