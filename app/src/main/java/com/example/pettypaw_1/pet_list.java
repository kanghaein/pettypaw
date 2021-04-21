package com.example.pettypaw_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class pet_list extends AppCompatActivity {

    Button btn_enroll;
    String name;

    // MainActivity 에서 가져온 lg_ID 라는 변수 이용 => 로그인한 ID를 부모로 펫정보 입력
    String getUserID = ((MainActivity)MainActivity.context_main).lg_ID.getText().toString();

    // 리스트뷰와 리스트를 담기위한 배열을 객체 선언
    private ListView enrolled_pet_list;
    private ArrayAdapter<String> adapter;
    List<Object> Array = new ArrayList<Object>();

    // 파이어베이스 연동
    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    // User_pet.java 를 통해 데이터베이스 접근
    final DatabaseReference petDB = mDatabase.getReference("User_pet");

    // 다른 액티비티에서 접근 가능
    public static Context context_pet_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pet_list);

        // 다른 액티비티에서 접근 가능
        context_pet_list = this;

        btn_enroll = findViewById(R.id.btn_enroll);

        enrolled_pet_list = (ListView) findViewById(R.id.enrolled_pet_list);
        enrolled_pet_list.setAdapter(adapter);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        enrolled_pet_list.setAdapter(adapter);


        petDB.child(getUserID).child("Pet List").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // 자식이 있는 수만큼 반복
                for(DataSnapshot ds : snapshot.getChildren()) {

                    name = ds.getValue().toString();
                    Array.add(name);
                    adapter.add(name);

                }
                adapter.notifyDataSetChanged();
                enrolled_pet_list.setSelection(adapter.getCount() - 1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // 리스트 누르면 해당 동물 편집화면으로
        enrolled_pet_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                String list_name = Array.get(position).toString();
                Intent intent = new Intent(getApplicationContext(), PetEdit.class);
                intent.putExtra("ListNamePosition", list_name);
                startActivity(intent);

            }
        });

        // 추가등록 버튼
        btn_enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PetAdditionalEnroll.class);
                startActivity(intent);
                };
        });
    }

}