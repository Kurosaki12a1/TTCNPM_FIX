package com.bku.picshub.toplike;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.bku.picshub.BottomNavigationViewHelper;
import com.bku.picshub.R;
import com.bku.picshub.account.LoginActivity;
import com.bku.picshub.info.PostInfo;
import com.bku.picshub.newfeed.NewFeedAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Huy on 05/13/18.
 */

public class TopLikeActivity extends AppCompatActivity{
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private static final int ACTIVITY_NUM = 3;

    RecyclerView recyclerView;
    ArrayList<PostInfo> lstPostInfo=new ArrayList<>();
    HashMap<String,String> postsID_Likes = new HashMap<>();
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_1);
        setupBottomNavigationView();

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference likedPostDatabaseRef = rootRef.child("All_Liked_Post_Database");
        likedPostDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get postsID + likes of postID
                for (DataSnapshot snap: dataSnapshot.getChildren()){
                    postsID_Likes.put(snap.getKey(),String.valueOf(snap.getChildrenCount()));
                }

                DatabaseReference postInfoRef = rootRef.child("All_Post_Info_Database");
                postInfoRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // get all posts -> PostInfo Array
                        for(DataSnapshot singleSnapshot :dataSnapshot.getChildren()){
                            lstPostInfo.add(singleSnapshot.getValue(PostInfo.class));
                        }
                        // set number of likes for each post
                        for(int i = 0; i < lstPostInfo.size();i++) {
                            lstPostInfo.get(i).setLiked(postsID_Likes.get(lstPostInfo.get(i).getPostId()));
                        }
                        Collections.sort(lstPostInfo, new SortByLike());
                        NewFeedAdapter newFeedAdapter=new NewFeedAdapter(TopLikeActivity.this,lstPostInfo);
                        recyclerView.setAdapter(newFeedAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            }
        };
    }

    private void setupBottomNavigationView(){
        Log.d("MainScreenActivity", "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation( TopLikeActivity.this,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}
