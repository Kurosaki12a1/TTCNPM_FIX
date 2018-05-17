package com.bku.picshub.newfeed;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bku.picshub.R;
import com.bku.picshub.info.CommentInfo;
import com.bku.picshub.info.ImageUploadInfo;
import com.bku.picshub.info.LikedPostInfo;
import com.bku.picshub.info.PostInfo;
import com.bku.picshub.info.UserInfo;
import com.bku.picshub.likedpost.LikedThisPostPopUp;
import com.bku.picshub.post.CommentAdapter;
import com.bku.picshub.post.CommentViewHolder;
import com.bku.picshub.post.UpLoadPostAdapter;
import com.bku.picshub.post.ViewPostActivity;
import com.bku.picshub.viewimage.CreateData;
import com.bku.picshub.viewimage.listener.OnClickShowImageListener;
import com.bku.picshub.viewimage.model.Profile;
import com.bku.picshub.viewimage.view.adapter.ImageNewFeedAdapter;
import com.bku.picshub.viewimage.view.adapter.ProfileAdapter;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Welcome on 5/12/2018.
 */

public class NewFeedAdapter  extends  RecyclerView.Adapter<NewFeedAdapter.ViewHolder> {
    static Context context;
    ArrayList<PostInfo> lstPost;

    public static final String Database_Path = "All_User_Info_Database";
    public static final String Post_Path = "All_Post_Info_Database";
    public static final String Image_Post = "Image_Of_Post_Database";
    public static final String Liked_Post = "All_Liked_Post_Database";
    public static final String All_Image = "All_Image_Uploads_Database";
    public static final String Comment_Database = "All_Comment_Info_Database";
    String userId;
    FirebaseAuth mAuth;
    private String userNameViewThisPost = "";
    private String avatarURLViewThisPost = "";


    public NewFeedAdapter(Context context, ArrayList<PostInfo> lst) {
        this.context = context;
        this.lstPost = new ArrayList<>(lst);
        this.lstPost = lst;
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        getValueCurrentUserViewThisPost(userId);
    }

    @Override
    public NewFeedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_new_feed_post_item, parent, false);

        NewFeedAdapter.ViewHolder viewHolder = new NewFeedAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final NewFeedAdapter.ViewHolder holder, int position) {
        final int nPostition = position;
        PostInfo postInfo = lstPost.get(nPostition);
        final String postID = postInfo.getPostId();
        final String userIdOfPost = postInfo.getAccountId();
        holder.getValueOfPost(postID);
        DatabaseReference databaseReference2nd = FirebaseDatabase.getInstance().getReference(Image_Post).child(postID);
        databaseReference2nd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ArrayList<String> arrayImageURL = new ArrayList<String>();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ImageUploadInfo imageUploadInfo = postSnapshot.getValue(ImageUploadInfo.class);
                    arrayImageURL.add(imageUploadInfo.getImageURL());
                }
                holder.initData(userIdOfPost, postID, 1, arrayImageURL);
                holder.initViews();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final DatabaseReference databaseReference4th = FirebaseDatabase.getInstance().getReference(Liked_Post).child(postID);

        databaseReference4th.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userId)) {
                    holder.mHeartRed.setVisibility(View.VISIBLE);
                    holder.mHeartWhite.setVisibility(View.INVISIBLE);

                } else {
                    holder.mHeartRed.setVisibility(View.INVISIBLE);
                    holder.mHeartWhite.setVisibility(View.VISIBLE);

                }
                holder.mLikes.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.mHeartWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LikedPostInfo likedPostInfo = new LikedPostInfo(userNameViewThisPost, avatarURLViewThisPost,userId);
                databaseReference4th.child(userId).setValue(likedPostInfo);

                holder.mHeartRed.setVisibility(View.VISIBLE);
                holder.mHeartWhite.setVisibility(View.INVISIBLE);

            }
        });

        holder.mHeartRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference4th.child(userId).removeValue();
//
                holder.mHeartRed.setVisibility(View.INVISIBLE);
                holder.mHeartWhite.setVisibility(View.VISIBLE);

            }
        });


        holder.mLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LikedThisPostPopUp.class);
                intent.putExtra("postId", postID);
                context.startActivity(intent);
            }
        });
        final DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference(Comment_Database).child(postID);
        holder.updateComment(commentRef);

        holder.mNewComment.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (!holder.mNewComment.getText().toString().isEmpty()) {
                        final String thisUser = mAuth.getCurrentUser().getUid().toString();
                        FirebaseDatabase.getInstance().getReference(Database_Path).child(thisUser).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                                Date date = Calendar.getInstance().getTime();
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd/ HH:mm:ss");
                                String timeStamp = simpleDateFormat.format(date);
                                CommentInfo commentInfo = new CommentInfo(userInfo.getUsername(), holder.mNewComment.getText().toString(), userInfo.getAvatarURL(), timeStamp);
                                Map<String, Object> newContent = commentInfo.toMap();
                                String key = commentRef.child(postID).push().getKey();
                                commentRef.child(key).updateChildren(newContent);
                                holder.updateComment(commentRef);
                                holder.mNewComment.setText("");
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        return true;
                    }
                }
                return false;
            }
        });

        holder.mTimestamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewPostActivity.class);
                intent.putExtra("postID", postID);
                context.startActivity(intent);
            }
        });

        holder.mSpeechWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mCommentLayout.setVisibility(View.GONE);
                holder.mSpeechWhite.setVisibility(View.INVISIBLE);
                holder.mSpeechBlue.setVisibility(View.VISIBLE);
            }
        });

        holder.mSpeechBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mCommentLayout.setVisibility(View.VISIBLE);
                holder.mSpeechBlue.setVisibility(View.INVISIBLE);
                holder.mSpeechWhite.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstPost.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mUsername, mTimestamp, mLikes, mComments;
        public EditText mCaption, mNewComment;
        public ImageView mEllipses, mHeartRed, mHeartWhite, mProfileImage, mSpeechWhite, mSpeechBlue;
        public RelativeLayout mCommentLayout;
        public ImageNewFeedAdapter imageNewFeedAdapter;
        public List<Profile> mProfileList;
        public RecyclerView recyclerView;
        public RecyclerView oldComment;
        public RecyclerView.LayoutManager mLayoutManager;
        public CommentAdapter commentAdapter;

        private void initData(String postId, String userId, long liked, ArrayList<String> ImageURL) {
            mProfileList = CreateData.createProfile(userId, postId, liked, ImageURL);
        }


        private void initViews() {
            imageNewFeedAdapter = new ImageNewFeedAdapter(context, mProfileList, new OnClickShowImageListener() {
                @Override
                public void onClickShowImage(Profile profile, int positionImage) {
                    startDownload(mProfileList.get(0).getImageList().get(positionImage));
                }
            });
            recyclerView.setAdapter(imageNewFeedAdapter);
        }

        private void getValueOfPost(final String postId) {
            DatabaseReference databaseReference3rd = FirebaseDatabase.getInstance().getReference(Post_Path).child(postId);
            databaseReference3rd.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mCaption.setText(dataSnapshot.child("caption").getValue(String.class));
                    String userIdOfPost = dataSnapshot.child("accountId").getValue(String.class);
                    mTimestamp.setText(dataSnapshot.child("timeStamp").getValue(String.class));
                    getValueUserOfPost(userIdOfPost);
                    //setImageOfPost(userIdOfPost,postId);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }



        private void getValueUserOfPost(String id) {
            final String strUserId = id;
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path).child(strUserId);

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mUsername.setText(dataSnapshot.child("username").getValue(String.class));
                    Glide.with(context).load(dataSnapshot.child("avatarURL").getValue(String.class)).into(mProfileImage);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        public ViewHolder(View itemView) {
            super(itemView);

            mCaption = (EditText) itemView.findViewById(R.id.image_caption);
            mUsername = (TextView) itemView.findViewById(R.id.username);
            mTimestamp = (TextView) itemView.findViewById(R.id.image_time_posted);
            mEllipses = (ImageView) itemView.findViewById(R.id.ivEllipses);
            mHeartRed = (ImageView) itemView.findViewById(R.id.image_heart_red);
            mHeartWhite = (ImageView) itemView.findViewById(R.id.image_heart);
            mProfileImage = (ImageView) itemView.findViewById(R.id.profile_photo);
            mSpeechWhite = (ImageView) itemView.findViewById(R.id.speech_bubble);
            mSpeechBlue = (ImageView) itemView.findViewById(R.id.speech_bubble_blue);
            mLikes = (TextView) itemView.findViewById(R.id.image_likes);
            mCommentLayout = (RelativeLayout) itemView.findViewById(R.id.comments);
            mComments = (TextView) itemView.findViewById(R.id.image_comments_link);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view_profile);
            mNewComment = (EditText) itemView.findViewById(R.id.addComment);
            oldComment = (RecyclerView) itemView.findViewById(R.id.oldComment);
            oldComment.setHasFixedSize(true);

            // Setting RecyclerView size true.
            recyclerView.setHasFixedSize(true);

            // Setting RecyclerView layout as LinearLayout.
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            oldComment.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(context);
            oldComment.setLayoutManager(mLayoutManager);
            oldComment.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
            oldComment.setItemAnimator(new DefaultItemAnimator());
        }

        private void updateComment(final DatabaseReference commentRef) {
            commentRef.orderByChild("timeStamp").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterator<DataSnapshot> snapshotList = dataSnapshot.getChildren().iterator();
                    List<CommentViewHolder> commentholderList = new ArrayList<CommentViewHolder>();
                    while (snapshotList.hasNext()) {
                        CommentInfo commentInfo = snapshotList.next().getValue(CommentInfo.class);
                        commentholderList.add(new CommentViewHolder(commentInfo.getUsername(), commentInfo.getContent(), commentInfo.getAvatarURL()));
                    }
                    commentAdapter = new CommentAdapter(commentholderList, context);
                    oldComment.setAdapter(commentAdapter);
                    commentAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void getValueCurrentUserViewThisPost(String userId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path).child(userId);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userNameViewThisPost = dataSnapshot.child("username").getValue(String.class);
                avatarURLViewThisPost = dataSnapshot.child("avatarURL").getValue(String.class);
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
        DownloadManager mManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request mRqRequest = new DownloadManager.Request(
                Uri.parse(urlImage));
        mRqRequest.setDescription("Image");
        mRqRequest.setDestinationInExternalPublicDir("/Download", "image.png");
        //mRqRequest.setDestinationUri(Uri.parse("/Download"));
        mRqRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        long idDownLoad = mManager.enqueue(mRqRequest);

    }

}
