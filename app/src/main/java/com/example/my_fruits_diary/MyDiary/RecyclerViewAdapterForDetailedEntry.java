package com.example.my_fruits_diary.MyDiary;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_fruits_diary.Model.Fruit;
import com.example.my_fruits_diary.Model.OnDetailedEntryCnangeListener;
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
        Set fruitKeys = mFruitEntries.keySet();
        List<Integer> fruitsId = new ArrayList<>(fruitKeys);
        int fruitId = fruitsId.get(position);
        int amountInt = mFruitEntries.get(fruitId);
        String amount = Integer.toString(amountInt);
        if (mFruitEntries.size() == 1) {
            onDetailedEntryCnangeListener.onEntryRemoved();
            notifyItemRemoved(position);
        } else {
            mFruitEntries.remove(fruitId);
            onDetailedEntryCnangeListener.onEntryFruitRemoved(mFruitEntries, fruitId, amount);
            notifyItemRemoved(position);
        }
    }

    public void setOnDetailedEntryCnangeListener(OnDetailedEntryCnangeListener onDetailedEntryCnangeListener) {
        this.onDetailedEntryCnangeListener = onDetailedEntryCnangeListener;
    }


    /**
     * Method pass Fruit attributes to the view section
     *
     * @param viewHolder
     * @param i
     */

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (mFruitEntries != null && mFruitEntries.size() != 0) {
            Set fruitKeys = mFruitEntries.keySet();
            List<Integer> fruitIdList = new ArrayList<Integer>(fruitKeys);
            int fruitId = fruitIdList.get(i);
            Log.d(TAG, "onBindViewHolder: fruit id" + fruitId);
            for (int k = 0; k < mFruitList.size(); k++) {
                if (mFruitList.get(k).getID() == fruitId) {
                    mFruitType = mFruitList.get(k).getType();
                    break;
                }
            }
            viewHolder.fruitTypeEntry.setText(mFruitType);
            viewHolder.fruitAmountEntry.setText(mFruitEntries.get(fruitId).toString());
            viewHolder.fruitAmountEntry.setOnKeyListener((view, i1, keyEvent) -> {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    String newAmountText = viewHolder.fruitAmountEntry.getText().toString().trim();
                    int newAmount = Integer.parseInt(newAmountText);
                    if (newAmount <= 0) {
                        viewHolder.fruitAmountEntry.setError("Amount must be larger than 0");
                    } else {
                        mFruitEntries.remove(fruitId);
                        mFruitEntries.put(fruitId, newAmount);
                        onDetailedEntryCnangeListener.onEntryAmountChanged(mFruitEntries, fruitId, newAmountText);
                        return true;
                    }
                }
                return false;
            });

            for (int j = 0; j < mFruitList.size(); j++) {
                if (fruitId == mFruitList.get(j).getID()) {
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
            viewHolder.fruitAmountEntry.setText(Integer.toString(mFruitAmount));
            viewHolder.fruitImageEntry.setImageResource(R.drawable.placeholder);
        }
    }


    /**
     * Method returns size of the entryList; returns 1 if data is 0 or null
     */

    @Override
    public int getItemCount() {
        return ((mFruitEntries != null) && (mFruitEntries.size() != 0) ? mFruitEntries.size() : 1);
    }

    /**
     * View Holder class initiates elements inside the Entry
     */

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView fruitTypeEntry;
        TextView amountEntryText;
        ImageView fruitImageEntry;
        EditText fruitAmountEntry;

        public ViewHolder(View itemView) {
            super(itemView);

            fruitTypeEntry = itemView.findViewById(R.id.fruit_type_detailed_entry);
            fruitAmountEntry = itemView.findViewById(R.id.fruit_amount_detailed_entry);
            fruitImageEntry = itemView.findViewById(R.id.fruit_image_detaled_entry);
            amountEntryText = itemView.findViewById(R.id.fruit_amount_text_detailed_row);
        }
    }
}
