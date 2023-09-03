package com.techcare.assistdr;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
//import com.techcare.assistdr.api.ApiClient;
//import com.techcare.assistdr.api.ApiInterface;
//import com.techcare.assistdr.api.response.ResponseAppointment;
//import com.techcare.assistdr.api.tablesclass.TableAppointmentDetails;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.techcare.assistdr.databinding.ActivityAppointmentDetailBinding;
import com.techcare.assistdr.modules.AppointmentDetails;
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
    AppointmentDetails details;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appointmentDetailBinding= ActivityAppointmentDetailBinding.inflate(getLayoutInflater());
        setContentView(appointmentDetailBinding.getRoot());

        Bundle extrasData=getIntent().getExtras();
        if (!extrasData.isEmpty()) {
            String id=extrasData.getString("AppointmentId");
            progressDialog = new ProgressDialog(AppointmentDetailActivity.this);
            progressDialog.setTitle("Please Wait...");
            progressDialog.setMessage("Getting Appointment Details");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            FirebaseFirestore.getInstance()
                    .collection("Appointments")
                    .document(id)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                DocumentSnapshot document=task.getResult();
                                if (document.exists()){
                                    Gson gson=new Gson();
                                    JsonElement jsonElement=gson.toJsonTree(document.getData());
                                    details = gson.fromJson(jsonElement, AppointmentDetails.class);
                                    String sDate=details.getappointmentBirthdate();
                                    LocalDate date=LocalDate.parse(sDate);
                                    LocalDate nowDate= LocalDate.now();
                                    int age=Period.between(date, nowDate).getYears();
                                    appointmentDetailBinding.textLayoutEditName.setText(details.getappointmentName());
                                    appointmentDetailBinding.textLayoutEditAge.setText(String.valueOf(age));
                                    appointmentDetailBinding.textLayoutEditGender.setText(details.getappointmentGender());
                                    appointmentDetailBinding.textLayoutEditPhone.setText(details.getappointmentPhone());
                                }
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Bundle Extras is Empty", Toast.LENGTH_SHORT).show();
        }


        appointmentDetailBinding.makePrescriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getBaseContext(), MakePrescriptionActivity.class);
                intent.putExtra("AppointmentId", details.getAppointmentId());
                intent.putExtra("Patient Name",appointmentDetailBinding.textLayoutEditName.getText().toString());
                intent.putExtra("Patient Age",appointmentDetailBinding.textLayoutEditAge.getText().toString());
                intent.putExtra("Patient Gender",appointmentDetailBinding.textLayoutEditGender.getText().toString());
                intent.putExtra("Patient Phone",appointmentDetailBinding.textLayoutEditPhone.getText().toString());
                startActivity(intent);
            }
        });
    }
}