package com.gmail.jskapplications.seoulpicture.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gmail.jskapplications.seoulpicture.R;
import com.gmail.jskapplications.seoulpicture.common.post_detail.PostPagerDetailActivity;
import com.gmail.jskapplications.seoulpicture.main.image_list.ImageRecyclerFragment;
import com.gmail.jskapplications.seoulpicture.model.PicturePost;
import com.gmail.jskapplications.seoulpicture.network.VolleySingleton;
import com.gmail.jskapplications.seoulpicture.util.DateUtil;
import com.gmail.jskapplications.seoulpicture.util.ServerInfo;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by for on 2017-10-14.
 */

public class PostRecyclerFragment extends BaseFragment {
    private static final String TYPE_STRING = "TYPE_STRING";


    // For Program RecyclerView - Start
    private RecyclerView mProgramRecyclerView;
    private PostAdapter mAdapter;
    // For Program RecyclerView - End
    ArrayList<PicturePost> posts = new ArrayList<>();


    private String mType;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    private static final int SPAN_COUNT = 3;
    //private int mCategory;

    public static PostRecyclerFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString(TYPE_STRING, type);
        PostRecyclerFragment fragment = new PostRecyclerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mType = getArguments().getString(TYPE_STRING);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_recyclerview, container, false);


        mProgramRecyclerView = (RecyclerView) view.findViewById(R.id.program_recycler_view);
        mProgramRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),SPAN_COUNT));

        int topMargin = getResources().getDimensionPixelOffset(R.dimen.grid_top_margin);
        int middleMargin = getResources().getDimensionPixelOffset(R.dimen.grid_middle_margin);
        int margin = getResources().getDimensionPixelOffset(R.dimen.grid_margin);


        // mProgramRecyclerView.addItemDecoration(new GridItemDecoration(topMargin, middleMargin, margin));

        mAdapter = new PostAdapter(posts);
        mProgramRecyclerView.setAdapter(mAdapter);
        // updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        //List<PicturePost> programs = ((BaseApplication) (getActivity().getApplicationContext())).getDbManagerR().selectAllProgram(mCategory);
        // PicassoUtil.fetchProgramImages(getActivity(), mCategory);
        // posts.clear();
        String url;
        if(mType.equals("best")) {
            url = ServerInfo.GET_BESTS;
        } else {
            url = ServerInfo.GET_NEWS;
        }

        StringRequest jsonStringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("VolleyTest~~~~", response.toString());
                        try {
                            JSONArray j = new JSONArray(response);

                            JSONObject jObject = j.getJSONObject(0);

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
                params.put("start", Integer.toString(0));
                params.put("end", Integer.toString(3));

                return params;

            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQue(jsonStringRequest);
    }

    // Used for exhibition recycler view - Start
    private class PostHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView mImageView;
        private PicturePost mPost;
        private TextView mBestTxt;
        private TextView mNewTxt;


        public PostHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.post_image);
            mBestTxt = (TextView) itemView.findViewById(R.id.best_txt);
            mNewTxt = (TextView) itemView.findViewById(R.id.new_txt);


            itemView.setOnClickListener(this);
            // mTitleTextView = (TextView) itemView;
        }

        public void bindProgram(PicturePost post, int rank) {
            mPost = post;

            if(mType.equals("best")) {
                mBestTxt.setVisibility(View.VISIBLE);
            } else {

                if(DateUtil.getTodayDate().getTime() < DateUtil.getTomorrowOfPostDate(mPost.getmDate()).getTime()) {
                    mNewTxt.setVisibility(View.VISIBLE);
                }

            }

            final int radius = 50;
            final int margin = 0;
            final Transformation transformation = new RoundedCornersTransformation(radius, margin);
            Picasso.with(getActivity().getApplicationContext()).load(post.getImgUrl()).resize(300,300).transform(transformation).into(mImageView);
            Log.d("PICCASSOO", "PPPPICCASSO: " + post.getImgUrl());
        }

        @Override
        public void onClick(View v) {
            v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.home_card_click));
            Intent i = new Intent(getActivity(), PostPagerDetailActivity.class);
            i.putExtra(PostPagerDetailActivity.POST_ID, mPost.getId());
            i.putExtra(PostPagerDetailActivity.POST_LIST, posts);
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
        }
    }

    private class PostAdapter extends RecyclerView.Adapter<PostHolder> {
        private List<PicturePost> mPosts;

        // Allows to remember the last item shown on screen
        private int lastPosition = -1;

        public PostAdapter(List<PicturePost> programs) {
            mPosts = programs;
        }

        @Override
        public PostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.grid_main_item_post, parent, false);
            return new PostHolder(view);
        }

        @Override
        public void onBindViewHolder(PostHolder holder, int position) {
            PicturePost programs = mPosts.get(position);
            holder.bindProgram(programs, position+1);
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
                animation =AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
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
}


