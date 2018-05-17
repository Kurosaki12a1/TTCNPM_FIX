package com.bku.picshub.likedpost;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import com.bku.picshub.R;
import com.bku.picshub.info.LikedPostInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * Created by Welcome on 3/26/2018.
 */

public class LikedThisPostPopUp extends Activity {
    RecyclerView recyclerView;

    String postId="";
    @Override
        protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_likedthispost);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width=dm.widthPixels;
        int height=dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.6));

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(LikedThisPostPopUp.this));

        Bundle extras=getIntent().getExtras();
        postId=extras.getString("postId");

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("All_Liked_Post_Database").child(postId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<LikedPostInfo> lst=new ArrayList<>();
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    lst.add(singleSnapshot.getValue(LikedPostInfo.class));
                }
                setRecyclerView(lst);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void setRecyclerView(ArrayList<LikedPostInfo> list){
        LikedThisPostAdapter likedThisPostAdapter=new LikedThisPostAdapter(this,list);
        recyclerView.setAdapter(likedThisPostAdapter);
    }
}
