package com.example.my_fruits_diary.DataHandling;

import android.os.AsyncTask;
import android.util.Log;

public class PostJSONParser extends AsyncTask<String, Void, String> implements PostJSONData.OnPostComplete {

        private final OnNewPostComplete mCallBack;
        private static final String TAG = "PostJSONParser";
        private String mDate;


        public interface OnNewPostComplete {
            void onNewPostComplete(PostStatus status);
        }

        public PostJSONParser(OnNewPostComplete CallBack, String date) {
            Log.d(TAG, "PostJSONParser: called");
            mCallBack = CallBack;
            mDate = date;
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG, "doInBackground: starts");
            PostJSONData postJSONData = new PostJSONData(this);
            postJSONData.postData(mDate);
            return mCallBack.toString();
        }

        @Override
        public void onPostComplete(String data, PostStatus status) {
            Log.d(TAG, "onPostComplete: Status " + status);
            if (status == PostStatus.OK) {
                Log.d(TAG, "onPostComplete: post completed");
            }
            Log.d(TAG, "onDownloadComplete: ends");
        }
    }


