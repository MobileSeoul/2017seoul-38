package com.gmail.jskapplications.seoulpicture.location;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gmail.jskapplications.seoulpicture.R;
import com.gmail.jskapplications.seoulpicture.base.BaseFragment;
import com.gmail.jskapplications.seoulpicture.common.post_detail.PostPagerDetailActivity;
import com.gmail.jskapplications.seoulpicture.model.PicturePost;
import com.gmail.jskapplications.seoulpicture.network.CategoryNetwork;
import com.gmail.jskapplications.seoulpicture.network.SortNetwork;
import com.gmail.jskapplications.seoulpicture.network.VolleySingleton;
import com.gmail.jskapplications.seoulpicture.util.DateUtil;
import com.gmail.jskapplications.seoulpicture.util.ServerInfo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends BaseFragment implements View.OnClickListener {


    private static final int CHANGED_PICTURE = 1;
    private static final int DELETE_PICTURE = 2;

    private Spinner mCategorySpinner;
    private Spinner mSortSpinner;

    private int num;
    private int mTotalDataCnt;
    private Button mShowMoreBtn;
    //private boolean mInit;

    private static final String USER_ID = "USER_ID";
    private static final String SPAN_CNT = "SPAN_CNT";
    private static final String LOCATION_ID = "LOCATION_ID";

    private String mUserId;

    private int mLocationId;


    private boolean isStart;

    private Button mSearchBtn;

    // For Program RecyclerView - Start
    private RecyclerView mImageRecyclerView;
    private LocationFragment.ImageAdapter mAdapter;
    // For Program RecyclerView - End
    ArrayList<PicturePost> posts = new ArrayList<>();


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    private int mSpanCnt = 2;
    //private int mCategory;

    public static LocationFragment newInstance(String userId, int spanCnt, int location_id) {
        Bundle args = new Bundle();
        args.putString(USER_ID, userId);
        args.putInt(SPAN_CNT, spanCnt);
        args.putInt(LOCATION_ID, location_id);
        LocationFragment fragment = new LocationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocationId = getArguments().getInt(LOCATION_ID, 0);

        mUserId = getArguments().getString(USER_ID);
        Log.d("USERIDDD", mUserId);
        mSpanCnt = getArguments().getInt(SPAN_CNT);
        isStart = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        //mInit = true;
        mImageRecyclerView = (RecyclerView) view.findViewById(R.id.image_recycler_view);
        mImageRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), mSpanCnt));
        mImageRecyclerView.setNestedScrollingEnabled(false);
        /*
        int topMargin = getResources().getDimensionPixelOffset(R.dimen.grid_top_margin);
        int middleMargin = getResources().getDimensionPixelOffset(R.dimen.grid_middle_margin);
        int margin = getResources().getDimensionPixelOffset(R.dimen.grid_margin);
        */

        // mProgramRecyclerView.addItemDecoration(new GridItemDecoration(topMargin, middleMargin, margin));

        mShowMoreBtn = (Button) view.findViewById(R.id.show_more_btn);
        mShowMoreBtn.setOnClickListener(this);

        mSearchBtn = (Button) view.findViewById(R.id.search_btn);
        mSearchBtn.setOnClickListener(this);

        // 카테고리 스피너 - START
        mCategorySpinner = (Spinner) view.findViewById(R.id.category_spinner);
        ArrayAdapter<String> categorySpinnerArrayAdapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_spinner_item, CategoryNetwork.getInstance().getCategory());
        categorySpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategorySpinner.setAdapter(categorySpinnerArrayAdapter);

        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // 카테고리 스피너 - END

        // 정렬 스피너 - START
        mSortSpinner = (Spinner) view.findViewById(R.id.sort_spinner);
        ArrayAdapter<String> sortSpinnerArrayAdapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_spinner_item, SortNetwork.getInstance().getSorts());
        sortSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSortSpinner.setAdapter(sortSpinnerArrayAdapter);

        mSortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // 정렬 스피너 - END

        mAdapter = new LocationFragment.ImageAdapter(posts);
        mImageRecyclerView.setAdapter(mAdapter);

        updateUI();

        return view;
    }



    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.show_more_btn:
                updateUI();
                break;
            case R.id.search_btn:
                posts.clear();
                updateUI();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(!isStart) {
            //List<PicturePost> programs = ((BaseApplication) (getActivity().getApplicationContext())).getDbManagerR().selectAllProgram(mCategory);
            // PicassoUtil.fetchProgramImages(getActivity(), mCategory);

            String url = ServerInfo.GET_POSTS;


            StringRequest jsonStringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("VolleyTest", response.toString());
                            try {
                                JSONArray j = new JSONArray(response);

                                JSONObject jObject = j.getJSONObject(0);
                                mTotalDataCnt = jObject.getInt("cnt");


                                JSONArray dataArray = j.getJSONArray(1);

                                // posts.clear();
                                for(int i=0; i<dataArray.length(); i++) {
                                    JSONObject jsonObject = dataArray.getJSONObject(i);
                                    PicturePost post = new PicturePost();
                                    if (!jsonObject.isNull("img_url")) {
                                        post.setImgUrl(jsonObject.getString("img_url"));
                                    }
                                    if(!jsonObject.isNull("id")) {
                                        post.setId(jsonObject.getInt("id"));
                                    }
                                    if(!jsonObject.isNull("user_id")) {
                                        post.setmUserId(jsonObject.getString("user_id"));
                                    }
                                    if(!jsonObject.isNull("date")) {
                                        post.setmDate(jsonObject.getString("date"));
                                        // String postDate = jsonObject.getString("date");
                                    }
                                    if(i < posts.size()) {
                                        if(!posts.get(i).getImgUrl().equals(post.getImgUrl())) {
                                            posts.set(i, post);
                                            mAdapter.notifyItemChanged(i);
                                            Log.d("HEEEEEEEEEEERR", "ASDSADASDSAD : " +i);
                                        }
                                    } else {
                                        posts.add(post);
                                        mAdapter.notifyItemChanged(i);
                                    }
                                    // posts.add(post);
                                    Log.d("TLQKFK", posts.size()-1+"");
                                    // mAdapter.notifyItemInserted(posts.size()-1);

                                }

                                Log.d("mPOSTSSIZE!!!", ""+posts.size());
                                Log.d("mPOSTSSIZE!!!", "::::"+mAdapter.getItemCount());

                                mShowMoreBtn.setText("더 보기 " + "" + posts.size() + "/" + mTotalDataCnt);
                                if(posts.size() == mTotalDataCnt) {
                                    mShowMoreBtn.setEnabled(false);
                                } else {
                                    mShowMoreBtn.setEnabled(true);
                                    Log.d("????!!!", "BUTTON!!!");
                                }
                                // mAdapter.addData(posts);
                                mAdapter.notifyDataSetChanged();

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
                    params.put("location_id", Integer.toString(mLocationId));
                    params.put("category_id", Integer.toString(mCategorySpinner.getSelectedItemPosition()));
                    params.put("sort_id", Integer.toString(mSortSpinner.getSelectedItemPosition()));
                    params.put("start", Integer.toString(0));
                    int mult_of_six = 6;
                    while(posts.size() > mult_of_six) {
                        mult_of_six += 6;
                    }

                    params.put("end", Integer.toString(mult_of_six));

                    params.put("user_id", mUserId);
                    return params;

                }
            };

            VolleySingleton.getInstance(getActivity()).addToRequestQue(jsonStringRequest);
        } else {
            isStart = false;
        }

    }

    public void setLocationId(int locationId) {
        mLocationId = locationId;
    }

    public void updateUIwithClear() {
        posts.clear();
        updateUI();
    }

    public void updateUI() {
        //List<PicturePost> programs = ((BaseApplication) (getActivity().getApplicationContext())).getDbManagerR().selectAllProgram(mCategory);
        // PicassoUtil.fetchProgramImages(getActivity(), mCategory);

        String url = ServerInfo.GET_POSTS;

        StringRequest jsonStringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("VolleyTest", response.toString());
                        try {
                            JSONArray j = new JSONArray(response);

                            JSONObject jObject = j.getJSONObject(0);
                            mTotalDataCnt = jObject.getInt("cnt");


                            JSONArray dataArray = j.getJSONArray(1);
                            for(int i=0; i<dataArray.length(); i++) {
                                JSONObject jsonObject = dataArray.getJSONObject(i);
                                PicturePost post = new PicturePost();
                                if (!jsonObject.isNull("img_url")) {
                                    post.setImgUrl(jsonObject.getString("img_url"));
                                }
                                if(!jsonObject.isNull("id")) {
                                    post.setId(jsonObject.getInt("id"));
                                }
                                if(!jsonObject.isNull("user_id")) {
                                    post.setmUserId(jsonObject.getString("user_id"));
                                }
                                if(!jsonObject.isNull("date")) {

                                    post.setmDate(jsonObject.getString("date"));
                                    // String postDate = jsonObject.getString("date");
                                }
                                posts.add(post);
                                Log.d("TLQKFK", posts.size()-1+"");
                                // mAdapter.notifyItemInserted(posts.size()-1);

                            }

                            Log.d("mPOSTSSIZE!!!", ""+posts.size());
                            Log.d("mPOSTSSIZE!!!", "::::"+mAdapter.getItemCount());

                            mShowMoreBtn.setText("더 보기 " + "" + posts.size() + "/" + mTotalDataCnt);
                            if(posts.size() == mTotalDataCnt) {
                                mShowMoreBtn.setEnabled(false);
                            } else {
                                mShowMoreBtn.setEnabled(true);
                                Log.d("????!!!", "BUTTON!!!");
                            }
                           // mAdapter.addData(posts);
                             mAdapter.notifyDataSetChanged();

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
                params.put("category_id", Integer.toString(mCategorySpinner.getSelectedItemPosition()));
                params.put("location_id", Integer.toString(mLocationId));
                params.put("sort_id", Integer.toString(mSortSpinner.getSelectedItemPosition()));
                params.put("start", Integer.toString(posts.size()));
                params.put("end", Integer.toString(posts.size()+6));
                params.put("user_id", mUserId);
                return params;

            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQue(jsonStringRequest);
    }

    public LocationFragment.ImageAdapter getRecyclerAdapter() {
        return mAdapter;
    }

    // Used for exhibition recycler view - Start
    private class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView mImageView;
        private PicturePost mPost;
        private TextView mNewTxt;

        public ImageHolder(View itemView) {
            super(itemView);
            mNewTxt = (TextView) itemView.findViewById(R.id.new_txt);

            mImageView = (ImageView) itemView.findViewById(R.id.post_image);
            itemView.setOnClickListener(this);
            // mTitleTextView = (TextView) itemView;
        }

        public void bindProgram(PicturePost post) {
            mPost = post;
            Picasso.with(getActivity().getApplicationContext()).load(post.getImgUrl()).resize(300,300).into(mImageView);
            Log.d("PICCASSOO", "PPPPICCASSO: " + post.getImgUrl());

            if(DateUtil.getTodayDate().getTime() < DateUtil.getTomorrowOfPostDate(mPost.getmDate()).getTime()) {
                mNewTxt.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            Log.d("ID!!!", mPost.getId() + "");
            v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.home_card_click));
            Intent i = new Intent(getActivity(), PostPagerDetailActivity.class);
            i.putExtra(PostPagerDetailActivity.POST_ID, mPost.getId());
            i.putExtra(PostPagerDetailActivity.POST_LIST, posts);
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivityForResult(i, DELETE_PICTURE);
        }
    }

    private class ImageAdapter extends RecyclerView.Adapter<LocationFragment.ImageHolder> {
        private List<PicturePost> mPosts;

        // Allows to remember the last item shown on screen
        private int lastPosition = -1;

        public ImageAdapter(List<PicturePost> programs) {
            mPosts = programs;
        }

        public void addData(List<PicturePost> posts) {
            this.mPosts= posts;
            notifyDataSetChanged();
        }

        @Override
        public LocationFragment.ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.grid_item_post, parent, false);
            return new LocationFragment.ImageHolder(view);
        }

        @Override
        public void onBindViewHolder(LocationFragment.ImageHolder holder, int position) {
            PicturePost programs = mPosts.get(position);
            holder.bindProgram(programs);
            // holder.mTextView.setText(programs.getTitle());
            setAnimation(holder.itemView,position);

        }

        @Override
        public int getItemCount() {
            return mPosts.size();
        }

        private void setAnimation(View viewToAnimate, int position) {

            Animation animation = null;
            if(position > lastPosition) {
                animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }

            /*
            else {
                animation =AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);
            }

            */
        }
    }
    // Used for exhibition recycler view - End

    public void deletePost(int id) {
        for (int i =0; i<posts.size(); i++) {

            if(posts.get(i).getId() == id) {
                posts.remove(i);
                if(mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CHANGED_PICTURE && resultCode == RESULT_OK && data!=null) {

        } else if(requestCode == DELETE_PICTURE && resultCode == RESULT_OK && data!=null) {

            int id = data.getIntExtra("DELETE", -1);
            if(id != -1) {
                deletePost(id);
                mShowMoreBtn.setText("더 보기 " + "" + posts.size() + "/" + (mTotalDataCnt-1));
            }
        }
    }
}
