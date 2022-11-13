package com.example.android.vhsmonitoring.handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.WindowManager;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.example.android.vhsmonitoring.R;

import org.w3c.dom.Text;

public class HandlerInputData extends AppCompatActivity {
    TextView tvInputTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_input_data);

        tvInputTitle = findViewById(R.id.tv_input_title);

        Intent intent = getIntent();
        String inputType = intent.getStringExtra("inputType");

        setPopUpWindow(inputType);

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