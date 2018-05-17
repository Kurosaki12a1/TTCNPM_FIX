package com.bku.picshub.follow;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bku.picshub.R;
import com.bku.picshub.ViewProfile;
import com.bku.picshub.ViewSelfProfile;
import com.bku.picshub.info.LikedPostInfo;
import com.bku.picshub.info.UserFollowInfo;
import com.bku.picshub.likedpost.LikedThisPostAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Welcome on 5/16/2018.
 */

public class FollowAdapter  extends RecyclerView.Adapter<FollowAdapter.ViewHolder> {

    static Context context;
    ArrayList<UserFollowInfo> lstUser;

    public FollowAdapter(Context context, ArrayList<UserFollowInfo> lst){
        this.context=context;
        this.lstUser=new ArrayList<>(lst);
        this.lstUser=lst;

    }

    @Override
    public FollowAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_up_liked, parent, false);

        FollowAdapter.ViewHolder viewHolder = new FollowAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FollowAdapter.ViewHolder holder, int position) {
        final UserFollowInfo userFollowInfo = lstUser.get(position);

        //   holder.imageNameTextView.setText(UploadInfo.getImageName());

        //Loading image from Glide library.
        Glide.with(context).load(userFollowInfo.getAvatarURL()).diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.imageView);
        holder.username.setText(userFollowInfo.getUsername());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ViewProfile.class);
                intent.putExtra("email",userFollowInfo.getEmail());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView username;
        //  public TextView liked;
        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.avatar_user_liked_post);

            username = (TextView) itemView.findViewById(R.id.user_name_liked_post);

            // liked=(TextView) itemView.findViewById(R.id.heartNumber);

        }
    }
}
