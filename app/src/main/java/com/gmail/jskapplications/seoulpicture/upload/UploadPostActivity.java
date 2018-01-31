package com.gmail.jskapplications.seoulpicture.upload;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.gmail.jskapplications.seoulpicture.R;
import com.gmail.jskapplications.seoulpicture.base.BaseActivity;
import com.gmail.jskapplications.seoulpicture.base.PostRecyclerFragment;

import java.util.List;

public class UploadPostActivity extends BaseActivity {

    Fragment mUploadPostFragment;
    private int mPostId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_post);
        configureBackToolbar();

        FragmentManager fm = getSupportFragmentManager();
        mPostId = getIntent().getIntExtra("POST_ID", -1);
        mUploadPostFragment = fm.findFragmentById(R.id.fragment_container);
        if(mUploadPostFragment == null) {
            mUploadPostFragment = UploadPostFragment.newInstance(mPostId);
            fm.beginTransaction()
                    .add(R.id.fragment_container, mUploadPostFragment)
                    .commit();
        }
        // Posts in Grid - End
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        Log.d("ActivityResult", "UploadPostActivity");
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if(fragment != null) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }
}
