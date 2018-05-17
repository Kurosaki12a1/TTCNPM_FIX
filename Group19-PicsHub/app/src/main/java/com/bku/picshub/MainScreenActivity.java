package com.bku.picshub;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;

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
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Welcome on 2/28/2018.
 */

public class MainScreenActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private static final int ACTIVITY_NUM = 0;

    RecyclerView recyclerView;
    ArrayList<PostInfo> lstPostInfo=new ArrayList<>();
    ArrayList<String> postIdList = new ArrayList<String>();
    ArrayList<String> followingList = new ArrayList<>();
    private boolean isScrolling = false;
    private int currentItems, totalItems, scrollOutItems;
    private int lastPosPost = 2;
    private int count = 0;
    private boolean isTime = true;
    private int toDo = 0;
    private static int shown = 0;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference followingRef;
    private DatabaseReference postRef;
    private NewFeedAdapter newFeedAdapter;
    private LinearLayoutManager mLayoutManager;
    ArrayList<PostInfo>lstNewFeed =new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_1);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

       /* followingRef = database.getReference("All_Following_User_Database");
        postRef = database.getReference("All_Post_Info_Database");*/
        auth = FirebaseAuth.getInstance();

        String userID = auth.getCurrentUser().getUid();
        Bundle extras=getIntent().getExtras();
        if(extras!=null) {
            shown = extras.getInt("shown");
        }
        else{
            shown=0;
        }
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("All_New_Feed_Database").child(userID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lstPostInfo=new ArrayList<>();
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                    lstPostInfo.add(singleSnapshot.getValue(PostInfo.class));
                }
                Collections.reverse(lstPostInfo);
                addCount();
                //makeRecycle();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


      /*  followingRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    addFollowing(singleSnapshot.getKey());
                }
                for (String i : followingList) {
                    postRef.orderByChild("accountId").equalTo(i).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                addPostId(singleSnapshot.getKey());
                            }

                            addCount();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });*/
        setupBottomNavigationView();


        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get current user
     //   final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                 /*   startActivity(new Intent(MainScreenActivity.this, LoginActivity.class));
                    finish();*/
                   startActivity(new Intent(getApplicationContext(),LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            }
        };

       /* menuPopUp=(Button) findViewById(R.id.profileMenu);
        menuPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMenu();
            }
        });*/

    }
    private void setupBottomNavigationView(){
        Log.d("MainScreenActivity", "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation( MainScreenActivity.this,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
    public void signOut() {
            auth.signOut();
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
    private void FetchData() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i < 2; i++) {
                    if (lastPosPost >= lstPostInfo.size()-1)
                        handler.removeCallbacks(this);
                    getNewPost(lastPosPost);
                    lastPosPost++;
                    shown++;
                    newFeedAdapter.notifyDataSetChanged();
                }
            }
        }, 3000);
    }

    private void addCount() {
            if (lstPostInfo.size() >= 2) {
                toDo=2;
                getNewPost(0);

            }
            else {
                lstNewFeed.addAll(lstPostInfo);
                makeRecycle();
            }
    }

    private void getNewPost(int pos) {
        if(pos<lstPostInfo.size()) {
            PostInfo postInfo = lstPostInfo.get(pos);
            addPost(postInfo);
            getMorePost();
        }

    }

    private void addPost(PostInfo postInfo) {
        lstNewFeed.add(new PostInfo(postInfo.getPostId(), postInfo.getAccountId(), postInfo.getLiked(), postInfo.getCaption(), postInfo.getTimeStamp()));
        if (newFeedAdapter != null)
            newFeedAdapter.notifyItemChanged(lstNewFeed.size() - 1);
        if (lstNewFeed.size()==2)
            makeRecycle();
    }

    private void getMorePost() {
        if (toDo > shown) {
            toDo--;
            getNewPost(toDo);
        }
    }




    private void updatePost(int start,int end){
        if(start>=lstPostInfo.size()){
            return;
        }
        if(end>=lstPostInfo.size()){
            for(int i=start;i<lstPostInfo.size();i++){
                shown++;
                lstNewFeed.add(lstPostInfo.get(i));
            }

        }
        else {
            for (int i = start; i < end; i++) {
                shown++;
                lstNewFeed.add(lstPostInfo.get(i));
            }

        }

    }

    private void makeRecycle() {

        newFeedAdapter = new NewFeedAdapter(MainScreenActivity.this, lstNewFeed);
        recyclerView.setAdapter(newFeedAdapter);
        newFeedAdapter.notifyDataSetChanged();
        shown=2;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }

            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = mLayoutManager.getChildCount();
                totalItems = mLayoutManager.getItemCount();
                scrollOutItems = mLayoutManager.findFirstVisibleItemPosition();
                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;
                    FetchData();
                }

            }
        });
    }

}
