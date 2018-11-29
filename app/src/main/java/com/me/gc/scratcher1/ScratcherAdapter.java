package com.me.gc.scratcher1;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
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
    private ViewTreeObserver vto;
    private ViewTreeObserver vto2;

    public ScratcherAdapter(Context c, BottomFragment f){
        fragment = f;
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


        viewModel = ViewModelProviders.of(fragment.getActivity()).get(MainViewModel.class);

        /*
        viewModel = ViewModelProviders.of()
        ((MainActivity)context).get()


        model = ViewModelProviders.of(context.getActivity()).get(SharedViewModel.class);
        itemSelector.setOnClickListener(item -> {
            model.select(item);
        });
        */

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scratcher_cell, parent, false);
/*
        RelativeLayout cellBackground = v.findViewById(R.id.backgroundImage);
        cellBackground.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {

                cellBackground.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = cellBackground.getWidth();
                int height = cellBackground.getHeight();

                Log.d("berttest", "cell3 width:" + width + " height:" + height);

            }
        });
        */

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ScratcherAdapter.ViewHolder holder, int position) {



        RelativeLayout cellBackground = (RelativeLayout) holder.view.findViewById(R.id.backgroundImage);

        cellBackground.setBackgroundResource(R.drawable.sm_guns);
        int cellWidth = cellBackground.getBackground().getIntrinsicWidth();
        int cellHeight = cellBackground.getBackground().getIntrinsicHeight();

        Log.d("bertest", "berttest cell width:"+ cellWidth + " cell height:" + cellHeight);

        //cellBackground.setBackgroundResource(R.drawable.thumbnail1);


        cellBackground.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        public void onGlobalLayout() {

                            cellBackground.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            int width = cellBackground.getWidth();
                            int height = cellBackground.getHeight();

                            Log.d("berttest", "cell2 width:" + width + " height:" + height);

                        }
                    });




/*
        vto2 = holder.view.getViewTreeObserver();

        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                holder.view.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                Log.d("berttest", "keeps on running");
                }


        });
    */


        /*
        final LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
        final ViewTreeObserver observer= layout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Log.d("Log", "Height: " + layout.getHeight());
                    }
                });
        */




        if(position==0 || position==1 || position == 2 || position == 3 || position == 4 || position == 5) {
            cellBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Log.d("berttest", "ScratcherAdaper cell onclick width:" + v.getWidth() + " height:" + v.getHeight());

                    Log.d("berttest", "pos clicked: " + position);
                    viewModel.setSelected(0);
                    //minus points shared pref points
                    sharedPref = fragment.getActivity().getPreferences(Context.MODE_PRIVATE);
                    int totalPoints = viewModel.getPoints().getValue() - 200;
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("points", totalPoints);
                    editor.commit();
                    //minus viewModel points
                    viewModel.setPoints(viewModel.getPoints().getValue() - 200);
                }
            });
        }


        /*
        ImageView imageView = holder.view.findViewById(R.id.imageView);

        if(position==0) {
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.thumbnail1, context.getApplicationContext().getTheme()));
            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //imageView.setMaxWidth();
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Log.d("berttest","pos 0 clicked");
                    viewModel.setSelected(0);
                }
            });

        }
        if(position==1) {
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.thumbnail1, context.getApplicationContext().getTheme()));
            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            //q1Image.setScaleType(ScaleType.FIT_CENTER);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Log.d("berttest","pos 1 clicked");
                    viewModel.setSelected(1);
                }
            });
        }
        if(position==2) {
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.thumbnail1, context.getApplicationContext().getTheme()));
            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Log.d("berttest","pos 2 clicked");
                    viewModel.setSelected(2);
                }
            });
        }
        if(position==3) {
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.test1, context.getApplicationContext().getTheme()));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Log.d("berttest","pos 0 clicked");
                    viewModel.setSelected(3);
                }
            });
        }
        if(position==4) {
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.test1, context.getApplicationContext().getTheme()));
        }
        if(position==5) {
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.test2, context.getApplicationContext().getTheme()));
        }
*/

    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
