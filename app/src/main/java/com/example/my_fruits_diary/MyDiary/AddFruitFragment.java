package com.example.my_fruits_diary.MyDiary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.my_fruits_diary.DataHandling.FruitsData;
import com.example.my_fruits_diary.R;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class AddFruitFragment extends Fragment implements RecyclerViewAdapterForAvialableFruits.OnEntryListener, Observer {

    private List<Fruit> mFruitList;
    private static final String TAG ="AddFruitFragment";
    private RecyclerViewAdapterForAvialableFruits mAdapterForFruits;
    private FruitsData mFruitsData;


    public AddFruitFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_fruit, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.frame_availavleFruits);

        mAdapterForFruits = new RecyclerViewAdapterForAvialableFruits(mFruitList, getActivity(), this);
        recyclerView.setAdapter(mAdapterForFruits);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    public void updateFruitsData(FruitsData fruitsData) {
            mFruitsData = fruitsData;
            mFruitsData.addObserver(this);
            mFruitList = mFruitsData.getFruitData();
            Log.d(TAG, "setData: data " + fruitsData.toString() );
            Log.d(TAG, "setData: observer added for Fruit list");
        }

    @Override
    public void update(Observable o, Object data) {
        mAdapterForFruits.loadNewData((List<Fruit>) data);
        Log.d(TAG, "UPDATED FROM OBSERVER FRUITS " + data.toString());
    }

    @Override
    public void onEntryClick(int position) {
        Log.d(TAG, "onEntryClick: clicked : " + position);
       //add to th entry

    }



}
