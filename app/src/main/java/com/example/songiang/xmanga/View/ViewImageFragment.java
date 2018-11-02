package com.example.songiang.xmanga.View;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.songiang.xmanga.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewImageFragment extends Fragment {

    private String url;
    private ImageView imgView;
    public ViewImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_view_image, container, false);
        url = getArguments().getString("URL");
        imgView = rootView.findViewById(R.id.image);
        Glide.with(getActivity())
                .load(url)
                .asBitmap()
                .atMost()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .animate(android.R.anim.fade_in)
                .approximate()
                .into(imgView);
        return rootView;
    }

}
