package com.example.my_fruits_diary.MyDiary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.my_fruits_diary.DataHandling.EntriesData;
import com.example.my_fruits_diary.R;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;


public class DetailedEntryFragment extends Fragment {

    private String mDate;
    private int mTotalVitamins;
    private int mTotalFruits;
    private HashMap<Integer, Integer> mFruitEntries;
    private EntriesData mEntriesData;
    private int mPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detailed_entry, container, false);
        TextView dateView = view.findViewById(R.id.date_entryFr);
        TextView totalVitaminsView = view.findViewById(R.id.totalVitamins_entryFr);
        TextView totalFruits = view.findViewById(R.id.totalFruits_entryFr);
        setData();
        dateView.setText(mDate);
        totalVitaminsView.setText(mTotalVitamins + "");
        totalFruits.setText(mTotalFruits + "");


        return view;
    }

    public void dataPassed(int position, EntriesData entryData) {
        mPosition = position;
        mEntriesData = entryData;
    }

    public void setData() {
        List<Entry> entries = mEntriesData.getEntriesData();
        Entry entry = entries.get(mPosition);
        mFruitEntries = entry.getmEatenFruits();
        mDate = entry.getDate();
        mTotalFruits = mFruitEntries.size();
        if (mFruitEntries.size() != 0) {
            Collection<Integer> values = mFruitEntries.values();
            mTotalVitamins = 0;
            for (Integer value : values) {
                mTotalVitamins += value;
            }
        } else {
            mTotalVitamins = 0;
        }
    }
}
