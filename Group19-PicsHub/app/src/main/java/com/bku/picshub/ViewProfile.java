package com.bku.picshub;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bku.picshub.info.ImageUploadInfo;
import com.bku.picshub.info.PostInfo;
import com.bku.picshub.info.UserFollowInfo;
import com.bku.picshub.newfeed.NewFeedAdapter;
import com.bku.picshub.newfeed.SortByDate;
import com.bku.picshub.viewimageprofile.RecyclerViewAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import java.util.List;


/**
 * Created by Welcome on 4/8/2018.
 */

public class ViewProfile extends AppCompatActivity {
    private static final String TAG = "View Profile Activity";
    public static final String Database_Path="All_User_Info_Database";
    public static final String Following_Path="All_Following_User_Database";
    public static final String Follower_Path="All_Follower_User_Database";
    public static final String Post_Path="All_Post_Info_Database";
    public static final String NewFeed_Path="All_New_Feed_Database";
    private ImageView avatarUser;

    private static final int ACTIVITY_NUM = 1;
    private TextView  username,description,website,follow,unfollow,numFollower,numFollwing,numPost;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2nd;
    DatabaseReference databaseReference3rd;
    DatabaseReference databaseReference4th;
    DatabaseReference databaseReference5th;
    RecyclerView recyclerView;
    // Creating RecyclerView.Adapter.
    RecyclerView.Adapter adapter ;

    // Creating List of ImageUploadInfo class.
    List<ImageUploadInfo> list = new ArrayList<>();


    FirebaseAuth.AuthStateListener mAuthListener;
    private BottomNavigationViewEx bottomNavigationView;
    String userIdOfCurrentPost="";
    String AvatarURLOfCurrentPost="";
    String userID="";
    String myUserName="";
    String myAvatarURL="";
    String myEmail="";
    ArrayList<PostInfo> lstPostOfCurrentPost;
    ArrayList<PostInfo> lstNewFeed;
    boolean isFollowing=false;
    int numOfPost=0;
    static int i=0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpplprofile);

        bottomNavigationView = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        username=(TextView)findViewById(R.id.display_name);
        description=(TextView)findViewById(R.id.description);
        website=(TextView) findViewById(R.id.website);
        avatarUser=(ImageView) findViewById(R.id.avatar_user);
        follow=(TextView)findViewById(R.id.follow);
        unfollow=(TextView)findViewById(R.id.unfollow);
        numFollower=(TextView)findViewById(R.id.tvFollowers);
        numFollwing=(TextView)findViewById(R.id.tvFollowing);
        numPost=(TextView)findViewById(R.id.tvPosts);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView) ;
        recyclerView.setHasFixedSize(true);

        mAuth=FirebaseAuth.getInstance();
        userID=mAuth.getCurrentUser().getUid().toString();
        databaseReference3rd=FirebaseDatabase.getInstance().getReference(Following_Path);
        databaseReference4th=FirebaseDatabase.getInstance().getReference(Follower_Path);
        setupFirebaseAuth();

        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

        Bundle extras=getIntent().getExtras();
        final String email=extras.getString("email");
        username.setText(extras.getString("username"));

       /* databaseReference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myUserName=dataSnapshot.child("username").getValue(String.class);
                myAvatarURL=dataSnapshot.child("avatarURL").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myUserName=dataSnapshot.child("username").getValue(String.class);
                myAvatarURL=dataSnapshot.child("avatarURL").getValue(String.class);
                myUserName=dataSnapshot.child("email").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        if (postSnapshot.child("email").getValue().toString().equals(email)) {
                            userIdOfCurrentPost = postSnapshot.getKey();
                            description.setText(postSnapshot.child("description").getValue().toString());
                            website.setText(postSnapshot.child("website").getValue().toString());
                            AvatarURLOfCurrentPost = postSnapshot.child("avatarURL").getValue(String.class);
                            Glide.with(ViewProfile.this).load(postSnapshot.child("avatarURL").getValue().toString())
                                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                    .into(avatarUser);
                            getDataUserPath(userIdOfCurrentPost);
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        setupBottomNavigationView();
    }

    private void checkFollowed(final String userIdOfCurrentPost){

        databaseReference3rd.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (postSnapshot.getKey().equals(userIdOfCurrentPost)) {
                        isFollowing = true;
                        follow.setVisibility(View.INVISIBLE);
                        unfollow.setVisibility(View.VISIBLE);
                        return;
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


            unfollow.setVisibility(View.INVISIBLE);
            follow.setVisibility(View.VISIBLE);

    }

    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation( ViewProfile.this,bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


    }

    private void getDataUserPath(String strUserIdOfCurrentPost){
        final String userCurrentPost=strUserIdOfCurrentPost;
        checkFollowed(userCurrentPost);
        databaseReference3rd.child(userCurrentPost).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numFollwing.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference4th.child(userCurrentPost).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numFollower.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference2nd = FirebaseDatabase.getInstance().getReference("All_Image_Uploads_Database").child(userCurrentPost);
        databaseReference2nd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ImageUploadInfo imageUploadInfo = postSnapshot.getValue(ImageUploadInfo.class);
                    list.add(imageUploadInfo);

                }
                //list.add(imageUploadInfo);
                //dao nguoc thu tu theo ngay thang
                Collections.reverse(list);
                adapter = new RecyclerViewAdapter(getApplicationContext(), list);

                // chia list ra lam 3
                recyclerView.setLayoutManager(new GridLayoutManager(ViewProfile.this, 3));

                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        databaseReference5th = FirebaseDatabase.getInstance().getReference(Post_Path);
        databaseReference5th.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numOfPost = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    if (postSnapshot.child("accountId").getValue().equals(userCurrentPost)) {
                        numOfPost++;
                    }
                }
                numPost.setText(String.valueOf(numOfPost));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserFollowInfo userFollowInfo=new UserFollowInfo(username.getText().toString(),AvatarURLOfCurrentPost,myEmail);
                UserFollowInfo userFollowerInfo=new UserFollowInfo(myUserName,myAvatarURL,myEmail);
                databaseReference3rd.child(userID).child(userCurrentPost).setValue(userFollowInfo);
                databaseReference4th.child(userCurrentPost).child(userID).setValue(userFollowerInfo);
                follow.setVisibility(View.INVISIBLE);
                unfollow.setVisibility(View.VISIBLE);
                getListPostFromUserFollow();

            }
        });

        unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference3rd.child(userID).child(userCurrentPost).removeValue();
                databaseReference4th.child(userCurrentPost).child(userID).removeValue();
                unfollow.setVisibility(View.INVISIBLE);
                follow.setVisibility(View.VISIBLE);
                i=0;
                final DatabaseReference databaseReference2=FirebaseDatabase.getInstance().getReference(NewFeed_Path).child(userID);
                databaseReference2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot singleSnapshot:dataSnapshot.getChildren()){
                            if(singleSnapshot.child("accountId").getValue(String.class).equals(userIdOfCurrentPost)){
                                databaseReference2.child(singleSnapshot.getKey()).removeValue();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
    private void getListPostFromUserFollow() {

            databaseReference5th.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    lstPostOfCurrentPost = new ArrayList<>();
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        if (singleSnapshot.child("accountId").getValue(String.class).equals(userIdOfCurrentPost))
                            lstPostOfCurrentPost.add(singleSnapshot.getValue(PostInfo.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference(NewFeed_Path).child(userID);
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lstNewFeed = new ArrayList<>();
                    if (i==0) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        lstNewFeed.add(singleSnapshot.getValue(PostInfo.class));
                    }
                    resetDatabase();
                    i++;

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void resetDatabase(){
        lstNewFeed.addAll(lstPostOfCurrentPost);
        Collections.sort(lstNewFeed, new SortByDate());
        DatabaseReference databaseReference2=FirebaseDatabase.getInstance().getReference(NewFeed_Path).child(userID);
        databaseReference2.removeValue();
        for(int i=0;i<lstNewFeed.size();i++){
            databaseReference2.child(lstNewFeed.get(i).getPostId()).setValue(lstNewFeed.get(i));
        }

    }
}
