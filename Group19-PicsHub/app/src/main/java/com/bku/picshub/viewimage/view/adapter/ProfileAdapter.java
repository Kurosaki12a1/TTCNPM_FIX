package com.bku.picshub.viewimage.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.bku.picshub.BR;
import com.bku.picshub.R;
import com.bku.picshub.databinding.ItemRecyclerProfileBinding;
import com.bku.picshub.viewimage.listener.OnClickShowImageListener;
import com.bku.picshub.viewimage.model.Profile;

import java.util.List;


public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {
    private static final int MAX_SIZE_IMAGE_LOAD = 8;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Profile> mProfileList;
    private OnClickShowImageListener mOnClickShowImageListener;

    public ProfileAdapter(Context context, List<Profile> profileList,
                          OnClickShowImageListener onClickShowImageListener) {
        mContext = context;
        mProfileList = profileList;
        mOnClickShowImageListener = onClickShowImageListener;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemRecyclerProfileBinding binding = DataBindingUtil.inflate(mLayoutInflater,
            R.layout.item_recycler_profile, parent, false);
        return new ProfileViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ProfileViewHolder holder, int position) {
        Profile profile = mProfileList.get(position);
        holder.bind(profile);
    }

    @Override
    public int getItemCount() {
        return mProfileList == null ? 0 : mProfileList.size();
    }

    public class ProfileViewHolder extends RecyclerView.ViewHolder {
        private ItemRecyclerProfileBinding mBinding;

        public ProfileViewHolder(ItemRecyclerProfileBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(Profile profile) {
            mBinding.setVariable(BR.viewModel, profile);
            SlideImageAdapter slideImageAdapter =
                new SlideImageAdapter(mContext, profile, mOnClickShowImageListener);
            mBinding.slideImageViewPager.setAdapter(slideImageAdapter);
            mBinding.slideImageViewPager.setOffscreenPageLimit(MAX_SIZE_IMAGE_LOAD);
            mBinding.indicator.setViewPager(mBinding.slideImageViewPager);
            mBinding.executePendingBindings();
        }
    }
}
