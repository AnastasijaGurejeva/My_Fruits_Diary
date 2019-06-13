package com.example.my_fruits_diary.MyDiary;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_fruits_diary.Model.Entry;
import com.example.my_fruits_diary.Model.Fruit;
import com.example.my_fruits_diary.Model.OnEntryDeleteListener;
import com.example.my_fruits_diary.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private List<Entry> mEntries;
    private Context mContext;
    private View.OnClickListener mOnEntryListener;
    private int mTotalFruitAmount = 0;
    private int mTotalVitaminsAmount = 0;
    private List<Fruit> mFruits;
    private String mDate;
    private OnEntryClickListener mOnEntryClickListener;
    private OnEntryDeleteListener mOnEntryDeleteListener;


    public RecyclerViewAdapter(List<Entry> mEntries, List<Fruit> mFruits,
                               Context mContext, OnEntryClickListener onEntryClickListener) {

        this.mContext = mContext;
        this.mOnEntryClickListener = onEntryClickListener;
        this.mFruits = mFruits;

    }

    /**
     * Method creates a Layout view for the Entry List
     *
     * @param viewGroup
     * @param i
     * @return viewholder
     */

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view, mOnEntryClickListener);
        return viewHolder;
    }

    public void setOnEntryDeleteListener(OnEntryDeleteListener onEntryDeleteListener) {
        this.mOnEntryDeleteListener = onEntryDeleteListener;
    }

    public void deleteItem(int position) {
        if (mEntries.size() == 1) {
            mOnEntryDeleteListener.onLastEntryRemoved();
            notifyItemRemoved(position);
        } else if (mEntries.size() == 0) {
            mOnEntryDeleteListener.onLastEntryRemoved();
            notifyItemRemoved(position);
        } else {
            int removedEntryId = mEntries.get(position).getEntryId();
            mOnEntryDeleteListener.onEntryRemoved(removedEntryId);
            mEntries.remove(position);
            notifyItemRemoved(position);
        }
    }


    /**
     * Method pass Entry values to the view section
     * Checks if data is uploaded, if not sets 0 values
     *
     * @param viewHolder
     * @param i
     */

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        if (mEntries != null && mEntries.size() != 0) {
            int totalVitaminsAmount = 0;
            mDate = mEntries.get(i).getDate();
            HashMap<Integer, Integer> eatenFruits = mEntries.get(i).getmEatenFruits();
            String fruitAmount = eatenFruits.values().stream()
                    .reduce(0, Integer::sum)
                    .toString();
            mTotalFruitAmount = Integer.valueOf(fruitAmount);

            if (eatenFruits.size() != 0 && mFruits != null) {
                for (int k = 0; k < eatenFruits.size(); k++) {
                    Set<Integer> keys = eatenFruits.keySet();
                    List<Integer> fruitIdList = new ArrayList(keys);
                    int fruitId = fruitIdList.get(k);
                    int vitamins = 0;
                    for (int j = 0; j < mFruits.size(); j++) {
                        if (mFruits.get(j).getID() == fruitId) {
                            vitamins = mFruits.get(j).getVitamins();
                            break;
                        }
                    }
                    totalVitaminsAmount = totalVitaminsAmount + vitamins * eatenFruits.get(fruitId);
                }
                mTotalVitaminsAmount = totalVitaminsAmount;

            } else {
                mTotalVitaminsAmount = 0;
            }
        }

        viewHolder.fruitAmount.setText("" + mTotalFruitAmount);
        viewHolder.totalVitamins.setText(" " + mTotalVitaminsAmount);
        viewHolder.date.setText(mDate);

    }

    void loadNewData(List<Entry> newEntries) {
        mEntries = newEntries;
        Log.d(TAG, "loadNewData: " + mEntries);
        notifyDataSetChanged();
    }

    void loadNewDataFruits(List<Fruit> fruits) {
        mFruits = fruits;
        Log.d(TAG, "loadNewDataFruits: " + mFruits);
        notifyDataSetChanged();
    }

    /**
     * Method returns size of the entryList, returns 1 if data is null or 0;
     */

    @Override
    public int getItemCount() {
        return ((mEntries != null) && (mEntries.size() != 0) ? mEntries.size() : 1);
    }


    /**
     * View Holder class initiates elements inside the Entry
     */

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cardView;
        TextView fruitAmount;
        TextView totalVitamins;
        TextView date;
        OnEntryClickListener onEntryClickListener;


        public ViewHolder(View itemView, OnEntryClickListener onEntryClickListener) {
            super(itemView);

            cardView = itemView.findViewById(R.id.listItem_view);
            fruitAmount = itemView.findViewById(R.id.fruit_amount);
            totalVitamins = itemView.findViewById(R.id.total_vitamins);
            date = itemView.findViewById(R.id.date_entry_fragment);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnEntryListener);
            this.onEntryClickListener = onEntryClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onEntryClickListener.onEntryClickListener(getAdapterPosition());

        }
    }

    public interface OnEntryClickListener {
        void onEntryClickListener(int position);
    }
}