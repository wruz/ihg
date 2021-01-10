package com.wruzjan.ihg.utils;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static final DateFormat DATABASE_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
    public static final DateFormat CSV_FILE_NAME_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    private DateUtils() {
        // no-op
    }

    @NonNull
    public static Calendar getDateOnlyCalendar(@NonNull Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }
}
