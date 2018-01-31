package com.gmail.jskapplications.seoulpicture.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.gmail.jskapplications.seoulpicture.R;

import com.gmail.jskapplications.seoulpicture.license.LicenseActivity;
import com.gmail.jskapplications.seoulpicture.location.LocationActivity;
import com.gmail.jskapplications.seoulpicture.user.UserDetailActivity;
import com.gmail.jskapplications.seoulpicture.user.UserRecyclerActivity;
import com.gmail.jskapplications.seoulpicture.util.GlobalInfo;
import com.gmail.jskapplications.seoulpicture.util.ServerInfo;
import com.squareup.picasso.Picasso;

import kr.go.seoul.airquality.AirQualityDetailTypeB;
import kr.go.seoul.culturalevents.CulturalEventSearchTypeB;

/**
 * Created by for on 2017-10-14.
 */

public class DrawerBaseActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent i = new Intent(getApplicationContext(), UserDetailActivity.class);
            i.putExtra(UserDetailActivity.USER_ID, AccessToken.getCurrentAccessToken().getUserId());
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(i);
        } else if( id == R.id.nav_user_list ) {
            Intent i = new Intent(getApplicationContext(), UserRecyclerActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(i);
        } else if (id == R.id.nav_loc_pic) {
            Intent i = new Intent(getApplicationContext(), LocationActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

            startActivity(i);
        } else if (id == R.id.nav_culture) {

            Intent intent = new Intent(getApplicationContext(), CulturalEventSearchTypeB.class);
            intent.putExtra("OpenAPIKey", GlobalInfo.SEOUL_API_KEY);
            startActivity(intent);
        } else if (id == R.id.nav_air) {
            Intent intent = new Intent(getApplicationContext(), AirQualityDetailTypeB.class);
            intent.putExtra("OpenAPIKey", GlobalInfo.SEOUL_API_KEY);
            startActivity(intent);
        } else if (id == R.id.nav_license) {
            Intent i = new Intent(getApplicationContext(), LicenseActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
