package com.wruzjan.ihg.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateUtils {

    public static final DateFormat DATABASE_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
    public static final DateFormat CSV_FILE_NAME_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
}
