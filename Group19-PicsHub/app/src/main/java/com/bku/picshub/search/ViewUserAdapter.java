package com.bku.picshub.search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bku.picshub.R;
import com.bku.picshub.ViewProfile;
import com.bku.picshub.ViewSelfProfile;
import com.bku.picshub.info.UserInfo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

/**
 * Created by Welcome on 4/8/2018.
 */

public class ViewUserAdapter extends RecyclerView.Adapter<ViewUserAdapter.ViewHolder> {
    static Context context;
    List<UserInfo> AllUserInfo;
    FirebaseAuth mAuth;
    private static AdapterView.OnItemClickListener listener;

    public ViewUserAdapter(Context context, List<UserInfo> TempList) {

        this.AllUserInfo = TempList;

        this.context = context;
        mAuth=FirebaseAuth.getInstance();
    }

    @Override
    public ViewUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_list, parent, false);

        ViewUserAdapter.ViewHolder viewHolder = new ViewUserAdapter.ViewHolder(view);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(ViewUserAdapter.ViewHolder holder, int position) {

        final UserInfo userInfo = AllUserInfo.get(position);

        //   holder.imageNameTextView.setText(UploadInfo.getImageName());

        //Loading image from Glide library.
        Glide.with(context).load(userInfo.getAvatarURL()).diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.avatarUser);

        holder.userName.setText(userInfo.getUsername());

        holder.email.setText(userInfo.getEmail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userInfo.getEmail().equals(mAuth.getCurrentUser().getEmail())){
                    Intent intent =  new Intent(context, ViewSelfProfile.class);
                    context.startActivity(intent);
                }
                else {
                    Intent intent = new Intent(context, ViewProfile.class);
                    intent.putExtra("email", userInfo.getEmail());
                    intent.putExtra("username", userInfo.getUsername());
                    context.startActivity(intent);
                }

            }
        });

    }

    @Override
    public int getItemCount() {

        return AllUserInfo.size();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView avatarUser;
        public TextView userName,email;

        public ViewHolder(View itemView) {
            super(itemView);

            avatarUser = (ImageView) itemView.findViewById(R.id.profile_image);

            userName = (TextView) itemView.findViewById(R.id.username);

            email=(TextView) itemView.findViewById(R.id.email);



        }
    }
}
