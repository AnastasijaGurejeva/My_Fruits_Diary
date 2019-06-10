package com.example.my_fruits_diary.DataHandling;

import android.os.AsyncTask;
import android.util.Log;

public class PostDeleteDataCaller extends AsyncTask<String, Void, String> implements PostDeleteData.OnPostComplete {
    private final OnNewPostComplete mCallBack;
    private static final String TAG = "PostDeleteDataCaller";
    private String mDate;
    private String mUrl;
    private String responseData;
    private PostStatus mPostStatus;
    private String mData;
    private String mPostRequest;


    public interface OnNewPostComplete {
        void onNewPostComplete(String data, PostStatus status);
    }

    public PostDeleteDataCaller(OnNewPostComplete CallBack, String date, String url, String postRequest) {
        Log.d(TAG, "PostDeleteDataCaller: called:" + postRequest + url+ date);
        mCallBack = CallBack;
        mDate = date;
        mUrl = url;
        mPostRequest = postRequest;
    }

    @Override
    protected void onPostExecute(String s) {
        if (mCallBack != null) {
            Log.d(TAG, "onPostExecute: callback " + mPostRequest + s);
            mCallBack.onNewPostComplete(s, mPostStatus);
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: starts");
        PostDeleteData postDeleteData = new PostDeleteData(this);
        postDeleteData.postData(mDate, mUrl, mPostRequest);
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


