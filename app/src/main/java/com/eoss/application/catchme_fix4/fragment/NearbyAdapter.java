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
public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.NearbyViewHolder> {

    public static class NearbyViewHolder extends RecyclerView.ViewHolder {

        CardView cv;



        NearbyViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.nearby_cardView);
//            personPhoto = (ImageView)itemView.findViewById(R.id.nearby_person_photo);
//            personName = (TextView)itemView.findViewById(R.id.nearby_person_name);
//            personGender = (TextView)itemView.findViewById(R.id.nearby_person_gender);

        }
    }

    List<ParseUser> parseUsers;
    Context c;

    NearbyAdapter(List<ParseUser> parseUsers, Context c){
        this.parseUsers = parseUsers;
        this.c = c;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public NearbyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.nearby_cardview, viewGroup, false);
        NearbyViewHolder pvh = new NearbyViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(NearbyViewHolder personViewHolder, final int position) {
        ImageView imageView = (ImageView)personViewHolder.cv.findViewById(R.id.nearby_person_photo);
        TextView nameTextView = (TextView)personViewHolder.cv.findViewById(R.id.nearby_person_name);
        TextView genderTextView = (TextView)personViewHolder.cv.findViewById(R.id.nearby_person_gender);

        nameTextView.setText(parseUsers.get(position).getString("faceName"));
        genderTextView.setText(parseUsers.get(position).getString("gender"));

        //personViewHolder.personPhoto.setImageResource(persons.get(i).photoId);
        Picasso.with(c).load(parseUsers.get(position).getString("profilePicUrl")).into(imageView);

        personViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ForeDebug",parseUsers.get(position).getString("faceName"));
            }
        });

    }

    @Override
    public int getItemCount() {
        return parseUsers.size();
    }


}
