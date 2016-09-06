package com.eoss.application.catchme_fix4.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.eoss.application.catchme_fix4.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Foremost on 31/8/2559.
 */
public class FavAdapter extends RecyclerView.Adapter<FavAdapter.FavViewHolder> {

    public static class FavViewHolder extends RecyclerView.ViewHolder {

//        CardView cv;
        TextView name;
        //TextView gender;
        ImageView photo;
        //ToggleButton requestToggle;




        FavViewHolder(View itemView) {
            super(itemView);
            //cv = (CardView)itemView.findViewById(R.id.nearby_cardView);
//            personPhoto = (ImageView)itemView.findViewById(R.id.nearby_person_photo);
            name = (TextView)itemView.findViewById(R.id.fav_list_name);
            //gender = (TextView)itemView.findViewById(R.id.nearby_list_gender);
            photo = (ImageView) itemView.findViewById(R.id.fav_person_photo);
            //requestToggle = (ToggleButton) itemView.findViewById(R.id.requestToggle);



        }
    }

    List<ParseUser> follows;
    Context c;

    FavAdapter(List<ParseUser> follows, Context c){
        this.follows = follows;
        this.c = c;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public FavViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fav_list, viewGroup, false);
        FavViewHolder pvh = new FavViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final FavViewHolder personViewHolder, final int position) {

        personViewHolder.name.setText(follows.get(position).getString("faceName"));
        Picasso.with(c).load(follows.get(position).getString("profilePicUrl")).into(personViewHolder.photo);


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
