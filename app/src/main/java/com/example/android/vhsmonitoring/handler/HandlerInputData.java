package com.example.android.vhsmonitoring.handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.example.android.vhsmonitoring.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.w3c.dom.Text;

public class HandlerInputData extends AppCompatActivity {
    TextView tvInputTitle, tvDataInputted, tvSuccessMessage;
    ImageView icSuccessMessage;
    EditText etInputData;
    ImageButton btnSendData;
    ExtendedFloatingActionButton btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_input_data);

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
        // cek jenis input (arrived stock/daily pickup)

        Drawable dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        dim.setAlpha(200);

        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.add(dim);
    }


}