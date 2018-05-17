package com.bku.picshub.info;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Welcome on 5/11/2018.
 */

public class CommentInfo {

    private String username;
    private String content;
    private String avatarURL;
    private String timeStamp;

    public CommentInfo(){}

    public CommentInfo(String username, String content, String avatarURL, String timeStamp)
    {
        this.username=username;
        this.content=content;
        this.avatarURL = avatarURL;
        this.timeStamp = timeStamp;
    }

    public String getUsername(){return username;}

    public String getContent() {
        return content;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("username", username);
        result.put("content", content);
        result.put("avatarURL", avatarURL);
        result.put("timeStamp", timeStamp);
        return result;
    }
}
