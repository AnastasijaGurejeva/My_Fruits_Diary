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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.my_fruits_diary.DataHandling.EntriesData;
import com.example.my_fruits_diary.DataHandling.FruitsData;
import com.example.my_fruits_diary.DataHandling.PostCaller;
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
    private int mSelectedEntryID;
    private int mSelectedFruitId;
    private String mSelectedAmount;
    private Button mSaveEntry;
    private TextView mSelectFruit;
    private EditText mSelectAmount;
    private PostCaller postCaller = new PostCaller();
    private final String url = "https://fruitdiary.test.themobilelife.com/api/entries";


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
                        calendar.set(year, month, day-151);
                        mSelectedDate = (new SimpleDateFormat("yyyy-MM-DD").format(calendar.getTime()));
                        setEntryDate.setText(mSelectedDate);
                        Log.d(TAG, "onDateSet: " + mSelectedDate);
                    }
                }, yy, mm, dd);

                datePicker.show();
            }
        });

        mSelectFruit = view.findViewById(R.id.selectFruit);
        mSelectAmount = view.findViewById(R.id.selectAmount);
        mSaveEntry = view.findViewById(R.id.saveEntry);
        mSelectedAmount = mSelectAmount.getText().toString().trim();

        RecyclerView recyclerView = view.findViewById(R.id.frame_availavleFruits);
        mAdapterForFruits = new RecyclerViewAdapterForAvialableFruits(mFruitList, getActivity(), this);
        recyclerView.setAdapter(mAdapterForFruits);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        onOkClickActivated();
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
        Fruit fruit = mFruitList.get(position);
        mSelectedFruitId = fruit.getID();
        mSelectFruit.setText(fruit.getType());


    }

    public void onOkClickActivated() {
        mSaveEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onOkClick: activated");
                postCaller.postNewEntry(mSelectedDate);
                List<Entry> entries = mEntriesData.getEntriesData();
                for (int i = 0; i < entries.size(); i++) {
                    if (mSelectedDate.equals(entries.get(i).getDate())) {
                        mSelectedEntryID = (entries.get(i).getEntryId());
                        break;
                    }
                }
                postCaller.editEntry(mSelectedEntryID, mSelectedFruitId, mSelectedAmount);
            }
        });
    }


}
