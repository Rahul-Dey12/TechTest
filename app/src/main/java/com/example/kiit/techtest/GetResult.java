package com.example.kiit.techtest;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GetResult extends AppCompatActivity {

    private RecyclerView mrecyclerView;
    private RecyclerView.Adapter madapter;
    private ArrayList<Results> marrayList;
    private DatabaseReference mreference;
    private ProgressDialog mprogressDialog;
    private FirebaseAuth mauth;

    @Override
    protected void onStart() {
        super.onStart();
        mreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                for(DataSnapshot mdataSnapshot : dataSnapshot.getChildren())
                {


                    Results mresults=mdataSnapshot.getValue(Results.class);
                    marrayList.add(mresults);

                }
                madapter.notifyDataSetChanged();
                mprogressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_result);

        String testname=getIntent().getExtras().getString("testname");
        mauth=FirebaseAuth.getInstance();
        mreference= FirebaseDatabase.getInstance().getReference().child("users").
        child(mauth.getCurrentUser().getUid())
        .child("Results").child(testname);

        mprogressDialog=new ProgressDialog(GetResult.this);
        mprogressDialog.setMessage("Getting Results...");
        mprogressDialog.show();
        marrayList=new ArrayList<Results>();
        mrecyclerView=(RecyclerView)findViewById(R.id.recycler_viewresult);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(GetResult.this));

        madapter=new ResultAdapter(GetResult.this,marrayList);
        mrecyclerView.setAdapter(madapter);

    }

}
