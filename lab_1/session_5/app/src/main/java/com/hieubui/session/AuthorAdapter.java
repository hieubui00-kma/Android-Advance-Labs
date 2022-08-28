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

public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.AuthorViewHolder> {
    private List<Author> authors = new ArrayList<>();

    private OnItemClickListener<Author> onItemClickListener;

    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_author, parent, false);

        return new AuthorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorViewHolder holder, int position) {
        holder.bind(authors.get(position));
    }

    @Override
    public int getItemCount() {
        return authors.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setAuthors(List<Author> authors) {
        this.authors = authors;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener<Author> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class AuthorViewHolder extends RecyclerView.ViewHolder {

        public AuthorViewHolder(@NonNull View itemView) {
            super(itemView);
            setEvents();
        }

        private void setEvents() {
            itemView.setOnClickListener(view -> {
                if (onItemClickListener != null) {
                    Author author = authors.get(getAdapterPosition());

                    onItemClickListener.onItemClicked(author);
                }
            });
        }

        public void bind(@NonNull Author author) {
            TextView tvAuthorName = itemView.findViewById(R.id.tv_author_name);

            tvAuthorName.setText(author.getName());
        }
    }
}
