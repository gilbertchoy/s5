package com.me.gc.scratcher6;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Random;

import static android.view.View.VISIBLE;

public class ScratcherCardFragment extends Fragment {
    private Context context;
    private RecyclerView mRecyclerView;
    private ImageView backgroundImage;
    private TextView scratcherCost;
    private ImageView scorecardBackgroundImage;
    private com.me.gc.scratcher6.ScratchImageView scratchImageView;
    private ScratchImageView extraScratchImageView;
    private MainViewModel viewModel;
    private Drawable drawable;
    private int resID;
    private int rewardAvgValue;
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
    private ImageView rewardImage;

    private String rewardImageName;


    private TextView rewardTextView;
    private TextView rewardTitle;
    private TextView clickHere;
    private ConstraintLayout clickArea;
    private Integer sum;
    private Integer cost;
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
        context = getActivity();
        //selected value
        viewModel = ViewModelProviders.of(this.getActivity()).get(MainViewModel.class);
        int selected = (int) viewModel.getSelected().getValue();
        cost = viewModel.getCost().getValue();
        Log.d("berttest", "ScratcherCardFragment test selected value:" + selected);

        View v = inflater.inflate(R.layout.scratcher_card, container, false);
        backgroundImage = v.findViewById(R.id.backgroundImage);
        scratcherCost = v.findViewById(R.id.scratcherCost);
        scratcherCost.setText(cost.toString());


        rewardImage = v.findViewById(R.id.rewardImage);

        String imageN = getString(R.string.lg_name);
        int i = getResources().getIdentifier(imageN, "drawable", getActivity().getPackageName());
        Drawable drawable1 = getResources().getDrawable(i);
        rewardImage.setImageDrawable(drawable1);



        //set background image
        switch (selected){
            case 0:
                String imageName = getString(R.string.lg_name);
                resID = getResources().getIdentifier(imageName, "drawable", getActivity().getPackageName());
                rewardAvgValue = getResources().getInteger(R.integer.reward);
                break;
            case 1:
                imageName = getString(R.string.lg_name1);
                resID = getResources().getIdentifier(imageName, "drawable", getActivity().getPackageName());
                rewardAvgValue = getResources().getInteger(R.integer.reward);
                break;
            case 2:
                imageName = getString(R.string.lg_name2);
                resID = getResources().getIdentifier(imageName, "drawable", getActivity().getPackageName());
                rewardAvgValue = getResources().getInteger(R.integer.reward1);
                break;
            case 3:
                imageName = getString(R.string.lg_name3);
                resID = getResources().getIdentifier(imageName, "drawable", getActivity().getPackageName());
                rewardAvgValue = getResources().getInteger(R.integer.reward1);
                break;
            case 4:
                imageName = getString(R.string.lg_name4);
                resID = getResources().getIdentifier(imageName, "drawable", getActivity().getPackageName());
                rewardAvgValue = getResources().getInteger(R.integer.reward2);
                break;
            case 5:
                imageName = getString(R.string.lg_name5);
                resID = getResources().getIdentifier(imageName, "drawable", getActivity().getPackageName());
                rewardAvgValue = getResources().getInteger(R.integer.reward2);
                break;
            case 6:
                imageName = getString(R.string.lg_name6);
                resID = getResources().getIdentifier(imageName, "drawable", getActivity().getPackageName());
                rewardAvgValue = getResources().getInteger(R.integer.reward3);
                break;
            case 7:
                imageName = getString(R.string.lg_name7);
                resID = getResources().getIdentifier(imageName, "drawable", getActivity().getPackageName());
                rewardAvgValue = getResources().getInteger(R.integer.reward3);
                break;
            case 8:
                imageName = getString(R.string.lg_name8);
                resID = getResources().getIdentifier(imageName, "drawable", getActivity().getPackageName());
                rewardAvgValue = getResources().getInteger(R.integer.reward4);
                break;
            default:
                imageName = getString(R.string.lg_name9);
                resID = getResources().getIdentifier(imageName, "drawable", getActivity().getPackageName());
                rewardAvgValue = getResources().getInteger(R.integer.reward5);
                break;
        }
        drawable = getResources().getDrawable(resID);

        //scale image
        Matrix m = backgroundImage.getImageMatrix();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int windowWidth = displayMetrics.widthPixels;  //window width
        int drawableWidth = drawable.getIntrinsicWidth();  //image width
        float scaleWidth = (float) windowWidth/drawableWidth;
        m.setScale(scaleWidth, scaleWidth ,0,0);
        backgroundImage.setImageMatrix(m);
        backgroundImage.setImageDrawable(drawable);

        //revealFlag set to true in beginning and changes to false after both scratchers fields revealed
        revealFlag = true;

        //set values for loot
        Random random = new Random();
        Integer r = random.nextInt(rewardAvgValue * 2);
        Integer r1 = random.nextInt(rewardAvgValue * 2);
        Integer r2 = random.nextInt(rewardAvgValue * 2);
        Integer r3 = random.nextInt(rewardAvgValue * 2);
        Integer r4 = random.nextInt(rewardAvgValue * 2);
        Integer r5 = random.nextInt(rewardAvgValue * 2);
        Integer r6 = random.nextInt(rewardAvgValue * 2);
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
        clickHere = (TextView) v.findViewById(R.id.clickHere);
        clickArea = (ConstraintLayout) v.findViewById(R.id.clickArea);

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
                if(percent>0.8) {
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
                if(percent>0.7) {
                    extraScratchRevealed = percent;
                    if(revealFlag==true){
                        revealCheck();
                    }
                }
            }
        });

        clickArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                viewModel.setBackToHome(0);
            }
        });
        return v;
    }

    public void revealCheck(){
        if(extraScratchRevealed>0.7){
            if(scratchRevealed>0.8){
                //sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                sharedPref = context.getSharedPreferences("scratcher",Context.MODE_PRIVATE);
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
        if(reward < cost){
            Log.d("berttest","berttest reward amount 200");
            aBackgroundStar.setVisibility(VISIBLE);
            aSmallFireworks.setVisibility(VISIBLE);
            aBackgroundStar.setRepeatCount(0);
            aBackgroundStar.playAnimation();
            aSmallFireworks.playAnimation();
        }
        else if(reward < (rewardAvgValue * 7)){
            Log.d("berttest","berttest reward amount 300");
            aBackgroundStar.setVisibility(VISIBLE);
            aConfetti.setVisibility(VISIBLE);
            aBackgroundStar.setRepeatCount(0);
            aBackgroundStar.playAnimation();
            aConfetti.playAnimation();
        }
        else if(reward < (rewardAvgValue * 7 * 1.2)){
            Log.d("berttest","berttest reward amount 400");
            aBackgroundStar.setVisibility(VISIBLE);
            aFireworks.setVisibility(VISIBLE);
            aBackgroundStar.setRepeatCount(0);
            aBackgroundStar.playAnimation();
            aFireworks.playAnimation();
        }
        else if(reward < (rewardAvgValue * 7 * 1.5)){
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
        clickHere.setVisibility(VISIBLE);
        clickArea.setVisibility(VISIBLE);
    }

}





