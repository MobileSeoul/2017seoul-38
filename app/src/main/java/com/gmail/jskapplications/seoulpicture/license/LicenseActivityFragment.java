package com.gmail.jskapplications.seoulpicture.license;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.jskapplications.seoulpicture.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class LicenseActivityFragment extends Fragment {

    public LicenseActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_license, container, false);
    }
}
