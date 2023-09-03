package com.techcare.assistdr.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.techcare.assistdr.R;
import com.techcare.assistdr.adapters.AppointmentAdapter;
import com.techcare.assistdr.modules.AppointmentDetails;

import java.util.ArrayList;

public class AppointmentFragment extends Fragment {
    ArrayList<AppointmentDetails> listData;
    AppointmentAdapter appointmentAdapter;
    RecyclerView recyclerView;

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

        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {

            ProgressDialog progressDialog= new ProgressDialog(getContext());
            progressDialog.setTitle("Appointments");
            progressDialog.setMessage("Please Wait while we are fetching your Appointments");
            progressDialog.show();
            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Appointments")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                            Toast.makeText(getContext(), "onChildAdded", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
                            for (DataSnapshot ds: snapshot.getChildren()){
//                                ds.child("") TODO : Get list of Appointments
                            }
                        }

                        @Override
                        public void onChildRemoved(DataSnapshot snapshot) {
                            Toast.makeText(getContext(), "onChildRemoved", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onChildMoved(DataSnapshot snapshot, String previousChildName) {
                            Toast.makeText(getContext(), "onChildMoved", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            Toast.makeText(getContext(), "onCancelled", Toast.LENGTH_SHORT).show();
                        }
                    });
            progressDialog.dismiss();
//            String apiKey=FirebaseAuth.getInstance().getCurrentUser().getUid();
//            Retrofit retrofit= ApiClient.getClient();
//            ApiInterface apiInterface=retrofit.create(ApiInterface.class);
//
//            ProgressDialog progressDialog= new ProgressDialog(getContext());
//            progressDialog.setTitle("Appointments");
//            progressDialog.setMessage("Please Wait while we are fetching your Appointments");
//            progressDialog.show();
//            apiInterface.getAppointmentsList(apiKey).enqueue(new Callback<ResponseAppointmentLists>() {
//                @Override
//                public void onResponse(Call<ResponseAppointmentLists> call, Response<ResponseAppointmentLists> response) {
//                    if (response!=null) {
//                        if (response.body().getStatusCode().equals("200") && response.body().getStatusMessage().equals("Data Found")) {
//                            for (TableAppointments appointments: response.body().getDataList()) {
//                                apiInterface.getAppointmentDetail(apiKey, appointments.getAppointmentId()).enqueue(new Callback<ResponseAppointment>() {
//                                    @Override
//                                    public void onResponse(Call<ResponseAppointment> call, Response<ResponseAppointment> response) {
//                                        if (response!=null) {
//                                            if (response.body().getStatusCode().equals("200") && response.body().getStatusMessage().equals("Data Found")) {
////                                                listData.add(response.body().getDataList().get(0));
//                                                listData.add(listData.size(),response.body().getDataList().get(0));
//                                                appointmentAdapter.notifyDataSetChanged();
//                                            } else {
//                                                Log.d("TAG", "onResponse: Response Not Match");
//                                            }
//                                        } else {
//                                            Log.d("TAG", "onResponse: Response Null");
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<ResponseAppointment> call, Throwable t) {
//                                        Log.e("TAG", "onFailure: Response  Fail ", t);
//                                    }
//                                });
//                            }
//                        } else {
//                            Log.d("TAG", "onResponse: Response List Not Match");
//                        }
//                    } else {
//                        Log.d("TAG", "onResponse: Response Null");
//                    }
//                    progressDialog.dismiss();
//                }
//
//                @Override
//                public void onFailure(Call<ResponseAppointmentLists> call, Throwable t) {
//                    progressDialog.dismiss();
//                    Log.e("TAG", "onFailure: Response list Fail ", t);
//                }
//            });

        } else {
            Toast.makeText(getContext(), "Not Logged In", Toast.LENGTH_SHORT).show();
        }


        return view;
    }


}