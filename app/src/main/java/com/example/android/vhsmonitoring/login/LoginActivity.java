package com.example.android.vhsmonitoring.login;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.android.vhsmonitoring.Beranda;
import com.example.android.vhsmonitoring.BerandaPusat;
import com.example.android.vhsmonitoring.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    public static final String EXTRA_MESSAGE = "LoginActivity data";
    public static final String sessions = "session";
    public static final boolean sessions_loggedIn = false;
    public static final String sessions_userid = "defaultId";
    public static final String sessions_username = "defaultUsername";
    public static final String sessions_userRole = "defaultRole";
    public static final String sessions_userPassword = "defaultPassword";
    public static final String sessions_userCode = "defaultCode";
    private Button buttonLogin;
    private RelativeLayout loginCard;
    private EditText number_id, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        sharedpreferences = getSharedPreferences(sessions, Context.MODE_PRIVATE);
        number_id = (EditText) findViewById(R.id.nomorID);
        password = (EditText) findViewById(R.id.kataSandi);

        resetSessions();
        checkSessions();

        loginCard = (RelativeLayout) findViewById(R.id.loginCard);
        loginCard.measure(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        int pagerHeight = loginCard.getMeasuredHeight() - 120;
        Log.d("newTag", String.valueOf(pagerHeight));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, pagerHeight));
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "button is clicked");
                db.collection("users_data")
                        .whereEqualTo("number_id", number_id.getText().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        if(document.getString("password").equals(password.getText().toString())){
                                            Log.d(TAG, document.getId() + " => " + document.getData());
                                            Log.d(TAG, document.getId() + " => " + document.getString("usercode"));
                                            SharedPreferences.Editor editor = sharedpreferences.edit();

                                            editor.putString(sessions_userid, number_id.getText().toString()); // need to be encrypted
                                            editor.putString(sessions_userPassword, password.getText().toString()); // need to be encrypted
                                            editor.putString(sessions_username, document.getString("name"));
                                            editor.putString(sessions_userRole, document.getString("Role"));
                                            editor.putString(sessions_userCode, document.getString("usercode"));
                                            editor.putBoolean(String.valueOf(sessions_loggedIn), true);
                                            editor.commit();

                                            if(document.getString("role").equals("Handler")){
                                                Intent i = new Intent(getApplicationContext(), Beranda.class);
                                                startActivity(i);
                                            } else if(document.getString("role").equals("Pertamina")) {
                                                Intent i = new Intent(getApplicationContext(), BerandaPusat.class);
                                                startActivity(i);
                                            } else if(document.getString("role").equals("Customer")) {
                                                // customer activity class foes here
                                                Log.d(TAG, "user is a customer");
                                            }
                                        }
                                    }
                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }
                            }
                        });
            }
        });
    }

    public void postUserData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("name", "Zayn Malik");
        user.put("usercode", "287-HANDLER-1A");
        user.put("number_id", "92817928");
        user.put("password", "91067ZMH");
        user.put("Role", "Handler");
        db.collection("users_test")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void checkSessions(){
        sharedpreferences = getSharedPreferences(sessions, Context.MODE_PRIVATE);
        boolean checkStatus = sharedpreferences.getBoolean(String.valueOf(sessions_loggedIn), false);
        if (checkStatus == true){
            Intent i = new Intent(getApplicationContext(), Beranda.class);
            i.putExtra("name", sharedpreferences.getString(sessions_username, ""));
            i.putExtra("code", sharedpreferences.getString(sessions_userCode, ""));
            startActivity(i);
        }
    }

    public void resetSessions(){
        sharedpreferences = getSharedPreferences(sessions, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(sharedpreferences.getString(sessions_userid, ""), "defaultId");
        editor.putString(sharedpreferences.getString(sessions_userPassword, ""), "defaultPassword");
        editor.putString(sharedpreferences.getString(sessions_username, ""), "defaultUsername");
        editor.putString(sharedpreferences.getString(sessions_userRole, ""), "defaultRole");
        editor.putString(sharedpreferences.getString(sessions_userCode, ""), "defaultCode");
        editor.putBoolean(String.valueOf(sessions_loggedIn), false);
        editor.commit();
    }
}