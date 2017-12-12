package com.example.kiit.techtest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TakeTest extends AppCompatActivity{

    private DatabaseReference tRef;
    private EditText tqstn, tans1, tans2, tans3, tans4;
    private FirebaseUser tUser;
    private FirebaseAuth tAuth;
    private int i=1,j=0,totalqstn=0,rightans=0;
    private ProgressBar tprogressBar;
    private TextView tvtpg,thead;
    CheckBox cb1,cb2,cb3,cb4;
    private String testname,testdes;
    private Button mSubmit;
    private ProgressDialog tprogressDialog;
    private AllQA[] all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_test);

        tqstn = (EditText) findViewById(R.id.ed_tqstn);
        tans1 = (EditText) findViewById(R.id.ed_tans1);
        tans2 = (EditText) findViewById(R.id.ed_tans2);
        tans3 = (EditText) findViewById(R.id.ed_tans3);
        tans4 = (EditText) findViewById(R.id.ed_tans4);
        thead=(TextView)findViewById(R.id.tv_thead);
        mSubmit=(Button)findViewById(R.id.btn_tksubmit);
        mSubmit.setVisibility(View.INVISIBLE);
        tprogressDialog=new ProgressDialog(TakeTest.this);
        cb1=(CheckBox)findViewById(R.id.tchk1);
        cb2=(CheckBox)findViewById(R.id.tchk2);
        cb3=(CheckBox)findViewById(R.id.tchk3);
        cb4=(CheckBox)findViewById(R.id.tchk4);




        Intent takeIntent=getIntent();
        totalqstn=takeIntent.getExtras().getInt("no");
        all=new AllQA[totalqstn];
        testname=takeIntent.getExtras().getString("name");
        testdes=takeIntent.getExtras().getString("des");



        tprogressBar=(ProgressBar)findViewById(R.id.tpg);
        tvtpg=(TextView)findViewById(R.id.tv_tpg);
        thead.setText(testname);
        tvtpg.setText("Q No"+i);
        tAuth = FirebaseAuth.getInstance();
        tUser=tAuth.getCurrentUser();
        tRef=FirebaseDatabase.getInstance().getReference().
                child("users").
                child(tUser.getUid()).child("Tests")
        .child(testname);
        QaList tklist=new QaList(testname,testdes,Integer.toString(totalqstn));
        tRef.setValue(tklist);





    }

    public void createDatabase(View view) {

        if(isNetworkAvailable())
        {

        if (i >= j) {


            if (updateDataBase(i)) {
                //tprogressDialog.dismiss();
                tprogressBar.setProgress(i * (100 / totalqstn));
                j = i;
                cleartext();
                clearfocus();
                i++;
                if (i == totalqstn + 1) {
                    startActivity(new Intent(TakeTest.this, QaActivity.class));
                    finish();
                } else {
                    tvtpg.setText("Q No." + i);
                    btnsetText();
                }
            }

        } else {
            if (updateDataBase(i)) {
                clearfocus();
                i++;
                tvtpg.setText("Q No." + i);
                btnsetText();
                getDataBase(i);

            }

        }
    }
    else
        {
            Toast.makeText(TakeTest.this,"Please Connect to Internet",Toast.LENGTH_SHORT).show();
        }


    }

    public void oncheckBoxClicked(View view)
    {
        boolean checked=((CheckBox)view).isChecked();

        switch (view.getId())
        {
            case R.id.tchk1:
                if(checked)
                {
                cb2.setChecked(false);
                cb3.setChecked(false);
                cb4.setChecked(false);
                rightans=1;
                }
                break;
            case R.id.tchk2:
                if(checked) {
                   cb1.setChecked(false);
                   cb3.setChecked(false);
                   cb4.setChecked(false);
                   rightans=2;
                }
                break;
            case R.id.tchk3:
                if (checked) {
                    cb1.setChecked(false);
                    cb2.setChecked(false);
                    cb4.setChecked(false);
                    rightans=3;
                }
                break;
            case R.id.tchk4:
                if(checked) {
                    cb1.setChecked(false);
                    cb2.setChecked(false);
                    cb3.setChecked(false);
                    rightans=4;
                }
                break;
        }
    }
    public void preDatabase(View view)
    {
        if(i>1&&isNetworkAvailable()) {
            if(i<=j)
            {
                updateDataBase(i);
            }
                clearfocus();
                i--;
                getDataBase(i);
                btnsetText();
                tvtpg.setText("Q No" + i);
        }
        else
        {
            if(!isNetworkAvailable())
            {
                Toast.makeText(TakeTest.this,"Please Connect to Internet",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void cleartext()
    {
        tqstn.getText().clear();
        tans1.getText().clear();
        tans2.getText().clear();
        tans3.getText().clear();
        tans4.getText().clear();
    }
    public void clearfocus()
    {
        tqstn.clearFocus();
        tans1.clearFocus();
        tans2.clearFocus();
        tans3.clearFocus();
        tans4.clearFocus();
    }
    public void btnsetText()
    {
        if(i==totalqstn) {
            mSubmit.setText("Submit");
        }
        else {
            mSubmit.setText("next");
        }
    }
    public void getDataBase(int datano)
    {

        tprogressDialog.setMessage("Getting  Data...");
        tprogressDialog.show();
        DatabaseReference qReference = tRef.child("question" + datano);

        qReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {

                    AllQA preQA = dataSnapshot.getValue(AllQA.class);
                    tqstn.setText(preQA.getQuestion());
                    tans1.setText(preQA.getAn1());
                    tans2.setText(preQA.getAns2());
                    tans3.setText(preQA.getAns3());
                    tans4.setText(preQA.getAns4());


                }catch (Exception e){}

                tprogressDialog.dismiss();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public boolean updateDataBase(int no)
    {
        tprogressDialog.setMessage("Uploading...");
        tprogressDialog.show();

        String tkqstn=tqstn.getText().toString();
        String tkans1=tans1.getText().toString();
        String tkans2=tans2.getText().toString();
        String tkans3=tans3.getText().toString();
        String tkans4=tans4.getText().toString();
        if(i<=totalqstn&&!TextUtils.isEmpty(tkqstn) && !TextUtils.isEmpty(tkans1) && !TextUtils.isEmpty(tkans2)
                && !TextUtils.isEmpty(tkans3) && !TextUtils.isEmpty(tkans4) && rightans != 0) {
            DatabaseReference qReference = tRef.child("question" + no);

            AllQA allQA = new AllQA(tkqstn, tkans1, tkans2, tkans3, tkans4, Integer.toString(rightans));
            qReference.setValue(allQA);
            tprogressDialog.dismiss();
            return true;
        }
        else {

            if (rightans == 0)
                Toast.makeText(TakeTest.this, "Please Check Right ans", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(TakeTest.this, "Fields are Empty", Toast.LENGTH_SHORT).show();

            tprogressDialog.dismiss();
            return false;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean addData(int pos)
    {
        pos--;
            String tkqstn = tqstn.getText().toString();
            String tkans1 = tans1.getText().toString();
            String tkans2 = tans2.getText().toString();
            String tkans3 = tans3.getText().toString();
            String tkans4 = tans4.getText().toString();
            if(pos<totalqstn&&pos>=0&&i<=totalqstn&&!TextUtils.isEmpty(tkqstn) && !TextUtils.isEmpty(tkans1) && !TextUtils.isEmpty(tkans2)
                    && !TextUtils.isEmpty(tkans3) && !TextUtils.isEmpty(tkans4) && rightans != 0)
            {
            AllQA allQA = new AllQA(tkqstn, tkans1, tkans2, tkans3, tkans4, Integer.toString(rightans));
            all[pos] = allQA;
            return true;
        }
        else {
                rightans=0;

                return false;
        }
    }
    public void Check(int no)
    {
        switch (no)
        {
            case 1:
                cb1.setChecked(true);
                cb2.setChecked(false);
                cb3.setChecked(false);
                cb4.setChecked(false);
                break;
            case 2:
                cb2.setChecked(true);
                cb1.setChecked(false);
                cb3.setChecked(false);
                cb4.setChecked(false);
                break;
            case 3:
                cb3.setChecked(true);
                cb1.setChecked(false);
                cb2.setChecked(false);
                cb4.setChecked(false);
                break;
            case 4:
                cb4.setChecked(true);
                cb1.setChecked(false);
                cb2.setChecked(false);
                cb3.setChecked(false);
                rightans=4;
                break;
            default:
                cb4.setChecked(false);
                cb1.setChecked(false);
                cb2.setChecked(false);
                cb3.setChecked(false);


        }
    }
    public boolean getData(int pos)
    {
        pos--;
        if(all[pos]!=null)
        {
            AllQA qa=all[pos];
            clearfocus();
            tqstn.setText(qa.getQuestion());
            tans1.setText(qa.getAn1());
            tans2.setText(qa.getAns2());
            tans3.setText(qa.getAns3());
            tans4.setText(qa.getAns4());
            int rgans=Integer.parseInt(qa.getRgans());
            rightans=rgans;
            Check(rgans);
            return true;
        }
        else
        {
            rightans=0;
            cleartext();
            Check(0);
            return false;
        }
    }
    public void OnNext(View view)
    {
        if(addData(i)) {
            if (i < totalqstn && addData(i)) {
                getData(++i);
                tvtpg.setText("Q No" + i);
            }
        }
        else
            Toast.makeText(TakeTest.this, "Fields are Empty", Toast.LENGTH_SHORT).show();
        setprgs();

    }
    public void OnPre(View view)
    {
        if (i>1) {
            addData(i);
            getData(--i);
            tvtpg.setText("Q No"+i);
        }
    }
    private void setprgs()
    {
        int prgrs=0;
        for(int k=0;k<totalqstn;k++)
        {
            if(all[k]!=null)
                prgrs++;
        }
        tprogressBar.setProgress(prgrs*(100/totalqstn));
        if(prgrs==totalqstn)
            mSubmit.setVisibility(View.VISIBLE);
    }
    public void testSubmit(View view)
    {
        tprogressDialog.setMessage("Uploading....");
        tprogressDialog.show();
        if(UploadData())
        {
            tprogressDialog.dismiss();
            startActivity(new Intent(TakeTest.this,QaActivity.class));
            finish();

        }

    }
    public boolean UploadData()
    {
        for(int l=0;l<totalqstn;l++)
        {
            DatabaseReference qReference = tRef.child("question" + (l+1));
            qReference.setValue(all[l]);
        }
        return true;
    }
}
