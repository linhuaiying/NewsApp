package com.example.newsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newsapp.R;
import com.example.newsapp.bean.UsersNewsbean.UsersNews;
import com.example.newsapp.View.showPublishContentView.showPublishContentActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersNewsAdapter extends RecyclerView.Adapter<UsersNewsAdapter.UsersNewsViewHolder> {
    private List<UsersNews> usersNewsList;
    private Context context;

    public UsersNewsAdapter(List<UsersNews> usersNewsList) {
        this.usersNewsList = usersNewsList;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.context = recyclerView.getContext();
    }

    @NonNull
    @Override
    public UsersNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_news_item_layout, parent, false);
        UsersNewsViewHolder usersNewsViewHolder = new UsersNewsViewHolder(view);
        usersNewsViewHolder.userNewsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = usersNewsViewHolder.getAdapterPosition();
                showPublishContentActivity.actionStart(parent.getContext(), usersNewsList.get(position));
            }
        });
        return usersNewsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UsersNewsViewHolder holder, int position) {
        UsersNews usersNews = usersNewsList.get(position);
        holder.title.setText(usersNews.getTitle());
        holder.time.setText(usersNews.getDate());
        if(usersNews.getNickName() != null ) {
            holder.nickName.setText(usersNews.getNickName());
        } else {
            holder.nickName.setText(usersNews.getUserName());
        }
        if(position == usersNewsList.size() - 1) holder.divide.setVisibility(View.GONE);
        if(usersNews.getUserIcon() != null) Glide.with(context).load(usersNews.getUserIcon()).into(holder.circleImageView);
    }

    @Override
    public int getItemCount() {
        return usersNewsList.size();
    }

    static class UsersNewsViewHolder extends RecyclerView.ViewHolder{

        View userNewsView;
        TextView title;
        TextView time;
        TextView nickName;
        CircleImageView circleImageView;
        View divide;
        public UsersNewsViewHolder(@NonNull View itemView) {
            super(itemView);
            userNewsView = itemView;
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
            divide = itemView.findViewById(R.id.divide);
            nickName = itemView.findViewById(R.id.nick_name);
            circleImageView = itemView.findViewById(R.id.user_icon);
        }

    }
}
