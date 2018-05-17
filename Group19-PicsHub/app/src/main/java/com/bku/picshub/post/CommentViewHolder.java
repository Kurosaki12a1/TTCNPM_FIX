package com.bku.picshub.post;

/**
 * Created by Welcome on 5/11/2018.
 */

public class CommentViewHolder {
    private String name;
    private String content;
    private String avatarURL;

    public CommentViewHolder(final String name, final String content, final String avatarURL) {
        this.name = name;
        this.content = content;
        this.avatarURL = avatarURL;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public String getContent() {
        return content;
    }

    public String getName() {
        return name;
    }

    public String getAvatarURL() {
        return avatarURL;
    }
}
