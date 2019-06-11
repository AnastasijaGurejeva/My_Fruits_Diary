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
import com.example.my_fruits_diary.Model.OnEntryDeleteListener;
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
    private OnEntryClickListener mOnEntryClickListener;
    private OnEntryDeleteListener mOnEntryDeleteListener;



    public RecyclerViewAdapter(List<Entry> mEntries,
                               Context mContext, OnEntryClickListener onEntryClickListener) {

        this.mContext = mContext;
        this.mOnEntryClickListener = onEntryClickListener;

    }

    /**
     * Method creates a Layout view for the Entry List
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
     * @param viewHolder
     * @param i
     */

    @Override
    public void onBindViewHolder( ViewHolder viewHolder, int i) {

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

        viewHolder.fruitAmount.setText("" + mTotalFruitAmount);
        viewHolder.totalVitamins.setText(" " + mTotalVitaminsAmount);
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cardView;
        TextView fruitAmount;
        TextView totalVitamins;
        TextView date;
        OnEntryClickListener onEntryClickListener;


        public ViewHolder( View itemView, OnEntryClickListener onEntryClickListener) {
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