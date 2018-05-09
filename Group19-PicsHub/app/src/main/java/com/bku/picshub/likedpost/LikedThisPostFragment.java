package com.bku.picshub.likedpost;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.bku.picshub.R;


/**
 * Created by Welcome on 3/26/2018.
 */

public class LikedThisPostFragment extends Activity {
    @Override
        protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_likedthispost);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width=dm.widthPixels;
        int height=dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.6));
    }
}
