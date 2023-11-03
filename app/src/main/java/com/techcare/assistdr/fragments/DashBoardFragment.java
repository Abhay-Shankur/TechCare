package com.techcare.assistdr.fragments;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techcare.assistdr.MainActivity;
import com.techcare.assistdr.R;
import com.techcare.assistdr.databinding.FragmentDashBoardBinding;

public class DashBoardFragment extends Fragment {

    FragmentDashBoardBinding binding;

    public DashBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        View view = (ViewGroup) inflater.inflate(R.layout.fragment_dash_board, container, false);
        binding = FragmentDashBoardBinding.inflate(inflater, container, false);


        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {
            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Appointments")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            binding.customNotificationsCount.setText(String.valueOf(snapshot.getChildrenCount()));
                            binding.customThismonthCount.setText(String.valueOf(snapshot.getChildrenCount()));
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {

                        }
                    });
        }
    }
}