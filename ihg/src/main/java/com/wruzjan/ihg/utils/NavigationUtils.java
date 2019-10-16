package com.wruzjan.ihg.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class NavigationUtils {

    public static final int DROPBOX_SHARE_REQUEST_CODE = 100;

    public static void openDropBoxApp(@NonNull Activity activity) {
        Intent sendProtocolsIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        sendProtocolsIntent.setType("text/plain");

        List<ResolveInfo> resInfo = activity.getPackageManager().queryIntentActivities(sendProtocolsIntent, 0);
        if (!resInfo.isEmpty()) {
            ArrayList<Intent> targetedShareIntents = new ArrayList<>();

            for (ResolveInfo info : resInfo) {
                Intent targetedShare = new Intent(Intent.ACTION_SEND_MULTIPLE);
                targetedShare.setType("text/plain");
                targetedShare.setPackage(info.activityInfo.packageName.toLowerCase());
                targetedShareIntents.add(targetedShare);
            }

            Intent intentPick = new Intent();
            intentPick.setAction(Intent.ACTION_PICK_ACTIVITY);
            intentPick.putExtra(Intent.EXTRA_INTENT, sendProtocolsIntent);
            intentPick.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray());
            activity.startActivityForResult(intentPick, DROPBOX_SHARE_REQUEST_CODE);
        } else {
            Toast.makeText(activity, "Brak klienta Dropbox na urządzeniu.", Toast.LENGTH_SHORT).show();
        }
    }
}
