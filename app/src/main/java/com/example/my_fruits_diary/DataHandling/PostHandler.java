package com.example.my_fruits_diary.DataHandling;

import android.util.Log;

public class PostHandler implements PostJSONParser.OnNewPostComplete {

    private static String newEntryPostUrl = "https://fruitdiary.test.themobilelife.com/api/entries";
    private static final String TAG = "PostHandler";
    private String mDate;

    public PostHandler(String date) {
        mDate =date;
    }

    public void postNewEntry(String date) {
       PostJSONParser postJSONParser = new PostJSONParser(this, mDate);
       postJSONParser.execute();
    }

    @Override
    public void onNewPostComplete(PostStatus status) {
        if (status == PostStatus.OK) {
            Log.d(TAG, "onNewPostComplete: DONE");

        } else {
            Log.e(TAG, "onDownloadComplete failed with status " + status);
        }
    }
}
