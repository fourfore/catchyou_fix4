package com.eoss.application.catchme_fix4.activity;

import android.content.Intent;
import android.location.Location;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import com.eoss.application.catchme_fix4.R;
import com.eoss.application.catchme_fix4.fragment.FavFragment;
import com.eoss.application.catchme_fix4.fragment.NearbyFragment;
import com.eoss.application.catchme_fix4.fragment.ProfileFragment;
import com.eoss.application.catchme_fix4.fragment.SettingFragment;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.parse.ParseUser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

public class AppActivity extends AppCompatActivity implements
        ConnectionCallbacks, OnConnectionFailedListener{

    protected static final String TAG = "AppActivity";

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    protected String mLatitudeText;
    protected String mLongitudeText;


    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_account_circle_white_24dp,
            R.drawable.ic_face_white_24dp,
            R.drawable.ic_favorite_white_24dp,
            R.drawable.ic_settings_white_24dp
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        //buildGoogleApiClient
        buildGoogleApiClient();

        //Code get user profile from face book
        Bundle params = new Bundle();
        params.putString("fields", "id,email,gender,cover,picture.type(large),first_name,last_name");
        new GraphRequest(AccessToken.getCurrentAccessToken(), "me", params, HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        if (response != null) {
                            try {
                                JSONObject data = response.getJSONObject();
                                if (data.has("picture")) {
                                    String profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                    String email = data.getString("email");
                                    String first_name = data.getString("first_name");
                                    String last_name = data.getString("last_name");
                                    String gender = data.getString("gender");

                                    Log.d("url:::>",profilePicUrl);
                                    Log.d("email:::>",email.toString());
                                    Log.d("first_name:::>",first_name.toString());
                                    Log.d("last_name:::>",last_name.toString());
                                    Log.d("gender:::>",gender.toString());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).executeAsync();




    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ProfileFragment(), "Profile");
        adapter.addFragment(new NearbyFragment(), "NearBy");
        adapter.addFragment(new FavFragment(), "Fav");
        adapter.addFragment(new SettingFragment(), "Setting");
        viewPager.setAdapter(adapter);
    }

    //***********************handle events form fragments***********************************
    public void onClickLogout(View view){
        ParseUser user = new ParseUser();
        user.getCurrentUser().logOut();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
    //**********END*************handle events form fragments*************END**********************
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //return mFragmentTitleList.get(position);
            return null;
        }
    }
    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeText = (String.valueOf(mLastLocation.getLatitude()));

            mLongitudeText = (String.valueOf(mLastLocation.getLongitude()));

            Log.d("lat::>" + mLatitudeText,"long::>" + mLongitudeText);

        } else {
            Log.d("lat::>null","long::>null");
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

}
