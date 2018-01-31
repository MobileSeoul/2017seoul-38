package com.gmail.jskapplications.seoulpicture.model;

import java.io.Serializable;

/**
 * Created by for on 2017-10-14.
 */

public class PicturePost implements Serializable{

    private String imgUrl;
    private int id;
    private String mUserId;
    private String mTitle;
    private String mDescription;
    private int mCategory;
    private int mLocation;
    private int mLikes;
    private String mDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public int getmCategory() {
        return mCategory;
    }

    public void setmCategory(int mCategory) {
        this.mCategory = mCategory;
    }

    public int getmLocation() {
        return mLocation;
    }

    public void setmLocation(int mLocation) {
        this.mLocation = mLocation;
    }

    public int getmLikes() {
        return mLikes;
    }

    public void setmLikes(int mLikes) {
        this.mLikes = mLikes;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }
}
