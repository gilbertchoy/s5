package com.me.gc.scratcher1;



import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class MainActivity extends FragmentActivity {
    private MainViewModel model;
    private Context context;
    private FragmentManager fragmentManager;
    private BottomFragment bottomFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;


        model = ViewModelProviders.of(this).get(MainViewModel.class);
        model.getSelected().observe(this, Integer -> {
            Log.d("berttest","item selected via main activity") ;
            // Create a new FragmentManager
            this.fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = this.fragmentManager.beginTransaction();

            // Create a new Fragment to be placed in the activity layout
            ScratcherCardFragment scratcherCardFragment = new ScratcherCardFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            scratcherCardFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            //fragmentTransaction.replace(R.id.fragment_bottom, )
            //fragmentTransaction.add(R.id.fragment_bottom, scratcherCardFragment);
            fragmentTransaction.replace(R.id.fragment_bottom, scratcherCardFragment)
                    .addToBackStack(null).commit();
        });

        if (findViewById(R.id.fragment_top) != null) {
            if (savedInstanceState != null) {
                return;
            }
            this.fragmentManager = getSupportFragmentManager();
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
            this.fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = this.fragmentManager.beginTransaction();
            this.bottomFragment = new BottomFragment();
            this.bottomFragment.setArguments(getIntent().getExtras());
            fragmentTransaction.add(R.id.fragment_bottom, this.bottomFragment);
            fragmentTransaction.commit();
        }

        /*
        if (findViewById(R.id.fragment_top) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new FragmentManager
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            // Create a new Fragment to be placed in the activity layout
            TopFragment topFragment = new TopFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            topFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            fragmentTransaction.add(R.id.fragment_top, topFragment);
            fragmentTransaction.commit();


        }

        if (findViewById(R.id.fragment_bottom) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new FragmentManager
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            // Create a new Fragment to be placed in the activity layout
            BottomFragment bottomFragment = new BottomFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            bottomFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            fragmentTransaction.add(R.id.fragment_bottom, bottomFragment);
            fragmentTransaction.commit();
        }
        */
    }
}
