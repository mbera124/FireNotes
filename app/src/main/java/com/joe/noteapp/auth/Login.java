package com.joe.noteapp.auth;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.joe.noteapp.MainActivity;
import com.joe.noteapp.R;

public class Login extends AppCompatActivity {
    //    EditText lEmail,lPassword;
//    Button loginNow;
//    TextView forgetPass,createAcc;
//    FirebaseAuth fAuth;
//    FirebaseFirestore fStore;
//    FirebaseUser user;
//    ProgressBar spinner;
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 1001;

    GoogleSignInClient googleSignInClient;
    SignInButton btngooglesignin;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();

        btngooglesignin = findViewById(R.id.btngooglesignin);
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }

////        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
////        getSupportActionBar().setTitle("Login to FireNotes");
//
////        lEmail = findViewById(R.id.email);
////        lPassword = findViewById(R.id.lPassword);
////        loginNow = findViewById(R.id.loginBtn);
////
////        spinner = findViewById(R.id.progressBar3);
////
////        forgetPass = findViewById(R.id.forgotPasword);
////        createAcc = findViewById(R.id.createAccount);
//        user = FirebaseAuth.getInstance().getCurrentUser();
//
//        fAuth = FirebaseAuth.getInstance();
//        fStore = FirebaseFirestore.getInstance();
//
//
//        showWarning();
//
//        loginNow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String mEmail = lEmail.getText().toString();
//                String mPassword = lPassword.getText().toString();
//
//                if(mEmail.isEmpty() || mPassword.isEmpty()){
//                    Toast.makeText(Login.this, "Fields Are Required.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                // delete notes first
//
//                spinner.setVisibility(View.VISIBLE);
//
//                if(fAuth.getCurrentUser().isAnonymous()){
//                    FirebaseUser user = fAuth.getCurrentUser();
//
//                    fStore.collection("notes").document(user.getUid()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Toast.makeText(Login.this, "All Temp Notes are Deleted.", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                    // delete Temp user
//
//                    user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Toast.makeText(Login.this, "Temp user Deleted.", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//
//                fAuth.signInWithEmailAndPassword(mEmail,mPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                    @Override
//                    public void onSuccess(AuthResult authResult) {
//                        Toast.makeText(Login.this, "Success !", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                        finish();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(Login.this, "Login Failed. " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                        spinner.setVisibility(View.GONE);
//                    }
//                });
//            }
//        });
//
//        createAcc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(),Register.class));
//            }
//        });
//    }
//
//    private void showWarning() {
//        final AlertDialog.Builder warning = new AlertDialog.Builder(this)
//                .setTitle("Are you sure ?")
//                .setMessage("Linking Existing Account Will delete all the temp notes. Create New Account To Save them.")
//                .setPositiveButton("Save Notes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        startActivity(new Intent(getApplicationContext(),Register.class));
//                        finish();
//                    }
//                }).setNegativeButton("Its Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // do nothing
//                    }
//                });
//
//        warning.show();
//    }
        btngooglesignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch Sign In
                signInToGoogle();
            }
        });

        // Configure Google Client
        configureGoogleClient();
    }

    private void configureGoogleClient() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // for the requestIdToken, this is in the values.xml file that
                // is generated from your google-services.json
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.btngooglesignin);
        signInButton.setSize(SignInButton.SIZE_WIDE);

    }

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            Log.d(TAG, "Currently Signed in: " + currentUser.getEmail());
            showToastMessage("Currently Logged in: " + currentUser.getEmail());
        }
    }

    public void signInToGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                showToastMessage("Google Sign in Succeeded");
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                showToastMessage("Google Sign in Failed " + e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = auth.getCurrentUser();

                            Log.d(TAG, "signInWithCredential:success: currentUser: " + user.getEmail());

                            showToastMessage("Authentication Succeeded ");
                            startActivity(new Intent(Login.this, MainActivity.class));
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.

                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                            showToastMessage("Firebase Authentication failed:" + task.getException());
                        }
                    }
                });
    }

    private void showToastMessage(String message) {
        Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Login.this);
        alertDialog.setTitle("MORIAH");
        alertDialog.setMessage("Do you want to exit?");
        alertDialog.setIcon(R.drawable.list);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }


        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }


}
