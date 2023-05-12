package com.techcare.findmydr;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.techcare.findmydr.api.ApiClient;
import com.techcare.findmydr.api.ApiInterface;
import com.techcare.findmydr.api.response.ResponsePatient;
import com.techcare.findmydr.api.tablesclass.TablePatient;
import com.techcare.findmydr.databinding.ActivityLoginBinding;
import com.techcare.findmydr.fragments.login.LoginFragment;
import com.techcare.findmydr.fragments.login.SignupFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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
        // Configure Google Sign In
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

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
//                Log.d("Login Activity", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Login Activity", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                            TablePatient user = new TablePatient();
                            user.setPatientEmail(firebaseUser.getEmail());
                            user.setPatientName(firebaseUser.getDisplayName());
                            user.setPatientPhone(firebaseUser.getPhoneNumber());
                            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).setValue(user);

//                            ProgressDialog loading= new ProgressDialog(LoginActivity.this);
//                            loading.setTitle("Sign Up");
//                            loading.setMessage("Loading...");
//                            loading.show();

                            Retrofit retrofit= ApiClient.getClient();
                            ApiInterface apiInterface= retrofit.create(ApiInterface.class);
                            apiInterface.setPatient(firebaseUser.getEmail(), firebaseUser.getUid(), firebaseUser.getDisplayName(), "", "", "", firebaseUser.getPhoneNumber(), "").enqueue(new Callback<ResponsePatient>() {
                                @Override
                                public void onResponse(Call<ResponsePatient> call, Response<ResponsePatient> response) {
//                                    loading.dismiss();
                                    if(response.body().getStatusCode().equals("200")) {
                                        Toast.makeText(LoginActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                                    } else {
                                        FirebaseAuth.getInstance().getCurrentUser().delete();
                                        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).removeValue();
                                        Toast.makeText(LoginActivity.this, response.body().getStatusMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponsePatient> call, Throwable t) {
//                                    loading.dismiss();
                                    FirebaseAuth.getInstance().getCurrentUser().delete();
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).removeValue();
                                    Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    Log.e("TAG", "onFailure: ", t);
                                }
                            });
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Login Activity", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }
}