package com.eoss.application.catchme_fix4.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.eoss.application.catchme_fix4.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeContainer;
    private LinearLayoutManager linearLayoutManager;
    private ProfileAdapter adapter;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
//        toolbar.setTitle("Profile");
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }




    @Override
    public void onStart(){
        super.onStart();
        //***********Push data to Fragment
        pushDatatoFragment();

        //***********Setup swipeContainer
        // Setup refresh listener which triggers new data loading
        swipeContainer = (SwipeRefreshLayout) getView().findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pushDatatoFragment();
                Log.d("RefreshListener2","RefreshListener");
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        //Log.d("OnStart","OnStart");


    }

    public void setUpAdapter(ParseUser data)
    {
        recyclerView=(RecyclerView)getView().findViewById(R.id.profile_RecyclerView);

        linearLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new ProfileAdapter(data,getContext());
        adapter.setListener(new ProfileAdapter.Listener() {
            public void onClick(int position) {
                Log.d("onClick","onClickIn profileFragment");
            }
        });
        recyclerView.setAdapter(adapter);
    }
    public void pushDatatoFragment()
    {
        Bundle params = new Bundle();
        params.putString("fields","id,email,gender,cover,picture.type(large),first_name,last_name");
        new GraphRequest(AccessToken.getCurrentAccessToken(), "me", params, HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        if (response != null) {
                            try {
                                ParseUser currentUser = ParseUser.getCurrentUser();
                                JSONObject data = response.getJSONObject();
                                Log.d("foretest-data",data.toString());
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
                                            setUpAdapter(ParseUser.getCurrentUser());
//
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
    }





}
