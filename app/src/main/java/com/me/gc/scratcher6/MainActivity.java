package com.me.gc.scratcher6;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

//import com.applovin.adview.AppLovinInterstitialAd;
//import com.applovin.adview.AppLovinInterstitialAdDialog;
//import com.applovin.sdk.AppLovinAd;
//import com.applovin.sdk.AppLovinAdDisplayListener;
//import com.applovin.sdk.AppLovinAdLoadListener;
//import com.applovin.sdk.AppLovinAdSize;
//import com.applovin.sdk.AppLovinPrivacySettings;
//import com.applovin.sdk.AppLovinSdk;
import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import java.net.MalformedURLException;
import java.net.URL;

//Vungle
import com.vungle.publisher.AdConfig;
import com.vungle.publisher.VungleAdEventListener;
import com.vungle.publisher.VungleInitListener;
import com.vungle.publisher.VunglePub;


public class MainActivity extends FragmentActivity {
    private MainViewModel viewModel;
    private Context context;
    private FragmentManager fragmentManager;
    private BottomFragment bottomFragment;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private int points;
    private AdRequest adRequest;
    private Bundle extras;
    private int scratcherCount;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private View headerLayoutDrawer;
    private TextView pointsDrawerTextView;
    private int screenHeight;
    private int snackbarHeight;
    private Snackbar snackbar;
    private boolean flagRewardUserAfterAdOfDay;
    private Server server;

    //admob
    private InterstitialAd interstitialAd;
    private ConsentForm form;
    //applovin
//    private AppLovinInterstitialAdDialog applovinInterstitialAd;
//    private AppLovinAd applovinLoadedAd;

    //Vungle
    private final VunglePub vunglePub = VunglePub.getInstance();
    private AdConfig vungleGlobalAdConfig;
    private String vungleAppId;
    private String vunglePlacementId;

    private String LOG_TAG = "berttest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        scratcherCount = 1; //start at 1 so ad isn't shown for 1st scratcher
        flagRewardUserAfterAdOfDay = false;
        this.fragmentManager = getSupportFragmentManager();
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        sharedPref = context.getSharedPreferences("scratcher",Context.MODE_PRIVATE);
        vungleAppId = context.getResources().getString(R.string.vungle_appid);
        vunglePlacementId = context.getResources().getString(R.string.vungle_placementid);

        /*
        //Vungle
        vunglePub.init(this, vungleAppId, new String[] { vungleAppId }, new VungleInitListener() {

            @Override
            public void onSuccess() {
                vungleGlobalAdConfig = vunglePub.getGlobalAdConfig();
                vungleGlobalAdConfig.setSoundEnabled(true);
                Log.d(LOG_TAG,"Init onSuccess");
                Log.d(LOG_TAG,"berttest onSuccess");
            }
            @Override
            public void onFailure(Throwable e){
                Log.d(LOG_TAG,"Init onFailure:" + e.toString());
            }
        });
        */







        //TEST CODE 1st time init - if points value is null then add points
        //works: test code for setting points
        /*
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("points", 100000);
        editor.commit();
        viewModel.setPoints(100000);
        points = 100000;
        */

        //  works this is deployment code
        if(sharedPref.contains("points") == false) { //check if 1st time init, check if points value exists if not then input starting points value
            Log.d("berttest", "input points");
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("points", 2000);
            editor.commit();
            viewModel.setPoints(2000);
            points = 2000;
        }else{
            points = sharedPref.getInt("points", 0);
            viewModel.setPoints(points);
        }

        //check if deviceuid exists, create new deviceuid if DNE
        server = new Server(context);
        server.create();

        /////////////////////
        //Open privacy policy in browser
        /////////////////////
        viewModel.getOpenBrowserPrivacyPolicy().observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object position){
                Log.d("berttest", "privacy policy pressed 1");
                Uri uriUrl = Uri.parse("https://scratcherserver.herokuapp.com/privacypolicy/");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });

        /////////////////////
        //Ads start
        /////////////////////
        //Vungle

        ////////////////
        //Ads end
        ///////////////

        //Play Ad when play ad button pressed
        viewModel.getAdOfDayPressed().observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object position){
                flagRewardUserAfterAdOfDay = true;
                //applovinInterstitialAd.showAndRender( applovinLoadedAd );
                //interstitialAd.show(); admob
                //Vungle Start
                /*
                if (Vungle.canPlayAd("INT-0631576")) {
                    Vungle.playAd("INT-0631576", null, new PlayAdCallback() {
                        @Override
                        public void onAdStart(String placementReferenceId) {
                            // Placement reference ID for the placement to be played
                            Log.d("berttest", "onAdStart placement:" + placementReferenceId);
                        }
                        @Override
                        public void onAdEnd(String placementReferenceId, boolean completed, boolean isCTAClicked) {
                            // Placement reference ID for the placement that has completed ad experience
                            // completed has value of true or false to notify whether video was
                            // watched for 80% or more
                            // isCTAClkcked has value of true or false to indicate whether download button
                            // of an ad has been clicked by the user
                            Log.d("berttest", "onAdEnd placemnt:" + placementReferenceId + " completed:" + completed + " isCTAClicked:" + isCTAClicked);
                            if (Vungle.isInitialized()) {
                                Vungle.loadAd("INT-0631576", new LoadAdCallback() {
                                    @Override
                                    public void onAdLoad(String placementReferenceId) {
                                        // Placement reference ID for the placement to load ad assets
                                        Log.d("berttest", "onAdLoad");
                                    }
                                    @Override
                                    public void onError(String placementReferenceId, VungleException exception) {
                                        // Placement reference ID for the placement that failed to download ad assets
                                        // VungleException contains error code and message
                                        Log.d("berttest", "load ad placement:" + placementReferenceId + "onError:" + exception);
                                    }
                                });
                            }
                        }
                        @Override
                        public void onError(String placementReferenceId, VungleException exception) {
                            // Placement reference ID for the placement that failed to play an ad
                            // VungleException contains error code and message
                            Log.d("berttest", "play ad onError:" + exception);
                        }
                    });
                }
                else{
                    if (Vungle.isInitialized()) {
                        Vungle.loadAd("INT-0631576", new LoadAdCallback() {
                            @Override
                            public void onAdLoad(String placementReferenceId) {
                                // Placement reference ID for the placement to load ad assets
                                Log.d("berttest", "onAdLoad");
                            }
                            @Override
                            public void onError(String placementReferenceId, VungleException exception) {
                                // Placement reference ID for the placement that failed to download ad assets
                                // VungleException contains error code and message
                                Log.d("berttest", "load ad placement:" + placementReferenceId + "onError:" + exception);
                            }
                        });
                    }
                }

                //Vunble end
                 */
            }
        });

        //when user clicks a cell
        viewModel.getSelected().observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object position){
                Integer selected = (Integer) position;
                Log.d("berttest", "MainActivity selected osberved: " + selected.toString());

                if(scratcherCount%3 == 0) {
                    //interstitialAd.show(); admob
                    //applovinInterstitialAd.showAndRender( applovinLoadedAd );
                    /*
                    //Vungle Start
                    if (Vungle.canPlayAd("INT-0631576")) {
                        Vungle.playAd("INT-0631576", null, new PlayAdCallback() {
                            @Override
                            public void onAdStart(String placementReferenceId) {
                                // Placement reference ID for the placement to be played
                                Log.d("berttest", "onAdStart placement:" + placementReferenceId);
                            }
                            @Override
                            public void onAdEnd(String placementReferenceId, boolean completed, boolean isCTAClicked) {
                                // Placement reference ID for the placement that has completed ad experience
                                // completed has value of true or false to notify whether video was
                                // watched for 80% or more
                                // isCTAClkcked has value of true or false to indicate whether download button
                                // of an ad has been clicked by the user
                                Log.d("berttest", "onAdEnd placemnt:" + placementReferenceId + " completed:" + completed + " isCTAClicked:" + isCTAClicked);
                                if (Vungle.isInitialized()) {
                                    Vungle.loadAd("INT-0631576", new LoadAdCallback() {
                                        @Override
                                        public void onAdLoad(String placementReferenceId) {
                                            // Placement reference ID for the placement to load ad assets
                                            Log.d("berttest", "onAdLoad");
                                        }
                                        @Override
                                        public void onError(String placementReferenceId, VungleException exception) {
                                            // Placement reference ID for the placement that failed to download ad assets
                                            // VungleException contains error code and message
                                            Log.d("berttest", "load ad placement:" + placementReferenceId + "onError:" + exception);
                                        }
                                    });
                                }
                            }
                            @Override
                            public void onError(String placementReferenceId, VungleException exception) {
                                // Placement reference ID for the placement that failed to play an ad
                                // VungleException contains error code and message
                                Log.d("berttest", "play ad onError:" + exception);
                            }
                        });
                    }
                    else{
                        if (Vungle.isInitialized()) {
                            Vungle.loadAd("INT-0631576", new LoadAdCallback() {
                                @Override
                                public void onAdLoad(String placementReferenceId) {
                                    // Placement reference ID for the placement to load ad assets
                                    Log.d("berttest", "onAdLoad");
                                }
                                @Override
                                public void onError(String placementReferenceId, VungleException exception) {
                                    // Placement reference ID for the placement that failed to download ad assets
                                    // VungleException contains error code and message
                                    Log.d("berttest", "load ad placement:" + placementReferenceId + "onError:" + exception);
                                }
                            });
                        }
                    }
                    //Vunble end
                     */
                }
                // Create a new FragmentManager
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ScratcherCardFragment scratcherCardFragment = new ScratcherCardFragment();
                scratcherCardFragment.setArguments(getIntent().getExtras());
                fragmentTransaction.replace(R.id.fragment_bottom, scratcherCardFragment)
                        .addToBackStack(null).commit();
                scratcherCount++;
            }
        });

        viewModel.getBackToHome().observe(this, Integer -> {
            Log.d("berttest","bertest backtohome received in mainActivity");
            this.fragmentManager.popBackStack();
        });

        //not enough coins to purchase scratcher
        viewModel.getNotEnoughPointsPurchaseScratcher().observe(this, Integer -> {
            Log.d("berttest","bertest notEnoughPoints received in mainActivity");
            snackbar = Snackbar.make(findViewById(R.id.snackbar_action),
                    R.string.snackbar_purchase_card_not_enough_coins,
                    Snackbar.LENGTH_SHORT);
            View sbView = snackbar.getView();
            TextView sbTextView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                sbTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            } else {
                sbTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            }
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)
                    snackbar.getView().getLayoutParams();
            params.setMargins(0, 0, 0, snackbarHeight);
            sbView.setLayoutParams(params);
            snackbar.show();
        });

        if (findViewById(R.id.fragment_top) != null) {
            if (savedInstanceState != null) {
                return;
            }
            // Create a new Fragment to be placed in the activity layout
            FragmentTransaction fragmentTransaction = this.fragmentManager.beginTransaction();
            TopFragment topFragment = new TopFragment();
            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            topFragment.setArguments(getIntent().getExtras());
            // Add the fragment to the 'fragment_container' FrameLayout
            fragmentTransaction.add(R.id.fragment_top, topFragment);
            fragmentTransaction.commit();
        }

        if (findViewById(R.id.fragment_bottom) != null) {
            if (savedInstanceState != null) {
                return;
            }
            FragmentTransaction fragmentTransaction = this.fragmentManager.beginTransaction();
            this.bottomFragment = new BottomFragment();
            this.bottomFragment.setArguments(getIntent().getExtras());
            fragmentTransaction.add(R.id.fragment_bottom, this.bottomFragment);
            fragmentTransaction.commit();
        }

        /////////////////////
        //Drawer start
        /////////////////////
        //update drawer points when points in top fragment updated
        navView = findViewById(R.id.nav_view);
        headerLayoutDrawer = navView.getHeaderView(0);
        pointsDrawerTextView = headerLayoutDrawer.findViewById(R.id.pointsDrawer);
        snackbarHeight = (int) screenHeight/2;

        viewModel.getPoints().observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object curPoints){
                Log.d("berttest", "drawer points osberved: " + curPoints.toString());
                Integer currentPoints = (Integer) curPoints;
                pointsDrawerTextView.setText(currentPoints.toString());
            }

        });

        //open drawer when drawer button pressed
        drawerLayout = findViewById(R.id.drawer_layout);
        viewModel.getOpenDrawer().observe(this, Integer -> {
            Log.d("berttest", "osberved openDrawer()");
            drawerLayout.openDrawer(GravityCompat.START);
        });

        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        String selected = menuItem.getTitle().toString();
                        switch(selected) {
                            case "Deposit":
                                Log.d("berttest","Deposit selected");
                                snackbar = Snackbar.make(findViewById(R.id.snackbar_action),
                                        R.string.coming_soon,
                                        Snackbar.LENGTH_SHORT);
                                break;
                            case "Withdraw":
                                Log.d("berttest","Withdraw selected");
                                int pointsTemp = viewModel.getPoints().getValue();

                                if(pointsTemp>1000000){
                                    snackbar = Snackbar.make(findViewById(R.id.snackbar_action),
                                            R.string.coming_soon,
                                            Snackbar.LENGTH_SHORT);
                                }else {
                                    snackbar = Snackbar.make(findViewById(R.id.snackbar_action),
                                            R.string.snackbar_withdraw_not_enough_coins,
                                            Snackbar.LENGTH_SHORT);
                                }
                                break;
                            case "Privacy Policy (Link)":
                                Log.d("besttest", "privacy policy selected");
                                viewModel.setOpenBrowserPrivacyPolicy(1);
                                break;
                            default:
                                Log.d("berttest","defualt drawer selection");
                        }

                        //snackbar - center text and move to middle of screen
                        View sbView = snackbar.getView();
                        TextView sbTextView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            sbTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        } else {
                            sbTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                        }
                        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)
                                snackbar.getView().getLayoutParams();
                        params.setMargins(0, 0, 0, snackbarHeight);
                        sbView.setLayoutParams(params);
                        snackbar.show();
                        return true;
                    }
                });
        /////////////////////
        //Drawer End
        /////////////////////
    }

    public void rewardUser200points(){
        flagRewardUserAfterAdOfDay = false;
        //add points
        int totalPoints = viewModel.getPoints().getValue() + 200;
        editor = sharedPref.edit();
        editor.putInt("points", totalPoints);
        editor.commit();
        viewModel.setPoints(totalPoints);
        //show snackbar
        snackbar = Snackbar.make(findViewById(R.id.snackbar_action),
                R.string.snackbar_200_coins_earned,
                Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        TextView sbTextView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            sbTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            sbTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        }
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)
                snackbar.getView().getLayoutParams();
        params.setMargins(0, 0, 0, snackbarHeight);
        sbView.setLayoutParams(params);
        snackbar.show();
    }
}
