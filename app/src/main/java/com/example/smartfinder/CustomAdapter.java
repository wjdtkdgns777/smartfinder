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

    private ArrayList<Dictionary> mList;
    public String url2;
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected TextView phone;
        protected TextView category;
        protected TextView url;



        public CustomViewHolder(View view) {
            super(view);


            this.name = (TextView) view.findViewById(R.id.textView1);
            this.phone = (TextView) view.findViewById(R.id.textView2);
            this.category = (TextView) view.findViewById(R.id.textView3);
            this.url = (TextView) view.findViewById(R.id.textView4);


        }
    }


    public CustomAdapter(ArrayList<Dictionary> list) {
        this.mList = list;
    }



    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }




    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        viewholder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        viewholder.phone.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        viewholder.category.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        viewholder.url.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);

        viewholder.name.setGravity(Gravity.CENTER);
        viewholder.phone.setGravity(Gravity.CENTER);
        viewholder.category.setGravity(Gravity.CENTER);
        viewholder.url.setGravity(Gravity.CENTER);


        viewholder.name.setText(mList.get(position).getName2());
        viewholder.phone.setText(mList.get(position).getPhone2());
        viewholder.category.setText(mList.get(position).getCategory2());



        viewholder.url.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mList.get(position).getUrl2()));
                v.getContext().startActivity(mIntent);

                Log.d("2", mList.get(position).getUrl2());

            }
        });


    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }






}
