package com.example.my_fruits_diary.DataHandling;

import android.util.Log;

import com.example.my_fruits_diary.MyDiary.OnPostDataReceivedListener;

public class PostCaller implements PostHandler.OnNewPostComplete {

    private String baseUrl = "https://fruitdiary.test.themobilelife.com/api/";
    private String mUrl = "https://fruitdiary.test.themobilelife.com/api/entries";
    private static final String TAG = "PostCaller";
    private String mDataReceived;
    private OnPostDataReceivedListener onPostDataReceivedListener;
    private boolean isEditEntryCalled = false;



    public PostCaller() {
    }

    public void buildEditUrl(int entryId, int fruitId, String fruitAmount) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(baseUrl);
        stringBuilder.append("entry/");
        stringBuilder.append(entryId);
        stringBuilder.append("/fruit/");
        stringBuilder.append(fruitId);
        stringBuilder.append("?amount=");
        stringBuilder.append(fruitAmount);
        mUrl = stringBuilder.toString();
    }

    public void postNewEntry(String date) {
        PostHandler postHandler = new PostHandler(this, date, mUrl);
        postHandler.execute();
    }

    public void editEntry(int entryId, int fruitId, String fruitAmount) {
        buildEditUrl(entryId, fruitId, fruitAmount);
        isEditEntryCalled = true;
        postNewEntry("");
    }

    public void onDeleteEntries() {

    }

    public void onDeleteOneEntry(String entryId) {

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
        if(!isEditEntryCalled) {
            onPostDataReceivedListener.onReceived(mDataReceived);
        }
        Log.d(TAG, "onNewPostComplete: DONE data " + data);
    }
}
