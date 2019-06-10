package com.example.my_fruits_diary.MyDiary;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.my_fruits_diary.DataHandling.DataHandler;
import com.example.my_fruits_diary.DataHandling.DatePickerFragment;
import com.example.my_fruits_diary.DataHandling.DownloadDataHandler;
import com.example.my_fruits_diary.DataHandling.EntriesData;
import com.example.my_fruits_diary.DataHandling.FruitsData;
import com.example.my_fruits_diary.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Tab fragment for entering eaten fruits on selected day
 * Created 5/06/2019
 * Author: Anastasija Gurejeva
 */
public class EntryListFragment extends Fragment implements Observer,
        OnPostDataReceivedListener, OnFruitDataReceivedListener, RecyclerViewAdapter.OnEntryClickListener {
    private static final String TAG = "EntryListFragment";

    protected ArrayList<Integer> mEntryId = new ArrayList<>();
    private ArrayList<Integer> mFruitAmount = new ArrayList<>();
    private ArrayList<Integer> mTotalVitamins = new ArrayList<>();
    private ArrayList<String> mDate = new ArrayList<>();
    protected int id;
    private FloatingActionButton onAddNewEntry;
    private FruitsData mFruitsData;
    private EntriesData mEntriesData;
    private List<Fruit> mFruits;
    private List<Entry> mEntries;
    private HashMap<Integer, Integer> mfruitEntries;
    private RecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static final int REQUEST_CODE = 1;
    private AddFruitFragment addFruitFragment;
    private String mSelectedDate;
    private int mSelectedEntryID;
    private boolean isFruitDataReceived = false;
    private DataHandler dataHandler = new DataHandler();
    private DownloadDataHandler mDownloadDataHandler;
    private RecyclerView recyclerView;


    public EntryListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_entry_list, container, false);
        onAddNewEntry = view.findViewById(R.id.add_Entry);

        recyclerView = view.findViewById(R.id.recycler_view_detailed_fragment);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        Log.d(TAG, "onCreateView: setting layout manager");
        recyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerViewAdapter(mEntries, getActivity(), this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDelete(mAdapter, getContext()));
        itemTouchHelper.attachToRecyclerView(recyclerView);


        activateOnAddNewEntry();
        return view;
    }






    public void setData(EntriesData entriesData, DownloadDataHandler downloadDataHandler) {
        mEntriesData = entriesData;
        mDownloadDataHandler = downloadDataHandler;
        mDownloadDataHandler.setOnFruitDataReceivedListener(this);
        mEntriesData.addObserver(this);
        Log.d(TAG, "setData: data " + entriesData.toString());
        Log.d(TAG, "setData: observer added");
    }

    @Override
    public void update(Observable o, Object data) {
        mAdapter.loadNewData((List<Entry>) data);
    }

    @Override
    public void onReceived(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            mSelectedEntryID = (Integer) jsonObject.get("id");
            Log.d(TAG, "onReceived: id " + mSelectedEntryID);
            addFruitFragment = new AddFruitFragment();
            addFruitFragment.updateFruitsData(mFruitsData, mEntriesData);
            if (mSelectedEntryID != 0) {
                dataHandler.removeOnPostDataReceivedListener(this);
                Log.d(TAG, "onReceived: starting addfruit fragment " + mSelectedEntryID);
                addFruitFragment.onPassId(mSelectedEntryID);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_fragment, addFruitFragment)
                        .commit();
            }
        } catch (JSONException e) {
            Log.e(TAG, "onReceived: id is" + e.getMessage());

        }
    }


    public void activateOnAddNewEntry() {
        onAddNewEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddNewEntry.hide();
                DialogFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setTargetFragment(EntryListFragment.this, REQUEST_CODE);
                datePickerFragment.show(getFragmentManager(), "datePicker");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mSelectedDate = data.getStringExtra("selectedDate");
            Log.d(TAG, "onActivityResult: selected date " + mSelectedDate);
            dataHandler.setOnPostDataReceivedListener(this);
            dataHandler.postNewEntry(mSelectedDate);
        }
    }

    @Override
    public void onEntryClickListener(int position) {
        Log.d(TAG, "onClick: clicked position " + position);
        mfruitEntries = mEntriesData.getEntriesData().get(position).getmEatenFruits();
        DetailedEntryFragment detailedEntryFragment = new DetailedEntryFragment();
        if (isFruitDataReceived) {
            detailedEntryFragment.dataPassed(mfruitEntries, mFruits);
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_fragment, detailedEntryFragment)
                    .commit();
        }
    }

    @Override
    public void onReceivedFruitsData(FruitsData fruitsData) {
        mFruitsData = fruitsData;
        mFruits = mFruitsData.getFruitData();
        Log.d(TAG, "onReceivedFruitsData: entryFragment" + mFruits.toString());
        isFruitDataReceived = true;
    }
}








