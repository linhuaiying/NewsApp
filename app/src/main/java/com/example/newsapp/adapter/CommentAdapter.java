package com.example.newsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newsapp.Application.MyApplication;
import com.example.newsapp.LocalUtils.SaveAccount;
import com.example.newsapp.R;
import com.example.newsapp.View.Activity.MainActivity;
import com.example.newsapp.View.HisView.HisActivity;
import com.example.newsapp.View.showPublishContentView.showPublishContentActivity;
import com.example.newsapp.bean.Commentbean.Comment;
import com.example.newsapp.bean.MyUserbean.MyUser;
import com.example.newsapp.bean.Userbean.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{
    private List<Comment> commentList;
    private Context context;
    public CommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.context = recyclerView.getContext();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item_layout, parent, false);
        CommentViewHolder commentViewHolder = new CommentViewHolder(view);
        return commentViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
         Comment comment = commentList.get(position);
         holder.commentText.setText(comment.getContent());
         if(comment.getNickName() != null) holder.nickName.setText(comment.getNickName());
         else holder.nickName.setText(comment.getUserName());
         String userName = SaveAccount.getUserInfo(MyApplication.getContext()).get("userName");
         holder.circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyUser myUser = new MyUser();
                myUser.setUserName(comment.getUserName());
                myUser.setNickName(comment.getNickName());
                if(!comment.getUserName().equals(userName)) HisActivity.actionStart(context, myUser);
                else {
                    User user = new User();
                    user.setUsername(userName);
                    MainActivity.actionStart(context, user, 2);
                }
            }
        });
         if(comment.getUserIcon() != null) Glide.with(context).load(comment.getUserIcon()).into(holder.circleImageView);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder{
        View commentView;
        TextView commentText;
        TextView nickName;
        CircleImageView circleImageView;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentView = itemView;
            commentText = itemView.findViewById(R.id.comment_text);
            nickName = itemView.findViewById(R.id.nick_name);
            circleImageView = itemView.findViewById(R.id.user_icon);
        }

    }
}
