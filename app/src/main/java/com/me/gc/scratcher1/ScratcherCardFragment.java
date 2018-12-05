package com.me.gc.scratcher1;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Random;

import static android.view.View.VISIBLE;
import static android.widget.ImageView.ScaleType.CENTER_CROP;
import static android.widget.ImageView.ScaleType.FIT_START;

public class ScratcherCardFragment extends Fragment {
    private Context context;
    private ImageView backgroundImage;
    private ImageView scorecardBackgroundImage;
    private com.me.gc.scratcher1.ScratchImageView scratchImageView;
    private ScratchImageView extraScratchImageView;
    private MainViewModel viewModel;
    private Drawable drawable;
    private float scratchRevealed;
    private float extraScratchRevealed;
    private SharedPreferences sharedPref;
    private TextView textView;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private TextView textView6;
    private TextView rewardTextView;
    private TextView rewardTitle;
    private Integer sum;
    private Boolean revealFlag;
    private ConstraintLayout onRewardTransparent;

    //animations
    private LottieAnimationView aMiniStar;
    private LottieAnimationView aFireworks;
    private LottieAnimationView aConfetti;
    private LottieAnimationView aSmallFireworks;
    private LottieAnimationView aStar;
    private LottieAnimationView aBackgroundStar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //selected value
        viewModel = ViewModelProviders.of(this.getActivity()).get(MainViewModel.class);
        int selected = (int) viewModel.getSelected().getValue();
        Log.d("berttest", "ScratcherCardFragment test selected value:" + selected);

        View v = inflater.inflate(R.layout.scratcher_card1, container, false);
        backgroundImage = v.findViewById(R.id.backgroundImage);

        //scale background image start - scale ratio using width and move image to top
        Matrix m = backgroundImage.getImageMatrix();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int windowWidth = displayMetrics.widthPixels;  //window width

        //set background image
        switch (selected){
            case 0:
                drawable = getResources().getDrawable(R.drawable.lg_guns);
                break;
            case 1:
                drawable = getResources().getDrawable(R.drawable.lg_surfing);
                break;
            case 2:
                drawable = getResources().getDrawable(R.drawable.lg_dunkfest);
                break;
            case 3:
                drawable = getResources().getDrawable(R.drawable.lg_drone);
                break;
            case 4:
                drawable = getResources().getDrawable(R.drawable.lg_fruit);
                break;
            case 5:
                drawable = getResources().getDrawable(R.drawable.lg_city);
                break;
            default:
                drawable = getResources().getDrawable(R.drawable.lg_guns);
                break;
        }

        //scale image
        int drawableWidth = drawable.getIntrinsicWidth();  //image width
        float scaleWidth = (float) windowWidth/drawableWidth;
        m.setScale(scaleWidth, scaleWidth ,0,0);
        backgroundImage.setImageMatrix(m);
        backgroundImage.setBackground(drawable);


        //revealFlag set to true in beginning and changes to false after both scratchers fields revealed
        revealFlag = true;



        //set values for loot
        Random random = new Random();
        Integer r = random.nextInt(11) * 10;
        Integer r1 = random.nextInt(11) * 10;
        Integer r2 = random.nextInt(11) * 10;
        Integer r3 = random.nextInt(11) * 10;
        Integer r4 = random.nextInt(11) * 10;
        Integer r5 = random.nextInt(11) * 10;
        Integer r6 = random.nextInt(11) * 10;
        sum = r + r1 + r2 + r3 + r4 + r5 +r6;

        textView = (TextView) v.findViewById(R.id.textView);
        textView1 = (TextView) v.findViewById(R.id.textView1);
        textView2 = (TextView) v.findViewById(R.id.textView2);
        textView3 = (TextView) v.findViewById(R.id.textView3);
        textView4 = (TextView) v.findViewById(R.id.textView4);
        textView5 = (TextView) v.findViewById(R.id.textView5);
        textView6 = (TextView) v.findViewById(R.id.textView6);
        rewardTextView = (TextView) v.findViewById(R.id.rewardAmount);
        rewardTitle = (TextView) v.findViewById(R.id.rewardTitle);
        onRewardTransparent = (ConstraintLayout) v.findViewById(R.id.onRewardTransparent);

        textView.setText(r.toString());
        textView1.setText(r1.toString());
        textView2.setText(r2.toString());
        textView3.setText(r3.toString());
        textView4.setText(r4.toString());
        textView5.setText(r5.toString());
        textView6.setText(r6.toString());
        rewardTextView.setText(sum.toString());

        aMiniStar = v.findViewById(R.id.aMiniStar);
        aFireworks = v.findViewById(R.id.aFireworks);
        aConfetti = v.findViewById(R.id.aConfetti);
        aSmallFireworks = v.findViewById(R.id.aSmallFireworks);
        aStar = v.findViewById(R.id.aStar);
        aBackgroundStar = v.findViewById(R.id.aBackgroundStar);

        scratchImageView = (ScratchImageView) v.findViewById(R.id.scratchImage);
        scratchImageView.setRevealListener(new ScratchImageView.IRevealListener() {
            @Override
            public void onRevealed(ScratchImageView tv) {
                // on reveal
            }

            @Override
            public void onRevealPercentChangedListener(ScratchImageView siv, float percent) {
                if(percent>0.1) {
                    scratchRevealed = percent;
                    if(revealFlag==true){
                        revealCheck();
                    }
                }
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
                if(percent>0.1) {
                    extraScratchRevealed = percent;
                    if(revealFlag==true){
                        revealCheck();
                    }
                }
            }
        });

        rewardTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d("berttest","pos 0 clicked");
                viewModel.setBackToHome(0);
            }
        });
        return v;
    }

    public void revealCheck(){
        if(extraScratchRevealed>0.1){
            if(scratchRevealed>0.1){
                sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                int totalPoints = viewModel.getPoints().getValue() + sum;
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("points", totalPoints);
                editor.commit();
                viewModel.setPoints(totalPoints);
                revealFlag = false;
                showEndAnimation(sum);
                Log.d("berttest","berttest sum is:" + sum);
            }
        }
    }

    public void showEndAnimation(int reward){
        onRewardTransparent.setVisibility(VISIBLE);
        if(reward < 200){
            Log.d("berttest","berttest reward amount 200");
            aBackgroundStar.setVisibility(VISIBLE);
            aSmallFireworks.setVisibility(VISIBLE);
            aBackgroundStar.setRepeatCount(0);
            aBackgroundStar.playAnimation();
            aSmallFireworks.playAnimation();
        }
        else if(reward < 300){
            Log.d("berttest","berttest reward amount 300");
            aBackgroundStar.setVisibility(VISIBLE);
            aConfetti.setVisibility(VISIBLE);
            aBackgroundStar.setRepeatCount(0);
            aBackgroundStar.playAnimation();
            aConfetti.playAnimation();
        }
        else if(reward <400){
            Log.d("berttest","berttest reward amount 400");
            aBackgroundStar.setVisibility(VISIBLE);
            aFireworks.setVisibility(VISIBLE);
            aBackgroundStar.setRepeatCount(0);
            aBackgroundStar.playAnimation();
            aFireworks.playAnimation();
        }
        else if(reward < 500){
            Log.d("berttest","berttest reward amount 500");
            aBackgroundStar.setVisibility(VISIBLE);
            aFireworks.setVisibility(VISIBLE);
            aStar.setVisibility(VISIBLE);
            aBackgroundStar.setRepeatCount(0);
            aBackgroundStar.playAnimation();
            aFireworks.playAnimation();
            aStar.playAnimation();
        }
        else{
            Log.d("berttest","berttest reward amount +");
            aBackgroundStar.setVisibility(VISIBLE);
            aFireworks.setVisibility(VISIBLE);
            aConfetti.setVisibility(VISIBLE);
            aStar.setVisibility(VISIBLE);
            aBackgroundStar.setRepeatCount(0);
            aBackgroundStar.playAnimation();
            aFireworks.playAnimation();
            aConfetti.playAnimation();
            aStar.playAnimation();
        }
        rewardTextView.setVisibility(VISIBLE);
        rewardTitle.setVisibility(VISIBLE);
    }

}





