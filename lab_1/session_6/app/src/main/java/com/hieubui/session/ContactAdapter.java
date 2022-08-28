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

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private List<Contact> contacts = new ArrayList<>();

    private OnItemLongClickListener<Contact> onItemLongClickListener = null;

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_contact, parent, false);

        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.bind(contacts.get(position));
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<Contact> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            setEvents();
        }

        private void setEvents() {
            itemView.setOnLongClickListener(view -> {
                if (onItemLongClickListener != null) {
                    Contact contact = contacts.get(getAdapterPosition());

                    onItemLongClickListener.onLongClick(contact);
                }
                return true;
            });
        }

        public void bind(@NonNull Contact contact) {
            TextView tvName = (TextView) itemView.findViewById(R.id.tv_name);
            TextView tvPhoneNumber = (TextView) itemView.findViewById(R.id.tv_phone_number);

            tvName.setText(contact.getName());
            tvPhoneNumber.setText(contact.getPhoneNumbers().get(0));
        }
    }
}
