package com.example.kiit.techtest;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Main extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference gdatabaseReference;

    TextView tvname,tvemail;
    ImageView imageView;
    private Button msgnout;
    private boolean keyres,ispress=false;
    String mUid,mtchrname,mtestname;

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        msgnout=(Button)findViewById(R.id.btn_sgnout);
        tvname=(TextView)findViewById(R.id.tv_name);
        tvemail=(TextView)findViewById(R.id.tv_email);
        imageView=(ImageView) findViewById(R.id.imgv_pic);
        msgnout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getInstance().signOut();

            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    display(user);

                } else {
                    // User is signed out
                    startActivity(new Intent(Main.this,SignIn.class));
                    finish();

                }
                // ...
            }
        };
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    private void display(FirebaseUser user)
    {
        tvname.setText(user.getDisplayName());
        tvemail.setText("email: "+user.getEmail());
        Glide.with(Main.this).load(user.getPhotoUrl()).centerCrop().into(imageView);
    }
    public void takeTest(View view){
        startActivity(new Intent(Main.this,QaActivity.class));
        finish();
    }

    public void giveTest(View view)
    {
        gdatabaseReference=FirebaseDatabase.getInstance().getReference().child("currenttests");

        AlertDialog.Builder gBuilder=new AlertDialog.Builder(Main.this);
        View gview=getLayoutInflater().inflate(R.layout.givetestpopup,null);
        gBuilder.setView(gview);
        AlertDialog galertDialog=gBuilder.create();
        galertDialog.show();

        final TextView tchrnm=(TextView)gview.findViewById(R.id.tv_tchrname);
        final EditText edkey=(EditText)gview.findViewById(R.id.ed_popkey);
        Button gbutton=(Button)gview.findViewById(R.id.btn_popgvtset);
        Button gCheck=(Button)gview.findViewById(R.id.btn_gvcheck);

        edkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ispress=false;
            }
        });

        gCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key=edkey.getText().toString();
                if(TextUtils.isEmpty(key)&&!isNetworkAvailable())
                {
                    if(!isNetworkAvailable())
                    {
                        Toast.makeText(Main.this,"Please Connect to Internet",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(Main.this, "Please Enter Key", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if (isChildExist(key))
                    {

                        DatabaseReference checkreference = gdatabaseReference.child(key);
                        checkreference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            TestInfo mtestInfo=dataSnapshot.getValue(TestInfo.class);
                            mtchrname=mtestInfo.getTchrname();
                            tchrnm.setText(mtchrname);
                            mUid=mtestInfo.getTchruid();
                            mtestname=mtestInfo.getTestname();
                            ispress=true;

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                }
            }
        });

        gbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String key=edkey.getText().toString();
                if(TextUtils.isEmpty(key)&&isNetworkAvailable())
                {
                    if(!isNetworkAvailable())
                    {
                        Toast.makeText(Main.this,"Please Connect to Internet",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(Main.this, "Please Enter Key", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if(ispress)
                    {
                        Intent gintent=new Intent(Main.this,GvTwelcome.class);
                        gintent.putExtra("ouid",mUid);
                        gintent.putExtra("otname",mtestname);
                        startActivity(gintent);
                        finish();

                    }
                    else
                    {
                        Toast.makeText(Main.this,"Please press Check",Toast.LENGTH_SHORT).show();
                    }
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

    public boolean isChildExist(final String k)
    {
        gdatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(k)){
                    keyres=true;
                }
                else {
                    Toast.makeText(Main.this,"key not found",Toast.LENGTH_SHORT).show();
                    keyres=false;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return keyres;
    }
}
