package com.bku.picshub.info;

/**
 * Created by Welcome on 4/12/2018.
 */

public class UserFollowInfo {
    private String username;
    private String avatarURL;

    public UserFollowInfo(){}

    public UserFollowInfo(String username,String avatarURL)
    {
        this.username=username;
        this.avatarURL=avatarURL;
    }

    public String getUsername(){return username;}

    public String getAvatarURL(){return avatarURL;}

}
