package com.eoss.application.catchme_fix4.fragment;


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
import com.eoss.application.catchme_fix4.activity.AppActivity;
import com.parse.ParseUser;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearbyFragment extends Fragment {
    private RecyclerView recyclerView ;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeContainer;
    private NearbyAdapter adapter;

    public NearbyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
//        toolbar.setTitle("Nearby");
        return inflater.inflate(R.layout.fragment_nearby, container, false);

    }

    @Override
    public void onStart(){
        super.onStart();
        swipeContainer = (SwipeRefreshLayout) getView().findViewById(R.id.nearby_swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AppActivity appActivity = (AppActivity)getActivity();
                appActivity.swipeUpdate();
                //appActivity.updateQueryNearby();
                //setUpAdapter(objects);
                Log.d("RefreshListener2","RefreshListener");
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void setUpAdapter(List<ParseUser> parseUsers) {

        recyclerView = (RecyclerView) getView().findViewById(R.id.nearby_RecyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new NearbyAdapter(parseUsers, getContext());


        recyclerView.setAdapter(adapter);
    }
}
