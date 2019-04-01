package com.servicesample;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Objects;

public class ParallelImageDownloadService extends Service {

    private HandlerThread handlerThread;
    private ServiceHandler serviceHandler;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handlerThread = new HandlerThread("MyThread");
        handlerThread.start();
        serviceHandler = new ServiceHandler(handlerThread.getLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Message msg = getMessage(intent);
        serviceHandler.sendMessage(msg);
        return START_STICKY;
    }

    //Method to download bitmap from url
    private Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Method to set intent in message
    private Message getMessage(Intent intent) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.messageKey), intent);
        Message msg = new Message();
        msg.setData(bundle);
        return msg;

    }

    //Class extending Handler which override method handleMessage
    class ServiceHandler extends Handler {

        ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Log.i("my",""+Thread.currentThread().getName());
            Intent intent = msg.getData().getParcelable(getString(R.string.messageKey));
            Bitmap bitmap = getBitmapFromURL(Objects.requireNonNull(intent).getStringExtra(getString(R.string.keyURL)));
            Intent intentBroadcast = new Intent(getString(R.string.broadcastAction));
            intentBroadcast.putExtra(getString(R.string.key_images), bitmap);
            intentBroadcast.putExtra(getString(R.string.uniqueID), intent.getIntExtra(getString(R.string.uniqueID), 0));
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intentBroadcast);
            if (!serviceHandler.hasMessages(0)) {
                stopSelf();
                handlerThread.quitSafely();
            }

        }
    }


}



