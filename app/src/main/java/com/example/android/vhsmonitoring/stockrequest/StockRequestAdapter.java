package com.example.android.vhsmonitoring.stockrequest;

import android.content.Context;
import android.hardware.lights.LightState;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.android.vhsmonitoring.R;

import java.util.List;

public class StockRequestAdapter extends PagerAdapter {
    public List<StockRequestModel> stockRequests;
    public Context context;

    public StockRequestAdapter(List<StockRequestModel> stockRequests, Context context) {
        this.stockRequests = stockRequests;
        this.context = context;
    }

    @Override
    public int getCount() {
        return stockRequests.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (CardView) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_stock_request, container, false);
        container.addView(view);

        ImageView custLogo = view.findViewById(R.id.logo_customer);
        TextView custName = view.findViewById(R.id.tv_customer_name);
        TextView custStockType = view.findViewById(R.id.tv_stock_type);

        custLogo.setImageResource(stockRequests.get(position).getCustLogo());
        custName.setText(stockRequests.get(position).getCustName());
        custStockType.setText("Stock: " + stockRequests.get(position).getStockType());

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
