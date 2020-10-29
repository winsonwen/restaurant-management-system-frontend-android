package com.example.application.user.UI.Interface;

import android.content.Context;

import androidx.fragment.app.Fragment;

public class userFragmentInterface extends Fragment {
    private LogOutListener logOutListener;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if(context instanceof LogOutListener){
            logOutListener = (LogOutListener) context;
        }else{
            throw new RuntimeException("Please implement the interface");
        }

    }

    public interface LogOutListener {
        void LogOutInteraction();
    }


}
