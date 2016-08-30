package com.eoss.application.catchme_fix4.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eoss.application.catchme_fix4.R;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    //private Map<String, String> facebookInfo = new HashMap<String, String>();
    private RecyclerView recyclerView;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
//        recyclerView=(RecyclerView)view.findViewById(R.id.profile_RecyclerView);
//
//        LinearLayoutManager llm = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(llm);
//        recyclerView.setHasFixedSize(true);
//        ProfileAdapter adapter = new ProfileAdapter(ParseUser.getCurrentUser());
//        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
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

                                    Log.d("ProfilePic URL-> ", profilePicUrl);

                                }
                                if (data.has("gender")) {
                                    String gender = data.getString("gender");
                                    currentUser.put("gender",gender);

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


                                }
                                currentUser.saveInBackground(new SaveCallback() {
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            // Saved successfully.
                                            Log.d("Saved successfully", "User update saved!");
                                            recyclerView=(RecyclerView)getView().findViewById(R.id.profile_RecyclerView);

                                            LinearLayoutManager llm = new LinearLayoutManager(getContext());
                                            recyclerView.setLayoutManager(llm);
                                            recyclerView.setHasFixedSize(true);
                                            ProfileAdapter adapter = new ProfileAdapter(ParseUser.getCurrentUser(),getContext());
                                            recyclerView.setAdapter(adapter);
                                        } else {
                                            // The save failed.
                                            Log.d("TAG", "User update error: " + e);
                                        }
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).executeAsync();
//        recyclerView=(RecyclerView)getView().findViewById(R.id.profile_RecyclerView);
//
//        LinearLayoutManager llm = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(llm);
//        recyclerView.setHasFixedSize(true);
//        ProfileAdapter adapter = new ProfileAdapter(ParseUser.getCurrentUser(),getContext());
//        recyclerView.setAdapter(adapter);
        Log.d("OnStart","OnStart");


    }





}
