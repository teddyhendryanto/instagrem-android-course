package com.teddyhendryanto.instagrem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {

    EditText username;
    EditText password;

    Button btn;
    TextView flagStatus;

    String status = "register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        // configure parse
        parse_init();

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        btn = (Button) findViewById(R.id.btn);
        flagStatus = (TextView) findViewById(R.id.flagStatus);

        if(ParseUser.getCurrentUser() != null){
            go_to_home();
        }

        // first time run
        status_init();

        flagStatus.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                status_init();
            }
        });

        btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status == "login"){
                    login();
                }
                else{
                    sign_up();
                }
            }
        });

    }

    private void status_init() {
        if(status == "login"){
            status = "register";
            btn.setText("Register");
            flagStatus.setText("Login Here.");
        }
        else{
            status = "login";
            btn.setText("Login");
            flagStatus.setText("Register Here.");
        }
    }

    private void parse_init(){
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("eca0ea755c312827a86fdc36068e534738c1299e")
                .server("http://ec2-52-14-193-130.us-east-2.compute.amazonaws.com:80/parse")
                .build()
        );
    }

    private void login() {
        String user_name = username.getText().toString();
        String pass_word = password.getText().toString();

        ParseUser.logInInBackground(user_name, pass_word, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e == null){
                    Toast.makeText(MainActivity.this, "Selamat Datang "+user.getUsername(), Toast.LENGTH_SHORT).show();
                    go_to_home();
                }
                else{
                    Toast.makeText(MainActivity.this, "Gagal Login. Error =" +e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void go_to_home() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }

    public void sign_up(){
        ParseUser user = new ParseUser();
        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Toast.makeText(MainActivity.this, "Berhasil.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "Gagal daftar. Error =" +e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    // ================================================
    // Cara input dan query standart
    // ================================================
//        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Crud");

//        ParseObject obj = new ParseObject("Crud");
//        obj.put("name","Teddy Hendryanto");
//        obj.put("age","26");
//        obj.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if(e == null){
//                    Toast.makeText(MainActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Toast.makeText(MainActivity.this, "Gagal "+e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }
}
