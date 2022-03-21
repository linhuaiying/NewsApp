package com.example.newsapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.R;
import com.example.newsapp.bean.Commentbean.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{
    private List<Comment> commentList;
    public CommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;
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
         holder.nickName.setText(comment.getNickName());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder{
        View commentView;
        TextView commentText;
        TextView nickName;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentView = itemView;
            commentText = itemView.findViewById(R.id.comment_text);
            nickName = itemView.findViewById(R.id.nick_name);
        }

    }
}
