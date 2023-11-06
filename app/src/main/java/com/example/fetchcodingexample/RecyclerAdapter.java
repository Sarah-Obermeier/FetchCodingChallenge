package com.example.fetchcodingexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    //String info[];
    List<Item> itemList;
    MainActivity context;
    public RecyclerAdapter(MainActivity ct, List<Item> itemList){
        context = ct;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        int id = itemList.get(position).getId();
        holder.textView1.setText("ID: " + id);
        int listID = itemList.get(position).getListId();
        holder.textView2.setText("List ID: " + listID);
        String itemName = itemList.get(position).getItemName();
        holder.textView3.setText(itemName);
        //holder.textView1.setText(info[position]);
        //holder.textView1.setText(item.getId());
        //holder.textView2.setText(item.getListId());
        //holder.textView3.setText(item.getItemName());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView1;
        TextView textView2;
        TextView textView3;
        public MyViewHolder(final View itemView){
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
        }
    }
}
