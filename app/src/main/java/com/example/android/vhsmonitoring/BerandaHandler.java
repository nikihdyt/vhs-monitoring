package com.example.android.vhsmonitoring;

import static android.content.ContentValues.TAG;
import static com.example.android.vhsmonitoring.login.LoginActivity.sessions_userCode;
import static com.example.android.vhsmonitoring.login.LoginActivity.sessions_userid;
import static com.example.android.vhsmonitoring.login.LoginActivity.sessions_username;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.vhsmonitoring.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BerandaHandler extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    public static final String sessions_affiliatedCustomer = "customer";
    public static final String sessions_stockId = "stock Id";

    private ImageButton btnNotification, btnArrivedStock, btnDailyPickup, btnTankRest;
    private ExtendedFloatingActionButton btnInput;
    public Boolean isInputCardVisible;
    private ConstraintLayout inputCard;
    private CardView overviewCard;
    private ImageView icTankRest;
    private TextView tvTankRest, tvTankRestTimeRemaining;
    private Button btnGoToOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);

        sharedpreferences = getSharedPreferences(LoginActivity.sessions, Context.MODE_PRIVATE);
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
        btnGoToOverview = findViewById(R.id.btn_go_to_overview);

        inputData();
        addArrivedStock();
        addDailyPickup();
        goToNotifications();
        goToOverview();

        // TODO
        // tambah validasi: jika tanggal == akhir bulan
        setTankRestAsActive();

        fillDashboardData();

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
                Intent intent = new Intent(BerandaHandler.this, InputData.class);
                intent.putExtra("inputType", "ARRIVED STOCK");
                startActivity(intent);
            }
        });
    }

    public void addDailyPickup() {
        btnDailyPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BerandaHandler.this, InputData.class);
                intent.putExtra("inputType", "DAILY PICKUP");
                startActivity(intent);
            }
        });
    }

    public void goToNotifications() {
        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BerandaHandler.this, NotificationActivity.class);
                startActivity(intent);
            }
        });
    }

    public void goToOverview() {
        btnGoToOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BerandaHandler.this, OverviewActivity.class);
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
                Intent intent = new Intent(BerandaHandler.this, StockOpname.class);
                startActivity(intent);
            }
        });
    }

    public void fillDashboardData() {
        TextView tvUserName = (TextView) findViewById(R.id.tv_user_name);
        TextView tvUserId = (TextView) findViewById(R.id.tv_user_id);
        tvUserName.setText(sharedpreferences.getString(sessions_username, ""));
        tvUserId.setText(sharedpreferences.getString(sessions_userCode, ""));
        Log.d("code", sharedpreferences.getString(sessions_userCode, ""));
        Log.d("id", sharedpreferences.getString(sessions_userid, ""));

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        TextView tvCalendar = (TextView) findViewById(R.id.tv_calendar);
        tvCalendar.setText(dateFormat.format(date).toString());
        getCustomerData();
    }

    public void getCustomerData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users_data")
                .whereEqualTo("number_id", sharedpreferences.getString(sessions_userid, ""))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                db.collection("users_data")
                                        .whereEqualTo("number_id", document.getString("affiliated_customer_id"))
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                                        TextView tvCustomerName = (TextView) findViewById(R.id.tv_customer_name);
                                                        TextView tvCustomerAddress = (TextView) findViewById(R.id.tv_customer_address);
                                                        TextView tvCustomerStock = (TextView) findViewById(R.id.tv_customer_stok);
                                                        ImageView ivLogoCustomer = (ImageView) findViewById(R.id.logo_customer);

                                                        tvCustomerName.setText(document.getString("name"));
                                                        tvCustomerAddress.setText(document.getString("address"));
                                                        tvCustomerStock.setText(document.getString("stock_type"));

                                                        String userProfilePicture = document.getString("user_logo");
                                                        Picasso.get().load(userProfilePicture)
                                                                .fit().centerCrop()
                                                                .into(ivLogoCustomer);

                                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                                        editor.putString(sessions_affiliatedCustomer, document.getString("name"));
                                                        editor.putString(sessions_stockId, document.getString("stock_id"));
                                                        editor.commit();
                                                        break;
                                                    }
                                                } else {
                                                    Log.w(TAG, "Error getting documents.", task.getException());
                                                }
                                            }
                                        });
                                break;
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        Log.d("tag", sharedpreferences.getString(sessions_stockId, ""));
        db.collection("stocks")
                .whereEqualTo("id", sharedpreferences.getString(sessions_stockId, ""))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                TextView tvCurrentStock = (TextView) findViewById(R.id.tv_current_stock);
                                TextView tvSold = (TextView) findViewById(R.id.tv_sold);

                                tvCurrentStock.setText(String.valueOf(document.getLong("tank_rest").intValue()) + " kL");
                                tvSold.setText(String.valueOf(document.getLong("sold").intValue()) + " kL");
                                break;
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }


}