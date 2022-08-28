package com.hieubui.session;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SMSMessageAdapter extends RecyclerView.Adapter<SMSMessageAdapter.SMSMessageViewHolder> {
    private List<SMSMessage> smsMessages = new ArrayList<>();

    private OnItemLongClickListener<SMSMessage> onItemLongClickListener = null;

    @NonNull
    @Override
    public SMSMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_sms_message, parent, false);

        return new SMSMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SMSMessageViewHolder holder, int position) {
        holder.bind(smsMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return smsMessages.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSMSMessages(List<SMSMessage> smsMessages) {
        this.smsMessages = smsMessages;
        notifyDataSetChanged();
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<SMSMessage> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public class SMSMessageViewHolder extends RecyclerView.ViewHolder {

        public SMSMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            setEvents();
        }

        private void setEvents() {
            itemView.setOnLongClickListener(view -> {
                if (onItemLongClickListener != null) {
                    SMSMessage SMSMessage = smsMessages.get(getAdapterPosition());

                    onItemLongClickListener.onLongClick(SMSMessage);
                }
                return true;
            });
        }

        @SuppressLint("SetTextI18n")
        public void bind(@NonNull SMSMessage smsMessage) {
            TextView tvAddress = (TextView) itemView.findViewById(R.id.tv_address);
            TextView tvBody = (TextView) itemView.findViewById(R.id.tv_body);

            tvAddress.setText("Sender: " + smsMessage.getAddress());
            tvBody.setText("Content: " + smsMessage.getBody());
        }
    }
}
