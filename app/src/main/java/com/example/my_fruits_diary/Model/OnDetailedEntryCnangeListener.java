package com.example.my_fruits_diary.Model;

import java.util.HashMap;

public interface OnDetailedEntryCnangeListener {
    void onEntryFruitRemoved(HashMap<Integer, Integer> fruitEntries, int fruitId, String fruitAmount);
    void onEntryAmountChanged(HashMap<Integer, Integer> fruitEntries, int fruitId, String fruitAmount);
    void onEntryRemoved();
}
