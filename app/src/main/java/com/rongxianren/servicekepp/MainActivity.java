package com.rongxianren.servicekepp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rongxianren.servicekepp.service.LocalService;
import com.rongxianren.servicekepp.service.RemoteService;

public class MainActivity extends Activity {


    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.btn_start_local_service);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.startService(new Intent(MainActivity.this, LocalService.class));
                MainActivity.this.startService(new Intent(MainActivity.this, RemoteService.class));
            }
        });
    }
}