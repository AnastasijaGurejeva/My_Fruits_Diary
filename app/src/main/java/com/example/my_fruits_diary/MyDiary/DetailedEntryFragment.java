package com.example.my_fruits_diary.MyDiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_fruits_diary.DataHandling.DataHandler;
import com.example.my_fruits_diary.MainActivity;
import com.example.my_fruits_diary.Model.EntriesData;
import com.example.my_fruits_diary.Model.Fruit;
import com.example.my_fruits_diary.Model.FruitsData;
import com.example.my_fruits_diary.Model.OnDetailedEntryChangeListener;
import com.example.my_fruits_diary.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class DetailedEntryFragment extends Fragment implements OnDetailedEntryChangeListener {

    private String mDate;
    private String mTotalFruits;
    private HashMap<Integer, Integer> mEatenFruits;
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
    private RecyclerView recyclerView;
    private TextView mDateView;
    private FloatingActionButton onBackPressed;
    private Button onSavePressed;
    private boolean isDetailedEntry = true;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapterForDetailedEntry = new RecyclerViewAdapterForDetailedEntry(mEatenFruits, mFruitList, mEntryId, getActivity());
        mAdapterForDetailedEntry.setOnDetailedEntryChangeListener(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detailed_entry, container, false);
        mDateView = view.findViewById(R.id.date_entry_fragment);
        totalVitaminsView = view.findViewById(R.id.total_vitamins_entry_fragment);
        totalFruitsView = view.findViewById(R.id.total_fruits_entry_fragment);
        onAddNewFruit = view.findViewById(R.id.on_add_new_fruit_detailed_entry);
        recyclerView = view.findViewById(R.id.recycler_view_detailed_fragment);
        onBackPressed = view.findViewById(R.id.back_button_detailed_entry_fragment);
        onSavePressed = view.findViewById(R.id.save_detailed_entry);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDateView.setText(mDate);
        calculateVitaminsAndFruit();
        recyclerView.setAdapter(mAdapterForDetailedEntry);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        activateOnAddNewFruit();
        activateOnBackPressed();
        activateOnSavePressed();
    }


    public void calculateVitaminsAndFruit() {
        if (mEatenFruits.size() != 0) {
            mTotalFruits = mEatenFruits.values().stream()
                    .reduce(0, Integer::sum)
                    .toString();
            totalFruitsView.setText(mTotalFruits);
        } else {
            totalFruitsView.setText("0");
        }
        if (mEatenFruits.size() != 0) {
            int totalVitaminsAmount = 0;
            for (int k = 0; k < mEatenFruits.size(); k++) {
                Set<Integer> keys = mEatenFruits.keySet();
                List<Integer> fruitIdList = new ArrayList(keys);
                int fruitId = fruitIdList.get(k);
                int vitamins = 0;
                for (int j = 0; j < mFruitList.size(); j++) {
                    if (mFruitList.get(j).getID() == fruitId) {
                        vitamins = mFruitList.get(j).getVitamins();
                        break;
                    }
                }
                totalVitaminsAmount = totalVitaminsAmount + vitamins * mEatenFruits.get(fruitId);
            }
            totalVitaminsView.setText(totalVitaminsAmount + "");
        } else {
            totalVitaminsView.setText("0");
        }
    }

    public void dataPassed(EntriesData entriesData, FruitsData fruitsData, int position) {
        mEatenFruits = entriesData.getEntriesData().get(position).getmEatenFruits();
        mDate = entriesData.getEntriesData().get(position).getDate();
        mEntriesData = entriesData;
        mEntryId = entriesData.getEntriesData().get(position).getEntryId();
        mPosition = position;
        mFruitList = fruitsData.getFruitData();
        mFruitsData = fruitsData;
        mPosition = position;
    }


    public void activateOnBackPressed() {
        onBackPressed.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });
    }

    public void activateOnSavePressed() {
        onSavePressed.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });
    }


    public void activateOnAddNewFruit() {
        onAddNewFruit.setOnClickListener(v -> {
            AddFruitFragment addFruitFragment = new AddFruitFragment();
            addFruitFragment.onPassedDataFromDetailedFragment(mEntriesData, mFruitsData, mPosition, isDetailedEntry);
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_fragment, addFruitFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }


    @Override
    public void onEntryAmountChanged(HashMap<Integer, Integer> fruitEntries, int fruitId, String fruitAmount) {
        mEatenFruits = fruitEntries;
        mDataHandler.editEntry(mEntryId, fruitId, fruitAmount);
        calculateVitaminsAndFruit();
    }
}
