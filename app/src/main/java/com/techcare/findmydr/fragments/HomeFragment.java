package com.techcare.findmydr.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.techcare.findmydr.adapters.DoctorListAdapter;
import com.techcare.findmydr.api.ApiClient;
import com.techcare.findmydr.api.ApiInterface;
import com.techcare.findmydr.api.response.ResponseDoctors;
import com.techcare.findmydr.api.tablesclass.TableDoctors;
import com.techcare.findmydr.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class HomeFragment extends Fragment {
    FragmentHomeBinding fragmentHomeBinding;
    ArrayList<TableDoctors> doctorsList;
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
//TODO FIREBASE REALTIME DATABASE setting 

        fragmentHomeBinding.includeTopDoctors.layoutTopDoctorsRecyclerView.setHasFixedSize(true);
        fragmentHomeBinding.includeTopDoctors.layoutTopDoctorsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        doctorsList= new ArrayList<TableDoctors>();
        adapter=new DoctorListAdapter(getContext(),doctorsList);
        fragmentHomeBinding.includeTopDoctors.layoutTopDoctorsRecyclerView.setAdapter(adapter);

        return fragmentHomeBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

//        Fetching List of Doctors
        fetchDoctorsList();
    }

    private void fetchDoctorsList() {
        Retrofit retrofit= ApiClient.getClient();
        ApiInterface apiInterface=retrofit.create(ApiInterface.class);

        ProgressDialog progressDialog= new ProgressDialog(getContext());
        progressDialog.setTitle("Find My DR");
        progressDialog.setMessage("Please Wait ");
        progressDialog.show();
//        String api= FirebaseAuth.getInstance().getCurrentUser().getUid();
        apiInterface.getDoctors().enqueue(new Callback<ResponseDoctors>() {
            @Override
            public void onResponse(Call<ResponseDoctors> call, Response<ResponseDoctors> response) {
                try {
                    if (response!=null){
                        if (response.body().getStatusCode().equals("200") && response.body().getStatusMessage().equals("Data Found")) {
                            for (TableDoctors dr:response.body().getDataList()) {
                                doctorsList.add(doctorsList.size(),dr);
                            }
//                            doctorsList=response.body().getDataList();
                            adapter.notifyDataSetChanged();
                        } else  {
                            Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Null Response", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Exception", Toast.LENGTH_SHORT).show();
                    Log.e("TAG", "onResponse: ", e);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseDoctors> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

}