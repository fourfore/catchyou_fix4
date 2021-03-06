package com.eoss.application.catchme_fix4.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.eoss.application.catchme_fix4.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);
    }

    //
    public void onClickLogin(View view){
        progress = ProgressDialog.show(this, "Now loading",
                "Please wait for a while.", true);
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, Arrays.asList("email", "user_photos", "public_profile", "user_friends")
                ,new LogInCallback() {
                    ParseUser user;
                    @Override
                    public void done(ParseUser user, ParseException err) {
                        if (user == null) {
                            Log.d("parse-->","cancel");
                            progress.dismiss();
                        } else if (user.isNew()) {
                            Log.d("parse-->","User Already logged up through facebook! new");
                            user.getCurrentUser();
                            if(user != null){
                                Intent intent = new Intent(LoginActivity.this,AppActivity.class);
                                startActivity(intent);
                                progress.dismiss();
                                finish();
                            }
                        } else {
                            Log.d("parse-->","User Already logged up through facebook!");
                            user.getCurrentUser();
                            if(user != null){
                                Log.d("parse-->","Register");
                                Intent intent = new Intent(LoginActivity.this,AppActivity.class);
                                startActivity(intent);
                                progress.dismiss();
                                finish();
                            }

                        }
                    }

                });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }
}
