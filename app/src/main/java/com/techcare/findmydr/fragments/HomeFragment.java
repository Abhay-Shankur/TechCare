package com.techcare.findmydr.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.techcare.findmydr.adapters.DoctorListAdapter;
import com.techcare.findmydr.databinding.FragmentHomeBinding;
import com.techcare.findmydr.modules.Doctor;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    FragmentHomeBinding fragmentHomeBinding;
    ArrayList<Doctor> doctorsList;
    DoctorListAdapter adapter;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentHomeBinding=FragmentHomeBinding.inflate(inflater, container, false);

//        To Hide Layout If No Appointments

//        fragmentHomeBinding.includeAppointmentToday.layoutAppointmentTodayMain.setVisibility(View.GONE);

        fragmentHomeBinding.includeTopDoctors.layoutTopDoctorsRecyclerView.setHasFixedSize(true);
        fragmentHomeBinding.includeTopDoctors.layoutTopDoctorsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        doctorsList= new ArrayList<Doctor>();
        adapter=new DoctorListAdapter(getContext(),doctorsList);
        fragmentHomeBinding.includeTopDoctors.layoutTopDoctorsRecyclerView.setAdapter(adapter);

        return fragmentHomeBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        doctorsList.clear();
//        Fetching List of Doctors
        fetchDoctorsList();
    }

    private void fetchDoctorsList() {
        FirebaseDatabase.getInstance().getReference()
                .child("Doctors").child("Users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()) {
                            Doctor doctor = ds.getValue(Doctor.class);
                            Log.d("TAG", "onDataChange: "+doctor.getDoctorFirestore());
                            FirebaseFirestore.getInstance()
                                    .collection("Doctors")
                                    .document(doctor.getDoctorFirestore())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @SuppressLint("NotifyDataSetChanged")
                                        @Override
                                        public void onComplete(Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    Gson gson = new Gson();
                                                    JsonElement jsonElement = gson.toJsonTree(document.getData());
                                                    Doctor d = gson.fromJson(jsonElement, Doctor.class);
                                                    doctorsList.add(doctorsList.size(), d);
                                                    adapter.notifyDataSetChanged();
                                                } else {
                                                    Log.d("TAG", "No such document");
                                                }
                                            } else {
                                                Log.d("TAG", "get failed with ", task.getException());
                                            }
                                        }
                                    });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });

    }

}