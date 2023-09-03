package com.techcare.findmydr;

import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.techcare.findmydr.modules.Patient;
import com.techcare.findmydr.databinding.ActivityLoginBinding;
import com.techcare.findmydr.fragments.login.LoginFragment;
import com.techcare.findmydr.fragments.login.SignupFragment;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding loginBinding;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());

        getSupportActionBar().hide();

        getSupportFragmentManager().beginTransaction().replace(loginBinding.changingLayout.getId(), new LoginFragment()).commit();
        loginBinding.textView.setText("Don't have an Account");

        loginBinding.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loginBinding.textView.getText().equals("Already have an Account")) {
                    loginBinding.textView.setText("Don't have an Account");
                    getSupportFragmentManager().beginTransaction().replace(loginBinding.changingLayout.getId(), new LoginFragment()).commit();
                } else {
                    loginBinding.textView.setText("Already have an Account");
                    getSupportFragmentManager().beginTransaction().replace(loginBinding.changingLayout.getId(), new SignupFragment()).commit();
                }
            }
        });

        loginBinding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

//        Google Sign In Authentication
        // Configure Google Sign In TODO:Google signin
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    //    Private Methods
    int RC_SIGN_IN=21;
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w("Login Activity", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                            Patient user = new Patient();
                            user.setPatientEmail(firebaseUser.getEmail());
                            user.setPatientName(firebaseUser.getDisplayName());
                            user.setPatientPhone(firebaseUser.getPhoneNumber());

                            ProgressDialog loading= new ProgressDialog(LoginActivity.this);
                            loading.setTitle("Sign Up");
                            loading.setMessage("Loading...");
                            loading.show();
                            FirebaseFirestore.getInstance()
                                    .collection("Patients")
                                    .add(user)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            FirebaseFirestore.getInstance().collection("Patients")
                                                    .document(documentReference.getId())
                                                    .update("patientFirestore",documentReference.getId());
                                            FirebaseDatabase.getInstance().getReference().child("Patients")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .child("patientFirestore")
                                                    .setValue(documentReference.getId());
                                            Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(Exception e) {
                                            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            loading.dismiss();
//
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Login Activity", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }
}