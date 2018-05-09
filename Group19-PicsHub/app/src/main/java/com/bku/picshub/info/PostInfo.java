package com.bku.picshub.info;

/**
 * Created by Welcome on 3/22/2018.
 */

public class PostInfo {
    private String postId;
    private String AccountId;
    private String Caption;
    private String Liked;


    public PostInfo(String postId,String AccountId,String Liked,String Caption){
        this.postId=postId;
        this.AccountId=AccountId;
        this.Liked=Liked;
        this.Caption=Caption;
    }

    public String getPostId(){return postId;}
    public String getAccountId(){return AccountId;}
    public String getCaption(){return Caption;}
    public String getLiked(){return Liked;}

}
