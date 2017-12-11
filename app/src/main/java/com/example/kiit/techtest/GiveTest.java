package com.example.kiit.techtest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GiveTest extends AppCompatActivity {

    private DatabaseReference gRef, uRef, Ref;
    private ProgressDialog gprogressDialog;
    private int grgans=0, rightans, i = 1, gtotal,k=0;
    TextView gqstn, gtestnm,tvno;
    RadioButton gans1, gans2, gans3, gans4;
    private FirebaseAuth gAuth;
    private int ans[];
    private Button Submit;
    private ProgressBar gprogressBar;
    private RadioGroup rdgr;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_test);

        gAuth = FirebaseAuth.getInstance();
        gprogressBar=(ProgressBar)findViewById(R.id.progressBar2);
        gqstn = (TextView) findViewById(R.id.tv_gvqstn);
        tvno=(TextView)findViewById(R.id.tv_gqno);
        gans1 = (RadioButton) findViewById(R.id.rd_g1);
        gans2 = (RadioButton) findViewById(R.id.rd_g2);
        gans3 = (RadioButton) findViewById(R.id.rd_g3);
        gans4 = (RadioButton) findViewById(R.id.rd_g4);
        Submit=(Button)findViewById(R.id.btn_gnext);
        rdgr=(RadioGroup)findViewById(R.id.tv_grdgr);
        btnsetText(i);
        tvno.setText("Q no"+i);
        gtestnm = (TextView) findViewById(R.id.tv_gtestname);

        String guid = getIntent().getExtras().getString("uid");
        String tnm = getIntent().getExtras().getString("name");
        gtotal = getIntent().getExtras().getInt("no");
        ans = new int[gtotal];
        gtestnm.setText(tnm);

        Ref = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(guid);

        gRef = Ref
                .child("Tests")
                .child(tnm);
        gprogressDialog = new ProgressDialog(GiveTest.this);

        uRef = Ref.child("Results").child(tnm).child(gAuth.getCurrentUser().getUid());

        getDatabase(i);

    }

    public void getDatabase(int no) {
        gprogressDialog.setMessage("Getting  Data...");
        gprogressDialog.show();

        DatabaseReference greference = gRef.child("question" + no);

        greference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    AllQA gallQA = dataSnapshot.getValue(AllQA.class);
                    gqstn.setText(gallQA.getQuestion());
                    gans1.setText(gallQA.getAn1());
                    gans2.setText(gallQA.getAns2());
                    gans3.setText(gallQA.getAns3());
                    gans4.setText(gallQA.getAns4());
                    rightans = Integer.parseInt(gallQA.getRgans());
                } catch (Exception e) {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        gprogressDialog.dismiss();

    }

    public void gNext(View view) {

        if (i<=gtotal) {

            checkAns(i);
            if(i>=k) {
                gprogressBar.setProgress(i * (100 / gtotal));
                k=i;
            }
            else if(k+1>=i)
            {
                getrdrg(i);
            }
            i++;
            if(i==gtotal+1)
            {
                int count=0;
                for(int j=0;j<=gtotal-1;j++)
                {
                    if(ans[j]==1)
                    {
                        count++;
                    }
                }
                Results results=new Results(gAuth.getCurrentUser().getDisplayName(),Integer.toString(count));
                uRef.setValue(results);
                startActivity(new Intent(GiveTest.this,Main.class));
                finish();
            }
            else {
                tvno.setText("Q no"+i);
                btnsetText(i);
                getDatabase(i);

            }
        }


    }

    public void gPrevious(View view) {

        if (i > 1) {

            checkAns(i);

            i--;
            if(k>=i)
            {
                getrdrg(i);
            }


            tvno.setText("Q no"+i);
            btnsetText(i);
            getDatabase(i);


        }
    }

   private void checkAns(int no) {
        uRef.child("question" + no).setValue(""+grgans);
        if (grgans == rightans) {
            ans[no-1] = 1;
        } else {
            ans[no-1] = 0;
        }
    }

    public void onRadiobtnclick(View view) {

        switch (view.getId()) {
            case R.id.rd_g1:

                grgans = 1;
                break;
            case R.id.rd_g2:

                grgans = 2;
                break;
            case R.id.rd_g3:

                grgans = 3;

                break;
            case R.id.rd_g4:

                grgans = 4;
                break;

            default:
                    grgans=0;
        }
    }

    public void btnsetText(int no)
    {
        if(no==gtotal) {
            Submit.setText("Submit");
        }
        else {
            Submit.setText("next");
        }
    }

    public void getrdrg(int no)
    {
        gprogressDialog.setMessage("Wait a Min...");
        gprogressDialog.show();
        DatabaseReference getRef=uRef.child("question"+no);

        getRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    try {

                        int rgno = Integer.parseInt(dataSnapshot.getValue().toString());

                        switch (rgno) {
                            case 1:
                                rdgr.check(R.id.rd_g1);
                                break;
                            case 2:
                                rdgr.check(R.id.rd_g2);
                                break;
                            case 3:
                                rdgr.check(R.id.rd_g3);
                                break;
                            case 4:
                                rdgr.check(R.id.rd_g4);
                                break;
                            default:
                               // Toast.makeText(GiveTest.this,"xero",Toast.LENGTH_SHORT).show();
                                rdgr.check(-1);
                        }
                    } catch (Exception e) {
                    }
                gprogressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
