package com.gmail.jskapplications.seoulpicture.model;

/**
 * Created by for on 2017-10-25.
 */

public class SingleUser {
    private String mId;
    private String mName;
    private String mImgUrl;
    private int mTotalLikes;

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

}
