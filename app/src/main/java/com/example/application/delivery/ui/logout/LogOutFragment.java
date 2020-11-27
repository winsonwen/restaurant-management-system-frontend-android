package com.example.application.delivery.ui.logout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.application.R;
import com.example.application.delivery.DeliveryManActivity;
import com.example.application.delivery.SharedViewModel;

public class LogOutFragment extends Fragment {

    private LogOutListener logOutListener;
    SharedViewModel sharedViewModel;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sharedViewModel = ViewModelProviders.of(this).get(SharedViewModel.class);
        sharedViewModel.stopLocationFuture();

        if (context instanceof LogOutListener) {
            logOutListener = (LogOutListener) context;
            logOutListener.LogOutInteraction();
        } else {
            throw new RuntimeException("Please implement the required interface(s)");
        }
    }


    public interface LogOutListener{
        void LogOutInteraction();
    }

}