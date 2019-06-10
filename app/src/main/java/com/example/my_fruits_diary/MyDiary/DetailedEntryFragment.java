package com.example.my_fruits_diary.MyDiary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_fruits_diary.DataHandling.EntriesData;
import com.example.my_fruits_diary.DataHandling.FruitsData;
import com.example.my_fruits_diary.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.List;


public class DetailedEntryFragment extends Fragment {

    private String mDate;
    private String mTotalVitamins;
    private String mTotalFruits;
    private HashMap<Integer, Integer> mFruitEntries;
    private RecyclerViewAdapterForDetailedEntry mAdapterForDetailedEntry;
    private List<Fruit> mFruitList;
    private int mPosition;
    private EntriesData mEntriesData;
    private FruitsData mFruitsData;
    private static final String TAG = "DetailedEntryFragment";
    private FloatingActionButton onAddNewFruit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detailed_entry, container, false);
        TextView dateView = view.findViewById(R.id.date_entry_fragment);
        TextView totalVitaminsView = view.findViewById(R.id.total_vitamins_entry_fragment);
        TextView totalFruitsView = view.findViewById(R.id.total_fruits_entry_fragment);
        onAddNewFruit = view.findViewById(R.id.on_add_new_fruit_detailed_entry);

        dateView.setText(mDate);
        if (mFruitEntries.size() != 0) {
            mTotalFruits = mFruitEntries.values().stream()
                    .reduce(0, Integer::sum)
                    .toString();
            totalFruitsView.setText(mTotalFruits);
        } else {
            totalFruitsView.setText(0);
        }
        if (mFruitEntries.size() != 0) {
            mTotalVitamins = mFruitEntries.keySet().stream()
                    .map(x -> (mFruitList.get(x).getVitamins()) * mFruitEntries.get(x))
                    .reduce(0, Integer::sum)
                    .toString();
            totalVitaminsView.setText(mTotalVitamins);
        } else {
            totalVitaminsView.setText(0);
        }

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_detailed_fragment);
        mAdapterForDetailedEntry = new RecyclerViewAdapterForDetailedEntry(mFruitEntries, mFruitList, getActivity());
        recyclerView.setAdapter(mAdapterForDetailedEntry);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        activateOnAddNewFruit();

        return view;
    }

    public void dataPassed(EntriesData entriesData, FruitsData fruitsData, int position) {
        mFruitEntries = entriesData.getEntriesData().get(position).getmEatenFruits();
        mFruitList = fruitsData.getFruitData();
        mDate = entriesData.getEntriesData().get(position).getDate();
        mEntriesData = entriesData;
        mFruitsData = fruitsData;
        mPosition = position;
    }

    public void activateOnAddNewFruit() {
        onAddNewFruit.setOnClickListener(v -> {
            AddFruitFragment addFruitFragment = new AddFruitFragment();
            addFruitFragment.onPassedDataFromDetailedFragment(mEntriesData, mFruitsData, mPosition);
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_fragment, addFruitFragment)
                    .commit();
        });
    }
}
