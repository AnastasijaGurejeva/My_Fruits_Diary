package com.example.my_fruits_diary.Model;

import java.util.List;
import java.util.Observable;

public class EntriesData extends Observable {

    private List<Entry> mEntriesData;

    public EntriesData() {
    }

    public void setData(List<Entry> fruitData) {
        setChanged();
        mEntriesData = fruitData;
        notifyObservers(fruitData);
    }

    public List<Entry> getEntriesData() {
        return mEntriesData;
    }

}
