package com.gmail.jskapplications.seoulpicture.common.post_detail;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.gmail.jskapplications.seoulpicture.R;
import com.gmail.jskapplications.seoulpicture.model.PicturePost;
import com.gmail.jskapplications.seoulpicture.network.CategoryNetwork;
import com.gmail.jskapplications.seoulpicture.network.LocationNetwork;
import com.gmail.jskapplications.seoulpicture.network.VolleySingleton;
import com.gmail.jskapplications.seoulpicture.upload.UploadPostActivity;
import com.gmail.jskapplications.seoulpicture.user.UserDetailActivity;
import com.gmail.jskapplications.seoulpicture.util.DateUtil;
import com.gmail.jskapplications.seoulpicture.util.LevelManager;
import com.gmail.jskapplications.seoulpicture.util.ServerInfo;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostDetailFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_ID = "ARG_ID";
    private static final String POSTER_ID = "POSTER_ID";

    private TextView mDateTxt;
    private TextView mNewTxt;

    private ImageView mPostImage;
    private TextView mLikesTxt;
    private TextView mTitleTxt;
    private TextView mDescriptionTxt;

    private String mPostUserId;

    private PicturePost mPicturePost;


    private ImageView mPosterImage;
    private TextView mPosterName;
    private TextView mPosterLevel;
    private LinearLayout mUserProfileBtn;

    private int mPostId;
    private String mPosterId;


    private TextView mLikeButton;
    private boolean mIsLike;


    // 편집과 삭제
    private LinearLayout mEditLayout;
    private Button mEditButton;
    private Button mDeleteButton;


    public PostDetailFragment() {
        // Required empty public constructor
    }

    public static PostDetailFragment newInstance(int id, String posterId) {
        PostDetailFragment fragment = new PostDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        args.putString(POSTER_ID, posterId);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPostId = getArguments().getInt(ARG_ID);
            mPosterId = getArguments().getString(POSTER_ID);
            Log.d("POSSSSSSSSTERID", mPosterId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_post_detail, container, false);

        mNewTxt = (TextView) v.findViewById(R.id.new_txt);
        mPostImage = (ImageView) v.findViewById(R.id.post_image);
        mPostImage.setOnClickListener(this);
        mPicturePost = new PicturePost();

        mLikesTxt = (TextView) v.findViewById(R.id.likes_txt);
        mTitleTxt = (TextView) v.findViewById(R.id.title_txt);
        mDateTxt = (TextView) v.findViewById(R.id.date_txt);
        mDescriptionTxt = (TextView) v.findViewById(R.id.description_text);

        mPosterImage = (ImageView) v.findViewById(R.id.poster_img);
        mPosterName = (TextView) v.findViewById(R.id.poster_name);
        mPosterLevel = (TextView) v.findViewById(R.id.poster_level);
        mUserProfileBtn = (LinearLayout) v.findViewById(R.id.user_profile_btn);
        mUserProfileBtn.setOnClickListener(this);

        mLikeButton = (TextView) v.findViewById(R.id.like_btn);
        mLikeButton.setOnClickListener(this);


        LinearLayout mEditLayout = (LinearLayout) v.findViewById(R.id.edit_delete_layout);
        if(mPosterId.equals(AccessToken.getCurrentAccessToken().getUserId())) {
            mEditLayout.setVisibility(View.VISIBLE);
        }
        Button mEditButton = (Button) v.findViewById(R.id.edit_btn);
        mEditButton.setOnClickListener(this);
        Button mDeleteButton = (Button) v.findViewById(R.id.delete_btn);
        mDeleteButton.setOnClickListener(this);

       // getPost();



        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPost();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // load data here
            getPost();
        }
    }
    @Override
    public void onClick(View v) {
        v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.home_card_click));
        switch(v.getId()) {
            case R.id.user_profile_btn:
                Intent i = new Intent(getActivity(), UserDetailActivity.class);
                i.putExtra(UserDetailActivity.USER_ID, mPosterId);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                break;

            case R.id.post_image:
                FragmentManager fm = getActivity().getFragmentManager();
                FragmentTransaction tr = fm.beginTransaction();
                android.app.Fragment prev = fm.findFragmentByTag("dialog");
                if (prev != null) {
                    tr.remove(prev);
                }
                    Log.d("HERERERERERERE", mPicturePost.getImgUrl());
                if(mPicturePost != null) {
                    ImageViewDialog dialog = ImageViewDialog.newInstance(mPicturePost.getImgUrl());
                    dialog.show(tr, "dialog");
                }
                break;

            case R.id.like_btn:
                likePost();
                break;

            case R.id.edit_btn:
                startUploadPostActivityForEdit();
                break;

            case R.id.delete_btn:
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("포스트 삭제");
                alert.setMessage("정말로 포스트를 삭제 하시겠습니까?");
                alert.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletePost();
                    }
                });
                alert.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
                break;
        }
    }

    private void startUploadPostActivityForEdit() {
        Intent i = new Intent(getApplicationContext(), UploadPostActivity.class);
        i.putExtra("POST_ID", mPostId);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
    }

    private void deletePost() {
        Log.d("VolleyTest", "HERE!");
        StringRequest jsonStringRequest = new StringRequest(Request.Method.POST, ServerInfo.DELETE_POST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("VolleyTest", response.toString());

                        if(response.equals("success")) {

                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setTitle("포스트 삭제 완료");
                            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.putExtra("DELETE", mPostId);
                                    getActivity().setResult(Activity.RESULT_OK, intent);
                                    getActivity().finish();
                                }
                            });
                            alert.show();
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
                params.put("post_id", Integer.toString(mPostId));
                params.put("poster_id", mPosterId);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQue(jsonStringRequest);
    }

    private void likePost() {
        Log.d("VolleyTest", "HERE!");
        StringRequest jsonStringRequest = new StringRequest(Request.Method.POST, ServerInfo.PUT_LIKE_POST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("VolleyTestisLike", response.toString());
                        try {
                            JSONArray j = new JSONArray(response);

                            JSONObject jObject = j.getJSONObject(0);
                            mIsLike = jObject.getBoolean("is_like");
                            if(mIsLike) {
                                mLikeButton.setText("좋아요 완료");
                                mLikeButton.setBackgroundResource(R.drawable.liked_layout);
                                mLikeButton.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white_100));

                            } else {
                                mLikeButton.setText("좋아요");
                                mLikeButton.setBackgroundResource(R.drawable.like_layout);
                                mLikeButton.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black_100));


                            }

                            JSONArray dataArray = j.getJSONArray(1);
                            for(int i=0; i<dataArray.length(); i++) {
                                JSONObject jsonObject = dataArray.getJSONObject(i);

                                if (!jsonObject.isNull("likes")) {
                                    mLikesTxt.setText(jsonObject.getInt("likes") + "");
                                }
                                if(!jsonObject.isNull("total_likes")) {
                                    mPosterLevel.setText(LevelManager.getLevel(jsonObject.getInt("total_likes")) + "");
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
                params.put("like_post_id", Integer.toString(mPostId));
                params.put("like_user_id", AccessToken.getCurrentAccessToken().getUserId());
                params.put("post_owner_id", mPostUserId);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQue(jsonStringRequest);
    }

    private void getPost() {
        Log.d("VolleyTest", "HERE!");
        StringRequest jsonStringRequest = new StringRequest(Request.Method.POST, ServerInfo.GET_POST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("VolleyTestLike", response.toString());

                        try {

                            JSONArray j = new JSONArray(response);

                            JSONObject jObject = j.getJSONObject(0);
                            mIsLike = jObject.getBoolean("is_like");
                            if(mIsLike) {
                                mLikeButton.setText("좋아요 완료");
                                mLikeButton.setBackgroundResource(R.drawable.liked_layout);

                                mLikeButton.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white_100));

                            } else {
                                mLikeButton.setText("좋아요");
                                mLikeButton.setBackgroundResource(R.drawable.like_layout);
                                mLikeButton.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black_100));

                            }

                            JSONArray dataArray = j.getJSONArray(1);
                            for(int i=0; i<dataArray.length(); i++) {
                                JSONObject jsonObject = dataArray.getJSONObject(i);
                                if (!jsonObject.isNull("img_url")) {
                                    mPicturePost.setImgUrl(jsonObject.getString("img_url"));
                                    Picasso.with(getApplicationContext()).load(mPicturePost.getImgUrl()).into(mPostImage);
                                }

                                if(!jsonObject.isNull("user_id")) {
                                    mPostUserId = jsonObject.getString("user_id");

                                    Picasso.with(getApplicationContext()).load(ServerInfo.getProfilePictureUrl(mPostUserId,200,200)).into(mPosterImage);

                                    Log.d("POSSOSOSOS",mPostUserId + " " + AccessToken.getCurrentAccessToken().getUserId());
                                }

                                if (!jsonObject.isNull("name")) {
                                    mTitleTxt.setText(jsonObject.getString("name"));
                                }
                                if (!jsonObject.isNull("likes")) {
                                    mLikesTxt.setText(jsonObject.getString("likes"));
                                    Log.d("VolleyTest", jsonObject.getString("likes"));

                                }
                                if (!jsonObject.isNull("category")) {
                                    if(getActivity() != null) {
                                         ((PostPagerDetailActivity)getActivity()).setCategory(CategoryNetwork.getInstance().getCat(jsonObject.getInt("category")));
                                    }

                                    // Log.d("VolleyTest", jsonObject.getInt("category")+"");
                                }
                                if (!jsonObject.isNull("location")) {
                                    if(getActivity() != null) {

                                        ((PostPagerDetailActivity) getActivity()).setLocation(LocationNetwork.getInstance().getLocation(jsonObject.getInt("location")));
                                    }
                                   //  Log.d("VolleyTest", jsonObject.getString("location"));
                                }
                                if (!jsonObject.isNull("description")) {
                                    mDescriptionTxt.setText(jsonObject.getString("description"));
                                    Log.d("VolleyTest", jsonObject.getString("description"));

                                }
                                if (!jsonObject.isNull("user_name")) {
                                    mPosterName.setText(jsonObject.getString("user_name"));
                                    Log.d("VolleyTest", jsonObject.getString("user_name"));

                                }
                                if (!jsonObject.isNull("user_likes")) {
                                    mPosterLevel.setText(LevelManager.getLevel(jsonObject.getInt("user_likes")) + "");
                                    Log.d("VolleyTest", jsonObject.getString("user_likes"));

                                } if(!jsonObject.isNull("date")) {

                                    String postDate = jsonObject.getString("date");
                                    mDateTxt.setText(postDate);
                                    // Log.d("POOOOSTTTDATE", "postDate: " + postDate + "     tomDate: " + DateUtil.getTomorrowDate());
                                    if(DateUtil.getTodayDate().getTime() < DateUtil.getTomorrowOfPostDate(postDate).getTime()) {
                                        mNewTxt.setVisibility(View.VISIBLE);
                                    }
                                }
                            }

                            Log.d("VolleyTest", "HERE3!");

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
                params.put("id", Integer.toString(mPostId));
                params.put("user_id", mPosterId);
                params.put("my_id", AccessToken.getCurrentAccessToken().getUserId());
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQue(jsonStringRequest);
    }

}
