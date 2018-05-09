package com.bku.picshub;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.bku.picshub.post.UploadPostActivity;
import com.bku.picshub.search.SearchActivity;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by Welcome on 3/14/2018.
 */

public class BottomNavigationViewHelper {
    private static final String TAG = "BottomNavigationViewHel";



    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx){
        Log.d(TAG, "setupBottomNavigationView: Setting up BottomNavigationView");
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
    }

    public static void enableNavigation(final Context context, BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.ic_house:
                        context.startActivity(new Intent(context, MainScreenActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        break;
                    case R.id.ic_search:
                    {
                        context.startActivity(new Intent(context, SearchActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }
                        break;
                    case R.id.ic_upload:
                    {
                        context.startActivity(new Intent(context,UploadPostActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }
                        break;
                    case R.id.ic_alert:
                        break;
                    case R.id.ic_profile:
                      context.startActivity(new Intent(context,ViewSelfProfile.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                      /*Intent intent1=new Intent(context,ViewSelfProfile.class);
                      context.startActivity(intent1);*/
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }




}
