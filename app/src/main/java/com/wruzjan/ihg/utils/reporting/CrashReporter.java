package com.wruzjan.ihg.utils.reporting;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CrashReporter {

    private static final CrashReporter instance = new CrashReporter();
    private static final SimpleDateFormat CRASH_FILE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US);

    public static CrashReporter getInstance() {
        return instance;
    }

    public void initialize() {
        Thread.UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new FileLoggingUncaughtExceptionHandler(defaultHandler));
    }

    private class FileLoggingUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

        @Nullable
        private final Thread.UncaughtExceptionHandler defaultHandler;

        private FileLoggingUncaughtExceptionHandler(@Nullable Thread.UncaughtExceptionHandler defaultHandler) {
            this.defaultHandler = defaultHandler;
        }

        @Override
        public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
            try {
                File crashFile = createCrashFile();
                PrintStream stream = new PrintStream(crashFile);
                try {
                    e.printStackTrace(stream);
                } finally {
                    stream.close();
                }
                if (defaultHandler != null) {
                    defaultHandler.uncaughtException(t, e);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                if (defaultHandler != null) {
                    defaultHandler.uncaughtException(t, e);
                }
            }
        }

        private File createCrashFile() throws IOException {
            File crashDir = new File(pathToIhgExternalStorage() + "/CRASHES/");
            if (!crashDir.exists()) {
                crashDir.mkdirs();
            }
            File crashFile = new File(crashDir, "crash-" + CRASH_FILE_DATE_FORMAT.format(new Date()) + ".log");
            crashFile.createNewFile();
            return crashFile;
        }

        private String pathToIhgExternalStorage() {
            return Environment.getExternalStorageDirectory().toString() + "/IHG";
        }
    }
}
