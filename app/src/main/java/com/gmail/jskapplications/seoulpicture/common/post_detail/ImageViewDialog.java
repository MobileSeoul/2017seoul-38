package com.gmail.jskapplications.seoulpicture.common.post_detail;

/**
 * Created by for on 2017-10-22.
 */

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.github.chrisbanes.photoview.PhotoView;
import com.gmail.jskapplications.seoulpicture.R;
import com.squareup.picasso.Picasso;


/**
 * Created by admin05 on 2016-11-10.
 *
 * 이미지 항목을 클릭했을때 이미지를 크게해서
 * 보여주는 다이얼로그
 */

public class ImageViewDialog extends DialogFragment {
    private static final String ARG_IMAGE_URL = "ARG_IMAGE_URL";



    private String mPhotoUrl;
    private PhotoView mPhotoView;

    public static ImageViewDialog newInstance(String imageUrl) {

        ImageViewDialog fragment = new ImageViewDialog();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE_URL, imageUrl);

        fragment.setArguments(args);

        return fragment;
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (getArguments() != null) {

            mPhotoUrl = getArguments().getString(ARG_IMAGE_URL);
        }


        return dialog;
    }

    //* 레이아웃 전개해서 리턴
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.image_view_dialog, container, false);

        mPhotoView = (PhotoView) v.findViewById(R.id.photo_view);
        Picasso.with(getActivity()).load(mPhotoUrl).into(mPhotoView);


        return v;
    }
}
