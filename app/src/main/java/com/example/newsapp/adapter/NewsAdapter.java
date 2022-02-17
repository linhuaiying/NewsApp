package com.example.newsapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newsapp.Application.MyApplication;
import com.example.newsapp.R;
import com.example.newsapp.View.Activity.NewsContentActivity;
import com.example.newsapp.bean.Newsbean.News;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

   private List<News> newsList;

   public NewsAdapter(List<News> newsList) {
       this.newsList = newsList;
   }
    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        NewsViewHolder newsViewHolder = new NewsViewHolder(view);
        newsViewHolder.newsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = newsViewHolder.getAdapterPosition();
                String url = newsList.get(position).getUrl();
                NewsContentActivity.Companion.actionStart(parent.getContext(), url);
            }
        });
        return newsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
         News news = newsList.get(position);
         holder.title.setText(news.getTitle());
         holder.time.setText(news.getDate());
         Glide.with(MyApplication.getContext()).load(news.getThumbnail_pic_s()).into(holder.imageView);
    }


    @Override
    public int getItemCount() {
        return newsList.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder{

        View newsView;
        TextView title;
        TextView time;
        ImageView imageView;
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsView = itemView;
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
            imageView = itemView.findViewById(R.id.iv);
        }

    }
}
