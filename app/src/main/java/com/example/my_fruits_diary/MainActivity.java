package com.example.my_fruits_diary;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.my_fruits_diary.About.AboutFragment;
import com.example.my_fruits_diary.MyDiary.EntryListFragment;

public class MainActivity extends AppCompatActivity {

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

    }

    private void setViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new EntryListFragment(),"My Diary");
        adapter.addFragment(new AboutFragment(), "About");
        viewPager.setAdapter(adapter);

    }
}
