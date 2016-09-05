package com.eoss.application.catchme_fix4.fragment;

import android.content.Context;
import android.support.v7.widget.CardView;
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
public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.NearbyViewHolder> {

    public static class NearbyViewHolder extends RecyclerView.ViewHolder {

//        CardView cv;
        TextView name;
        //TextView gender;
        ImageView photo;
        ToggleButton requestToggle;




        NearbyViewHolder(View itemView) {
            super(itemView);
            //cv = (CardView)itemView.findViewById(R.id.nearby_cardView);
//            personPhoto = (ImageView)itemView.findViewById(R.id.nearby_person_photo);
            name = (TextView)itemView.findViewById(R.id.nearby_list_name);
            //gender = (TextView)itemView.findViewById(R.id.nearby_list_gender);
            photo = (ImageView) itemView.findViewById(R.id.nearby_person_photo);
            requestToggle = (ToggleButton) itemView.findViewById(R.id.requestToggle);



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
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.nearby_list, viewGroup, false);
        NearbyViewHolder pvh = new NearbyViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final NearbyViewHolder personViewHolder, final int position) {


        personViewHolder.name.setText(parseUsers.get(position).getString("faceName"));
        //personViewHolder.gender.setText(parseUsers.get(position).getString("gender"));
        Picasso.with(c).load(parseUsers.get(position).getString("profilePicUrl")).into(personViewHolder.photo);
        personViewHolder.requestToggle.setText("Send Request");
        personViewHolder.requestToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b == true)
                {
                    ParseObject follow = new ParseObject("Follow");
                    follow.put("from",ParseUser.getCurrentUser().getUsername());
                    follow.put("to",parseUsers.get(position).getUsername());
                    follow.put("status",0);
                    follow.saveInBackground();
                    Log.d("Foreb","Request Sent"+parseUsers.get(position).getString("faceName"));
                    personViewHolder.requestToggle.setTextOn("Request Sent");
                }
                else
                {
//                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Follow");
//                    query.whereEqualTo("From",ParseUser.getCurrentUser().getUsername());
//                    query.whereEqualTo("To",parseUsers.get(position).getUsername());
//                    query.getInBackground(null, new GetCallback<ParseObject>() {
//                        public void done(ParseObject object, ParseException e) {
//                            if (e == null) {
//                                object.toString();
//
//                            } else {
//                                // something went wrong
//                                Log.d("checkfore","Fore");
//                            }
//                        }
//                    });
                    Log.d("Foreb","Request "+parseUsers.get(position).getString("faceName"));
                    personViewHolder.requestToggle.setTextOff("Send Request");
                }
            }
        });
//        personViewHolder.cv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("ForeDebug",parseUsers.get(position).getString("faceName"));
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return parseUsers.size();
    }


}
