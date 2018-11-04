package com.me.gc.scratcher1;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<Integer> selected = new MutableLiveData<Integer>();
    private final MutableLiveData<Integer> points = new MutableLiveData<Integer>();
    private final MutableLiveData<Integer> backToHome = new MutableLiveData<Integer>();
    private final MutableLiveData<Integer> openDrawer = new MutableLiveData<Integer>();

    public void setSelected(Integer i) {
        Log.d("berttest","setSelected works");
        selected.setValue(i);
    }

    public LiveData<Integer> getSelected() {
        Log.d("berttest", "getSelected works");
        return selected;
    }

    //for points in top fragment
    public void setPoints(Integer i) {
        Log.d("berttest","setPoints works");
        points.setValue(i);
    }

    //for points in top fragment
    public LiveData<Integer> getPoints() {
        Log.d("berttest", "getPoints works");
        return points;
    }

    public void setBackToHome(Integer i) {
        Log.d("berttest","viewmodel setbackToHome works");
        backToHome.setValue(i);
    }

    public LiveData<Integer> getBackToHome() {
        Log.d("berttest", "viewmodel getbackToHome works");
        return backToHome;
    }

    public void setOpenDrawer(Integer i) {
        Log.d("berttest","viewmodel setbackToHome works");
        openDrawer.setValue(i);
    }

    public LiveData<Integer> getOpenDrawer() {
        Log.d("berttest", "viewmodel openDrawer works");
        return openDrawer;
    }

    /*
    //for points in drawer
    public void setPointsDrawer(Integer i) {
        Log.d("berttest","viewmodel setbackToHome works");
        openDrawer.setValue(i);
    }

    //for points in drawer
    public LiveData<Integer> getPointsDrawer() {
        Log.d("berttest", "viewmodel openDrawer works");
        return openDrawer;
    }
    */
}
