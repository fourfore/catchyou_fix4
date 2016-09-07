package com.eoss.application.catchme_fix4.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.eoss.application.catchme_fix4.R;
import com.eoss.application.catchme_fix4.fragment.AddFriendFragment;
import com.eoss.application.catchme_fix4.fragment.FavFragment;
import com.eoss.application.catchme_fix4.fragment.NearbyFragment;
import com.eoss.application.catchme_fix4.fragment.ProfileFragment;
import com.eoss.application.catchme_fix4.fragment.SettingFragment;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.parse.SaveCallback;

public class AppActivity extends AppCompatActivity implements
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener {
    //Google location Variable
    protected static final String TAG = "location-updates";
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected Location mCurrentLocation;
    protected Button mStartUpdatesButton;
    protected Boolean mRequestingLocationUpdates;
    protected String mLastUpdateTime;
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    // Fragment Variable

    private ProfileFragment profileFragment;
    private NearbyFragment nearbyFragment;
    private FavFragment favFragment;
    private AddFriendFragment addFriendFragment;
    private SettingFragment settingFragment;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private int[] tabIcons = {
            R.drawable.ic_account_circle_white_24dp,
            R.drawable.ic_my_location_white_24dp,
            R.drawable.ic_favorite_white_24dp,
            R.drawable.ic_person_add_white_24dp,
            R.drawable.ic_settings_white_24dp
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (savedInstanceState != null) {
            //Restore your fragment instance
            profileFragment = (ProfileFragment)getSupportFragmentManager().getFragment(
                    savedInstanceState, "fragmentProfile");
            nearbyFragment = (NearbyFragment)getSupportFragmentManager().getFragment(
                    savedInstanceState, "fragmentNearby");
            favFragment = (FavFragment)getSupportFragmentManager().getFragment(
                    savedInstanceState, "fragmentFav");
            addFriendFragment = (AddFriendFragment)getSupportFragmentManager().getFragment(
                    savedInstanceState, "fragmentAddFriend");
            settingFragment = (SettingFragment)getSupportFragmentManager().getFragment(
                    savedInstanceState, "fragmentSetting");

        }
        else {
            profileFragment = new ProfileFragment();
            nearbyFragment = new NearbyFragment();
            favFragment = new FavFragment();
            settingFragment = new SettingFragment();
            addFriendFragment = new AddFriendFragment();
        }
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("check5",position+"");
                if(position == 0)
                    toolbar.setTitle("Profile");
                else if(position == 1)
                    toolbar.setTitle("Nearby");
                else if(position == 2)
                    toolbar.setTitle("Favorite");
                else if(position == 3)
                    toolbar.setTitle("Add Friends");
                else if(position == 4)
                    toolbar.setTitle("Setting");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //how to get another fragment
        //FavFragment favFragment = (FavFragment)adapter.getItem(2);
        viewPager.setOffscreenPageLimit(4);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(1);
        //toolbar.setTitle("Nearby");
        setupTabIcons();


        //Google Location initial
        mStartUpdatesButton = (Button) findViewById(R.id.button);
        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";
        Log.d("connect","google");
        buildGoogleApiClient();




        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {

                final Status status = locationSettingsResult.getStatus();
                final LocationSettingsStates LS_state = locationSettingsResult.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(AppActivity.this, REQUEST_CHECK_SETTINGS);

                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Toast.makeText(AppActivity.this, "please Check your internet connection and Location service",
                                Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });

    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(profileFragment, "Profile");
        adapter.addFragment(nearbyFragment, "NearBy");
        adapter.addFragment(favFragment, "Fav");
        adapter.addFragment(addFriendFragment, "AddFriends");
        adapter.addFragment(settingFragment, "Setting");
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
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Handles the Start Updates button and requests start of location updates. Does nothing if
     * updates have already been requested.
     */

    public void swipeUpdate() {
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
            //setButtonsEnabledState();
            startLocationUpdates();
        }
    }
    /**
     * Handles the Stop Updates button, and requests removal of location updates. Does nothing if
     * updates were not previously requested.
     */
    public void stopUpdatesButtonHandler(View view) {
        if (mRequestingLocationUpdates) {
            mRequestingLocationUpdates = false;
            //setButtonsEnabledState();
            stopLocationUpdates();
        }
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_ACCESS_COARSE_LOCATION);
        }
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }


    public void updateQueryNearby2(){
        Log.d("user:Name Current", ParseUser.getCurrentUser().getObjectId());

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Follow");
        query1.whereEqualTo("to",ParseUser.getCurrentUser());



        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Follow");
        query2.whereEqualTo("from",ParseUser.getCurrentUser());
        query2.whereEqualTo("status",1);

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(query1);
        queries.add(query2);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
        mainQuery.include("from");
        mainQuery.include("to");

        mainQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {


                    if(objects.size() != 0){

                        List<String> list = new ArrayList<String>();
                        for(ParseObject po:objects) {
                            Log.d("TestList","TestList");
                            list.add(po.getParseObject("from").getObjectId());
                            list.add(po.getParseObject("to").getObjectId());
                            Log.d("From:Name" + po.getParseObject("from").getObjectId(), "To:Name" + po.getParseObject("to").getObjectId());
                        }
                        ParseQuery<ParseUser> query = ParseUser.getQuery();
                        query.whereNotEqualTo("objectId",ParseUser.getCurrentUser().getObjectId());
                        query.whereNotContainedIn("objectId",list);
                        query.findInBackground(new FindCallback<ParseUser>() {
                            @Override
                            public void done(List<ParseUser> objects, ParseException e) {
                                if(objects.size() != 0) {
                                    for (ParseUser pu : objects) {
                                        NearbyFragment nearbyFragment= (NearbyFragment)adapter.getItem(1);
                                        nearbyFragment.setUpAdapter(objects);
                                        Log.d("user:Name" + pu.getString("faceName"), "user:id" + pu.getObjectId());
                                    }
                                }else{
                                    Log.d("QueryUser1" ,"null");
                                }

                            }
                        });

                    }else{

                        List<String> list = new ArrayList<String>();
                        for(ParseObject po:objects) {
                            Log.d("TestList2","TestList2");
                            list.add(po.getParseObject("from").getObjectId());
                            list.add(po.getParseObject("to").getObjectId());
                            Log.d("From:Name" + po.getParseObject("from").getObjectId(), "To:Name" + po.getParseObject("to").getObjectId());
                        }
                        ParseQuery<ParseUser> query = ParseUser.getQuery();
                        query.whereNotEqualTo("objectId",ParseUser.getCurrentUser().getObjectId());
                        query.whereNotContainedIn("objectId",list);
                        query.findInBackground(new FindCallback<ParseUser>() {
                            @Override
                            public void done(List<ParseUser> objects, ParseException e) {
                                if(objects.size() != 0) {
                                    for (ParseUser pu : objects) {
                                        NearbyFragment nearbyFragment= (NearbyFragment)adapter.getItem(1);
                                        nearbyFragment.setUpAdapter(objects);
                                        Log.d("user:Name" + pu.getString("faceName"), "user:id" + pu.getObjectId());
                                    }
                                }else{
                                    Log.d("QueryUser2" ,"null");
                                }

                            }
                        });
                    }
                } else {

                    e.printStackTrace();
                }
            }
        });
    }
    public void updateQueryNearby(){

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Follow");
        query1.whereEqualTo("to",ParseUser.getCurrentUser());

        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Follow");
        query2.whereEqualTo("from",ParseUser.getCurrentUser());
        query2.whereEqualTo("status",1);

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(query1);
        queries.add(query2);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
        mainQuery.include("from");
        mainQuery.include("to");

        mainQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if(objects.size() != 0){

                        List<String> list = new ArrayList<String>();
                        for(ParseObject po:objects) {
                            Log.d("TestList","TestList");
                            list.add(po.getParseObject("from").getObjectId());
                            list.add(po.getParseObject("to").getObjectId());
                            Log.d("From:Name" + po.getParseObject("from").getObjectId(), "To:Name" + po.getParseObject("to").getObjectId());

                        }

                        ParseQuery<ParseUser> query = ParseUser.getQuery();

                        query.whereNotEqualTo("objectId",ParseUser.getCurrentUser().getObjectId());
                        query.whereNotContainedIn("objectId",list);
                        query.whereWithinKilometers("Location",ParseUser.getCurrentUser().getParseGeoPoint("Location"),1);
                        query.findInBackground(new FindCallback<ParseUser>() {
                            @Override
                            public void done(List<ParseUser> objects, ParseException e) {
                                if(objects.size() != 0) {
                                    for (ParseUser pu : objects) {
                                        NearbyFragment nearbyFragment= (NearbyFragment)adapter.getItem(1);
                                        nearbyFragment.setUpAdapter(objects);
                                        Log.d("user:Name" + pu.getString("faceName"), "user:id" + pu.getObjectId());
                                    }
                                }else{
                                    Log.d("QueryUser1" ,"null");
                                }
                            }
                        });

                    }else{

                        List<String> list = new ArrayList<String>();
                        for(ParseObject po:objects) {
                            Log.d("TestList2","TestList2");
                            list.add(po.getParseObject("from").getObjectId());
                            list.add(po.getParseObject("to").getObjectId());
                            Log.d("From:Name" + po.getParseObject("from").getObjectId(), "To:Name" + po.getParseObject("to").getObjectId());
                        }

                        ParseQuery<ParseUser> query = ParseUser.getQuery();

                        query.whereNotEqualTo("objectId",ParseUser.getCurrentUser().getObjectId());
                        query.whereNotContainedIn("objectId",list);
                        query.whereWithinKilometers("Location",ParseUser.getCurrentUser().getParseGeoPoint("Location"),1);
                        Log.d("currlo",ParseUser.getCurrentUser().getParseGeoPoint("Location").toString());
                        query.findInBackground(new FindCallback<ParseUser>() {

                            @Override
                            public void done(List<ParseUser> objects, ParseException e) {
                                if(objects.size() != 0) {
                                    for (ParseUser pu : objects) {
                                        NearbyFragment nearbyFragment= (NearbyFragment)adapter.getItem(1);
                                        nearbyFragment.setUpAdapter(objects);
                                        Log.d("user:Name" + pu.getString("faceName"), "user:id" + pu.getObjectId());
                                    }
                                }else{
                                    Log.d("QueryUser2" ,"null");
                                }
                            }
                        });
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });


    }
    /**
     * Updates the latitude, the longitude, and the last location time in the UI.
     */
    private void updateUI() {
        //updateQueryNearby2();
        if (mCurrentLocation != null) {


            Log.d("lat::>" + mCurrentLocation.getLatitude(),"long::>" + mCurrentLocation.getLongitude());
            mRequestingLocationUpdates = false;
            //setButtonsEnabledState();
            stopLocationUpdates();

            //set in parse user
            final ParseUser currentUser = ParseUser.getCurrentUser();
            ParseGeoPoint point = new ParseGeoPoint(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            currentUser.put("Location",point);
            currentUser.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        // Saved successfully.
                        ParseGeoPoint geoPoint = new ParseGeoPoint(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
                        Log.d("Saved successfully", "update location");
                        updateQueryNearby();

                    } else {
                        // The save failed.
                        Log.d("TAG", "User update error: " + e);

                    }
                }
            });

        } else {

            Log.d("lat::>null","long::>null");
            updateQueryNearby2();

        }

    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.

        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.

        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();

        super.onStop();
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_ACCESS_COARSE_LOCATION);
        }
        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            updateUI();
        }

        // If the user presses the Start Updates button before GoogleApiClient connects, we set
        // mRequestingLocationUpdates to true (see startUpdatesButtonHandler()). Here, we check
        // the value of mRequestingLocationUpdates and if it is true, we start location updates.
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
        Toast.makeText(this, getResources().getString(R.string.location_updated_message),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    /**
     * Stores activity data in the Bundle.
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);

        getSupportFragmentManager().putFragment(savedInstanceState, "fragmentProfile", profileFragment);
        getSupportFragmentManager().putFragment(savedInstanceState, "fragmentNearby", nearbyFragment);
        getSupportFragmentManager().putFragment(savedInstanceState, "fragmentFav", favFragment);
        getSupportFragmentManager().putFragment(savedInstanceState, "fragmentAddFriend", addFriendFragment);
        getSupportFragmentManager().putFragment(savedInstanceState, "fragmentSetting", settingFragment);

        //super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        startLocationUpdates();
                        // All required changes were successfully made
                        //FINALLY YOUR OWN METHOD TO GET YOUR USER LOCATION HERE

                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        ParseUser user = new ParseUser();
                        user.getCurrentUser().logOut();
                        Intent intent = new Intent(this,LoginActivity.class);
                        startActivity(intent);
                        finish();

                        break;
                    default:
                        break;
                }
                break;
        }
    }
}


