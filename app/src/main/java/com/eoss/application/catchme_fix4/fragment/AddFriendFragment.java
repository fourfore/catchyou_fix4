package com.eoss.application.catchme_fix4.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eoss.application.catchme_fix4.R;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFriendFragment extends Fragment {

    private RecyclerView recyclerView ;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeContainer;
    private AddFriendAdapter adapter;

    public AddFriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_friend, container, false);
    }

    @Override
    public void  onStart()
    {
        super.onStart();
        queryAndShowFav();
        setUpSwipe();

    }

    public void queryAndShowFav()
    {

        final List<ParseUser> parseUsers = new ArrayList<>();
        //query
        ParseUser user= ParseUser.getCurrentUser();
        ParseQuery myQuery1 = new ParseQuery("Follow");
        myQuery1.whereEqualTo("to",user);
        myQuery1.whereEqualTo("status",0);
        myQuery1.include("from");
        myQuery1.include("to");
        myQuery1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                //query success
                if (e == null) {
                    if(objects.size()!=0){

                        Log.d("testFore",parseUsers.toString());
                        //setup adapter
                        setUpAdapter(objects);

                    }
                } else {
                    // Something went wrong.
                    Log.d("fail pointer","pointer");
                }
            }
        });

    }

    public void setUpAdapter(List<ParseObject> follows)
    {
        recyclerView=(RecyclerView)getView().findViewById(R.id.addFriend_RecyclerView);

        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new AddFriendAdapter(follows,getContext(),this);

        recyclerView.setAdapter(adapter);
    }

    public void setUpSwipe(){
        swipeContainer = (SwipeRefreshLayout) getView().findViewById(R.id.addFriend_swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(adapter != null){
                    adapter.clear();
                }
                queryAndShowFav();
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_blue_light);
    }

}
