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

import java.util.List;

/**
 * Created by Foremost on 31/8/2559.
 */
public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {


    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public static interface Listener {
        public void onClick(int position);
    }
    public static class ProfileViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        ImageView personPhoto;
        TextView personName;
        TextView personGender;


        ProfileViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.profile_cardView);
            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            personGender = (TextView)itemView.findViewById(R.id.person_gender);

        }
    }

    private Listener listener;
    ParseUser person;
    Context c;

    ProfileAdapter(ParseUser person, Context c){
        this.person = person;
        this.c = c;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.profile_cardview, viewGroup, false);
        ProfileViewHolder pvh = new ProfileViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ProfileViewHolder personViewHolder,final int position) {
        if(person.getString("faceName") != null)
        Log.d("ForeWait",person.getString("faceName"));
        personViewHolder.personName.setText(person.getString("faceName"));
        personViewHolder.personGender.setText(person.getString("gender"));
        //personViewHolder.personPhoto.setImageResource(persons.get(i).photoId);
        Picasso.with(c).load(person.getString("profilePicUrl")).into(personViewHolder.personPhoto);

        //click photo Listenner for do something
        personViewHolder.personPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("onClick-> Photo","onClick-> Photo");
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }


}
