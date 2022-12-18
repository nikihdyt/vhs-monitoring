package com.example.android.vhsmonitoring.SplashScreeen;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.android.vhsmonitoring.Admin.Model.Users.UserCustomer;
import com.example.android.vhsmonitoring.Admin.Model.Users.UserData;
import com.example.android.vhsmonitoring.Admin.Model.Users.UserHandler;
import com.example.android.vhsmonitoring.Dashboard.Dashboard;
import com.example.android.vhsmonitoring.Encryption;
import com.example.android.vhsmonitoring.Login.PagerAdapter;
import com.example.android.vhsmonitoring.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    SharedPreferences sessions;
    Gson gsonData = new Gson();
    String jsonData;

    public boolean rememberme_state;
    public static final String sharedpreferences = "sharedpreferences";
    public static final String sessions_userRole = "role";
    public static final String data = "data";
    public static final boolean sessions_loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        sessions = getSharedPreferences(sharedpreferences, Context.MODE_PRIVATE);

        // view splash screen
        viewSplashScreen();
    }

    public void viewSplashScreen() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // check user sessions
                boolean userSession = sessions.getBoolean(String.valueOf(sessions_loggedIn), false);
                if (userSession) {
                    // redirect user to dashboard
                    startActivity(new Intent(MainActivity.this, Dashboard.class));
                } else {
                    resetSessions();
                }

                // view login screen if user has no sessions
                viewLoginScreen();
            }
        }, 2000);
    }

    public void viewLoginScreen() {
        // set content view to login page
        setContentView(R.layout.activity_login);

        // measure loginCard carousel size based on loginCard width and height
        RelativeLayout loginCard = findViewById(R.id.loginCard);
        loginCard.measure(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        int pagerHeight = loginCard.getMeasuredHeight() - 120;

        // prepare TabLayout for carousel
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // prepare loginCard carousel with TabLayout and Adapter
        final ViewPager viewPager = findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, pagerHeight));
        viewPager.setAdapter(adapter);

        // add event listener
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) { viewPager.setCurrentItem(tab.getPosition()); }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        // Check if user want to save their sessions
        CheckBox rememberme = findViewById(R.id.checkBox);
        if (rememberme.isChecked()) {
            rememberme_state = true;
        }

        // add button event listener
        Button btn_login = findViewById(R.id.buttonLogin);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyUserData();
            }
        });
    }

    public void verifyUserData() {
        // get user login data
        EditText number_id = findViewById(R.id.nomorID);
        EditText password = findViewById(R.id.kataSandi);

        Encryption encode = new Encryption();
        String userID = number_id.getText().toString();
        String userPassword = password.getText().toString();

        // check user data in database
        db.child("data_user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    UserData user = item.getValue(UserData.class);
                    if (user.getCode().equals(encode.encrypt(userID)) && user.getPassword().equals(encode.encrypt(userPassword))) {
                        // Toast.makeText(getApplicationContext(), user.getName(), Toast.LENGTH_SHORT).show();
                        CheckBox userSession = findViewById(R.id.checkBox);
                        SharedPreferences.Editor editorSession = sessions.edit();
                        if (userSession.isChecked()) {
                            editorSession.putBoolean((String.valueOf(sessions_loggedIn)), true);
                        }
                        editorSession.putString(sessions_userRole, user.getRole());
                        editorSession.apply();

                        Intent redirect = new Intent(MainActivity.this, Dashboard.class);
                        SharedPreferences.Editor editor = sessions.edit();
                        switch (user.getRole()) {
                            case "handler":
                                UserHandler handler = item.getValue(UserHandler.class);
                                jsonData = gsonData.toJson(handler);
                                break;
                            case "customer":
                                UserCustomer customer = item.getValue(UserCustomer.class);
                                jsonData = gsonData.toJson(customer);
                                break;
                            case "pertamina":
                                jsonData = gsonData.toJson(user);
                                break;
                        }

                        editor.putString("userSessions", jsonData);
                        editor.apply();

                        startActivity(redirect);
                        finish();
                    }
                }

                if ("role".equals(sessions.getString(sessions_userRole, ""))) {
                    Toast.makeText(getApplicationContext(), "Wrong Login Credentials!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", "An Error when accessing database has occured");
            }
        });
    }

    public void resetSessions() {
        SharedPreferences.Editor editor = sessions.edit();
        editor.putBoolean(String.valueOf(sessions_loggedIn), false);
        editor.putString(sessions_userRole, "role");
        editor.apply();
    }
}