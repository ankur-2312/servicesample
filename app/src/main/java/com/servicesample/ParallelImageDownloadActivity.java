package com.servicesample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ParallelImageDownloadActivity extends AppCompatActivity implements View.OnClickListener {
    private String[] url;
    private ImageView ivFirst, ivSecond, ivThird;
    private Intent intent;
    private BroadcastReceiver br;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_download);
        init();
        setURl();
        onReceiveBroadcast();
    }

    //Registering the broadcastReceiver
    @Override
    protected void onStart() {
        super.onStart();
        onReceiveBroadcast();
    }

    //unRegistering the broadcastReceiver
    @Override
    protected void onStop() {
        super.onStop();
        //if(br!=null)
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(br);
    }

    //Method to initialize button and imageView and intent
    private void init() {
        Button butDownload = findViewById(R.id.butDownload);
        ivFirst = findViewById(R.id.ivFirst);
        ivSecond = findViewById(R.id.ivSecond);
        ivThird = findViewById(R.id.ivThird);
        butDownload.setOnClickListener(this);
        intent = new Intent(ParallelImageDownloadActivity.this, ParallelImageDownloadService.class);

    }

    //Method to set Image URl in String Array
    private void setURl() {
        url = new String[3];
        url[0] = getString(R.string.url1);
        url[1] = getString(R.string.url2);
        url[2] = getString(R.string.url3);
    }

    //Method to receive broadcast
    private void onReceiveBroadcast() {
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Bitmap bmp = intent.getParcelableExtra(getString(R.string.key_images));
                switch (intent.getIntExtra(getString(R.string.uniqueID), 0)) {

                    case 0:
                        ivFirst.setImageBitmap(bmp);
                        break;

                    case 1:
                        ivSecond.setImageBitmap(bmp);
                        break;

                    case 2:
                        ivThird.setImageBitmap(bmp);
                        break;

                    default:
                }
            }
        };
        IntentFilter filter = new IntentFilter(getString(R.string.broadcastAction));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(br, filter);
    }

    //Starting Service
    @Override
    public void onClick(View v) {
        for (int i = 0; i <= 2; i++) {
            intent.putExtra(getString(R.string.keyURL), url[i]);
            intent.putExtra(getString(R.string.uniqueID), i);
            startService(intent);
        }

    }
}
