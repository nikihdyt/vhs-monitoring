package com.example.android.vhsmonitoring.Notifications;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.vhsmonitoring.Admin.Model.NotificationsData;
import com.example.android.vhsmonitoring.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.WordViewHolder> {
    private final List<NotificationsData> notificationsData;
    private final LayoutInflater inflater;

    public NotificationAdapter(Context context, List<NotificationsData> notificationsData) {
        inflater = LayoutInflater.from(context);
        this.notificationsData = notificationsData;
    }

    class WordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final NotificationAdapter mAdapter;
        TextView tvNotifType, tvTime, tvHeading, tvSelfDescriptions, tv_dateNotifications;
        ImageView logoCustomer;
        public WordViewHolder(View itemView, NotificationAdapter adapter) {
            super(itemView);
            tvNotifType = itemView.findViewById(R.id.tv_notif_type);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvHeading = itemView.findViewById(R.id.tv_heading);
            logoCustomer = itemView.findViewById(R.id.logo_customer);
            tvSelfDescriptions = itemView.findViewById(R.id.tv_selfDescription);
            tv_dateNotifications = itemView.findViewById(R.id.tv_dateNotifications);

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
        View mItemView = inflater.inflate(R.layout.item_notification, parent, false);
        return new WordViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        // get today date
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        NotificationsData mCurrent = notificationsData.get(position);
        if (mCurrent.getDate().equals(dateFormat.format(date))) {
            holder.tvTime.setText(R.string.today);
        } else {
            holder.tvTime.setText(mCurrent.getDate());
        }

        holder.tvNotifType.setText(R.string.approval_default);
        holder.tvTime.setText(String.valueOf(mCurrent.getDate()));
        holder.tvSelfDescriptions.setText(mCurrent.getMessages());
        holder.tv_dateNotifications.setText(mCurrent.getDate());
        holder.tvHeading.setText(R.string.default_company_name);

    }

    @Override
    public int getItemCount() {
        return notificationsData.size();
    }
}
