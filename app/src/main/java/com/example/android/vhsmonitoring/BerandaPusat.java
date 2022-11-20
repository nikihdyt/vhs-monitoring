package com.example.android.vhsmonitoring;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.android.vhsmonitoring.stockrequest.StockRequestAdapter;
import com.example.android.vhsmonitoring.stockrequest.StockRequestModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BerandaPusat extends AppCompatActivity {
    // stock requests stack
    private ViewPager mStockRequestsViewPager;
    public StockRequestAdapter mStockRequestAdapter;
    private ArrayList<StockRequestModel> mStcoRequestsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda_pusat);

        stockRequestStack();

//        ApproveDailyPickup();
    }

//    TODO
//    implement recyclerview to show list of approval (get data from API)

//    TODO
//    implement function to show an approval popup
    private void ApproveDailyPickup() {

    }


    private void stockRequestStack() {
        mStockRequestsViewPager = findViewById(R.id.vp_stock_request);
        mStcoRequestsList = new ArrayList<>();

        int custLogo[] = {R.drawable.logo_customer, R.drawable.logo_customer, R.drawable.logo_customer};
        String custName[] = {"PT Shell Indonesia", "PT Shell Indonesia", "PT Shell Indonesia"};
        String custStock[] = {"Solar", "Solar", "Solar"};

        for(int i = 0; i < custLogo.length; i++) {
            StockRequestModel stockRequestModel = new StockRequestModel();
            stockRequestModel.setCustLogo(custLogo[i]);
            stockRequestModel.setCustName(custName[i]);
            stockRequestModel.setStokType(custStock[i]);

            mStcoRequestsList.add(stockRequestModel);
        }

        mStockRequestAdapter = new StockRequestAdapter(mStcoRequestsList,this);
        mStockRequestsViewPager.setPageTransformer(true, new StockRequestViewPagerStack());
        mStockRequestsViewPager.setOffscreenPageLimit(2);

        mStockRequestsViewPager.setAdapter(mStockRequestAdapter);
    }


    private class StockRequestViewPagerStack implements ViewPager.PageTransformer {
        @Override
        public void transformPage(@NonNull View page, float position) {
            if (position >= 0) {
//                page.setScaleX(0.7f - 0.05f * position);
//                page.setScaleY(0.7f);

                page.setTranslationX(page.getWidth() * position);
                page.setTranslationY(30 * position);
            }
        }
    }
}