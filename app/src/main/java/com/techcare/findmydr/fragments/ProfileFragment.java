package com.techcare.findmydr.fragments;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.techcare.findmydr.LoginActivity;
import com.techcare.findmydr.R;
import com.techcare.findmydr.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding fragmentProfileBinding;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentProfileBinding=FragmentProfileBinding.inflate(inflater, container, false);


        return fragmentProfileBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {
            fragmentProfileBinding.button.setText("Log Out");
            fragmentProfileBinding.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                }
            });
        } else {
            fragmentProfileBinding.button.setText("Log In");
            fragmentProfileBinding.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}