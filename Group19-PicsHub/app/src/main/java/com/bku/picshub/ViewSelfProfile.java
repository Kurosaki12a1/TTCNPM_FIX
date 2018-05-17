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
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bku.picshub.account.AccountSettingsActivity;
import com.bku.picshub.account.LoginActivity;
import com.bku.picshub.follow.FollowerPopUp;
import com.bku.picshub.follow.FollowingPopUp;
import com.bku.picshub.info.ImageUploadInfo;
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
 * Created by Welcome on 3/3/2018.
 */

public class ViewSelfProfile extends AppCompatActivity {
    private Button popUpMenu;
    private TextView userName, emailName, description, website,numFollower,numFollwing,numPost;
    private ImageView avatar;
    private static final int ACTIVITY_NUM = 4;

    public static final String Database_Path="All_User_Info_Database";
    public static final String Following_Path="All_Following_User_Database";
    public static final String Follower_Path="All_Follower_User_Database";
    public static final String Post_Path="All_Post_Info_Database";

    DatabaseReference databaseReference;
    DatabaseReference databaseReference2nd;
    DatabaseReference databaseReference3rd;
    DatabaseReference databaseReference4th;
    DatabaseReference databaseReference5th;
    // FirebaseAuth Auth;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListener;

    // Creating RecyclerView.
    RecyclerView recyclerView;

    // Creating RecyclerView.Adapter.
    RecyclerView.Adapter adapter;

    // Creating List of ImageUploadInfo class.
    List<ImageUploadInfo> list = new ArrayList<>();

    int nPostOfUser=0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfprofile);
        setupBottomNavigationView();
        //declare
        userName = (TextView) findViewById(R.id.display_name);
        avatar = (ImageView) findViewById(R.id.avatar_user);
        emailName = (TextView) findViewById(R.id.username);
        description = (TextView) findViewById(R.id.description);
        website = (TextView) findViewById(R.id.website);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        numFollower=(TextView)findViewById(R.id.tvFollowers);
        numFollwing=(TextView)findViewById(R.id.tvFollowing);
        numPost=(TextView)findViewById(R.id.tvPosts);

        // Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);
        // Setting 3 column Recycler View
        recyclerView.setLayoutManager(new GridLayoutManager(ViewSelfProfile.this, 3));


//        Get userId to get fileChild
        mAuth = FirebaseAuth.getInstance();
        final String userId = mAuth.getCurrentUser().getUid().toString();

        //
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            }
        };
        //Store

        //get UserInfo From DataBase
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path).child(userId);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userName.setText(dataSnapshot.child("username").getValue(String.class));
                emailName.setText(dataSnapshot.child("email").getValue(String.class));
                Glide.with(ViewSelfProfile.this).load(dataSnapshot.child("avatarURL").getValue(String.class))
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(avatar);
                description.setText(dataSnapshot.child("description").getValue(String.class));
                website.setText(dataSnapshot.child("website").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference2nd = FirebaseDatabase.getInstance().getReference("All_Image_Uploads_Database").child(userId);

        // Adding Add Value Event Listener to databaseReference.
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
                recyclerView.setLayoutManager(new GridLayoutManager(ViewSelfProfile.this, 3));

                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference3rd=FirebaseDatabase.getInstance().getReference(Following_Path).child(userId);
        databaseReference3rd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numFollwing.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference4th=FirebaseDatabase.getInstance().getReference(Follower_Path).child(userId);
        databaseReference4th.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numFollower.setText(String.valueOf(dataSnapshot.getChildrenCount()));
           }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference5th=FirebaseDatabase.getInstance().getReference(Post_Path);
        databaseReference5th.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nPostOfUser=0;
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    if(singleSnapshot.child("accountId").getValue().equals(userId)){
                        nPostOfUser++;
                    }
                }
                numPost.setText(String.valueOf(nPostOfUser));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        numFollower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ViewSelfProfile.this, FollowerPopUp.class);
                startActivity(intent);
            }
        });

        numFollwing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ViewSelfProfile.this, FollowingPopUp.class);
                startActivity(intent);
            }
        });


        popUpMenu = (Button) findViewById(R.id.profileMenu);
        popUpMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMenu();
            }
        });
    }

    private void ShowMenu() {
        PopupMenu MenuPopUp = new PopupMenu(this, popUpMenu);
        MenuPopUp.getMenuInflater().inflate(R.menu.menu_popup, MenuPopUp.getMenu());
        MenuPopUp.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit_profile_button: {
                        list = new ArrayList<>();
                        Intent intent = new Intent(ViewSelfProfile.this, EditProfileActivity.class);
                        startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                        //startActivity(new Intent(getApplicationContext(),EditProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        break;
                    }
                    case R.id.accountSettings: {
                        startActivity(new Intent(ViewSelfProfile.this,AccountSettingsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        break;
                    }


                    case R.id.sign_out: {
                        signOut();
                        break;

                    }

                }
                return false;
            }
        });
        MenuPopUp.show();
    }

    public void signOut() {
        mAuth.signOut();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            mAuth.removeAuthStateListener(authListener);
        }
    }

    private void setupBottomNavigationView(){
        Log.d("MainScreenActivity", "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation( ViewSelfProfile.this,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}

