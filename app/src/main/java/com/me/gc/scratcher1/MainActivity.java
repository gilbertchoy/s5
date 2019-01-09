package com.me.gc.scratcher1;



import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.consent.DebugGeography;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
    private FusedLocationProviderClient mFusedLocationClient;
    private String deviceuid;

    //device info
    String model;
    String brand;
    String device;
    String buildId;
    String manufacturer;
    String user;
    String product;
    String version;
    int sdkVersion;

    //ads
    private InterstitialAd interstitialAd;
    private ConsentForm form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        scratcherCount = 0;
        flagRewardUserAfterAdOfDay = false;
        this.fragmentManager = getSupportFragmentManager();
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;

        //1st time init - if points value is null then add points
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        points = sharedPref.getInt("points",1000); //returns -1 if points value is 0

        //test code for setting points
        editor = sharedPref.edit();
        editor.putInt("points", 10000);
        editor.commit();
        viewModel.setPoints(10000);

        /*  works this is deployment code
        if(points == -1) { //check if 1st time init, check if points value exists if not then input starting points value
            Log.d("berttest", "input points");
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("points", 1000);
            editor.commit();
            viewModel.setPoints(1000);
        }else{
            viewModel.setPoints(points);
        }

*/


        /////////////////////
        //Get device info start
        /////////////////////
        String model = android.os.Build.MODEL;
        String brand = Build.BRAND;
        String device = Build.DEVICE;
        String buildId = Build.ID;
        String manufacturer = Build.MANUFACTURER;
        String user = Build.USER;
        String product = Build.PRODUCT;
        String version = Build.VERSION.RELEASE;
        int sdkVersion = android.os.Build.VERSION.SDK_INT;

        Log.d ("berttest", "berttest model:" + model + " brand:"+brand+" device:"+device+" id:"
            +buildId + " manufacturer:" + manufacturer + " user:" + user + " product:" + product + " version:" + version + " sdkVersion:" + sdkVersion);

        /////////////////////
        //Get device info end
        /////////////////////


        /////////////////////
        //Create new deviceuid start
        /////////////////////
        File file = new File("startdust");
        if(file.exists())
            Log.d("berttest", "file exists");
        else{
            Log.d("berttest", "file DNE");
        }

        /* works except for the check
        try {
            Log.d("berttest", "create start");
            FileInputStream userInputStream = context.openFileInput("startdust");
            Log.d("berttest", "userInputStream");
            InputStreamReader inputStreamReader = new InputStreamReader(userInputStream);
            if(userInputStream.available() == 1){
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String lineData = bufferedReader.readLine();
                Log.d("berttest","lineData length is:" + lineData.length());
                int fileLength = lineData.length();
                if(fileLength>29 && fileLength<58){
                    //deviceuid exists do nothing
                }else{
                    createNewDeviceUID();
                }
            }
            else{
                createNewDeviceUID();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        /////////////////////
        //Create new deviceuid end
        /////////////////////

        /////////////////////
        //Ads start
        /////////////////////
        MobileAds.initialize(this, "ca-app-pub-6760835969070814~3267571022");
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        //get GDPR consent value
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        int consentStored = sharedPref.getInt("targeted",0);
        //concent form
        final ConsentInformation consentInformation = ConsentInformation.getInstance(context);
        //for testing only
        /*
        ConsentInformation.getInstance(context).addTestDevice("935FAE0E91CBAAC1C5FA5E91E419651A");
        ConsentInformation.getInstance(context).
                setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_EEA);
                */
        Log.d("berttest","consentStored:" +consentStored);
        String[] publisherIds = {"pub-6760835969070814"};
        consentInformation.requestConsentInfoUpdate(publisherIds, new ConsentInfoUpdateListener() {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                // User's consent status successfully updated.
                Log.d("berttest", "berttest onConsentInfoUpdated: " + consentStatus.toString());
            }

            @Override
            public void onFailedToUpdateConsentInfo(String errorDescription) {
                Log.d("berttest", "berttest onFailedToUpdateConsentInfo:" + errorDescription.toString());
            }
        });
        if (consentStored == 0) {
            if(consentInformation.isRequestLocationInEeaOrUnknown() == true){
                URL privacyUrl = null;
                try {
                    privacyUrl = new URL("https://policies.google.com/technologies/partner-sites");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.d("berttest","error loading privacyUrl");
                }

                form = new ConsentForm.Builder(context, privacyUrl)
                        .withListener(new ConsentFormListener() {
                            @Override
                            public void onConsentFormLoaded() {
                                Log.d("berttest","onConsentFormLoaded");
                                form.show();
                            }

                            @Override
                            public void onConsentFormOpened() {
                                Log.d("berttest","onConsentFormOpened");
                            }

                            @Override
                            public void onConsentFormClosed(
                                    ConsentStatus consentStatus, Boolean userPrefersAdFree) {
                                Log.d("berttest","onConsentFormClosed consentStatus: " + consentStatus.toString() +
                                        " userPrefersAdFree: " + userPrefersAdFree.toString());

                                if(consentStatus.toString() == "PERSONALIZED"){
                                    //set Google to personalized
                                    ConsentInformation.getInstance(context)
                                            .setConsentStatus(ConsentStatus.PERSONALIZED);
                                    //set shared pref variable to personalized
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putInt("targeted", 1); //1 personalized, 2 non-personalized, 0 no value
                                    editor.commit();
                                    adRequest = new AdRequest.Builder().build();
                                }
                                else{
                                    //set Google to nonpersonalized
                                    ConsentInformation.getInstance(context)
                                            .setConsentStatus(ConsentStatus.NON_PERSONALIZED);
                                    //set shared pref variable to non-personalized
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putInt("targeted", 2); //1 personalized, 2 non-personalized, 0 no value
                                    editor.commit();
                                    //set admob ad request to non-personalized
                                    extras.putString("npa", "1");
                                    adRequest = new AdRequest.Builder()
                                            .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                                            .build();
                                }
                            }

                            @Override
                            public void onConsentFormError(String errorDescription) {
                                Log.d("berttest","onConsentFormError: " + errorDescription.toString());
                            }
                        })
                        .withPersonalizedAdsOption()
                        .withNonPersonalizedAdsOption()
                        .build();
                form.load();
            }
            else{
                //set Google to personalized
                ConsentInformation.getInstance(context)
                        .setConsentStatus(ConsentStatus.PERSONALIZED);
                //set shared pref variable to personalized
                editor = sharedPref.edit();
                editor.putInt("targeted", 1); //1 personalized, 2 non-personalized, 0 no value
                editor.commit();
                adRequest = new AdRequest.Builder().build();
            }
        }
        if(consentStored == 1){
            //do nothing, by default admob ads are personalized
            adRequest = new AdRequest.Builder().build();
        }
        if(consentStored == 2){
            extras.putString("npa", "1");
            adRequest = new AdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                    .build();
        }
        interstitialAd.loadAd(adRequest);

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                interstitialAd.loadAd(adRequest);
                if(flagRewardUserAfterAdOfDay==true){
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
        });
        ////////////////
        //Ads end
        ///////////////

        //Play Ad when play ad button pressed
        viewModel.getAdOfDayPressed().observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object position){
                flagRewardUserAfterAdOfDay = true;
                interstitialAd.show();
            }
        });

        viewModel.getSelected().observe(this, new Observer() {
            @Override
            public void onChanged(@Nullable Object position){
                Integer selected = (Integer) position;
                Log.d("berttest", "MainActivity selected osberved: " + selected.toString());

                if(scratcherCount%3 == 0) {
                    interstitialAd.show();
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

    private void createNewDeviceUID(){
        MediaType MEDIA_TYPE =
                MediaType.parse("application/json");

        //RequestBody body = RequestBody.create(json, jsonstring);
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("model", "booyah");
            postdata.put("Email", "anand.abhay1910@gmail.com");
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE,
                postdata.toString());
        Request request = new Request.Builder()
                .addHeader("Content-Type", "application/json")
                .url("https://scratcherserver.herokuapp.com/api/create")
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.d("berttest", "berttest onFailure http not sent");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Log.d("berttest", "berttest onResponse http sent");
                //Log.d("berttest", "response string is:" + response.body().string());
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                String jsonData = response.body().string();
                Log.d("berttest","jsonData string is:" + jsonData);
                try {
                    JSONObject jsonObject = new JSONObject(jsonData);
                    String deviceuid = jsonObject.getString("deviceuid");
                    Log.d("berttest", "deviceuid:"+deviceuid);

                    //create new file start
                    String filename = "startdust";
                    String fileContents = deviceuid;
                    FileOutputStream outputStream;
                    try {
                        outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                        outputStream.write(fileContents.getBytes());
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //create new file end
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
