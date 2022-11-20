package com.example.android.vhsmonitoring;

import static android.content.ContentValues.TAG;

import static com.example.android.vhsmonitoring.BerandaHandler.sessions_stockId;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.example.android.vhsmonitoring.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InputData extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    TextView tvInputTitle, tvDataInputted, tvSuccessMessage;
    ImageView icSuccessMessage;
    EditText etInputData;
    ImageButton btnSendData;
    ExtendedFloatingActionButton btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data);

        sharedpreferences = getSharedPreferences(LoginActivity.sessions, Context.MODE_PRIVATE);
        tvInputTitle = findViewById(R.id.tv_input_title);
        etInputData = findViewById(R.id.et_input_data);
        btnSendData = findViewById(R.id.btn_send_data);
        tvDataInputted = findViewById(R.id.tvDataInputted);
        tvSuccessMessage = findViewById(R.id.tv_success_message);
        icSuccessMessage = findViewById(R.id.ic_success_message);
        btnClose = findViewById(R.id.btn_close);

        // get intent message from beranda
        Intent intent = getIntent();
        String inputType = intent.getStringExtra("inputType");

        setPopUpWindow(inputType);
        sendData();
        closePopUp();

    }

    public void sendData() {
        btnSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etInputData.setVisibility(View.GONE);
                btnSendData.setVisibility(View.GONE);
                tvDataInputted.setVisibility(View.VISIBLE);
                icSuccessMessage.setVisibility(View.VISIBLE);
                tvSuccessMessage.setVisibility(View.VISIBLE);
                btnClose.setVisibility(View.VISIBLE);

                handlerAddArrivedStock();
            }
        });
    }

    public void closePopUp() {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void setPopUpWindow(String inputType) {
        CardView cardInputData = findViewById(R.id.card_input_data);
        TextView addDataType = findViewById(R.id.tv_input_title);
        addDataType.setText(inputType);

        ViewGroup.LayoutParams layoutParams = cardInputData.getLayoutParams();
        int width = layoutParams.width;

        getWindow().setLayout(width, ListPopupWindow.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);

        ViewGroup root = (ViewGroup) getWindow().getDecorView().getRootView();
        applyDim(root);
    }

    private static void applyDim(ViewGroup parent) {

        Drawable dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        dim.setAlpha(200);

        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.add(dim);
    }

    public void handlerAddArrivedStock(){
        String data = etInputData.getText().toString();
        int arrivedAmount = new Integer(data).intValue();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> approval = new HashMap<>();
        approval.put("pertamina", false);
        approval.put("handler", true);

        Map<String, Object> ArrivedStock = new HashMap<>();
        ArrivedStock.put("amount_arrival", arrivedAmount);
        ArrivedStock.put("approval", approval);
        ArrivedStock.put("date_arrival", dateFormat.format(date).toString());

        notifyPertamina();

        db.collection("stocks")
                .whereEqualTo("id", sharedpreferences.getString(sessions_stockId, ""))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                db.collection("stocks/" + document.getId() + "/restock")
                                        .add(ArrivedStock)
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
                                break;
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void notifyPertamina() {
        // send approval notification to pertamina that resotck opname has been done

    }
}

