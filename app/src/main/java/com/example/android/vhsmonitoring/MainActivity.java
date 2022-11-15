package com.example.android.vhsmonitoring;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
//    public Button buttonBeranda1;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
//        buttonBeranda1 = (Button) findViewById(R.id.buttonBeranda);
//        buttonBeranda1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("tag10", "button is clicked");
//                Intent i = new Intent(getApplicationContext(), Beranda.class);
//                startActivity(i);
//            }
//        });

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, BerandaPusat.class);
                startActivity(i);
                finish();
            }
        }, 2000);
    }

}