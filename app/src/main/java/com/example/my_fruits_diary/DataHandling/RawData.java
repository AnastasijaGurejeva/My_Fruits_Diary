package com.example.my_fruits_diary.DataHandling;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Class to download raw data from the web on separate thread
 * author: Anastasija Gurejeva
 */
enum DownloadStatus {IDLE, PROCESSING, NOT_INITIALISED, FAILED_OR_EMPTY, OK}

class RawData extends AsyncTask<String, Void, String> {
    private static final String TAG = "RawDataForEntries";

    private DownloadStatus mDownloadStatus;
    private final OnDownloadComplete mCallBack;


    interface OnDownloadComplete {
        void onDownloadComplete(String data, DownloadStatus status);
    }

    public RawData(OnDownloadComplete mCallBack) {
        this.mCallBack = mCallBack;
        this.mDownloadStatus = DownloadStatus.IDLE;
    }

    void fetchRawData(String s) {
        Log.d(TAG, "fetchRawData: starts");
        onPostExecute(download(s));
        Log.d(TAG, "fetchRawData: ends");
    }

    @Override
    protected void onPostExecute(String s) {
        if (mCallBack != null) {
            mCallBack.onDownloadComplete(s, mDownloadStatus);
        }
    }

    private String download(String url) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        if (url == null) {
            mDownloadStatus = DownloadStatus.NOT_INITIALISED;
            return null;
        }

        try {
            mDownloadStatus = DownloadStatus.PROCESSING;
            URL link = new URL(url);
            connection = (HttpURLConnection) link.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int response = connection.getResponseCode();
            Log.d(TAG, "doInBackground: response code is: " + response);

            StringBuilder result = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                result.append(line).append("\n");
            }
            Log.d(TAG, "download: raw " + result.toString());
            mDownloadStatus = DownloadStatus.OK;
            return result.toString();

        } catch (MalformedURLException e) {
            Log.e(TAG, "download: Invalid url " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "download: IO exception " + e.getMessage());
        } catch (SecurityException e) {
            Log.e(TAG, "download: security needs permission" + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: Error closing stream " + e.getMessage());
                }
            }
        }
        mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: starts with " + strings[0]);
        return download(strings[0]);
    }


}
