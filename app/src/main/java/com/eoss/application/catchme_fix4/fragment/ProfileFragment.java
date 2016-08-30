package com.eoss.application.catchme_fix4.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //********************Get facebook profile detail (name, pic, sex, age)
//        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
//                new GraphRequest.GraphJSONObjectCallback() {
//                    @Override
//                    public void onCompleted(
//                            JSONObject object,
//                            GraphResponse response) {
//                        // Application code
//                        //object.toString();
//                        Log.d("foretest",object.toString());
//                    }
//                });
//        Bundle parameters = new Bundle();
//        parameters.putString("fields", "id,email,gender,cover,picture.type(large)");
//        request.setParameters(parameters);
//        request.executeAsync();

        Bundle params = new Bundle();
        params.putString("fields", "id,first_name,email,gender,cover,picture.type(large)");
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


        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ParseUser currentUser = ParseUser.getCurrentUser();
        TextView textView = (TextView) view.findViewById(R.id.user_profile_name);
        textView.setText(currentUser.getString("faceName"));
        if(currentUser.getString("gender") != null) {
            textView = (TextView) view.findViewById(R.id.user_gender);
            textView.setText("SEX : " + currentUser.getString("gender").toUpperCase());
        }
        ImageView imageView = (ImageView)view.findViewById(R.id.header_cover_image);
        Picasso.with(inflater.getContext()).load(ParseUser.getCurrentUser().getString("profilePicUrl")).into(imageView);
        imageView = (ImageView)view.findViewById(R.id.user_profile_photo);
        Picasso.with(inflater.getContext()).load(ParseUser.getCurrentUser().getString("profilePicUrl")).into(imageView);
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
//        ParseUser currentUser = ParseUser.getCurrentUser();
//        TextView textView = (TextView) getView().findViewById(R.id.user_profile_name);
//        textView.setText(currentUser.getString("faceName"));
//        if(currentUser.getString("gender") != null) {
//            textView = (TextView) getView().findViewById(R.id.user_gender);
//            textView.setText("SEX : " + currentUser.getString("gender").toUpperCase());
//        }
//        ImageView imageView = (ImageView)getView().findViewById(R.id.header_cover_image);
//        Picasso.with(inflater.getContext()).load(ParseUser.getCurrentUser().getString("profilePicUrl")).into(imageView);
//        imageView = (ImageView)view.findViewById(R.id.user_profile_photo);
//        Picasso.with(inflater.getContext()).load(ParseUser.getCurrentUser().getString("profilePicUrl")).into(imageView);
    }

}
