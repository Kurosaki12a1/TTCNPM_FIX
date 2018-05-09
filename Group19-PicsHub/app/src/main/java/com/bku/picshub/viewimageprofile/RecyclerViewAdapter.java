package com.bku.picshub.viewimageprofile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bku.picshub.R;
import com.bku.picshub.info.ImageUploadInfo;
import com.bku.picshub.post.ViewPostActivity;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by AndroidJSon.com on 6/18/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>  {

    static Context context;
    List<ImageUploadInfo> MainImageUploadInfoList;


    public RecyclerViewAdapter(Context context, List<ImageUploadInfo> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ImageUploadInfo UploadInfo = MainImageUploadInfoList.get(position);

     //   holder.imageNameTextView.setText(UploadInfo.getImageName());

        //Loading image from Glide library.
        Glide.with(context).load(UploadInfo.getImageURL()).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ViewPostActivity.class);
                intent.putExtra("postID",UploadInfo.getPostId());
                intent.putExtra("Caption",UploadInfo.getCaption());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);





        }
    }
}
