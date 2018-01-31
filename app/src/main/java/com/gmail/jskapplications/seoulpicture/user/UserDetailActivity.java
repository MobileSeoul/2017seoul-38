package com.gmail.jskapplications.seoulpicture.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.facebook.AccessToken;
import com.gmail.jskapplications.seoulpicture.R;
import com.gmail.jskapplications.seoulpicture.base.BaseActivity;
import com.gmail.jskapplications.seoulpicture.main.image_list.ImageRecyclerFragment;
import com.gmail.jskapplications.seoulpicture.upload.UploadPostActivity;

public class UserDetailActivity extends BaseActivity {

    private Fragment mUserDetailFragment;
    public static final String USER_ID = "USER_ID";

    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        configureBackToolbar();

        mUserId = getIntent().getStringExtra(USER_ID);

        FragmentManager fm = getSupportFragmentManager();
        // Artworks in Grid - Start
        mUserDetailFragment = fm.findFragmentById(R.id.fragment_container);
        if(mUserDetailFragment == null) {
            mUserDetailFragment = UserDetailFragment.newInstance(mUserId);
            fm.beginTransaction()
                    .add(R.id.fragment_container, mUserDetailFragment)
                    .commit();
        }
        // Artworks in Grid - End


        // FloatingActionButton - START
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startUploadPostActivity();
            }
        });
        // FloatingActionButton - END

    }
    // FloatingActionButton - END
    private void startUploadPostActivity() {
        Intent i = new Intent(getApplicationContext(), UploadPostActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
    }
}
