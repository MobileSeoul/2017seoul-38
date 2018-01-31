package com.gmail.jskapplications.seoulpicture.location;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.gmail.jskapplications.seoulpicture.R;
import com.gmail.jskapplications.seoulpicture.base.BaseActivity;
import com.gmail.jskapplications.seoulpicture.base.DrawerBaseActivity;
import com.gmail.jskapplications.seoulpicture.main.MainPageAdapter;
import com.gmail.jskapplications.seoulpicture.model.Location;
import com.gmail.jskapplications.seoulpicture.model.LocationSingleton;
import com.gmail.jskapplications.seoulpicture.network.CategoryNetwork;
import com.gmail.jskapplications.seoulpicture.network.LocationNetwork;
import com.gmail.jskapplications.seoulpicture.upload.UploadPostActivity;
import com.gmail.jskapplications.seoulpicture.user.UserDetailFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class LocationActivity extends BaseActivity implements OnMapReadyCallback  {
    // Google Map
    private GoogleMap mGoogleMap;

    private Spinner mLocationSpinner;
    private Location[] mLocations;

    private Fragment mLocationFragment;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;

    private Marker[] mMarkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        configureBackToolbar();
        mToolbar.setTitle("");



        FragmentManager fm = getSupportFragmentManager();


        // Google map fragment - Start
        CustomMapFragment mapFragment = (CustomMapFragment)  fm.findFragmentById(R.id.mapview);
        mapFragment.getMapAsync(this);
        // Google map fragment - Start3.

        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if(scrollRange + verticalOffset == 0) {
                    mCollapsingToolbar.setTitle("서울을 보여줘");
                    isShow = true;
                } else if(isShow) {
                    mCollapsingToolbar.setTitle("");
                    isShow = false;

                }
            }
        });


        mLocationSpinner = (Spinner) findViewById(R.id.location_spinner);

        mapFragment.setListener(new CustomMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                mCollapsingToolbar.requestDisallowInterceptTouchEvent(true);
            }
        });
        // Location  - Start
        mLocationFragment = fm.findFragmentById(R.id.fragment_container);
        if(mLocationFragment == null) {
            mLocationFragment = LocationFragment.newInstance("null", 3, 0);
            fm.beginTransaction()
                    .add(R.id.fragment_container, mLocationFragment)
                    .commit();
        }
        // Location  - End



    }




    // Google Map CallBack
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mLocations = LocationSingleton.getInstance().getLocations();
        mMarkers = new Marker[mLocations.length];

        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);


        moveCameraToSeoul(false);  // Move camera to art Museum
        googleMap.setPadding(0,0,0, dpToPx(41));

        ArrayAdapter<String> categorySpinnerArrayAdapter = new ArrayAdapter<String>
                (getApplicationContext(), android.R.layout.simple_spinner_item, LocationNetwork.getInstance().getLocations());
        categorySpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLocationSpinner.setAdapter(categorySpinnerArrayAdapter);

        mLocationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                Log.d("POSSSSITION", "position: " + position);
                if(position != 0) {

                    if(getSupportFragmentManager().getFragments().get(1) != null) {
                        ((LocationFragment)(getSupportFragmentManager().getFragments().get(1))).setLocationId(position);
                         ((LocationFragment)(getSupportFragmentManager().getFragments().get(1))).updateUIwithClear();
                    }
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(mLocations[position].getmLatitude(), mLocations[position].getmLongitude()))      // Sets the center of the map to Mountain View
                            .zoom(11)                   // Sets the zoom
                            .build();                   // Creates a CameraPosition from the builder

                    mGoogleMap.moveCamera
                            (CameraUpdateFactory.newCameraPosition(cameraPosition
                    ));
                    mMarkers[position].showInfoWindow();

                } else {
                    if(getSupportFragmentManager().getFragments().get(1) != null) {
                        ((LocationFragment)(getSupportFragmentManager().getFragments().get(1))).setLocationId(position);
                        ((LocationFragment)(getSupportFragmentManager().getFragments().get(1))).updateUIwithClear();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // 카테고리 스피너 - END

        Log.i("GoogleMap!!", "1");
    }

    private void moveCameraToSeoul(boolean animate) {
        LatLng seoul = new LatLng(37.561229,126.983915);

        for(int i=1; i<mLocations.length; i++) {

            mMarkers[i] = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(mLocations[i].getmLatitude(), mLocations[i].getmLongitude())).
                title(mLocations[i].getmName()));
        }
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for(int i=1; i<mLocations.length; i++) {
                    if(mLocations[i].getmName().equals(marker.getTitle())) {
                        mLocationSpinner.setSelection(i);
                        break;
                    }
                }
                Log.d("LOCATIOOON", "Marker: " +  marker.getTitle());
                return true;
            }
        });

        /*
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(artMuseum));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);


        mGoogleMap.moveCamera(zoom);
*/
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(seoul)      // Sets the center of the map to Mountain View
                .zoom(11)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder

        if(animate) {
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } else {
            mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    // dp to pixel
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
