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

import com.example.my_fruits_diary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Tab fragment for entering eaten fruits on selected day
 * Created 5/06/2019
 * Author: Anastasija Gurejeva
 */
public class EntryListFragment extends Fragment implements RecyclerViewAdapter.OnEntryListener {
    private static final String TAG = "EntryListFragment";

    protected ArrayList<Integer> mEntryId = new ArrayList<>();
    private ArrayList<Integer> mFruitAmount = new ArrayList<>();
    private ArrayList<Integer> mTotalVitamins = new ArrayList<>();
    private ArrayList<String> mDate = new ArrayList<>();

    protected int id;
    private FloatingActionButton onAddNewEntry;
    private List<Fruit> mFruits;
    private List<Entry> mEntries;

    public EntryListFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_entry_list, container, false);
        onAddNewEntry = view.findViewById(R.id.add_Entry);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mEntries, getActivity(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        activateOnAddNewEntry();


        return view;
    }

    public void updateData(List<Fruit> fruits) {
        mFruits = fruits;
    }

    public void updateEntryData(List<Entry> entries) {
        mEntries = entries;
    }


    @Override
    public void onEntryClick(int position) {
        Log.d(TAG, "onEntryClick: clicked : " + position);
        id = mEntryId.get(position);

        DetailedEntryFragment detailedEntryFragment = new DetailedEntryFragment();
        detailedEntryFragment.

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragment, detailedEntryFragment)
                .commit();
    }



    public void activateOnAddNewEntry() {

        onAddNewEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onAddEntry: activated");
                //check if data downloading is complete
                //otherwise create a fragment asking to wait.
                AddFruitFragment addFruitFragment = new AddFruitFragment();
                addFruitFragment.updateData(mFruits);

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_new_entry, addFruitFragment)
                        .commit();

            }
        });
    }



}
