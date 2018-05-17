package com.bku.picshub.post;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import com.bku.picshub.search.ViewUserAdapter;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Welcome on 5/11/2018.
 */

public class UpLoadPostAdapter extends  RecyclerView.Adapter<UpLoadPostAdapter.ViewHolder>{
    static Context context;
    ArrayList<Bitmap> ListBitMap;
    ArrayList<Uri> ListUri;
    Uri File;
    public UpLoadPostAdapter(Context context, ArrayList<Bitmap> TempList,ArrayList<Uri> lst) {
        this.ListBitMap=new ArrayList<>(TempList);
        this.ListBitMap = TempList;
        this.context = context;
        this.ListUri=new ArrayList<>(lst);
        this.ListUri=lst;
    }



    @Override
    public UpLoadPostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.uploadpost_recycleview, parent, false);

        UpLoadPostAdapter.ViewHolder viewHolder = new UpLoadPostAdapter.ViewHolder(view);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(UpLoadPostAdapter.ViewHolder holder, int position) {

         final int nTempPosition=position;
         Bitmap bitmap = ListBitMap.get(position);

         holder.imageView.setImageBitmap(bitmap);

         holder.delete.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 ListBitMap.remove(nTempPosition);
                 ListUri.remove(nTempPosition);
                 notifyItemRemoved(nTempPosition);
                 notifyItemRangeChanged(nTempPosition,ListBitMap.size());
             }
         });


    }

    @Override
    public int getItemCount() {

        return ListBitMap.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView delete,imageView;


        public ViewHolder(View itemView) {
            super(itemView);

            delete = (ImageView) itemView.findViewById(R.id.deleteImage);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);




        }
    }
}
