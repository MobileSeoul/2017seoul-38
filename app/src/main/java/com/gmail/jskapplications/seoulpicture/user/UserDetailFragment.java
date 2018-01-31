package com.gmail.jskapplications.seoulpicture.user;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.gmail.jskapplications.seoulpicture.R;
import com.gmail.jskapplications.seoulpicture.base.BaseFragment;
import com.gmail.jskapplications.seoulpicture.main.home.HomeFragment;
import com.gmail.jskapplications.seoulpicture.main.image_list.ImageRecyclerFragment;
import com.gmail.jskapplications.seoulpicture.network.VolleySingleton;
import com.gmail.jskapplications.seoulpicture.util.LevelManager;
import com.gmail.jskapplications.seoulpicture.util.ServerInfo;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by for on 2017-10-15.
 */

public class UserDetailFragment extends BaseFragment {

    private static final String USER_ID = "USER_ID";

    private Fragment mUserPostsFragment;

    private ImageView mProfileImageView;
    private TextView mProfileName;
    private TextView mUserLevel;

    private TextView mLikeTxt;



    private String mUserId;


    // TODO: Rename and change types and number of parameters
    public static UserDetailFragment newInstance(String userId) {
        UserDetailFragment fragment = new UserDetailFragment();
        Bundle args = new Bundle();
        args.putString(USER_ID, userId);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserId = getArguments().getString(USER_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_detail, container, false);


        mProfileImageView = (ImageView) v.findViewById(R.id.profile_img);
        mProfileName = (TextView) v.findViewById(R.id.name);

        mUserLevel = (TextView) v.findViewById(R.id.level);

       Picasso.with(getActivity()).load(ServerInfo.getProfilePictureUrl(mUserId,200,200)).into(mProfileImageView);
        // mProfileName.setText(Profile.getCurrentProfile().getName());


        mLikeTxt = (TextView) v.findViewById(R.id.like_txt);


        FragmentManager fm = getChildFragmentManager();
        // Artworks in Grid - Start
        mUserPostsFragment = fm.findFragmentById(R.id.user_posts_list_container);
        if(mUserPostsFragment == null) {
            mUserPostsFragment = ImageRecyclerFragment.newInstance(mUserId, 3);
            fm.beginTransaction()
                    .add(R.id.user_posts_list_container, mUserPostsFragment)
                    .commit();
        }
        // Artworks in Grid - End

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getLikes();
    }

    private void getLikes() {
        Log.d("VolleyTest", "HERE!");
        StringRequest jsonStringRequest = new StringRequest(Request.Method.POST, ServerInfo.GET_USER_LIKES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("VolleyTest", response.toString());
                        try {
                            JSONArray j = new JSONArray(response);

                            //JSONObject jObject = j.getJSONObject(0);

                            JSONArray dataArray = j.getJSONArray(1);
                            for(int i=0; i<dataArray.length(); i++) {
                                JSONObject jsonObject = dataArray.getJSONObject(i);

                                if(!jsonObject.isNull("total_likes")) {
                                    mLikeTxt.setText(jsonObject.getInt("total_likes") + "");
                                    mUserLevel.setText(LevelManager.getLevel(jsonObject.getInt("total_likes"))+"");
                                }
                                if(!jsonObject.isNull("user_name")) {
                                    mProfileName.setText(jsonObject.getString("user_name"));
                                }
                                // mAdapter.notifyItemInserted(posts.size()-1);
                            }

                        } catch(JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VolleyTest", error.toString());

            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("post_owner_id", mUserId);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQue(jsonStringRequest);

    }
}
