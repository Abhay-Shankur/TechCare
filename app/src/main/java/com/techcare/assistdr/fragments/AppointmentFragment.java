package com.techcare.assistdr.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.techcare.assistdr.R;
import com.techcare.assistdr.adapters.AppointmentAdapter;
import com.techcare.assistdr.modules.AppointmentDetails;

import java.util.ArrayList;
import java.util.List;

public class AppointmentFragment extends Fragment {
    ArrayList<AppointmentDetails> listData;
    AppointmentAdapter appointmentAdapter;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;

    public AppointmentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_appointment, container, false);
        recyclerView=view.findViewById(R.id.appointmentFragmentRecyclerView);



        listData= new ArrayList<AppointmentDetails>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        appointmentAdapter= new AppointmentAdapter(getContext(), listData);
        recyclerView.setAdapter(appointmentAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        listData.clear();


        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {

            progressDialog= new ProgressDialog(getLayoutInflater().getContext());
            progressDialog.setTitle("Appointments");
            progressDialog.setMessage("Please Wait while we are fetching your Appointments");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Appointments")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {

                            FirebaseFirestore firestore=FirebaseFirestore.getInstance();
                            for (DataSnapshot ds:snapshot.getChildren()) {
                                firestore.collection("Appointments")
                                        .document(ds.getValue().toString())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()){
                                                    DocumentSnapshot document=task.getResult();
                                                    if (document.exists()){
                                                        Gson gson=new Gson();
                                                        JsonElement jsonElement= gson.toJsonTree(document.getData());
                                                        AppointmentDetails details = gson.fromJson(jsonElement, AppointmentDetails.class);
                                                        listData.add(listData.size(), details);
                                                        appointmentAdapter.notifyDataSetChanged();
                                                    }
                                                }
                                            }
                                        });
                            }

//                            appointmentAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                            if (!snapshot.hasChildren())
                                Toast.makeText(getLayoutInflater().getContext(), "No Appointments", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {

                            progressDialog.dismiss();
                        }
                    });
//            progressDialog.dismiss();
        } else {
            Toast.makeText(getContext(), "Not Logged In", Toast.LENGTH_SHORT).show();
        }

    }
}