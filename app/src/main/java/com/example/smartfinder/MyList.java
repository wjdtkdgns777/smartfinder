package com.example.smartfinder;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyList extends AppCompatActivity{//내 찜리스트 보여주는 클래스
    private ArrayList<Dictionary> mArrayList;//딕셔너리라는 클래스 리스트 형식 이용해 내부 정보 얻을것임
    private CustomAdapter mAdapter;//커스텀 어댑터 사용


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_main);

        FirebaseFirestore db = FirebaseFirestore.getInstance();//파이어 베이스에서 찜리스트 받아옴

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);//리사이클러 뷰를 만들어 리스트 개수에 맞게 보여줄수 있도록 함
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);//리니어 레이아웃으로 설정


        mArrayList = new ArrayList<>();//리스트를 만듬

        mAdapter = new CustomAdapter(mArrayList);//딕셔너리 클래스에 이용되는 커스텀 어댑터로 m어뎁터 만듬
        mRecyclerView.setAdapter(mAdapter);//리사이클러뷰의 어댑터로 설정함

        //파이어 베이스에서 정보를 얻어와 리사이클러 뷰에 추가하는 과점
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        db.collection("users")//users 라는 컬렉션에서 정보 받아옴
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                            //파이어베이스에서 받아온 정보들의 개수만큼 Setlist
                            Setlist(document.getString("Name"), document.getString("Phone"), document.getString("Category"), document.getString("URL"),document.getId());

                            }

                            }
                         else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                        }
                    }
                });


    }


    protected void Setlist(String name,String phone, String category,String url,String doc) {
    Dictionary data = new Dictionary(name, phone, category, url,doc);
        //파이어베이스에서 받아온 정보를 딕셔너리 형태로 data란 이름으로 저장

        mArrayList.add(data);//리스트에 데이터 넣기
        mAdapter.notifyDataSetChanged();

    }


}


