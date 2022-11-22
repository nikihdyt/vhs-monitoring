package com.example.android.vhsmonitoring;

import static android.content.ContentValues.TAG;
import static com.example.android.vhsmonitoring.login.LoginActivity.sessions_userCode;
import static com.example.android.vhsmonitoring.login.LoginActivity.sessions_userid;
import static com.example.android.vhsmonitoring.login.LoginActivity.sessions_username;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

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

public class BerandaCustomer extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    public static final String sessions_affiliatedCustomer = "customer";
    public static final String sessions_stockId = "stock Id";

    private ImageButton btnNotification;
    private ExtendedFloatingActionButton btnInput, btnApproval;
    private ImageView icFilterHandler;
    private TextView tvHistoryHandler, tvHistoryCustomer;
    private LinearLayout groupHistoryFilterHandler;
    private Button btnGoToOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);

        sharedpreferences = getSharedPreferences(LoginActivity.sessions, Context.MODE_PRIVATE);
        btnInput = findViewById(R.id.btn_input);
        btnApproval = findViewById(R.id.btn_approval_customer);
        btnNotification = findViewById(R.id.btnNotification);
        tvHistoryHandler = findViewById(R.id.tv_history);
        tvHistoryCustomer = findViewById(R.id.tv_history_customer);
        icFilterHandler = findViewById(R.id.ic_filter);
        groupHistoryFilterHandler = findViewById(R.id.group_histories);
        btnGoToOverview = findViewById(R.id.btn_go_to_overview);

        btnInput.setVisibility(View.GONE);
        btnApproval.setVisibility(View.VISIBLE);
        tvHistoryHandler.setVisibility(View.GONE);
        tvHistoryCustomer.setVisibility(View.VISIBLE);
        icFilterHandler.setVisibility(View.GONE);
        groupHistoryFilterHandler.setVisibility(View.GONE);

        ApproveDailyPickup();
        goToNotifications();
        goToOverview();


    }

    private void ApproveDailyPickup() {
        btnApproval.bringToFront();
        btnApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BerandaCustomer.this, Approval.class);
                intent.putExtra("approvalType", "DAILY PICKUP");
                startActivity(intent);
            }
        });
    }

    private void goToNotifications() {
        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BerandaCustomer.this, NotificationActivity.class);
                startActivity(intent);
            }
        });
    }

    public void goToOverview() {
        btnGoToOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BerandaCustomer.this, OverviewActivity.class);
                startActivity(intent);
            }
        });
    }

}
