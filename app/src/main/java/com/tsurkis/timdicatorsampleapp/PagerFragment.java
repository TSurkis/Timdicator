package com.tsurkis.timdicatorsampleapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by T.Surkis on 12/24/17.
 */
public class PagerFragment extends Fragment {
    public static final String KEY_COLOR = "com.tsurkis.indicatororiginal.PagerFragment.color_bundle_key";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(KEY_COLOR)) {
            String color = getArguments().getString(KEY_COLOR);

            TextView colorTextView = view.findViewById(R.id.color_text_view);
            if (colorTextView != null) {
                colorTextView.setText(color);
            }

            ViewGroup container = view.findViewById(R.id.container);
            if (container != null) {
                container.setBackgroundColor(Color.parseColor(color));
            }
        }
    }
}
