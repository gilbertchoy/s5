package com.me.gc.scratcher1;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.ClipData;
import android.util.Log;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<Integer> selected = new MutableLiveData<Integer>();

    public void setSelected(Integer i) {
        Log.d("berttest","setSelected works");
        selected.setValue(i);
    }

    public LiveData<Integer> getSelected() {
        Log.d("berttest", "getSelected works");
        return selected;
    }

}
