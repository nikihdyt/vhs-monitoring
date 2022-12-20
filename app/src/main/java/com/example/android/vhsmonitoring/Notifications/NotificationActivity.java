package com.example.android.vhsmonitoring.Notifications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.android.vhsmonitoring.Admin.Model.ClaimContract;
import com.example.android.vhsmonitoring.Admin.Model.CustomerAddress;
import com.example.android.vhsmonitoring.Admin.Model.NotificationsData;
import com.example.android.vhsmonitoring.Admin.Model.StockData;
import com.example.android.vhsmonitoring.Admin.Model.Users.UserCustomer;
import com.example.android.vhsmonitoring.Admin.Model.Users.UserHandler;
import com.example.android.vhsmonitoring.Dashboard.Dashboard;
import com.example.android.vhsmonitoring.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    private ImageButton btnBack;
    public List<NotificationsData> notificationsData = new ArrayList<>();
    public DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    public Gson gsonData = new Gson();
    public String jSonData;

    public UserHandler handler;
    public UserCustomer handlerCustomerData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // get handler data from intent
        Intent data = getIntent();
        String userId = data.getStringExtra("user");

        btnBack = findViewById(R.id.btn_back);
        backToBeranda();

        Log.d("userid", userId);
        displayCustomerNotifications(userId);
    }

    public void backToBeranda() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void displayCustomerNotifications(String userid) {
        db.child("data_notifications").orderByChild("date").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    NotificationsData notifications = item.getValue(NotificationsData.class);
                    if (notifications.getId_user().equals(userid)) {
                        notificationsData.add(notifications);
                        Log.d("id found data", notifications.getId());
                    }
                }

                // check array of data
                Log.d("length", String.valueOf(notificationsData.size()));
                if (notificationsData.size() > 0) {
                    RecyclerView notificationRecycler = findViewById(R.id.notificationRecycler);
                    NotificationAdapter adapter = new NotificationAdapter(NotificationActivity.this, notificationsData);
                    notificationRecycler.setAdapter(adapter);
                    notificationRecycler.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
                    notificationRecycler.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", "error while taking handlerCustomerStockData");
            }
        });
    }
}