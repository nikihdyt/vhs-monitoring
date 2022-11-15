package com.example.android.vhsmonitoring;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.example.android.vhsmonitoring.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class StockOpname extends AppCompatActivity {
    ConstraintLayout stockOpnameInputCard, stockOpnameApprovalCard;
    ImageButton btnTankRest;
    ExtendedFloatingActionButton btnClose;
    LinearLayout tvCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_opname);

        stockOpnameInputCard = findViewById(R.id.stock_opname_input_tank_rest);
        stockOpnameApprovalCard = findViewById(R.id.stock_opname_send_approval);
        btnTankRest = findViewById(R.id.btn_send_data);
        btnClose = findViewById(R.id.btn_close);
        tvCalendar = findViewById(R.id.calendar);

        stockOpnameInputCard.setVisibility(View.VISIBLE);
        stockOpnameApprovalCard.setVisibility(View.GONE);
        tvCalendar.setVisibility(View.GONE);
        setPopUpWindow();

        sendData();
        closePopUp();
    }


    public void sendData() {
        btnTankRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stockOpnameInputCard.setVisibility(View.GONE);
                stockOpnameApprovalCard.setVisibility(View.VISIBLE);
                tvCalendar.setVisibility(View.VISIBLE);
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