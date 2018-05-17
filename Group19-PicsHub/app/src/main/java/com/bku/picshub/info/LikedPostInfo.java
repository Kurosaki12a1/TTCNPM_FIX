package com.bku.picshub.info;

/**
 * Created by Welcome on 5/11/2018.
 */

public class LikedPostInfo {

    private  String username;
    private  String avatarURL;
    private String userId;

    public LikedPostInfo(){

    }
    public LikedPostInfo(String username,String avatarURL,String userId){
        this.username=username;
        this.avatarURL=avatarURL;
        this.userId=userId;
    }

    public String getusername(){return username;}
    public String getAvatarURL(){return avatarURL;}
    public String getUserId(){return  userId;}
}
