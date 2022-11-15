package com.example.android.vhsmonitoring;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class Beranda extends AppCompatActivity {
    private ImageButton btnNotification, btnArrivedStock, btnDailyPickup, btnTankRest;
    private ExtendedFloatingActionButton btnInput;
    public Boolean isInputCardVisible;
    private ConstraintLayout inputCard;
    private CardView overviewCard;
    private ImageView icTankRest;
    private TextView tvTankRest, tvTankRestTimeRemaining;

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
        icTankRest = findViewById(R.id.ic_TANK_REST);
        tvTankRest = findViewById(R.id.tv_TANK_REST);
        tvTankRestTimeRemaining = findViewById(R.id.tv_tank_rest_time_remaining);
        btnTankRest = findViewById(R.id.btn_add_tank_rest);

        inputData();
        addArrivedStock();
        addDailyPickup();
        goToNotifications();

        // TODO
        // tambah validasi: jika tanggal == akhir bulan
        setTankRestAsActive();

    }

    public void inputData() {
        inputCard.setVisibility(View.GONE);
        isInputCardVisible = false;

        btnInput.bringToFront();
        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                Intent intent = new Intent(Beranda.this, InputData.class);
                intent.putExtra("inputType", "ARRIVED STOCK");
                startActivity(intent);
            }
        });
    }


    public void addDailyPickup() {
        btnDailyPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Beranda.this, InputData.class);
                intent.putExtra("inputType", "DAILY PICKUP");
                startActivity(intent);
            }
        });
    }

    public void goToNotifications() {
        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Beranda.this, NotificationActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setTankRestAsActive() {
        icTankRest.setImageResource(R.drawable.ic_progress_doing);
        tvTankRest.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_700));
        tvTankRestTimeRemaining.setVisibility(View.VISIBLE);
        btnTankRest.setVisibility(View.VISIBLE);

        addTankRest();
    }

    public void addTankRest() {
        btnTankRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Beranda.this, StockOpname.class);
                startActivity(intent);
            }
        });
    }


}