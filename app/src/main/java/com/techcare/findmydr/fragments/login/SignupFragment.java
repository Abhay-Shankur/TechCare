package com.techcare.findmydr.fragments.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.techcare.findmydr.modules.Patient;
import com.techcare.findmydr.databinding.FragmentSignupBinding;

import org.jetbrains.annotations.NotNull;

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

                    String email = binding.textEditEmailSignup.getText().toString();
                    String password = binding.textEditPasswordSignup.getText().toString();
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NotNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String uid=task.getResult().getUser().getUid();
                                Patient user= new Patient();
                                user.setPatientName(binding.textEditNameSignup.getText().toString());
                                user.setPatientEmail(email);
                                user.setPatientPassword(password);
                                user.setPatientPhone(binding.textEditPhoneSignup.getText().toString());
                                FirebaseFirestore.getInstance()
                                        .collection("Patients")
                                        .add(user)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                FirebaseFirestore.getInstance().collection("Patients")
                                                        .document(documentReference.getId())
                                                        .update("patientFirestore",documentReference.getId(),
                                                                "patientUid",uid);
                                                Patient p = new Patient();
                                                p.setPatientFirestore(documentReference.getId());
                                                FirebaseDatabase.getInstance()
                                                        .getReference()
                                                        .child("Patients")
                                                        .child("Users")
                                                        .child(uid)
                                                        .setValue(p);
                                                Toast.makeText(getActivity(), "Login Success", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(Exception e) {
                                                Log.e("TAG", "onFailure: ", e);
                                                Toast.makeText(getActivity(), "Login Failed", Toast.LENGTH_SHORT).show();
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