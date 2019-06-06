package com.example.my_fruits_diary.MyDiary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.my_fruits_diary.R;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private List<Entry> mEntries;

    private Context mContext;
    private OnEntryListener mOnEntryListener;


    public RecyclerViewAdapter(List<Entry> mEntries,
                               Context mContext, OnEntryListener onEntryListener) {


        this.mContext = mContext;
        this.mOnEntryListener = onEntryListener;
    }


    /**
     * Method creates a Layout view for the Entry List
     *
     * @param viewGroup
     * @param i
     * @return viewholder
     */

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view, mOnEntryListener);
        return viewHolder;
    }

    /**
     * Method pass Entry values to the view section
     * @param viewHolder
     * @param i
     */

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {


        HashMap<Integer, Integer> eatenFruits = mEntries.get(i).getmEatenFruits();
        Set<Integer> keys = eatenFruits.keySet();
        Collection <Integer> values = eatenFruits.values();

        int totalFruitAmount = 0;
        int totalVitaminsAmount = 0;
        for (Integer key : keys) {
            totalFruitAmount += key;
        }

        for (Integer value : values) {
            totalVitaminsAmount += value;
        }

        viewHolder.fruitAmount.setText("Total Fruits: " + totalFruitAmount);
        viewHolder.totalVitamins.setText("Total Vitamins: " + totalVitaminsAmount);

        viewHolder.date.setText(mEntries.get(i).getDate());
    }

    /**
     * Method returns size of the entryList
     */

    @Override
    public int getItemCount() {
        return mEntries.size();
    }

    /**
     * View Holder class initiates elements inside the Entry
     */

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cardView;
        TextView fruitAmount;
        TextView totalVitamins;
        TextView date;
        OnEntryListener onEntryListener;

        public ViewHolder(@NonNull View itemView, OnEntryListener onEntryListener) {
            super(itemView);

            cardView =itemView.findViewById(R.id.listItem_view);
            fruitAmount = itemView.findViewById(R.id.fruitAmount);
            totalVitamins = itemView.findViewById(R.id.totalVitamins);
            date = itemView.findViewById(R.id.date);
            this.onEntryListener = onEntryListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onEntryListener.onEntryClick(getAdapterPosition());

        }
    }

    public interface OnEntryListener {
        void onEntryClick(int position);
    }
}