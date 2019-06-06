package com.example.my_fruits_diary.DataHandling;

import android.util.Log;

import com.example.my_fruits_diary.MyDiary.Entry;
import com.example.my_fruits_diary.MyDiary.Fruit;

import java.util.List;

public class DataHandler implements FruitJSONParser.OnDataAvailable, EntriesJSONParser.OnEntryDataAvailable {
    public static final String TAG = "DataHandler";

    private List<Fruit> mFruits;
    private List<Entry> mEntries;
    private boolean flag = false;

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

    public Boolean getFruitDownloadFlag() {
        return flag;
    }

    public Boolean getEntriesDownloadFlag() {
        return flag;
    }

    @Override
    public void onDataAvailable(List<Fruit> data, DownloadStatus status) {
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "onDownloadComplete: data is " + data);
            mFruits = data;
            flag = true;

        } else {
            Log.e(TAG, "onDownloadComplete failed with status " + status);
            flag = false;
        }
    }

    public List<Entry> passEntryData() {
        return mEntries;
    }

    public List<Fruit> passFruitData() {
        return mFruits;
    }


    @Override
    public void onEntryDataAvailable(List<Entry> data, DownloadStatus status) {
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "onDownloadComplete: data is " + data);
            mEntries = data;
            flag = true;
        } else {
            Log.e(TAG, "onDownloadComplete failed with status " + status);
            flag = false;
        }
    }

}
