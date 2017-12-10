package com.example.kiit.techtest;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.RandomAccess;

/**
 * Created by KIIT on 07-12-2017.
 */

public class QaAdapter extends RecyclerView.Adapter<QaAdapter.MyviewHolder> {

    private LayoutInflater inflater;
    private List<QaList> qaLists;
    private Context context;

    public QaAdapter(Context context,List<QaList> qaLists){

       inflater =LayoutInflater.from(context);
       this.qaLists=qaLists;
       this.context=context;
    }
    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=inflater.inflate(R.layout.qalayout,parent,false);
        MyviewHolder holder=new MyviewHolder(view,context,qaLists);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyviewHolder holder, final int position) {

        final QaList qaList = qaLists.get(position);
        holder.tv_tname.setText(qaList.getTestname());
        holder.tv_des.setText(qaList.getTestdes());
        holder.tv_totalno.setText("Total Question->"+qaList.getTotalqstn());
    }

    @Override
    public int getItemCount() {
        return qaLists.size();
    }
    public class MyviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tv_tname,tv_des,tv_totalno;
        List<QaList> tlists=new ArrayList<QaList>();
        Context ctx;


        public MyviewHolder(View itemView,Context ctx,List<QaList> tlists) {
            super(itemView);
            this.tlists=tlists;
            this.ctx=ctx;
            itemView.setOnClickListener(this);
            tv_tname=(TextView)itemView.findViewById(R.id.tv_qstnadp);
            tv_des=(TextView)itemView.findViewById(R.id.tv_descript);
            tv_totalno=(TextView)itemView.findViewById(R.id.tv_adpqstnno);
        }

        @Override
        public void onClick(View view) {
            int pos=getAdapterPosition();
            QaList polist=this.tlists.get(pos);
            Intent intent=new Intent(ctx,KeyGenerate.class);
            intent.putExtra("TestName",polist.getTestname());
            this.ctx.startActivity(intent);
        }
    }


}
