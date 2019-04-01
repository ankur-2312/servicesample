package com.servicesample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            //Button for SerialImageDownloadActivity
            case R.id.butSerialImageDownload:
                Intent intentSerial = new Intent(MainActivity.this, SerialImageDownloadActivity.class);
                startActivity(intentSerial);
                break;

            //Button for ParallelImageDownloadActivity
            case R.id.butParallelImageDownload:
                Intent intentParallel = new Intent(MainActivity.this, ParallelImageDownloadActivity.class);
                startActivity(intentParallel);
                break;

            default:
        }
    }

    //Method to initialize button
    private void init() {
        Button butSerialImageDownload = findViewById(R.id.butSerialImageDownload);
        Button butParallelImageDownload = findViewById(R.id.butParallelImageDownload);
        butSerialImageDownload.setOnClickListener(this);
        butParallelImageDownload.setOnClickListener(this);
    }
}
