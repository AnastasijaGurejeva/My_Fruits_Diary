package com.example.my_fruits_diary.Model;

import java.util.HashMap;

public interface OnDetailedEntryChangeListener {
    void onEntryAmountChanged(HashMap<Integer, Integer> fruitEntries, int fruitId, String fruitAmount);
}
