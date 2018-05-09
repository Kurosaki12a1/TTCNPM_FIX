package com.bku.picshub.viewimage.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.bku.picshub.R;
import com.bku.picshub.databinding.ActivityViewimageBinding;
import com.bku.picshub.viewimage.listener.OnClickShowImageListener;
import com.bku.picshub.viewimage.model.Constant;
import com.bku.picshub.viewimage.model.Profile;
import com.bku.picshub.viewimage.view.adapter.ProfileAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewImageActivity extends AppCompatActivity implements OnClickShowImageListener {
    private ActivityViewimageBinding mBinding;
    private ProfileAdapter mProfileAdapter;
    private ArrayList<Profile> mProfileList;

    public String postId;
    public String userID;
    public long liked;
    public List<String> imageURL;

    public ViewImageActivity(String postId,String userID,long liked,List<String> ImageURL){
        this.postId=postId;
        this.userID=userID;
        this.liked=liked;
        this.imageURL=ImageURL;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            mBinding = DataBindingUtil.setContentView(this, R.layout.activity_viewimage);
        initData();
        initViews();
    }




    public String getPostId(){return postId;}

    public String getUserID(){return userID;}

    public long getLiked(){return liked;}

    public List<String> getImageURL(){return imageURL;}
    private void initData() {
   //     mProfileList = CreateData.createProfile();
    }

    private void initViews() {
        mProfileAdapter = new ProfileAdapter(this, mProfileList, this);
        mBinding.recyclerViewProfile.setAdapter(mProfileAdapter);
        mBinding.recyclerViewProfile.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClickShowImage(Profile profile, int positionImage) {
        Intent intent = new Intent(this, ShowImageActivity.class);
        intent.putExtra(Constant.KEY_POSITION_IMAGE, positionImage);
        intent.putExtra(Constant.KEY_DATA_PROFILE, profile);
        startActivity(intent);
    }
}
