package com.example.application.user.ui.googlemaps;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GoogleMapsViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public GoogleMapsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is google maps fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}