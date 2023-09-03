package com.techcare.assistdr.adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.techcare.assistdr.fragments.LogInFragment;
import com.techcare.assistdr.fragments.SignUpFragment;

import org.jetbrains.annotations.NotNull;

public class LoginAdapter extends FragmentStateAdapter {

    private Context context;
    int totalTabs=2;

    public LoginAdapter(FragmentActivity fa) {
        super(fa);
    }

    @NotNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new SignUpFragment();
        }
        return new LogInFragment();
    }

    @Override
    public int getItemCount() {
        return totalTabs;
    }

}
