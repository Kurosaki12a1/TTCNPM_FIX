package com.bku.picshub.likedpost;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bku.picshub.R;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Welcome on 3/26/2018.
 */

public class LikedThisPostAdapter extends RecyclerView.Adapter<LikedThisPostAdapter.ViewHolder> {
    static Context context;
    List<LikedThisPostInfo> MainLikedThisPostList;


    public LikedThisPostAdapter(Context context, List<LikedThisPostInfo> TempList) {

        this.MainLikedThisPostList = TempList;

        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_likedthispost, parent, false);

        LikedThisPostAdapter.ViewHolder viewHolder = new LikedThisPostAdapter.ViewHolder(view);
        return viewHolder;
    }




    @Override
    public void onBindViewHolder(LikedThisPostAdapter.ViewHolder holder, int position) {
        LikedThisPostInfo likedThisPostInfo = MainLikedThisPostList.get(position);

        //   holder.imageNameTextView.setText(UploadInfo.getImageName());

        //Loading image from Glide library.
        Glide.with(context).load(likedThisPostInfo.getAvatarUrl()).into(holder.imageView);
        holder.username.setText(likedThisPostInfo.getUsername());

    }

    @Override
    public int getItemCount() {

        return MainLikedThisPostList.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView username;
      //  public TextView liked;
        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.avatar_user_liked_post);

            username = (TextView) itemView.findViewById(R.id.usernamelikedphoto);

           // liked=(TextView) itemView.findViewById(R.id.heartNumber);

        }
    }
}
