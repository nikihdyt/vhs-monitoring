package com.example.android.vhsmonitoring.InputData;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.android.vhsmonitoring.Admin.Model.ClaimContract;
import com.example.android.vhsmonitoring.Admin.Model.CustomerAddress;
import com.example.android.vhsmonitoring.Admin.Model.StockData;
import com.example.android.vhsmonitoring.Admin.Model.Users.UserCustomer;
import com.example.android.vhsmonitoring.Admin.Model.Users.UserHandler;
import com.example.android.vhsmonitoring.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.gson.Gson;

public class InputData extends AppCompatActivity {
    Gson gsonData = new Gson();
    String jSonData;

    TextView tvInputTitle, tvDataInputted, tvSuccessMessage;
    ImageView icSuccessMessage;
    EditText etInputData;
    ImageButton btnSendData;
    ExtendedFloatingActionButton btnClose;

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

                // handlerAddArrivedStock();

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
}

