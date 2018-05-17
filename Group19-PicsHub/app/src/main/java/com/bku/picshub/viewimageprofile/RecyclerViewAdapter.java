package com.bku.picshub.viewimageprofile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bku.picshub.R;
import com.bku.picshub.info.ImageUploadInfo;
import com.bku.picshub.post.ViewPostActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

/**
 * Created by AndroidJSon.com on 6/18/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>  {

    private final  Context context;
    private List<ImageUploadInfo> MainImageUploadInfoList;


    public RecyclerViewAdapter(Context context, List<ImageUploadInfo> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.square_grid_relative_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);


        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ImageUploadInfo UploadInfo = MainImageUploadInfoList.get(position);

     //   holder.imageNameTextView.setText(UploadInfo.getImageName());

        //Loading image from Glide library.
        Glide
                .with(context)
                .load(UploadInfo.getImageURL())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.mProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.image);



        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ViewPostActivity.class);
                intent.putExtra("postID",UploadInfo.getPostId());
                intent.putExtra("Caption",UploadInfo.getCaption());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        ProgressBar mProgressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.gridImageView);
            this.mProgressBar = itemView.findViewById(R.id.gridImageProgressBar);
        }
    }
}
