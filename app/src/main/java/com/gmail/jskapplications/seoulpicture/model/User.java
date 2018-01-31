package com.gmail.jskapplications.seoulpicture.model;

import android.graphics.Bitmap;

import com.gmail.jskapplications.seoulpicture.network.CategoryNetwork;

import java.util.ArrayList;

/**
 * Created by for on 2017-10-14.
 */

public class User {

    private String mId;
    private String mName;
    private String mImgUrl;
    private int mTotalLikes;
    private static User instance = new User();
    private User() { }

    public static User getInstance() {
        return instance;
    }


    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }


    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmImgUrl() {
        return mImgUrl;
    }

    public void setmImgUrl(String mImgUrl) {
        this.mImgUrl = mImgUrl;
    }

    public int getmTotalLikes() {
        return mTotalLikes;
    }

    public void setmTotalLikes(int mTotalLikes) {
        this.mTotalLikes = mTotalLikes;
    }

    public static void setInstance(User instance) {
        User.instance = instance;
    }
}
