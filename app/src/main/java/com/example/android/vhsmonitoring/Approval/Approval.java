package com.example.android.vhsmonitoring.Approval;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.android.vhsmonitoring.Admin.Model.ClaimContract;
import com.example.android.vhsmonitoring.Admin.Model.CustomerAddress;
import com.example.android.vhsmonitoring.Admin.Model.StockData;
import com.example.android.vhsmonitoring.Admin.Model.StockDistributions.DailyPickupData;
import com.example.android.vhsmonitoring.Admin.Model.Users.UserCustomer;
import com.example.android.vhsmonitoring.Admin.Model.Users.UserHandler;
import com.example.android.vhsmonitoring.R;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Approval extends AppCompatActivity {
    private String approvalType;
    private Button btnClose;
    private TextView tvDeliveredStock, tvDeliveredStockAmount, tvArrivedStock, tvArrivedStockAmount;

    public Gson gsonData = new Gson();
    public String jSonData;

    public UserCustomer customer;
    public CustomerAddress SelfCustomerAddress;
    public StockData selfStockData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval);

        // get today date
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        TextView tvMonth = findViewById(R.id.tv_month);
        TextView tvCustomerName = findViewById(R.id.tv_customer_name);
        TextView tvCustomerAddress = findViewById(R.id.tv_customer_address);
        TextView tvCustomerStock = findViewById(R.id.tv_customer_stok);

        TextView tvCurrentStockAmountArrivedStock = findViewById(R.id.tv_current_stock_amount_arrived_stock);
        TextView tvPickupAmount = findViewById(R.id.daily_pickup_amount);
        TextView tvApprovedBy = findViewById(R.id.tv_approved_by);

        btnClose = findViewById(R.id.btn_close);
        tvDeliveredStock = findViewById(R.id.tv_delivered);
        tvDeliveredStockAmount = findViewById(R.id.tv_delivered_amount);
        tvArrivedStock = findViewById(R.id.tv_arrived_stock);
        tvArrivedStockAmount = findViewById(R.id.tv_arrived_stock_amount);

        // get handler data from intent
        Intent data = getIntent();
        approvalType = data.getStringExtra("approvalType");
        String data1 = data.getStringExtra("customer");
        String data2 = data.getStringExtra("SelfCustomerAddress");
        String data3 = data.getStringExtra("selfStockData");

        // convert handler data in json into object
        customer = gsonData.fromJson(data1, UserCustomer.class);
        SelfCustomerAddress = gsonData.fromJson(data2, CustomerAddress.class);
        selfStockData = gsonData.fromJson(data3, StockData.class);

        // fill report data
        tvMonth.setText(dateFormat.format(date));
        tvCustomerName.setText(customer.getName());
        tvCustomerAddress.setText(String.format("%s, %s %s", SelfCustomerAddress.getCity(), SelfCustomerAddress.getProvince(), SelfCustomerAddress.getPostalCode()));
        tvCustomerStock.setText(String.format("stock: %s", selfStockData.getType()));
        tvCurrentStockAmountArrivedStock.setText(String.format("%skL", String.valueOf(selfStockData.getTank_rest())));
        tvPickupAmount.setText(String.valueOf(selfStockData.getSold()));
        tvApprovedBy.setText(String.format("by customer at%s", customer.getName()));

        setPopUpWindow(approvalType);
        closePopUp();
    }

    public void closePopUp() {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void setPopUpWindow(String approvalType) {
        CardView cardApproval = findViewById(R.id.card_approval);
        Button cardHeaderTitle = findViewById(R.id.tv_card_header);
        cardHeaderTitle.setText(approvalType);

        tvDeliveredStock.setVisibility(View.GONE);
        tvDeliveredStockAmount.setVisibility(View.GONE);
        tvArrivedStock.setVisibility(View.GONE);
        tvArrivedStockAmount.setVisibility(View.GONE);

        ViewGroup.LayoutParams layoutParams = cardApproval.getLayoutParams();
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