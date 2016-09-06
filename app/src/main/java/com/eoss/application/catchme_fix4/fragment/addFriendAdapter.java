package com.eoss.application.catchme_fix4.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.eoss.application.catchme_fix4.R;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Foremost on 31/8/2559.
 */
public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.AddFriendViewHolder> {

    public static class AddFriendViewHolder extends RecyclerView.ViewHolder {

//        CardView cv;
        TextView name;
        //TextView gender;
        ImageView photo;
        Button acceptButton;
        Button rejectButton;




        AddFriendViewHolder(View itemView) {
            super(itemView);
            //cv = (CardView)itemView.findViewById(R.id.nearby_cardView);
//            personPhoto = (ImageView)itemView.findViewById(R.id.nearby_person_photo);
            name = (TextView)itemView.findViewById(R.id.addFriend_list_name);
            //gender = (TextView)itemView.findViewById(R.id.nearby_list_gender);
            photo = (ImageView) itemView.findViewById(R.id.addFriend_person_photo);
            acceptButton = (Button) itemView.findViewById(R.id.addFriend_acceptButton);
            rejectButton = (Button) itemView.findViewById(R.id.addFriend_rejectButton);



        }
    }

    List<ParseUser> follows;
    Context c;

    AddFriendAdapter(List<ParseUser> follows, Context c){
        this.follows = follows;
        this.c = c;
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

        personViewHolder.name.setText(follows.get(position).getString("faceName"));
        Picasso.with(c).load(follows.get(position).getString("profilePicUrl")).into(personViewHolder.photo);
        personViewHolder.acceptButton.setText("Accept");
        personViewHolder.rejectButton.setText("Decline");


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
}
