package com.example.android.vhsmonitoring;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

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

public class Approval extends AppCompatActivity {
    private String approvalType;
    private Button btnClose, btnApprove, btnOverviewNextToCloseBtn, btnOverviewNextToApprovalBtn;
    private TextView tvDeliveredStock, tvDeliveredStockAmount, tvArrivedStock, tvArrivedStockAmount;
    private ConstraintLayout viewGroupApproval, viewGroupApprovalConfirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval);

        // do approval pop-up
        viewGroupApproval = findViewById(R.id.viewgroup_approve);
        btnApprove = findViewById(R.id.btn_approve);
        btnOverviewNextToApprovalBtn = findViewById(R.id.btn_overview_next_to_btn_approve);

        // success message pop-op
        viewGroupApprovalConfirmation = findViewById(R.id.viewgroup_approval_confirmation);
        btnClose = findViewById(R.id.btn_close);
        btnOverviewNextToCloseBtn = findViewById(R.id.btn_overview_next_to_btn_close);

        tvDeliveredStock = findViewById(R.id.tv_delivered);
        tvDeliveredStockAmount = findViewById(R.id.tv_delivered_amount);
        tvArrivedStock = findViewById(R.id.tv_arrived_stock);
        tvArrivedStockAmount = findViewById(R.id.tv_arrived_stock_amount);

        Intent intent = getIntent();
        approvalType = intent.getStringExtra("approvalType");

        setPopUpWindow(approvalType);
        approve();
        goToOverview();
        closePopUp();

    }

    private void setPopUpWindow(String approvalType) {
        CardView cardApproval = findViewById(R.id.card_approval);
        Button cardHeaderTitle = findViewById(R.id.tv_card_header);

        viewGroupApprovalConfirmation.setVisibility(View.GONE);
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


    public void approve() {
        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewGroupApproval.setVisibility(View.GONE);
                viewGroupApprovalConfirmation.setVisibility(View.VISIBLE);

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


    public void goToOverview() {
        btnOverviewNextToApprovalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Approval.this, OverviewActivity.class);
                startActivity(intent);
            }
        });
        btnOverviewNextToCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Approval.this, OverviewActivity.class);
                startActivity(intent);
            }
        });
    }

}