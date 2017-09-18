package com.teddyhendryanto.instagrem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class UserFeedActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        Intent intent = getIntent();

        username = intent.getStringExtra("username");

        ParseQuery <ParseObject> query = new ParseQuery("Pictures");
        query.whereEqualTo("username",username);
        query.orderByDescending("created_at");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    Log.i("obj ",objects.toString());
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(linearLayoutManager);

                    MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(objects, getApplicationContext());
                    recyclerView.setAdapter(adapter);
                }
                else{
                    Toast.makeText(UserFeedActivity.this, "Error."+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
