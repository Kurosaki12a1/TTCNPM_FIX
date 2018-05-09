package com.bku.picshub.viewimage.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bku.picshub.R;
import com.bku.picshub.databinding.ActivityViewimageBinding;
import com.bku.picshub.viewimage.CreateData;
import com.bku.picshub.viewimage.listener.OnClickShowImageListener;
import com.bku.picshub.viewimage.model.Profile;
import com.bku.picshub.viewimage.view.adapter.ProfileAdapter;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Welcome on 3/31/2018.
 */

public class ViewImageFragment extends Fragment implements OnClickShowImageListener {

   /* protected ViewPostActivity viewPost;*/
    private ActivityViewimageBinding mBinding;
    private ProfileAdapter mProfileAdapter;
    private List<Profile> mProfileList;

    private String postId;
    private String userId;
    private long liked;
    private ArrayList<String> imageURL;

   /* @Override
    public void onAttach(Context context){
            super.onAttach(context);
            if(viewPost==null && context instanceof  ViewPostActivity){
                viewPost=(ViewPostActivity)context;
            }
    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_viewimage, container, false);
      //  mBinding = DataBindingUtil.setContentView(this, R.layout.activity_viewimage);
        postId=this.getArguments().getString("POSTID");
        userId=this.getArguments().getString("USERID");
        liked=Long.parseLong(this.getArguments().getString("LIKED"));
        imageURL=this.getArguments().getStringArrayList("arrayImageURL");
        initData(postId,userId,liked,imageURL);
        initViews();
     //   View view = inflater.inflate(R.layout.activity_viewimage, container, false);
        return view;
    }

    public ViewImageFragment(){}

    @Override
    public void onClickShowImage(Profile profile, int positionImage) {

    }
    private void initData(String postId,String userId,long liked,ArrayList<String>ImageURL) {
        mProfileList = CreateData.createProfile(userId,postId,liked,ImageURL);
    }


    private void initViews() {
        mProfileAdapter = new ProfileAdapter(getActivity().getApplicationContext(), mProfileList, this);
        mBinding.recyclerViewProfile.setAdapter(mProfileAdapter);
        mBinding.recyclerViewProfile.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
    }

}
