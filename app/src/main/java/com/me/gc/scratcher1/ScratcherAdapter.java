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

import org.w3c.dom.Text;


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
        TextView tvScratcherCost = holder.view.findViewById(R.id.scratcherCost);

        if(position==0){
            holder.view.post(new Runnable() {
                @Override
                public void run() {
                    String imageName = context.getResources().getString(R.string.sm_name);
                    int resID = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
                    Integer costID = context.getResources().getInteger(R.integer.cost);
                    int cellWidth = holder.view.getWidth();// this will give you cell width dynamically
                    Matrix m = cellBackgroundImage.getImageMatrix();
                    Drawable drawable = context.getResources().getDrawable(resID);
                    int drawableWidth = drawable.getIntrinsicWidth();  //image width
                    float scaleWidth = (float) cellWidth / drawableWidth;
                    m.setScale(scaleWidth, scaleWidth, 0, 0);
                    cellBackgroundImage.setImageMatrix(m);
                    cellBackgroundImage.setImageResource(resID);
                    tvScratcherCost.setText(costID.toString());
                }
            });
        }
        if(position==1){
            holder.view.post(new Runnable() {
                @Override
                public void run() {
                    String imageName = context.getResources().getString(R.string.sm_name1);
                    int resID = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
                    Integer costID = context.getResources().getInteger(R.integer.cost);
                    int cellWidth = holder.view.getWidth();// this will give you cell width dynamically
                    Matrix m = cellBackgroundImage.getImageMatrix();
                    Drawable drawable = context.getResources().getDrawable(resID);
                    int drawableWidth = drawable.getIntrinsicWidth();  //image width
                    float scaleWidth = (float) cellWidth / drawableWidth;
                    m.setScale(scaleWidth, scaleWidth, 0, 0);
                    cellBackgroundImage.setImageMatrix(m);
                    cellBackgroundImage.setImageResource(resID);
                    tvScratcherCost.setText(costID.toString());
                }
            });
        }
        if(position==2){
            holder.view.post(new Runnable() {
                @Override
                public void run() {
                    String imageName = context.getResources().getString(R.string.sm_name2);
                    int resID = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
                    Integer costID = context.getResources().getInteger(R.integer.cost1);
                    int cellWidth = holder.view.getWidth();// this will give you cell width dynamically
                    Matrix m = cellBackgroundImage.getImageMatrix();
                    Drawable drawable = context.getResources().getDrawable(resID);
                    int drawableWidth = drawable.getIntrinsicWidth();  //image width
                    float scaleWidth = (float) cellWidth / drawableWidth;
                    m.setScale(scaleWidth, scaleWidth, 0, 0);
                    cellBackgroundImage.setImageMatrix(m);
                    cellBackgroundImage.setImageResource(resID);
                    tvScratcherCost.setText(costID.toString());
                }
            });
        }
        if(position==3){
            holder.view.post(new Runnable() {
                @Override
                public void run() {
                    String imageName = context.getResources().getString(R.string.sm_name3);
                    int resID = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
                    Integer costID = context.getResources().getInteger(R.integer.cost1);
                    int cellWidth = holder.view.getWidth();// this will give you cell width dynamically
                    Matrix m = cellBackgroundImage.getImageMatrix();
                    Drawable drawable = context.getResources().getDrawable(resID);
                    int drawableWidth = drawable.getIntrinsicWidth();  //image width
                    float scaleWidth = (float) cellWidth / drawableWidth;
                    m.setScale(scaleWidth, scaleWidth, 0, 0);
                    cellBackgroundImage.setImageMatrix(m);
                    cellBackgroundImage.setImageResource(resID);
                    tvScratcherCost.setText(costID.toString());
                }
            });
        }
        if(position==4){
            holder.view.post(new Runnable() {
                @Override
                public void run() {
                    String imageName = context.getResources().getString(R.string.sm_name4);
                    int resID = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
                    Integer costID = context.getResources().getInteger(R.integer.cost2);
                    int cellWidth = holder.view.getWidth();// this will give you cell width dynamically
                    Matrix m = cellBackgroundImage.getImageMatrix();
                    Drawable drawable = context.getResources().getDrawable(resID);
                    int drawableWidth = drawable.getIntrinsicWidth();  //image width
                    float scaleWidth = (float) cellWidth / drawableWidth;
                    m.setScale(scaleWidth, scaleWidth, 0, 0);
                    cellBackgroundImage.setImageMatrix(m);
                    cellBackgroundImage.setImageResource(resID);
                    tvScratcherCost.setText(costID.toString());
                }
            });
        }
        if(position==5){
            holder.view.post(new Runnable() {
                @Override
                public void run() {
                    String imageName = context.getResources().getString(R.string.sm_name5);
                    int resID = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
                    Integer costID = context.getResources().getInteger(R.integer.cost2);
                    int cellWidth = holder.view.getWidth();// this will give you cell width dynamically
                    Matrix m = cellBackgroundImage.getImageMatrix();
                    Drawable drawable = context.getResources().getDrawable(resID);
                    int drawableWidth = drawable.getIntrinsicWidth();  //image width
                    float scaleWidth = (float) cellWidth / drawableWidth;
                    m.setScale(scaleWidth, scaleWidth, 0, 0);
                    cellBackgroundImage.setImageMatrix(m);
                    cellBackgroundImage.setImageResource(resID);
                    tvScratcherCost.setText(costID.toString());
                }
            });
        }
        if(position==6){
            holder.view.post(new Runnable() {
                @Override
                public void run() {
                    String imageName = context.getResources().getString(R.string.sm_name6);
                    int resID = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
                    Integer costID = context.getResources().getInteger(R.integer.cost3);
                    int cellWidth = holder.view.getWidth();// this will give you cell width dynamically
                    Matrix m = cellBackgroundImage.getImageMatrix();
                    Drawable drawable = context.getResources().getDrawable(resID);
                    int drawableWidth = drawable.getIntrinsicWidth();  //image width
                    float scaleWidth = (float) cellWidth / drawableWidth;
                    m.setScale(scaleWidth, scaleWidth, 0, 0);
                    cellBackgroundImage.setImageMatrix(m);
                    cellBackgroundImage.setImageResource(resID);
                    tvScratcherCost.setText(costID.toString());
                }
            });
        }
        if(position==7){
            holder.view.post(new Runnable() {
                @Override
                public void run() {
                    String imageName = context.getResources().getString(R.string.sm_name7);
                    int resID = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
                    Integer costID = context.getResources().getInteger(R.integer.cost3);
                    int cellWidth = holder.view.getWidth();// this will give you cell width dynamically
                    Matrix m = cellBackgroundImage.getImageMatrix();
                    Drawable drawable = context.getResources().getDrawable(resID);
                    int drawableWidth = drawable.getIntrinsicWidth();  //image width
                    float scaleWidth = (float) cellWidth / drawableWidth;
                    m.setScale(scaleWidth, scaleWidth, 0, 0);
                    cellBackgroundImage.setImageMatrix(m);
                    cellBackgroundImage.setImageResource(resID);
                    tvScratcherCost.setText(costID.toString());
                }
            });
        }
        if(position==8){
            holder.view.post(new Runnable() {
                @Override
                public void run() {
                    String imageName = context.getResources().getString(R.string.sm_name8);
                    int resID = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
                    Integer costID = context.getResources().getInteger(R.integer.cost4);
                    int cellWidth = holder.view.getWidth();// this will give you cell width dynamically
                    Matrix m = cellBackgroundImage.getImageMatrix();
                    Drawable drawable = context.getResources().getDrawable(resID);
                    int drawableWidth = drawable.getIntrinsicWidth();  //image width
                    float scaleWidth = (float) cellWidth / drawableWidth;
                    m.setScale(scaleWidth, scaleWidth, 0, 0);
                    cellBackgroundImage.setImageMatrix(m);
                    cellBackgroundImage.setImageResource(resID);
                    tvScratcherCost.setText(costID.toString());
                }
            });
        }
        if(position==9){
            holder.view.post(new Runnable() {
                @Override
                public void run() {
                    String imageName = context.getResources().getString(R.string.sm_name9);
                    int resID = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
                    Integer costID = context.getResources().getInteger(R.integer.cost5);
                    int cellWidth = holder.view.getWidth();// this will give you cell width dynamically
                    Matrix m = cellBackgroundImage.getImageMatrix();
                    Drawable drawable = context.getResources().getDrawable(resID);
                    int drawableWidth = drawable.getIntrinsicWidth();  //image width
                    float scaleWidth = (float) cellWidth / drawableWidth;
                    m.setScale(scaleWidth, scaleWidth, 0, 0);
                    cellBackgroundImage.setImageMatrix(m);
                    cellBackgroundImage.setImageResource(resID);
                    tvScratcherCost.setText(costID.toString());
                }
            });
        }


        //setting onclick for each cell
        switch (position){
            case 0:
                cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        setSharedPrefViewModel(0, R.integer.cost);
                    }
                });
                break;
            case 1:
                cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        setSharedPrefViewModel(1, R.integer.cost);
                    }
                });
                break;
            case 2:
                cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        setSharedPrefViewModel(2, R.integer.cost1);
                    }
                });
                break;
            case 3:
                cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        setSharedPrefViewModel(3, R.integer.cost1);
                    }
                });
                break;
            case 4:
                cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        setSharedPrefViewModel(4, R.integer.cost2);
                    }
                });
                break;
            case 5:
                cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        setSharedPrefViewModel(5, R.integer.cost2);
                    }
                });
                break;
            case 6:
                cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        setSharedPrefViewModel(6, R.integer.cost3);
                    }
                });
                break;
            case 7:
                cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        setSharedPrefViewModel(7, R.integer.cost3);
                    }
                });
                break;
            case 8:
                cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        setSharedPrefViewModel(8, R.integer.cost4);
                    }
                });
                break;
            case 9:
                cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        setSharedPrefViewModel(9, R.integer.cost5);
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
        viewModel.setCost(points);
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
