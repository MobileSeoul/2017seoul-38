package com.gmail.jskapplications.seoulpicture.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.login.widget.LoginButton;
import com.gmail.jskapplications.seoulpicture.R;
import com.gmail.jskapplications.seoulpicture.base.BaseActivity;
import com.gmail.jskapplications.seoulpicture.base.DrawerBaseActivity;
import com.gmail.jskapplications.seoulpicture.upload.UploadPostActivity;
import com.gmail.jskapplications.seoulpicture.util.ServerInfo;
import com.squareup.picasso.Picasso;


public class MainActivity extends DrawerBaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private TabLayout mTabLayout;
    private ImageView mRefreshBtn;

    private ImageView mProfileImageView;
    private TextView mProfileName;

    private NavigationView mNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //mRefreshBtn =

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        View v = mNavigationView.getHeaderView(0);

        mProfileImageView = (ImageView) v.findViewById(R.id.profile_img);
        Picasso.with(getApplicationContext()).load(ServerInfo.getProfilePictureUrl(AccessToken.getCurrentAccessToken().getUserId(),200,200)).into(mProfileImageView);

        mProfileName = (TextView) v.findViewById(R.id.name);
        mProfileName.setText(Profile.getCurrentProfile().getName());

        // TabLayout - Start
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        final PagerAdapter adapter = new MainPageAdapter
                (getSupportFragmentManager(), mTabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Set initial starting tab
        viewPager.setCurrentItem(0);
        // TabLayout - End


        // FloatingActionButton - START
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startUploadPostActivity();
            }
        });
        // FloatingActionButton - END


        //  - END
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onClick(View v) {
    }

    private void startUploadPostActivity() {
        Intent i = new Intent(getApplicationContext(), UploadPostActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
    }
}
