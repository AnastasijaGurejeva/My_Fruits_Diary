package com.example.my_fruits_diary.MyDiary;


public class Fruit {
    private int mID;
    private String mType;
    private int mVitamins;
    private String mImage;

    public Fruit(int id, String type, int vitamins, String image) {
        mImage = image;
        mType = type;
        mVitamins = vitamins;
        mID = id;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public int getID() {
        return mID;
    }

    public void setID(int mID) {
        this.mID = mID;
    }

    public int getVitamins() {
        return mVitamins;
    }

    public void setVitamins(int mVitamins) {
        this.mVitamins = mVitamins;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String mImage) {
        this.mImage = mImage;
    }



    @Override
    public String toString() {
        return "Fruit{" +
                "mID'" + mID + '\'' +
                ", mType'" + mType + '\'' +
                ", mVitamins'" + mVitamins + '\'' +
                ", mImage'" + mImage + '\'' +
                '}';
    }
}
