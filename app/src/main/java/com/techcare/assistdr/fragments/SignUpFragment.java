package com.techcare.assistdr.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.techcare.assistdr.R;
//import com.techcare.assistdr.api.ApiClient;
//import com.techcare.assistdr.api.ApiInterface;
//import com.techcare.assistdr.api.response.ResponseDrAuth;
//import com.techcare.assistdr.api.tablesclass.TableDrAuth;
import com.techcare.assistdr.modules.Doctor;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

//    Initialize the Views
    EditText name,email,phone,pass,cpass;
    Button signup;
    ProgressDialog loading;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_sign_up, container, false);

        name=view.findViewById(R.id.custom_editTextTextPersonName);
        email=view.findViewById(R.id.custom_editTextTextEmailAddress2);
        phone=view.findViewById(R.id.custom_editTextPhone2);
        pass=view.findViewById(R.id.custom_editTextTextPassword2);
        cpass=view.findViewById(R.id.custom_editTextTextConfirmPassword2);
        signup=view.findViewById(R.id.custom_signup);

        loading= new ProgressDialog(getContext());
        loading.setTitle("Sign Up");
        loading.setMessage("Please Wait while we are Creating your Account ");
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkForRequired()) {
                    loading.show();
                    if (pass.getText().toString().equals(cpass.getText().toString())) {
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString()).addOnCompleteListener(
                                new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NotNull Task<AuthResult> task) {
                                        loading.dismiss();
                                        if (task.isSuccessful()){
                                            loading= new ProgressDialog(getContext());
                                            loading.setTitle("Sign Up");
                                            loading.setMessage("Loading...");
                                            loading.show();
                                            String id=task.getResult().getUser().getUid();
                                            Doctor doctor = new Doctor();
                                            doctor.setDoctorUid(id);
                                            doctor.setDoctorName(name.getText().toString());
                                            doctor.setDoctorEmail(email.getText().toString());
                                            doctor.setDoctorPhone(phone.getText().toString());
                                            doctor.setDoctorPassword(pass.getText().toString());
//                                            TableDrAuth doctor
                                            FirebaseFirestore.getInstance()
                                                    .collection("Doctors")
                                                    .add(doctor)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                                                            FirebaseFirestore.getInstance().collection("Doctors")
                                                                    .document(documentReference.getId())
                                                                    .update("doctorFirestore",documentReference.getId());
                                                            Doctor d = new Doctor();
                                                            d.setDoctorFirestore(documentReference.getId());
                                                            FirebaseDatabase.getInstance()
                                                                    .getReference()
                                                                    .child("Doctors")
                                                                    .child("Users")
                                                                    .child(id)
                                                                    .setValue(d);
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(Exception e) {

                                                        }
                                                    });


                                            Toast.makeText(getContext(), "Sign Up Successful", Toast.LENGTH_SHORT).show();
                                            loading.dismiss();
                                            getActivity().finish();
//                                            Retrofit retrofit =ApiClient.getClient();
//                                            ApiInterface apiInterface=retrofit.create(ApiInterface.class);
//                                            apiInterface.postDrAuth(id, name.getText().toString(), email.getText().toString(), pass.getText().toString(),                                                       phone.getText().toString()).enqueue(new Callback<ResponseDrAuth>() {
//                                                    @Override
//                                                    public void onResponse(Call<ResponseDrAuth> call, Response<ResponseDrAuth> response) {
//                                                        loading.dismiss();
//                                                        if(response.body().getStatusCode().equals("200")) {
//                                                            Toast.makeText(getContext(), "Sign Up Successful", Toast.LENGTH_SHORT).show();
//                                                        } else {
//                                                            FirebaseAuth.getInstance().getCurrentUser().delete();
//                                                            FirebaseDatabase.getInstance().getReference().child("Users").child(id).removeValue();
//                                                            Toast.makeText(getContext(), "Couldn't Sign Up", Toast.LENGTH_SHORT).show();
//                                                        }
//                                                        getActivity().finish();
//                                                    }
//
//                                                    @Override
//                                                    public void onFailure(Call<ResponseDrAuth> call, Throwable t) {
//                                                        loading.dismiss();
//                                                        FirebaseAuth.getInstance().getCurrentUser().delete();
//                                                        FirebaseDatabase.getInstance().getReference().child("Users").child(id).removeValue();
//                                                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
//                                                        Log.e("TAG", "onFailure: ", t);
//                                                        getActivity().finish();
//                                                    }
//                                                });

//                                            getActivity().finish();
                                        } else {
                                            Toast.makeText(getContext(), checkExceptions(task.getException().toString()), Toast.LENGTH_SHORT).show();
                                            Log.d("SignUp Fragment", "Exception: "+task.getException().toString());
                                        }
                                    }
                                }
                        );
                    } else {
                        loading.dismiss();
                        cpass.setError("Password doesn't match !");
                    }
                }
            }
        }); //Signup On click end



        return view;
    }

//    Check for not Null
    private boolean checkForRequired() {
        int t=0;
        if (name.getText().toString().isEmpty()) {
            name.setError("Name is Required");
            t=1;
        }
        if (email.getText().toString().isEmpty()) {
            email.setError("Email is Required");
            t=1;
        }
        if (phone.getText().toString().isEmpty()) {
            phone.setError("Phone is Required");
            t=1;
        }
        if (pass.getText().toString().isEmpty()) {
            pass.setError("Password is Required");
            t=1;
        }
        if (cpass.getText().toString().isEmpty()) {
            cpass.setError("Password is Required");
            t=1;
        }
        return t==0;
    }

//    Check for Exception
    private String checkExceptions(String exceptionMsg){
        String msg=null;
        if (exceptionMsg.contains("The email address is already in use by another account")) {
            msg="Already a User !";
        } else if (exceptionMsg.contains("Password should be at least 6 characters")) {
            msg="Password should be at least 6 characters !";
        }
        return msg;
    }

//    Remove Child
    private void removeAuth() {

    }
}