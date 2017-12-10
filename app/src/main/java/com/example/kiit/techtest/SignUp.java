package com.example.kiit.techtest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText sgname,sgpass,sgemail;
    private String uname,upass,uemail;
    private ImageButton imageButton;
    private static final int GALLERY_RQ=1;
    private Uri imageuri;
    private ProgressDialog progressDialog;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sgname=(EditText)findViewById(R.id.ed_name);
        sgemail=(EditText)findViewById(R.id.ed_email);
        sgpass=(EditText)findViewById(R.id.ed_pass);
        imageButton=(ImageButton)findViewById(R.id.imgbtn_propic);
        progressDialog=new ProgressDialog(SignUp.this);

        mRef = FirebaseDatabase.getInstance().getReference().child("users");

        mAuth=FirebaseAuth.getInstance();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_RQ);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageuri=data.getData();
        //imageButton.setImageURI(imageuri);
        Glide.with(this).load(imageuri).centerCrop().into(imageButton);
    }

    public void SignUpuser(View view)
    {   if(isNetworkAvailable()) {
        progressDialog.setMessage("SigningUp....");
        progressDialog.show();
        uname = sgname.getText().toString();
        upass = sgpass.getText().toString();
        uemail = sgemail.getText().toString();
        if(TextUtils.isEmpty(uname)||TextUtils.isEmpty(upass)||TextUtils.isEmpty(uemail))
        {
            progressDialog.dismiss();
            Toast.makeText(SignUp.this,"Please fill al the details",Toast.LENGTH_SHORT).show();
        }
        else
        {
            mAuth.createUserWithEmailAndPassword(uemail, upass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignUp.this, "SIgnUp Problem", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                            } else {

                                updateprofile();


                            }

                        }
                    });
        }
    }
    else {
        Toast.makeText(SignUp.this,"Can't connect to Internet",Toast.LENGTH_LONG).show();
    }
    }
    private void updateprofile(){

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null) {

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(uname)
                    .setPhotoUri(imageuri)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                signinuser();

                            } else {
                                Toast.makeText(SignUp.this, "SignUpProblem", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        }

    }
    private void signinuser(){
        mAuth.signInWithEmailAndPassword(uemail,upass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {

                            Toast.makeText(SignUp.this,"failed",
                                    Toast.LENGTH_SHORT).show();

                        }
                        else {
                            FirebaseUser mUser=mAuth.getCurrentUser();
                            FirebaseDatabase.getInstance().getReference().child("users")
                                    .child(mUser.getUid())
                                    .child("name").setValue(mUser.getDisplayName());
                            progressDialog.dismiss();
                            startActivity(new Intent(SignUp.this,Main.class));
                            finish();
                        }

                    }
                });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
