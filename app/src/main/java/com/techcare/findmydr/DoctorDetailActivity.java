package com.techcare.findmydr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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

import java.io.IOException;

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
                                mapView.getMapAsync(DoctorDetailActivity.this);
                            } else {
                                Log.d("TAG", "No such document");
                            }
                        } else {
                            Log.d("TAG", "get failed with ", task.getException());
                        }
                    }
                });

        doctorDetailBinding.btnbookapt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FirebaseAuth.getInstance().getCurrentUser() !=null) {
                    Intent intent= new Intent(DoctorDetailActivity.this, AppointmentActivity.class);
                    intent.putExtra("Firestore Id", druid);
                    startActivity(intent);
                    finish();
                } else  {
                    Toast.makeText(DoctorDetailActivity.this, "Please Login First", Toast.LENGTH_SHORT).show();
                }
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
            }else {
                // Coordinates are not provided, hide the map and show the custom marker
                googleMap.clear(); // Clear any existing markers

                // Create a custom marker icon with the text "Doctor is off Duty"
                BitmapDescriptor customMarker = BitmapDescriptorFactory.fromBitmap(
                        createCustomMarker("Doctor is off Duty"));

                // Add the custom marker to the map
                Geocoder geocoder= new Geocoder(DoctorDetailActivity.this);
                String city= doctor.getDoctorHomeTown();
                double lat=geocoder.getFromLocationName(city,1).get(0).getLatitude();
                double longi=geocoder.getFromLocationName(city,1).get(0).getLongitude();
                LatLng markerPosition = new LatLng(lat, longi);
                googleMap.addMarker(new MarkerOptions()
                        .position(markerPosition)
                        .icon(customMarker));
                // Optionally, move the camera to the marker's position
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, 18));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Create a custom marker bitmap with text
    private Bitmap createCustomMarker(String text) {
        // Customize the appearance of your custom marker here
        Bitmap bitmap = Bitmap.createBitmap(300, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(30);
        paint.setTextAlign(Paint.Align.CENTER);

        // Draw the text on the marker
        canvas.drawText(text, canvas.getWidth() / 2, canvas.getHeight() / 2, paint);

        return bitmap;
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