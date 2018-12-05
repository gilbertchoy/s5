package com.me.gc.scratcher1;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ScratcherAdapter extends RecyclerView.Adapter<ScratcherAdapter.ViewHolder> {

    private Context context;
    private MainViewModel viewModel;
    private Fragment fragment;
    private SharedPreferences sharedPref;
    private boolean flagViewTreeLoaded;

    public ScratcherAdapter(Context c, BottomFragment f){
        fragment = f;
        context = c;
        flagViewTreeLoaded = false;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    @Override
    public ScratcherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        viewModel = ViewModelProviders.of(fragment.getActivity()).get(MainViewModel.class);
        sharedPref = fragment.getActivity().getPreferences(Context.MODE_PRIVATE);

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scratcher_cell, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ScratcherAdapter.ViewHolder holder, int position) {
        ImageView cellBackgroundImage = holder.view.findViewById(R.id.backgroundImage);
        RelativeLayout cell = holder.view.findViewById(R.id.cell);

        if(position==0){
            holder.view.post(new Runnable() {
                @Override
                public void run() {

                    int cellWidth = holder.view.getWidth();// this will give you cell width dynamically
                    int cellHeight = holder.view.getHeight();// this will give you cell height dynamically

                    Matrix m = cellBackgroundImage.getImageMatrix();
                    Drawable drawable = context.getResources().getDrawable(R.drawable.sm_guns);
                    int drawableWidth = drawable.getIntrinsicWidth();  //image width
                    float scaleWidth = (float) cellWidth / drawableWidth;
                    m.setScale(scaleWidth, scaleWidth, 0, 0);
                    cellBackgroundImage.setImageMatrix(m);
                    cellBackgroundImage.setImageResource(R.drawable.sm_guns);
                }
            });
        }
        if(position==1){
            holder.view.post(new Runnable() {
                @Override
                public void run() {
                    int cellWidth = holder.view.getWidth();// this will give you cell width dynamically
                    Matrix m = cellBackgroundImage.getImageMatrix();
                    Drawable drawable = context.getResources().getDrawable(R.drawable.sm_surfing);
                    int drawableWidth = drawable.getIntrinsicWidth();  //image width
                    float scaleWidth = (float) cellWidth / drawableWidth;
                    m.setScale(scaleWidth, scaleWidth, 0, 0);
                    cellBackgroundImage.setImageMatrix(m);
                    cellBackgroundImage.setImageResource(R.drawable.sm_surfing);
                }
            });
        }
        if(position==2){
            holder.view.post(new Runnable() {
                @Override
                public void run() {
                    int cellWidth = holder.view.getWidth();// this will give you cell width dynamically
                    Matrix m = cellBackgroundImage.getImageMatrix();
                    Drawable drawable = context.getResources().getDrawable(R.drawable.sm_dunkfest);
                    int drawableWidth = drawable.getIntrinsicWidth();  //image width
                    float scaleWidth = (float) cellWidth / drawableWidth;
                    m.setScale(scaleWidth, scaleWidth, 0, 0);
                    cellBackgroundImage.setImageMatrix(m);
                    cellBackgroundImage.setImageResource(R.drawable.sm_dunkfest);
                }
            });
        }
        if(position==3){
            holder.view.post(new Runnable() {
                @Override
                public void run() {
                    int cellWidth = holder.view.getWidth();// this will give you cell width dynamically
                    Matrix m = cellBackgroundImage.getImageMatrix();
                    Drawable drawable = context.getResources().getDrawable(R.drawable.sm_drone);
                    int drawableWidth = drawable.getIntrinsicWidth();  //image width
                    float scaleWidth = (float) cellWidth / drawableWidth;
                    m.setScale(scaleWidth, scaleWidth, 0, 0);
                    cellBackgroundImage.setImageMatrix(m);
                    cellBackgroundImage.setImageResource(R.drawable.sm_drone);
                }
            });
        }


        //setting onclick for each cell
        switch (position){
            case 0:
                cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        setSharedPrefViewModel(0, 200);
                    }
                });
                break;
            case 1:
                cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        setSharedPrefViewModel(1, 200);
                    }
                });
                break;
            case 2:
                cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        setSharedPrefViewModel(2, 200);
                    }
                });
                break;
            case 3:
                cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        setSharedPrefViewModel(3, 200);
                    }
                });
                break;
            default:
                cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        setSharedPrefViewModel(0, 200);
                    }
                });
                break;
        }
    }

    public void setSharedPrefViewModel(int position, int points){
        Log.d("berttest", "changePoints: " + position);
        viewModel.setSelected(position);
        //minus points shared pref points
        int totalPoints = viewModel.getPoints().getValue() - points;
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("points", totalPoints);
        editor.commit();
        //minus viewModel points
        viewModel.setPoints(viewModel.getPoints().getValue() - points);
    }



    @Override
    public int getItemCount() {
        return 10;
    }
}
