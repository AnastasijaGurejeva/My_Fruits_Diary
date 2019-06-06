package com.example.my_fruits_diary;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.my_fruits_diary.About.AboutFragment;
import com.example.my_fruits_diary.DataHandling.DataHandler;
import com.example.my_fruits_diary.MyDiary.EntryListFragment;

public class MainActivity extends AppCompatActivity {

    public static final String TAG ="MainActivity";
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    private DataHandler dataHandler = new DataHandler();



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
        dataHandler.downloadDataForAvailableEntries();
        dataHandler.downloadDataForAvailableFruits();
    }

    private void setViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        EntryListFragment entryListFragment = new EntryListFragment();
        entryListFragment.updateEntryData(dataHandler.passEntryData());
        entryListFragment.updateData(dataHandler.passFruitData());
        adapter.addFragment(entryListFragment,"My Diary");
        adapter.addFragment(new AboutFragment(), "About");
        viewPager.setAdapter(adapter);

    }


}
