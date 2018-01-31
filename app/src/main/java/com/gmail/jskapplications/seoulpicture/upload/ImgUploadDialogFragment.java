package com.gmail.jskapplications.seoulpicture.upload;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.gmail.jskapplications.seoulpicture.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.Console;

import static android.app.Activity.RESULT_OK;

/**
 * Created by for on 2017-10-15.
 */

public class ImgUploadDialogFragment extends DialogFragment implements View.OnClickListener {

    private ImageView mGalleryImage;
    private ImageView mCameraImage;

    private final int MY_REQUEST_CODE = 0;

    private final int GALLERY_IMG_REQUEST = 1;
    private final int CAMERA_IMG_REQUEST = 2;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_img_upload, container, false);

        mGalleryImage = (ImageView) v.findViewById(R.id.gallery_imageview);
        mCameraImage = (ImageView) v.findViewById(R.id.camera_imageview);
        mGalleryImage.setOnClickListener(this);
        mCameraImage.setOnClickListener(this);



        return v;
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.home_card_click));
        switch (v.getId()) {
            case R.id.gallery_imageview:
                selectGalleryImage();
                break;

            case R.id.camera_imageview:
                CropImage.activity()
                        .setAspectRatio(1,1)
                        .start(getActivity());
                // selectCameraImage();
                break;
        }
    }

    private void selectGalleryImage() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, GALLERY_IMG_REQUEST);
    }

    private void selectCameraImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_REQUEST_CODE);
            } else {
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, CAMERA_IMG_REQUEST);
                }
            }
        } else {
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, CAMERA_IMG_REQUEST);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, CAMERA_IMG_REQUEST);
                }
            }
            else {
                // Your app will not have this permission. Turn off all functions
                // that require this permission or it will force close like your
                // original question
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("ActivityResult", "ImgUploadDialogFragment");
        // Gallery에서 사진 가져오기
        if(requestCode == GALLERY_IMG_REQUEST && resultCode == RESULT_OK && data!=null) {
            Uri path = data.getData();
            CropImage.activity(path)
                    .setAspectRatio(1,1)
                    .start(getActivity());
        } else if(requestCode == CAMERA_IMG_REQUEST && resultCode == RESULT_OK && data!=null) {
            Uri path = data.getData();
            CropImage.activity(path)
                    .setAspectRatio(1,1)
                    .start(getActivity());
        }
    }
}
