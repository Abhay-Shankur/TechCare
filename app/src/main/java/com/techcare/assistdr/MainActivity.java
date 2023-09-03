package com.techcare.assistdr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.techcare.assistdr.databinding.ActivityMainBinding;
import com.techcare.assistdr.fragments.AppointmentFragment;
import com.techcare.assistdr.fragments.DashBoardFragment;
import com.techcare.assistdr.fragments.EPrescriptionFragment;
import com.techcare.assistdr.fragments.MenuFragment;
import com.techcare.assistdr.fragments.NotificationFragment;

import org.jetbrains.annotations.NotNull;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    int count=0;
//    Initializing the View
    ActivityMainBinding activityMainBinding;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        getSupportActionBar().hide();

//      Default Fragment setting.
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.replaceable_frameLayout,new DashBoardFragment());
        fragmentTransaction.commit();

//        Bottom Navigation Bar.
        activityMainBinding.customBottomNavigationBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NotNull MenuItem item) {
                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()){
                    case R.id.menuitem_dashboard :
                        fragmentTransaction.replace(activityMainBinding.replaceableFrameLayout.getId(),new DashBoardFragment());
                        break;

                    case R.id.menuitem_appointment :
                        fragmentTransaction.replace(activityMainBinding.replaceableFrameLayout.getId(),new AppointmentFragment());
                        break;

                    case R.id.menuitem_eprescription :
                        fragmentTransaction.replace(activityMainBinding.replaceableFrameLayout.getId(),new EPrescriptionFragment());
                        break;

//                    case R.id.menuitem_notification:
//                        fragmentTransaction.replace(activityMainBinding.replaceableFrameLayout.getId(),new NotificationFragment());
//                        break;

                    case R.id.menuitem_more:
                        fragmentTransaction.replace(activityMainBinding.replaceableFrameLayout.getId(),new MenuFragment());
                        break;
                }
                fragmentTransaction.commit();
                return true;
            }
        });
//        To Create Directory
        File dirPrescription = new File(getExternalFilesDir(null)+File.separator+"Prescriptions");
        if (!dirPrescription.exists()) {
            dirPrescription.mkdirs();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

//        Post Delay Function to execute
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FirebaseAuth.getInstance().getCurrentUser()==null && count==0) {
                    count++;
                    Intent intent = new Intent(getBaseContext(),LoginActivity.class);
                    startActivity(intent);
                }
            }
        },5000);
    }
}