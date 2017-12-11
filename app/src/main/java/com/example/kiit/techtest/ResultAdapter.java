package com.example.kiit.techtest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by KIIT on 11-12-2017.
 */

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.viewholder> {

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Results> resultsArrayList;

    public ResultAdapter(Context context, ArrayList<Results> resultsArrayList) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.resultsArrayList = resultsArrayList;
    }

    @Override
    public viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.resultlist,parent,false);
        viewholder viewholder=new viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(ResultAdapter.viewholder holder, int position) {


        Results arrayList=resultsArrayList.get(position);

        holder.tvsnm.setText("Name : "+arrayList.getName());
        holder.tvsmarks.setText("Marks : "+arrayList.getMarks());
    }

    @Override
    public int getItemCount() {
        return resultsArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        TextView tvsnm,tvsmarks;

        public viewholder(View itemView) {
            super(itemView);

            tvsnm=(TextView)itemView.findViewById(R.id.tv_stdnname);
            tvsmarks=(TextView)itemView.findViewById(R.id.tv_stdnmarks);
        }
    }
}
