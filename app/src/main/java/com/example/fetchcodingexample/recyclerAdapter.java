package com.example.fetchcodingexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {

    //String id[], listID[], name[];
    String info[];
    Context context;
    //public recyclerAdapter(Context ct, String s1[], String s2[], String s3[]){
    public recyclerAdapter(Context ct, String s1[]){
        context = ct;
        info = s1;
        //listID = s2;
        //name = s3;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.MyViewHolder holder, int position) {
        holder.textView1.setText(info[position]);
    }

    @Override
    public int getItemCount() {
        return info.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView1;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView1);
        }
    }
}
