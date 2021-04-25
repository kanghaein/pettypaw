package com.example.pettypaw_1;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestAdapter extends BaseAdapter{

    // 파이어베이스 연동
    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    final FirebaseDatabase mDatabase1 = FirebaseDatabase.getInstance();
    // User_pet.java 를 통해 데이터베이스 접근
    final DatabaseReference GroupDB = mDatabase.getReference("Group");
    final DatabaseReference UserDB = mDatabase1.getReference("User");

    String getUserID = ((MainActivity)MainActivity.context_main).lg_ID.getText().toString();
    String getUserName = ((RequestInvitation)RequestInvitation.context_RequestInvitation).UserName;

    // 아이템을 세트로 담기 위한 Array
    private ArrayList<Request_item> mItems = new ArrayList<>();

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Request_item getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.request_list_item, parent, false);
        }

        // 리스트 안의 textview 가져오기, text 내용은 초대한 사람의 이름(RequestInvitation 에서)
        TextView list_name = (TextView) convertView.findViewById(R.id.textview) ;
        Request_item myItem = getItem(position);
        list_name.setText(myItem.getText());

        // 리스트 안의 수락버튼
        Button accept_button = (Button) convertView.findViewById(R.id.accept_button);
        accept_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupDB.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Group DB 에 리더와 멤버 저장. 멤버는 리더의 자식노드
                        GroupDB.child(getUserName).child(getUserID).setValue(getUserID);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                context.startActivity(new Intent(context, ViewCalendar.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        return convertView;
    }


    // 아이템 데이터 추가를 위한 함수
    public void addItem(String name) {

        Request_item mItem = new Request_item();
        mItem.setText(name);
        mItems.add(mItem);

    }
}