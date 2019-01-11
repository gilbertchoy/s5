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
import android.support.v4.util.ArrayMap;
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
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private String hashkey;
    private ArrayMap<String,String> deviceInfo = new ArrayMap<>();
    //for running thread synchronously
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    Server server;

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
        String hash = "";
        Hashing hashing = new Hashing();
        try {
            hash = hashing.sha1("1234");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        server = new Server(context);


        Log.d("berttest", "hash is:" + hash);

        executorService.submit(playad);
        executorService.submit(adclosed);

        /////////////////////
        //Get device info start
        /////////////////////
        deviceInfo.put("model", android.os.Build.MODEL);
        deviceInfo.put("brand", Build.BRAND);
        deviceInfo.put("device", Build.DEVICE);
        deviceInfo.put("buildid", Build.ID);
        deviceInfo.put("manufacturer", Build.MANUFACTURER);
        deviceInfo.put("user", Build.USER);
        deviceInfo.put("product", Build.PRODUCT);
        deviceInfo.put("releaseversion", Build.VERSION.RELEASE);
        Integer sdkInt = android.os.Build.VERSION.SDK_INT;
        deviceInfo.put("sdkversion", sdkInt.toString());

        Log.d("berttest", "array test model:" + deviceInfo.get("model") + " sdkversio:" + deviceInfo.get("sdkversion"));
        /////////////////////
        //Get device info end
        /////////////////////

        server.create();
        /////////////////////
        //Create new deviceuid start
        /////////////////////
        /*works
        try {
            FileInputStream userInputStream = context.openFileInput("startdust");
            InputStreamReader inputStreamReader = new InputStreamReader(userInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String lineData = bufferedReader.readLine();
            Log.d("berttest","lineData length is:" + lineData.length());
            int fileLength = lineData.length();
            if(fileLength>90 && fileLength<100){
                //deviceuid exists do nothing
                String[] parts = lineData.split("-");
                deviceuid = parts[0]; // 004
                hashkey = parts[1]; // 034556
                Log.d("berttest", "deviceuid exists and is:" + deviceuid + " hashkey:" + hashkey);
            }else{
                //createNewDeviceUID();
                //executorService.submit(createNewDeviceUID);
                server.createNewDeviceUID
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //createNewDeviceUID();
            executorService.submit(createNewDeviceUID);
        } catch (IOException e) {
            e.printStackTrace();
            //createNewDeviceUID();
            executorService.submit(createNewDeviceUID);
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
            public void onAdOpened() {

            }

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

    final Runnable playad = new Runnable() {
        public void run() {
            Log.d("berttest", "playad runnable called");
        }
    };

    final Runnable adclosed = new Runnable() {
        public void run() {
            Log.d("berttest", "adclosed runnable called");
        }
    };

    final Runnable createNewDeviceUID = new Runnable() {
        public void run() {
            MediaType MEDIA_TYPE =
                    MediaType.parse("application/json");

            JSONObject postdata = new JSONObject();
            try {
                //currentlu data is unsed serverside
                postdata.put("model", deviceInfo.get("model"));
                postdata.put("brand", deviceInfo.get("brand"));
                postdata.put("device", deviceInfo.get("device"));
                postdata.put("buildid", deviceInfo.get("buildid"));
                postdata.put("manufacturer", deviceInfo.get("manufacturer"));
                postdata.put("user", deviceInfo.get("user"));
                postdata.put("product", deviceInfo.get("product"));
                postdata.put("releaseversion", deviceInfo.get("releaseversion"));
                postdata.put("sdkversion", deviceInfo.get("sdkversion"));
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
            try {
                Response response = client.newCall(request).execute();
                if(response.isSuccessful()){
                    String jsonData = response.body().string();
                    Log.d("berttest","jsonData string is:" + jsonData);
                    try {
                        JSONObject jsonObject = new JSONObject(jsonData);
                        String newdeviceuid = jsonObject.getString("deviceuid");
                        Log.d("berttest", "deviceuid:"+newdeviceuid);
                        String newhk = jsonObject.getString("hk");
                        //create new file start
                        //creates new file and new entry even if exists
                        String filename = "startdust";
                        deviceuid = newdeviceuid;
                        hashkey = newhk;
                        String fileContents = deviceuid + "-" + hashkey;
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
}
