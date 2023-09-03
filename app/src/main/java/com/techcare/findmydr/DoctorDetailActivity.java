package com.techcare.findmydr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.techcare.findmydr.databinding.ActivityDoctorDetailBinding;
import com.techcare.findmydr.modules.Doctor;

import org.jetbrains.annotations.NotNull;

public class DoctorDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    ActivityDoctorDetailBinding doctorDetailBinding;
    MapView mapView;
    private Doctor doctor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doctorDetailBinding=ActivityDoctorDetailBinding.inflate(getLayoutInflater());
        setContentView(doctorDetailBinding.getRoot());
        getSupportActionBar().setTitle(" ");


        mapView=findViewById(R.id.mapView);
        mapView.getMapAsync(this::onMapReady);
        mapView.onCreate(savedInstanceState);

//      Getting Intent String Data
        Bundle extrasData=getIntent().getExtras();
        String druid=extrasData.getString("Firestore Id");

        if(FirebaseAuth.getInstance().getCurrentUser() !=null) {
            FirebaseFirestore.getInstance()
                    .collection("Doctors")
                    .document(druid)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Gson gson = new Gson();
                                    JsonElement jsonElement = gson.toJsonTree(document.getData());
                                    doctor = gson.fromJson(jsonElement, Doctor.class);
                                    doctorDetailBinding.textViewName.setText(doctor.getDoctorName());
                                    doctorDetailBinding.textViewSpecialis.setText(doctor.getDoctorSpecialis());
                                    if (doctor.getLiveLocation() != null && !doctor.getLiveLocation().isEmpty()) {
                                        mapView.getMapAsync(DoctorDetailActivity.this);
                                    }
                                } else {
                                    Log.d("TAG", "No such document");
                                }
                            } else {
                                Log.d("TAG", "get failed with ", task.getException());
                            }
                        }
                    });
        } else  {

        }

        doctorDetailBinding.btnbookapt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(DoctorDetailActivity.this, AppointmentActivity.class);
                intent.putExtra("Firestore Id", druid);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(@NotNull GoogleMap googleMap) {
        try {
//            String city="Mumbai";
            if (doctor != null && doctor.getLiveLocation() != null && !doctor.getLiveLocation().isEmpty()) {
                try {
                    double latitude = Double.parseDouble(doctor.getLiveLocation().get("latitude").toString());
                    double longitude = Double.parseDouble(doctor.getLiveLocation().get("longitude").toString());
                    LatLng position = new LatLng(latitude, longitude);
                    googleMap.addMarker(new MarkerOptions()
                            .position(position)
                            .title("Doctor's Location")); // You can set a title for the marker
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 18)); // Adjust zoom level as needed
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


//    Methods Override For MapView
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}