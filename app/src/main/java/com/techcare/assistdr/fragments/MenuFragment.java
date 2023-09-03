package com.techcare.assistdr.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.techcare.assistdr.EditProfileActivity;
import com.techcare.assistdr.LoginActivity;
import com.techcare.assistdr.databinding.FragmentMenuBinding;
import com.techcare.assistdr.modules.Doctor;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class MenuFragment extends Fragment {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 21;
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
            DatabaseReference databaseReference =firebaseDatabase.getReference().child("Doctors").child("Users").child(uid);
            ProgressDialog progressDialog=new ProgressDialog(getContext());
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NotNull DataSnapshot snapshot) {
//                    Log.d(TAG, "onDataChange: "+snapshot.exists());
                    if (snapshot.exists()) {
                        Doctor doctor =snapshot.getValue(Doctor.class);
                        menuBinding.textViewProfile.setText(doctor.getDoctorName());
                    }
                    progressDialog.dismiss();
                }
                @Override
                public void onCancelled(@NotNull DatabaseError error) {
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

            menuBinding.switch1.setVisibility(View.VISIBLE);
//            menuBinding.switch
            menuBinding.switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                   @Override
                   public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                       if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                               != PackageManager.PERMISSION_GRANTED) {
                           // Request permission
                           ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                   MY_PERMISSIONS_REQUEST_LOCATION);
                       }
                       if (menuBinding.switch1.isChecked()){
                           // Check if permission is granted
                           FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
                           fusedLocationClient.getLastLocation()
                                   .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                                       @Override
                                       public void onSuccess(Location location) {
                                           if (location != null) {
                                               HashMap<String,Object> livelocation = new HashMap<>();
                                               livelocation.put("latitude", location.getLatitude());
                                               livelocation.put("longitude", location.getLongitude());
                                               FirebaseDatabase.getInstance().getReference()
                                                       .child("Doctors")
                                                       .child("Users")
                                                       .child(uid)
                                                       .addListenerForSingleValueEvent(new ValueEventListener() {
                                                           @Override
                                                           public void onDataChange(DataSnapshot snapshot) {
                                                               Doctor d = snapshot.getValue(Doctor.class);
                                                               FirebaseFirestore.getInstance()
                                                                       .collection("Doctors")
                                                                       .document(d.getDoctorFirestore())
                                                                       .update("liveLocation", livelocation);
                                                           }

                                                           @Override
                                                           public void onCancelled(DatabaseError error) {

                                                           }
                                                       });
//                                               FirebaseFirestore.getInstance()
//                                                       .collection("Doctors")
//                                                       .document()
//                                               FirebaseDatabase.getInstance()
//                                                       .getReference()
//                                                       .child("Doctors")
//                                                       .child("Users")
//                                                       .child(uid)
//                                                       .child("liveLocation")
//                                                       .setValue(livelocation)
//                                                       .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                           @Override
//                                                           public void onSuccess(Void unused) {
//                                                               Toast.makeText(getContext(), "Live Location Updated", Toast.LENGTH_SHORT).show();
//                                                           }
//                                                       })
//                                                       .addOnFailureListener(new OnFailureListener() {
//                                                           @Override
//                                                           public void onFailure(Exception e) {
//                                                               Toast.makeText(getContext(), "ERROR for Live location", Toast.LENGTH_SHORT).show();
//                                                           }
//                                                       });
                                           }
                                       }
                                   });
                       } else {
                           FirebaseDatabase.getInstance().getReference()
                                   .child("Doctors")
                                   .child("Users")
                                   .child(uid)
                                   .addListenerForSingleValueEvent(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(DataSnapshot snapshot) {
                                           Doctor d = snapshot.getValue(Doctor.class);
                                           FirebaseFirestore.getInstance()
                                                   .collection("Doctors")
                                                   .document(d.getDoctorFirestore())
                                                   .update("liveLocation", null);
                                       }

                                       @Override
                                       public void onCancelled(DatabaseError error) {

                                       }
                                   });
                       }
                   }
               }
            );


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
            menuBinding.switch1.setVisibility(View.INVISIBLE);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

}