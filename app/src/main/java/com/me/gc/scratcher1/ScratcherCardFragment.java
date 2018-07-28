package com.me.gc.scratcher1;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.me.gc.scratcher1.ScratchImageView;

public class ScratcherCardFragment extends Fragment {
    private ImageView backgroundImage;
    private ImageView scorecardBackgroundImage;
    private com.me.gc.scratcher1.ScratchImageView scratchImageView;
    private ScratchImageView extraScratchImageView;
    private MainViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.scratcher_card, container, false);
        backgroundImage = v.findViewById(R.id.backgroundImage);
        backgroundImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        backgroundImage.setImageResource(R.drawable.scratcher1);

        //selected value
        viewModel = ViewModelProviders.of(this.getActivity()).get(MainViewModel.class);
        int selected = (int) viewModel.getSelected().getValue();

        //set values for loot





        scratchImageView = (ScratchImageView) v.findViewById(R.id.scratchImage);

        scratchImageView.setRevealListener(new ScratchImageView.IRevealListener() {
            @Override
            public void onRevealed(ScratchImageView tv) {
                Log.d("berttest", "onReveal");
            }

            @Override
            public void onRevealPercentChangedListener(ScratchImageView siv, float percent) {
                Log.d("berttest", "on percent change: " + percent);
            }
        });

        extraScratchImageView = (ScratchImageView) v.findViewById(R.id.extraScratchImage);
        extraScratchImageView.setRevealListener(new ScratchImageView.IRevealListener() {
            @Override
            public void onRevealed(ScratchImageView tv) {
                // on reveal
            }

            @Override
            public void onRevealPercentChangedListener(ScratchImageView siv, float percent) {
                Log.d("berttest", "percent change1: " + percent);
            }
        });

        return v;
    }


}
