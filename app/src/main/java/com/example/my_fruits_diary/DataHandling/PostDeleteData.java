package com.example.my_fruits_diary.DataHandling;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

enum PostStatus { IDLE, PROCESSING, NOT_INITIALISED, FAILED, OK }

public class PostDeleteData extends AsyncTask<String, Void, String> {

    private static final String TAG = "PostDeleteData";
    private PostStatus mPostStatus;
    private final OnPostComplete mCallBack;
    private String mPostRequest;


    interface OnPostComplete {
        void onPostComplete(String data, PostStatus status);
    }

    public PostDeleteData(OnPostComplete mCallBack) {
        this.mCallBack = mCallBack;
        this.mPostStatus = PostStatus.IDLE;
    }

    void postData(String date, String url, String postRequest) {
        Log.d(TAG, "posting: starts " + date + "" + url);
        onPostExecute(postJSONData(date, url, postRequest));
        Log.d(TAG, "posting: ends ");
    }

    @Override
    protected void onPostExecute(String s) {
        if(mCallBack != null) {
            mCallBack.onPostComplete(s, mPostStatus);
        }
    }

    public String postJSONData(String date, String url, String requestType) {
        HttpURLConnection connection = null;
        BufferedReader reader;
        InputStream inputStream = null;
        mPostRequest = requestType;

        if(url == null) {
          mPostStatus = PostStatus.NOT_INITIALISED;
            return null;
        }
        try {
            mPostStatus = PostStatus.PROCESSING;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("date", date);

            URL link = new URL(url);
            connection = (HttpURLConnection) link.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod(mPostRequest);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(jsonObject.toString());
            wr.flush();
            wr.close();

            inputStream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line ;

            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                Log.e(TAG, "doInBackground: IOException" + e.getMessage() );
                e.printStackTrace();
            }
            mPostStatus = PostStatus.OK;
            return sb.toString();

        } catch (MalformedURLException e) {
            Log.e(TAG, "posting: Invalid url " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "posting: IO exception " + e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: Error closing stream " + e.getMessage());
                }
            }
        }
        mPostStatus = PostStatus.FAILED;
        return null;
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: starts with " + strings[0] + strings[1]);
        return postJSONData(strings[0], strings[1], strings[3]);
    }
}

