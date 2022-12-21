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
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.android.vhsmonitoring.Admin.Model.ClaimContract;
import com.example.android.vhsmonitoring.Admin.Model.CustomerAddress;
import com.example.android.vhsmonitoring.Admin.Model.NotificationsData;
import com.example.android.vhsmonitoring.Admin.Model.StockData;
import com.example.android.vhsmonitoring.Admin.Model.StockDistributions.StockOpnameData;
import com.example.android.vhsmonitoring.Admin.Model.StockLosess;
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

public class StockOpname extends AppCompatActivity {
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    ConstraintLayout stockOpnameInputCard, stockOpnameApprovalCard;
    ImageButton btnTankRest;
    ExtendedFloatingActionButton btnClose;
    LinearLayout tvCalendar;

    Gson gsonData = new Gson();
    public UserHandler handler;
    public UserCustomer handlerCustomerData;
    public CustomerAddress handlerCustomerAddress;
    public StockData handlerCustomerStock;
    public ClaimContract claimContract;

    // get today date
    @SuppressLint("SimpleDateFormat")
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date date = new Date();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_opname);

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

        stockOpnameInputCard = findViewById(R.id.stock_opname_input_tank_rest);
        stockOpnameApprovalCard = findViewById(R.id.stock_opname_send_approval);
        btnClose = findViewById(R.id.btn_close);
        btnTankRest = findViewById(R.id.btn_send_data);
        tvCalendar = findViewById(R.id.calendar);

        stockOpnameInputCard.setVisibility(View.VISIBLE);
        stockOpnameApprovalCard.setVisibility(View.GONE);
        tvCalendar.setVisibility(View.GONE);

        TextView tvCurrentStockAmount_pp = findViewById(R.id.tv_current_stock_amount);
        TextView tvCapacityAmount_pp = findViewById(R.id.tv_capacity_amount);
        TextView tvSoldAmount_pp = findViewById(R.id.tv_sold_amount);

        tvCurrentStockAmount_pp.setText(String.format("%skL", String.valueOf(handlerCustomerStock.getTank_rest())));
        tvCapacityAmount_pp.setText(R.string.tank_capacity);
        tvSoldAmount_pp.setText(String.format("%skl", String.valueOf(handlerCustomerStock.getSold())));

        setPopUpWindow();
        sendData();
        closePopUp();
    }

    public void sendData() {
        btnTankRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etInputData = findViewById(R.id.et_input_data);
                if ("".equals(etInputData.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please insert the stock amount", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(etInputData.getText().toString()) > handlerCustomerStock.getTank_rest()) {
                    Toast.makeText(getApplicationContext(), "Please insert reasonable amount", Toast.LENGTH_SHORT).show();
                } else {
                    int tankRest = Integer.parseInt(etInputData.getText().toString());
                    String notification_id = db.child("data_notification").push().getKey();
                    String notificationMessage = "Approval Needed for " + String.valueOf(tankRest) + "kL stock opname form " + handlerCustomerData.getName()  + " stock";
                    String stockOpname_id = db.child("data_stock/" + handlerCustomerStock.getId()).push().getKey();

                    NotificationsData notification = new NotificationsData(notification_id, handlerCustomerStock.getManagerId(), handlerCustomerStock.getId(), stockOpname_id, notificationMessage, dateFormat.format(date), false);
                    StockOpnameData stockOpname = new StockOpnameData(stockOpname_id, handlerCustomerData.getHandlerId(), handlerCustomerData.getPertaminaId(), handlerCustomerData.getId(), handlerCustomerStock.getId(), dateFormat.format(date), "", "Approval Needed (Pertamina)", "StockOpname", tankRest, true, false);

                    assert stockOpname_id != null;
                    db.child("data_stock/" + handlerCustomerStock.getId() + "/stock_distributions").child(stockOpname_id).setValue(stockOpname).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            stockOpnameInputCard.setVisibility(View.GONE);
                            stockOpnameApprovalCard.setVisibility(View.VISIBLE);
                            tvCalendar.setVisibility(View.VISIBLE);

                            int losessAmount = handlerCustomerStock.getTank_rest() - tankRest;

                            // check if data input from stock opname is same as last current stock saved by handler
                            if (tankRest != handlerCustomerStock.getTank_rest()) {
                                // String losses_id = db.child("data_losses").push().getKey();
                                //  losses = new StockLosess(losses_id, handlerCustomerStock.getId(), losessAmount);
                                int lossesAmount = handlerCustomerStock.getTank_rest() - tankRest;

                                db.child("data_losses").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot item : snapshot.getChildren()) {
                                            StockLosess stockLosses = item.getValue(StockLosess.class);
                                            Log.d("SUCCESS", stockLosses.getId());

                                            // Update losses
                                            int totalLosses = lossesAmount + stockLosses.getAmount();
                                            DatabaseReference referenceLosses = FirebaseDatabase.getInstance().getReference();
                                            Map<String, Object> updateLosses = new HashMap<>();
                                            updateLosses.put("amount", totalLosses);
                                            referenceLosses.child("data_losses").child(stockLosses.getId()).updateChildren(updateLosses);

                                            TextView tvStockOpnameLosses = findViewById(R.id.tv_stock_opname_losses);
                                            tvStockOpnameLosses.setText(String.format("%skL", String.valueOf(losessAmount)));

                                            // update stock data from stock opname
                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                            Map<String, Object> updateTankAmount = new HashMap<>();
                                            updateTankAmount.put("tank_rest", tankRest);
                                            reference.child("data_stock").child(handlerCustomerStock.getId()).updateChildren(updateTankAmount);

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.d("ERROR", "error while taking Data Losses!");
                                    }
                                });
                            }

                            // update views with updated data
                            TextView tvStockOpnameSoldAmount = findViewById(R.id.tv_stock_opname_sold_amount);
                            TextView tvStockOpnameDeliveredAmount = findViewById(R.id.stock_opname_delivered_amount);
                            TextView tvStockOpnameCurrentStockAmount = findViewById(R.id.tv_stock_opname_current_stock_amount);
                            TextView tvStockOpnameTankRestAmount = findViewById(R.id.tv_stock_opname_tank_rest_amount);
                            TextView tvStockOpnameLosessClaim = findViewById(R.id.tv_stock_opname_losses_claim);
                            TextView tvClaimContract = findViewById(R.id.tv_claim_contract);
                            TextView tvApprovedBy = findViewById(R.id.tv_approved_by);

                            tvStockOpnameSoldAmount.setText(String.format("%skL", String.valueOf(handlerCustomerStock.getSold())));
                            tvStockOpnameDeliveredAmount.setText("1000kl");
                            tvStockOpnameCurrentStockAmount.setText(String.format("%skL", String.valueOf(handlerCustomerStock.getTank_rest())));
                            tvStockOpnameTankRestAmount.setText(String.format("%skL", String.valueOf(tankRest)));
                            tvStockOpnameLosessClaim.setText(R.string.default_amount);
                            tvApprovedBy.setText(String.format("by %s at %s", handler.getName(), stockOpname.getDate_sent()));
                            tvClaimContract.setText(String.format("Claim Contract : %s%%", claimContract.getAmount()));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("ERROR", "Error adding data!");
                        }
                    });

                    db.child("data_notifications").child(notification_id).setValue(notification).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("SUCCESS", notification_id);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("ERROR", "Error adding data!");
                        }
                    });
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
    public void setPopUpWindow() {
        CardView cardStockOpname = findViewById(R.id.card_stock_opname);

        ViewGroup.LayoutParams layoutParams = cardStockOpname.getLayoutParams();
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
}