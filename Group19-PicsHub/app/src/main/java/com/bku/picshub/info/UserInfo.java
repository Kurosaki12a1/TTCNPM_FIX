package com.bku.picshub.info;

/**
 * Created by Welcome on 3/3/2018.
 */

public class UserInfo {
    private String email;
    //public String password;
    private String username;

    private String AvatarURL;

    private String description;

    private String website;

    private String phoneNumber;


    public UserInfo(String email,String username,String AvatarURL,String description,String website
    ,String phoneNumber) {
        this.email=email;
        this.username=username;
        this.AvatarURL=AvatarURL;
        this.description=description;
        this.website=website;
        this.phoneNumber=phoneNumber;

    }

    public UserInfo() {

    }
    public String getUsername() {
        return username;
    }

    public String getAvatarURL() {
        return AvatarURL;
    }

    public String getEmail(){return email;}

    public String getPhoneNumber(){return phoneNumber;}

    public String getDescription(){return description;}

    public String getWebsite(){return website;}



}
