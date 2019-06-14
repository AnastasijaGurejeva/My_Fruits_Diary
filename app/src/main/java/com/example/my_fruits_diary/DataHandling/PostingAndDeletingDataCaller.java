package com.example.my_fruits_diary.DataHandling;

import android.os.AsyncTask;
import android.util.Log;

public class PostingAndDeletingDataCaller extends AsyncTask<String, Void, String> implements DeleteAndPostData.OnPostComplete {
    private final OnNewPostComplete mCallBack;
    private static final String TAG = "PostingAndDeletingDataCaller";
    private String mDate;
    private String mUrl;
    private PostStatus mPostStatus;
    private String mDataReceived;
    private String mPostRequest;


    public interface OnNewPostComplete {
        void onNewPostComplete(String data, PostStatus status);
    }

    public PostingAndDeletingDataCaller(OnNewPostComplete CallBack, String date, String url, String postRequest) {
        Log.d(TAG, "PostingAndDeletingDataCaller: called:" + postRequest + url + date);
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
        DeleteAndPostData deleteAndPostData = new DeleteAndPostData(this);
        deleteAndPostData.postData(mDate, mUrl, mPostRequest);
        return mDataReceived;
    }

    @Override
    public void onPostComplete(String data, PostStatus status) {
        Log.d(TAG, "onPostComplete: Status " + status);
        if (status == PostStatus.OK) {
            mDataReceived = data;
        }
        Log.d(TAG, "onDownloadComplete: ends");
    }
}


