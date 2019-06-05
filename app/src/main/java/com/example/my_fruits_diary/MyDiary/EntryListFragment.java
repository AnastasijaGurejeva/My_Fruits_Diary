package com.example.my_fruits_diary.MyDiary;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.my_fruits_diary.R;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Tab fragment for entering eaten fruits on selected day
 * Created 5/06/2019
 * Author: Anastasija Gurejeva
 */
public class EntryListFragment extends Fragment implements RecyclerViewAdapter.OnEntryListener {
    private static final String TAG = "EntryListFragment";

    protected ArrayList<Integer> mEntryId = new ArrayList<>();
    private ArrayList<String> mFruitTitle = new ArrayList<>();
    private ArrayList<Integer> mFruitAmount = new ArrayList<>();
    private ArrayList<Integer> mTotalVitamins = new ArrayList<>();
    private ArrayList<LocalDate> mDate = new ArrayList<>();
    // private ArrayList<Integer> mFruitImages = new ArrayList<>();

    protected int id;
    private FloatingActionButton onAddNewEntry;

    public EntryListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_entry_list, container, false);
        onAddNewEntry = view.findViewById(R.id.add_Entry);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mEntryId, mFruitTitle, mFruitAmount, mTotalVitamins,
                mDate, getActivity(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setDataTest();
        activateOnAddNewEntry();


        return view;
    }


    @Override
    public void onEntryClick(int position) {
        Log.d(TAG, "onEntryClick: clicked : " + position);
        id = mEntryId.get(position);

        DetailedEntryFragment detailedEntryFragment = new DetailedEntryFragment();

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragment, detailedEntryFragment)
                .commit();
    }

    public void setDataTest() {

        mEntryId.add(1);
        mFruitTitle.add("Apple");
        mFruitAmount.add(3);
        mTotalVitamins.add(10);
        mDate.add(LocalDate.now());

    }


    public void activateOnAddNewEntry() {

        onAddNewEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onAddEntry: activated");
                NewEntryFragment newEntryFragment = new NewEntryFragment();

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_fragment, newEntryFragment)
                        .commit();

            }
        });
    }



}
