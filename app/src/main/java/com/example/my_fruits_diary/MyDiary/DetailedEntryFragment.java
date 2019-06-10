package com.example.my_fruits_diary.MyDiary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_fruits_diary.R;

import java.util.HashMap;
import java.util.List;


public class DetailedEntryFragment extends Fragment {

    private String mDate;
    private int mTotalVitamins;
    private int mTotalFruits;
    private HashMap<Integer, Integer> mFruitEntries;
    private RecyclerViewAdapterForDetailedEntry mAdapterForDetailedEntry;
    private List<Fruit> mFruits;
    private static final String TAG = "DetailedEntryFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detailed_entry, container, false);
        TextView dateView = view.findViewById(R.id.date_entry_fragment);
        TextView totalVitaminsView = view.findViewById(R.id.total_vitamins_entry_fragment);
        TextView totalFruits = view.findViewById(R.id.total_fruits_entry_fragment);


        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_detailed_fragment);
        mAdapterForDetailedEntry = new RecyclerViewAdapterForDetailedEntry(mFruitEntries, mFruits, getActivity());
        recyclerView.setAdapter(mAdapterForDetailedEntry);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    public void dataPassed(HashMap<Integer, Integer> fruitEntries, List<Fruit> fruit){
        mFruitEntries = fruitEntries;
        mFruits = fruit;

    }
}
