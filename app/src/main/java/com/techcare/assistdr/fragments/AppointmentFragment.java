package com.techcare.assistdr.fragments;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.techcare.assistdr.R;
import com.techcare.assistdr.adapters.AppointmentAdapter;
import com.techcare.assistdr.api.ApiClient;
import com.techcare.assistdr.api.ApiInterface;
import com.techcare.assistdr.api.response.ResponseAppointment;
import com.techcare.assistdr.api.response.ResponseAppointmentLists;
import com.techcare.assistdr.api.tablesclass.TableAppointmentDetails;
import com.techcare.assistdr.api.tablesclass.TableAppointments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AppointmentFragment extends Fragment {
    ArrayList<TableAppointmentDetails> listData;
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

        listData= new ArrayList<TableAppointmentDetails>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        appointmentAdapter= new AppointmentAdapter(getContext(), listData);
        recyclerView.setAdapter(appointmentAdapter);

        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {

            String apiKey=FirebaseAuth.getInstance().getCurrentUser().getUid();
            Retrofit retrofit= ApiClient.getClient();
            ApiInterface apiInterface=retrofit.create(ApiInterface.class);

            ProgressDialog progressDialog= new ProgressDialog(getContext());
            progressDialog.setTitle("Appointments");
            progressDialog.setMessage("Please Wait while we are fetching your Appointments");
            progressDialog.show();
            apiInterface.getAppointmentsList(apiKey).enqueue(new Callback<ResponseAppointmentLists>() {
                @Override
                public void onResponse(Call<ResponseAppointmentLists> call, Response<ResponseAppointmentLists> response) {
                    if (response!=null) {
                        if (response.body().getStatusCode().equals("200") && response.body().getStatusMessage().equals("Data Found")) {
                            for (TableAppointments appointments: response.body().getDataList()) {
                                apiInterface.getAppointmentDetail(apiKey, appointments.getAppointmentId()).enqueue(new Callback<ResponseAppointment>() {
                                    @Override
                                    public void onResponse(Call<ResponseAppointment> call, Response<ResponseAppointment> response) {
                                        if (response!=null) {
                                            if (response.body().getStatusCode().equals("200") && response.body().getStatusMessage().equals("Data Found")) {
//                                                listData.add(response.body().getDataList().get(0));
                                                listData.add(listData.size(),response.body().getDataList().get(0));
                                                appointmentAdapter.notifyDataSetChanged();
                                            } else {
                                                Log.d("TAG", "onResponse: Response Not Match");
                                            }
                                        } else {
                                            Log.d("TAG", "onResponse: Response Null");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseAppointment> call, Throwable t) {
                                        Log.e("TAG", "onFailure: Response  Fail ", t);
                                    }
                                });
                            }
                        } else {
                            Log.d("TAG", "onResponse: Response List Not Match");
                        }
                    } else {
                        Log.d("TAG", "onResponse: Response Null");
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseAppointmentLists> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.e("TAG", "onFailure: Response list Fail ", t);
                }
            });

        } else {
            Toast.makeText(getContext(), "Not Logged In", Toast.LENGTH_SHORT).show();
        }


        return view;
    }


}