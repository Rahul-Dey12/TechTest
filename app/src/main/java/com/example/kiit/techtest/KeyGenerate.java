package com.example.kiit.techtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class KeyGenerate extends AppCompatActivity {

    private TextView tvname,tvKey;
    DatabaseReference mdatabaseReference;
    private int key;
    private FirebaseAuth mAuth;
    private String testname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_generate);

        mAuth = FirebaseAuth.getInstance();
        mdatabaseReference= FirebaseDatabase.getInstance().getReference().child("currenttests");

        tvname=(TextView)findViewById(R.id.keygen_tname);
        tvKey=(TextView)findViewById(R.id.tv_key);

        testname=getIntent().getExtras().getString("TestName");
        tvname.setText(testname);

        Random random=new Random();
        key=random.nextInt(100000);
        tvKey.setText(""+key);



    }

    public void generateTest(View view)
    {
        DatabaseReference testRef=mdatabaseReference.child(""+key);
        FirebaseUser firebaseUser=mAuth.getCurrentUser();
        String mtchruid=firebaseUser.getUid();
        String mtchrname=firebaseUser.getDisplayName();

        TestInfo testInfo=new TestInfo(testname,mtchruid,mtchrname);
        testRef.setValue(testInfo);
    }
}
