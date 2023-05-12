package com.techcare.findmydr.fragments.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.techcare.findmydr.LoginActivity;
import com.techcare.findmydr.R;
import com.techcare.findmydr.api.ApiClient;
import com.techcare.findmydr.api.ApiInterface;
import com.techcare.findmydr.api.response.ResponsePatient;
import com.techcare.findmydr.api.tablesclass.TablePatient;
import com.techcare.findmydr.databinding.FragmentSignupBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignupFragment extends Fragment {

    FragmentSignupBinding binding;

    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentSignupBinding.inflate(inflater, container, false);

        binding.btnSignupSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkEmpty()) {
                    String email=binding.textEditEmailSignup.getText().toString();
                    String password=binding.textEditPasswordSignup.getText().toString();
                    String name=binding.textEditNameSignup.getText().toString();
                    String phone=binding.textEditPhoneSignup.getText().toString();

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String uid=task.getResult().getUser().getUid();

                                Retrofit retrofit= ApiClient.getClient();
                                ApiInterface apiInterface= retrofit.create(ApiInterface.class);
                                apiInterface.setPatient(email, uid, name, password, "","",phone, "").enqueue(new Callback<ResponsePatient>() {
                                    @Override
                                    public void onResponse(Call<ResponsePatient> call, Response<ResponsePatient> response) {
                                        if(response.body().getStatusCode().equals("200")) {
                                            TablePatient user= new TablePatient();
                                            user.setPatientName(name);
                                            user.setPatientEmail(email);
                                            user.setPatientPassword(password);
                                            user.setPatientPhone(phone);
                                            FirebaseDatabase.getInstance().getReference().child("Users").child(uid).setValue(user);
                                            Toast.makeText(getContext(), "Sign Up Successful", Toast.LENGTH_SHORT).show();
                                        } else {
                                            FirebaseAuth.getInstance().getCurrentUser().delete();
                                            Toast.makeText(getContext(), response.body().getStatusMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponsePatient> call, Throwable t) {
                                        FirebaseAuth.getInstance().getCurrentUser().delete();
                                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                        Log.e("TAG", "onFailure: ", t);
                                    }
                                });

                            } else {
                                Toast.makeText(getContext(), "Authentication failed"+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                            getActivity().finish();
                        }
                    });
                }
            }
        });

        return binding.getRoot();
    }

    private boolean checkEmpty() {
        if (binding.textEditNameSignup.getText().toString().isEmpty()) {
            binding.textEditNameSignup.setError("Empty Field");
            return false;
        }

        if (binding.textEditEmailSignup.getText().toString().isEmpty()) {
            binding.textEditEmailSignup.setError("Empty Field");
            return false;
        }

        if (binding.textEditPhoneSignup.getText().toString().isEmpty()) {
            binding.textEditPhoneSignup.setError("Empty Field");
            return false;
        }

        if (binding.textEditPasswordSignup.getText().toString().isEmpty()) {
            binding.textEditPasswordSignup.setError("Empty Field");
            return false;
        }

        return true;
    }
}