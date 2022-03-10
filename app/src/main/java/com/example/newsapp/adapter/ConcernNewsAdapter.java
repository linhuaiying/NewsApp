package com.example.newsapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.R;
import com.example.newsapp.bean.ConcernNewsbean.ConcernNews;
import com.example.newsapp.View.Activity.showPublishContentActivity;

import java.util.List;

public class ConcernNewsAdapter extends RecyclerView.Adapter<ConcernNewsAdapter.ConcernNewsViewHolder> {
    private List<ConcernNews> concernNewsList;

    public ConcernNewsAdapter(List<ConcernNews> concernNewsList) {
        this.concernNewsList = concernNewsList;
    }
    @NonNull
    @Override
    public ConcernNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.concern_item_layout, parent, false);
        ConcernNewsViewHolder concernNewsViewHolder = new ConcernNewsViewHolder(view);
        concernNewsViewHolder.concernNewsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = concernNewsViewHolder.getAdapterPosition();
                String content = concernNewsList.get(position).getNewsContent();
                showPublishContentActivity.actionStart(parent.getContext(), content);
            }
        });
        return  concernNewsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ConcernNewsViewHolder holder, int position) {
        ConcernNews concernNews = concernNewsList.get(position);
        holder.title.setText(concernNews.getTitle());
        holder.time.setText(concernNews.getDate());
        if(position == concernNewsList.size() - 1) holder.divide.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return concernNewsList.size();
    }

    static class ConcernNewsViewHolder extends RecyclerView.ViewHolder{

        View concernNewsView;
        TextView title;
        TextView time;
        View divide;
        public ConcernNewsViewHolder(@NonNull View itemView) {
            super(itemView);
            concernNewsView = itemView;
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
            divide = itemView.findViewById(R.id.divide);
        }

    }
}
