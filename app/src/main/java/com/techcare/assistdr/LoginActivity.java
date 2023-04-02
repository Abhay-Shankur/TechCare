package com.techcare.assistdr;

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
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.techcare.assistdr.adapters.LoginAdapter;
import com.techcare.assistdr.api.ApiClient;
import com.techcare.assistdr.api.ApiInterface;
import com.techcare.assistdr.api.response.ResponseDrAuth;
import com.techcare.assistdr.databinding.ActivityLoginBinding;
import com.techcare.assistdr.modules.Doctor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

//import io.realm.Realm;


public class LoginActivity extends AppCompatActivity {

//    Initialize the Views
    GoogleSignInClient mGoogleSignInClient;
    ActivityLoginBinding activityLoginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(activityLoginBinding.getRoot());
        getSupportActionBar().hide();

        LoginAdapter loginAdapter = new LoginAdapter(this);
        activityLoginBinding.customViewPager2.setAdapter(loginAdapter);
        new TabLayoutMediator(activityLoginBinding.customTabLayout, activityLoginBinding.customViewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position > 0) {
                    tab.setText("Sign Up");
                } else {
                    tab.setText("Log In");
                }
            }
        }).attach();

        activityLoginBinding.customFloatingActionButton.setOnClickListener(new View.OnClickListener() {
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
                Log.d("Login Activity", "firebaseAuthWithGoogle:" + account.getId());
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
                            Log.d("Login Activity", "signInWithCredential:success");
                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//                            updateUI(user);
                            Doctor dr=new Doctor();
                            dr.setDoctorEmail(firebaseUser.getEmail());
                            dr.setDoctorName(firebaseUser.getDisplayName());
                            dr.setDoctorPhone(firebaseUser.getPhoneNumber());
                            dr.setDoctorProfilepic(firebaseUser.getPhotoUrl().toString());
                            FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).setValue(dr);

                            Retrofit retrofit = ApiClient.getClient();
                            ApiInterface apiInterface=retrofit.create(ApiInterface.class);
                            ProgressDialog progressDialog= new ProgressDialog(LoginActivity.this);
                            progressDialog.setTitle("Sign Up");
                            progressDialog.setMessage("Loading...");
                            progressDialog.show();
                            apiInterface.postDrAuth(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getEmail(), null,firebaseUser.getPhoneNumber()).enqueue(new Callback<ResponseDrAuth>() {
                                @Override
                                public void onResponse(Call<ResponseDrAuth> call, Response<ResponseDrAuth> response) {
                                    progressDialog.dismiss();
                                    if(response.body().getStatusCode().equals("200")) {
                                        Toast.makeText(LoginActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        FirebaseAuth.getInstance().getCurrentUser().delete();
                                        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).removeValue();
                                        Toast.makeText(LoginActivity.this, "Couldn't Sign Up", Toast.LENGTH_SHORT).show();
                                        Log.d("TAG", "onResponse: "+response.body().getStatusMessage());
                                    }
//                                    getActivity().finish();
                                }

                                @Override
                                public void onFailure(Call<ResponseDrAuth> call, Throwable t) {
                                    progressDialog.dismiss();
                                    FirebaseAuth.getInstance().getCurrentUser().delete();
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid()).removeValue();
                                    Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    Log.e("TAG", "onFailure: ", t);
//                                    getActivity().finish();
                                }
                            });
//                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Login Activity", "signInWithCredential:failure", task.getException());
//                            updateUI(null);
                        }
                    }
                });
    }
}