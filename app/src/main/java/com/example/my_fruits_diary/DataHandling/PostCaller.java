package com.example.my_fruits_diary.DataHandling;

import android.util.Log;

public class PostCaller implements PostHandler.OnNewPostComplete {

    private String baseUrl = "https://fruitdiary.test.themobilelife.com/api/";
    private String mUrl = "https://fruitdiary.test.themobilelife.com/api/entries";
    private static final String TAG = "PostCaller";

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
        postNewEntry("");
    }

    public void onDeleteEntries() {

    }

    public void onDeleteOneEntry(String entryId) {

    }

    @Override
    public void onNewPostComplete(PostStatus status) {
            Log.d(TAG, "onNewPostComplete: DONE");

    }
}
