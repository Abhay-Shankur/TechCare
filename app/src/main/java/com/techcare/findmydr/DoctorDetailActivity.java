package com.techcare.findmydr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.techcare.findmydr.api.ApiClient;
import com.techcare.findmydr.api.ApiInterface;
import com.techcare.findmydr.api.response.ResponseDoctors;
import com.techcare.findmydr.api.tablesclass.TableDoctors;
import com.techcare.findmydr.databinding.ActivityDoctorDetailBinding;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DoctorDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    ActivityDoctorDetailBinding doctorDetailBinding;
    MapView mapView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doctorDetailBinding=ActivityDoctorDetailBinding.inflate(getLayoutInflater());
        setContentView(doctorDetailBinding.getRoot());
        getSupportActionBar().setTitle(" ");


//        Google Map
        // Get a handle to the fragment and register the callback.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//        mapFragment.onCreate(savedInstanceState);
        mapView=findViewById(R.id.mapView);
        mapView.getMapAsync(this::onMapReady);
        mapView.onCreate(savedInstanceState);

//      Getting Intent String Data
        Bundle extrasData=getIntent().getExtras();
        String druid=extrasData.getString("Doctor Id");

        if(FirebaseAuth.getInstance().getCurrentUser() !=null) {
            String apikey=FirebaseAuth.getInstance().getCurrentUser().getUid();

//            <Api - Request >
            Retrofit retrofit= ApiClient.getClient();
            ApiInterface apiInterface= retrofit.create(ApiInterface.class);

            apiInterface.getDoctor(apikey,druid).enqueue(new Callback<ResponseDoctors>() {
                @Override
                public void onResponse(Call<ResponseDoctors> call, Response<ResponseDoctors> response) {
                    if (response!=null) {
                        if (response.body().getStatusCode().equals("200") && response.body().getStatusMessage().equals("Data Found")) {
                            TableDoctors tableDoctors=response.body().getDataList().get(0);
                            doctorDetailBinding.textViewName.setText(tableDoctors.getDoctorName());
                            doctorDetailBinding.textViewSpecialis.setText(tableDoctors.getDoctorSpecialis());

                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseDoctors> call, Throwable t) {

                }
            });
        } else  {

        }

        doctorDetailBinding.btnbookapt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(DoctorDetailActivity.this, AppointmentActivity.class);
                intent.putExtra("Doctor Id",getIntent().getExtras().getString("Doctor Id"));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
//        googleMap.addMarker(new MarkerOptions()
//                .position(new LatLng(19.075983, 72.877655))
//                .title("Marker"));

        // Add a marker in Sydney and move the camera
        Geocoder geocoder= new Geocoder(DoctorDetailActivity.this);
        try {
            String city="Mumbai";
            double lat=geocoder.getFromLocationName(city,1).get(0).getLatitude();
            double longi=geocoder.getFromLocationName(city,1).get(0).getLongitude();
            LatLng position = new LatLng(lat, longi);
            googleMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title("Marker in "+city));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        } catch (IOException e) {
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
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}