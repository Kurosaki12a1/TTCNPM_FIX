package com.bku.picshub.viewimage.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bku.picshub.R;
import com.bku.picshub.databinding.FragmentSlideProfileBinding;
import com.bku.picshub.viewimage.listener.OnClickShowImageListener;
import com.bku.picshub.viewimage.model.Profile;

import java.util.List;


public class SlideImageAdapter extends PagerAdapter {
    private Context mContext;
    private List<String> mImageList;
    private LayoutInflater mLayoutInflater;
    private Profile mProfile;

    private OnClickShowImageListener mOnClickShowImageListener;

    public SlideImageAdapter(Context context, Profile profile,
                             OnClickShowImageListener onClickShowImageListener) {
        mContext = context;
        mProfile = profile;
        mImageList = profile.getImageList();
        mOnClickShowImageListener = onClickShowImageListener;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mImageList == null ? 0 : mImageList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        FragmentSlideProfileBinding binding = DataBindingUtil.inflate(mLayoutInflater,
            R.layout.fragment_slide_profile, container, false);
        binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnClickShowImageListener != null) {
                    mOnClickShowImageListener.onClickShowImage(mProfile, position);
                    return true;
                }
                return false;
            }
        });
        binding.setUrl(mImageList.get(position));
        binding.setPosition(position);
        binding.getRoot().setTag(position);
        container.addView(binding.getRoot(), 0);
        return binding.getRoot();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
