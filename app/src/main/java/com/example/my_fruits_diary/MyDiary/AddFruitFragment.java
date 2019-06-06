package com.example.my_fruits_diary.MyDiary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.my_fruits_diary.R;

import java.util.List;

public class AddFruitFragment extends Fragment implements RecyclerViewAdapterForAvialableFruits.OnEntryListener{

    private List<Fruit> mFruitList;
    public static final String TAG ="AddFruitFragment";
    //protected int id;

    public AddFruitFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_fruit, container, false);


        RecyclerView recyclerView = view.findViewById(R.id.frame_availavleFruits);
        RecyclerViewAdapterForAvialableFruits adapter =
                new RecyclerViewAdapterForAvialableFruits(mFruitList, getActivity(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        setDataTest();



        return view;
    }

    public void updateData(List<Fruit> fruits) {
        mFruitList = fruits;
    }


    @Override
    public void onEntryClick(int position) {
        Log.d(TAG, "onEntryClick: clicked : " + position);
       //add to th entry

    }

    public void setDataTest() {



    }


}
