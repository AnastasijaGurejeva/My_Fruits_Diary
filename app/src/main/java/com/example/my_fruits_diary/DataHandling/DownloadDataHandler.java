package com.example.my_fruits_diary.DataHandling;

import android.util.Log;

import com.example.my_fruits_diary.MyDiary.Entry;
import com.example.my_fruits_diary.MyDiary.Fruit;
import com.example.my_fruits_diary.MyDiary.OnFruitDataReceivedListener;

import java.util.List;

public class DownloadDataHandler implements FruitJSONParser.OnDataAvailable, EntriesJSONParser.OnEntryDataAvailable {
    public static final String TAG = "DownloadDataHandler";
    private FruitsData mFruitsData = new FruitsData();
    private EntriesData mEntriesData = new EntriesData();
    private OnFruitDataReceivedListener onFruitDataReceivedListener;


    private static String availableFruitsUrl = "https://fruitdiary.test.themobilelife.com/api/fruit";
    private static String availableEntriesUrl = "https://fruitdiary.test.themobilelife.com/api/entries";

    public DownloadDataHandler() {
    }

    public void downloadDataForAvailableFruits() {
        FruitJSONParser getJsonData = new FruitJSONParser(this, availableFruitsUrl);
        getJsonData.execute();
    }

    public void downloadDataForAvailableEntries() {
        EntriesJSONParser getJsonData = new EntriesJSONParser(this, availableEntriesUrl);
        getJsonData.execute();
    }

    public void setOnFruitDataReceivedListener(OnFruitDataReceivedListener fruitDataReceivedListener) {
        this.onFruitDataReceivedListener = fruitDataReceivedListener;
    }

    public void removeOnFruitDataReceivedListener(OnFruitDataReceivedListener fruitDataReceivedListener) {
        this.onFruitDataReceivedListener = null;
    }

    @Override
    public void onDataAvailable(List<Fruit> data, DownloadStatus status) {
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "onDownloadComplete: data is " + data);
            mFruitsData.setData(data);
            onFruitDataReceivedListener.onReceivedFruitsData(mFruitsData);
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
