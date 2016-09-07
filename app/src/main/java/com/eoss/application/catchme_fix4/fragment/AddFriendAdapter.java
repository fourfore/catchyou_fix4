package com.eoss.application.catchme_fix4.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.eoss.application.catchme_fix4.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Foremost on 31/8/2559.
 */
public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.AddFriendViewHolder> {

    public static class AddFriendViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView photo;
        Button acceptButton;
        Button rejectButton;


        AddFriendViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.addFriend_list_name);
            photo = (ImageView) itemView.findViewById(R.id.addFriend_person_photo);
            acceptButton = (Button) itemView.findViewById(R.id.addFriend_acceptButton);
            rejectButton = (Button) itemView.findViewById(R.id.addFriend_rejectButton);
        }
    }

    List<ParseObject> follows;
    Context c;
    AddFriendFragment addFriendFragment;

    AddFriendAdapter(List<ParseObject> follows, Context c, AddFriendFragment addFriendFragment){
        this.follows = follows;
        this.c = c;
        this.addFriendFragment = addFriendFragment;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public AddFriendViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.addfriend_list, viewGroup, false);
        AddFriendViewHolder pvh = new AddFriendViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final AddFriendViewHolder personViewHolder, final int position) {

        personViewHolder.name.setText(follows.get(position).getParseObject("from").getString("faceName"));
       Picasso.with(c).load(follows.get(position).getParseObject("from").getString("profilePicUrl")).into(personViewHolder.photo);
       personViewHolder.acceptButton.setText("Accept");
        personViewHolder.rejectButton.setText("Decline");

        personViewHolder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Foremost",follows.get(position).get("status").toString());
                Log.d("Foremost",follows.get(position).getObjectId());
                follows.get(position).put("status",1);
                follows.get(position).saveInBackground();
                Log.d("Foremost",follows.get(position).get("status").toString()+" END");
//                AddFriendAdapter addFriendAdapter = new AddFriendAdapter(follows,c);
                //follows.remove(position);
                removeItem(position);
//                addFriendAdapter.notifyDataSetChanged();
                //addFriendFragment.queryAndShowFav();


            }
        });

        personViewHolder.rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                follows.get(position).put("status",-1);
                follows.get(position).saveInBackground();
                removeItem(position);
                //(Activity)c.finish();
//                AddFriendAdapter addFriendAdapter = new AddFriendAdapter(follows,c);
//                follows.remove(position);
//                addFriendAdapter.notifyDataSetChanged();

                //addFriendFragment.queryAndShowFav();

            }


        });
    }

    @Override
    public int getItemCount() {
        return follows.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        follows.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<ParseUser> list) {
        follows.addAll(list);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        follows.remove(position);
        //notifyItemRemoved(position);
        //notifyItemRangeRemoved(0,getItemCount()-1);
        notifyDataSetChanged();

    }
}
