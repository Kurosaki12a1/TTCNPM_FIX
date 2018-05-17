package com.bku.picshub.follow;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import com.bku.picshub.R;
import com.bku.picshub.info.UserFollowInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Welcome on 5/16/2018.
 */

public class FollowingPopUp extends Activity {
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_following);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .6));

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(FollowingPopUp.this));
        mAuth=FirebaseAuth.getInstance();
        final String userId=mAuth.getCurrentUser().getUid();

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("All_Follower_User_Database");
        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<UserFollowInfo> lst=new ArrayList<>();
                for(DataSnapshot singleSnapshot:dataSnapshot.getChildren()){
                    lst.add(singleSnapshot.getValue(UserFollowInfo.class));
                }
                setRecyclerView(lst);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void setRecyclerView(ArrayList<UserFollowInfo> list){
        FollowAdapter followAdapter=new FollowAdapter(FollowingPopUp.this,list);
        recyclerView.setAdapter(followAdapter);
    }
}
