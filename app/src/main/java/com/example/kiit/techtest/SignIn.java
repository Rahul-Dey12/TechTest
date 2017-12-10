package com.example.kiit.techtest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class SignIn extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,View.OnClickListener{

    private SignInButton signInbtn;
    private Button mSign, mSignUp;
    private GoogleApiClient googleApiClient;
    private static final int RQ_CODE = 1;
    private FirebaseAuth lAuth;
    private static final String TAG = "nm";
    private FirebaseAuth.AuthStateListener lAuthlistener;
    private ProgressDialog mProgress;
    private EditText edName, edPass;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = lAuth.getCurrentUser();
        lAuth.addAuthStateListener(lAuthlistener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (lAuthlistener != null) {
            lAuth.removeAuthStateListener(lAuthlistener);


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mProgress = new ProgressDialog(this);

        mSign = (Button) findViewById(R.id.btn_newsign);
        mSign.setOnClickListener(this);

        mSignUp = (Button) findViewById(R.id.btn_signUp);
        mSignUp.setOnClickListener(this);

        edName = (EditText) findViewById(R.id.ed_name);
        edPass = (EditText) findViewById(R.id.ed_pass);

        lAuth = FirebaseAuth.getInstance();
        lAuthlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (lAuth.getCurrentUser() != null) {
                    startActivity(new Intent(SignIn.this, Main.class));
                    finish();

                }
            }
        };

        signInbtn = (SignInButton) findViewById(R.id.btn_login);

        signInbtn.setOnClickListener(this);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();

    }


    private void signIn() {

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, RQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleresult(result);
        }
    }

    private void handleresult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            mProgress.setMessage("SignIn....");
            mProgress.show();
            GoogleSignInAccount account = result.getSignInAccount();
            firebaseAuthWithGoogle(account);

            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {

                }
            });


        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {

        if(isNetworkAvailable()) {
            switch (v.getId()) {
                case R.id.btn_login:
                    signIn();
                    break;
                case R.id.btn_newsign:
                    mSignIn();
                    break;
                case R.id.btn_signUp:
                    startActivity(new Intent(this, SignUp.class));
                    finish();
                    break;
            }
        }
        else
        {
            Toast.makeText(SignIn.this,"Please connect to Internet",Toast.LENGTH_SHORT).show();
        }

    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        lAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser lAuthCurrentUser=lAuth.getCurrentUser();
                                FirebaseDatabase.getInstance().getReference().child("users")
                                        .child(lAuthCurrentUser.getUid())
                                        .child("name").setValue(lAuthCurrentUser.getDisplayName());
                            mProgress.dismiss();
                            Log.d(TAG, "signInWithCredential:success");

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void mSignIn() {


        String name = edName.getText().toString();
        String pass = edPass.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pass)) {

            Toast.makeText(this, "Field are Empty", Toast.LENGTH_SHORT).show();

        } else {
            mProgress.setMessage("SignIn...");
            mProgress.show();

            lAuth.signInWithEmailAndPassword(name, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(SignIn.this, "SignIn problem", Toast.LENGTH_SHORT).show();
                        mProgress.dismiss();
                    }
                }
            });

        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
