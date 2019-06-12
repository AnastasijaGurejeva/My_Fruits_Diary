package com.example.my_fruits_diary.MyDiary;


import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_fruits_diary.DataHandling.DataHandler;
import com.example.my_fruits_diary.DataHandling.DownloadDataHandler;
import com.example.my_fruits_diary.MainActivity;
import com.example.my_fruits_diary.Model.EntriesData;
import com.example.my_fruits_diary.Model.Entry;
import com.example.my_fruits_diary.Model.FruitsData;
import com.example.my_fruits_diary.Model.OnEntryDeleteListener;
import com.example.my_fruits_diary.Model.OnFruitDataReceivedListener;
import com.example.my_fruits_diary.Model.OnPostDataReceivedListener;
import com.example.my_fruits_diary.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Tab fragment for entering eaten fruits on selected day
 * Created 5/06/2019
 * Author: Anastasija Gurejeva
 */
public class EntryListFragment extends Fragment implements Observer, OnPostDataReceivedListener,
        OnFruitDataReceivedListener, RecyclerViewAdapter.OnEntryClickListener,
        DatePickerDialog.OnDateSetListener, OnEntryDeleteListener {
    private static final String TAG = "EntryListFragment";

    protected int id;
    private FloatingActionButton onAddNewEntry;
    private FloatingActionButton onDeleteAllEntries;
    private FloatingActionButton onRefreshData;
    private FruitsData mFruitsData;
    private EntriesData mEntriesData;
    private List<Entry> mEntries;
    private RecyclerViewAdapter mAdapter;
    private boolean isFruitDataReceived = false;
    private DataHandler dataHandler = new DataHandler();
    private DownloadDataHandler mDownloadDataHandler;
    private RecyclerView recyclerView;
    private int mPosition;
    private int mSelectedEntryID;


    public EntryListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new RecyclerViewAdapter(mEntries, getActivity(), this);
        mAdapter.setOnEntryDeleteListener(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entry_list, container, false);
        onAddNewEntry = view.findViewById(R.id.add_Entry);
        onDeleteAllEntries = view.findViewById(R.id.deleteButton);
        onRefreshData = view.findViewById(R.id.refreshButton);
        recyclerView = view.findViewById(R.id.recycler_view_detailed_fragment);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            mPosition = savedInstanceState.getInt("selectedPosition");
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getActivity());
        Log.d(TAG, "onCreateView: setting layout manager");
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        SwipeToDelete swipeHandler = new SwipeToDelete(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                mAdapter.deleteItem(position);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeHandler);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        activateOnAddNewEntry();
        activateOnRefreshData();
        activateOnDeleteAllEntries();
    }


    public void activateOnDeleteAllEntries() {
        onDeleteAllEntries.setOnClickListener(v -> {
            DeleteAllEntriesDialog deleteDialog = new DeleteAllEntriesDialog();
            deleteDialog.show(getActivity().getSupportFragmentManager(), "delete dialog");
        });
    }

    public void activateOnRefreshData() {
        onRefreshData.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selectedPosition", mPosition);
    }

    /**
     * Method is called by Main activity to pass instance of EntriesData
     * Observer is added to monitor changes
     */

    public void setData(EntriesData entriesData, DownloadDataHandler downloadDataHandler) {
        mEntriesData = entriesData;
        mEntries = mEntriesData.getEntriesData();
        mDownloadDataHandler = downloadDataHandler;
        mDownloadDataHandler.setOnFruitDataReceivedListener(this);
        mEntriesData.addObserver(this);
        Log.d(TAG, "setData: data " + entriesData.toString());
        Log.d(TAG, "setData: observer added");
    }

    /**
     * Method receives callback when Entry List if fully loaded
     * And calls method to update recyclerView
     */

    @Override
    public void update(Observable o, Object data) {
        if (data != null) {
            mAdapter.loadNewData((List<Entry>) data);
            mEntries = (List<Entry>) data;
            Log.d(TAG, "update: dataEntries loaded" + data.toString());
        }
    }

    /**
     * Callback received when new Entry is added.
     * Data is extracted and id received to update Entry
     */

    @Override
    public void onReceivedPostIdData(String s) {
        if (s != null) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                mSelectedEntryID = (Integer) jsonObject.get("id");
                Log.d(TAG, "onReceived: id " + mSelectedEntryID);
                AddFruitFragment addFruitFragment = new AddFruitFragment();
                addFruitFragment.updateFruitsData(mFruitsData, mEntriesData);
                if (mSelectedEntryID != 0) {
                    dataHandler.removeOnPostDataReceivedListener(this);
                    Log.d(TAG, "onReceived: starting addfruit fragment " + mSelectedEntryID);
                    addFruitFragment.onPassId(mSelectedEntryID);
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_fragment, addFruitFragment)
                            .addToBackStack(null)
                            .commit();
                }
            } catch (JSONException e) {
                Log.e(TAG, "onReceived: id is" + e.getMessage());
            }
        }
    }


    public void activateOnAddNewEntry() {
        onAddNewEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = (DatePickerDialog) DatePickerDialog.newInstance(
                        EntryListFragment.this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                String[] existingDateEntries = mEntriesData.getEntriesData().stream()
                        .map(x -> x.getDate())
                        .sorted()
                        .toArray(String[]::new);
                java.util.Date date = null;

                for (int i = 0; i < existingDateEntries.length; i++) {
                    try {
                        date = simpleDateFormat.parse(existingDateEntries[i]);
                    } catch (ParseException e) {
                        Log.e(TAG, "showDatePicker: Parse date exception" + e.getMessage());
                        e.printStackTrace();
                    }
                    calendar = dateToCalendar(date);
                    System.out.println(calendar.getTime());

                    List<Calendar> unavailableDates = new ArrayList<>();
                    unavailableDates.add(calendar);
                    Calendar[] disabledDays1 = unavailableDates.toArray(new Calendar[unavailableDates.size()]);
                    datePickerDialog.setDisabledDays(disabledDays1);
                }
            }

            private Calendar dateToCalendar(Date date) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                return calendar;
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String mSelectedDate = dateFormat.format(calendar.getTime());
        Log.d(TAG, "onDateSet: " + mSelectedDate);
        dataHandler.setOnPostDataReceivedListener(this);
        dataHandler.postNewEntry(mSelectedDate);
    }


    @Override
    public void onEntryClickListener(int position) {
        Log.d(TAG, "onClick: clicked position " + position);
        if (mEntries.size() != 0) {
            mPosition = position;
            DetailedEntryFragment detailedEntryFragment = new DetailedEntryFragment();
            if (isFruitDataReceived) {
                detailedEntryFragment.dataPassed(mEntriesData, mFruitsData, position);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_fragment, detailedEntryFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }
    }

    @Override
    public void onReceivedFruitsData(FruitsData fruitsData) {
        mFruitsData = fruitsData;
        Log.d(TAG, "onReceivedFruitsData: entryFragment" + mFruitsData.getFruitData().toString());
        isFruitDataReceived = true;
        dataHandler.removeOnPostDataReceivedListener(this);
    }

    @Override
    public void onEntryRemoved(int entryId) {
        dataHandler.onDeleteOneEntry(entryId);
    }

    @Override
    public void onLastEntryRemoved() {
        dataHandler.onDeleteAllEntries();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }
}








