package com.example.kiit.techtest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GvTwelcome extends AppCompatActivity {

    private TextView gwelnm,gweldes,gwelno;
    DatabaseReference databaseReference;
    String name,des,uid;
    int no;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gv_twelcome);

        gweldes=(TextView)findViewById(R.id.tv_gweldes);
        gwelnm=(TextView)findViewById(R.id.tv_gwelnam);
        gwelno=(TextView)findViewById(R.id.gwel_n0);

        progressDialog=new ProgressDialog(GvTwelcome.this);
        progressDialog.setMessage("Getting Info...");
        progressDialog.show();

        uid=getIntent().getExtras().getString("ouid");
        String tname=getIntent().getExtras().getString("otname");
        databaseReference= FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("Tests")
        .child(tname);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    QaList qaList = dataSnapshot.getValue(QaList.class);
                    name = qaList.getTestname();
                    des = qaList.getTestdes();
                    no = Integer.parseInt(qaList.getTotalqstn());
                    gweldes.setText(des);
                    gwelnm.setText(name);
                    gwelno.setText("" + no);

                }catch (Exception e){}
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    public void onNext(View view)
    {
        Intent intent=new Intent(GvTwelcome.this,GiveTest.class);

        intent.putExtra("uid",uid);
        intent.putExtra("no",no);
        intent.putExtra("name",name);

        startActivity(intent);
        finish();

    }
}
