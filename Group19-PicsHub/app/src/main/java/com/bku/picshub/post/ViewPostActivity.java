package com.bku.picshub.post;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bku.picshub.BottomNavigationViewHelper;
import com.bku.picshub.MainScreenActivity;
import com.bku.picshub.R;
import com.bku.picshub.info.CommentInfo;
import com.bku.picshub.info.ImageUploadInfo;
import com.bku.picshub.info.LikedPostInfo;
import com.bku.picshub.info.UserInfo;
import com.bku.picshub.likedpost.LikedThisPostPopUp;
import com.bku.picshub.viewimage.CreateData;
import com.bku.picshub.viewimage.listener.OnClickShowImageListener;
import com.bku.picshub.viewimage.model.Profile;
import com.bku.picshub.viewimage.view.adapter.ProfileAdapter;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Welcome on 3/22/2018.
 */

public class ViewPostActivity extends AppCompatActivity implements OnClickShowImageListener {

    private static final String TAG = "View Post Activity";
    private static final int ACTIVITY_NUM = 1;
    public static final String Database_Path="All_User_Info_Database";
    public static final String Post_Path = "All_Post_Info_Database";
    public static final String Image_Post="Image_Of_Post_Database";
    public static final String Liked_Post="All_Liked_Post_Database";
    public static final String All_Image="All_Image_Uploads_Database";
    public static final String Comment_Database ="All_Comment_Info_Database";
    private TextView   mUsername, mTimestamp, mLikes,mComments;
    private EditText mCaption ,mNewComment;
    private Button mWriteComment;
    private ImageView  mBackArrow, mEllipses, mHeartRed, mHeartWhite, mProfileImage, mComment;
    private ProfileAdapter mProfileAdapter;
    private List<Profile> mProfileList;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2nd;
    DatabaseReference databaseReference3rd;
    DatabaseReference databaseReference4th;
    DatabaseReference databaseReference5th;
    private BottomNavigationViewEx bottomNavigationView;

    // Creating RecyclerView.
    RecyclerView recyclerView;
    RecyclerView oldComment;
    RecyclerView.LayoutManager mLayoutManager;
    CommentAdapter commentAdapter;

    // Creating RecyclerView.Adapter.


   // List<ImageUploadInfo> list = new ArrayList<>();
    private FirebaseAuth.AuthStateListener mAuthListener;
    String postId="";
    String userIdOfPost="";
    String userNameViewThisPost="";
    String avatarURLViewThisPost="";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpost);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        bottomNavigationView = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        mBackArrow = (ImageView) findViewById(R.id.backArrow);
        mCaption = (EditText) findViewById(R.id.image_caption);
        mUsername = (TextView) findViewById(R.id.username);
        mTimestamp = (TextView) findViewById(R.id.image_time_posted);
        mEllipses = (ImageView) findViewById(R.id.ivEllipses);
        mHeartRed = (ImageView) findViewById(R.id.image_heart_red);
        mHeartWhite = (ImageView) findViewById(R.id.image_heart);
        mProfileImage = (ImageView) findViewById(R.id.profile_photo);
        mLikes = (TextView) findViewById(R.id.image_likes);
        mComment = (ImageView) findViewById(R.id.speech_bubble);
        mComments = (TextView) findViewById(R.id.image_comments_link);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_profile);
        mNewComment = (EditText) findViewById(R.id.addComment);
        mWriteComment=(Button) findViewById(R.id.writeComment);
        oldComment = (RecyclerView) findViewById(R.id.oldComment);
        oldComment.setHasFixedSize(true);
        setupFirebaseAuth();

        // Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);

        // Setting RecyclerView layout as LinearLayout.
        recyclerView.setLayoutManager(new LinearLayoutManager(ViewPostActivity.this));

        mAuth=FirebaseAuth.getInstance();
        final String userId=mAuth.getCurrentUser().getUid().toString();

        Bundle extras=getIntent().getExtras();
        postId=extras.getString("postID");

        getValueCurrentUserViewThisPost(userId);
        databaseReference3rd = FirebaseDatabase.getInstance().getReference(Post_Path).child(postId);
        databaseReference3rd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCaption.setText(dataSnapshot.child("caption").getValue(String.class));
                userIdOfPost=dataSnapshot.child("accountId").getValue(String.class);
                mTimestamp.setText(dataSnapshot.child("timeStamp").getValue(String.class));
                getValueUser(userIdOfPost);
                if(!userIdOfPost.equals(userId)){
                    mEllipses.setVisibility(View.GONE);
                    mBackArrow.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        databaseReference2nd=FirebaseDatabase.getInstance().getReference(Image_Post).child(postId);

        databaseReference2nd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                final ArrayList<String> arrayImageURL=new ArrayList<>();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    final ImageUploadInfo imageUploadInfo = postSnapshot.getValue(ImageUploadInfo.class);
                    arrayImageURL.add(imageUploadInfo.getImageURL());
                }
                initData(userIdOfPost,postId,1,arrayImageURL);
                initViews();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        databaseReference4th=FirebaseDatabase.getInstance().getReference(Liked_Post).child(postId);

        databaseReference4th.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(userId)){
                    mHeartRed.setVisibility(View.VISIBLE);
                    mHeartWhite.setVisibility(View.INVISIBLE);

                }
                else
                {
                    mHeartRed.setVisibility(View.INVISIBLE);
                    mHeartWhite.setVisibility(View.VISIBLE);

                }
                mLikes.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mHeartWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LikedPostInfo likedPostInfo=new LikedPostInfo(userNameViewThisPost,avatarURLViewThisPost,userId);
                databaseReference4th.child(userId).setValue(likedPostInfo);
                mHeartRed.setVisibility(View.VISIBLE);
                mHeartWhite.setVisibility(View.INVISIBLE);

            }
        });

        mHeartRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference4th.child(userId).removeValue();
                mHeartRed.setVisibility(View.INVISIBLE);
                mHeartWhite.setVisibility(View.VISIBLE);

            }
        });

        //go to mainscreen
        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainScreenActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        mLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(ViewPostActivity.this,LikedThisPostPopUp.class);
                intent.putExtra("postId",postId);
                startActivity(intent);
            }
        });


        databaseReference5th=FirebaseDatabase.getInstance().getReference(All_Image).child(userId);

        mEllipses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowMenu();
            }
        });

      /*  mLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ViewPostActivity.this,LikedThisPostPopUp.class);
                intent.putExtra("postId",postId);
                startActivity(intent);
            }
        });*/

        oldComment=(RecyclerView)findViewById(R.id.oldComment);
        oldComment.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(ViewPostActivity.this);
        oldComment.setLayoutManager(mLayoutManager);
        oldComment.addItemDecoration(new DividerItemDecoration(ViewPostActivity.this,LinearLayoutManager.VERTICAL));
        oldComment.setItemAnimator(new DefaultItemAnimator());
        final DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference(Comment_Database).child(postId);
        updateComment(commentRef);

        mWriteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mNewComment.getText().toString().isEmpty()) {
                    final String thisUser = mAuth.getCurrentUser().getUid().toString();
                    FirebaseDatabase.getInstance().getReference(Database_Path).child(thisUser).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                            Date date = Calendar.getInstance().getTime();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss");
                            String timeStamp = simpleDateFormat.format(date);
                            CommentInfo commentInfo = new CommentInfo(userInfo.getUsername(), mNewComment.getText().toString(), userInfo.getAvatarURL(), timeStamp);
                            Map<String, Object> newContent = commentInfo.toMap();
                            String key = commentRef.child(postId).push().getKey();
                            commentRef.child(key).updateChildren(newContent);
                            updateComment(commentRef);
                            mNewComment.setText("");
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
        setupBottomNavigationView();



    }

    private void ShowMenu(){
        PopupMenu popupMenu =new PopupMenu(this,mEllipses);
        popupMenu.getMenuInflater().inflate(R.menu.menupost,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.edit_post:
                    {
                        mCaption.setEnabled(true);
                        mCaption.requestFocus();
                        mCaption.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                            @Override
                            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                if(actionId== EditorInfo.IME_ACTION_DONE){
                                    mCaption.setEnabled(false);
                                    return true;
                                }
                                return false;
                            }
                        });
                        // startActivity(new Intent(getApplicationContext(),ViewSelfProfile.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        break;
                    }
                    case R.id.delete_post:
                    {

                        databaseReference5th.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                                    if(singleSnapshot.child("postId").getValue().equals(postId)){
                                        databaseReference5th.child(singleSnapshot.getKey()).removeValue();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        databaseReference2nd.removeValue();
                        databaseReference3rd.removeValue();
                        databaseReference4th.removeValue();

                        startActivity(new Intent(getApplicationContext(),MainScreenActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        break;
                    }

                }
                return false;
            }
        });
        popupMenu.show();
    }



    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation( ViewPostActivity.this,bottomNavigationView);
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

    private void initData(String postId,String userId,long liked,ArrayList<String>ImageURL) {
        mProfileList = CreateData.createProfile(userId,postId,liked,ImageURL);
    }


    private void initViews() {
        mProfileAdapter = new ProfileAdapter(this, mProfileList, this);
        recyclerView.setAdapter(mProfileAdapter);
    }

    private void updateComment(final DatabaseReference commentRef){
        commentRef.orderByChild("timeStamp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> snapshotList = dataSnapshot.getChildren().iterator();
                List<CommentViewHolder> commentholderList = new ArrayList<CommentViewHolder>();
                while (snapshotList.hasNext()) {
                    CommentInfo commentInfo = snapshotList.next().getValue(CommentInfo.class);
                    commentholderList.add(new CommentViewHolder(commentInfo.getUsername(), commentInfo.getContent(), commentInfo.getAvatarURL()));
                }
                commentAdapter = new CommentAdapter(commentholderList, ViewPostActivity.this);
                oldComment.setAdapter(commentAdapter);
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClickShowImage(Profile profile, int positionImage) {
        startDownload(mProfileList.get(0).getImageList().get(positionImage));
    }

    private void getValueUser(String id){
        final String strUserId=id;
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path).child(strUserId);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUsername.setText(dataSnapshot.child("username").getValue(String.class));
                Glide.with(ViewPostActivity.this).load(dataSnapshot.child("avatarURL").getValue(String.class)).diskCacheStrategy(DiskCacheStrategy.RESULT).into(mProfileImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getValueCurrentUserViewThisPost(String userId){
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path).child(userId);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userNameViewThisPost=dataSnapshot.child("username").getValue(String.class);
                avatarURLViewThisPost=dataSnapshot.child("avatarURL").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void startDownload(String urlImage) {
        File folder = new File(Environment.getExternalStorageDirectory() + "/Download");
        if (!folder.exists()) {
            folder.mkdir();
        }
        DownloadManager mManager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request mRqRequest = new DownloadManager.Request(
                Uri.parse(urlImage));
        mRqRequest.setDescription("Image");
        mRqRequest.setDestinationInExternalPublicDir("/Download", "image.png");
        //mRqRequest.setDestinationUri(Uri.parse("/Download"));
        mRqRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        long idDownLoad=mManager.enqueue(mRqRequest);


    }

}
