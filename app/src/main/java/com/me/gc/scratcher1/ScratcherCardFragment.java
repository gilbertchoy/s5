package com.me.gc.scratcher1;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ScratcherCardFragment extends Fragment {
    private ImageView backgroundImage;
    private ImageView scorecardBackgroundImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.scratcher_card, container, false);
        backgroundImage = v.findViewById(R.id.backgroundImage);
        backgroundImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //backgroundImage.setBackground(getActivity().getDrawable(R.drawable.test));
        backgroundImage.setImageResource(R.drawable.scratcher1);
        //scorecardBackgroundImage = v.findViewById(R.id.scorecardBackgroundImage);
        //scorecardBackgroundImage.setImageResource(R.drawable.greypattern);

        return v;
    }
}
