package com.example.android.vhsmonitoring.handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.android.vhsmonitoring.NotificationActivity;
import com.example.android.vhsmonitoring.R;
import com.example.android.vhsmonitoring.databinding.ActivityBerandaBinding;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class Beranda extends AppCompatActivity {
    private ImageButton btnNotification, btnArrivedStock, btnDailyPickup, btnTankRest;
    private ExtendedFloatingActionButton btnInput;
    public Boolean isBtnInputVisible;
    private ConstraintLayout inputCard;
    private CardView overviewCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);

        inputCard = findViewById(R.id.input_expanded_view);
        overviewCard = findViewById(R.id.card_overview_and_input);
        btnInput = (ExtendedFloatingActionButton) findViewById(R.id.btn_input);
        btnNotification = (ImageButton) findViewById(R.id.btnNotification);
        btnArrivedStock = findViewById(R.id.btn_add_arrived_stock);
        btnDailyPickup = findViewById(R.id.btn_add_daily_pickup);

        inputData();
        addArrivedStock();
        addDailyPickup();

        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tag2", "button is clicked");
            }
        });

    }

    public void inputData() {
        inputCard.setVisibility(View.GONE);
        isBtnInputVisible = false;

        btnInput.bringToFront();
        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tag1", "button is clicked");
                if (inputCard.getVisibility()==View.GONE) {
                    TransitionManager.beginDelayedTransition(overviewCard, new AutoTransition());
                    inputCard.setVisibility(View.VISIBLE);
                } else {
                    TransitionManager.beginDelayedTransition(overviewCard, new AutoTransition());
                    inputCard.setVisibility(View.GONE);
                }
            }
        });
    }

    public void addArrivedStock() {
        btnArrivedStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Beranda.this, HandlerInputData.class);
                intent.putExtra("inputType", "ARRIVED STOCK");
                startActivity(intent);
            }
        });
    }


    public void addDailyPickup() {
        btnDailyPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Beranda.this, HandlerInputData.class);
                intent.putExtra("inputType", "DAILY PICKUP");
                startActivity(intent);
            }
        });
    }


}