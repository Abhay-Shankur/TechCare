package com.techcare.findmydr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.DateTime;
import com.techcare.findmydr.modules.AppointmentDetails;
import com.techcare.findmydr.databinding.ActivityAppointmentBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AppointmentActivity extends AppCompatActivity {

    private LocationRequest locationRequest;
    ActivityAppointmentBinding activityAppointmentBinding;
    ProgressDialog progressDialog;
    AppointmentDetails appointmentDetails = new AppointmentDetails();
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAppointmentBinding = ActivityAppointmentBinding.inflate(getLayoutInflater());
        setContentView(activityAppointmentBinding.getRoot());
        getSupportActionBar().setTitle(" ");

        String drfrid = getIntent().getExtras().getString("Firestore Id");
        activityAppointmentBinding.btnBkApt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fusedLocationClient = LocationServices.getFusedLocationProviderClient(AppointmentActivity.this);

                // Check for location permission
                if (ContextCompat.checkSelfPermission(AppointmentActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    progressDialog = new ProgressDialog(AppointmentActivity.this);
                    progressDialog.setTitle("Booking an Appointment for you");
                    progressDialog.setMessage("Please wait until we are booking your Appointment.");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show();
                    requestLocationUpdates();
                } else {
                    // Request location permission
                    ActivityCompat.requestPermissions(AppointmentActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_PERMISSION_REQUEST_CODE);
                }

                int id = activityAppointmentBinding.radioGroup.getCheckedRadioButtonId();
                RadioButton r = findViewById(id);

                appointmentDetails.setDoctorId(drfrid);
                appointmentDetails.setappointmentName(activityAppointmentBinding.textLayoutNameEdit.getText().toString());
                appointmentDetails.setappointmentPhone(activityAppointmentBinding.textLayoutPhoneEdit.getText().toString());
                appointmentDetails.setappointmentGender(r.getText().toString());
                appointmentDetails.setappointmentBirthdate(activityAppointmentBinding.editTextBirthDate.getText().toString());
                String schdate = activityAppointmentBinding.editTextSchDate.getText().toString();
                String schtime = activityAppointmentBinding.editTextSchTime.getText().toString();
                String sch = schdate + " " + schtime;
                appointmentDetails.setAppointmentSchedule(sch);
                r = findViewById(activityAppointmentBinding.radioGroupEmergency.getCheckedRadioButtonId());
                appointmentDetails.setEmergency(r.getText().equals("Yes"));
                appointmentDetails.setAppointmentDescription(activityAppointmentBinding.editTextTextMultiLine.getText().toString());

            }
        });
    }

    private void requestLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setNumUpdates(1);
        locationRequest.setInterval(0); // Update location every 5 seconds

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        HashMap<String, Object> livelocation = new HashMap<>();
                        livelocation.put("latitude", location.getLatitude());
                        livelocation.put("longitude", location.getLongitude());
                        appointmentDetails.setLiveLocation(livelocation);
                        setAppointment(appointmentDetails);
                    }
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AppointmentActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted, request location updates
                requestLocationUpdates();
            } else {
                // Location permission denied, handle accordingly (e.g., show a message)
                Log.w("TAG", "onRequestPermissionsResult: " );
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop location updates when the activity is destroyed
        if (locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }
//    Creating an Appointment Object and setting it into Firestore and FirestoreId assigned to Doctor into RTDb.
    private void setAppointment(AppointmentDetails appointmentDetails) {

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        firestore.collection("Doctors")
                .document(appointmentDetails.getDoctorId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            Log.w("TAG", "onComplete: "+task.getResult().toString());
                            String doctorUid=task.getResult().get("doctorUid").toString();
                            firestore.collection("Appointments")
                                    .add(appointmentDetails)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            firestore.collection("Appointments")
                                                    .document(documentReference.getId())
                                                    .update("appointmentId",documentReference.getId());

                                            reference.child("Appointments")
                                                    .child(doctorUid)
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot snapshot) {
                                                            reference.child("Appointments")
                                                                    .child(doctorUid)
                                                                    .child(String.valueOf(snapshot.getChildrenCount()))
                                                                    .setValue(documentReference.getId());
                                                            Toast.makeText(AppointmentActivity.this, "Appointment Booked Successfully.", Toast.LENGTH_SHORT).show();
                                                            progressDialog.dismiss();
                                                            finish();
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError error) {
                                                            Log.e("TAG", "onCancelled: ", error.toException());
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(Exception e) {
                                            Log.e("TAG", "onFailure: ", e);
                                        }
                                    });
                        }
                    }
                });
        progressDialog.dismiss();
    }

//    Function for setting the BirthDate and schedule.
    public void handleDateAndTime(View view) {
        final Calendar calendar= Calendar.getInstance();
        if (view.getId() == activityAppointmentBinding.btnSetBday.getId()){

            int Y=calendar.get(Calendar.YEAR);
            int M=calendar.get(Calendar.MONTH);
            int D=calendar.get(Calendar.DATE);

            DatePickerDialog datePickerDialog= new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                    Calendar c=Calendar.getInstance();
                    c.set(Calendar.YEAR, year);
                    c.set(Calendar.MONTH, month);
                    c.set(Calendar.DATE, date);

                    CharSequence dateSequence= DateFormat.format("yyyy-MM-dd",c);
                    activityAppointmentBinding.editTextBirthDate.setText(dateSequence);
                }
            }, Y, M, D);
            datePickerDialog.show();
        } else if (view.getId() == activityAppointmentBinding.btnSetSchdate.getId()) {

            int Y=calendar.get(Calendar.YEAR);
            int M=calendar.get(Calendar.MONTH);
            int D=calendar.get(Calendar.DATE);

            DatePickerDialog datePickerDialog= new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                    Calendar c=Calendar.getInstance();
                    c.set(Calendar.YEAR, year);
                    c.set(Calendar.MONTH, month);
                    c.set(Calendar.DATE, date);

                    CharSequence dateSequence= DateFormat.format("yyyy-MM-dd",c);
                    activityAppointmentBinding.editTextSchDate.setText(dateSequence);
                }
            }, Y, M, D);
            datePickerDialog.show();
        } else if (view.getId() == activityAppointmentBinding.btnSetSchtime.getId()) {

            int H=calendar.get(Calendar.HOUR);
            int M=calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog= new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int min) {
                    Calendar c=Calendar.getInstance();
                    c.set(Calendar.HOUR, hour);
                    c.set(Calendar.MINUTE, min);
                    c.set(Calendar.SECOND, 0);

                    CharSequence timeSequence= DateFormat.format("HH:mm:ss",c);
                    activityAppointmentBinding.editTextSchTime.setText(timeSequence);
                }
            }, H, M, true);
            timePickerDialog.show();
        }
    }
}