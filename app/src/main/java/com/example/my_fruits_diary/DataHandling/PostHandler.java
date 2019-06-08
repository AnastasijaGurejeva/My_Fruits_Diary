package com.example.my_fruits_diary.DataHandling;

import android.os.AsyncTask;
import android.util.Log;

public class PostHandler extends AsyncTask<String, Void, String> implements PostJSONData.OnPostComplete {

        private final OnNewPostComplete mCallBack;
        private static final String TAG = "PostHandler";
        private String mDate;
        private String mUrl;


        public interface OnNewPostComplete {
            void onNewPostComplete(PostStatus status);
        }

        public PostHandler(OnNewPostComplete CallBack, String date, String url) {
            Log.d(TAG, "PostHandler: called");
            mCallBack = CallBack;
            mDate = date;
            mUrl = url;
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: starts");
            PostJSONData postJSONData = new PostJSONData(this);
            postJSONData.postData(mDate, mUrl);
            return null;
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


