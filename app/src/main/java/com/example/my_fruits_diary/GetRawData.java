package com.example.my_fruits_diary;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Class to download JSON data from the web on separate thread
 * author: Anastasij Gurejeva
 */

class GetRawData extends AsyncTask<String, Void, String> {
    private static final String TAG = "GetRawData";

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: parameter s is: " + s);

    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: starts with " + strings[0]);
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(strings[0]);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int response = connection.getResponseCode();
            Log.d(TAG, "doInBackground: response code is: " + response);

            StringBuilder result = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                result.append(line).append("\n");
                return result.toString();
            }
            reader.close();


        } catch (MalformedURLException e) {
            Log.e(TAG, "downloadXML: Invalid url " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "downloadXML: IO exception " + e.getMessage());
        } catch (SecurityException e) {
            Log.e(TAG, "downloadXML: security needs permission" + e.getMessage());
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
        return null;
    }

}
