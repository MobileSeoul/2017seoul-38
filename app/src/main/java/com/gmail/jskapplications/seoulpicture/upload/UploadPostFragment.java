package com.gmail.jskapplications.seoulpicture.upload;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.gmail.jskapplications.seoulpicture.R;
import com.gmail.jskapplications.seoulpicture.base.BaseFragment;
import com.gmail.jskapplications.seoulpicture.base.PostRecyclerFragment;
import com.gmail.jskapplications.seoulpicture.common.post_detail.PostDetailFragment;
import com.gmail.jskapplications.seoulpicture.network.CategoryNetwork;
import com.gmail.jskapplications.seoulpicture.network.LocationNetwork;
import com.gmail.jskapplications.seoulpicture.network.VolleySingleton;
import com.gmail.jskapplications.seoulpicture.util.ServerInfo;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by for on 2017-10-14.
 */

public class UploadPostFragment extends BaseFragment implements View.OnClickListener {

    private static final String ARG_ID = "ARG_ID";

    private Bitmap mPostImageBitmap;
    private ImageView mPostImage;
    private Spinner mLocSpinner;
    private Spinner mCategorySpinner;
    private EditText mNameEditText;
    private EditText mDescriptionEditText;

    private Button mPostButton;
    private Button mEditButton;

    private ImgUploadDialogFragment mImgUploadDialog;

    private String mPostImageUrl;


    private int mPostId;
    public static UploadPostFragment newInstance(int postId) {
        UploadPostFragment fragment = new UploadPostFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, postId);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPostId = getArguments().getInt(ARG_ID);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_upload_posts, container, false);

        mPostImage = (ImageView) v.findViewById(R.id.post_image);
        mPostImage.setOnClickListener(this);

        mNameEditText = (EditText) v.findViewById(R.id.name_edit_text);
        mDescriptionEditText = (EditText) v.findViewById(R.id.description_edit_text);
        mPostButton = (Button) v.findViewById(R.id.post_button);
        mPostButton.setOnClickListener(this);

        mEditButton = (Button) v.findViewById(R.id.edit_button);
        mEditButton.setOnClickListener(this);

        // 위치 스피너 - START
        mLocSpinner = (Spinner) v.findViewById(R.id.location_spinner);
        ArrayAdapter<String> locSpinnerArrayAdapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_spinner_item, LocationNetwork.getInstance().getLocationsUpload());
        locSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLocSpinner.setAdapter(locSpinnerArrayAdapter);
        // 위치 스피너 - END


        // 카테고리 스피너 - START
        mCategorySpinner = (Spinner) v.findViewById(R.id.category_spinner);
        ArrayAdapter<String> categorySpinnerArrayAdapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_spinner_item, CategoryNetwork.getInstance().getCategoryUpload());
        categorySpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategorySpinner.setAdapter(categorySpinnerArrayAdapter);
        // 카테고리 스피너 - END


        Log.d("MPOSTIIIIDDD", mPostId+"");
        if(mPostId != -1) {
            getPost();
            mEditButton.setVisibility(View.VISIBLE);
            mPostButton.setVisibility(View.GONE);
        } else {
            mEditButton.setVisibility(View.GONE);
            mPostButton.setVisibility(View.VISIBLE);
        }



        return v;
    }

    private void getPost() {
        Log.d("VolleyTest", "HERE!");
        StringRequest jsonStringRequest = new StringRequest(Request.Method.POST, ServerInfo.GET_POST_FOR_EDIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("VolleyTest", response.toString());

                        try {

                            JSONArray j = new JSONArray(response);

                            JSONObject jObject = j.getJSONObject(0);
                            /*
                            if(mIsLike) {
                            } else {
                            }
                            */
                            JSONArray dataArray = j.getJSONArray(1);
                            for(int i=0; i<dataArray.length(); i++) {
                                JSONObject jsonObject = dataArray.getJSONObject(i);
                                if (!jsonObject.isNull("img_url")) {
                                    mPostImageUrl = jsonObject.getString("img_url");
                                    Picasso.with(getApplicationContext()).load(mPostImageUrl).into(mPostImage);
                                }

                                if (!jsonObject.isNull("name")) {
                                    mNameEditText.setText(jsonObject.getString("name"));
                                }

                                if (!jsonObject.isNull("category")) {
                                    mCategorySpinner.setSelection(jsonObject.getInt("category"));
                                    Log.d("VolleyTest", jsonObject.getInt("category")+"");

                                }
                                if (!jsonObject.isNull("location")) {
                                    mLocSpinner.setSelection(jsonObject.getInt("location"));
                                    Log.d("VolleyTest", jsonObject.getString("location"));

                                }
                                if (!jsonObject.isNull("description")) {
                                    mDescriptionEditText.setText(jsonObject.getString("description"));
                                    Log.d("VolleyTest", jsonObject.getString("description"));

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
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQue(jsonStringRequest);
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.home_card_click));
        switch(v.getId()) {
            case R.id.post_image:
                CropImage.activity()
                        .setAspectRatio(1,1)
                        .start(getActivity());

                // showImgUploadDialog();
                break;
            case R.id.post_button:
                sendPostToServer();
                break;

            case R.id.edit_button:
                Log.d("EDDITTTTTING!!", "HERERE");
                sendEditToServer();
                break;
        }
    }
    private void sendEditToServer() {
        Log.d("EDDITTTTTING!!", "HERERE");
        if(mPostImageBitmap == null && mPostImageUrl == null) {
            Toast.makeText(getActivity(), "이미지를 업로드 하세요", Toast.LENGTH_SHORT).show();
        } else if(mNameEditText.getText().toString().length() <= 0) {
            Toast.makeText(getActivity(), "이름을 입력하세요", Toast.LENGTH_SHORT).show();
        } else if(mCategorySpinner.getSelectedItem() == null || mCategorySpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(getActivity(), "카테고리를 선택하세요", Toast.LENGTH_SHORT).show();
        } else if(mLocSpinner.getSelectedItem() == null || mLocSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(getActivity(), "지역을 선택하세요", Toast.LENGTH_SHORT).show();
        } else if(mDescriptionEditText.getText().toString().length() <= 0) {
            Toast.makeText(getActivity(), "상세설명을 입력하세요", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("EDDITTTTTING!!", "HE!!!!RERE");

            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("포스트 편집");
            alert.setMessage("정말로 포스트를 편집 하시겠습니까?");
            alert.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sendEditDataToServer();
                }
            });
            alert.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }
    }
    private void sendEditDataToServer() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerInfo.EDIT_UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONNNNNSE", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String Response = jsonObject.getString("response");
                            getActivity().finish();
                        } catch(JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("post_id", Integer.toString(mPostId));
                params.put("name", mNameEditText.getText().toString().trim());
                params.put("category",Integer.toString(mCategorySpinner.getSelectedItemPosition()));
                params.put("location",Integer.toString(mLocSpinner.getSelectedItemPosition()));
                params.put("description", mDescriptionEditText.getText().toString().trim());
                params.put("user_id", AccessToken.getCurrentAccessToken().getUserId());
                if(mPostImageBitmap != null) {
                    params.put("image", imageToString(mPostImageBitmap));
                    Log.d("MPOSTIMAGEBITMAPPPP", mPostImageBitmap + "HERE");
                }
                params.put("image_url", mPostImageUrl);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQue(stringRequest);
    }


    private void sendPostToServer() {
        if(mPostImageBitmap == null) {
            Toast.makeText(getActivity(), "이미지를 업로드 하세요", Toast.LENGTH_SHORT).show();
        } else if(mNameEditText.getText().toString().length() <= 0) {
            Toast.makeText(getActivity(), "이름을 입력하세요", Toast.LENGTH_SHORT).show();
        } else if(mCategorySpinner.getSelectedItem() == null  || mCategorySpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(getActivity(), "카테고리를 선택하세요", Toast.LENGTH_SHORT).show();
        } else if(mLocSpinner.getSelectedItem() == null || mLocSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(getActivity(), "지역을 선택하세요", Toast.LENGTH_SHORT).show();
        } else if(mDescriptionEditText.getText().toString().length() <= 0) {
            Toast.makeText(getActivity(), "상세설명을 입력하세요", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("포스트 쓰기");
            alert.setMessage("정말로 포스트를 쓰시겠습니까?");
            alert.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sendPostDataToServer();
                }
            });
            alert.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }
    }

    private void sendPostDataToServer() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerInfo.UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            Log.d("RESPONNNNNSE", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String Response = jsonObject.getString("response");
                            getActivity().finish();
                        } catch(JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", mNameEditText.getText().toString().trim());
                params.put("category",Integer.toString(mCategorySpinner.getSelectedItemPosition()));
                params.put("location",Integer.toString(mLocSpinner.getSelectedItemPosition()));
                params.put("description", mDescriptionEditText.getText().toString().trim());
                params.put("user_id", AccessToken.getCurrentAccessToken().getUserId());
                params.put("image", imageToString(mPostImageBitmap));
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQue(stringRequest);
    }
    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }


    private void showImgUploadDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction tr = fm.beginTransaction();
        mImgUploadDialog = new ImgUploadDialogFragment();
        if(!mImgUploadDialog.isAdded()) {
            mImgUploadDialog.show(tr, "ImgUploadDialog");
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("ActivityResult", "UploadPostFragment");

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);
                    mPostImage.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 500, 500, false));
                    mPostImageBitmap=Bitmap.createScaledBitmap(bitmap, 500, 500, false);
                    // 다이어로그 종료
                    // getActivity().getSupportFragmentManager().beginTransaction().remove(mImgUploadDialog).commit();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }
}
