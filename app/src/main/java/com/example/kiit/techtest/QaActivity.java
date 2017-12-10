package com.example.kiit.techtest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class QaActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter ;
    private List<QaList> list;
    private Button qabtn;
    private DatabaseReference qaRef;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa);

        mAuth = FirebaseAuth.getInstance();
        qaRef= FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("Tests");
        qabtn=(Button)findViewById(R.id.btn_qa);
        progressDialog=new ProgressDialog(QaActivity.this);
        progressDialog.setMessage("Getting Information.....");
        progressDialog.show();
        list=new ArrayList<QaList>();

        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(QaActivity.this));

        adapter=new QaAdapter(QaActivity.this,list);

        recyclerView.setAdapter(adapter);
        preparedata();
    }

    private void preparedata() {

        qaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot qslistsnapshot : dataSnapshot.getChildren())
                {
                    try {
                        QaList mList = qslistsnapshot.getValue(QaList.class);
                        list.add(mList);

                    }catch (Exception e){}
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void qaCreate(View view)
    {
        AlertDialog.Builder mBuilder=new AlertDialog.Builder(QaActivity.this);
        View PopView=getLayoutInflater().inflate(R.layout.popup,null);
                final EditText ed_textname=(EditText)PopView.findViewById(R.id.ed_poptnm);
                final EditText ed_textdes=(EditText)PopView.findViewById(R.id.ed_poptdes);
                final EditText ed_qstnno=(EditText)PopView.findViewById(R.id.ed_popqno);
                Button tcreate=(Button)PopView.findViewById(R.id.btn_pop);

                tcreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String qno=ed_qstnno.getText().toString();
                        String textname=ed_textname.getText().toString();
                        String description=ed_textdes.getText().toString();
                        if(TextUtils.isEmpty(qno)||TextUtils.isEmpty(textname)||TextUtils.isEmpty(description))
                        {
                            Toast.makeText(QaActivity.this,"Please fill the details",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                        Intent createintent=new Intent(QaActivity.this,TakeTest.class);
                        createintent.putExtra("name",textname);
                        createintent.putExtra("des",description);
                        createintent.putExtra("no",Integer.parseInt(qno));

                        startActivity(createintent);
                        finish();
                        }

                    }
                });
        mBuilder.setView(PopView);
        AlertDialog dialog=mBuilder.create();
        dialog.show();
    }

}
