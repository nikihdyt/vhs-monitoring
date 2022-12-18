package com.example.android.vhsmonitoring.Dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.vhsmonitoring.Admin.Model.StockDistributions.StockTransactions;
import com.example.android.vhsmonitoring.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.WordViewHolder> {
    private final List<StockTransactions> historyData;
    private final LayoutInflater inflater;

    public HistoryAdapter(Context context, List<StockTransactions> historyData) {
        inflater = LayoutInflater.from(context);
        this.historyData = historyData;
    }

    class WordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View lineEndData; final HistoryAdapter mAdapter;
        TextView stockDetailsDate, stockDetailsAmount, stockStatus, stockOpnameType;
        ImageView warningIcons;
        public WordViewHolder(View itemView, HistoryAdapter adapter) {
            super(itemView);
            stockDetailsDate = itemView.findViewById(R.id.dateStockOpname);
            stockDetailsAmount = itemView.findViewById(R.id.amountStockOpname);
            stockStatus = itemView.findViewById(R.id.stockOpnameDescription);
            warningIcons = itemView.findViewById(R.id.warningIcons);

            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = inflater.inflate(R.layout.fragment_view_1, parent, false);
        return new WordViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        // get today date
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        StockTransactions mCurrent = historyData.get(position);
        if (mCurrent.getDate_sent().equals(dateFormat.format(date))) {
            holder.stockDetailsDate.setText(R.string.today);
        } else {
            holder.stockDetailsDate.setText(mCurrent.getDate_sent());
        }

        holder.stockDetailsAmount.setText(String.format("%d kL", mCurrent.getAmount()));
        holder.stockStatus.setText(mCurrent.getStatus());
        if ("Approved".equals(mCurrent.getStatus())) {
            holder.warningIcons.setBackgroundResource(R.drawable.ic_finished);
        } else {
            holder.warningIcons.setBackgroundResource(R.drawable.ic_warning);
        }

    }

    @Override
    public int getItemCount() {
        return historyData.size();
    }
}
