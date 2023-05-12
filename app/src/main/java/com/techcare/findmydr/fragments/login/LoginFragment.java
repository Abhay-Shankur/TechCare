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
import com.google.firebase.auth.FirebaseUser;
import com.techcare.findmydr.R;
import com.techcare.findmydr.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    FragmentLoginBinding binding;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentLoginBinding.inflate(inflater, container, false);

        binding.btnLoginLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkEmpty()) {
                    String email=binding.textEditEmailLogin.getText().toString();
                    String password=binding.textEditPasswordLogin.getText().toString();

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Authentication success", Toast.LENGTH_SHORT).show();
//                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                                getActivity().finish();
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
        if (binding.textEditEmailLogin.getText().toString().isEmpty()) {
            binding.textEditEmailLogin.setError("Empty Field");
            return false;
        }
        if (binding.textEditPasswordLogin.getText().toString().isEmpty()) {
            binding.textEditPasswordLogin.setError("Empty Field");
            return false;
        }
        return true;
    }

}