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
import android.widget.ImageView;
import android.widget.TextView;

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
    public List<NotificationsData> handlerNotofications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // get handler data from intent
        Intent data = getIntent();
        String userId = data.getStringExtra("user");
        String data1 = data.getStringExtra("handler");
        String data2 = data.getStringExtra("handlerCustomerData");

        displayCustomerNotifications(userId);

        btnBack = findViewById(R.id.btn_back);
        backToBeranda();
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
        // view for notification header
        ImageView icNotification = findViewById(R.id.ic_notif);
        TextView tvNotificationCount = findViewById(R.id.tv_notifications_count);

        db.child("data_notifications").orderByChild("date").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    NotificationsData notifications = item.getValue(NotificationsData.class);
                    if (notifications.getId_user().equals(userid)) {
                        notificationsData.add(notifications);
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

                    int itemCount = adapter.getItemCount();
                    if (itemCount > 0) {
                        icNotification.setBackgroundResource(R.drawable.ic_notifications);
                        tvNotificationCount.setText(String.valueOf(itemCount));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", "error while taking handlerCustomerStockData");
            }
        });
    }
}