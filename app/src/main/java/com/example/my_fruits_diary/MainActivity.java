package com.example.my_fruits_diary;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.my_fruits_diary.About.AboutFragment;
import com.example.my_fruits_diary.DataHandling.DownloadDataHandler;
import com.example.my_fruits_diary.DataHandling.EntriesData;
import com.example.my_fruits_diary.DataHandling.FruitsData;
import com.example.my_fruits_diary.MyDiary.EntryListFragment;
import com.example.my_fruits_diary.MyDiary.Fruit;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    private DownloadDataHandler mDownloadDataHandler = new DownloadDataHandler();
    private FruitsData mFruitsData;
    private EntriesData mEntriesData;
    private List<Fruit> mFruits;
    EntryListFragment entryListFragment;


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
    }


    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: starts");
        super.onResume();
        mDownloadDataHandler.downloadDataForAvailableEntries();
        mDownloadDataHandler.downloadDataForAvailableFruits();
    }


    private void setViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        entryListFragment = new EntryListFragment();
        entryListFragment.setData(mDownloadDataHandler.getEntriesData(), mDownloadDataHandler);
        adapter.addFragment(entryListFragment, "My Diary");
        adapter.addFragment(new AboutFragment(), "About");
        viewPager.setAdapter(adapter);
    }

}
