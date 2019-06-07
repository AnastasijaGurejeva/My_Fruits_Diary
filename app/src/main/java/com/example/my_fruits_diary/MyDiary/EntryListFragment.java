package com.example.my_fruits_diary.MyDiary;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.my_fruits_diary.DataHandling.EntriesData;
import com.example.my_fruits_diary.DataHandling.FruitsData;
import com.example.my_fruits_diary.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Tab fragment for entering eaten fruits on selected day
 * Created 5/06/2019
 * Author: Anastasija Gurejeva
 */
public class EntryListFragment extends Fragment implements Observer {
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
    private RecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            Log.d(TAG, "onClick: clicked position " + position);
            DetailedEntryFragment detailedEntryFragment = new DetailedEntryFragment();
            detailedEntryFragment.dataPassed(position, mEntriesData);
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_fragment, detailedEntryFragment)
                    .commit();
        }
    };


    public EntryListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_entry_list, container, false);
        onAddNewEntry = view.findViewById(R.id.add_Entry);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        Log.d(TAG, "onCreateView: setting layout manager");
        recyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerViewAdapter(mEntries, getActivity());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter.setOnEntryListener(onItemClickListener);

        activateOnAddNewFruit();
        return view;
    }

    public void setData(EntriesData entriesData) {
        mEntriesData = entriesData;
        mEntriesData.addObserver(this);
        Log.d(TAG, "setData: data " + entriesData.toString());
        Log.d(TAG, "setData: observer added");
    }

    @Override
    public void update(Observable o, Object data) {
        mAdapter.loadNewData((List<Entry>) data);
        Log.d(TAG, "UPDATED FROM OBSERVER " + data.toString());
    }

    public void setFruitsData(FruitsData fruitsData) {
        mFruitsData = fruitsData;
    }

    public void activateOnAddNewFruit() {
       onAddNewEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onAddEntry: activated");
                AddFruitFragment addFruitFragment = new AddFruitFragment();

                addFruitFragment.updateFruitsData(mFruitsData);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_new_entry, addFruitFragment)
                        .commit();

            }
        });
    }


}
