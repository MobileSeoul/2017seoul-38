package com.gmail.jskapplications.seoulpicture.main.home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gmail.jskapplications.seoulpicture.R;
import com.gmail.jskapplications.seoulpicture.base.BaseFragment;
import com.gmail.jskapplications.seoulpicture.base.PostRecyclerFragment;
import com.gmail.jskapplications.seoulpicture.model.PicturePost;
import com.gmail.jskapplications.seoulpicture.network.VolleySingleton;
import com.gmail.jskapplications.seoulpicture.util.GlobalInfo;
import com.gmail.jskapplications.seoulpicture.util.LevelManager;
import com.gmail.jskapplications.seoulpicture.util.ServerInfo;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import kr.go.seoul.airquality.AirQualityTypeMini;
import kr.go.seoul.culturalevents.CulturalEventTypeMini;



public class HomeFragment extends BaseFragment {

    private CulturalEventTypeMini typeMiniCulture;
    private AirQualityTypeMini typeMiniAir;

    private NoticePagerViewAdapter pagerAdapter;
    private TextView mTitleTxt;
    private ImageView mPosterImage;
    private TextView mPosterName;
    private TextView mPosterLevel;


    private CountDownTimer timer;
    private ViewPager mPager;


    private Fragment mBestPostsFragment;
    private Fragment mNewPostsFragment;

    // Need to change this to be dynamic later
    private String[] mNotice;
    private String[] mTitle;
    private String[] mUserName;
    private String[] mTotalLikes;
    private String[] mUserIds;

    private ImageView icon_banner_1;
    private ImageView icon_banner_2;
    private ImageView icon_banner_3;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

   // private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        FragmentManager fm = getChildFragmentManager();

        icon_banner_1 = (ImageView) v.findViewById(R.id.icon_banner_1);
        icon_banner_2 = (ImageView) v.findViewById(R.id.icon_banner_2);
        icon_banner_3 = (ImageView) v.findViewById(R.id.icon_banner_3);
        mNotice = new String[5];
        mTitle = new String[5];;
        mUserName = new String[5];;
        mTotalLikes = new String[5];;
        mUserIds = new String[5];

       mTitleTxt = (TextView) v.findViewById(R.id.best_title);
       mPosterImage = (ImageView) v.findViewById(R.id.poster_img);
       mPosterName = (TextView) v.findViewById(R.id.poster_name);
       mPosterLevel = (TextView) v.findViewById(R.id.poster_level);



        // Posts in Grid - Start
        mBestPostsFragment = fm.findFragmentById(R.id.best_posts_container);
        if(mBestPostsFragment == null) {
            mBestPostsFragment = PostRecyclerFragment.newInstance("best");
            fm.beginTransaction()
                    .add(R.id.best_posts_container, mBestPostsFragment)
                    .commit();
        }

        mNewPostsFragment = fm.findFragmentById(R.id.new_posts_container);
        if(mNewPostsFragment == null) {
            mNewPostsFragment = PostRecyclerFragment.newInstance("new");
            fm.beginTransaction()
                    .add(R.id.new_posts_container, mNewPostsFragment)
                    .commit();
        }
        // Posts in Grid - End

        //getFewBest();
        // initImageScroll(v);

        /*
        typeMiniCulture = (CulturalEventTypeMini) v.findViewById(R.id.type_mini_culture);
        typeMiniCulture.setOpenAPIKey(GlobalInfo.SEOUL_API_KEY);
        */
        typeMiniAir = (AirQualityTypeMini) v.findViewById(R.id.type_mini_air);
        typeMiniAir.setOpenAPIKey(GlobalInfo.SEOUL_API_KEY);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getFewBest();
    }

    private void getFewBest() {
        String url = ServerInfo.GET_BESTS_WITH_DETAIL;

        StringRequest jsonStringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("VolleyTest~~!~", "FFF: "+ response.toString());
                        try {
                            JSONArray j = new JSONArray(response);

                            JSONObject jObject = j.getJSONObject(0);
                            //mTotalDataCnt = jObject.getInt("cnt");


                            JSONArray dataArray = j.getJSONArray(1);
                            for(int i=0; i<dataArray.length(); i++) {
                                JSONObject jsonObject = dataArray.getJSONObject(i);
                                PicturePost post = new PicturePost();

                                if( i == 0) {
                                    if (!jsonObject.isNull("img_url")) {
                                        String img_url = jsonObject.getString("img_url");
                                        //post.setImgUrl(jsonObject.getString("img_url"));
                                        mNotice[0] = img_url;
                                        mNotice[1] = img_url;


                                        mNotice[mNotice.length-1] = img_url;
                                    }
                                } else {

                                    if (!jsonObject.isNull("img_url")) {
                                        mNotice[i+1] = jsonObject.getString("img_url");
                                        // Log.d("CHECKIING", (i+1) + ": "+ mNotice[i+1]);
                                    }
                                }
                                if(!jsonObject.isNull("name")) {
                                    mTitle[i] = jsonObject.getString("name");
                                }
                                if(!jsonObject.isNull("user_name")) {
                                    mUserName[i] = jsonObject.getString("user_name");

                                }
                                if(!jsonObject.isNull("total_likes")) {
                                    mTotalLikes[i] = Integer.toString(jsonObject.getInt("total_likes"));
                                }
                                if(!jsonObject.isNull("user_id")) {
                                    mUserIds[i] = jsonObject.getString("user_id");
                                }
                            }
                            for(int i=0; i<5; i++) {
                                Log.d("TITTTTTLE", ""+
                                        mTitle[i]);
                            }

                            if(pagerAdapter != null) {
                                pagerAdapter.notifyDataSetChanged();
                            } else {
                                initImageScroll(getView());
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
                params.put("start", Integer.toString(0));
                params.put("end", Integer.toString(3));
                return params;

            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQue(jsonStringRequest);
    }
    // TODO: Rename method, update argument and hook method into UI event
    /*
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
*/
    /*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
*/
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    */


    // 박물관 소식 - Start
    private void initImageScroll(View v) {
        mPager = (NoticeViewPager) v.findViewById(R.id.pager);

        pagerAdapter = new NoticePagerViewAdapter(getActivity(), mNotice);
        mPager.setAdapter(pagerAdapter);
        // 실제 첫번째 공지 위치로 설정한다
        mPager.setCurrentItem(1);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    switch (position) {
                        case 0://
                            icon_banner_1.setBackgroundResource(R.color.black_30);
                            icon_banner_2.setBackgroundResource(R.color.black_30);
                            icon_banner_3.setBackgroundResource(R.color.black_100);

                            mTitleTxt.setText(mTitle[2]);
                            Picasso.with(getActivity().getApplicationContext()).load(ServerInfo.getProfilePictureUrl(mUserIds[2],200,200)).into(mPosterImage);

                            mPosterName.setText(mUserName[2]);
                            if(mTotalLikes[2
                                    ] != null) {
                            mPosterLevel.setText(LevelManager.getLevel(Integer.parseInt(mTotalLikes[2])));
                            }

                            break;
                        case 1:
                            icon_banner_1.setBackgroundResource(R.color.black_100);
                            icon_banner_2.setBackgroundResource(R.color.black_30);
                            icon_banner_3.setBackgroundResource(R.color.black_30);

                            mTitleTxt.setText(mTitle[0]);
                            Picasso.with(getActivity().getApplicationContext()).load(ServerInfo.getProfilePictureUrl(mUserIds[0],200,200)).into(mPosterImage);

                            mPosterName.setText(mUserName[0]);
                            if(mTotalLikes[0] != null) {
                                mPosterLevel.setText(LevelManager.getLevel(Integer.parseInt(mTotalLikes[0]))+"");
                            }
                            break;
                        case 2:
                            icon_banner_1.setBackgroundResource(R.color.black_30);
                            icon_banner_2.setBackgroundResource(R.color.black_100);
                            icon_banner_3.setBackgroundResource(R.color.black_30);
                            mTitleTxt.setText(mTitle[1]);
                            Picasso.with(getActivity().getApplicationContext()).load(ServerInfo.getProfilePictureUrl(mUserIds[1],200,200)).into(mPosterImage);

                            mPosterName.setText(mUserName[1]);

                            if(mTotalLikes[1] != null) {

                                mPosterLevel.setText(LevelManager.getLevel(Integer.parseInt(mTotalLikes[1])) + "");
                            }
                            break;
                        case 3:
                            icon_banner_1.setBackgroundResource(R.color.black_30);
                            icon_banner_2.setBackgroundResource(R.color.black_30);
                            icon_banner_3.setBackgroundResource(R.color.black_100);

                            mTitleTxt.setText(mTitle[2]);
                            Picasso.with(getActivity().getApplicationContext()).load(ServerInfo.getProfilePictureUrl(mUserIds[2],200,200)).into(mPosterImage);

                            mPosterName.setText(mUserName[2]);
                            if(mTotalLikes[2] != null) {

                                mPosterLevel.setText(LevelManager.getLevel(Integer.parseInt(mTotalLikes[2])) + "");
                            }
                            break;
                        case 4:
                            icon_banner_1.setBackgroundResource(R.color.black_100);
                            icon_banner_2.setBackgroundResource(R.color.black_30);
                            icon_banner_3.setBackgroundResource(R.color.black_30);

                            mTitleTxt.setText(mTitle[0]);
                            Picasso.with(getActivity().getApplicationContext()).load(ServerInfo.getProfilePictureUrl(mUserIds[0],200,200)).into(mPosterImage);

                            mPosterName.setText(mUserName[0]);
                            if(mTotalLikes[0] != null) {

                                mPosterLevel.setText(LevelManager.getLevel(Integer.parseInt(mTotalLikes[0])) + "");
                            }

                            break;
                    }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    if (timer != null) {
                        timer.cancel();
                    }
                } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (timer != null) {
                        timer.start();
                    }

                    int curr = mPager.getCurrentItem();
                    int lastReal = mPager.getAdapter().getCount() -2;
                    if (curr == 0) {
                        mPager.setCurrentItem(lastReal, false);
                    } else if (curr > lastReal) {
                        mPager.setCurrentItem(1, false);
                    }
                }
            }
        });

        // 자동스크롤 타이머 설정
        timer = new CountDownTimer(2000, 2000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                int currentPosition = mPager.getCurrentItem();
                mPager.setCurrentItem(currentPosition + 1);
            }
        };
        timer.start();
    }
    // 박물관 소식 - End
}
