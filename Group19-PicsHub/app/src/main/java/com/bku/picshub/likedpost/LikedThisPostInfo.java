package com.bku.picshub.likedpost;

/**
 * Created by Welcome on 3/26/2018.
 */

public class  LikedThisPostInfo {
    protected String username;
    protected String avatarUrl;

    public LikedThisPostInfo(){}

    public LikedThisPostInfo(String username,String avatarUrl){
        this.username=username;
        this.avatarUrl=avatarUrl;
    }

    public String getUsername(){return username;}

    public String getAvatarUrl(){return avatarUrl;}

}
