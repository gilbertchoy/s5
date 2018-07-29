package com.me.gc.scratcher1;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TopFragment extends Fragment {
    private MainViewModel viewModel;
    private TextView pointsTextView;
    private Context context;
    private Integer points;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_top, container, false);

        viewModel = ViewModelProviders.of(this.getActivity()).get(MainViewModel.class);
        points = viewModel.getPoints().getValue();

        pointsTextView = v.findViewById(R.id.points);
        pointsTextView.setText(points.toString());


        viewModel.getPoints().observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object o){
                Log.d("berttest", "osberved: " + o.toString());
            }

        });


        return v;
    }
}
