package com.bku.picshub.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.bku.picshub.BottomNavigationViewHelper;
import com.bku.picshub.R;
import com.bku.picshub.info.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Welcome on 4/8/2018.
 */

public class SearchActivity extends AppCompatActivity {

    public static final String Database_Path="All_User_Info_Database";
    private static final int ACTIVITY_NUM = 1;
    private EditText mSearch;
    private RecyclerView mListView;
    private List<UserInfo>  mListUserInfo;
    private ViewUserAdapter mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mSearch=(EditText)findViewById(R.id.search);
        mListView=(RecyclerView)findViewById(R.id.recyclerView);

        mListView.setHasFixedSize(true);
        mListView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        hideSoftKeyboard();
        setupBottomNavigationView();
        initTextListener();
    }

    private void initTextListener(){


        mListUserInfo = new ArrayList<>();

        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
              /*  String text = mSearch.getText().toString().toLowerCase(Locale.getDefault());
                searchForMatch(text);*/
            }

            @Override
            public void afterTextChanged(Editable s) {

                String text = mSearch.getText().toString()  ;
                searchForMatch(text);
            }
        });
    }

    private void searchForMatch(final String keyword) {

        mListUserInfo.clear();
        //update the users list view
        if (keyword.length() == 0) {

        } else {

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                       // Toast.makeText(SearchActivity.this, postSnapshot.child("username").getValue().toString() + "and " +keyword, Toast.LENGTH_SHORT).show();
                        if (postSnapshot.child("username").getValue().toString().contains(keyword)) {
                         //   Toast.makeText(SearchActivity.this, "FINALLY WE CAn", Toast.LENGTH_SHORT).show();
                            mListUserInfo.add(postSnapshot.getValue(UserInfo.class));

                        }

                    }
                    updateUsersList();
                   // updateUsersList();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

           /* Query query = databaseReference
                    .orderByChild("username").equalTo(keyword);
            Query query1 = databaseReference

                    .orderByChild("username");
            Toast.makeText(SearchActivity.this,query1.toString(),Toast.LENGTH_LONG).show();
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                        mListUserInfo.add(singleSnapshot.getValue(UserInfo.class));
                        //update the users list view

                        updateUsersList();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }*/
        }

    }

    private void updateUsersList(){
     //   Log.d(TAG, "updateUsersList: updating users list");

     //   mAdapter.notifyDataSetChanged();
        mAdapter = new ViewUserAdapter(SearchActivity.this, mListUserInfo);
        mAdapter.notifyDataSetChanged();

        mListView.setAdapter(mAdapter);
        mListView.invalidate();



    }
    private void hideSoftKeyboard(){
        if(getCurrentFocus() != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void setupBottomNavigationView(){
        Log.d("MainScreenActivity", "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation( SearchActivity.this,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


}
