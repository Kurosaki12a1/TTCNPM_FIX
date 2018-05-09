package com.bku.picshub;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.bku.picshub.account.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by Welcome on 2/28/2018.
 */

public class MainScreenActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private static final int ACTIVITY_NUM = 0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_1);
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
      /*  private void ShowMenu(){
            PopupMenu popupMenu =new PopupMenu(this,menuPopUp);
            popupMenu.getMenuInflater().inflate(R.menu.menu_popup,popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.edit_profile_button:
                        {

                           // startActivity(new Intent(getApplicationContext(),ViewSelfProfile.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            break;
                        }
                        case R.id.accountSettings:
                        {
                            //Intent intent = new Intent(MainScreenActivity.this, MainLoginActivity.class);
                            //finish();
                           // startActivity(new Intent(getApplicationContext(),MainLoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            break;
                        }

                        case R.id.yourPhoto:
                        {
                            //Intent intent = new Intent(MainScreenActivity.this, MainActivity.class);
                            //finish();
                          //  startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            break;
                        }

                        case R.id.sign_out:{
                            signOut();

                        }

                    }
                    return false;
                }
            });
            popupMenu.show();
        }*/
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
