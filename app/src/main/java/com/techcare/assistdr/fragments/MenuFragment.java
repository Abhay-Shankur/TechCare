package com.techcare.assistdr.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techcare.assistdr.EditProfileActivity;
import com.techcare.assistdr.LoginActivity;
import com.techcare.assistdr.databinding.FragmentMenuBinding;
import com.techcare.assistdr.modules.Doctor;

public class MenuFragment extends Fragment {
//    RecyclerView recyclerView;
//    MenuAdapter menuAdapter;
//    ArrayList<MenuListData> listData;
    final  String TAG="Menu Fragment";
    FragmentMenuBinding menuBinding;

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        menuBinding= FragmentMenuBinding.inflate(inflater, container, false);



        return menuBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();

            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            DatabaseReference databaseReference =firebaseDatabase.getReference().child("Users").child(uid);
            ProgressDialog progressDialog=new ProgressDialog(getContext());
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    Log.d(TAG, "onDataChange: "+snapshot.exists());
                    if (snapshot.exists()) {
                        Doctor doctor =snapshot.getValue(Doctor.class);
                        menuBinding.textViewProfile.setText(doctor.getDoctorName());
                    }
                    progressDialog.dismiss();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "onCancelled: ", error.toException());
                }
            });
            menuBinding.buttonEditProfile.setText("Edit Profile");
            menuBinding.buttonEditProfile.setVisibility(View.VISIBLE);
            menuBinding.buttonEditProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(getContext(),EditProfileActivity.class);
                    getActivity().startActivity(intent);
                }
            });

            menuBinding.buttonLoginsignup.setText("Log Out");
            ProgressDialog progressDialog1= new ProgressDialog(getContext());
            progressDialog1.setMessage("Logging out...");
            menuBinding.buttonLoginsignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog1.show();
                    FirebaseAuth.getInstance().signOut();
                    progressDialog1.dismiss();
                    FragmentTransaction fragmentTransaction= getParentFragmentManager().beginTransaction();
                    fragmentTransaction.replace(menuBinding.replaceableFrameLayout.getId(), new MenuFragment());
                    fragmentTransaction.commit();
                }
            });
        } else {
            menuBinding.textViewProfile.setText("Please Log In First !");
            menuBinding.buttonEditProfile.setVisibility(View.GONE);
            menuBinding.buttonLoginsignup.setText("Log In");
            menuBinding.buttonLoginsignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });
        }

    }
}