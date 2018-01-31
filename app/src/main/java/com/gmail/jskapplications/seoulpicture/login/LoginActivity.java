package com.gmail.jskapplications.seoulpicture.login;

import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.gmail.jskapplications.seoulpicture.R;
import com.gmail.jskapplications.seoulpicture.base.BaseActivity;
import com.gmail.jskapplications.seoulpicture.main.MainActivity;
import com.gmail.jskapplications.seoulpicture.model.PicturePost;
import com.gmail.jskapplications.seoulpicture.model.User;
import com.gmail.jskapplications.seoulpicture.network.VolleySingleton;
import com.gmail.jskapplications.seoulpicture.util.ServerInfo;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.READ_CONTACTS;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    LoginButton mLoginButton;
    CallbackManager callbackManager;
    private ProfileTracker mProfileTracker;
    private LoginResult mLoginResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(AccessToken.getCurrentAccessToken() != null) {
            changeToMainActivity();
            Toast.makeText(getApplicationContext(), "환영합니다! "+Profile.getCurrentProfile().getName()+ "님", Toast.LENGTH_LONG).show();
        }


        callbackManager = CallbackManager.Factory.create();
        mLoginButton = (LoginButton) findViewById(R.id.fb_login_btn);
        mLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mLoginResult = loginResult;
                if(Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                            Toast.makeText(getApplicationContext(), "환영합니다! "+ currentProfile.getName() + "님", Toast.LENGTH_LONG).show();
                            getOrPutUser();
                            mProfileTracker.stopTracking();

                            changeToMainActivity();
                        }
                    };
                    // no need to call startTracking() on mProfileTracker
                    // because it is called by its constructor, internally.
                }
                else {
                    getOrPutUser();
                    Profile profile = Profile.getCurrentProfile();
                    Toast.makeText(getApplicationContext(), "환영합니다! "+ profile.getName() + "님", Toast.LENGTH_LONG).show();
                    Log.v("facebook - profile", profile.getFirstName());
                    changeToMainActivity();
                }


                // changeToMainActivity();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    private void getOrPutUser() {
        Log.d("VolleyTest", "HERE!");
        StringRequest jsonStringRequest = new StringRequest(Request.Method.POST, ServerInfo.GET_OR_PUT_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("VolleyTest", response.toString());

                        try {
                            JSONArray j = new JSONArray(response);

                            for(int i=0; i<j.length(); i++) {
                                JSONObject jsonObject = j.getJSONObject(i);

                                User user = User.getInstance();
                                if(!jsonObject.isNull("id")) {
                                    user.setmId(jsonObject.getString("id"));
                                }
                                if(!jsonObject.isNull("id")) {
                                    user.setmName(jsonObject.getString("name"));
                                }
                                if(!jsonObject.isNull("total_likes")) {
                                    user.setmTotalLikes(jsonObject.getInt("total_likes"));
                                }
                            Log.d("ONRESPONSEE", jsonObject.getString("name"));
                            }
                            changeToMainActivity();
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
                params.put("id", AccessToken.getCurrentAccessToken().getUserId());
                params.put("name", Profile.getCurrentProfile().getName());
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQue(jsonStringRequest);
    }









    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void changeToMainActivity() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}