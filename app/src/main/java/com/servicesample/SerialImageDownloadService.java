package com.servicesample;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class SerialImageDownloadService extends IntentService {

    public SerialImageDownloadService() {
        super("MyService");
    }

    //Download image one at a time
    @Override
    protected void onHandleIntent(Intent intent) {
        Bitmap bitmap = getBitmapFromURL(intent.getStringExtra(getString(R.string.keyURL)));
        Intent intentBroadcast = new Intent(getString(R.string.broadcastactionserial));
        intentBroadcast.putExtra(getString(R.string.key_images), bitmap);
        intentBroadcast.putExtra(getString(R.string.uniqueID), intent.getIntExtra(getString(R.string.uniqueID), 0));
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intentBroadcast);
    }

    //Method to download bitmap from URl
    private Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
