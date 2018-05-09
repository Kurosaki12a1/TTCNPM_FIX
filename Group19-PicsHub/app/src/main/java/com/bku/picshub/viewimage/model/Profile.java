package com.bku.picshub.viewimage.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by framgia on 27/04/2017.
 */
public class Profile implements Serializable {
    private String mId;//postID
    private String mUserID;
    private long liked;
    private ArrayList<String> mImageList;

    public Profile(String id, String mUserID, long liked, ArrayList<String> imageList) {
        this.mId = id;
        this.mUserID = mUserID;
        this.liked = liked;
        this.mImageList = imageList;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getUserID() {
        return mUserID;
    }

    public void setUserID(String userID) {
        mUserID = userID;
    }

    public List<String> getImageList() {
        return mImageList;
    }

    public void setImageList(ArrayList<String> imageList) {
        mImageList = imageList;
    }

    public long getLiked() {
        return liked;
    }

    public void setLIked(long nLiked) {
        liked = nLiked;
    }


}
