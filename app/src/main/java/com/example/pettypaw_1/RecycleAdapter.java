package com.example.pettypaw_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {

    private ArrayList<Recycler_item> Data = null;

    //아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView1, dogName;
        CheckBox feed_checked, walk_checked;

        ViewHolder(View itemView){
            super(itemView);

            //뷰 객체에 대한 참조
            textView1 = itemView.findViewById(R.id.textView);
            feed_checked = itemView.findViewById(R.id.feed_checked);
            walk_checked = itemView.findViewById(R.id.walk_checked);
            dogName = itemView.findViewById(R.id.dogName);

            //int pos = getAdapterPosition(); //어댑터 내 아이템의 위치
            //if(pos != RecyclerView.NO_POSITION);
        }
    }

    RecycleAdapter(ArrayList<Recycler_item> list){

        Data = list;
    }


    //뷰홀더가 만들어지는 시점에 호출되는 메소드(각 아이템을 위한 뷰홀더 객체가 처음 만들어질때)
    //각각 아이템 뷰홀더가 재사용될 수 있는 상태라면 호출되지 않는다
    @Override
    public RecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.recyclerview_item, parent, false) ;
        RecycleAdapter.ViewHolder vh = new RecycleAdapter.ViewHolder(view) ;

        return vh;
    }


    //아이템 뷰의 xml레이아웃과 결합되는 경우 자동 호출
    //position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    //아이템뷰에 데이터를 표시하는 작업
    //어떤 데이터를 아이템뷰에 표시할 것인지는 position값 참조
    @Override
    public void onBindViewHolder(RecycleAdapter.ViewHolder holder, int position){
        Recycler_item item = Data.get(position);
        holder.dogName.setText(item.getPetName());
        holder.textView1.setText(item.getDetail());
    }

    //어댑터에서 관리하는 아이템의 개수를 반환
    @Override
    public int getItemCount(){
        return Data.size();
    }


}

