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
public class FavFragment extends Fragment {

    private RecyclerView recyclerView ;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeContainer;
    private FavAdapter adapter;

    public FavFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav, container, false);
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
        myQuery1.whereEqualTo("status",1);
        ParseQuery myQuery2 = new ParseQuery("Follow");
        myQuery2.whereEqualTo("from",user);
        myQuery2.whereEqualTo("status",1);
        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(myQuery1);
        queries.add(myQuery2);
        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
        mainQuery.include("from");
        mainQuery.include("to");
        mainQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                //query success
                if (e == null) {
                    if(objects.size()!=0){
                        for(ParseObject o :objects){

                            if(!ParseUser.getCurrentUser().getUsername().equals(o.getParseObject("to").getString("username"))){
                                parseUsers.add((ParseUser)o.getParseObject("to"));
                                Log.d("testFore","to = "+o.getParseObject("to").getString("faceName"));
                            }
                            else if(!ParseUser.getCurrentUser().getUsername().equals(o.getParseObject("from").getString("username"))){
                                parseUsers.add((ParseUser)o.getParseObject("from"));
                                Log.d("testFore","from = "+o.getParseObject("from").getString("faceName"));
                            }
                            else {
                                Log.d("testFore","form = to = Currentuser = "+o.getParseObject("from").getString("faceName")+o.getParseObject("to").getString("faceName"));
                            }
                        }
                        Log.d("testFore",parseUsers.toString());
                        //setup adapter
                        setUpAdapter(parseUsers);

                    }
                } else {
                    // Something went wrong.
                    Log.d("fail pointer","pointer");
                }
            }
        });

    }

    public void setUpAdapter(List<ParseUser> follows)
    {
        recyclerView=(RecyclerView)getView().findViewById(R.id.fav_RecyclerView);

        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new FavAdapter(follows,getContext());
        recyclerView.setAdapter(adapter);
    }

    public void setUpSwipe(){
        swipeContainer = (SwipeRefreshLayout) getView().findViewById(R.id.fav_swipeContainer);
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
