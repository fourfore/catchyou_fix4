package com.eoss.application.catchme_fix4.fragment;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eoss.application.catchme_fix4.R;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

/**
 * Created by Foremost on 31/8/2559.
 */
public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.ProfileViewHolder> {

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        ImageView personPhoto;
        TextView personName;
        TextView personGender;


        ProfileViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.nearby_cardView);
            personPhoto = (ImageView)itemView.findViewById(R.id.nearby_person_photo);
            personName = (TextView)itemView.findViewById(R.id.nearby_person_name);
            personGender = (TextView)itemView.findViewById(R.id.nearby_person_gender);

        }
    }

    ParseUser person;
    Context c;

    NearbyAdapter(ParseUser person, Context c){
        this.person = person;
        this.c = c;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.nearby_cardview, viewGroup, false);
        ProfileViewHolder pvh = new ProfileViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ProfileViewHolder personViewHolder, int i) {
        if(person.getString("faceName") != null)
        Log.d("ForeWait",person.getString("faceName"));
        personViewHolder.personName.setText(person.getString("faceName"));
        personViewHolder.personGender.setText(person.getString("gender"));
        //personViewHolder.personPhoto.setImageResource(persons.get(i).photoId);
        Picasso.with(c).load(person.getString("profilePicUrl")).into(personViewHolder.personPhoto);
    }

    @Override
    public int getItemCount() {
        return 1;
    }


}
