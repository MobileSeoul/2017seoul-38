package com.gmail.jskapplications.seoulpicture.user;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.gmail.jskapplications.seoulpicture.model.SingleUser;
import com.gmail.jskapplications.seoulpicture.model.User;
import com.gmail.jskapplications.seoulpicture.network.CategoryNetwork;
import com.gmail.jskapplications.seoulpicture.network.SortNetwork;
import com.gmail.jskapplications.seoulpicture.network.VolleySingleton;
import com.gmail.jskapplications.seoulpicture.util.LevelManager;
import com.gmail.jskapplications.seoulpicture.util.ServerInfo;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserRecyclerActivityFragment extends BaseFragment implements View.OnClickListener {


    private static final int CHANGED_PICTURE = 1;
    private static final int DELETE_PICTURE = 2;

    private Spinner mCategorySpinner;
    private Spinner mSortSpinner;

    private int num;
    private int mTotalDataCnt;
    private Button mShowMoreBtn;
    //private boolean mInit;

    private String mUserId;

    private boolean isStart;

    private Button mSearchBtn;

    // For Program RecyclerView - Start
    private RecyclerView mImageRecyclerView;
    private UserRecyclerActivityFragment.UserAdapter mAdapter;
    // For Program RecyclerView - End
    ArrayList<SingleUser> users = new ArrayList<>();


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    //private int mCategory;

    public static UserRecyclerActivityFragment newInstance() {
        Bundle args = new Bundle();
        UserRecyclerActivityFragment fragment = new UserRecyclerActivityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isStart = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_recycler, container, false);
        //mInit = true;
        mImageRecyclerView = (RecyclerView) view.findViewById(R.id.image_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mImageRecyclerView.setLayoutManager(layoutManager);
        mImageRecyclerView.setNestedScrollingEnabled(false);
        /*
        int topMargin = getResources().getDimensionPixelOffset(R.dimen.grid_top_margin);
        int middleMargin = getResources().getDimensionPixelOffset(R.dimen.grid_middle_margin);
        int margin = getResources().getDimensionPixelOffset(R.dimen.grid_margin);
        */

        // mProgramRecyclerView.addItemDecoration(new GridItemDecoration(topMargin, middleMargin, margin));

        mShowMoreBtn = (Button) view.findViewById(R.id.show_more_btn);
        mShowMoreBtn.setOnClickListener(this);

        /*
        mSearchBtn = (Button) view.findViewById(R.id.search_btn);
        mSearchBtn.setOnClickListener(this);*/


        mAdapter = new UserRecyclerActivityFragment.UserAdapter(users);
        mImageRecyclerView.setAdapter(mAdapter);

        //updateUI();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.show_more_btn:
                updateUI();
                break;
            /*
            case R.id.search_btn:
                posts.clear();
                updateUI();
                break;
                */
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // users.clear();
        updateUI();
    }

    private void updateUI() {
        //List<PicturePost> programs = ((BaseApplication) (getActivity().getApplicationContext())).getDbManagerR().selectAllProgram(mCategory);
        // PicassoUtil.fetchProgramImages(getActivity(), mCategory);

        String url = ServerInfo.GET_USER_LIST;

        StringRequest jsonStringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("VolleyTestS","Users: " + response.toString());
                        try {
                            JSONArray j = new JSONArray(response);

                            JSONObject jObject = j.getJSONObject(0);
                            mTotalDataCnt = jObject.getInt("cnt");


                            JSONArray dataArray = j.getJSONArray(1);
                            for(int i=0; i<dataArray.length(); i++) {
                                JSONObject jsonObject = dataArray.getJSONObject(i);
                                SingleUser singleUser = new SingleUser();
                                if (!jsonObject.isNull("userid")) {
                                    singleUser.setmId(jsonObject.getString("userid"));
                                    singleUser.setmImgUrl(ServerInfo.getProfilePictureUrl(singleUser.getmId(),200,200));
                                    //post.setImgUrl(jsonObject.getString("userid"));
                                }
                                if(!jsonObject.isNull("user_name")) {
                                    singleUser.setmName(jsonObject.getString("user_name"));
                                }
                                if(!jsonObject.isNull("total_likes")) {
                                    singleUser.setmTotalLikes(jsonObject.getInt("total_likes"));
                                }
                                if(i < users.size()) {
                                    if(!users.get(i).getmId().equals(singleUser.getmId())) {
                                        users.set(i, singleUser);
                                        mAdapter.notifyItemChanged(i);
                                        Log.d("HEEEEEEEEEEERR", "ASDSADASDSAD : " +i);
                                    }
                                } else {
                                    users.add(singleUser);
                                    mAdapter.notifyItemChanged(i);
                                }
                                // users.add(singleUser);
                                Log.d("TLQKFK", users.size()-1+"");
                                // mAdapter.notifyItemInserted(posts.size()-1);

                            }

                            Log.d("mPOSTSSIZE!!!", ""+users.size());
                            Log.d("mPOSTSSIZE!!!", "::::"+mAdapter.getItemCount());

                            mShowMoreBtn.setText("더 보기 " + "" + users.size() + "/" + mTotalDataCnt);
                            if(users.size() == mTotalDataCnt) {
                                mShowMoreBtn.setEnabled(false);
                            } else {
                                mShowMoreBtn.setEnabled(true);
                                Log.d("????!!!", "BUTTON!!!");
                            }
                            // mAdapter.addData(posts);
                            // mAdapter.notifyDataSetChanged();

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
                int mult_of_six = 6;
                while(users.size() > mult_of_six) {
                    mult_of_six += 6;
                }
                Log.d("USERSSIZEE", "mult_of_size: " + mult_of_six+"");
                params.put("end", Integer.toString(users.size()+6));

                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQue(jsonStringRequest);
    }

    public UserRecyclerActivityFragment.UserAdapter getRecyclerAdapter() {
        return mAdapter;
    }

    // Used for exhibition recycler view - Start
    private class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView mImageView;
        private TextView mName;
        private TextView mLevel;
        private TextView mTotalLikes;
        private TextView mRankTxt;

        private SingleUser mUser;

        public ImageHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.user_image);
            mName = (TextView) itemView.findViewById(R.id.user_name);
            mLevel = (TextView) itemView.findViewById(R.id.user_level);
            mTotalLikes = (TextView) itemView.findViewById(R.id.user_likes);
            mRankTxt = (TextView) itemView.findViewById(R.id.rank_txt);
            itemView.setOnClickListener(this);
            // mTitleTextView = (TextView) itemView;
        }

        public void bindProgram(SingleUser user, int rank) {
            Picasso.with(getActivity().getApplicationContext()).load(user.getmImgUrl()).into(mImageView);
            mName.setText(user.getmName());
            mLevel.setText(LevelManager.getLevel(user.getmTotalLikes())+"");
            mTotalLikes.setText(user.getmTotalLikes()+"");
            mRankTxt.setText(rank+"");
            if(rank == 1) {
                mRankTxt.setTextColor(ContextCompat.getColor(getActivity(),R.color.gold));
            } else if(rank == 2) {
                mRankTxt.setTextColor(ContextCompat.getColor(getActivity(),R.color.silver));
            } else if(rank == 3) {
                mRankTxt.setTextColor(ContextCompat.getColor(getActivity(),R.color.bronze));
            }
            Log.d("PICCASSOO", "PPPPICCASSO: " + user.getmImgUrl());
            mUser = user;
        }

        @Override
        public void onClick(View v) {
            Log.d("ID!!!", mUser.getmId() + "");
            v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.home_card_click));
            Intent i = new Intent(getActivity(), UserDetailActivity.class);
            i.putExtra(UserDetailActivity.USER_ID, mUser.getmId());
            i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
        }
    }

    private class UserAdapter extends RecyclerView.Adapter<UserRecyclerActivityFragment.ImageHolder> {
        private List<SingleUser> mUsers;

        // Allows to remember the last item shown on screen
        private int lastPosition = -1;


        public UserAdapter(List<SingleUser> programs) {
            mUsers = programs;
        }

        public void addData(List<SingleUser> posts) {
            this.mUsers= posts;
            notifyDataSetChanged();
        }

        @Override
        public UserRecyclerActivityFragment.ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_user, parent, false);
            return new UserRecyclerActivityFragment.ImageHolder(view);
        }

        @Override
        public void onBindViewHolder(UserRecyclerActivityFragment.ImageHolder holder, int position) {
            SingleUser users = mUsers.get(position);
            holder.bindProgram(users, position+1);
            // holder.mTextView.setText(programs.getTitle());
            setAnimation(holder.itemView,position);

        }

        @Override
        public int getItemCount() {
            return mUsers.size();
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
}
