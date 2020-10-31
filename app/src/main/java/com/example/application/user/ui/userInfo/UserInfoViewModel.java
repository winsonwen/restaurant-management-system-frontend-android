package com.example.application.user.ui.userInfo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.RequestQueue;

public class UserInfoViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    RequestQueue requestqueue;


    public UserInfoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is User Info fragment");

    }

    public LiveData<String> getText() {
        return mText;
    }


}