package com.example.my_fruits_diary.MyDiary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.my_fruits_diary.DataHandling.DataHandler;
import com.example.my_fruits_diary.DataHandling.EntriesData;
import com.example.my_fruits_diary.DataHandling.FruitsData;
import com.example.my_fruits_diary.MainActivity;
import com.example.my_fruits_diary.R;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class AddFruitFragment extends Fragment
        implements RecyclerViewAdapterForAvialableFruits.OnEntryListener, Observer {

    private List<Fruit> mFruitList;
    private static final String TAG = "AddFruitFragment";
    private RecyclerViewAdapterForAvialableFruits mAdapterForFruits;
    private FruitsData mFruitsData;
    private EntriesData mEntriesData;
    private TextView setEntryDate;
    private String mSelectedDate;
    private int mSelectedEntryID;
    private int mSelectedFruitId;
    private String mSelectedAmount;
    private Button mSaveEntry;
    private TextView mSelectFruit;
    private EditText mSelectAmount;
    private DataHandler dataHandler = new DataHandler();
    private boolean isIdReceived;

    public AddFruitFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_fruit, container, false);

        mSelectFruit = view.findViewById(R.id.select_fruit);
        mSelectAmount = view.findViewById(R.id.select_amount);
        mSaveEntry = view.findViewById(R.id.save_entry);
        mSelectedAmount = mSelectAmount.getText().toString().trim();

        RecyclerView recyclerView = view.findViewById(R.id.frame_availavle_fruits);
        mAdapterForFruits = new RecyclerViewAdapterForAvialableFruits(mFruitList, getActivity(), this);
        recyclerView.setAdapter(mAdapterForFruits);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        onOkClickActivated();
        return view;
    }


    public void updateFruitsData(FruitsData fruitsData, EntriesData entriesData) {
        mFruitsData = fruitsData;
        mEntriesData = entriesData;
        mFruitsData.addObserver(this);
        mFruitList = mFruitsData.getFruitData();
        Log.d(TAG, "setData: data " + fruitsData.toString());
        Log.d(TAG, "setData: observer added for Fruit list");
    }

    @Override
    public void update(Observable o, Object data) {
        mAdapterForFruits.loadNewData((List<Fruit>) data);
        Log.d(TAG, "UPDATED FROM OBSERVER FRUITS " + data.toString());
    }

    @Override
    public void onEntryClick(int position) {
        Log.d(TAG, "onEntryClick: clicked : " + position);
        List<Entry> entries = mEntriesData.getEntriesData();
        Fruit fruit = mFruitList.get(position);
        mSelectedFruitId = fruit.getID();
        mSelectFruit.setText(fruit.getType());
    }

    public void onPassId(int id) {
        mSelectedEntryID = id;
    }

    public void onOkClickActivated() {
        mSaveEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onOkClick: activated");
                mSelectedAmount = mSelectAmount.getText().toString().trim();
                Log.d(TAG, "onClick: amount is " + mSelectedAmount);

                if (mSelectedEntryID != 0) {
                        dataHandler.editEntry(mSelectedEntryID, mSelectedFruitId, mSelectedAmount);
                    }
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
