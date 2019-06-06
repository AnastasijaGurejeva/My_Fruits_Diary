package com.example.my_fruits_diary;

import android.os.AsyncTask;
import android.util.Log;

import com.example.my_fruits_diary.MyDiary.Fruit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParseJSON extends AsyncTask<String, Void, List<Fruit>> implements GetRawData.OnDownloadComplete {
    public static final String TAG = "ParseJSON";

    private List<Fruit> mFruitsList = null;
    private final OnDataAvailable mCallBack;
    private boolean runningOnSameThread;
    private String mUrl;


    interface OnDataAvailable {
        void onDataAvailable (List<Fruit> data, DownloadStatus status);
    }

    public ParseJSON(OnDataAvailable CallBack, String url) {
        Log.d(TAG, "ParseJSON: called");
        mCallBack = CallBack;
        mUrl = url;
    }

    @Override
    protected void onPostExecute(List<Fruit> fruits) {
        Log.d(TAG, "onPostExecute: starts");
        if(mCallBack != null) {
            mCallBack.onDataAvailable(mFruitsList, DownloadStatus.OK);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected List<Fruit> doInBackground(String... params) {
        Log.d(TAG, "doInBackground: starts");

        GetRawData getRawData = new GetRawData(this);
        getRawData.fetchRawData(mUrl);
        return mFruitsList;
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete: Status " + status);
        if (status == DownloadStatus.OK) {
            mFruitsList = new ArrayList<>();

            try {
                JSONArray itemsArray = new JSONArray(data);

                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject jsonFruit = itemsArray.getJSONObject(i);
                    int id = jsonFruit.getInt("id");
                    String type = jsonFruit.getString("type");
                    int vitamins = jsonFruit.getInt("vitamins");
                    String imageUrl = jsonFruit.getString("image");


//                    String imageUrl = image.getString("m");

//                    Fruit fruitObject = new Fruit(id, type, vitamins, imageUrl);
//                    mFruitsList.add(fruitObject);

                    //Log.d(TAG, "onDownloadComplete: " + fruitObject.toString());

                }
            } catch (JSONException e) {
                Log.e(TAG, "onDownloadComplete: " + e.getMessage());
                e.printStackTrace();
                status = DownloadStatus.FAILED_OR_EMPTY;
            }
        }

        if(runningOnSameThread && mCallBack != null) {
            mCallBack.onDataAvailable(mFruitsList, status);
        }
        Log.d(TAG, "onDownloadComplete: ends");
    }
}
