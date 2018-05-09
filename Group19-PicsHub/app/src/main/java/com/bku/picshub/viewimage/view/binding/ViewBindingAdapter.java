package com.bku.picshub.viewimage.view.binding;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bku.picshub.R;
import com.squareup.picasso.Picasso;


public class ViewBindingAdapter {
    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView imageView, String url) {
        Picasso.with(imageView.getContext())
            .load(url)
            .placeholder(R.drawable.ic_android)
            .into(imageView);
    }
}
