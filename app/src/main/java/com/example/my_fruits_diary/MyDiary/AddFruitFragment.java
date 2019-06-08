package com.example.my_fruits_diary.MyDiary;

import android.app.DatePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.my_fruits_diary.DataHandling.EntriesData;
import com.example.my_fruits_diary.DataHandling.FruitsData;
import com.example.my_fruits_diary.DataHandling.PostHandler;
import com.example.my_fruits_diary.R;

import java.util.Calendar;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class AddFruitFragment extends Fragment
        implements RecyclerViewAdapterForAvialableFruits.OnEntryListener, Observer {

    private List<Fruit> mFruitList;
    private static final String TAG = "AddFruitFragment";
    private RecyclerViewAdapterForAvialableFruits mAdapterForFruits;
    private FruitsData mFruitsData;
    private EntriesData mEntriesData;
    private TextView setEntryDate;
    private String mSelectedDate;
    private final String url ="https://fruitdiary.test.themobilelife.com/api/entries";


    public AddFruitFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_fruit, container, false);

        setEntryDate = view.findViewById(R.id.setDate);
        setEntryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                final int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(android.widget.DatePicker v, int year, int month, int day) {
                        calendar.set(year, month , day);
                        mSelectedDate = ( new SimpleDateFormat("yyyy-MM-DD").format(calendar.getTime()));
                        setEntryDate.setText(mSelectedDate);
                        createEntry();
                        Log.d(TAG, "onDateSet: " + mSelectedDate);
                    }
                }, yy, mm, dd);

                datePicker.show();
            }
        });


        RecyclerView recyclerView = view.findViewById(R.id.frame_availavleFruits);

        mAdapterForFruits = new RecyclerViewAdapterForAvialableFruits(mFruitList, getActivity(), this);
        recyclerView.setAdapter(mAdapterForFruits);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }


    public void updateFruitsData(FruitsData fruitsData, EntriesData entriesData) {
        mFruitsData = fruitsData;
        mEntriesData = entriesData;
        mFruitsData.addObserver(this);
        mFruitList = mFruitsData.getFruitData();
        Log.d(TAG, "setData: data " + fruitsData.toString());
        Log.d(TAG, "setData: observer added for Fruit list");
    }

    @Override
    public void update(Observable o, Object data) {
        mAdapterForFruits.loadNewData((List<Fruit>) data);
        Log.d(TAG, "UPDATED FROM OBSERVER FRUITS " + data.toString());
    }

    @Override
    public void onEntryClick(int position) {
        Log.d(TAG, "onEntryClick: clicked : " + position);
        List<Entry> entries = mEntriesData.getEntriesData();

        //add to th entry

    }

    public void createEntry() {
        PostHandler postHandler = new PostHandler("2019-06-10");
        postHandler.postNewEntry();
    }



}
