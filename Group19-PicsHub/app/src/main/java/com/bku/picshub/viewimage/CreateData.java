package com.bku.picshub.viewimage;

import com.bku.picshub.viewimage.model.Profile;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thinh on 3/29/2018.
 */

public class CreateData {

    public static List<Profile> createProfile(String userID, String PostID, long liked, ArrayList<String> imageList) {

        List<Profile> profileList = new ArrayList<>();
        profileList.add(create(userID,PostID,liked, imageList));

        return profileList;
    }

    public static Profile create(String userID, String PostID, long liked, ArrayList<String> imageList) {
        return new Profile(userID, PostID, liked, imageList);
    }
}
