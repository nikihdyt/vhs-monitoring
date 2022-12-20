package com.example.android.vhsmonitoring.InputData;
import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.android.vhsmonitoring.Admin.Model.ClaimContract;
import com.example.android.vhsmonitoring.Admin.Model.CustomerAddress;
import com.example.android.vhsmonitoring.Admin.Model.NotificationsData;
import com.example.android.vhsmonitoring.Admin.Model.StockData;
import com.example.android.vhsmonitoring.Admin.Model.StockDistributions.DailyPickupData;
import com.example.android.vhsmonitoring.Admin.Model.StockDistributions.RestockData;
import com.example.android.vhsmonitoring.Admin.Model.StockDistributions.StockOpnameData;
import com.example.android.vhsmonitoring.Admin.Model.Users.UserCustomer;
import com.example.android.vhsmonitoring.Admin.Model.Users.UserHandler;
import com.example.android.vhsmonitoring.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InputData extends AppCompatActivity {
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    Gson gsonData = new Gson();
    String jSonData;
    private String inputType;

    private TextView tvInputTitle, tvDataInputted, tvSuccessMessage;
    private ImageView icSuccessMessage;
    private EditText etInputData;
    private ImageButton btnSendData;
    private ExtendedFloatingActionButton btnClose;

    public UserHandler handler;
    public UserCustomer handlerCustomerData;
    public CustomerAddress handlerCustomerAddress;
    public StockData handlerCustomerStock;
    public ClaimContract claimContract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data);

        // get handler data from intent
        Intent data = getIntent();
        String data1 = data.getStringExtra("handler");
        String data2 = data.getStringExtra("handlerCustomerData");
        String data3 = data.getStringExtra("handlerCustomerAddress");
        String data4 = data.getStringExtra("handlerCustomerStock");
        String data5 = data.getStringExtra("claimContract");

        // convert handler data in json into object
        handler = gsonData.fromJson(data1, UserHandler.class);
        handlerCustomerData = gsonData.fromJson(data2, UserCustomer.class);
        handlerCustomerAddress = gsonData.fromJson(data3, CustomerAddress.class);
        handlerCustomerStock = gsonData.fromJson(data4, StockData.class);
        claimContract = gsonData.fromJson(data5, ClaimContract.class);

        // views in inputData
        tvInputTitle = findViewById(R.id.tv_input_title);
        etInputData = findViewById(R.id.et_input_data);
        btnSendData = findViewById(R.id.btn_send_data);
        tvDataInputted = findViewById(R.id.tvDataInputted);
        tvSuccessMessage = findViewById(R.id.tv_success_message);
        icSuccessMessage = findViewById(R.id.ic_success_message);
        btnClose = findViewById(R.id.btn_close);

        // get intent message from beranda
        Intent intent = getIntent();
        inputType = intent.getStringExtra("inputType");

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

                @SuppressLint("SimpleDateFormat")
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();

                if (!"".equals(etInputData.getText().toString())) {
                    int amount = Integer.parseInt(etInputData.getText().toString());
                    TextView tvDataInputted = findViewById(R.id.tvDataInputted);
                    tvDataInputted.setText(String.format("%skL", String.valueOf(amount)));

                    // Separate based on inputType
                    if ("ARRIVED STOCK".equals(inputType)) {
                        String arrivedStock_id = db.child("data_stock").child(handlerCustomerStock.getId()).child("stock_distributions").push().getKey();
                        String CustomerAddress = String.format("%s, %s %s", handlerCustomerAddress.getCity(), handlerCustomerAddress.getProvince(), handlerCustomerAddress.getPostalCode());
                        RestockData restock = new RestockData(arrivedStock_id, handlerCustomerData.getHandlerId(), handlerCustomerData.getPertaminaId(), handlerCustomerData.getId(), CustomerAddress, "", dateFormat.format(date), "Need Approval (pertamina)", 100, amount, true, false);
                        if (arrivedStock_id != null) {
                            db.child("data_stock").child(handlerCustomerStock.getId()).child("stock_distributions").child(arrivedStock_id).setValue(restock).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("SUCCESS", arrivedStock_id);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("ERROR", "Error adding data!");
                                }
                            });

                            // update stock amount
                            int newTankAmount = amount + handlerCustomerStock.getTank_rest();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                            Map<String, Object> updateTankAmount = new HashMap<>();
                            updateTankAmount.put("tank_rest", newTankAmount);
                            reference.child("data_stock").child(handlerCustomerStock.getId()).updateChildren(updateTankAmount);

                            // send notifications to pertamina
                            String notification_id = db.child("data_notifications").push().getKey();
                            String notificationMessage = "Approval Needed for arrived stock in " + handlerCustomerData.getName() + "with value of " + String.valueOf(amount)  + "kL and total value as much as " + String.valueOf(newTankAmount);
                            NotificationsData notification = new NotificationsData(notification_id, handlerCustomerStock.getManagerId(), handlerCustomerStock.getId(), arrivedStock_id, notificationMessage, dateFormat.format(date), false);
                            if (notification_id != null) {
                                db.child("data_notifications").child(notification_id).setValue(notification).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(), "Successfully added Arrived Stock!", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Something is wrong...", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    } else if ("DAILY PICKUP".equals(inputType)) {
                        String dailyPickup_id = db.child("data_stock").child(handlerCustomerStock.getId()).push().getKey();
                        DailyPickupData dailyPickup = new DailyPickupData(dailyPickup_id, handler.getId(), handlerCustomerData.getPertaminaId(), handlerCustomerData.getId(), handlerCustomerStock.getId(), dateFormat.format(date), "", "Need Approval", "Daily Pickup", amount, true, false, false);
                        if (dailyPickup_id != null) {
                            db.child("data_stock").child(handlerCustomerStock.getId()).child("stock_distributions").child(dailyPickup_id).setValue(dailyPickup).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("SUCCESS", dailyPickup_id);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("ERROR", "Error adding data!");
                                }
                            });

                            // update stock amount
                            int newTankAmount = handlerCustomerStock.getTank_rest() - amount;
                            int soldAmount = handlerCustomerStock.getSold() + amount;
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                            Map<String, Object> updateTankAmount = new HashMap<>();
                            updateTankAmount.put("tank_rest", newTankAmount);
                            updateTankAmount.put("sold", soldAmount);
                            reference.child("data_stock").child(handlerCustomerStock.getId()).updateChildren(updateTankAmount);

                            // send notifications to pertamina and customer
                            String updateNotification_id = db.child("data_notifications").push().getKey();
                            String notificationMessage = "Approval Needed for arrived stock in " + handlerCustomerData.getName() + "with value of " + String.valueOf(amount)  + "kL and total value as much as " + String.valueOf(newTankAmount);

                            NotificationsData notificationPertamina = new NotificationsData(updateNotification_id, handlerCustomerStock.getManagerId(), handlerCustomerStock.getId(), dailyPickup_id, notificationMessage, dateFormat.format(date), false);
                            NotificationsData notificationCustomer = new NotificationsData(updateNotification_id, handlerCustomerStock.getManagerId(), handlerCustomerStock.getId(), dailyPickup_id, notificationMessage, dateFormat.format(date), false);
                            if (updateNotification_id != null) {
                                // pertamina
                                db.child("data_notifications").child(updateNotification_id).setValue(notificationPertamina).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("SUCCESS", updateNotification_id);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("ERROR", "Error adding data!");
                                    }
                                });

                                // customer
                                db.child("data_notifications").child(updateNotification_id).setValue(notificationCustomer).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(), "Successful Daily Pickup!", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Something is wrong...", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please input your data", Toast.LENGTH_SHORT).show();
                }


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
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        CardView cardInputData = findViewById(R.id.card_input_data);
        TextView addDataType = findViewById(R.id.tv_input_title);
        TextView tvDate = findViewById(R.id.tv_date);
        TextView tvCurrentStock = findViewById(R.id.tv_current_stock);
        TextView tvSold = findViewById(R.id.tv_sold);
        TextView tvCustomerName = findViewById(R.id.tv_customer_name);
        TextView tvCustomerAddress = findViewById(R.id.tv_customer_address);
        TextView tvCustomerStock = findViewById(R.id.tv_customer_stok);

        ViewGroup.LayoutParams layoutParams = cardInputData.getLayoutParams();
        int width = layoutParams.width;

        getWindow().setLayout(width, ListPopupWindow.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);

        ViewGroup root = (ViewGroup) getWindow().getDecorView().getRootView();
        applyDim(root);

        // stock data
        tvDate.setText(dateFormat.format(date));
        tvCurrentStock.setText(String.format("%skL", String.valueOf(handlerCustomerStock.getTank_rest())));
        tvSold.setText(String.format("%skL", String.valueOf(handlerCustomerStock.getSold())));
        addDataType.setText(inputType);

        // customer data
        tvCustomerName.setText(handlerCustomerData.getName());
        tvCustomerAddress.setText(String.format("%s, %s %s", handlerCustomerAddress.getCity(), handlerCustomerAddress.getProvince(), handlerCustomerAddress.getPostalCode()));
        tvCustomerStock.setText(String.format("Stok : %s", handlerCustomerStock.getType()));
    }
    private static void applyDim(ViewGroup parent) {
        Drawable dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        dim.setAlpha(200);

        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.add(dim);
    }
}

