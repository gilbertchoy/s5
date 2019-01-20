package com.me.gc.scratcher1;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TopFragment extends Fragment {
    private MainViewModel viewModel;
    private TextView pointsTextView;
    private Context context;
    private Integer points;
    private View v;
    private Button menuButton;
    private Button playButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_top, container, false);

        viewModel = ViewModelProviders.of(this.getActivity()).get(MainViewModel.class);
        points = viewModel.getPoints().getValue();

        menuButton = v.findViewById(R.id.menuButton);
        playButton = v.findViewById(R.id.playButton);
        pointsTextView = v.findViewById(R.id.points);

        pointsTextView.setText(points.toString());

        viewModel.getPoints().observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object curPoints){
                Log.d("berttest", "osberved: " + curPoints.toString());
                Integer currentPoints = (Integer) curPoints;
                pointsTextView.setText(currentPoints.toString());
            }

        });

        //open Drawer
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                viewModel.setOpenDrawer(1);
            }
        });

        //play ad
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                viewModel.setAdOfDayPressed(1);
            }
        });

        return v;
    }
}
