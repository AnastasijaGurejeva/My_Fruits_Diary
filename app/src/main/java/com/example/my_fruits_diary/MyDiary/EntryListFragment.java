package com.example.my_fruits_diary.MyDiary;


import android.os.Bundle;
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
    protected int id;
    // private ArrayList<Integer> mFruitImages = new ArrayList<>();
    private ArrayList<Integer> mTotalVitamins = new ArrayList<>();
    private ArrayList<LocalDate> mDate = new ArrayList<>();

    public EntryListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_entry_list, container, false);
        return view;
    }

    /**
     * Method initializes List view with values for Wish List
     */

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: recycler view init");

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mEntryId, mFruitTitle, mFruitAmount, mTotalVitamins,
                mDate, (ActivityMain) this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onEntryClick(int position) {
        Log.d(TAG, "onEntryClick: clicked : " + position);
        id = mEntryId.get(position);

        DetailedEntryFragment detailedEntryFragment = new DetailedEntryFragment();

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_entryDetails_fragment, detailedEntryFragment)
                .commit();
    }

}
