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

import java.util.ArrayList;

public class GiveTest extends AppCompatActivity {

    private DatabaseReference gRef, uRef, Ref;
    private ProgressDialog gprogressDialog;
    private int i = 1, gtotal,k=1;
    TextView gqstn, gtestnm, tvno;
    RadioButton gans1, gans2, gans3, gans4;
    private FirebaseAuth gAuth;
    private int giveans[];
    private Button Submit;
    private ProgressBar gprogressBar;
    private RadioGroup rdgr;
    private ArrayList<AllQA> allQAS;
    private ArrayList<String> gvansarrayList;


    @Override
    protected void onStart() {
        super.onStart();

        gRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gprogressDialog.setMessage("Loading..");
                gprogressDialog.show();
                for (DataSnapshot gdataSnapshot : dataSnapshot.getChildren()) {

                    try {
                        AllQA allQA = gdataSnapshot.getValue(AllQA.class);
                        allQAS.add(allQA);
                    } catch (Exception e) {
                    }

                }
                gprogressDialog.dismiss();
                getqstnans(i);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_test);

        gAuth = FirebaseAuth.getInstance();
        gprogressBar = (ProgressBar) findViewById(R.id.progressBar2);
        gqstn = (TextView) findViewById(R.id.tv_gvqstn);
        tvno = (TextView) findViewById(R.id.tv_gqno);
        gans1 = (RadioButton) findViewById(R.id.rd_g1);
        gans2 = (RadioButton) findViewById(R.id.rd_g2);
        gans3 = (RadioButton) findViewById(R.id.rd_g3);
        gans4 = (RadioButton) findViewById(R.id.rd_g4);
        Submit = (Button) findViewById(R.id.btn_gvsubmit);
        Submit.setVisibility(View.INVISIBLE);
        rdgr = (RadioGroup) findViewById(R.id.tv_grdgr);
        gtestnm = (TextView) findViewById(R.id.tv_gtestname);
        allQAS = new ArrayList<AllQA>();
        gvansarrayList = new ArrayList<String>();

        String guid = getIntent().getExtras().getString("uid");
        String tnm = getIntent().getExtras().getString("name");
        gtotal = getIntent().getExtras().getInt("no");
        giveans=new int[gtotal];
        gtestnm.setText(tnm);

        Ref = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(guid);

        gRef = Ref
                .child("Tests")
                .child(tnm);
        gprogressDialog = new ProgressDialog(GiveTest.this);

        uRef = Ref.child("Results").child(tnm).child(gAuth.getCurrentUser().getUid());




    }

    public void onRadiobtnclick(View view) {

        switch (view.getId()) {
            case R.id.rd_g1:
                giveans[i-1]=1;
                break;
            case R.id.rd_g2:
                giveans[i-1]=2;
                break;
            case R.id.rd_g3:
                giveans[i-1]=3;
                break;
            case R.id.rd_g4:
                giveans[i-1]=4;
                break;

            default:
                giveans[i-1]=0;
        }
    }



    private boolean getgvans(int position) {
        if(giveans[position-1]!=0)
        {

        switch (giveans[position-1]) {
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
                rdgr.check(-1);

        }
            return true;
        }
        else {
            rdgr.clearCheck();
            return false;
        }

    }
    public boolean getqstnans(int position) {

            position=position-1;
            if(position<gtotal&&position>=0) {
                AllQA qa = allQAS.get(position);
                tvno.setText("Q no" + position);
                gqstn.setText(qa.getQuestion());
                gans1.setText(qa.getAn1());
                gans2.setText(qa.getAns2());
                gans3.setText(qa.getAns3());
                gans4.setText(qa.getAns4());
                return true;
            }
        else {
                return false;
            }

    }
    public void nextqstn(View view)
    {
            if (i<gtotal&&getqstnans(++i)) {
                setprgs();
                int ans = Integer.parseInt(allQAS.get(i - 1).getRgans());
                getgvans(i);
            }
    }
    public void previousqstn(View view)
    {
            if (i>1&&getqstnans(--i)) {
                setprgs();
                getgvans(i);
        }
      }

    public void setsubmit(View view)
    {
        setAns();
        startActivity(new Intent(GiveTest.this,Main.class));
        finish();
    }

    public void setAns()
    {
        DatabaseReference gvansRef= uRef.child("giveans");
        int count=0;
        for(int bi=0;bi<gtotal;bi++)
        {
            int ans=Integer.parseInt(allQAS.get(bi).getRgans());
            if(ans==giveans[bi])
                count++;
            gvansRef.child("question" + (bi+1)).setValue(""+giveans[bi]);
        }
        uRef.child("marks").setValue(""+count);
    }
    private void setprgs()
    {
        int prgrs=0;
        for(int k=0;k<gtotal;k++)
        {
            if(giveans[k]!=0)
                prgrs++;
        }
        gprogressBar.setProgress(prgrs*(100/gtotal));
        if(prgrs==gtotal)
            Submit.setVisibility(View.VISIBLE);
    }
}
