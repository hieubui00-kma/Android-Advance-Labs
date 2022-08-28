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

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private List<Book> books = new ArrayList<>();

    private OnItemClickListener<Book> onItemClickListener;

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_book, parent, false);

        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        holder.bind(books.get(position));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setBooks(List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener<Book> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            setEvents();
        }

        private void setEvents() {
            itemView.setOnClickListener(view -> {
                if (onItemClickListener != null) {
                    Book book = books.get(getAdapterPosition());

                    onItemClickListener.onItemClicked(book);
                }
            });
        }

        public void bind(@NonNull Book book) {
            TextView tvBookTitle = itemView.findViewById(R.id.tv_book_title);

            tvBookTitle.setText(book.getTitle());
        }
    }
}
