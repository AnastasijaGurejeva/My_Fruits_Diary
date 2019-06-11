package com.example.my_fruits_diary.MyDiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_fruits_diary.DataHandling.DataHandler;
import com.example.my_fruits_diary.Model.EntriesData;
import com.example.my_fruits_diary.Model.Fruit;
import com.example.my_fruits_diary.Model.FruitsData;
import com.example.my_fruits_diary.MainActivity;
import com.example.my_fruits_diary.Model.OnDetailedEntryCnangeListener;
import com.example.my_fruits_diary.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.List;


public class DetailedEntryFragment extends Fragment implements OnDetailedEntryCnangeListener {

    private String mDate;
    private String mTotalVitamins;
    private String mTotalFruits;
    private HashMap<Integer, Integer> mFruitEntries;
    private RecyclerViewAdapterForDetailedEntry mAdapterForDetailedEntry;
    private List<Fruit> mFruitList;
    private int mPosition;
    private int mEntryId;
    private EntriesData mEntriesData;
    private FruitsData mFruitsData;
    private static final String TAG = "DetailedEntryFragment";
    private FloatingActionButton onAddNewFruit;
    private TextView totalVitaminsView;
    private TextView totalFruitsView;
    private DataHandler mDataHandler = new DataHandler();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detailed_entry, container, false);
        TextView dateView = view.findViewById(R.id.date_entry_fragment);
        totalVitaminsView = view.findViewById(R.id.total_vitamins_entry_fragment);
        totalFruitsView = view.findViewById(R.id.total_fruits_entry_fragment);
        onAddNewFruit = view.findViewById(R.id.on_add_new_fruit_detailed_entry);

        dateView.setText(mDate);
        calculateVitaminsAndFruit();


        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_detailed_fragment);
        mAdapterForDetailedEntry = new RecyclerViewAdapterForDetailedEntry(mFruitEntries, mFruitList, mEntryId, getActivity());
        mAdapterForDetailedEntry.setOnDetailedEntryCnangeListener(this);
        recyclerView.setAdapter(mAdapterForDetailedEntry);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteForDetailedEntry(mAdapterForDetailedEntry, getContext()));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        activateOnAddNewFruit();

        return view;
    }
     public void calculateVitaminsAndFruit() {
         if (mFruitEntries.size() != 0) {
             mTotalFruits = mFruitEntries.values().stream()
                     .reduce(0, Integer::sum)
                     .toString();
             totalFruitsView.setText(mTotalFruits);
         } else {
             totalFruitsView.setText("0");
         }
         if (mFruitEntries.size() != 0) {
             mTotalVitamins = mFruitEntries.keySet().stream()
                     .map(x -> (mFruitList.get(x).getVitamins()) * mFruitEntries.get(x))
                     .reduce(0, Integer::sum)
                     .toString();
             totalVitaminsView.setText(mTotalVitamins);
         } else {
             totalVitaminsView.setText("0");
         }

     }

    public void dataPassed(EntriesData entriesData, FruitsData fruitsData, int position) {
        mFruitEntries = entriesData.getEntriesData().get(position).getmEatenFruits();
        mFruitList = fruitsData.getFruitData();
        mDate = entriesData.getEntriesData().get(position).getDate();
        mEntriesData = entriesData;
        mFruitsData = fruitsData;
        mPosition = position;
        mEntryId = entriesData.getEntriesData().get(position).getEntryId();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    public void activateOnAddNewFruit() {
        onAddNewFruit.setOnClickListener(v -> {
            AddFruitFragment addFruitFragment = new AddFruitFragment();
            addFruitFragment.onPassedDataFromDetailedFragment(mEntriesData, mFruitsData, mPosition);
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_fragment, addFruitFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public void onEntryFruitRemoved(HashMap<Integer, Integer> fruitEntries, int fruitId, String fruitAmount) {
        mFruitEntries = fruitEntries;
        mDataHandler.editEntry(mEntryId, fruitId, fruitAmount);
        calculateVitaminsAndFruit();
    }

    public void onEntryRemoved() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        mDataHandler.onDeleteOneEntry(mEntryId);
    }

    @Override
    public void onEntryAmountChanged(HashMap<Integer, Integer> fruitEntries, int fruitId, String fruitAmount) {
        mFruitEntries = fruitEntries;
        mDataHandler.editEntry(mEntryId, fruitId, fruitAmount);
        calculateVitaminsAndFruit();
    }
}
