package com.teddyhendryanto.instagrem;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by teddyhendryanto on 2017-09-13.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> implements View.OnClickListener {
    List<ParseObject> parseObjectList;
    Context context;

    public MyRecyclerViewAdapter(List<ParseObject> parseObjectList, Context context) {
        this.parseObjectList = parseObjectList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, null);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ParseObject obj = parseObjectList.get(position);
        if(obj.getParseFile("image").getUrl() != ""){
            Picasso.with(context).load(obj.getParseFile("image").getUrl()).into(holder.image_view);
        }

        holder.txt_datetime.setText(obj.getCreatedAt().toString());
        holder.txt_caption.setText(obj.getString("caption"));

    }

    @Override
    public int getItemCount() {
        return parseObjectList.size();
    }

    @Override
    public void onClick(View view) {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image_view;
        TextView txt_datetime;
        TextView txt_caption;

        public MyViewHolder(View itemView) {
            super(itemView);

            image_view = (ImageView) itemView.findViewById(R.id.image_view);
            txt_datetime = (TextView) itemView.findViewById(R.id.txt_datetime);
            txt_caption = (TextView) itemView.findViewById(R.id.txt_caption);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
