package com.bku.picshub.likedpost;

/**
 * Created by Welcome on 3/26/2018.
 */

public class  LikedThisPostInfo {
    protected String username;
    protected String avatarURL;

    public LikedThisPostInfo(){}

    public LikedThisPostInfo(String username,String avatarURL){
        this.username=username;
        this.avatarURL=avatarURL;
    }

    public String getUsername(){return username;}

    public String getAvatarUrl(){return avatarURL;}

}
