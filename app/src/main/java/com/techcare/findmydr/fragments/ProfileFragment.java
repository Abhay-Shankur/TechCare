package com.techcare.findmydr.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.techcare.findmydr.LoginActivity;
import com.techcare.findmydr.databinding.FragmentProfileBinding;
import com.techcare.findmydr.modules.Patient;

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
            FirebaseDatabase.getInstance()
                    .getReference("Patients")
                    .child("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {

                            if (snapshot.exists()) {
                                Patient p = snapshot.getValue(Patient.class);
                                FirebaseFirestore.getInstance().collection("Patients")
                                        .document(p.getPatientFirestore())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(Task<DocumentSnapshot> task) {
                                                if(task.isSuccessful()){
                                                    fragmentProfileBinding.textView3.setText(task.getResult().get("patientName").toString());
                                                }
                                            }
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            fragmentProfileBinding.textView3.setText("Error!");
                        }
                    });
            fragmentProfileBinding.textView3.setText("");
            fragmentProfileBinding.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    onResume();
                }
            });
        } else {
            fragmentProfileBinding.button.setText("Log In");
            fragmentProfileBinding.textView3.setText("");
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