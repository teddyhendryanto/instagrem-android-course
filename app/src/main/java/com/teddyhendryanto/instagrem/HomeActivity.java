package com.teddyhendryanto.instagrem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.parse.ParseUser;

public class HomeActivity extends AppCompatActivity {

    Button btn_logout;
    Button btn_user_list;

    boolean clicked = false;
    int duration = 2500; // TOAST LONG LENGTH in MS
    Long timeClicked;

    // AdMob
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btn_logout = (Button) findViewById(R.id.btn_logout);
        btn_user_list = (Button) findViewById(R.id.btn_user_list);

        // AdMob
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    @Override
    public void onBackPressed() {
        if(clicked){
            if((System.currentTimeMillis() - timeClicked) < duration){
                finish();
            }
            else{
                Toast.makeText(this, "Press Back Again to Exit", Toast.LENGTH_SHORT).show();
                timeClicked = System.currentTimeMillis();
            }
        }
        else{
            clicked = true;
            Toast.makeText(this, "Press Back Again to Exit", Toast.LENGTH_SHORT).show();
            timeClicked = System.currentTimeMillis();
        }
    }

    // CREATE MENU DOT di HEADER ==============================================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // LayoutInflater = ambil semua layout, dipilih dan di lempar ke layout yang diingini
        // MenuInflater = ambil semua menu, dipilih dan di lempar ke menu yang diingini
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.btn_upload){
            Intent intent = new Intent(getApplicationContext(),UploadActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.btn_user_list){
            Intent intent = new Intent(getApplicationContext(),UserListActivity.class);
            startActivity(intent);
        }
        else{
            ParseUser.logOut();
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    // END OF CREATE MENU DOT di HEADER ==============================================================

}
