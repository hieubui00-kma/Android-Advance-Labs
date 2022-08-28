package com.hieubui.session;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NumberAdapter extends RecyclerView.Adapter<NumberAdapter.NumberViewHolder> {
    private List<Integer> numbers = new ArrayList<>();

    @NonNull
    @Override
    public NumberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_number, parent, false);

        return new NumberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NumberViewHolder holder, int position) {
        holder.bind(numbers.get(position));
    }

    @Override
    public int getItemCount() {
        return numbers.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setNumbers(List<Integer> numbers) {
        this.numbers = numbers;
        notifyDataSetChanged();
    }

    public static class NumberViewHolder extends RecyclerView.ViewHolder {

        public NumberViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @SuppressLint("SetTextI18n")
        public void bind(int number) {
            ((Button) itemView.findViewById(R.id.btn_number)).setText(number + "");
        }
    }
}
