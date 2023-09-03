package com.techcare.assistdr;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.techcare.assistdr.api.ApiClient;
//import com.techcare.assistdr.api.ApiInterface;
//import com.techcare.assistdr.api.response.ResponseDoctors;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.techcare.assistdr.databinding.ActivityEditProfileBinding;
import com.techcare.assistdr.modules.Doctor;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditProfileActivity extends AppCompatActivity {

    ActivityEditProfileBinding binding;


    String drSpecialisList[] = {"Allergists/Immunologists", "Anesthesiologists", "Cardiologists", "Colon and Rectal Surgeons", "Critical Care Medicine Specialists", "Dermatologists", "Endocrinologists", "Emergency Medicine Specialists", "Family Physicians", "Gastroenterologists", "Geriatric Medicine Specialists", "Hematologists", "Hospice and Palliative Medicine Specialists", "Infectious Disease Specialists", "Internists", "Medical Geneticists", "Nephrologies", "Neurologists", "Obstetricians and Gynecologists", "Oncologists", "Ophthalmologists", "Osteopaths ", "Otolaryngologies", "Pathologists", "Pediatricians", "Psychiatrists", "Plastic Surgeons", "Podiatrists", "Preventive Medicine Specialists", "Psychiatrists", "Pulmonologists", "Radiologists", "Rheumatologists", "Sleep Medicine Specialists", "Sports Medicine Specialists", "General Surgeons", "Urologists"};

    String drEducationList[] ={"Bachelor of Medicine, Bachelor of Surgery - MBBS", "Master of Surgery - MS", "Doctor of Medicine - MD", "Bachelor of Ayurvedic Medicine and Surgery - BAMS", "Bachelor of Homeopathic Medicine and Surgery - BHMS", "Bachelor of Physiotherapy - BPT", "Bachelor of Veterinary Science - B.VSc", "Bachelor of Unani Medicine and Surgery - BUMS", "Bachelor of Siddha Medicine and Surgery - BSMS", "Bachelor of Naturopathy and Yoga - BNYS"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        TODO Complete the Activity
        getSupportActionBar().hide();

//        ArrayAdapter adapter= new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, drSpecialisList);
        ArrayAdapter<String> adapterDrSpecialisList = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, drSpecialisList);
        binding.editTextSpecialis.setAdapter(adapterDrSpecialisList);

        ArrayAdapter<String> adapterDrEducationList = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, drEducationList);
        binding.editTextEducation.setAdapter(adapterDrEducationList);

        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {
            String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            DatabaseReference databaseReference =firebaseDatabase.getReference().child("Doctors").child("Users").child(uid);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NotNull DataSnapshot snapshot) {
                    Doctor doctor =snapshot.getValue(Doctor.class);
                    binding.editTextName.setText(doctor.getDoctorName());
                    binding.editTextSpecialis.setText(doctor.getDoctorSpecialis());
                    binding.editTextEducation.setText(doctor.getDoctorEducation());
                    binding.editTextHomeTown.setText(doctor.getDoctorHomeTown());
                }

                @Override
                public void onCancelled(@NotNull DatabaseError error) {
                    Log.e("TAG", "onCancelled: ", error.toException());
                }
            });
        }

        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkFields()) {
                    String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference =firebaseDatabase.getReference().child("Doctors").child("Users").child(uid);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NotNull DataSnapshot snapshot) {
                            Doctor doctor =snapshot.getValue(Doctor.class);
                            doctor.setDoctorName(binding.editTextName.getText().toString());
                            doctor.setDoctorSpecialis(binding.editTextSpecialis.getText().toString());
                            doctor.setDoctorEducation(binding.editTextEducation.getText().toString());
                            doctor.setDoctorHomeTown(binding.editTextHomeTown.getText().toString());
//                            FirebaseDatabase.getInstance().getReference().child("Users").child(uid).setValue(doctor);
                            updateData(doctor);
                            EditProfileActivity.this.finish();
                        }
                        @Override
                        public void onCancelled(@NotNull DatabaseError error) {
                            Log.e("TAG", "onCancelled: ", error.toException());
                        }
                    });
                }
            }
        });

    }

    private boolean checkFields() {
        if (binding.editTextName.getText().toString().isEmpty()) {
            binding.editTextName.setError("Enter Name");
            return false;
        }
        if (binding.editTextSpecialis.getText().toString().isEmpty()) {
            binding.editTextSpecialis.setError("Enter Specialist");
            return false;
        }
        if (binding.editTextEducation.getText().toString().isEmpty()) {
            binding.editTextEducation.setError("Enter Education");
            return false;
        }
        if (binding.editTextHomeTown.getText().toString().isEmpty()) {
            binding.editTextHomeTown.setError("Enter Home Town");
            return false;
        }
        return true;
    }

    private void updateData(Doctor doctor) {
        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        doctor.setDoctorName(binding.editTextName.getText().toString());
        doctor.setDoctorSpecialis(binding.editTextSpecialis.getText().toString());
        doctor.setDoctorEducation(binding.editTextEducation.getText().toString());
        doctor.setDoctorHomeTown(binding.editTextHomeTown.getText().toString());

        FirebaseDatabase.getInstance()
                .getReference()
                .child("Doctors")
                .child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Doctor d =snapshot.getValue(Doctor.class);
                        FirebaseFirestore.getInstance()
                                .collection("Doctors")
                                .document(d.getDoctorFirestore())
                                .update(
                                        "doctorName", doctor.getDoctorName().toString(),
                                        "doctorSpecialis", doctor.getDoctorSpecialis().toString(),
                                        "doctorEducation", doctor.getDoctorEducation().toString(),
                                        "doctorHomeTown", doctor.getDoctorHomeTown().toString(),
                                        "doctorFirestore", d.getDoctorFirestore().toString())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(EditProfileActivity.this, "Saved Changes", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                      Log.w("TAG", "Error adding document", e);
                                    }
                                });

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.w("TAG", "Error ");
                    }
                });
//        FirebaseFirestore.getInstance()
//                .collection("Doctors")
//                .add(doctor)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
//                        FirebaseDatabase.getInstance().getReference().child("Doctors").child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("doctorFirestore").setValue(documentReference.getId());
//                        Toast.makeText(EditProfileActivity.this, "Saved Changes", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NotNull Exception e) {
//                        Log.w("TAG", "Error adding document", e);
//                    }
//                });
//        Retrofit retrofit= ApiClient.getClient();
//        ApiInterface apiInterface=retrofit.create(ApiInterface.class);
//        ProgressDialog progressDialog=new ProgressDialog(EditProfileActivity.this);
//        progressDialog.setTitle("Profile");
//        progressDialog.setMessage("Saving Changes...");
//        progressDialog.show();
//        apiInterface.putDoctor(uid, name, "", specialis, education, homeTown).enqueue(new Callback<ResponseDoctors>() {
//            @Override
//            public void onResponse(Call<ResponseDoctors> call, Response<ResponseDoctors> response) {
//                progressDialog.dismiss();
//                if (response.body().getStatusCode().equals("200")) {
//                    FirebaseDatabase.getInstance().getReference().child("Users").child(uid).setValue(dr);
//                    Toast.makeText(EditProfileActivity.this, "Saved Changes", Toast.LENGTH_SHORT).show();
//                    finish();
//                } else {
//                    Log.d("TAG", "onResponse: Response not match "+response.body().getStatusMessage());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseDoctors> call, Throwable t) {
//                progressDialog.dismiss();
//                Log.d("TAG", "onResponse: Response Failed");
//            }
//        });
    }
}