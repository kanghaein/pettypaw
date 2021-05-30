package com.example.pettypaw_1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.example.pettypaw_1.CheckDetail.context_CheckDetail;
import static com.example.pettypaw_1.ViewCalendar.context_view;

// 리사이클러뷰에 표시될 아이템 뷰를 생성하는 어댑터
public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {

    private ArrayList<Recycler_item> Data = null;

    // 파이어베이스 연동
    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    // User_pet.java 를 통해 데이터베이스 접근
    final DatabaseReference userDB = mDatabase.getReference("User");
    // User_event.java를 통해 데이터베이스 접근
    final DatabaseReference eventDB = mDatabase.getReference("User_event");
    // User_pet.java를 통해 데이터베이스 접근
    final DatabaseReference petDB = mDatabase.getReference("User_pet");

    // MainActivity에서 ID 문자열로 사용하는 lg_ID 가져온다.
    String getUserID = ((MainActivity) MainActivity.context_main).lg_ID.getText().toString();
    // CheckDetail에서 날짜 문자열로 사용하는 clickDay 가져온다.
    String getDay = ((CheckDetail) context_CheckDetail).clickDay;
    String LeaderID; // 그룹의 리더 아이디를 전달받을 변수


    // 아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, dogName;
        CheckBox feed_checked, walk_checked;
        ImageView colorimage;
        Button btn_delete;


        ViewHolder(View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조, 레이아웃 id 값들을 불러와 변수에 저장
            textView1 = itemView.findViewById(R.id.textView);
            feed_checked = itemView.findViewById(R.id.feed_checked);
            walk_checked = itemView.findViewById(R.id.walk_checked);
            dogName = itemView.findViewById(R.id.dogName);
            colorimage = itemView.findViewById(R.id.colorimage);
            btn_delete = itemView.findViewById(R.id.btn_delete);

        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받는다.
    RecycleAdapter(ArrayList<Recycler_item> list) {
        Data = list;
    }


    // 뷰홀더가 만들어지는 시점에 호출되는 메소드(각 아이템을 위한 뷰홀더 객체가 처음 만들어질때)
    // 각각 아이템 뷰홀더가 재사용될 수 있는 상태라면 호출되지 않는다.
    @Override
    public RecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recyclerview_item, parent, false);
        RecycleAdapter.ViewHolder vh = new RecycleAdapter.ViewHolder(view);

        return vh;
    }


    // 아이템 뷰의 xml레이아웃과 결합되는 경우 자동 호출
    // position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시한다.
    // 어떤 데이터를 아이템뷰에 표시할 것인지는 position값 참조
    @Override
    public void onBindViewHolder(RecycleAdapter.ViewHolder holder, int position) {
        Recycler_item item = Data.get(position);

        holder.dogName.setText(item.getPetName());
        holder.textView1.setText(item.getDetail());
        holder.colorimage.setImageDrawable(item.getIcon());

        // 아이템의 초기화 버튼 클릭 시 해당 아이템의 이벤트를 초기화하는 이벤트 수행
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 리더 아이디를 받아오기 위해 userDB에 접근
                userDB.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // 해당 그룹의 리더 아이디를 받아온다.
                        String LeaderID = snapshot.child("User List").child(getUserID).child("Leader_ID").getValue().toString();

                        // 이벤트가 등록되어 있는 eventDB로 접근한다.
                        eventDB.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                // 해당 이벤트 초기화 후
                                eventDB.child(LeaderID).child(item.getPetName()).child(getDay).setValue(null);
                                notifyDataSetChanged(); // 변경사항을 알려준다.
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
            }
        });

        // 밥 체크박스 클릭 이벤트
        holder.feed_checked.setOnCheckedChangeListener(null);
        holder.feed_checked.setChecked(item.getSelected_feed());
        // 사용자가 "밥" 체크박스를 체크 또는 해제할 때 반응하는 이벤트 처리
        holder.feed_checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                holder.feed_checked.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userDB.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                // 해당 그룹의 리더 아이디를 받아온다.
                                String LeaderID = snapshot.child("User List").child(getUserID).child("Leader_ID").getValue().toString();

                                eventDB.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        // 체크박스를 체크할 때
                                        if (holder.feed_checked.isChecked()) {
                                            // 기존에 등록된 상세 일정이 없을 때 상세일정의 값을 따로 설정 후
                                            if (!snapshot.child(LeaderID).child(item.getPetName()).child(getDay).child("Detail").exists()) {
                                                eventDB.child(LeaderID).child(item.getPetName()).child(getDay).child("Detail").setValue("등록된 일정 없음");
                                            }
                                            // "밥" 체크박스 체크
                                            eventDB.child(LeaderID).child(item.getPetName()).child(getDay).child("Feed").setValue("checked");

                                        }
                                        // 체크박스를 해제한다면
                                        else {
                                            // 기존에 등록된 "산책"일정이 없고, 상세 일정 또한 없을 때
                                            if (!snapshot.child(LeaderID).child(item.getPetName()).child(getDay).child("Walk").exists()
                                                    && snapshot.child(LeaderID).child(item.getPetName()).child(getDay).child("Detail").getValue().toString().equals("등록된 일정 없음")) {
                                                // 모든 일정 초기화
                                                eventDB.child(LeaderID).child(item.getPetName()).child(getDay).setValue(null);

                                            } else // 그렇지 않다면, "밥"일정만 초기화
                                                eventDB.child(LeaderID).child(item.getPetName()).child(getDay).child("Feed").setValue(null);
                                        }

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
                    }
                });
                item.setSelected_feed(isChecked);
            }
        });

        // 산책 체크박스 클릭 이벤트
        holder.walk_checked.setOnCheckedChangeListener(null);
        holder.walk_checked.setChecked(item.getSelected_walk());
        // 사용자가 "산책" 체크박스를 체크 또는 해제할 때 반응하는 이벤트 처리
        holder.walk_checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                holder.walk_checked.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userDB.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                // 해당 그룹의 리더 아이디를 받아온다.
                                String LeaderID = snapshot.child("User List").child(getUserID).child("Leader_ID").getValue().toString();
                                eventDB.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        // 체크박스를 체크할 때
                                        if (holder.walk_checked.isChecked()) {
                                            // 기존에 등록된 상세 일정이 없을 때 상세일정의 값을 따로 설정 후
                                            if (!snapshot.child(LeaderID).child(item.getPetName()).child(getDay).child("Detail").exists()) {
                                                eventDB.child(LeaderID).child(item.getPetName()).child(getDay).child("Detail").setValue("등록된 일정 없음");
                                            }
                                            // "산책" 체크박스 체크
                                            eventDB.child(LeaderID).child(item.getPetName()).child(getDay).child("Walk").setValue("checked");

                                        }
                                        // 체크박스를 해제한다면
                                        else {
                                            // 기존에 등록된 "밥"일정이 없고, 상세 일정 또한 없을 때
                                            if (!snapshot.child(LeaderID).child(item.getPetName()).child(getDay).child("Feed").exists()
                                                    && snapshot.child(LeaderID).child(item.getPetName()).child(getDay).child("Detail").getValue().toString().equals("등록된 일정 없음")) {
                                                // 모든 일정 초기화
                                                eventDB.child(LeaderID).child(item.getPetName()).child(getDay).setValue(null);
                                            } else // 그렇지 않다면, "산책"일정만 초기화
                                                eventDB.child(LeaderID).child(item.getPetName()).child(getDay).child("Walk").setValue(null);
                                        }

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
                    }
                });
                item.setSelected_walk(isChecked);
            }
        });

    }

    // 어댑터에서 관리하는 전체 아이템의 개수를 반환
    @Override
    public int getItemCount() {
        return Data.size();
    }

}

