package com.example.pettypaw_1;

import android.content.Context;
import android.content.Intent;
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




public class CurrentCalendar extends AppCompatActivity {

    Button btn_back;
    String name;
    String member;
    String LeaderID;

    //edittext
    private EditText T_LeaderID;

    public static Context context_view;

    // 리스트뷰와 리스트를 담기위한 배열을 객체 선언
    private ListView enroll_pet_list;
    private ListView sharing_member_list;

    private ArrayAdapter<String> adapter_p;
    private ArrayAdapter<String> adapter_m;

    List<Object> Array_p = new ArrayList<Object>();
    List<Object> Array_m = new ArrayList<Object>();

    // MainActivity 에서 가져온 lg_ID 라는 변수 이용 => 로그인한 ID 값을 이용
    String getUserID = ((MainActivity)MainActivity.context_main).lg_ID.getText().toString();

    // 파이어베이스 연동
    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    // User_pet.java 를 통해 데이터베이스 접근
    final DatabaseReference petDB = mDatabase.getReference("User_pet");
    // User.java 를 통해 데이터베이스 접근
    final DatabaseReference UserDB = mDatabase.getReference("User");
    final DatabaseReference GroupDB = mDatabase.getReference("Group");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_calendar);

        context_view = this;
        T_LeaderID = (EditText) findViewById(R.id.textLeaderID);

        //EditText에 리더 id
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


        //등록된 반려동물
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
                        // 리스트 출력. 자식이 있는 수만큼 반복
                        for(DataSnapshot ds : snapshot.getChildren()) {

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


        //함께 이용 중인 사람
        sharing_member_list = (ListView) findViewById(R.id.sharing_member_list);
        sharing_member_list.setAdapter(adapter_m);

        adapter_m = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        sharing_member_list.setAdapter(adapter_m);

        //(User -> User List -> 로그인한 ID -> Leader_ID) 의 값은 같은 그룹에 속한 사람
        // leader id를 받아 해당 아이디를 가진 사용자를 list에 띄움
        //그룹원일 때 그룹장의 아이디가 안 뜸...
        UserDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LeaderID = snapshot.child("User List").child(getUserID).child("Leader_ID").getValue().toString();
                GroupDB.child(LeaderID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // 리스트 출력.
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
                UserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String Invite = snapshot.child("User List").child(getUserID).child("Invite").getValue().toString();
                        // 유저가 리더-> LeaderSetting 으로 이동
                        if (Invite.equals("Leader")) {
                            Intent intent = new Intent(getApplicationContext(), LeaderSetting.class);
                            startActivity(intent);
                        }
                        // 유저가 멤버 -> MemberSetting 으로 이동
                        else{
                            Intent intent = new Intent(getApplicationContext(), MemberSetting.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }




}