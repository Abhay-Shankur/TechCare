package com.techcare.assistdr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.techcare.assistdr.api.ApiClient;
import com.techcare.assistdr.api.ApiInterface;
import com.techcare.assistdr.api.response.ResponseAppointment;
import com.techcare.assistdr.api.tablesclass.TableAppointmentDetails;
import com.techcare.assistdr.databinding.ActivityAppointmentDetailBinding;
import com.techcare.assistdr.modules.MakePrescription;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AppointmentDetailActivity extends AppCompatActivity {

    ActivityAppointmentDetailBinding appointmentDetailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appointmentDetailBinding= ActivityAppointmentDetailBinding.inflate(getLayoutInflater());
        setContentView(appointmentDetailBinding.getRoot());

        Bundle extrasData=getIntent().getExtras();
        if (!extrasData.isEmpty()) {
            String id=extrasData.getString("AppointmentId");
            // Setting On click for Make Prescription Button
            appointmentDetailBinding.makePrescriptionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getBaseContext(), MakePrescriptionActivity.class);
                    intent.putExtra("AppointmentId", id);
                    intent.putExtra("Patient Name",appointmentDetailBinding.textLayoutEditName.getText().toString());
                    intent.putExtra("Patient Age",appointmentDetailBinding.textLayoutEditAge.getText().toString());
                    intent.putExtra("Patient Gender",appointmentDetailBinding.textLayoutEditGender.getText().toString());
                    intent.putExtra("Patient Phone",appointmentDetailBinding.textLayoutEditPhone.getText().toString());
                    startActivity(intent);
                }
            });

//      < Api Request >
            Retrofit retrofit= ApiClient.getClient();
            ApiInterface apiInterface=retrofit.create(ApiInterface.class);
            apiInterface.getAppointmentDetail(FirebaseAuth.getInstance().getCurrentUser().getUid(), id).enqueue(new Callback<ResponseAppointment>() {
                @Override
                public void onResponse(Call<ResponseAppointment> call, Response<ResponseAppointment> response) {
                    if (response!=null) {
                        if (response.body().getStatusCode().equals("200") && response.body().getStatusMessage().equals("Data Found")) {
                            String sDate=response.body().getDataList().get(0).getAppointmentbirthdate();
                            LocalDate date=LocalDate.parse(sDate);
                            LocalDate nowDate= LocalDate.now();
                            int age=Period.between(date, nowDate).getYears();
                            appointmentDetailBinding.textLayoutEditName.setText(response.body().getDataList().get(0).getAppointmentname());
                            appointmentDetailBinding.textLayoutEditAge.setText(String.valueOf(age));
                            appointmentDetailBinding.textLayoutEditGender.setText(response.body().getDataList().get(0).getAppointmentgender());
                            appointmentDetailBinding.textLayoutEditPhone.setText(response.body().getDataList().get(0).getAppointmentphone());
                        } else {
                            Log.d("TAG", "onResponse: Respone Not Match");
                        }
                    } else {
                        Log.d("TAG", "onResponse: Response Null");
                    }
                }

                @Override
                public void onFailure(Call<ResponseAppointment> call, Throwable t) {
                    Log.e("TAG", "onResponse: Response Fail", t);
                }
            });
//      </ Api Request >

        } else {
            Toast.makeText(this, "Bundle Extras is Empty", Toast.LENGTH_SHORT).show();
        }
    }
}