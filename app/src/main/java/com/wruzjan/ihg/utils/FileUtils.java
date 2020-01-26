package com.wruzjan.ihg.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

import java.io.File;

import androidx.core.content.FileProvider;

public class FileUtils {

    public static Uri getUriFromFile(Context context, String filePath) {
        // na Androidach 4.x FileProvider nie działa i nie udostępnia plików do GMaila/DB - jak będzie chwila to przyjrzeć się temu jak to działa
        if (Build.VERSION.SDK_INT >= 21) {
            return FileProvider.getUriForFile(context, "com.ihg.fileprovider", new File(filePath));
        } else {
            return Uri.fromFile(new File(filePath));
        }
    }
}
