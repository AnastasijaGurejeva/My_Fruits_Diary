package com.example.my_fruits_diary.DataHandling;

import android.os.AsyncTask;
import android.util.Log;

import com.example.my_fruits_diary.Model.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EntriesJSONParser extends AsyncTask<String, Void, List<Entry>> implements RawData.OnDownloadComplete {
    public static final String TAG = "FruitJSONParser";

    private List<Entry> mEntries = null;
    private final OnEntryDataAvailable mCallBack;
    private String mUrl;


    interface OnEntryDataAvailable {
        void onEntryDataAvailable(List<Entry> data, DownloadStatus status);
    }

    public EntriesJSONParser(OnEntryDataAvailable CallBack, String url) {
        Log.d(TAG, "FruitJSONParser: called");
        mCallBack = CallBack;
        mUrl = url;
    }

    @Override
    protected void onPostExecute(List<Entry> entries) {
        Log.d(TAG, "onPostExecute: starts");
        if(mCallBack != null) {
            mCallBack.onEntryDataAvailable(mEntries, DownloadStatus.OK);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected List<Entry> doInBackground(String... params) {
        Log.d(TAG, "doInBackground: starts");

        RawData rawData = new RawData(this);
        rawData.fetchRawData(mUrl);
        return mEntries;
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete: Status " + status);
        if (status == DownloadStatus.OK) {
            mEntries = new ArrayList<>();

            try {
                JSONArray itemsArray = new JSONArray(data);

                for (int i = 0; i < itemsArray.length(); i++) {
                    HashMap<Integer, Integer> mEatenFruits = new HashMap<>();

                    JSONObject jsonEntry = itemsArray.getJSONObject(i);
                    int id = jsonEntry.getInt("id");
                    String date = jsonEntry.getString("date");
                    JSONArray fruits = jsonEntry.getJSONArray("fruit");
                        for (int k = 0; k < fruits.length(); k++) {
                            Integer fruitID = fruits.getJSONObject(k).getInt("fruitId");
                            Integer fruitAmount = fruits.getJSONObject(k).getInt("amount");
                            mEatenFruits.put(fruitID, fruitAmount);
                        }


                    Entry entryObject = new Entry(id, date, mEatenFruits);
                    mEntries.add(entryObject);


                    Log.d(TAG, "onDownloadComplete: " + entryObject.toString());
                }
            } catch (JSONException e) {
                Log.e(TAG, "onDownloadComplete: " + e.getMessage());
                e.printStackTrace();
                status = DownloadStatus.FAILED_OR_EMPTY;
            }
        }

        Log.d(TAG, "onDownloadComplete: ends");
    }
}
