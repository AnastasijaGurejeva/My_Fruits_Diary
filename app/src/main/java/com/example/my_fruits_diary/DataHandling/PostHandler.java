package com.example.my_fruits_diary.DataHandling;

import android.os.AsyncTask;
import android.util.Log;

public class PostHandler extends AsyncTask<String, Void, String> implements PostJSONData.OnPostComplete {
    private final OnNewPostComplete mCallBack;
    private static final String TAG = "PostHandler";
    private String mDate;
    private String mUrl;
    private String responseData;
    private PostStatus mPostStatus;
    private String mData;


    public interface OnNewPostComplete {
        void onNewPostComplete(String data, PostStatus status);
    }

    public PostHandler(OnNewPostComplete CallBack, String date, String url) {
        Log.d(TAG, "PostHandler: called");
        mCallBack = CallBack;
        mDate = date;
        mUrl = url;
    }

    @Override
    protected void onPostExecute(String s) {
        if (mCallBack != null) {
            mCallBack.onNewPostComplete(s, mPostStatus);
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: starts");
        PostJSONData postJSONData = new PostJSONData(this);
        postJSONData.postData(mDate, mUrl);
        return mData;
    }

    @Override
    public void onPostComplete(String data, PostStatus status) {
        Log.d(TAG, "onPostComplete: Status " + status);
        if (status == PostStatus.OK) {
           mData = data;
        }
        Log.d(TAG, "onDownloadComplete: ends");
    }
}


