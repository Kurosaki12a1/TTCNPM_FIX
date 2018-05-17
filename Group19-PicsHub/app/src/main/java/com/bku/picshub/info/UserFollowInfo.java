package com.bku.picshub.info;

/**
 * Created by Welcome on 4/12/2018.
 */

public class UserFollowInfo {
    private String username;
    private String avatarURL;
    private String email;

    public UserFollowInfo(){}

    public UserFollowInfo(String username,String avatarURL,String email)
    {
        this.username=username;
        this.avatarURL=avatarURL;
        this.email=email;
    }

    public String getUsername(){return username;}

    public String getAvatarURL(){return avatarURL;}

    public String getEmail(){return email;}

}
