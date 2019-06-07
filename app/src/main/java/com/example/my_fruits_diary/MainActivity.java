package com.example.my_fruits_diary;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.my_fruits_diary.About.AboutFragment;
import com.example.my_fruits_diary.DataHandling.DataHandler;
import com.example.my_fruits_diary.DataHandling.EntriesData;
import com.example.my_fruits_diary.DataHandling.FruitsData;
import com.example.my_fruits_diary.MyDiary.EntryListFragment;

public class MainActivity extends AppCompatActivity  {

    public static final String TAG ="MainActivity";
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    private DataHandler mDataHandler = new DataHandler();
    private FruitsData mFruitsData;
    private EntriesData mEntriesData;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Starting app");

//        mFruitsData = new FruitsData();
//        mFruitsData.addObserver(this);
//        mFruitsData.getFruitData();

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

            mViewPager = findViewById(R.id.container);
            setViewPager(mViewPager);


            TabLayout tabLayout = findViewById(R.id.tabLayout);
            tabLayout.setupWithViewPager(mViewPager);

    }

//    @Override
//    public void update(Observable o, Object data) {
//
//        Log.d(TAG, "UPDATED FROM OBSERVER");
//
//    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: starts");
        super.onResume();
        mDataHandler.downloadDataForAvailableEntries();
        mDataHandler.downloadDataForAvailableFruits();
    }



    private void setViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        EntryListFragment entryListFragment = new EntryListFragment();
        entryListFragment.setData(mDataHandler.getEntriesData());
        entryListFragment.setFruitsData(mDataHandler.getFruitsData());
        adapter.addFragment(entryListFragment,"My Diary");
        adapter.addFragment(new AboutFragment(), "About");
        viewPager.setAdapter(adapter);

    }


}
