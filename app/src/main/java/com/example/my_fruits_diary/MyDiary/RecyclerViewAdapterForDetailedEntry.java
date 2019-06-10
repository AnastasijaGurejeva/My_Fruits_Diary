package com.example.my_fruits_diary.MyDiary;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.my_fruits_diary.DataHandling.DataHandler;
import com.example.my_fruits_diary.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class RecyclerViewAdapterForDetailedEntry extends RecyclerView.Adapter<RecyclerViewAdapterForDetailedEntry.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private HashMap<Integer, Integer> mFruitEntries;
    private Context mContext;
    private List<Fruit> mFruitList;
    private String mFruitType;
    private int mFruitAmount;
    private int mEntryId;
    private OnDetailedEntryCnangeListener onDetailedEntryCnangeListener;


    public RecyclerViewAdapterForDetailedEntry(HashMap<Integer, Integer> fruitEntries,
                                               List<Fruit> fruitList, int entryId, Context mContext) {

        this.mFruitEntries = fruitEntries;
        this.mContext = mContext;
        this.mFruitList = fruitList;
        this.mEntryId = entryId;
    }


    /**
     * Method creates a Layout view for the Detailed entry Fruit List
     *
     * @param viewGroup
     * @param i
     * @return viewholder
     */


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_for_detailed_entry, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    public void deleteItem(int position) {
         DataHandler dataHandler = new DataHandler();
         Set fruitKeys = mFruitEntries.keySet();
         List<Integer> fruitsId = new ArrayList<>(fruitKeys);
         int fruitId = fruitsId.get(position);
         int amountInt = mFruitEntries.get(fruitId);
         String amount = Integer.toString(amountInt);
         dataHandler.editEntry(mEntryId, fruitId,amount);
         mFruitEntries.remove(fruitId);
         onDetailedEntryCnangeListener.onEntryRemoved(mFruitEntries);
         notifyItemRemoved(position);
    }
    public void setOnDetailedEntryCnangeListener(OnDetailedEntryCnangeListener onDetailedEntryCnangeListener) {
        this.onDetailedEntryCnangeListener = onDetailedEntryCnangeListener;
    }

    public void removeOnDetailedEntryChangeListener(OnDetailedEntryCnangeListener onDetailedEntryCnangeListener) {
        this.onDetailedEntryCnangeListener = null;
    }

    /**
     * Method pass Fruit attributes to the view section
     * @param viewHolder
     * @param i
     */

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (mFruitEntries != null && mFruitEntries.size() != 0) {
            Set fruitKeys = mFruitEntries.keySet();
            List <Integer> fruitIdList = new ArrayList<>(fruitKeys);
            int fruitId = fruitIdList.get(i);
            Log.d(TAG, "onBindViewHolder: fruit id" + fruitId);
            for (int k = 0; k < mFruitList.size(); k ++) {
                if(mFruitList.get(k).getID() == fruitId) {
                    mFruitType = mFruitList.get(k).getType();
                    break;
                }
            }
            viewHolder.fruitTypeEntry.setText(mFruitType);
            viewHolder.fruitAmountEntry.setText("Eaten: " + mFruitEntries.get(fruitId));
            for(int j = 0; j < mFruitList.size(); j++) {
                if(fruitId == mFruitList.get(j).getID()) {
                    Picasso.get()
                            .load(mFruitList.get(j).getImage())
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(viewHolder.fruitImageEntry);
                    break;
                }
            }
        } else {
            viewHolder.fruitTypeEntry.setText(mFruitType);
            viewHolder.fruitAmountEntry.setText("" + mFruitAmount);
            viewHolder.fruitImageEntry.setImageResource(R.drawable.placeholder);
        }
    }


    /**
     * Method returns size of the entryList; returns 1 if data is 0 or null
     */

    @Override
    public int getItemCount() {
        return ((mFruitEntries != null) && (mFruitEntries.size() !=0) ? mFruitEntries.size() : 1);
    }
    /**
     * View Holder class initiates elements inside the Entry
     */

    public class ViewHolder extends RecyclerView.ViewHolder  {

        TextView fruitTypeEntry;
        ImageView fruitImageEntry;
        TextView fruitAmountEntry;

        public ViewHolder(View itemView) {
            super(itemView);

            fruitTypeEntry = itemView.findViewById(R.id.fruit_type_detailed_entry);
            fruitAmountEntry = itemView.findViewById(R.id.fruit_amount_detailed_entry);
            fruitImageEntry = itemView.findViewById(R.id.fruit_image_detaled_entry);
        }
    }
}
