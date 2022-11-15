package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {
    public static final String EXTRA_MESSAGE = "com.example.android.RecyclerView.extra.MESSAGE";
    public int count = 0;
    private final List<ApprovalDetailsStatusData> mWordList;
    private LayoutInflater mInflater;

    public WordListAdapter(Context context, List<ApprovalDetailsStatusData> wordList) {
        mInflater = LayoutInflater.from(context);
        this.mWordList = wordList;
    }

    static class ApprovalDetailsStatusData {
        String id, status, stock_amount, date;
        public ApprovalDetailsStatusData(String id, String status, String stock_amount, String date) {
            this.id = id;
            this.status = status;
            this.stock_amount = stock_amount;
            this.date = date;
        }
    }

    class WordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView data_id, stockStatusDetails, stockDetailsAmount, stockDetailsDate;
        public ImageView icons;
        public View lineEndData;
        final WordListAdapter mAdapter;
        public WordViewHolder(View itemView, WordListAdapter adapter) {
            super(itemView);
            stockDetailsDate = (TextView) itemView.findViewById(R.id.stockDetailsDate);
            stockDetailsAmount = (TextView) itemView.findViewById(R.id.stockDetailsAmount);
            stockStatusDetails = (TextView) itemView.findViewById(R.id.stockStatusDetails);
            icons = (ImageView) itemView.findViewById(R.id.statusApproval_icons);
            data_id = (TextView) itemView.findViewById(R.id.approvalDataStatus_id);
            count = count + 1;
            if (count == getItemCount()) {
                lineEndData = (View) itemView.findViewById(R.id.lineEnder);
                lineEndData.setVisibility(View.VISIBLE);
            }
            if (stockStatusDetails.getText().toString().equals("ARRIVED STOCK")) {
                icons.setBackgroundResource(R.drawable.icons_approval_finished);
            }
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
        View mItemView = mInflater.inflate(R.layout.fragment_view_2, parent, false);
        return new WordViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        ApprovalDetailsStatusData mCurrent = mWordList.get(position);
        holder.stockStatusDetails.setText(mCurrent.status);
        holder.stockDetailsAmount.setText(mCurrent.stock_amount);
        holder.stockDetailsDate.setText(mCurrent.date);
        String currentData = mCurrent.id;
    }

    @Override
    public int getItemCount() {
        return mWordList.size();
    }
}
