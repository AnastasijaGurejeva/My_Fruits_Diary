package com.example.my_fruits_diary;

import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.my_fruits_diary.About.AboutFragment;
import com.example.my_fruits_diary.MyDiary.EntryListFragment;
import com.example.my_fruits_diary.MyDiary.Fruit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.security.auth.login.LoginException;

public class MainActivity extends AppCompatActivity implements ParseJSON.OnDataAvailable {

    public static final String TAG ="MainActivity";
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Starting app");

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        setViewPager(mViewPager);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mViewPager);

        Log.d(TAG, "onCreate: staring AsyncTask");
//        GetRawData getRawData = new GetRawData(this);
//        getRawData.execute("https://fruitdiary.test.themobilelife.com/api/fruit");

    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: starts");
        super.onResume();
        String baseUrl = "https://fruitdiary.test.themobilelife.com";
        String fruitAPI ="/api/fruit";
        ParseJSON getJsondata = new ParseJSON(this, baseUrl + fruitAPI);
        getJsondata.execute();

    }

    private void setViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new EntryListFragment(),"My Diary");
        adapter.addFragment(new AboutFragment(), "About");
        viewPager.setAdapter(adapter);

    }

    @Override
    public void onDataAvailable(List<Fruit> data, DownloadStatus status) {
        if(status == DownloadStatus.OK) {
            Log.d(TAG, "onDownloadComplete: data is " + data);
        } else {
            Log.e(TAG, "onDownloadComplete failed with status " + status);
        }
    }
}
