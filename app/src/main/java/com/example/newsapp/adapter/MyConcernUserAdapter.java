package com.example.newsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newsapp.R;
import com.example.newsapp.View.HisView.HisActivity;
import com.example.newsapp.bean.MyUserbean.MyUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyConcernUserAdapter extends RecyclerView.Adapter<MyConcernUserAdapter.ConcernUserViewHolder> {
    List<MyUser> myUserList;
    Context context;
    public MyConcernUserAdapter(List<MyUser> myUserList) {
        this.myUserList = myUserList;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.context = recyclerView.getContext();
    }

    @NonNull
    @Override
    public MyConcernUserAdapter.ConcernUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_user_item_layout, parent, false);
        MyConcernUserAdapter.ConcernUserViewHolder concernUserViewHolder = new MyConcernUserAdapter.ConcernUserViewHolder(view);
        concernUserViewHolder.searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = concernUserViewHolder.getAdapterPosition();
                HisActivity.actionStart(context, myUserList.get(position));
            }
        });
        return concernUserViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyConcernUserAdapter.ConcernUserViewHolder holder, int position) {
        MyUser myUser = myUserList.get(position);
        if(myUser.getNickName() != null) holder.nickName.setText(myUser.getNickName());
        else holder.nickName.setText(myUser.getUserName());
        if(myUser.getSign() != null && !myUser.getSign().equals("")) holder.sign.setText(myUser.getSign());
        if(myUser.getUserIcon() != null) Glide.with(context).load(myUser.getUserIcon()).into(holder.circleImageView);
        holder.concernBtn.setText("已关注");
    }

    @Override
    public int getItemCount() {
        return myUserList.size();
    }

    static class ConcernUserViewHolder extends RecyclerView.ViewHolder{
        View searchView;
        TextView nickName;
        CircleImageView circleImageView;
        TextView sign;
        Button concernBtn;
        public ConcernUserViewHolder(@NonNull View itemView) {
            super(itemView);
            searchView = itemView;
            nickName = itemView.findViewById(R.id.nick_name);
            circleImageView = itemView.findViewById(R.id.user_icon);
            sign = itemView.findViewById(R.id.sign);
            concernBtn = itemView.findViewById(R.id.concern_btn);
        }
    }
}
