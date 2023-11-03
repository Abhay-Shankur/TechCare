package com.techcare.findmydr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.techcare.findmydr.databinding.ActivityMainBinding;
import com.techcare.findmydr.fragments.HomeFragment;
import com.techcare.findmydr.fragments.ProfileFragment;
import com.techcare.findmydr.fragments.ScheduleFragment;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        getSupportActionBar().hide();

        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(mainBinding.changingLayout.getId(), new HomeFragment());
        fragmentTransaction.commit();

        mainBinding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NotNull MenuItem item) {
                FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()) {
                    case R.id.bottomMenuHome:
                        fragmentTransaction.replace(mainBinding.changingLayout.getId(), new HomeFragment());
                        break;

                    case R.id.bottomMenuSchedule:
                        fragmentTransaction.replace(mainBinding.changingLayout.getId(), new ScheduleFragment());
                        break;

//                    case R.id.bottomMenuChat:
//                        fragmentTransaction.replace(mainBinding.changingLayout.getId(), new HomeFragment());
//                        break;

                    case R.id.bottomMenuMenu:
                        fragmentTransaction.replace(mainBinding.changingLayout.getId(), new ProfileFragment());
                        break;
                }
                fragmentTransaction.commit();
                return true;
            }
        });

    }
}