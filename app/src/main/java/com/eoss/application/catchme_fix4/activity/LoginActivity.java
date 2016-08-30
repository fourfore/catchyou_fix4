package com.eoss.application.catchme_fix4.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.eoss.application.catchme_fix4.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    public void onClickLogin(View view){
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, Arrays.asList("email", "user_photos", "public_profile", "user_friends")
                ,new LogInCallback() {
                    ParseUser user;
                    @Override
                    public void done(ParseUser user, ParseException err) {
                        if (user == null) {
                            Log.d("parse-->","cancel");
                        } else if (user.isNew()) {
                            Log.d("parse-->","User Already logged up through facebook! new");
                            user.getCurrentUser();
                            if(user != null){
                                //

                                //Code get user profile from face book
                                Bundle params = new Bundle();
                                params.putString("fields", "id,email,gender,cover,picture.type(large),first_name,last_name");
                                new GraphRequest(AccessToken.getCurrentAccessToken(), "me", params, HttpMethod.GET,
                                        new GraphRequest.Callback() {
                                            @Override
                                            public void onCompleted(GraphResponse response) {
                                                if (response != null) {
                                                    try {
                                                        ParseUser currentUser = ParseUser.getCurrentUser();
                                                        JSONObject data = response.getJSONObject();
                                                        Log.d("foretest",data.toString());
                                                        if (data.has("picture")) {
                                                            String profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                                            currentUser = ParseUser.getCurrentUser();
                                                            currentUser.put("profilePicUrl",profilePicUrl);
                                                            currentUser.saveInBackground();
                                                            Log.d("ProfilePic URL-> ", profilePicUrl);

                                                        }
                                                        if (data.has("gender")) {
                                                            String gender = data.getString("gender");
                                                            currentUser.put("gender",gender);
                                                            currentUser.saveInBackground();
                                                            Log.d("gender-> ", gender);

                                                        }
                                                        //age
//                                if (data.has("birthday")) {
//                                    String birthday = data.getString("birthday");
//                                    currentUser.put("age",gender);
//                                    currentUser.saveInBackground();
//                                }
                                                        //facebook name
                                                        if (data.has("first_name")) {
                                                            String faceName = data.getString("first_name");
                                                            currentUser.put("faceName",faceName);
                                                            currentUser.saveInBackground();

                                                        }


                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        }).executeAsync();
                                //
                                Intent intent = new Intent(LoginActivity.this,AppActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        } else {

                            Log.d("parse-->","User Already logged up through facebook!");
                            user.getCurrentUser();
                            if(user != null){
                                Log.d("parse-->","Register");
                                Intent intent = new Intent(LoginActivity.this,AppActivity.class);
                                startActivity(intent);
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
