package com.example.my_fruits_diary.MyDiary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.my_fruits_diary.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapterForAvialableFruits extends RecyclerView.Adapter<RecyclerViewAdapterForAvialableFruits.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private List<Fruit> mFruitList;
    private Context mContext;
    private OnEntryListener mOnEntryListener;


    public RecyclerViewAdapterForAvialableFruits(List<Fruit> mFruitList, Context mContext,
                                                 OnEntryListener onEntryListener) {

        this.mFruitList = mFruitList;
        this.mContext = mContext;
        this.mOnEntryListener = onEntryListener;
    }


    /**
     * Method creates a Layout view for the Available Fruit List
     *
     * @param viewGroup
     * @param i
     * @return viewholder
     */

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_for_available_fruits, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view, mOnEntryListener);
        return viewHolder;
    }

    /**
     * Method pass Fruit attributes to the view section
     * @param viewHolder
     * @param i
     */

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.fruitType.setText(mFruitList.get(i).getType());
        viewHolder.vitamins.setText(String.valueOf(mFruitList.get(i).getVitamins()));

        // Picasso Third party library which helps to download images from web
        Log.d(TAG, "onBindViewHolder: Link for picasso: " + mFruitList.get(i).getImage());
        Picasso.get()
                .load(mFruitList.get(i).getImage())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(viewHolder.fruitImage);

    }


    /**
     * Method returns size of the entryList
     */

    @Override
    public int getItemCount() {
        return mFruitList.size();
    }

    /**
     * View Holder class initiates elements inside the Entry
     */

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView fruitType;
        TextView vitamins;
        ImageView fruitImage;
        OnEntryListener onEntryListener;

        public ViewHolder(@NonNull View itemView, OnEntryListener onEntryListener) {
            super(itemView);

            fruitType= itemView.findViewById(R.id.text_fruitType);
            vitamins = itemView.findViewById(R.id.text_Vitamins);
            fruitImage = itemView.findViewById(R.id.fruit_image);
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