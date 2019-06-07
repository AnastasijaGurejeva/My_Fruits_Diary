package com.example.my_fruits_diary.DataHandling;

import android.util.Log;

import com.example.my_fruits_diary.MyDiary.Entry;
import com.example.my_fruits_diary.MyDiary.Fruit;

import java.util.List;

public class DataHandler implements FruitJSONParser.OnDataAvailable, EntriesJSONParser.OnEntryDataAvailable {
    public static final String TAG = "DataHandler";
    private FruitsData mFruitsData = new FruitsData();
    private EntriesData mEntriesData = new EntriesData();


    private static String availableFruitsUrl = "https://fruitdiary.test.themobilelife.com/api/fruit";
    private static String availableEntriesUrl = "https://fruitdiary.test.themobilelife.com/api/entries";

    public DataHandler() {
    }

    public void downloadDataForAvailableFruits() {
        FruitJSONParser getJsonData = new FruitJSONParser(this, availableFruitsUrl);
        getJsonData.execute();
    }

    public void downloadDataForAvailableEntries() {
        EntriesJSONParser getJsonData = new EntriesJSONParser(this, availableEntriesUrl);
        getJsonData.execute();
    }

    @Override
    public void onDataAvailable(List<Fruit> data, DownloadStatus status) {
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "onDownloadComplete: data is " + data);
            mFruitsData.setData(data);
        } else {
            Log.e(TAG, "onDownloadComplete failed with status " + status);
        }
    }

    /**
     * Method passes Downloaded Entries Data to Main activity
     */

    public EntriesData getEntriesData() {
        return mEntriesData;
    }

    /**
     * Method passes Downloaded Fruit Data to Main activity
     */

    public FruitsData getFruitsData() {
        return mFruitsData;
    }


    @Override
    public void onEntryDataAvailable(List<Entry> data, DownloadStatus status) {
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "onDownloadComplete: data is " + data);
            mEntriesData.setData(data);
        } else {
            Log.e(TAG, "onDownloadComplete failed with status " + status);
        }
    }
}
