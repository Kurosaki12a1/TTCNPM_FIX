package com.bku.picshub.likedpost;

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
 * Created by Welcome on 3/26/2018.
 */

public class LikedThisPostAdapter extends RecyclerView.Adapter<LikedThisPostAdapter.ViewHolder> {
    static Context context;
    ArrayList<LikedPostInfo> MainLikedThisPostList;

    FirebaseAuth mAuth;
    String emailOfUser;
    public LikedThisPostAdapter(Context context, ArrayList<LikedPostInfo> TempList) {
        this.MainLikedThisPostList=new ArrayList<>(TempList);
        this.MainLikedThisPostList = TempList;

        this.context = context;
        mAuth=FirebaseAuth.getInstance();
        emailOfUser=mAuth.getCurrentUser().getEmail();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_up_liked, parent, false);

        LikedThisPostAdapter.ViewHolder viewHolder = new LikedThisPostAdapter.ViewHolder(view);
        return viewHolder;
    }




    @Override
    public void onBindViewHolder(LikedThisPostAdapter.ViewHolder holder, int position) {
        final LikedPostInfo likedThisPostInfo = MainLikedThisPostList.get(position);

        //   holder.imageNameTextView.setText(UploadInfo.getImageName());

        //Loading image from Glide library.
        Glide.with(context).load(likedThisPostInfo.getAvatarURL()).diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.imageView);
        holder.username.setText(likedThisPostInfo.getusername());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("All_User_Info_Database");
                databaseReference.child(likedThisPostInfo.getUserId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String email=dataSnapshot.child("email").getValue(String.class);
                        if(emailOfUser.equals(email)){
                            Intent intent=new Intent(context, ViewSelfProfile.class);
                            context.startActivity(intent);
                        }
                        else{
                            Intent intent=new Intent(context, ViewProfile.class);
                            intent.putExtra("email",email);
                            intent.putExtra("username",likedThisPostInfo.getusername());
                            context.startActivity(intent);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
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

            username = (TextView) itemView.findViewById(R.id.user_name_liked_post);

           // liked=(TextView) itemView.findViewById(R.id.heartNumber);

        }
    }
}
