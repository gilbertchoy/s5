package com.me.gc.scratcher1;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BottomFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context context;
    private MainViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*
        viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        viewModel.setSelected(2);
        */

        // Inflate the layout for this fragment
        View bottomFragmentView = inflater.inflate(R.layout.fragment_scratcher, container, false);

        context = getActivity();
        mRecyclerView = (RecyclerView) bottomFragmentView.findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(context,1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new ScratcherAdapter(context,BottomFragment.this);
        mRecyclerView.setAdapter(mAdapter);

        return bottomFragmentView;
    }
}
