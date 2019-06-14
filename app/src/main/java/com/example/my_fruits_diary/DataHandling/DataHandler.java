package com.example.my_fruits_diary.DataHandling;

import android.util.Log;

import com.example.my_fruits_diary.Model.OnPostDataReceivedListener;

public class DataHandler implements PostingAndDeletingDataCaller.OnNewPostComplete {

    private String mBaseUrl = "https://fruitdiary.test.themobilelife.com/api/";
    private static final String TAG = "DataHandler";
    private String mUrl;
    private String mDataReceived;
    private OnPostDataReceivedListener onPostDataReceivedListener;
    private boolean isNewEntryCalled = false;
    private String mPostRequest;
    private PostingAndDeletingDataCaller postingAndDeletingDataCaller;



    public DataHandler() {
    }

    public void buildEditUrl(int entryId, int fruitId, String fruitAmount) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(mBaseUrl);
        stringBuilder.append("entry/");
        stringBuilder.append(entryId);
        stringBuilder.append("/fruit/");
        stringBuilder.append(fruitId);
        stringBuilder.append("?amount=");
        stringBuilder.append(fruitAmount);
        mUrl = stringBuilder.toString();
    }

    public void post(String date) {
        postingAndDeletingDataCaller = new PostingAndDeletingDataCaller(this, date, mUrl, mPostRequest);
        postingAndDeletingDataCaller.execute();
    }
    public void postNewEntry(String date) {
        mPostRequest = "POST";
        mUrl = mBaseUrl + "entries";
        isNewEntryCalled = true;
        post(date);
    }

    public void editEntry(int entryId, int fruitId, String fruitAmount) {
        mPostRequest = "POST";
        buildEditUrl(entryId, fruitId, fruitAmount);
        post("");
    }

    public void onDeleteAllEntries() {
        mPostRequest = "DELETE";
        mUrl = mBaseUrl + "entries";
        post("");

    }

    public void onDeleteOneEntry(int entryId) {
        mPostRequest = "DELETE";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(mBaseUrl);
        stringBuilder.append("entry/");
        stringBuilder.append(entryId);
        mUrl = stringBuilder.toString();
        post("");
    }

    public void setOnPostDataReceivedListener(OnPostDataReceivedListener postDataReceivedListener) {
        this.onPostDataReceivedListener = postDataReceivedListener;
    }

    public void removeOnPostDataReceivedListener(OnPostDataReceivedListener postDataReceivedListener) {
        this.onPostDataReceivedListener = null;
    }


    @Override
    public void onNewPostComplete(String data, PostStatus status) {
        mDataReceived = data;
        if(isNewEntryCalled ) {
            onPostDataReceivedListener.onReceivedPostIdData(mDataReceived);
        }
        Log.d(TAG, "onNewPostComplete: DONE data " + data);
    }
}
