package com.bku.picshub.info;

/**
 * Created by Welcome on 3/22/2018.
 */

public class PostInfo {
    private String postId;
    private String AccountId;
    private String Caption;
    private String Liked;
    private String timeStamp;

    public PostInfo(){

    }
    public PostInfo(String postId,String AccountId,String Liked,String Caption,String timeStamp){
        this.postId=postId;
        this.AccountId=AccountId;
        this.Liked=Liked;
        this.Caption=Caption;
        this.timeStamp=timeStamp;
    }

    public String getPostId(){return postId;}
    public String getAccountId(){return AccountId;}
    public String getCaption(){return Caption;}
    public String getLiked(){return Liked;}
    public String getTimeStamp(){return timeStamp;}

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setAccountId(String accountId) {
        AccountId = accountId;
    }

    public void setCaption(String caption) {
        Caption = caption;
    }

    public void setLiked(String liked) {
        Liked = liked;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
