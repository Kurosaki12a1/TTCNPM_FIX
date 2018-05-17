package com.bku.picshub.toplike;

import com.bku.picshub.info.PostInfo;

import java.util.Comparator;

/**
 * Created by Huy on 05/13/18.
 */
public class SortByLike implements Comparator<PostInfo> {
    @Override
    public int compare(PostInfo post1, PostInfo post2) {
        return Integer.parseInt(post2.getLiked()) - Integer.parseInt(post1.getLiked());
    }
}
