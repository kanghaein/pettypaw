package com.example.pettypaw_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GroupManagement extends AppCompatActivity {

    Button btn_search, btn_exit;
    SearchView searchView;
    String user_invite;

    // 리스트뷰와 리스트를 담기위한 배열을 객체 선언
    private ListView search_list;
    private ArrayAdapter<String> adapter;
    List<Object> Array = new ArrayList<Object>();

    String getUserID = ((MainActivity)MainActivity.context_main).lg_ID.getText().toString();

    // 파이어베이스 연동
    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    final FirebaseDatabase mDatabase1 = FirebaseDatabase.getInstance();
    // User.java 와 User_list 를 통해 데이터베이스 접근
    final DatabaseReference UserDB = mDatabase.getReference("User");
    final DatabaseReference UserListDB = mDatabase1.getReference("User_list");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_management);

        search_list = (ListView) findViewById(R.id.search_list);
        search_list.setAdapter(adapter);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        search_list.setAdapter(adapter);

        // 서치뷰(검색창) 이벤트
        searchView = findViewById(R.id.search_member);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            public boolean onQueryTextSubmit(String query) {

                UserDB.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        // 서치뷰의 검색창에 입력한 query 값이 UserDB 상에 존재하지 않는다면
                        if(!snapshot.child(query).exists()){
                            Toast.makeText(GroupManagement.this, "존재하지 않는 유저입니다", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            // 서치뷰의 검색창에 입력한 query 값을 리스트에 출력
                            Array.add(query);
                            adapter.add(query);
                        }

                        adapter.notifyDataSetChanged();
                        search_list.setSelection(adapter.getCount() - 1);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                return true;
            }

            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        // 리스트 누르면 팝업창 출력
        search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                AlertDialog.Builder ad = new AlertDialog.Builder(GroupManagement.this);
                ad.setTitle("초대");
                ad.setMessage("해당 사용자를 초대하시겠습니까?");

                // 확인버튼
                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        UserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                // 초대하고자 하는 사람의 Invite 라는 노드의 값을 얻어와 문자열로 저장
                                user_invite = snapshot.child("User List").child(Array.get(position).toString()).child("Invite").getValue().toString();

                                // 초대하고자 하는 사람이 이미 다른 그룹에 속해있다면
                                if(user_invite.equals("invited")){
                                    Toast.makeText(GroupManagement.this, "이미 그룹에 속한 회원입니다", Toast.LENGTH_SHORT).show();
                                }
                                // 초대하고자 하는 사람이 다른 그룹의 리더라면
                                else if(user_invite.equals("Leader")){
                                    Toast.makeText(GroupManagement.this, "이미 그룹에 속한 회원입니다", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    // (User -> User List -> 선택된 리스트의 유저 ID) 에게 자식으로 invited 라는 값과 초대한 자신의 ID 값을 준다
                                    User_list user_list = new User_list("invited", getUserID);
                                    UserDB.child("User List").child(Array.get(position).toString()).setValue(user_list);
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });

                // 취소버튼
                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
            }
        });

        // 검색버튼
        btn_search = findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setQuery(searchView.getQuery(), true);
            }
        });

        // 나가기 버튼
        btn_exit = findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LeaderSetting.class);
                startActivity(intent);
            }
        });
    }
}