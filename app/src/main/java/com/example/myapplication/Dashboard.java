package com.example.myapplication;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {
    private TextView handlerName, handlerCode;
    private RecyclerView mRecyclerView;
    private Button inputData, filter1, filter2, filter3;
    private ImageButton resetFilter;
    private WordListAdapter mAdapter;
    private String onClickState1 = "clicked", onClickState2 = "unclicked", onClickState3 = "unclicked";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_1);

        Intent intent = getIntent();
        String username = intent.getStringExtra("name");
        String userCode = intent.getStringExtra("code");

        handlerName = (TextView) findViewById(R.id.handlerName);
        handlerCode = (TextView) findViewById(R.id.handlerCode);
        handlerName.setText(username);
        handlerCode.setText(userCode);

        Log.d("logTag", handlerCode.getText().toString());

        List<WordListAdapter.ApprovalDetailsStatusData> mWordList = new ArrayList<>();
        WordListAdapter.ApprovalDetailsStatusData data1 = new WordListAdapter.ApprovalDetailsStatusData("1", "ARRIVED STOCK", "900 kL", "Approved 2 days ago");
        WordListAdapter.ApprovalDetailsStatusData data2 = new WordListAdapter.ApprovalDetailsStatusData("2", "DAILY PICKUP", "50 kL", "18/09/2022 at 2:09pm");
        WordListAdapter.ApprovalDetailsStatusData data3 = new WordListAdapter.ApprovalDetailsStatusData("3", "DAILY PICKUP", "50 kL", "18/09/2022 at 2:09pm");

        mWordList.add(data1);
        mWordList.add(data2);
        mWordList.add(data3);

        if (mWordList.size() >= 1) {
            mRecyclerView = findViewById(R.id.ApprovalDetailStatus_Recycler);
            mAdapter = new WordListAdapter(this, mWordList);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        inputData = (Button) findViewById(R.id.buttonInputData);
        inputData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRecyclerView.getVisibility()==View.GONE) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    inputData.setTextColor(ColorStateList.valueOf((getResources().getColor(R.color.lightBlue))));
                    inputData.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.darkBlue)));
                    inputData.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icons_add_input_1_light, 0, 0, 0);
                } else {
                    mRecyclerView.setVisibility(View.GONE);
                    inputData.setTextColor(ColorStateList.valueOf((getResources().getColor(R.color.darkBlue))));
                    inputData.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.lightBlue)));
                    inputData.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icons_add_input_1, 0, 0, 0);
                }
            }
        });

        filter1 = (Button) findViewById(R.id.filterData_1);
        filter2 = (Button) findViewById(R.id.filterData_2);
        filter3 = (Button) findViewById(R.id.filterData_3);
        resetFilter = (ImageButton) findViewById(R.id.filterIcons);

        filter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickState1.equals("unclicked")) {
                    filter1.setBackground(getDrawable(R.drawable.background_filter_button_rounded));
                    filter1.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    onClickState1 = "clicked";
                } else {
                    filter1.setBackground(getDrawable(R.drawable.background_filter_unselected_button_rounded));
                    filter1.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.darkBlue)));
                    onClickState1 = "unclicked";
                }
            }
        });

        filter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickState2.equals("unclicked")) {
                    filter2.setBackground(getDrawable(R.drawable.background_filter_button_rounded));
                    filter2.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    onClickState2 = "clicked";
                } else {
                    filter2.setBackground(getDrawable(R.drawable.background_filter_unselected_button_rounded));
                    filter2.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.darkBlue)));
                    onClickState2 = "unclicked";
                }
            }
        });

        filter3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickState3.equals("unclicked")) {
                    filter3.setBackground(getDrawable(R.drawable.background_filter_button_rounded));
                    filter3.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    onClickState3 = "clicked";
                } else {
                    filter3.setBackground(getDrawable(R.drawable.background_filter_unselected_button_rounded));
                    filter3.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.darkBlue)));
                    onClickState3 = "unclicked";
                }
            }
        });

        resetFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter1.setBackground(getDrawable(R.drawable.background_filter_button_rounded));
                filter2.setBackground(getDrawable(R.drawable.background_filter_unselected_button_rounded));
                filter3.setBackground(getDrawable(R.drawable.background_filter_unselected_button_rounded));
                filter1.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                filter2.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.darkBlue)));
                filter3.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.darkBlue)));
                onClickState1 = "clicked";
                onClickState2 = "unclicked";
                onClickState3 = "unclicked";
            }
        });
    }
}
