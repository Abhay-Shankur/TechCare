package com.techcare.assistdr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.techcare.assistdr.databinding.ActivityMakePrescriptionBinding;
import com.techcare.assistdr.modules.MakePrescription;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class MakePrescriptionActivity extends AppCompatActivity {

    ActivityMakePrescriptionBinding makePrescriptionBinding;
    private static final int PERMISSION_REQUSET_CODE=3812;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makePrescriptionBinding= ActivityMakePrescriptionBinding.inflate(getLayoutInflater());
        setContentView(makePrescriptionBinding.getRoot());

        if (ContextCompat.checkSelfPermission(MakePrescriptionActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MakePrescriptionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MakePrescriptionActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUSET_CODE);
        }

//        TODO ...
        Bundle extras=getIntent().getExtras();
        if (!extras.getString("AppointmentId").equals("Null")) {
            Toast.makeText(this, "From List", Toast.LENGTH_SHORT).show();
            makePrescriptionBinding.editTextTextPersonName1.setText(extras.getString("Patient Name"));
            makePrescriptionBinding.editTextTextPersonName2.setText(extras.getString("Patient Age"));
            makePrescriptionBinding.editTextTextPersonName3.setText(extras.getString("Patient Gender"));
        } else {
            Toast.makeText(this, "New", Toast.LENGTH_SHORT).show();
        }

        makePrescriptionBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkEmpty()) {
//                    TODO Set Data for Make Prescription
//                    SimpleDateFormat formatter6=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    Date date=new Date();
//                    Log.d("TAG", "onClick: "+date.toLocaleString());
                    date.setDate(date.getDate()+7);
//                    Log.d("TAG", "onClick: "+date.toLocaleString());
                    MakePrescription makePrescription=new MakePrescription(makePrescriptionBinding.getRoot().getContext());
                    makePrescription.setAppointmentId(getIntent().getExtras().getString("AppointmentId"));
                    makePrescription.setPatientName(makePrescriptionBinding.editTextTextPersonName1.getText().toString());
                    makePrescription.setPatientAge(makePrescriptionBinding.editTextTextPersonName2.getText().toString());
                    makePrescription.setPatientGender(makePrescriptionBinding.editTextTextPersonName3.getText().toString());
                    makePrescription.setPatientPhone(extras.getString("Patient Phone"));
                    makePrescription.setAdvice(makePrescriptionBinding.editTextTextMultiLine1.getText().toString());
                    makePrescription.setMedicine(makePrescriptionBinding.editTextTextMultiLine2.getText().toString().split("\\."));
                    makePrescription.setFollowup(date.toLocaleString());
                    makePrescription.onCreatePdf();
                    makePrescription.savePdf();
                } else {
                    Toast.makeText(MakePrescriptionActivity.this, "Empty Fields ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean checkEmpty() {
        if (makePrescriptionBinding.editTextTextPersonName1.getText().toString().isEmpty()) {
            makePrescriptionBinding.editTextTextPersonName1.setError("Empty Field");
            return false;
        }
        if (makePrescriptionBinding.editTextTextPersonName2.getText().toString().isEmpty()) {
            makePrescriptionBinding.editTextTextPersonName2.setError("Empty Field");
            return false;
        }
        if (makePrescriptionBinding.editTextTextPersonName3.getText().toString().isEmpty()) {
            makePrescriptionBinding.editTextTextPersonName3.setError("Empty Field");
            return false;
        }
        if (makePrescriptionBinding.editTextTextMultiLine1.getText().toString().isEmpty()) {
            makePrescriptionBinding.editTextTextMultiLine1.setError("Empty Field");
            return false;
        }
        if (makePrescriptionBinding.editTextTextMultiLine2.getText().toString().isEmpty()) {
            makePrescriptionBinding.editTextTextMultiLine2.setError("Empty Field");
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (
            requestCode==PERMISSION_REQUSET_CODE &&
            grantResults[0] == PackageManager.PERMISSION_DENIED &&
            grantResults[1] == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(MakePrescriptionActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUSET_CODE);
        }
    }
}