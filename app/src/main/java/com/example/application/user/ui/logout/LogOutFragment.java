package com.example.application.user.ui.logout;

import android.content.Context;

import androidx.fragment.app.Fragment;

public class LogOutFragment extends Fragment {

    private com.example.application.delivery.ui.logout.LogOutFragment.LogOutListener logOutListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        if (context instanceof com.example.application.delivery.ui.logout.LogOutFragment.LogOutListener) {
            logOutListener = (com.example.application.delivery.ui.logout.LogOutFragment.LogOutListener) context;
            logOutListener.LogOutInteraction();
        } else {
            throw new RuntimeException("Please implement the required interface(s)");
        }
    }

}