package com.example.smartfinder;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
    //찜리스트의 요소들을 위한 커스텀 어댑터

    private ArrayList<Dictionary> mList;
    public String url2;
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;//내 찜리스트에 저장된 식당의 이름
        protected TextView phone;//전화번호
        protected TextView category;//카테고리
        protected Button url;//바로가기를 할수있는 버튼



        public CustomViewHolder(View view) {
            super(view);

            this.name = (TextView) view.findViewById(R.id.textView1);
            this.phone = (TextView) view.findViewById(R.id.textView2);
            this.category = (TextView) view.findViewById(R.id.textView3);
            this.url = (Button) view.findViewById(R.id.button4);
            //각자 R에 정의된 보이는 부분들 연결

        }
    }


    public CustomAdapter(ArrayList<Dictionary> list) {
        this.mList = list;
    }



    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item, viewGroup, false);//item xml을 뷰 객체로 만들기

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }




    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        viewholder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);//찜리스트 글씨들의 크기설정
        viewholder.phone.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        viewholder.category.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);


        viewholder.name.setGravity(Gravity.CENTER);//중간에 있도록 설정
        viewholder.phone.setGravity(Gravity.CENTER);
        viewholder.category.setGravity(Gravity.CENTER);



        viewholder.name.setText(mList.get(position).getName2());//텍스트 설정
        viewholder.phone.setText(mList.get(position).getPhone2());
        viewholder.category.setText(mList.get(position).getCategory2());




        viewholder.url.setOnClickListener(new View.OnClickListener()
        {//찜리스트에서 바로가기 버튼 눌렀을때
            @Override
            public void onClick(View v)
            {
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mList.get(position).getUrl2()));
                v.getContext().startActivity(mIntent);//그 요소의 다음플레이스 url을 이용해 연결



            }
        });


    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }






}
