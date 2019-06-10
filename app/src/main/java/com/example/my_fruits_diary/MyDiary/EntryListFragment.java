package com.example.my_fruits_diary.MyDiary;


import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_fruits_diary.DataHandling.DataHandler;
import com.example.my_fruits_diary.DataHandling.DownloadDataHandler;
import com.example.my_fruits_diary.DataHandling.EntriesData;
import com.example.my_fruits_diary.DataHandling.FruitsData;
import com.example.my_fruits_diary.MainActivity;
import com.example.my_fruits_diary.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Tab fragment for entering eaten fruits on selected day
 * Created 5/06/2019
 * Author: Anastasija Gurejeva
 */
public class EntryListFragment extends Fragment implements Observer,
        OnPostDataReceivedListener, OnFruitDataReceivedListener, RecyclerViewAdapter.OnEntryClickListener, DatePickerDialog.OnDateSetListener {
    private static final String TAG = "EntryListFragment";

    protected ArrayList<Integer> mEntryId = new ArrayList<>();
    private ArrayList<Integer> mFruitAmount = new ArrayList<>();
    private ArrayList<Integer> mTotalVitamins = new ArrayList<>();
    private ArrayList<String> mDate = new ArrayList<>();
    protected int id;
    private FloatingActionButton onAddNewEntry;
    private FruitsData mFruitsData;
    private EntriesData mEntriesData;
    private List<Entry> mEntries;
    private List<Fruit> mFruits;
    private HashMap<Integer, Integer> mFruitEntries;
    private RecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static final int REQUEST_CODE = 1;
    private AddFruitFragment addFruitFragment;
    private String mSelectedDate;
    private int mSelectedEntryID;
    private boolean isFruitDataReceived = false;
    private DataHandler dataHandler = new DataHandler();
    private DownloadDataHandler mDownloadDataHandler;
    private RecyclerView recyclerView;


    public EntryListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_entry_list, container, false);
        onAddNewEntry = view.findViewById(R.id.add_Entry);

        recyclerView = view.findViewById(R.id.recycler_view_detailed_fragment);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        Log.d(TAG, "onCreateView: setting layout manager");
        recyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerViewAdapter(mEntries, getActivity(), this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDelete(mAdapter, getContext()));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        activateOnAddNewEntry();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.options_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.mnuDeleteAllEntries:
                dataHandler.onDeleteAllEntries();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                break;
  //          case R.id.mnuRefreshData:
//                intent = new Intent(this, PlantInfoActivity.class);
//                startActivity(intent);
//                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    public void setData(EntriesData entriesData, DownloadDataHandler downloadDataHandler) {
        mEntriesData = entriesData;
        mDownloadDataHandler = downloadDataHandler;
        mDownloadDataHandler.setOnFruitDataReceivedListener(this);
        mEntriesData.addObserver(this);
        Log.d(TAG, "setData: data " + entriesData.toString());
        Log.d(TAG, "setData: observer added");
    }

    /**
     * Method receives callback when Entry List if fully loaded
     * And calls method to ipdate recyclerView
     *
     * @param o
     * @param data
     */

    @Override
    public void update(Observable o, Object data) {
        mAdapter.loadNewData((List<Entry>) data);
        Log.d(TAG, "update: dataEntrie loaded" + data.toString());

    }

    @Override
    public void onReceivedPostIdData(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            mSelectedEntryID = (Integer) jsonObject.get("id");
            Log.d(TAG, "onReceived: id " + mSelectedEntryID);
            addFruitFragment = new AddFruitFragment();
            addFruitFragment.updateFruitsData(mFruitsData, mEntriesData);
            if (mSelectedEntryID != 0) {
                dataHandler.removeOnPostDataReceivedListener(this);
                Log.d(TAG, "onReceived: starting addfruit fragment " + mSelectedEntryID);
                addFruitFragment.onPassId(mSelectedEntryID);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_fragment, addFruitFragment)
                        .commit();
            }
        } catch (JSONException e) {
            Log.e(TAG, "onReceived: id is" + e.getMessage());

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

        mSelectedDate = dateFormat.format(calendar.getTime());

        Log.d(TAG, "onDateSet: " + mSelectedDate);
        dataHandler.setOnPostDataReceivedListener(this);
        dataHandler.postNewEntry(mSelectedDate);
    }



    @Override
    public void onEntryClickListener(int position) {
        Log.d(TAG, "onClick: clicked position " + position);
        onAddNewEntry.hide();
        DetailedEntryFragment detailedEntryFragment = new DetailedEntryFragment();
        if (isFruitDataReceived) {
            String date = mEntriesData.getEntriesData().get(position).getDate();
            detailedEntryFragment.dataPassed(mEntriesData, mFruitsData, position);
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_fragment, detailedEntryFragment)
                    .commit();
        }
    }

    @Override
    public void onReceivedFruitsData(FruitsData fruitsData) {
        mFruitsData = fruitsData;
        mFruits = mFruitsData.getFruitData();
        Log.d(TAG, "onReceivedFruitsData: entryFragment" + mFruits.toString());
        isFruitDataReceived = true;
        dataHandler.removeOnPostDataReceivedListener(this);
    }

}








