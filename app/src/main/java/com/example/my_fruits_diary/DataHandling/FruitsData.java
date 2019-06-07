package com.example.my_fruits_diary.DataHandling;

import com.example.my_fruits_diary.MyDiary.Fruit;

import java.util.List;
import java.util.Observable;

public class FruitsData extends Observable {

    private List<Fruit> mFruitsData;

    public FruitsData () {
    }

    public void setData(List<Fruit> fruitData) {
        setChanged();
        mFruitsData = fruitData;
        notifyObservers(fruitData);
    }

    public List<Fruit> getFruitData() {
        return mFruitsData;
    }

}
