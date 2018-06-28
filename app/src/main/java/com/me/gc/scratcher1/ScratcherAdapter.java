package com.me.gc.scratcher1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ScratcherAdapter extends RecyclerView.Adapter<ScratcherAdapter.ViewHolder> {

    private Context context;

    public ScratcherAdapter(Context c){
        context = c;
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

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scratcher_cell, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ScratcherAdapter.ViewHolder holder, int position) {
        ImageView imageView = holder.view.findViewById(R.id.imageView);

        if(position==0) {
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.test, context.getApplicationContext().getTheme()));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                }
            });

        }
        if(position==1) {
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.test1, context.getApplicationContext().getTheme()));
        }
        if(position==2) {
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.test2, context.getApplicationContext().getTheme()));
        }
        if(position==3) {
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.test, context.getApplicationContext().getTheme()));
        }
        if(position==4) {
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.test1, context.getApplicationContext().getTheme()));
        }
        if(position==5) {
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.test2, context.getApplicationContext().getTheme()));
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
