package com.wruzjan.ihg.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.wruzjan.ihg.utils.model.AwaitingProtocol;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

public class NavigationUtils {

    public static void openDropBoxApp(@NonNull Context context, @NonNull List<AwaitingProtocol> awaitingProtocols) {
        ArrayList<Uri> uris = new ArrayList<>(awaitingProtocols.size());
        for (AwaitingProtocol awaitingProtocol : awaitingProtocols) {
            Uri uri = FileProvider.getUriForFile(context, "com.ihg.fileprovider", new File(awaitingProtocol.getProtocolPdfUrl()));
            uris.add(uri);
        }

        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("text/plain");
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

        try {
            context.startActivity(Intent.createChooser(intent, "Wybierz aplikację Dropbox"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "Brak klienta Dropbox na urządzeniu.", Toast.LENGTH_SHORT).show();
        }
    }
}
