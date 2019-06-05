package com.example.my_fruits_diary.MyDiary;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.my_fruits_diary.R;

import java.time.LocalDate;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<Integer> mEntryId = new ArrayList<>();
    private ArrayList<String> mFruitTitle = new ArrayList<>();
    private ArrayList<Integer> mFruitAmount = new ArrayList<>();
   // private ArrayList<Integer> mFruitImages = new ArrayList<>();
    private ArrayList<Integer> mTotalVitamins = new ArrayList<>();
    private ArrayList<LocalDate> mDate = new ArrayList<>();

    private Context mContext;
    private OnEntryListener mOnEntryListener;


    public RecyclerViewAdapter(ArrayList<Integer> mEntryId, ArrayList<String> mFruitTitle, ArrayList<Integer> mFruitAmount,
                               ArrayList<Integer> mTotalVitamins, ArrayList<LocalDate> mDate,
                               Context mContext, OnEntryListener onWishListener) {

        this.mEntryId = mEntryId;
        this.mFruitTitle = mFruitTitle;
        this.mFruitAmount = mFruitAmount;
        this.mTotalVitamins = mTotalVitamins;
        this.mDate = mDate;
        this.mContext = mContext;
        this.mOnEntryListener = onWishListener;
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

        viewHolder.fruitTitle.setText(mFruitTitle.get(i));
        viewHolder.fruitAmount.setText("Eaten: " + mFruitAmount.get(i));
        viewHolder.totalVitamins.setText("Vitamins: " + mTotalVitamins.get(i));
        viewHolder.date.setText("" + mDate.get(i));
    }

    /**
     * Method returns size of the entryList
     */

    @Override
    public int getItemCount() {
        return mFruitTitle.size();
    }

    /**
     * View Holder class initiates elements inside the Entry
     */

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView fruitTitle;
        TextView fruitAmount;
        TextView totalVitamins;
        TextView date;
        ConstraintLayout parentLayout;
        OnEntryListener onEntryListener;

        public ViewHolder(@NonNull View itemView, OnEntryListener onEntryListener) {
            super(itemView);

            fruitTitle = itemView.findViewById(R.id.fruitTitle);
            fruitAmount = itemView.findViewById(R.id.fruitAmount);
            totalVitamins = itemView.findViewById(R.id.totalVitamins);
            date = itemView.findViewById(R.id.date);
            parentLayout = itemView.findViewById(R.id.listItem_view);
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