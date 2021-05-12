package com.example.pettypaw_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recyclerview_Adapter extends RecyclerView.Adapter<recyclerview_Adapter.ViewHolder> {

    private ArrayList<String> mData = null;
    public class ViewHolder extends  RecyclerView.ViewHolder {

        TextView textView;
        CheckBox feed_checked;
        CheckBox walk_checked;
        TextView dogName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            feed_checked = itemView.findViewById(R.id.feed_checked);
            walk_checked = itemView.findViewById(R.id.walk_checked);
            dogName = itemView.findViewById(R.id.dogName);
        }
    }

    recyclerview_Adapter(ArrayList<String> list){
        mData = list;
    }

    @NonNull
    @Override
    public recyclerview_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recyclerview_item,parent,false);
        recyclerview_Adapter.ViewHolder vh = new recyclerview_Adapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerview_Adapter.ViewHolder holder, int position) {
        String text = mData.get(position);
        holder.textView.setText(text);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


}
