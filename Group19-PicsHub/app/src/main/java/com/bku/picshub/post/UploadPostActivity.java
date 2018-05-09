package com.bku.picshub.post;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bku.picshub.MainScreenActivity;
import com.bku.picshub.R;
import com.bku.picshub.info.ImageUploadInfo;
import com.bku.picshub.info.PostInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Welcome on 3/22/2018.
 */

public class UploadPostActivity extends AppCompatActivity {


    public static final String Database_Path = "All_Image_Uploads_Database";
    public static final String Post_Path="All_Post_Info_Database";
    public static final String Image_Of_Post="Image_Of_Post_Database";
    public static final String Liked_Path="All_Liked_Post_Database";
    String Storage_Path = "Image Store";
    // Image request code for onActivityResult() .
    int Image_Request_Code = 7;

    // Creating StorageReference and DatabaseReference object.
    StorageReference storageReference;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2nd;
    DatabaseReference databaseReference3rd;
    DatabaseReference databaseReference4th;
    FirebaseAuth mAuth;

    Uri FilePathUri;
    ArrayList<Uri> LFilePathUri;

    ProgressDialog progressDialog ;

    private ImageView imagePost,backArrow;
    private EditText Caption;
    private TextView confirm;

    String userId="";
    String imageUrl="";
    String postID="";

    boolean isChooseImage=false;
    ProgressDialog progressDialog2nd ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadpost);

        progressDialog = new ProgressDialog(UploadPostActivity.this);


        mAuth=FirebaseAuth.getInstance();
        userId=mAuth.getCurrentUser().getUid().toString();

        imagePost=(ImageView)findViewById(R.id.imageShare);
        Caption=(EditText)findViewById(R.id.caption);
        confirm=(TextView)findViewById(R.id.tvShare);
        backArrow=(ImageView)findViewById(R.id.ivBackArrow);

        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
        databaseReference2nd=FirebaseDatabase.getInstance().getReference(Post_Path);
        databaseReference3rd=FirebaseDatabase.getInstance().getReference(Image_Of_Post);
        databaseReference4th=FirebaseDatabase.getInstance().getReference(Liked_Path);

        postID=databaseReference3rd.push().getKey();
        storageReference = FirebaseStorage.getInstance().getReference();

        LFilePathUri=new ArrayList<>();

        progressDialog2nd=new ProgressDialog(UploadPostActivity.this);
        // Creating intent.
        Intent intent = new Intent();

        // Setting intent type as image to select image from phone storage.
        intent.setType("image/*");
        //this will make select multi image
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Please Select Picture You Want Post"), Image_Request_Code);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isChooseImage)
                UploadImageFileToFirebaseStorage();
                else{
                    Toast.makeText(UploadPostActivity.this, "You forget select it so please click back Arrow", Toast.LENGTH_SHORT).show();
                }

            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UploadPostActivity.this, MainScreenActivity.class);
                startActivity(intent);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null ) {
            isChooseImage=true;
           //multi Upload Image
            if(data.getClipData()!=null){
                isChooseImage=true;
                int totalItemSelected=data.getClipData().getItemCount();
                for (int i=0; i<totalItemSelected;i++){
                    Uri uri=data.getClipData().getItemAt(i).getUri();
                    LFilePathUri.add(uri);

                }
                try {

                    // Getting selected image into Bitmap.
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), LFilePathUri.get(0));

                    // Setting up bitmap selected image into ImageView.
                    imagePost.setImageBitmap(bitmap);

                    // After selecting image change choose button above text.


                } catch (IOException e) {

                    e.printStackTrace();
                }
            }

            else if (data.getData() != null) {

                FilePathUri = data.getData();

                try {

                    // Getting selected image into Bitmap.
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);

                    // Setting up bitmap selected image into ImageView.
                    imagePost.setImageBitmap(bitmap);

                    // After selecting image change choose button above text.


                } catch (IOException e) {

                    e.printStackTrace();
                }
            }

        }
        else{
            Toast.makeText(this,"User did not choose any picture to upload",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(UploadPostActivity.this, MainScreenActivity.class);
            startActivity(intent);
        }
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    // Creating UploadImageFileToFirebaseStorage method to upload image on storage.
    public void UploadImageFileToFirebaseStorage() {

        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {

            // Setting progressDialog Title.
            progressDialog.setTitle("Image is Uploading...");

            // Showing progressDialog.
          //  progressDialog.show();

            // Creating second StorageReference.
            StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Getting image name from EditText and store into string variable.
                            //       String TempImageName = ImageName.getText().toString().trim();

                            // Hiding the progressDialog after done uploading.
                           // progressDialog.dismiss();

                            // Showing toast message after done uploading.
                            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();

                            // Getting image upload ID.
                            String ImageUploadId = databaseReference.push().getKey();

                            String txtCaption=Caption.getText().toString();
                            //get UserId
                            mAuth= FirebaseAuth.getInstance();

                          //  String userId=mAuth.getCurrentUser().getUid().toString();

                            imageUrl=taskSnapshot.getDownloadUrl().toString();

                          //  @SuppressWarnings("VisibleForTests")
                            ImageUploadInfo imageUploadInfo = new ImageUploadInfo(taskSnapshot.getDownloadUrl().toString(),0,txtCaption,userId,postID );

                            // Getting image upload ID.
                            //String ImageUploadId = databaseReference.push().getKey();

                            // Adding image upload id s child element into databaseReference.
                            databaseReference.child(userId).child(ImageUploadId).setValue(imageUploadInfo);

                            //Add PostId and PostDatabase


                            databaseReference3rd.child(postID).child(ImageUploadId).setValue(imageUploadInfo);



                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.
                            progressDialog.dismiss();

                            // Showing exception erro message.
                            Toast.makeText(UploadPostActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            // Setting progressDialog Title.
                           // progressDialog.setTitle("Image is Uploading...");
                            progressDialog.setTitle("Upload photo is "+progress+" % done");
                            if(progress==100){
                                databaseReference4th.child(postID).setValue("Like of this post");
                                databaseReference4th.child(postID).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        PostInfo postInfo = new PostInfo(postID, userId, String.valueOf(dataSnapshot.getChildrenCount()), Caption.getText().toString());
                                        databaseReference2nd.child(postID).setValue(postInfo);
                                        Intent intent = new Intent(UploadPostActivity.this, ViewPostActivity.class);
                                        intent.putExtra("postID",postID);
                                        intent.putExtra("Caption",Caption.getText().toString());

                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        }
                    });
        }
        else if(LFilePathUri!=null){


            for(int i=0;i<LFilePathUri.size();i++) {
                StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(LFilePathUri.get(i)));
                // Adding addOnSuccessListener to second StorageReference.

                final int finalI = i;
                storageReference2nd.putFile(LFilePathUri.get(i))
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                // Getting image name from EditText and store into string variable.
                                //       String TempImageName = ImageName.getText().toString().trim();

                                // Hiding the progressDialog after done uploading.
                                // progressDialog.dismiss();

                                // Showing toast message after done uploading.
                                Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();

                                // Getting image upload ID.
                                String ImageUploadId = databaseReference.push().getKey();

                                String txtCaption = Caption.getText().toString();
                                //get UserId
                                mAuth = FirebaseAuth.getInstance();

                                //  String userId=mAuth.getCurrentUser().getUid().toString();

                                imageUrl = taskSnapshot.getDownloadUrl().toString();

                                //  @SuppressWarnings("VisibleForTests")
                                ImageUploadInfo imageUploadInfo = new ImageUploadInfo(taskSnapshot.getDownloadUrl().toString(), 0, txtCaption, userId,postID);

                                // Getting image upload ID.
                                //String ImageUploadId = databaseReference.push().getKey();

                                // Adding image upload id s child element into databaseReference.
                                databaseReference.child(userId).child(ImageUploadId).setValue(imageUploadInfo);


                                //Add PostId and PostDatabase
                                databaseReference4th.child(postID).setValue("Like of this post");
                                databaseReference4th.child(postID).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        PostInfo postInfo = new PostInfo(postID, userId, String.valueOf(dataSnapshot.getChildrenCount()), Caption.getText().toString());
                                        databaseReference2nd.child(postID).setValue(postInfo);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                             /*   PostInfo postInfo = new PostInfo(postID, userId, numOfLike, Caption.getText().toString());
                                databaseReference2nd.child(postID).setValue(postInfo);*/
                                databaseReference3rd.child(postID).child(ImageUploadId).setValue(imageUploadInfo);


                            }
                        })
                        // If something goes wrong .
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {

                                // Hiding the progressDialog.
                                progressDialog.dismiss();

                                // Showing exception erro message.
                                Toast.makeText(UploadPostActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        })

                        // On progress change upload time.
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                // Setting progressDialog Title.
                               // int count=i;
                                progressDialog.setTitle("Upload photo is "+progress+" % done");
                                if(progress==100 &&finalI==LFilePathUri.size()-1){
                                    progressDialog.dismiss();
                                    databaseReference4th.child(postID).setValue("Like of this post");
                                    databaseReference4th.child(postID).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            PostInfo postInfo = new PostInfo(postID, userId, String.valueOf(dataSnapshot.getChildrenCount()), Caption.getText().toString());
                                            databaseReference2nd.child(postID).setValue(postInfo);
                                            Intent intent = new Intent(UploadPostActivity.this, ViewPostActivity.class);
                                            intent.putExtra("postID",postID);
                                            intent.putExtra("Caption",Caption.getText().toString());

                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }

                            }
                        });
            }
        }
        else {

            Toast.makeText(UploadPostActivity.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }





}
