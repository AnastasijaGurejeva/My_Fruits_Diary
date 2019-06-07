package com.example.my_fruits_diary.MyDiary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.my_fruits_diary.R;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private List<Entry> mEntries;
    private Context mContext;
    private View.OnClickListener mOnEntryListener;
    private int mTotalFruitAmount = 0;
    private int mTotalVitaminsAmount = 0;
    private String mDate;


    public RecyclerViewAdapter(List<Entry> mEntries,
                               Context mContext) {

        this.mContext = mContext;

    }

    /**
     * Method creates a Layout view for the Entry List
     * @param viewGroup
     * @param i
     * @return viewholder
     */

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /**
     * Method pass Entry values to the view section
     * Checks if data is uploaded, if not sets 0 values
     * @param viewHolder
     * @param i
     */

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        if (mEntries != null && mEntries.size() != 0) {
            mDate = mEntries.get(i).getDate();
            HashMap<Integer, Integer> eatenFruits = mEntries.get(i).getmEatenFruits();
            mTotalFruitAmount = eatenFruits.size();
            if (eatenFruits.size() != 0) {
                Collection<Integer> values = eatenFruits.values();
                for (Integer value : values) {
                    mTotalVitaminsAmount += value;
                }
            } else {
                mTotalVitaminsAmount = 0;
            }
        }

        viewHolder.fruitAmount.setText("Total Fruits: " + mTotalFruitAmount);
        viewHolder.totalVitamins.setText("Total Vitamins: " + mTotalVitaminsAmount);
        viewHolder.date.setText(mDate);

    }

    void loadNewData(List<Entry> newEntries) {
        mEntries = newEntries;
        Log.d(TAG, "loadNewData: " + mEntries);
        notifyDataSetChanged();
    }

    /**
     * Method returns size of the entryList, returns 1 if data is null or 0;
     */

    @Override
    public int getItemCount() {
        return ((mEntries != null) && (mEntries.size() !=0) ? mEntries.size() : 1);
    }


    /**
     * View Holder class initiates elements inside the Entry
     */

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView fruitAmount;
        TextView totalVitamins;
        TextView date;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView =itemView.findViewById(R.id.listItem_view);
            fruitAmount = itemView.findViewById(R.id.fruitAmount);
            totalVitamins = itemView.findViewById(R.id.totalVitamins);
            date = itemView.findViewById(R.id.date_entryFr);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnEntryListener);
    }


    }

    public void setOnEntryListener (View.OnClickListener itemClickListener) {
        mOnEntryListener = itemClickListener;
    }
}