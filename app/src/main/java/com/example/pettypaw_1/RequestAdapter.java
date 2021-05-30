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

// 초대 요청 리스트뷰에 표시될 아이템 뷰를 생성하는 어댑터
public class RequestAdapter extends BaseAdapter {

    // 파이어베이스 연동
    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    final FirebaseDatabase mDatabase1 = FirebaseDatabase.getInstance();
    // Group.java 를 통해 데이터베이스 접근
    final DatabaseReference GroupDB = mDatabase.getReference("Group");
    // User.java를 통해 데이터베이스 접근
    final DatabaseReference UserDB = mDatabase1.getReference("User");

    // MainActivity에서 ID 문자열로 사용하는 lg_ID 가져온다.
    String getUserID = ((MainActivity) MainActivity.context_main).lg_ID.getText().toString();

    // RequestInvitation에서 사용자 이름 문자열로 사용하는 UserName 가져온다.
    String getLeaderID = ((RequestInvitation) RequestInvitation.context_RequestInvitation).UserName;

    // 아이템을 세트로 담기 위한 Array
    private ArrayList<Request_item> mItems = new ArrayList<>();

    // 어댑터에서 관리하는 전체 아이템의 개수를 반환
    @Override
    public int getCount() {
        return mItems.size();
    }

    // position에 해당하는 데이터를 반환
    @Override
    public Request_item getItem(int position) {
        return mItems.get(position);
    }

    // position에 해당하는 데이터의 id값을 가져온다.
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // position값을 참조하여 position에 해당하는 데이터를 뷰에 표시한다.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext();

        // convertview가 null인 경우에는 레이아웃을 inflate
        // convertview가 null이 아닌 경우에는 기존의 뷰를 재사용한다.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.request_list_item, parent, false);
        }

        // 리스트 안의 textview 가져오기, text 내용은 초대한 사람의 이름(RequestInvitation 에서)
        TextView list_name = (TextView) convertView.findViewById(R.id.textview);
        Request_item myItem = getItem(position);
        list_name.setText(myItem.getText());

        // 리스트 안의 수락버튼 클릭 이벤트 처리
        Button accept_button = (Button) convertView.findViewById(R.id.accept_button);
        accept_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupDB.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Group DB 에 리더와 멤버 저장. 멤버는 리더의 자식노드
                        GroupDB.child(getLeaderID).child(getUserID).setValue(getUserID);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                // 수락버튼을 누르면 Member 가 된다.
                UserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User_list user_list = new User_list("Member", getLeaderID);
                        UserDB.child("User List").child(getUserID).setValue(user_list);

                        // 수락 완료 후 ViewCalendar 창으로 이동
                        context.startActivity(new Intent(context, ViewCalendar.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        // 리스트 안의 거절버튼 클릭 이벤트 처리
        Button deny_button = (Button) convertView.findViewById(R.id.deny_button);
        deny_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        // (User List -> 자신의 노드) 에 저장된 값들을 모두 null 로 변경
                        User_list user_list = new User_list("null", "null");
                        UserDB.child("User List").child(getUserID).setValue(user_list);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                // 거절 완료 후 welcome 창으로 이동
                context.startActivity(new Intent(context, welcome.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
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