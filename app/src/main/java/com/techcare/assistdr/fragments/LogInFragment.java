package com.techcare.assistdr.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.techcare.assistdr.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogInFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogInFragment extends Fragment {

//    final String TAG="Login Fragment";

//    Initialize the Views
    EditText editText_email,editText_password;
    Button button_login;
    CheckBox checkBox_savepassword;


    public LogInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);

//        Getting the Views
        editText_email = view.findViewById(R.id.custom_editTextTextEmailAddress);
        editText_password=view.findViewById(R.id.custom_editTextPassword);
        button_login = view.findViewById(R.id.custom_login_button);
        checkBox_savepassword = view.findViewById(R.id.custom_checkBox);
        ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Signing in...");
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=editText_email.getText().toString();
                String password=editText_password.getText().toString();
                if(checkForRequired()) {
                    progressDialog.show();
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                getActivity().finish();
                            } else {
                                checkLoginException(task.getException().getLocalizedMessage());
                            }
                        }
                    });
                }
            }
        });

        return view;
    }

    private boolean checkForRequired(){
        int t=0;
        if (editText_email.getText().toString().isEmpty()) {
            editText_email.setError("Email is Required");
            t=1;
        }
        if (editText_password.getText().toString().isEmpty()) {
            editText_password.setError("Password is required");
            t=1;
        } else if (editText_password.length() < 8) {
            editText_password.setError("Password must be minimum 8 characters");
            t=1;
        }
        return t == 0;
    }

    private void checkLoginException(String msg) {
        if (msg.contains("password is invalid")) {
            Toast.makeText(getContext(), "You have Entered Invalid Password !", Toast.LENGTH_SHORT).show();
        } else if (msg.contains("no user record")) {
            Toast.makeText(getContext(), "You are Not Registered User !", Toast.LENGTH_SHORT).show();
        }
    }
}
//                                    dialog = new AlertDialog.Builder(getContext())
//                                        .setTitle("Login")
//                                        .setIcon(R.drawable.ic_baseline_warning_24)
//                                        .setMessage("You are Not Registered")
//                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialogInterface, int i) {
//                                                dialog.dismiss();
//                                            }
//                                        }).show();