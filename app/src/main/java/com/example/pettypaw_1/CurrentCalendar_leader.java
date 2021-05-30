package com.example.pettypaw_1;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

// 그룹탈퇴에 대한 그룹장과 그룹멤버의 권한을 다르게 주기 위한 그룹장 전용 CurrentCalendar.java
public class CurrentCalendar_leader extends AppCompatActivity {

    Button btn_back; // 뒤로가기 버튼
    String name; // 반려동물 이름 저장할 변수
    String member; // 그룹멤버 이름 저장할 변수
    String LeaderID; // 그룹장 ID 저장할 변수

    // (**의 캘린더) 텍스트 선언
    private EditText T_LeaderID;

    // 리스트뷰와 리스트를 담기위한 배열을 객체 선언
    private ListView enroll_pet_list;
    private ListView sharing_member_list;

    // 리스트의 어댑터 선언
    private ArrayAdapter<String> adapter_p;
    private ArrayAdapter<String> adapter_m;

    // 등록된 반려동물, 함께 이용중인 사람에 정보를 담기 위한 리스트 선언
    List<Object> Array_p = new ArrayList<Object>();
    List<Object> Array_m = new ArrayList<Object>();

    // MainActivity 에서 로그인 할 때 입력한 ID 값, 즉 자신의 ID
    String getUserID = ((MainActivity) MainActivity.context_main).lg_ID.getText().toString();

    // 파이어베이스 연동
    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    // User_pet.java 를 통해 데이터베이스 접근
    final DatabaseReference petDB = mDatabase.getReference("User_pet");
    // User.java 를 통해 데이터베이스 접근
    final DatabaseReference UserDB = mDatabase.getReference("User");
    // Group.java 를 통해 데이터베이스 접근
    final DatabaseReference GroupDB = mDatabase.getReference("Group");

    public static Context context_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_calendar_leader);

        context_view = this;

        // 그룹장의 ID를 표시하기 위한 변수 (**의 캘린더)
        T_LeaderID = findViewById(R.id.textLeaderID);

        // DB 에서 그룹장의 ID를 가져와 (**의 캘린더)에 세트
        UserDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LeaderID = snapshot.child("User List").child(getUserID).child("Leader_ID").getValue().toString();
                T_LeaderID.setText(LeaderID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // 등록된 반려동물 어댑터 설정
        enroll_pet_list = (ListView) findViewById(R.id.enroll_pet_list);
        enroll_pet_list.setAdapter(adapter_p);
        adapter_p = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        enroll_pet_list.setAdapter(adapter_p);

        // (User -> User List -> 로그인한 ID -> Leader_ID) 의 값은 같은 그룹에 속한 사람들이라면
        // 모두 리더의 ID로 통일되어 같으므로 이것을 그룹내 공유를 위한 키값으로 사용.
        // 그 키값이 아래의 LeaderID 라는 변수
        UserDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LeaderID = snapshot.child("User List").child(getUserID).child("Leader_ID").getValue().toString();
                petDB.child(LeaderID).child("Pet List").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // 리스트에 등록된 반려동물을 출력. 자식이 있는 수만큼 반복
                        for (DataSnapshot ds : snapshot.getChildren()) {

                            name = ds.getValue().toString();
                            Array_p.add(name);
                            adapter_p.add(name);
                        }
                        adapter_p.notifyDataSetChanged();
                        enroll_pet_list.setSelection(adapter_p.getCount() - 1);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //함께 이용 중인 사람 어댑터 설정
        sharing_member_list = findViewById(R.id.sharing_member_list);
        sharing_member_list.setAdapter(adapter_m);
        adapter_m = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        sharing_member_list.setAdapter(adapter_m);

        //(User -> User List -> 로그인한 ID -> Leader_ID) 의 값은 같은 그룹에 속한 사람
        // Leader_ID 를 받아 해당 아이디를 가진 사용자를 list 에 띄움
        UserDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LeaderID = snapshot.child("User List").child(getUserID).child("Leader_ID").getValue().toString();
                GroupDB.child(LeaderID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // DB의 (Group -> 그룹장 ID)에 존재하는 리스트 출력 (그룹멤버 출력)
                        for (DataSnapshot ds : snapshot.getChildren()) {

                            member = ds.getValue().toString();
                            Array_m.add(member);
                            adapter_m.add(member);
                        }
                        adapter_m.notifyDataSetChanged();
                        sharing_member_list.setSelection(adapter_m.getCount() - 1);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // 뒤로 가기 버튼
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}