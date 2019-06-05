package com.example.my_fruits_diary.About;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.my_fruits_diary.R;

/**
 * Tab fragment describing the app and it's usage.
 * Created 5/06/2019
 * Author: Anastasija Gurejeva
 */
public class AboutFragment extends Fragment {
    public static final String TAG ="TabAboutFragment";


//    public AboutFragment() {
//        // Required empty public constructor
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        TextView title = view.findViewById(R.id.titleAbout);
        TextView textAbout = view.findViewById(R.id.textAbout);
        return view;
    }

}
