package com.example.my_fruits_diary.DataHandling;

import android.os.AsyncTask;
import android.util.Log;

import com.example.my_fruits_diary.MyDiary.Fruit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FruitJSONParser extends AsyncTask<String, Void, List<Fruit>> implements RawData.OnDownloadComplete {
    public static final String TAG = "FruitJSONParser";

    private List<Fruit> mFruitsList = null;
    private final OnDataAvailable mCallBack;
    private String mUrl;
    private String baseUrl;


    interface OnDataAvailable {
        void onDataAvailable (List<Fruit> data, DownloadStatus status);
    }

    public FruitJSONParser(OnDataAvailable CallBack, String url) {
        Log.d(TAG, "FruitJSONParser: called");
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

        RawData rawData = new RawData(this);
        rawData.fetchRawData(mUrl);
        return mFruitsList;
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete: Status " + status);
        if (status == DownloadStatus.OK) {
            mFruitsList = new ArrayList<>();

            try {
                JSONArray itemsArray = new JSONArray(data);
                baseUrl = "https://fruitdiary.test.themobilelife.com/";

                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject jsonFruit = itemsArray.getJSONObject(i);
                    int id = jsonFruit.getInt("id");
                    String type = jsonFruit.getString("type");
                    int vitamins = jsonFruit.getInt("vitamins");
                    String image = baseUrl.concat(jsonFruit.getString("image"));

                    Fruit fruitObject = new Fruit(id, type, vitamins, image);
                    mFruitsList.add(fruitObject);

                    Log.d(TAG, "onDownloadComplete: " + fruitObject.toString());
                }
            } catch (JSONException e) {
                Log.e(TAG, "onDownloadComplete: " + e.getMessage());
                e.printStackTrace();
                status = DownloadStatus.FAILED_OR_EMPTY;
            }
        }

        if(mCallBack != null) {
            mCallBack.onDataAvailable(mFruitsList, status);
        }
        Log.d(TAG, "onDownloadComplete: ends");
    }
}
