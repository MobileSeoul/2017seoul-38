package com.gmail.jskapplications.seoulpicture.common.post_detail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gmail.jskapplications.seoulpicture.R;
import com.gmail.jskapplications.seoulpicture.base.BaseActivity;
import com.gmail.jskapplications.seoulpicture.model.PicturePost;
import com.gmail.jskapplications.seoulpicture.network.VolleySingleton;
import com.gmail.jskapplications.seoulpicture.util.ServerInfo;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PostPagerDetailActivity extends BaseActivity {

    public static final String POST_ID = "POST_ID";
    public static final String POSTER_ID = "POSTER_ID";

    public static final String POST_LIST = "POST_LIST";
    public static final String BUNDLE = "BUNDLE";


    private int mPostId;
    private String mPosterId;
    private int mPostSize;
    private ViewPager mViewPager;
    private ArrayList<PicturePost> mPicturePosts;

    private TextView mCategoryTxt;
    private TextView mRegionTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        configureBackToolbar();

        mPostId = getIntent().getIntExtra(POST_ID, 0);
        Bundle args = getIntent().getBundleExtra("BUNDLE");
        mPicturePosts = (ArrayList<PicturePost>) getIntent().getSerializableExtra(POST_LIST);

        mCategoryTxt = (TextView) findViewById(R.id.category_txt);
        mRegionTxt = (TextView) findViewById(R.id.region_txt);

        //mPostImage = (ImageView) findViewById(R.id.post_image);
        //mPicturePost = new PicturePost();


        // getPost();
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                PicturePost post = mPicturePosts.get(position);
                return PostDetailFragment.newInstance(post.getId(), post.getmUserId());
            }

            @Override
            public int getCount() {
                return mPicturePosts.size();
            }
        });
        for(int i=0; i<mPicturePosts.size(); i++) {
            if(mPicturePosts.get(i).getId() == mPostId)  {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    public void setCategory(String category) {
        mCategoryTxt.setText(category);
    }
    public void setLocation(String location) {
        mRegionTxt.setText(location);
    }
}
