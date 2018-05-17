package com.bku.picshub.post;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bku.picshub.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by Welcome on 5/11/2018.
 */

public class CommentHolder extends RecyclerView.ViewHolder  {

    private TextView mName;
    private TextView mContent;
    private ImageView mAvatarURL;


    public CommentHolder(View view) {
        super(view);
        mName = (TextView) view.findViewById(R.id.name);
        mContent = (TextView) view.findViewById(R.id.content);
        mAvatarURL = (ImageView) view.findViewById(R.id.profile_photo);
    }

    public void whenBind(final CommentViewHolder commentView, final Context context) {
        mName.setText(commentView.getName());
        mContent.setText(commentView.getContent());
        Glide.with(context).load(commentView.getAvatarURL()).diskCacheStrategy(DiskCacheStrategy.RESULT).into(mAvatarURL);
    }
}
