package com.example.my_fruits_diary.MyDiary;

import java.util.HashMap;

public class Entry {
    private int mEntryId;

    private String mDate;
    private HashMap<Integer, Integer> mEatenFruits;

    public Entry(String date) {
        this.mDate = date;
        this.mEatenFruits = new HashMap<>();
        this.mEntryId = 0;
    }

    public Entry(int mEntryId, String mDate, HashMap<Integer, Integer> mEatenFruits) {
        this.mEntryId = mEntryId;
        this.mDate = mDate;
        this.mEatenFruits = mEatenFruits;
    }

    public int getEntryId() {
        return mEntryId;
    }

    public void setEntryId(int mEntryId) {
        this.mEntryId = mEntryId;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public HashMap<Integer, Integer> getmEatenFruits() {
        return mEatenFruits;
    }

    public void setmEatenFruits(HashMap<Integer, Integer> mEatenFruits) {
        this.mEatenFruits = mEatenFruits;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "mEntryId'" + mEntryId + '\'' +
                ", mDate'" + mDate + '\'' +
                ", mEatenFruits'" + mEatenFruits + '\'' +
                '}';
    }
}
